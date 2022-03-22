package com.env.whatshey.data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.env.whatshey.helper.DateCustom;
import com.env.whatshey.helper.HistoricPreferences;
import com.env.whatshey.model.Historic;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.UUID;

public class ServiceHey extends Observable {
    private HistoricPreferences preferences;

    public void openChat(String phone){
        setChanged();
        if (phone.length() < 14){
            notifyObservers("Digite um número de telefone válido");
        } else {
            notifyObservers(new Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("whatsapp://send?phone=+55" + phone)));
        }
    }

    public void openChat(Context context, int position){
        setChanged();
        List<Historic> historicList = new ArrayList<>();
        setPreferences(context);
        if (preferences.loadHistoric() != null) {
            historicList = preferences.loadHistoric();
        }
        Historic historic = historicList.get(position);
        notifyObservers(new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("whatsapp://send?phone=+55" + historic.getNumber())));
    }

    public void shareNumber(Context context, int position){
        setChanged();
        List<Historic> historicList = new ArrayList<>();
        setPreferences(context);
        if (preferences.loadHistoric() != null) {
            historicList = preferences.loadHistoric();
        }
        Historic historic = historicList.get(position);
        notifyObservers(new Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, historic.getNumber()));
    }

    public void deleteHistoric(Context context, int position){
        setChanged();
        List<Historic> historicList = new ArrayList<>();
        setPreferences(context);
        if (preferences.loadHistoric() != null) {
            historicList = preferences.loadHistoric();
        }
        historicList.remove(position);
        preferences.saveHistoric(historicList);
        notifyObservers(historicList);
    }

    public void deleteAll(Context context){
        setChanged();
        List<Historic> historicList = new ArrayList<>();
        setPreferences(context);
        preferences.saveHistoric(historicList);
        notifyObservers(historicList);
    }

    public void addHistoric(Context context, String phone){
        setChanged();
        if (phone.length() == 14) {
            List<Historic> historicList = new ArrayList<>();
            setPreferences(context);
            Historic historic = new Historic.Builder()
                    .setId(UUID.randomUUID().toString())
                    .setNumber(phone)
                    .setTime(DateCustom.getCurrentTimeMillis())
                    .build();
            if (preferences.loadHistoric() != null) {
                historicList = preferences.loadHistoric();
            }

            historicList.add(historic);
            preferences.saveHistoric(historicList);

            notifyObservers(historicList);
        }
    }

    private void setPreferences(Context context){
        if(preferences == null){
            preferences = new HistoricPreferences(context);
        }
    }
}
