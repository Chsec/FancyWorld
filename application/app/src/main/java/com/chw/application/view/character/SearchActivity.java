package com.chw.application.view.character;

import android.os.Bundle;
import android.view.View;
import com.chw.application.databinding.ActivitySearchBinding;
import com.chw.application.view.BaseActivity;
import com.xuexiang.xui.widget.searchview.MaterialSearchView;

public class SearchActivity extends BaseActivity<ActivitySearchBinding> {

    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ActivitySearchBinding viewBinding() {
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        return binding;
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

    public class SearchListener implements MaterialSearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }
}