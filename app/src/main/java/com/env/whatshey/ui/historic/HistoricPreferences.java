package com.env.whatshey.ui.historic;

import static com.env.whatshey.ui.historic.ItemType.TYPE_CHAT_NUMBER_LEFT;
import static com.env.whatshey.ui.historic.ItemType.TYPE_CHAT_NUMBER_RIGHT;
import static com.env.whatshey.utilities.AppUtils.openChatWhatsapp;
import static com.env.whatshey.utilities.FormatDateUtils.getDateMessage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HistoricPreferences {

    private final SharedPreferences.Editor editor;
    private final Gson gson = new GsonBuilder().create();
    private final HistoricListener historicListener;
    private final String HISTORIC_KEY = "historic.key2";
    private final List<Historic> offlineHistoric;
    private final Context context;

    public HistoricPreferences(Context context, HistoricListener historicListener) {
        SharedPreferences preferences = context.getSharedPreferences("historic.preferences2", Context.MODE_PRIVATE);
        this.historicListener = historicListener;
        this.editor = preferences.edit();
        this.context = context;
        this.offlineHistoric = new ArrayList<>(gson.fromJson(preferences.getString(HISTORIC_KEY, "[]"),
                new TypeToken<Set<Historic>>() {
                }.getType()));
    }

    public void addToHistoric(String number) {
        if (number.length() < 14) {
            historicListener.onAddedError();
        } else {
            long timeMillis = System.currentTimeMillis();
            Historic historic = new Historic(System.currentTimeMillis(), number);
            Object[] params = makeHistoricParams(timeMillis);
            historic.setDateVisible((Boolean) params[0]);
            historic.setType((ItemType) params[1]);
            offlineHistoric.add(historic);
            editor.putString(HISTORIC_KEY, gson.toJson(offlineHistoric)).apply();
            historicListener.onAddedHistoric(historic);
            openChatWhatsapp(context, number);
        }
    }

    public void saveHistoric(List<Historic> list) {
        editor.putString(HISTORIC_KEY, gson.toJson(list)).apply();
    }


    public List<Historic> loadHistoric() {
        //list for return formatted dates...
        List<Historic> formattedList = new ArrayList<>();
        List<String> sectionDates = new ArrayList<>();
        for (int i = 0; i < offlineHistoric.size(); i++) {
            Historic item = offlineHistoric.get(i);
            ItemType type = i % 2 != 0
                    ? (ItemType.TYPE_CHAT_NUMBER_LEFT)
                    : (TYPE_CHAT_NUMBER_RIGHT);
            item.setType(type);
            //Check date indicator...
            String dateMessage = getDateMessage(item.getTimestamp());
            if (sectionDates.size() == 0) {
                sectionDates.add(dateMessage);
                item.setDateVisible(true);
            } else {
                //Add date indicator in bubble chat inside.
                if (!sectionDates.contains(dateMessage)) {
                    sectionDates.add(dateMessage);
                    item.setDateVisible(true);
                }
            }
            formattedList.add(item);
        }
        return formattedList;
    }

    private Object[] makeHistoricParams(long timestamp) {
        if (offlineHistoric.size() == 0) {
            return new Object[]{true, TYPE_CHAT_NUMBER_RIGHT};
        }
        int length = offlineHistoric.size();
        for (int i = 0; i < length; i++) {
            if (getDateMessage(timestamp).equals(
                    getDateMessage(offlineHistoric.get(i).getTimestamp()))) {
                return new Object[]{false, getReverseItemType(offlineHistoric.get(length - 1).getType())};
            }
        }
        return new Object[]{true, getReverseItemType(offlineHistoric.get(length - 1).getType())};
    }

    private ItemType getReverseItemType(ItemType type) {
        return type.equals(TYPE_CHAT_NUMBER_RIGHT) ? TYPE_CHAT_NUMBER_LEFT : TYPE_CHAT_NUMBER_RIGHT;
    }

    public void clear() {
        offlineHistoric.clear();
        editor.putString(HISTORIC_KEY, "[]").apply();
    }

    public void deleteHistoric(Historic historic) {
        int position = offlineHistoric.indexOf(historic);
        offlineHistoric.remove(historic);
        editor.putString(HISTORIC_KEY, gson.toJson(offlineHistoric)).apply();
        historicListener.onDeleteHistoric(position);
    }

    public interface HistoricListener {
        void onAddedHistoric(Historic historic);

        void onDeleteHistoric(int position);

        void onAddedError();
    }
}
