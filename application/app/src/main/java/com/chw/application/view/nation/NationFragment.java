package com.chw.application.view.nation;

import android.os.Bundle;
import android.view.View;
import com.chw.application.databinding.FragmentMainOrgBinding;
import com.chw.application.view.BaseFragment;

public class NationFragment extends BaseFragment<FragmentMainOrgBinding> {

    public static NationFragment newInstance() {
        Bundle args = new Bundle();
        NationFragment fragment = new NationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FragmentMainOrgBinding viewBinding() {
        return FragmentMainOrgBinding.inflate(getLayoutInflater());
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