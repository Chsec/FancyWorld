package com.chw.application.view.character;

import android.os.Bundle;
import android.view.View;
import com.chw.application.databinding.ActivityCharDetailBinding;
import com.chw.application.view.BaseActivity;

public class CharDetailActivity extends BaseActivity<ActivityCharDetailBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ActivityCharDetailBinding viewBinding() {
        return ActivityCharDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initPrepareFirst() {

    }

    @Override
    protected void initViewSecond() {

    }

    @Override
    protected void initListenerThird() {

    }

    @Override
    protected void initObserveFourth() {

    }

    @Override
    public void onClick(View view) {

    }

}
