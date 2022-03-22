package com.env.whatshey.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.env.whatshey.model.Historic;
import com.env.whatshey.util.GsonToList;

import java.util.List;

public class HistoricPreferences {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private final String NAME_KEY = "historic.preferences";
    private final String HISTORIC_KEY = "historic.key";

    public HistoricPreferences(Context context) {
        preferences = context.getSharedPreferences(NAME_KEY, 0);
        editor = preferences.edit();
    }

    public void clear(){
        editor.clear().apply();
    }

    public void saveHistoric(List<Historic> historicList){
        editor.putString(HISTORIC_KEY, GsonToList.historicToString(historicList)).apply();
    }

    public List<Historic> loadHistoric(){
        return GsonToList.stringToHistoric(preferences.getString(HISTORIC_KEY, null));
    }
}
