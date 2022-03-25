package com.env.whatshey.ui.main;

import com.env.whatshey.R;
import com.env.whatshey.databinding.ActivityMainBinding;
import com.env.whatshey.ui.splash.SplashFragment;

import br.kleberf65.androidutils.base.BaseBindingActivity;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {
    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initializeUi() {
        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new SplashFragment()).commit();
    }
}
