package com.chw.application.viewmodel.home.setter;

import com.chw.application.App;
import com.chw.application.R;
import com.chw.application.viewmodel.BaseVM;

import java.util.ArrayList;
import java.util.Arrays;

public class SetterVM extends BaseVM {

    public final ArrayList<String> setters = new ArrayList<>();

    public int SELECTED_SETTER_INDEX = 0;

    public SetterVM() {
        setters.addAll(Arrays.asList(App.getInstance().getResources().getStringArray(R.array.setter)));
    }

}
