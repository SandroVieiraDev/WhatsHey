package com.env.whatshey.ui.view.binding;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.env.whatshey.R;
import com.env.whatshey.base.BaseViewBinding;

public class HomeFragmentViewBinding extends BaseViewBinding {
    public EditText editText;
    public LinearLayout linearSend, linearDate;
    public TextView textDate;
    public RecyclerView recyclerView;


    public HomeFragmentViewBinding(View viewRoot) {
        super(viewRoot);
    }

    @Override
    protected void initViews() {
        editText = find(R.id.editText);
        linearSend = find(R.id.linearSend);
        linearDate = find(R.id.linearDate);
        textDate = find(R.id.textDate);
        recyclerView = find(R.id.recylerView);
    }
}
