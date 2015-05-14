package com.personal.rprs.cashtracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

        adapter = new TransactionListAdapter(this, list);
        listview.setAdapter(adapter);

    }

    private ArrayList<Transaction> restoreOrCreateList() {
        ArrayList<Transaction> result = new ArrayList<Transaction>();
        File file = new File(this.getFilesDir(), CASHTRACKER_FILENAME);
        FileInputStream fileIStream;
        ObjectInputStream objectIStream;
        if (file.exists()) {
            Log.d("CASHTRACKER", "file Exists");
            try {
                fileIStream = new FileInputStream(file);
                objectIStream = new ObjectInputStream(fileIStream);
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
            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                adapter.notifyDataSetChanged();

            }
        }
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
            Log.d("CASHTRACKER", "Saved file");
            objectOStream.close();
            fileOStream.close();
        } catch (IOException e) {
            Log.e("CASH_TRACKER", "IOException: Failed to save list", e);
        } catch (Exception e) {
            Log.e("CASH_TRACKER", "Exception: Failed to save List", e);
        }
    }
}
