package com.env.whatshey.base;

import android.view.View;

public abstract class BaseViewBinding {
    private View viewRoot;

    public BaseViewBinding(View viewRoot) {
        this.viewRoot = viewRoot;
        initViews();
    }

    protected abstract void initViews();

    protected <V extends View> V find(int id){
        return viewRoot.findViewById(id);
    }

    public View getViewRoot() {
        return viewRoot;
    }
}
