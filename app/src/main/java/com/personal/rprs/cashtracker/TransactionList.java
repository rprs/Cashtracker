package com.personal.rprs.cashtracker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class TransactionList extends ActionBarActivity {

    static final int ADD_NEW_REQUEST = 1;
    private ArrayList<Transaction> list;
    private TransactionListAdapter adapter;
    private String CASHTRACKER_FILENAME = "cashtracker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);


        final ListView listview = (ListView) findViewById(R.id.transactionlist);
        list = this.restoreOrCreateList();
        this.sortTransactionList();

        adapter = new TransactionListAdapter(this, list);
        listview.setAdapter(adapter);

    }

    private ArrayList<Transaction> restoreOrCreateList() {
        ArrayList<Transaction> result = new ArrayList<>();
        File file = new File(this.getFilesDir(), CASHTRACKER_FILENAME);
        FileInputStream fileIStream;
        ObjectInputStream objectIStream;
        if (file.exists()) {
            Log.d("CASHTRACKER", "file Exists");
            try {
                fileIStream = new FileInputStream(file);
                objectIStream = new ObjectInputStream(fileIStream);
                // no idea to remove compile warning.
                result = (ArrayList<Transaction>) objectIStream.readObject();
                Log.d("CASHTRACKER", String.format("Deserialized Data. Items on list: %d",
                        result.size()));
                objectIStream.close();
                fileIStream.close();
            } catch (ClassNotFoundException e) {
                Log.w("CASHTRACKER", "Class not found", e);
            } catch (IOException e) {
                Log.w("CASHTRACKER", "IOException", e);
            }
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transaction_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            Intent addNew = new Intent(this, AddTransaction.class);
            startActivityForResult(addNew, TransactionList.ADD_NEW_REQUEST);
            return true;
        } else if (id == R.id.action_share) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "##gastos##");
            emailIntent.putExtra(Intent.EXTRA_TEXT, this.buildEmailBodyFromList());
            startActivity(Intent.createChooser(emailIntent, "Select email app"));
            return true;
        } else if (id == R.id.action_delete) {
            Log.d("CASH_TRACKER", "deleting list.");
            list.clear();
            adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    private String buildEmailBodyFromList() {
        StringBuilder body = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
        for (Transaction t : list) {
            /**
             * Each row has the following structure in my file:
             * - Amount
             * - Date
             * - Category
             * - Group
             * - Store
             * - Description
             * - cc
             * - Month
             * - Year
             * - Debt with roomie
             */

            body.append(String.format("%1$,.2f;", t.amount));
            body.append(String.format("%s;", sdf.format(t.date)));
            body.append(" ; ; ;"); // Category; Group; Store;
            body.append(String.format("%s;", t.description));
            body.append(String.format("%d;", t.cc ? 1 : 0));
            body.append(" ; ;"); // Month, Year
            body.append(String.format("%d", t.debtWithRoomie ? 1 : 0));
            body.append("\n");
        }
        return body.toString();
    }


    /**
     * Function called when another activity returns a value.
     * Currently used to create transaction based on information
     * from AddTransaction.
     * @param requestCode
     *          Code used to identify what action is returning.
     * @param resultCode
     *          What was the state (cancelled, completed).
     * @param data
     *          Information returned by the activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == TransactionList.ADD_NEW_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                double amount = extras.getDouble(getString(R.string.intent_amout_key));
                String description = extras.getString(getString(R.string.intent_description_key));
                boolean cc = extras.getBoolean(getString(R.string.intent_cc_key));
                boolean roomie = extras.getBoolean(getString(R.string.intent_roomie_key));
                Date date = new Date();
                date.setTime(extras.getLong(getString(R.string.intent_date_key)));
                list.add(new Transaction(amount, description, date, cc, roomie));
                this.sortTransactionList();
                adapter.notifyDataSetChanged();

            }
        }
    }

    private void sortTransactionList() {
        Collections.sort(list, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction lhs, Transaction rhs) {
                if (lhs.date.getTime() < rhs.date.getTime())
                    return 1;
                else if (lhs.date.getTime() == rhs.date.getTime())
                    return 0;
                else
                    return -1;
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        FileOutputStream fileOStream;
        ObjectOutputStream objectOStream;
        try {
            fileOStream = openFileOutput(CASHTRACKER_FILENAME, Context.MODE_PRIVATE);
            objectOStream = new ObjectOutputStream(fileOStream);
            objectOStream.writeObject(list);
            Log.d("CASH_TRACKER", "Saved file");
            objectOStream.close();
            fileOStream.close();
        } catch (IOException e) {
            Log.e("CASH_TRACKER", "IOException: Failed to save list", e);
        } catch (Exception e) {
            Log.e("CASH_TRACKER", "Exception: Failed to save List", e);
        }
    }
}
