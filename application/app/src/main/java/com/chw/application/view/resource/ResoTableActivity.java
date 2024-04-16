package com.chw.application.view.resource;

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
import com.chw.application.viewmodel.resource.ResoTablVM;
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
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.util.ArrayList;

public class ResoTableActivity extends BaseActivity<TableTemplatePage1Binding> {

    private final String ORIGINAL_EVENT_MARK = "RESO_OEM";

    private final String MENU_EVENT_MARK = "RESO_MEM";

    private TableTemplatePage1Binding binding;

    private ResoTablVM resoTempVM;

    //RecyclerView的拖动处理器
    private ItemTouchHelper rvTouchHelper;

    //RecyclerView布局管理器
    private LinearLayoutManager rvLineLayout;

    // RecyclerView适配器
    private CommonAdapter<TableTemplate> rvAdapter;

    // RecyclerView视图
    private RecyclerView rvView;

    private MaterialSpinner tempNameSpinner;

    private RoundButton cancelEdit, saveEdit, openEdit;

    // onKeyDown事件计时变量
    private long clickTime = 0L;

    private String[] typeArray;

    private IncludeOriginalBinding oneInclude, twoInclude, thrInclude, fouInclude, fivInclude;

    private TextView tempNameTitle;

    private Toolbar toolbar;

    private RvTableTemplateBind1 databind;

    @Override
    protected TableTemplatePage1Binding viewBinding() {
        binding = TableTemplatePage1Binding.inflate(getLayoutInflater());
        return binding;
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
        fouInclude = IncludeOriginalBinding.inflate(LayoutInflater.from(scrollView.getContext()));
        fivInclude = IncludeOriginalBinding.inflate(LayoutInflater.from(scrollView.getContext()));
        scrollLayout.addView(oneInclude.getRoot(), 0);
        scrollLayout.addView(twoInclude.getRoot(), 1);
        scrollLayout.addView(thrInclude.getRoot(), 2);
        scrollLayout.addView(thrInclude.getRoot(), 3);
        scrollLayout.addView(thrInclude.getRoot(), 4);
        oneInclude.getRoot().setId(R.id.resource_one);
        twoInclude.getRoot().setId(R.id.resource_two);
        thrInclude.getRoot().setId(R.id.resource_thr);
        fouInclude.getRoot().setId(R.id.resource_fou);
        fivInclude.getRoot().setId(R.id.resource_fiv);

        rvView = viewBinding.recyclerView;
        rvLineLayout = new LinearLayoutManager(this);

        // 准备其他对象
        typeArray = getResources().getStringArray(R.array.table_template_item_type);
        resoTempVM = new ViewModelProvider(this).get(ResoTablVM.class);
    }

    /**
     * 1、RecyclerView初始化时默认为浏览状态
     */
    @Override
    protected void initViewSecond() {
        // 初始化视图
        tempNameTitle.setText(R.string.resource_template_name);
        // 初始化原始字段视图
        oneInclude.star.setVisibility(View.VISIBLE);
        oneInclude.fill.setVisibility(View.INVISIBLE);
        oneInclude.key.setText(getResources().getString(R.string.resource_one));
        twoInclude.key.setText(getResources().getString(R.string.resource_two));
        thrInclude.key.setText(getResources().getString(R.string.resource_three));
        fouInclude.key.setText(getResources().getString(R.string.resource_four));
        fivInclude.key.setText(getResources().getString(R.string.resource_five));

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
                resoTempVM.otArraylist.get(0).setItemName(editable.toString());
            }
        });
        oneInclude.set.setOnClickListener(this);
        oneInclude.fill.setOnCheckedChangeListener(new SwitchButtonListener());

        twoInclude.value.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable editable) {
                resoTempVM.otArraylist.get(1).setItemName(editable.toString());
            }
        });
        twoInclude.set.setOnClickListener(this);
        twoInclude.fill.setOnCheckedChangeListener(new SwitchButtonListener());

        thrInclude.value.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable editable) {
                resoTempVM.otArraylist.get(2).setItemName(editable.toString());
            }
        });
        thrInclude.set.setOnClickListener(this);
        thrInclude.fill.setOnCheckedChangeListener(new SwitchButtonListener());

        thrInclude.value.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable editable) {
                resoTempVM.otArraylist.get(3).setItemName(editable.toString());
            }
        });
        thrInclude.set.setOnClickListener(this);
        thrInclude.fill.setOnCheckedChangeListener(new SwitchButtonListener());

        thrInclude.value.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable editable) {
                resoTempVM.otArraylist.get(4).setItemName(editable.toString());
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
        resoTempVM.templateSortLD.observe(this, list -> {
            tempNameSpinner.setItems(list);
            resoTempVM.templateSort.clear();
            resoTempVM.templateSort.addAll(list);
            // 增、删、改模板时设置默认选中
            if (list.contains(resoTempVM.PRESENT_TEMPLATE)) {
                tempNameSpinner.setSelectedItem(resoTempVM.PRESENT_TEMPLATE);
            } else {
                resoTempVM.PRESENT_TEMPLATE = tempNameSpinner.getSelectedItem();
            }
            if (rvAdapter == null) {
                databind = new RvTableTemplateBind1(rvView, ResoTableActivity.this, rvTouchHelper);
                rvAdapter = new CommonAdapter<>(resoTempVM.getPageData(), databind);
                rvView.setAdapter(rvAdapter);
            } else {
                rvAdapter.updateData(resoTempVM.getPageData());
            }
            syncOriginalGroupView(0, false);
        });

        StickLiveDataBus.get()
                .getChannel(ORIGINAL_EVENT_MARK, Result.class)
                .observe(this, result -> {
                    switch (result.getResultSort()) {
                        case 0:
                            syncOriginalGroupView(0, true);
                            break;
                        case 1:
                            syncOriginalGroupView(1, true);
                            break;
                        case 2:
                            syncOriginalGroupView(2, true);
                            break;
                        case 3:
                            syncOriginalGroupView(3, true);
                            break;
                        case 4:
                            syncOriginalGroupView(4, true);
                            break;
                    }
                });

        // 菜单的提示性弹窗：
        StickLiveDataBus.get()
                .getChannel(MENU_EVENT_MARK, Result.class)
                .observe(this, result -> {
                    Result res = null;
                    switch (result.getResultSort()) {
                        case 0:
                            res = resoTempVM.newTemplate(result.getResultTip());
                            break;
                        case 1:
                            if (result.isResultState()) {
                                res = resoTempVM.delTemplate();
                            }
                            break;
                        case 2:
                            res = resoTempVM.reNameTemp(result.getResultTip());
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
        if (viewId == openEdit.getId()) {
            // 编辑模式：切换编辑模式
            editModel();
            rvAdapter.refresh(1);

        } else if (viewId == cancelEdit.getId()) {
            // 取消编辑：重新从数据库提取当前模板数据刷新RecyclerView
            lookModel();
            rvAdapter.updateData(resoTempVM.templateDate);
            resoTempVM.resetOTArrayList();
            syncOriginalGroupView(0, false);

        } else if (viewId == saveEdit.getId()) {
            // 保存编辑：获取rvAdapter最新数据更新至数据库
            lookModel();
            ArrayList<TableTemplate> data = (ArrayList<TableTemplate>) rvAdapter.getMDatas();
            resoTempVM.saveTempData(data);
            syncOriginalGroupView(0, false);
            rvAdapter.updateData(data);

        } else if (getIncludeId(view) == oneInclude.getRoot().getId()) {
            // 原始字段一：设置
            CustomDialog.itemComDialog(this, ORIGINAL_EVENT_MARK, 0, resoTempVM.otArraylist.get(0));
        } else if (getIncludeId(view) == twoInclude.getRoot().getId()) {
            // 原始字段二：设置
            CustomDialog.itemTypeDialog(this, ORIGINAL_EVENT_MARK, 1, resoTempVM.otArraylist.get(1));
        } else if (getIncludeId(view) == thrInclude.getRoot().getId()) {
            // 原始字段三：设置
            CustomDialog.itemTypeDialog(this, ORIGINAL_EVENT_MARK, 2, resoTempVM.otArraylist.get(2));
        } else if (getIncludeId(view) == fouInclude.getRoot().getId()) {
            // 原始字段四：设置
            CustomDialog.itemTypeDialog(this, ORIGINAL_EVENT_MARK, 3, resoTempVM.otArraylist.get(3));
        } else if (getIncludeId(view) == fivInclude.getRoot().getId()) {
            // 原始字段五：设置
            CustomDialog.itemTypeDialog(this, ORIGINAL_EVENT_MARK, 4, resoTempVM.otArraylist.get(4));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (resoTempVM.PAGE_STATUS) {
                Toast.makeText(this, "编辑内容未保存，再按返回键将退出！", Toast.LENGTH_SHORT).show();
                // 内容未保存时，两下返回键强制退出
                if (System.currentTimeMillis() - clickTime > 2000) {
                    clickTime = System.currentTimeMillis();
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
        fouInclude.value.setEnabled(true);
        fouInclude.set.setVisibility(View.VISIBLE);
        fouInclude.fill.setEnabled(true);
        fivInclude.value.setEnabled(true);
        fivInclude.set.setVisibility(View.VISIBLE);
        fivInclude.fill.setEnabled(true);

        // 重新加载RecyclerView列表，开启功能图标
        CommonViewHolder.setVisible(View.VISIBLE);
        CommonViewHolder.setAvailable(true);
        resoTempVM.PAGE_STATUS = true;
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

        fouInclude.value.setEnabled(false);
        fouInclude.set.setVisibility(View.INVISIBLE);
        fouInclude.fill.setEnabled(false);

        fivInclude.value.setEnabled(false);
        fivInclude.set.setVisibility(View.INVISIBLE);
        fivInclude.fill.setEnabled(false);

        // 重新加载RecyclerView列表，关闭功能图标
        CommonViewHolder.setVisible(View.INVISIBLE);
        CommonViewHolder.setAvailable(false);
        resoTempVM.PAGE_STATUS = false;
    }

    /**
     * @param view include内的view
     * @return 所属include标签的id
     */
    public int getIncludeId(View view) {
        ViewGroup parent = (ViewGroup) view.getParent().getParent();
        return parent.getId();
    }

    /**
     * 原始字段模板容器数据同步至视图
     *
     * @param flag  需要更改字段的索引
     * @param model true为单，false为全部，同步全部字段时flag必须为0。
     */
    private void syncOriginalGroupView(int flag, boolean model) {
        switch (flag) {
            case 0:
                twoInclude.star.setVisibility(resoTempVM.otArraylist.get(1).isItemFill() ? View.VISIBLE : View.INVISIBLE);
                oneInclude.value.setText(resoTempVM.otArraylist.get(0).getItemName());
                oneInclude.type.setText(typeArray[resoTempVM.otArraylist.get(0).getItemType()]);
                if (model) {
                    break;
                }
            case 1:
                twoInclude.value.setText(resoTempVM.otArraylist.get(1).getItemName());
                twoInclude.fill.setChecked(resoTempVM.otArraylist.get(1).isItemFill());
                twoInclude.type.setText(typeArray[resoTempVM.otArraylist.get(1).getItemType()]);
                String groupName = MainVM.getGroupName(resoTempVM.otArraylist.get(1).getItemRely());
                twoInclude.rely.setText(groupName);
                if (model) {
                    break;
                }
            case 2:
                thrInclude.star.setVisibility(resoTempVM.otArraylist.get(2).isItemFill() ? View.VISIBLE : View.INVISIBLE);
                thrInclude.value.setText(resoTempVM.otArraylist.get(2).getItemName());
                thrInclude.fill.setChecked(resoTempVM.otArraylist.get(2).isItemFill());
                thrInclude.type.setText(typeArray[0]);
                if (model) {
                    break;
                }
            case 3:
                fouInclude.star.setVisibility(resoTempVM.otArraylist.get(3).isItemFill() ? View.VISIBLE : View.INVISIBLE);
                fouInclude.value.setText(resoTempVM.otArraylist.get(3).getItemName());
                fouInclude.fill.setChecked(resoTempVM.otArraylist.get(3).isItemFill());
                fouInclude.type.setText(typeArray[resoTempVM.otArraylist.get(3).getItemType()]);
                fouInclude.rely.setText(MainVM.getGroupName(resoTempVM.otArraylist.get(3).getItemRely()));
                if (model) {
                    break;
                }
            case 4:
                fivInclude.star.setVisibility(resoTempVM.otArraylist.get(4).isItemFill() ? View.VISIBLE : View.INVISIBLE);
                fivInclude.value.setText(resoTempVM.otArraylist.get(4).getItemName());
                fivInclude.fill.setChecked(resoTempVM.otArraylist.get(4).isItemFill());
                fivInclude.type.setText(typeArray[resoTempVM.otArraylist.get(4).getItemType()]);
                fivInclude.rely.setText(MainVM.getGroupName(resoTempVM.otArraylist.get(4).getItemRely()));
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
                // 新建模板：内部会更新当前模版名观察者标记
                CustomDialog.inputDialog(ResoTableActivity.this, MENU_EVENT_MARK, 0, "新建模板", "请输入模板名称");
            } else if (itemId == R.id.menu_temp_delete) {
                // 删除模板：无需更新当前模版名观察者标记
                String content = "确认删除【" + resoTempVM.PRESENT_TEMPLATE + "】";
                CustomDialog.deleteDialog(ResoTableActivity.this, MENU_EVENT_MARK, 1, "删除模板", content);
            } else if (itemId == R.id.menu_temp_rename) {
                // 重命名模板：内部会更新当前模版名观察者标记
                CustomDialog.inputDialog(ResoTableActivity.this, MENU_EVENT_MARK, 2, "重命名模板", "请输入模板名");
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
            resoTempVM.PRESENT_TEMPLATE = item;
            rvAdapter.updateData(resoTempVM.getPageData());
            syncOriginalGroupView(0, false);
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
            } else if (id == fouInclude.getRoot().getId()) {
                fouInclude.star.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            } else if (id == fivInclude.getRoot().getId()) {
                fivInclude.star.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        }
    }

}