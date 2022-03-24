package com.env.whatshey.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.env.whatshey.model.Historic;
import com.env.whatshey.util.GsonToList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HistoricPreferences {

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;
    private final Gson gson = new GsonBuilder().create();

    private final String HISTORIC_KEY = "historic.key";

    public HistoricPreferences(Context context) {
        preferences = context.getSharedPreferences("historic.preferences", 0);
        editor = preferences.edit();
    }

    public void clear() {
        editor.clear().apply();
    }

    public void saveHistoric(List<Historic> historicList) {
        editor.putString(HISTORIC_KEY, GsonToList.historicToString(historicList)).apply();
    }

    public List<Historic> loadHistoric() {
        return new ArrayList<>(gson.fromJson(
                preferences.getString(HISTORIC_KEY, "[]"),
                new TypeToken<Set<Historic>>() {
                }.getType()));
    }

    public void addToHistoric(Historic historic) {
        List<Historic> historicList = loadHistoric();
        historicList.add(historic);
        editor.putString(HISTORIC_KEY, gson.toJson(historicList)).apply();
    }
}
