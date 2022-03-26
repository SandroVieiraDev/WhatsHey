package com.env.whatshey.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrientationUtils extends RecyclerView.OnScrollListener {

    private final SharedPreferences prefs;
    private final LinearLayoutManager layoutManager;

    public OrientationUtils(Context context, LinearLayoutManager layoutManager) {
        this.prefs = context.getSharedPreferences("orientation.utils", Context.MODE_PRIVATE);
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        try {
            if (layoutManager != null) {
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (totalItemCount > lastVisibleItemPosition) {
                    prefs.edit().putBoolean("orientation.key", true).apply();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isLastVisibleItemPosition() {
        return prefs.getBoolean("orientation.key", false);
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
