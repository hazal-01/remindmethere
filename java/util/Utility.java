package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {


    private static final String TAG = "Utility";

    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String currentDateTime = dateFormat.format(new Date());

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

}