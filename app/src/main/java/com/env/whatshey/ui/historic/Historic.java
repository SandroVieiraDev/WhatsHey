package com.env.whatshey.ui.historic;

import android.view.Gravity;

import com.env.whatshey.R;

import java.util.Objects;

public class Historic {

    private ItemType type;
    private long timestamp;
    private String whatsNumber;
    private boolean itemSelected;
    private boolean dateVisible;
    private int adapterPosition;

    public Historic() {
    }

    public Historic(long timestamp, String whatsNumber) {
        this.timestamp = timestamp;
        this.whatsNumber = whatsNumber;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getWhatsNumber() {
        return whatsNumber;
    }

    public boolean isItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(boolean itemSelected) {
        this.itemSelected = itemSelected;
    }

    public boolean isDateVisible() {
        return dateVisible;
    }

    public void setDateVisible(boolean dateVisible) {
        this.dateVisible = dateVisible;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    public int getBackgroundBubble() {
        if (type.equals(ItemType.TYPE_CHAT_NUMBER_LEFT)) {
            return R.drawable.bg_historic_item_left;
        }
        return R.drawable.bg_historic_item_right;
    }

    public int getGravity() {
        if (type.equals(ItemType.TYPE_CHAT_NUMBER_LEFT)) {
            return Gravity.START;
        }
        return Gravity.END;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Historic historic = (Historic) o;
        return timestamp == historic.timestamp && Objects.equals(whatsNumber, historic.whatsNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, whatsNumber);
    }
}
