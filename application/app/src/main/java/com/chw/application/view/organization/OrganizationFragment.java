package com.chw.application.view.organization;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.chw.application.databinding.FragmentMainOrgBinding;
import com.chw.application.view.nation.NationFragment;
import org.jetbrains.annotations.NotNull;


public class OrganizationFragment extends Fragment {

    private FragmentMainOrgBinding binding;

    public static NationFragment newInstance() {
        NationFragment fragment = new NationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainOrgBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}