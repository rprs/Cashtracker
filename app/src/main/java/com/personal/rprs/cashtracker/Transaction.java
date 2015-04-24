package com.personal.rprs.cashtracker;

import java.util.Date;

/**
 * Container for each transaction.
 */
public class Transaction {

    public double amount;
    public String description;
    public Date date;
    public boolean cc;
    public boolean debtWithRoomie;

    public Transaction(
            double inAmount,
            String inDescription,
            Date inDate,
            boolean inCc,
            boolean inDebtWithRoomie) {
        amount = (inAmount < 0) ? 0 : inAmount;
        description = (inDescription == null) ? "" : inDescription;
        date = (inDate == null) ? new Date() : inDate;
        cc = inCc;
        debtWithRoomie = inDebtWithRoomie;
    }
}
