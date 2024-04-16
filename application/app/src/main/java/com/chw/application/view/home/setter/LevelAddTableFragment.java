package com.chw.application.view.home.setter;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chw.application.R;
import com.chw.application.databinding.TableTemplatePage2Binding;
import com.chw.application.viewmodel.home.setter.LevelTableVM;
import com.chw.application.widget.recyclerview.CommonAdapter;
import com.chw.application.widget.recyclerview.CommonViewHolder;
import com.chw.application.model.table.TableTemplate;
import com.chw.application.widget.recyclerview.ItemTouchCallback;
import com.chw.application.view.component.RvTableTemplateBind2;
import com.chw.application.view.BaseFragment;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;

import java.util.ArrayList;

public class LevelAddTableFragment extends BaseFragment<TableTemplatePage2Binding> {

    private TextView tempNameTitle, rvTitle;

    private EditText tempName;

    private RecyclerView rvView;

    private RoundButton save, cancel, edit;

    private LevelTableVM settingTabVM;

    private CommonAdapter<TableTemplate> rvAdapter;

    private LinearLayoutManager rvLineLayout;

    private ItemTouchHelper rvTouchHelper;

    @Override
    protected TableTemplatePage2Binding viewBinding() {
        return TableTemplatePage2Binding.inflate(getLayoutInflater());
    }

    @Override
    protected void initPrepareFirst() {
        tempNameTitle = viewBinding.temp2NameTitle;
        tempName = viewBinding.temp2Name;
        rvTitle = viewBinding.temp2RvTitle;
        rvView = viewBinding.recyclerView;
        save = viewBinding.temp2Save;
        cancel = viewBinding.temp2Cancel;
        edit = viewBinding.temp2Edit;

        settingTabVM = new ViewModelProvider(this).get(LevelTableVM.class);
        settingTabVM.PRESENT_TEMPLATE = requireActivity().getIntent().getStringExtra("tempName");
        rvLineLayout = new LinearLayoutManager(requireContext());
        rvTouchHelper = new ItemTouchHelper(new ItemTouchCallback<TableTemplate>());
        rvAdapter = new CommonAdapter<>(settingTabVM.getTemplateData(), new RvTableTemplateBind2(rvView, requireActivity(), rvTouchHelper));
    }

    @Override
    protected void initViewSecond() {
        tempNameTitle.setText(R.string.setting_template_title);
        tempName.setText(requireActivity().getIntent().getStringExtra("tempName"));
        tempName.setEnabled(false);
        rvTitle.setText(R.string.setting_rv_title);

        // RecyclerView设置null数据及设置RecyclerView拖动工具类、回调实现
        rvLineLayout = new LinearLayoutManager(requireContext());
        rvLineLayout.setOrientation(LinearLayoutManager.VERTICAL);
        rvView.setLayoutManager(rvLineLayout);
        rvView.setAdapter(rvAdapter);

        rvTouchHelper.attachToRecyclerView(rvView);

        lookModel();
    }

    @Override
    protected void initListenerThird() {
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        edit.setOnClickListener(this);
    }

    @Override
    protected void initObserveFourth() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == edit.getId()) {
            editModel();
            rvAdapter.refresh();

        } else if (id == save.getId()) {
            lookModel();
            ArrayList<TableTemplate> mDatas = (ArrayList<TableTemplate>) rvAdapter.getMDatas();
            rvAdapter.updateData(mDatas);
            settingTabVM.saveTempData(mDatas);

        } else if (id == cancel.getId()) {
            lookModel();
            rvAdapter.updateData(settingTabVM.templateDate);

        }
    }

    /**
     * 浏览模式
     */
    private void lookModel() {
        CommonViewHolder.setAvailable(false);
        CommonViewHolder.setVisible(View.VISIBLE);
        save.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }

    /**
     * 编辑模式
     */
    private void editModel() {
        CommonViewHolder.setAvailable(true);
        CommonViewHolder.setVisible(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.VISIBLE);
        save.setVisibility(View.VISIBLE);
    }
}
