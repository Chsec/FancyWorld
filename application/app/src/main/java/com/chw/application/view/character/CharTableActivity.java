package com.chw.application.view.character;

import android.text.Editable;
import android.view.*;
import android.widget.*;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chw.application.R;
import com.chw.application.databinding.IncludeOriginalBinding;
import com.chw.application.databinding.TableTemplatePage1Binding;
import com.chw.application.model.bean.Result;
import com.chw.application.viewmodel.character.CharTableVM;
import com.chw.application.widget.recyclerview.CommonAdapter;
import com.chw.application.widget.recyclerview.CommonViewHolder;
import com.chw.application.model.table.TableTemplate;
import com.chw.application.widget.recyclerview.ItemTouchCallback;
import com.chw.application.view.component.RvTableTemplateBind1;
import com.chw.application.widget.dialog.CustomDialog;
import com.chw.application.view.BaseActivity;
import com.chw.application.viewmodel.MainVM;
import com.chw.application.util.bus.StickLiveDataBus;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.util.ArrayList;

public class CharTableActivity extends BaseActivity<TableTemplatePage1Binding> {

    /**
     * 原始字段的StickLiveDataBus事件标志
     */
    private final String ORIGINAL_EVENT_MARK = "CHAR_OEM";

    /**
     * 菜单的StickLiveDataBus事件标志
     */
    private final String MENU_EVENT_MARK = "CHAR_MEM";

    /**
     * 页面的退出计时
     */
    private long EXIT_TIME = 0L;

    private CharTableVM charTablVM;

    private ItemTouchHelper rvTouchHelper;

    private LinearLayoutManager rvLineLayout;

    private CommonAdapter<TableTemplate> rvAdapter;

    private RecyclerView rvView;

    private Button cancelEdit, saveEdit, openEdit;

    private String[] typeArray;

    private IncludeOriginalBinding oneInclude, twoInclude, thrInclude;

    private MaterialSpinner tempNameSpinner;

    private TextView tempNameTitle;

    private Toolbar toolbar;

    private RvTableTemplateBind1 databind;

    @Override
    protected TableTemplatePage1Binding viewBinding() {
        return TableTemplatePage1Binding.inflate(getLayoutInflater());
    }

    @Override
    protected void initPrepareFirst() {
        toolbar = viewBinding.tempToolbar.toolbar;

        openEdit = viewBinding.tempEdit;
        cancelEdit = viewBinding.tempCancel;
        saveEdit = viewBinding.tempSave;

        tempNameSpinner = viewBinding.tempName;
        tempNameTitle = viewBinding.tempNameTitle;

        // 准备原始字段视图
        ScrollView scrollView = viewBinding.scroll2;
        LinearLayout scrollLayout = viewBinding.line2;
        oneInclude = IncludeOriginalBinding.inflate(LayoutInflater.from(scrollView.getContext()));
        twoInclude = IncludeOriginalBinding.inflate(LayoutInflater.from(scrollView.getContext()));
        thrInclude = IncludeOriginalBinding.inflate(LayoutInflater.from(scrollView.getContext()));
        scrollLayout.addView(oneInclude.getRoot(), 0);
        scrollLayout.addView(twoInclude.getRoot(), 1);
        scrollLayout.addView(thrInclude.getRoot(), 2);
        oneInclude.getRoot().setId(R.id.character_one);
        twoInclude.getRoot().setId(R.id.character_two);
        thrInclude.getRoot().setId(R.id.character_thr);

        rvView = viewBinding.recyclerView;
        rvLineLayout = new LinearLayoutManager(this);

        // 准备其他对象
        typeArray = getResources().getStringArray(R.array.table_template_item_type);
        charTablVM = new ViewModelProvider(this).get(CharTableVM.class);
    }

    @Override
    protected void initViewSecond() {
        // 初始化视图
        tempNameTitle.setText(R.string.character_template_name);

        // 初始化原始字段
        oneInclude.star.setVisibility(View.VISIBLE);
        oneInclude.key.setText(getResources().getString(R.string.character_one));
        oneInclude.fill.setVisibility(View.INVISIBLE);
        oneInclude.getRoot().setId(R.id.character_one);

        twoInclude.key.setText(getResources().getString(R.string.character_two));
        twoInclude.getRoot().setId(R.id.character_two);

        thrInclude.key.setText(getResources().getString(R.string.character_three));
        thrInclude.getRoot().setId(R.id.character_thr);

        // RecyclerView设置null数据及设置RecyclerView拖动工具类、回调实现
        rvLineLayout = new LinearLayoutManager(this);
        rvLineLayout.setOrientation(LinearLayoutManager.VERTICAL);
        rvView.setLayoutManager(rvLineLayout);
        rvView.setHasFixedSize(true);

        rvTouchHelper = new ItemTouchHelper(new ItemTouchCallback<TableTemplate>());
        rvTouchHelper.attachToRecyclerView(rvView);

        lookModel();
    }

    @Override
    protected void initListenerThird() {

        toolbar.setOnMenuItemClickListener(new ToolbarListener());
        tempNameSpinner.setOnItemSelectedListener(new SpinnerListener());

        oneInclude.value.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable editable) {
                charTablVM.originalContainer.get(0).setItemName(editable.toString());
            }
        });
        oneInclude.set.setOnClickListener(this);
        oneInclude.fill.setOnCheckedChangeListener(new SwitchButtonListener());

        twoInclude.value.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable editable) {
                charTablVM.originalContainer.get(1).setItemName(editable.toString());
            }
        });
        twoInclude.set.setOnClickListener(this);
        twoInclude.fill.setOnCheckedChangeListener(new SwitchButtonListener());

        thrInclude.value.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable editable) {
                charTablVM.originalContainer.get(2).setItemName(editable.toString());
            }
        });
        thrInclude.set.setOnClickListener(this);
        thrInclude.fill.setOnCheckedChangeListener(new SwitchButtonListener());

        saveEdit.setOnClickListener(this);
        cancelEdit.setOnClickListener(this);
        openEdit.setOnClickListener(this);
    }

    @Override
    protected void initObserveFourth() {
        // 初始化模板选择器：控制RecyclerView的初始化与模板变更时的数据刷新；
        charTablVM.getTableSortLD().observe(this, list -> {
            tempNameSpinner.setItems(list);
            charTablVM.tableSortCache.clear();
            charTablVM.tableSortCache.addAll(list);
            // 增、删、改模板时设置默认选中
            if (list.contains(charTablVM.PRESENT_TABLE)) {
                tempNameSpinner.setSelectedItem(charTablVM.PRESENT_TABLE);
            } else {
                charTablVM.PRESENT_TABLE = tempNameSpinner.getSelectedItem();
            }
            if (rvAdapter == null) {
                databind = new RvTableTemplateBind1(rvView, CharTableActivity.this, rvTouchHelper);
                rvAdapter = new CommonAdapter<>(charTablVM.getTemplateData(), databind);
                rvView.setAdapter(rvAdapter);
            } else {
                rvAdapter.updateData(charTablVM.getTemplateData());
            }
            syncOriginalView(0, false);
        });

        //原始字段类型设置观察者
        StickLiveDataBus.get()
                .getChannel(ORIGINAL_EVENT_MARK, Result.class)
                .observe(this, result -> {
                    switch (result.getResultSort()) {
                        case 0:
                            syncOriginalView(0, true);
                            break;
                        case 1:
                            syncOriginalView(1, true);
                            break;
                        case 2:
                            syncOriginalView(2, true);
                            break;
                    }
                });

        // 菜单观察者
        StickLiveDataBus.get()
                .getChannel(MENU_EVENT_MARK, Result.class)
                .observe(this, result -> {
                    Result res = null;
                    switch (result.getResultSort()) {
                        case 0:
                            res = charTablVM.newTemplate(result.getResultTip());
                            break;
                        case 1:
                            if (result.isResultState()) {
                                res = charTablVM.delTemplate();
                            }
                            break;
                        case 2:
                            res = charTablVM.reNameTemp(result.getResultTip());
                            break;
                    }
                    if (res != null) {
                        if (res.isResultState()) {
                            XToastUtils.success(res.getResultTip());
                        } else {
                            XToastUtils.error(res.getResultTip());
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        int includeId = getIncludeId(view);
        if (viewId == openEdit.getId()) {
            // 编辑模式：切换编辑模式
            editModel();
            rvAdapter.refresh(1);
        } else if (viewId == cancelEdit.getId()) {
            // 取消编辑：恢复初始状态
            lookModel();
            rvAdapter.updateData(charTablVM.customCache);
            charTablVM.resetOriginalContainer();
            syncOriginalView(0, false);

        } else if (viewId == saveEdit.getId()) {
            // 保存编辑：获取rvAdapter最新数据更新至数据库
            lookModel();
            ArrayList<TableTemplate> data = (ArrayList<TableTemplate>) rvAdapter.getMDatas();
            charTablVM.saveTempData(data);
            syncOriginalView(0, false);
            rvAdapter.updateData(data);

        } else if (includeId == oneInclude.getRoot().getId()) {
            // 原始字段一：设置
            CustomDialog.itemComDialog(this, ORIGINAL_EVENT_MARK, 0, charTablVM.originalContainer.get(0));

        } else if (includeId == twoInclude.getRoot().getId()) {
            // 原始字段二：设置
            CustomDialog.itemTypeDialog(this, ORIGINAL_EVENT_MARK, 1, charTablVM.originalContainer.get(1));

        } else if (includeId == thrInclude.getRoot().getId()) {
            // 原始字段三：设置
            CustomDialog.itemTypeDialog(this, ORIGINAL_EVENT_MARK, 2, charTablVM.originalContainer.get(2));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 内容未保存时，两下返回键强制退出
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (charTablVM.PAGE_STATUS) {
                XToastUtils.error("编辑内容未保存，再按返回键将退出！");
                if (System.currentTimeMillis() - EXIT_TIME > 2000) {
                    EXIT_TIME = System.currentTimeMillis();
                } else {
                    finish();
                }
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databind = null;
    }

    /**
     * @param view include内的view
     * @return 所属include标签的id
     */
    private int getIncludeId(View view) {
        ViewGroup parent = (ViewGroup) view.getParent().getParent();
        return parent.getId();
    }

    /**
     * 编辑模式下组件状态设置
     */
    private void editModel() {
        toolbar.getMenu().clear();

        openEdit.setVisibility(View.INVISIBLE);
        cancelEdit.setVisibility(View.VISIBLE);
        saveEdit.setVisibility(View.VISIBLE);
        tempNameSpinner.setEnabled(false);

        oneInclude.value.setEnabled(true);
        oneInclude.set.setVisibility(View.VISIBLE);
        twoInclude.value.setEnabled(true);
        twoInclude.set.setVisibility(View.VISIBLE);
        twoInclude.fill.setEnabled(true);
        thrInclude.value.setEnabled(true);
        thrInclude.set.setVisibility(View.VISIBLE);
        thrInclude.fill.setEnabled(true);

        // 重新加载RecyclerView列表，开启功能图标
        CommonViewHolder.setVisible(View.VISIBLE);
        CommonViewHolder.setAvailable(true);

        charTablVM.PAGE_STATUS = true;
    }

    /**
     * 查看模式下组件状态设置
     */
    private void lookModel() {
        toolbar.inflateMenu(R.menu.menu_table_template);

        openEdit.setVisibility(View.VISIBLE);
        cancelEdit.setVisibility(View.INVISIBLE);
        saveEdit.setVisibility(View.INVISIBLE);
        tempNameSpinner.setEnabled(true);

        oneInclude.value.setEnabled(false);
        oneInclude.set.setVisibility(View.INVISIBLE);

        twoInclude.value.setEnabled(false);
        twoInclude.set.setVisibility(View.INVISIBLE);
        twoInclude.fill.setEnabled(false);

        thrInclude.value.setEnabled(false);
        thrInclude.set.setVisibility(View.INVISIBLE);
        thrInclude.fill.setEnabled(false);

        // 重新加载RecyclerView列表，关闭功能图标
        CommonViewHolder.setVisible(View.INVISIBLE);
        CommonViewHolder.setAvailable(false);

        charTablVM.PAGE_STATUS = false;
    }

    /**
     * 原始字段模板容器数据同步至视图
     *
     * @param flag  需要更改字段的索引
     * @param model true为单，false为全部，同步全部字段时flag必须为0。
     */
    private void syncOriginalView(int flag, boolean model) {
        switch (flag) {
            case 0:
                oneInclude.value.setText(charTablVM.originalContainer.get(0).getItemName());
                oneInclude.type.setText(typeArray[charTablVM.originalContainer.get(0).getItemType()]);
                oneInclude.rely.setText(MainVM.getGroupName(charTablVM.originalContainer.get(0).getItemRely()));
                if (model) {
                    break;
                }
            case 1:
                twoInclude.star.setVisibility(charTablVM.originalContainer.get(1).isItemFill() ? View.VISIBLE : View.INVISIBLE);
                twoInclude.value.setText(charTablVM.originalContainer.get(1).getItemName());
                twoInclude.type.setText(typeArray[charTablVM.originalContainer.get(1).getItemType()]);
                twoInclude.rely.setText(MainVM.getGroupName(charTablVM.originalContainer.get(1).getItemRely()));
                if (model) {
                    break;
                }
            case 2:
                thrInclude.star.setVisibility(charTablVM.originalContainer.get(2).isItemFill() ? View.VISIBLE : View.INVISIBLE);
                thrInclude.value.setText(charTablVM.originalContainer.get(2).getItemName());
                thrInclude.type.setText(typeArray[charTablVM.originalContainer.get(2).getItemType()]);
                thrInclude.rely.setText(MainVM.getGroupName(charTablVM.originalContainer.get(2).getItemRely()));
                if (model) {
                    break;
                }
        }
    }

    private class ToolbarListener implements Toolbar.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_temp_new) {
                // 新建模板：内部会更新当前模版名标记
                CustomDialog.inputDialog(CharTableActivity.this, MENU_EVENT_MARK, 1, "新建模板", "请输入模板名称");
            } else if (itemId == R.id.menu_temp_delete) {
                // 删除模板：无需更新当前模版名观察者标记
                String content = "确认删除【" + charTablVM.PRESENT_TABLE + "】";
                CustomDialog.deleteDialog(CharTableActivity.this, MENU_EVENT_MARK, 2, "删除模板", content);
            } else if (itemId == R.id.menu_temp_rename) {
                // 重命名模板：内部会更新当前模版名观察者标记
                CustomDialog.inputDialog(CharTableActivity.this, MENU_EVENT_MARK, 3, "重命名模板", "请输入模板名");
            }
            return true;
        }
    }

    /**
     * 主动更改模板选择器当前选中项时，控制RecyclerView数据刷新等操作
     */
    private class SpinnerListener implements MaterialSpinner.OnItemSelectedListener<String> {

        @Override
        public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
            charTablVM.PRESENT_TABLE = item;
            rvAdapter.updateData(charTablVM.getTemplateData());
            syncOriginalView(0, false);
        }
    }

    private class SwitchButtonListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int id = getIncludeId(compoundButton);
            if (id == twoInclude.getRoot().getId()) {
                twoInclude.star.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            } else if (id == thrInclude.getRoot().getId()) {
                thrInclude.star.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        }
    }

}