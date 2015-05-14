package com.personal.rprs.cashtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created on 4/22/15.
 * Class to create each row of the list layout.
 */
public class TransactionListAdapter extends ArrayAdapter<Transaction> {
    private final Context context;
    private final List<Transaction> list;

    static class ViewHolder {
        TextView description;
        TextView date;
        TextView amount;
    }

    public TransactionListAdapter(Context context, List<Transaction> list) {
        super(context, R.layout.activity_rowlayout, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Checking if layout can be recycled.
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.activity_rowlayout, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.description = (TextView) rowView.findViewById(R.id.transaction_row_description);
            holder.date = (TextView) rowView.findViewById(R.id.transaction_row_date);
            holder.amount = (TextView) rowView.findViewById(R.id.transaction_row_amount);
            rowView.setTag(holder);
        }

        // Filling data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        Transaction transaction = list.get(position);
        holder.description.setText(transaction.description);
        DateFormat dateFormat = DateFormat.getDateInstance();
        holder.date.setText(dateFormat.format(transaction.date));
        NumberFormat baseFormat = NumberFormat.getCurrencyInstance();
        holder.amount.setText(baseFormat.format(transaction.amount));
        return rowView;
    }
}
