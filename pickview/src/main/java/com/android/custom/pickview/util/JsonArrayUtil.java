package com.android.custom.pickview.util;

import com.google.gson.Gson;

public class JsonArrayUtil {
    public static String toJson(Object obj) {
            Gson gson = new Gson();
        return gson.toJson(obj);
    }
}
