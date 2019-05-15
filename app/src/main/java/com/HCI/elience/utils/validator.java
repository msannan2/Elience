package com.HCI.elience.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*Class containing common validation functions*/

public class validator {
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isValidDate(String date) throws ParseException {
        return new SimpleDateFormat("dd-MM-yyyy").parse(date).before(new Date());
    }

}
