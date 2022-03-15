package com.env.whatshey.ui.view.binding;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.env.whatshey.R;
import com.env.whatshey.base.BaseViewBinding;

public class HomeFragmentViewBinding extends BaseViewBinding {
    public EditText editText;
    public LinearLayout linearSend;
    public TextView textDate;

    public HomeFragmentViewBinding(View viewRoot) {
        super(viewRoot);
    }

    @Override
    protected void initViews() {
        editText = find(R.id.editText);
        linearSend = find(R.id.linearSend);
        textDate = find(R.id.textDate);
    }
}
