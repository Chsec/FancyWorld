package com.chw.application.view.home.setter;

import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.chw.application.databinding.FragmentLevelSetterBinding;
import com.chw.application.viewmodel.home.setter.LevelSetterVM;
import com.chw.application.widget.fragment.ViewPaper2FragmentAdapter;
import com.chw.application.view.BaseFragment;
import com.chw.application.util.bus.StickLiveDataBus;

import java.util.ArrayList;

public class LevelSetterFragment extends BaseFragment<FragmentLevelSetterBinding> {

    private RecyclerView rvDotView;

    private ViewPager2 vpView;

    private ArrayList<Fragment> fragments;

    private LinearLayoutManager rvDotViewLM;

    private LevelAddTableFragment levelAddTableFragment;

    private LevelAddTreeFragment levelAddTreeFragment;

    /**
     * 等级设定器的ViewModel
     */
    private LevelSetterVM levelVM;

    private ViewPaper2FragmentAdapter vpAdapter;

    public static LevelSetterFragment newInstance() {
        Bundle args = new Bundle();
        LevelSetterFragment fragment = new LevelSetterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FragmentLevelSetterBinding viewBinding() {
        return FragmentLevelSetterBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initPrepareFirst() {
        rvDotView = viewBinding.levelSetterDot;
        rvDotViewLM = new LinearLayoutManager(requireContext());

        vpView = viewBinding.levelSetterPage;

        fragments = new ArrayList<>();

        levelAddTreeFragment = new LevelAddTreeFragment();
        levelAddTableFragment = new LevelAddTableFragment();

        levelVM = new ViewModelProvider(requireActivity()).get(LevelSetterVM.class);
    }

    @Override
    protected void initViewSecond() {

        rvDotViewLM.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvDotView.setLayoutManager(rvDotViewLM);

        fragments.add(LevelManagerFragment.newInstance());
        vpAdapter = new ViewPaper2FragmentAdapter(requireActivity().getSupportFragmentManager(), requireActivity().getLifecycle(), fragments);
        vpView.setAdapter(vpAdapter);

    }

    @Override
    protected void initListenerThird() {

    }

    @Override
    protected void initObserveFourth() {
        StickLiveDataBus.get()
                .getChannel("Level_New", String.class)
                .observe(getViewLifecycleOwner(), s -> {

                });
    }

    @Override
    public void onClick(View view) {

    }
}
