package com.personal.rprs.cashtracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;


public class AddTransaction extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
    }

    private void setError(EditText editText, int stringCode) {
        // Set error message on textEdit.
        editText.setError(getString(stringCode));

        // Request focus.
        editText.requestFocus();

        // Pops up the soft keyboard.
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void CollectNewTransactionData(View view) {
        // Verifying amount. not empty, only 2 decimals.
        EditText amountEditText = (EditText) findViewById(R.id.layout_add_amount);
        String amountString = amountEditText.getText().toString();
        int amountLenght = amountString.length();
        if (amountLenght == 0) {
            this.setError(amountEditText, R.string.error_add_transaction_empty_amount);
            return;
        }
        int indexOfDecimalPoint =  amountString.indexOf(".");
        if (indexOfDecimalPoint >=0 ) {
            if( (amountLenght - indexOfDecimalPoint) > 3)
            {
                this.setError(
                        amountEditText, R.string.error_add_transaction_amount_too_many_decimals);
                return;
            }
        }
        double amount = Double.parseDouble(amountString);

        // Verifying description is not empty.
        EditText descriptionEditText = (EditText) findViewById(R.id.layout_add_description);
        String descriptionString = descriptionEditText.getText().toString();
        if (descriptionString.length() == 0) {
            this.setError(descriptionEditText, R.string.error_add_transaction_empty_description);
            return;
        }

        DatePicker datePicker = (DatePicker) findViewById(R.id.layout_add_date);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        Date date = calendar.getTime();


        boolean cc = ((CheckBox) findViewById(R.id.layout_add_cc)).isChecked();
        boolean roomie = ((CheckBox) findViewById(R.id.layout_add_roomie_debt)).isChecked();

        // add data to Intent
        Intent intent = new Intent();

        intent.putExtra(getString(R.string.intent_amout_key), amount);
        intent.putExtra(getString(R.string.intent_description_key), descriptionString);
        intent.putExtra(getString(R.string.intent_cc_key), cc);
        intent.putExtra(getString(R.string.intent_roomie_key), roomie);
        intent.putExtra(getString(R.string.intent_date_key), date.getTime());

        // Return Intent
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
