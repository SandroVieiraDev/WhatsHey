package com.env.whatshey.ui.historic;

import com.env.whatshey.R;

public class Historic {

    public static final int TYPE_CHAT_LEFT = 0;
    public static final int TYPE_CHAT_RIGHT = 1;
    public static final int TYPE_DATE_INDICATOR = 2;

    private String id;
    private long timestamp;
    private int historicType;
    private String whatsNumber;
    private boolean itemSelected;

    public Historic(String id, long timestamp, int historicType, String whatsNumber) {
        this.id = id;
        this.timestamp = timestamp;
        this.historicType = historicType;
        this.whatsNumber = whatsNumber;
    }

    public Historic(long timestamp, int historicType) {
        this.timestamp = timestamp;
        this.historicType = historicType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getHistoricType() {
        return historicType;
    }

    public void setHistoricType(int historicType) {
        this.historicType = historicType;
    }

    public String getWhatsNumber() {
        return whatsNumber;
    }

    public void setWhatsNumber(String whatsNumber) {
        this.whatsNumber = whatsNumber;
    }

    public boolean isItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(boolean itemSelected) {
        this.itemSelected = itemSelected;
    }

    public int getBackground() {
        return itemSelected ? R.drawable.historic_item_selected : 0;
    }
}
