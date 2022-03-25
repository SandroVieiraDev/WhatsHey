package com.env.whatshey.ui.splash;

import android.os.Handler;

import com.env.whatshey.R;
import com.env.whatshey.databinding.FragmentSplashBinding;
import com.env.whatshey.ui.historic.HistoricFragment;

import br.kleberf65.androidutils.base.BaseBindingFragment;

public class SplashFragment extends BaseBindingFragment<FragmentSplashBinding> {

    @Override
    public int getLayout() {
        return R.layout.fragment_splash;
    }

    @Override
    public void initializeUi() {
        hideLoadingContentView();

        new Handler().postDelayed(() -> getParentFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new HistoricFragment()).commit(), 2000);
    }
}