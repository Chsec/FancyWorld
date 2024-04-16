package com.chw.application.view.resource;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.chw.application.App;
import com.chw.application.AppConfigCenter;
import com.chw.application.R;
import com.chw.application.comparator.TableNameComparator;
import com.chw.application.databinding.FragmentResourceBinding;
import com.chw.application.databinding.ItemResourceDetailBinding;
import com.chw.application.databinding.ItemResourceSortBinding;
import com.chw.application.model.bean.Result;
import com.chw.application.model.table.Resource;
import com.chw.application.model.table.TableTemplate;
import com.chw.application.util.bus.StickLiveDataBus;
import com.chw.application.view.BaseFragment;
import com.chw.application.viewmodel.resource.ResourceVM;
import com.chw.application.widget.AsyncThreadCallback;
import com.chw.application.widget.recyclerview.BindDataVBImpl;
import com.chw.application.widget.recyclerview.CommonAdapter;
import com.chw.application.widget.recyclerview.CommonViewHolder;
import com.chw.application.widget.recyclerview.ResourceDiffCallBack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ResourceFragment extends BaseFragment<FragmentResourceBinding> {

    /**
     * 资源清单观察者引用
     */
    private Observer<List<Resource>> resoDetailObserver;

    private LinearLayout filter;

    private AppCompatCheckBox filter1, filter2, filter3;

    private RecyclerView rvDetailView, rvSortView;

    private LinearLayoutManager rvDetailLM, rvSortLM;

    private CommonAdapter<Resource> rvDetailAdapter;

    private Secondary rvDetailDB;

    private Primary rvSortDB;

    private ImageButton add;

    private ResourceVM resourceVM;

    private CommonAdapter<TableTemplate> rvSortAdapter;

    private LifecycleOwner lifeOwner;

    public static ResourceFragment newInstance() {
        Bundle args = new Bundle();
        ResourceFragment fragment = new ResourceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FragmentResourceBinding viewBinding() {
        return FragmentResourceBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initPrepareFirst() {
        //准备筛选栏
        filter = viewBinding.resourceFiltrate;
        filter1 = viewBinding.resourceFit1;
        filter2 = viewBinding.resourceFit2;
        filter3 = viewBinding.resourceFit3;
        add = viewBinding.resourceAdd;

        //资源
        rvSortView = viewBinding.resourcePrimary;
        rvSortLM = new LinearLayoutManager(requireContext());
        rvSortDB = new Primary();
        rvSortAdapter = new CommonAdapter<>(null, rvSortDB);

        //准备数据展示区
        rvDetailView = viewBinding.resourceSecondary;
        rvDetailLM = new LinearLayoutManager(requireContext());
        rvDetailDB = new Secondary();
        rvDetailAdapter = new CommonAdapter<>(null, rvDetailDB);
        //准备ViewModel对象
        resourceVM = new ViewModelProvider(this).get(ResourceVM.class);
    }

    @Override
    protected void initViewSecond() {

        rvDetailLM.setOrientation(LinearLayoutManager.VERTICAL);
        rvDetailView.setLayoutManager(rvDetailLM);
        rvDetailView.setAdapter(rvDetailAdapter);
    }

    @Override
    protected void initListenerThird() {
        add.setOnClickListener(this);
    }

    @Override
    protected void initObserveFourth() {
        lifeOwner = getViewLifecycleOwner();

        //资源种类
        StickLiveDataBus.get()
                .getChannel(AppConfigCenter.BOOK_CHANGE, String.class)
                .observe(lifeOwner, s -> {
                    if (resourceVM.resoSortLD != null) {
                        resourceVM.resoSortLD.removeObservers(lifeOwner);
                    }
                    resourceVM.getResoSortLD()
                            .observe(lifeOwner, tableSort -> {
                                App.getInstance().getExecutor().async(() -> {
                                    Collections.sort(tableSort, new TableNameComparator());
                                    for (TableTemplate t : tableSort) {
                                        if (t.getTableFlag() == resourceVM.preSelectedSortFlag) {
                                            break;
                                        } else {
                                            resourceVM.preSelectedSortIndex = 0;
                                        }
                                    }
                                    rvSortAdapter.updateData(tableSort);
                                    return new Result(true, "资源种类列表刷新成功！");
                                }, new AsyncThreadCallback());
                            });
                });

        //资源清单
        StickLiveDataBus.get()
                .getChannel(AppConfigCenter.RESOURCE_REFRESH)
                .observe(lifeOwner, l -> {
                    App.getInstance().getExecutor().async(() -> {
                        List<Resource> resources = ADC.resourceCache.get(resourceVM.preSelectedSortFlag);
                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ResourceDiffCallBack(rvDetailAdapter.getMDatas(), resources), true);
                        diffResult.dispatchUpdatesTo(rvDetailAdapter);
                        return new Result(true, "ResourceFragment刷新成功！");
                    }, new AsyncThreadCallback());
                });
    }

    @Override
    public void onClick(View view) {
        rvSortAdapter.setItemListener(new CommonAdapter.ItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int viewId) {
                int po = rvSortView.getChildAdapterPosition(view);
                long tableFlag = rvSortAdapter.getMDatas().get(po).getTableFlag();
                if (resourceVM.preSelectedSortFlag != tableFlag) {
                    resourceVM.preSelectedSortFlag = tableFlag;
                    rvSortAdapter.refresh(1);
                }
            }

            @Override
            public void OnItemLongClickListener(View view, int viewId) {

            }
        });

    }

    private class Primary extends BindDataVBImpl<TableTemplate> {

        @Override
        public void onBindViewHolder(CommonAdapter<TableTemplate> adapter, CommonViewHolder viewHolder, TableTemplate model, int type) {
            ItemResourceSortBinding binding = (ItemResourceSortBinding) viewHolder.binding;
            TextView t = binding.resourceSort;
            t.setText(model.getTableName());
            if (model.getTableFlag() == resourceVM.preSelectedSortFlag) {
                t.setTextColor(ContextCompat.getColor(requireContext(), R.color.red0));
                StickLiveDataBus.get().getChannel(AppConfigCenter.RESOURCE_REFRESH).setValue("");
            } else {
                t.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            }
        }

        @Override
        public void onBindViewHolder(@NotNull CommonViewHolder holder, int position, @NotNull List<Object> payloads) {
            // 维护选中状态并刷新清单列表数据
            ItemResourceSortBinding binding = (ItemResourceSortBinding) holder.binding;
            TextView t = binding.resourceSort;
            TableTemplate item = rvSortAdapter.getMDatas().get(position);
            if (resourceVM.preSelectedSortFlag == item.getTableFlag()) {
                t.setTextColor(ContextCompat.getColor(requireContext(), R.color.red0));
                StickLiveDataBus.get().getChannel(AppConfigCenter.RESOURCE_REFRESH).setValue("");
            } else {
                t.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            }
        }

        @Override
        public ViewBinding getItemViewBinding(TableTemplate model, int position) {
            return ItemResourceSortBinding.inflate(LayoutInflater.from(rvSortView.getContext()), rvSortView, false);
        }
    }

    private class Secondary extends BindDataVBImpl<Resource> {

        @Override
        public void onBindViewHolder(CommonAdapter<Resource> adapter, CommonViewHolder viewHolder, Resource model, int type) {
            ItemResourceDetailBinding binding = (ItemResourceDetailBinding) viewHolder.binding;
            binding.itemShowA.setText(model.getResoShowA());
            binding.itemShowB.setText(model.getResoShowB());
            binding.itemShowC.setText(model.getResoShowC());
            binding.itemShowD.setText(model.getResoShowD());
            binding.itemShowE.setText(model.getResoShowE());
        }

        @Override
        public ViewBinding getItemViewBinding(Resource model, int position) {
            return ItemResourceDetailBinding.inflate(LayoutInflater.from(rvDetailView.getContext()), rvDetailView, false);
        }
    }

}