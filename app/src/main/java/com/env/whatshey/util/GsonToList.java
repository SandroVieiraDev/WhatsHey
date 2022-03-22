package com.env.whatshey.util;

import com.env.whatshey.model.Historic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class GsonToList {

    public static List<Historic> stringToHistoric(String s){
        return new Gson().fromJson(s,
                new TypeToken<List<Historic>>(){
                }.getType());
    }

    public static String historicToString(List<Historic> list) {
        return new Gson().toJson(list);
    }
}
