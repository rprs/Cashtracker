package com.personal.rprs.cashtracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;


public class AddTransaction extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
    }

    public void addTransaction(View view) {
        // Retrieve data from each widget.
        // String amount = findViewById(R.id.)

        // Validate the data.

        // add data to Intent
        Intent intent = new Intent();
        // intent.putExtra("result", );

        // Return Intent
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
