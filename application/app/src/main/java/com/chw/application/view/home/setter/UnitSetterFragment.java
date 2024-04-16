package com.chw.application.view.home.setter;

import android.os.Bundle;
import android.view.View;
import com.chw.application.databinding.FragmentSetterUnitBinding;
import com.chw.application.view.BaseFragment;

public class UnitSetterFragment extends BaseFragment<FragmentSetterUnitBinding> {

    public static UnitSetterFragment newInstance() {
        Bundle args = new Bundle();
        UnitSetterFragment fragment = new UnitSetterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FragmentSetterUnitBinding viewBinding() {
        return FragmentSetterUnitBinding.inflate(getLayoutInflater());
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
