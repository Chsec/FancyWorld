package com.chw.application.view.home.setter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.chw.application.AppConfigCenter;
import com.chw.application.databinding.FragmentLevelManagerBinding;
import com.chw.application.databinding.ItemLevelListBinding;
import com.chw.application.databinding.ItemLevelSortBinding;
import com.chw.application.model.table.Setting;
import com.chw.application.model.table.SettingSort;
import com.chw.application.util.bus.StickLiveDataBus;
import com.chw.application.view.BaseFragment;
import com.chw.application.viewmodel.home.setter.LevelSetterVM;
import com.chw.application.widget.recyclerview.BindDataVBImpl;
import com.chw.application.widget.recyclerview.CommonAdapter;
import com.chw.application.widget.recyclerview.CommonViewHolder;

import java.util.Collections;
import java.util.List;

public class LevelManagerFragment extends BaseFragment<FragmentLevelManagerBinding> {
    /**
     * 等级设定器的ViewModel
     */
    private LevelSetterVM levelVM;

    private RecyclerView rvSortView, rvListView;

    private LinearLayoutManager rvSortLM;

    private LinearLayoutManager rvListLM;

    private SortBindData rvSortBD;

    private ListBindData rvListBD;

    private CommonAdapter<SettingSort> rvSortAdapter;

    private CommonAdapter<Setting> rvListAdapter;

    public static LevelManagerFragment newInstance() {
        Bundle args = new Bundle();
        LevelManagerFragment fragment = new LevelManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FragmentLevelManagerBinding viewBinding() {
        return FragmentLevelManagerBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initPrepareFirst() {

        rvSortView = viewBinding.levelManagerSort;
        rvSortLM = new LinearLayoutManager(requireContext());
        rvSortBD = new SortBindData();
        rvSortAdapter = new CommonAdapter<>(null, rvSortBD);

        rvListView = viewBinding.levelManagerList;
        rvListLM = new LinearLayoutManager(requireContext());
        rvListBD = new ListBindData();
        rvListAdapter = new CommonAdapter<>(null, rvListBD);

        levelVM = new ViewModelProvider(requireActivity()).get(LevelSetterVM.class);
    }

    @Override
    protected void initViewSecond() {

        rvSortLM.setOrientation(LinearLayoutManager.VERTICAL);
        rvSortView.setLayoutManager(rvSortLM);
        rvSortView.setAdapter(rvSortAdapter);

        rvListLM.setOrientation(LinearLayoutManager.VERTICAL);
        rvListView.setLayoutManager(rvListLM);
        rvListView.setAdapter(rvListAdapter);
    }

    @Override
    protected void initListenerThird() {
        rvSortAdapter.setItemListener(new CommonAdapter.ItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int viewId) {

            }

            @Override
            public void OnItemLongClickListener(View view, int viewId) {

            }
        });

        rvListAdapter.setItemListener(new CommonAdapter.ItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int viewId) {

            }

            @Override
            public void OnItemLongClickListener(View view, int viewId) {

            }
        });
    }

    @Override
    protected void initObserveFourth() {
        LifecycleOwner lifecycleOwner = getViewLifecycleOwner();

        //初始化等级设定器种类
        StickLiveDataBus.get()
                .getChannel(AppConfigCenter.BOOK_CHANGE, String.class)
                .observe(lifecycleOwner, s -> {
                    levelVM.getSettingSortList().removeObservers(lifecycleOwner);
                    levelVM.getSettingSortList()
                            .observe(lifecycleOwner, settingSorts -> {
                                Collections.sort(settingSorts);
                                levelVM.settingSortList = settingSorts;
                                rvSortAdapter.updateData(settingSorts);
                            });

                    levelVM.getSetting().removeObservers(lifecycleOwner);
                    levelVM.getSetting()
                            .observe(lifecycleOwner, new Observer<List<Setting>>() {
                                @Override
                                public void onChanged(List<Setting> settings) {
                                    levelVM.settingList = settings;
                                }
                            });
                });
    }

    @Override
    public void onClick(View view) {

    }

    private class SortBindData extends BindDataVBImpl<SettingSort> {

        @Override
        public void onBindViewHolder(CommonAdapter<SettingSort> adapter, CommonViewHolder viewHolder, SettingSort model, int type) {
            ItemLevelSortBinding binding = (ItemLevelSortBinding) viewHolder.binding;
            binding.levelSort.setText(model.getSortName());

            if (levelVM.SELECT_SORT == -1) {
                levelVM.SELECT_SORT = model.getSortFlag();
                rvListAdapter.updateData(levelVM.parseSetting(model.getSortFlag()));
            } else {
                if (model.getSortFlag() == levelVM.SELECT_SORT) {
                    rvListAdapter.updateData(levelVM.parseSetting(levelVM.SELECT_SORT));
                }
            }
            binding.levelOrder.setVisibility(CommonViewHolder.visible);
        }

        @Override
        public ViewBinding getItemViewBinding(SettingSort model, int position) {
            return ItemLevelSortBinding.inflate(LayoutInflater.from(rvSortView.getContext()), rvSortView, false);
        }

        @Override
        public void onBindViewHolder(@NonNull CommonViewHolder holder, int position, @NonNull List<Object> payloads) {
            ItemLevelSortBinding binding = (ItemLevelSortBinding) holder.binding;
            binding.levelOrder.setVisibility(CommonViewHolder.visible);
        }
    }

    private class ListBindData extends BindDataVBImpl<Setting> {

        @Override
        public void onBindViewHolder(CommonAdapter<Setting> adapter, CommonViewHolder viewHolder, Setting model, int type) {
            ItemLevelListBinding binding = (ItemLevelListBinding) viewHolder.binding;
        }

        @Override
        public ViewBinding getItemViewBinding(Setting model, int position) {
            return ItemLevelListBinding.inflate(LayoutInflater.from(rvListView.getContext()), rvListView, false);
        }
    }

}
