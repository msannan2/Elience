package com.HCI.elience.utils;

import java.util.ArrayList;

public class CaseInsensitiveList extends ArrayList<String> {
    @Override
    public boolean contains(Object obj) {
        String object = (String)obj;
        for (String string : this) {
            if (object.equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }
}