package com.personal.rprs.cashtracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Utility methods or ease of development.
 */
public class TestUtils {

    public static List<Transaction> CreateTransactions(int howMany) {
        double minX = 50.00;
        double maxX = 100.00;
        double randomAmount;
        Date today = new Date();
        Random rand = new Random();

        List<Transaction> result = new ArrayList<>();
        for (int i = 0; i < howMany; i++) {
            randomAmount = rand.nextDouble() * (maxX - minX) + minX;
            result.add(new Transaction(
                    randomAmount,
                    String.format("description... %d", i+1),
                    today,
                    (i % 2 == 0),
                    (i % 3 == 0)));
        }
        return result;
    }
}
