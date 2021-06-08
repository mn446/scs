package com.logic;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class Utils 
{
    // Methods --------------------------------------------------------------------------------------------------------//

    public static String ordinal(int i) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
        case 11:
        case 12:
        case 13:
            return i + "th";
        default:
            return i + suffixes[i % 10];

        }
    }

    public static void onlyDecimal(TextField textField) {

        DecimalFormat format = new DecimalFormat("#");

        final TextFormatter<Object> decimalTextFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().isEmpty()) {
                return change;
            }
            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(change.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < change.getControlNewText().length()) {
                return null;
            } else {
                return change;
            }
        });
        textField.setTextFormatter(decimalTextFormatter);
    }
}
