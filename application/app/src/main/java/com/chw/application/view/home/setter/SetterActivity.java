package com.chw.application.view.home.setter;

import android.view.LayoutInflater;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager2.widget.ViewPager2;
import com.chw.application.AppConfigCenter;
import com.chw.application.R;
import com.chw.application.databinding.ActivitySetterBinding;
import com.chw.application.databinding.ItemSetterBinding;
import com.chw.application.viewmodel.home.setter.LevelSetterVM;
import com.chw.application.viewmodel.home.setter.SetterVM;
import com.chw.application.widget.recyclerview.BindDataVBImpl;
import com.chw.application.widget.recyclerview.CommonAdapter;
import com.chw.application.widget.recyclerview.CommonViewHolder;
import com.chw.application.widget.fragment.ViewPaper2FragmentAdapter;
import com.chw.application.view.BaseActivity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SetterActivity extends BaseActivity<ActivitySetterBinding> {

    private RecyclerView rvView;

    private LinearLayoutManager rvLayoutManager;

    private ViewPager2 vpView;

    private ViewPaper2FragmentAdapter vpAdapter;

    private CommonAdapter<String> rvAdapter;

    private ArrayList<Fragment> fragments;

    /**
     * 设定器的ViewModel
     */
    private SetterVM setterVM;

    /**
     * 等级设定器的ViewModel
     */
    private LevelSetterVM levelVM;

    @Override
    protected ActivitySetterBinding viewBinding() {
        return ActivitySetterBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initPrepareFirst() {
        vpView = viewBinding.setterPage;
        rvView = viewBinding.setterGuide;
        rvLayoutManager = new LinearLayoutManager(this);

        fragments = new ArrayList<>();

        setterVM = new ViewModelProvider(this).get(SetterVM.class);
        levelVM = new ViewModelProvider(this).get(LevelSetterVM.class);
    }

    @Override
    protected void initViewSecond() {

        rvAdapter = new CommonAdapter<>(AppConfigCenter.setterList, new GuideDataBind());
        rvLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvView.setLayoutManager(rvLayoutManager);
        rvView.setAdapter(rvAdapter);

        fragments.add(LevelSetterFragment.newInstance());
        fragments.add(UnitSetterFragment.newInstance());

        vpAdapter = new ViewPaper2FragmentAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        vpView.setAdapter(vpAdapter);
        vpView.setUserInputEnabled(false);
    }

    @Override
    protected void initListenerThird() {

        rvAdapter.setItemListener(new CommonAdapter.ItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int viewId) {
                int position = rvView.getChildViewHolder(view).getAbsoluteAdapterPosition();
                if (position != setterVM.SELECTED_SETTER_INDEX) {
                    setterVM.SELECTED_SETTER_INDEX = position;
                    rvAdapter.refresh(1);
                }
            }

            @Override
            public void OnItemLongClickListener(View view, int viewId) {

            }
        });
    }

    @Override
    protected void initObserveFourth() {
    }

    @Override
    public void onClick(View view) {
    }

    private class GuideDataBind extends BindDataVBImpl<String> {

        @Override
        public void onBindViewHolder(CommonAdapter<String> adapter, CommonViewHolder viewHolder, String model, int type) {
            ItemSetterBinding bind = (ItemSetterBinding) viewHolder.binding;
            bind.setter.setText(model);
            if (setterVM.SELECTED_SETTER_INDEX == viewHolder.getAbsoluteAdapterPosition()) {
                bind.setter.setTextColor(ContextCompat.getColor(SetterActivity.this, R.color.red0));
            } else {
                bind.setter.setTextColor(ContextCompat.getColor(SetterActivity.this, R.color.black));
            }
        }

        @Override
        public ViewBinding getItemViewBinding(String model, int position) {
            return ItemSetterBinding.inflate(LayoutInflater.from(rvView.getContext()), rvView, false);
        }

        @Override
        public void onBindViewHolder(@NotNull CommonViewHolder holder, int position, @NotNull List<Object> payloads) {
            ItemSetterBinding bind = (ItemSetterBinding) holder.binding;
            if (setterVM.SELECTED_SETTER_INDEX == holder.getAbsoluteAdapterPosition()) {
                bind.setter.setTextColor(ContextCompat.getColor(SetterActivity.this, R.color.red0));
                vpView.setCurrentItem(setterVM.SELECTED_SETTER_INDEX, false);
            } else {
                bind.setter.setTextColor(ContextCompat.getColor(SetterActivity.this, R.color.black));
            }
        }

    }
}
