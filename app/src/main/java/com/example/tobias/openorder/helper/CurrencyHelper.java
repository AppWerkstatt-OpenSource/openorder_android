package com.example.tobias.openorder.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by tobias on 19.07.17.
 */

public class CurrencyHelper {
    public static String convertToEur(double eur) {
        return format(eur) + " €";
    }

    public static String format(double i)
    {
        DecimalFormat f = new DecimalFormat("#0.00");
        double toFormat = ((double)Math.round(i*100))/100;
        return f.format(toFormat);
    }
}
