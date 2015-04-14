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
        float minX = 50.00f;
        float maxX = 100.00f;
        float randomAmount;
        Date randomDate = new Date();
        Random rand = new Random();

        List<Transaction> result = new ArrayList<>();
        for (int i = 0; i < howMany; i++) {
            randomAmount = rand.nextFloat() * (maxX - minX) + minX;
            randomDate.setTime(rand.nextInt());
            result.add(new Transaction(
                    randomAmount,
                    String.format("description...%d", i),
                    randomDate,
                    (i % 2 == 0),
                    (i % 3 == 0)));
        }
        return result;
    }
}
