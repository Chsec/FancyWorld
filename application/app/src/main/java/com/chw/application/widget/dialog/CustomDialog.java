package com.chw.application.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.chw.application.App;
import com.chw.application.R;
import com.chw.application.databinding.DialogItemSettingBinding;
import com.chw.application.model.bean.Result;
import com.chw.application.model.repository.SetterRepo;
import com.chw.application.model.repository.TableTemplateRepo;
import com.chw.application.model.repository.TreeTemplateRepo;
import com.chw.application.model.table.Setting;
import com.chw.application.model.table.TableTemplate;
import com.chw.application.view.character.CharTableActivity;
import com.chw.application.util.bus.StickLiveDataBus;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.GravityEnum;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.strategy.InputCallback;
import com.xuexiang.xui.widget.dialog.strategy.InputInfo;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomDialog {

    /**
     * 设定器种类
     */
    private static final List<String> setterOwners = new ArrayList<>();

    /**
     * 设定项缓存
     */
    private static final List<String> settingsStr = new ArrayList<>();

    /**
     * 设定器种类
     */
    private static List<Setting> setterSortName;

    private static List<Setting> settings;

    private static String setterOwner;

    private static List<String> settingNames;

    private static List<Setting> setters;

    /**
     * 数据类型设置对话框（数据类型、数据引用、备注）
     *
     * @param context    上下文
     * @param eventMark  StickLiveDataBus事件标志
     * @param resultSort 结果种类
     * @param container  数据容器
     */
    public static void itemTypeDialog(Activity context, String eventMark, int resultSort, TableTemplate container) {
        // 初始化dialog视图设置
        DialogItemSettingBinding dialogBinding = DialogItemSettingBinding.inflate(context.getLayoutInflater());
        MultiLineEditText comment = dialogBinding.dialogComment;
        MaterialSpinner type = dialogBinding.dialogType;
        MaterialSpinner setter = dialogBinding.dialogRelySetter;
        MaterialSpinner setting = dialogBinding.dialogRelySetting;

        //字段类型的设置
        type.setItems(context.getResources().getStringArray(R.array.table_template_item_type));
        type.setSelectedItem(container.getItemType());
        type.setOnItemSelectedListener((view, position, id, item) -> {
            itemSetOfRely(item.toString(), dialogBinding);
        });
        itemSetOfRely(type.getSelectedItem().toString(), dialogBinding);
        //设定器的选择监听
        setter.setOnItemSelectedListener((view, position, id, item) -> {
            List<Setting> settings = SetterRepo.getSettings(item.toString());
            Collections.sort(settings);
            List<String> temp = new ArrayList<>();
            for (Setting l : settings) {
                temp.add(l.getSettingName());
            }
            setting.setItems(temp);
            setting.setEnabled(true);
        });

        // 创建dialog
        MaterialDialog show = new MaterialDialog.Builder(context)
                // 自定义视图，参数一表示自定义视图的布局，参数二表示是否将自定义视图放置在 ScrollView 中，
                // 这允许用户根据需要滚动自定义视图（小屏幕、长内容等）。但是，有些情况下不需要使用这种行为，
                // 这些情况主要包括在自定义布局中包含 ScrollView、ListViews、RecycleViews、WebViews、GridViews 等
                // wrapInScrollView 该值为 true 时，自定义视图会自动填充它。
                .customView(dialogBinding.getRoot(), true)
                .title("字段类型设置")
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        container.setItemComment(comment.getContentText());
                        container.setItemType(type.getSelectedIndex());

                        String string = setting.getSelectedItem().toString();

                        container.setItemRely(SetterRepo.getSettingFlag(string));
                        StickLiveDataBus.get().getChannel(eventMark, Result.class).postValue(new Result(true, resultSort));
                        dialog.dismiss();
                    }
                })
                .negativeText("取消")
                .cancelable(true)
                .autoDismiss(true)
                .contentGravity(GravityEnum.START)
                .show();

        // 依赖图标的点击事件
        dialogBinding.dialogRelyNew.setOnClickListener(view -> {
            container.setItemComment(comment.getContentText());
            container.setItemType(type.getSelectedIndex());

            String string = setting.getSelectedItem().toString();
            container.setItemRely(TreeTemplateRepo.getGroupFlag(string));

            context.startActivity(new Intent(context, CharTableActivity.class));
            show.dismiss();
        });
    }

    /**
     * 数据类型设置对话框（备注）
     * 1、类型选择列表锁定为typeList数组0索引元素
     *
     * @param context    上下文
     * @param eventMark  StickLiveDataBus事件标志
     * @param resultSort 结果种类
     * @param container  数据容器
     */
    public static void itemComDialog(Activity context, String eventMark, int resultSort, TableTemplate container) {
        DialogItemSettingBinding dialogBinding = DialogItemSettingBinding.inflate(context.getLayoutInflater());
        MultiLineEditText comment = dialogBinding.dialogComment;
        ConstraintLayout rootLayout = dialogBinding.setDialog;
        rootLayout.removeViews(0, rootLayout.getChildCount() - 2);
        comment.setContentText(container.getItemComment());
        new MaterialDialog.Builder(context)
                // 自定义视图，参数一表示自定义视图的布局，参数二表示是否将自定义视图放置在 ScrollView 中，
                // 这允许用户根据需要滚动自定义视图（小屏幕、长内容等）。但是，有些情况下不需要使用这种行为，
                // 这些情况主要包括在自定义布局中包含 ScrollView、ListViews、RecycleViews、WebViews、GridViews 等
                // wrapInScrollView 该值为 true 时，自定义视图会自动填充它。
                .customView(dialogBinding.getRoot(), true)
                .title("字段类型设置")
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        container.setItemComment(comment.getContentText());
                        StickLiveDataBus.get().getChannel(eventMark, Result.class).setValue(new Result(true, resultSort));
                        dialog.dismiss();
                    }
                })
                .negativeText("取消")
                .cancelable(true)
                .autoDismiss(true)
                .show();
    }

    /**
     * 输入对话框，输入结果在事件观察者接收对象的ResultInformation.resultTip字段
     *
     * @param context    上下文
     * @param eventMark  StickLiveDataBus事件标志
     * @param resultSort 结果种类
     * @param title      输入框标题
     * @param content    输入框内容
     */
    public static void inputDialog(Context context, String eventMark, int resultSort, String title, String content) {
        DialogLoader.getInstance().showInputDialog(context,
                R.drawable.vector_delete,
                title,
                content,
                new InputInfo(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_CAP_WORDS),
                new InputCallback() {
                    @Override
                    public void onInput(@NonNull DialogInterface dialog, CharSequence input) {
                        StickLiveDataBus.get().getChannel(eventMark, Result.class).postValue(new Result(true, resultSort, input.toString()));
                    }
                },
                "确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                },
                "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    /**
     * @param context    上下文
     * @param eventMark  StickLiveDataBus事件标志
     * @param resultSort 结果种类
     * @param title      输入框标题
     * @param content    输入框内容
     */
    public static void deleteDialog(Context context, String eventMark, int resultSort, String title, String content) {
        DialogLoader.getInstance().showConfirmDialog(
                context,
                title,
                content,
                "确认",
                (dialogInterface, i) -> {
                    StickLiveDataBus.get().getChannel(eventMark, Result.class).postValue(new Result(true, resultSort));
                    dialogInterface.dismiss();
                },
                "取消",
                (dialogInterface, i) -> {
                    StickLiveDataBus.get().getChannel(eventMark, Result.class).postValue(new Result(false, resultSort));
                    dialogInterface.dismiss();
                }).show();
    }

    /**
     * @param item 当前item选中项
     */
    private static void itemSetOfRely(String item, DialogItemSettingBinding bind) {
        MaterialSpinner setter = bind.dialogRelySetter;
        MaterialSpinner setting = bind.dialogRelySetting;
        switch (item) {
            case "引用设定":
                if (setterSortName == null) {
                    setters = SetterRepo.getSettersItem();
//                    Collections.sort(setters, new ComparatorSetter());
                    for (Setting l : setters) {
                        setterOwners.add(l.getSettingOwner());
                    }
                }
                setter.setItems(setterOwners);
                setter.setEnabled(true);
                if (setterOwner != setter.getSelectedItem()) {
                    setterOwner = setter.getSelectedItem();
                    List<Setting> settings = SetterRepo.getSettings(setterOwner);
                    Collections.sort(settings);
                    settingNames = new ArrayList<>();
                    for (Setting l : settings) {
                        settingNames.add(l.getSettingName());
                    }
                }
                setting.setItems(settingNames);
                setting.setEnabled(true);
                break;
            case "引用单位":
                setter.setItems(TableTemplateRepo.getTablesName(App.TABLE_OWNER[5]));
                setter.setEnabled(true);
                break;
            default:
                setter.setItems("");
                setter.setEnabled(false);
                setting.setItems("");
                setting.setEnabled(false);
        }
    }

}
