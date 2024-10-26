package com.fmsh.temperature.activity;

import androidx.viewbinding.ViewBinding;
import com.fmsh.temperature.databinding.ActivitySplashBinding;

/**
 * @author wuyajiang
 * @date 2021/1/22
 */
public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding binding;
    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected ActivitySplashBinding inflateBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }
}
