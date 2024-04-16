package com.chw.application.view.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.chw.application.App;
import com.chw.application.R;
import com.chw.application.databinding.ItemTableUtilize1CommonBinding;
import com.chw.application.databinding.ItemTableUtilize1TextBinding;
import com.chw.application.databinding.ItemTableUtilize1UnitBinding;
import com.chw.application.widget.recyclerview.BindDataVBImpl;
import com.chw.application.widget.recyclerview.CommonAdapter;
import com.chw.application.widget.recyclerview.CommonViewHolder;
import com.chw.application.model.bean.TableTransformer;
import com.chw.application.model.repository.TreeTemplateRepo;
import com.chw.application.model.table.TableTemplate;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.popupwindow.popup.XUIPopup;

import java.util.ArrayList;
import java.util.List;

public class RvTableUtilizeBind1 extends BindDataVBImpl<TableTemplate> {

    private final RecyclerView mParent;

    private final List<TableTransformer> container;

    private final String[] itemType;

    private final AppCompatActivity activity;

    private final Comment commentPop;

    private final RadioDialog radioDialog;

    /**
     * @param mParent   RecyclerView
     * @param activity  activity
     * @param container 存储转换数据，保证id顺序一致
     */
    public RvTableUtilizeBind1(RecyclerView mParent, AppCompatActivity activity, List<TableTransformer> container) {
        this.mParent = mParent;
        this.itemType = App.getInstance().getResources().getStringArray(R.array.table_template_item_type);
        this.activity = activity;
        this.container = container;
        commentPop = new Comment(activity);
        radioDialog = new RadioDialog(activity);
    }

    @Override
    public void onBindViewHolder(CommonAdapter<TableTemplate> adapter, CommonViewHolder viewHolder, TableTemplate model, int type) {
        switch (itemType[model.getItemType()]) {
            case "文本类型":
                ItemTableUtilize1TextBinding textBinding = (ItemTableUtilize1TextBinding) viewHolder.binding;
                textBinding.utilize1Star.setVisibility(model.isItemFill() ? View.VISIBLE : View.INVISIBLE);
                textBinding.utilize1Key.setText(model.getItemName());

                mulListener(textBinding.utilize1Value, viewHolder);
                comListener(textBinding.utilize1Comment, model);
                break;

            case "引用设定":
                ItemTableUtilize1CommonBinding refeBinding = (ItemTableUtilize1CommonBinding) viewHolder.binding;
                refeBinding.utilize1Star.setVisibility(model.isItemFill() ? View.VISIBLE : View.INVISIBLE);
                refeBinding.utilize1Key.setText(model.getItemName());
                MaterialEditText value = refeBinding.utilize1Value;
                value.setCursorVisible(false);
                value.setFocusable(false);
                value.setTextIsSelectable(false);

                cliListener(value, model, viewHolder);
                comListener(refeBinding.utilize1Comment, model);
                break;

            case "数字类型":
                ItemTableUtilize1UnitBinding numbBinding = (ItemTableUtilize1UnitBinding) viewHolder.binding;
                numbBinding.getRoot().removeView(numbBinding.utilize1Unit);
                numbBinding.utilize1Star.setVisibility(model.isItemFill() ? View.VISIBLE : View.INVISIBLE);
                numbBinding.utilize1Key.setText(model.getItemName());

                valListener(numbBinding.utilize1Value, viewHolder);
                comListener(numbBinding.utilize1Comment, model);
                break;

            case "引用单位":
                ItemTableUtilize1UnitBinding unitBinding = (ItemTableUtilize1UnitBinding) viewHolder.binding;
                TextView unit = unitBinding.utilize1Unit;
                unitBinding.utilize1Star.setVisibility(model.isItemFill() ? View.VISIBLE : View.INVISIBLE);
                unitBinding.utilize1Key.setText(model.getItemName());
                unit.setHint(R.string.activity_main_character);

                comListener(unitBinding.utilize1Comment, model);
                break;

            case "字符串型":
            default:
                ItemTableUtilize1CommonBinding striBinding = (ItemTableUtilize1CommonBinding) viewHolder.binding;
                striBinding.utilize1Star.setVisibility(model.isItemFill() ? View.VISIBLE : View.INVISIBLE);
                striBinding.utilize1Key.setText(model.getItemName());

                valListener(striBinding.utilize1Value, viewHolder);
                comListener(striBinding.utilize1Comment, model);
        }

    }

    @Override
    public ViewBinding getItemViewBinding(TableTemplate model, int position) {
        switch (itemType[model.getItemType()]) {
            case "文本类型":
                return ItemTableUtilize1TextBinding.inflate(LayoutInflater.from(mParent.getContext()), mParent, false);
            case "数字类型":
            case "引用单位":
                return ItemTableUtilize1UnitBinding.inflate(LayoutInflater.from(mParent.getContext()), mParent, false);
            case "引用设定":
            case "字符串型":
            default:
                return ItemTableUtilize1CommonBinding.inflate(LayoutInflater.from(mParent.getContext()), mParent, false);
        }
    }

    private void valListener(EditText value, CommonViewHolder holder) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                container.get(holder.getAbsoluteAdapterPosition()).setValue(editable.toString());
            }
        };
        value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    value.addTextChangedListener(textWatcher);
                } else {
                    value.removeTextChangedListener(textWatcher);
                }
            }
        });
    }

    /**
     * 多行文本框输入框监听
     */
    private void mulListener(MultiLineEditText value, CommonViewHolder holder) {
        value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                container.get(holder.getAbsoluteAdapterPosition()).setValue(value.getContentText());
            }
        });
    }

    /**
     * 文本输入框的点击事件
     */
    private void cliListener(MaterialEditText value, TableTemplate model, CommonViewHolder holder) {
        value.setOnClickListener(view -> {
            ArrayList<String> group = TreeTemplateRepo.getGroupList(model.getItemRely());
            group.add(0, " ");
            radioDialog.show(container.get(holder.getAbsoluteAdapterPosition()), group, value);
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void comListener(ImageButton comment, TableTemplate model) {
        comment.setOnTouchListener((view, motionEvent) -> {
            if (model.getItemComment() == null || model.getItemComment().isEmpty()) {
                return false;
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                commentPop.show(comment, model.getItemComment());
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                commentPop.dismiss();
            }
            return true;
        });
    }

    private class Comment {

        private final Context mContext;

        private final TextView textView;

        private final XUIPopup comment;

        public Comment(Activity activity) {
            comment = new XUIPopup(activity);
            mContext = comment.getContext();
            textView = new TextView(mContext);

            textView.setLayoutParams(comment.generateLayoutParam(
                    DensityUtils.dp2px(mContext, 250),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            textView.setLineSpacing(DensityUtils.dp2px(mContext, 4), 1.0f);
            int padding = DensityUtils.dp2px(mContext, 20);
            textView.setPadding(padding, padding, padding, padding);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.green3));

            comment.setContentView(textView);
            comment.setAnimStyle(XUIPopup.ANIM_GROW_FROM_CENTER);
            comment.setPreferredDirection(XUIPopup.DIRECTION_TOP);
        }

        private void show(View view, String text) {
            textView.setText(text);
            comment.show(view);
        }

        private void dismiss() {
            textView.setText("");
            comment.dismiss();
        }

    }

    private class RadioDialog {

        private final MaterialDialog.Builder builder;

        private final Activity activity;

        public RadioDialog(Activity activity) {
            this.activity = activity;
            builder = new MaterialDialog.Builder(activity)
                    .title("请选择")
                    .cancelable(false)
                    .autoDismiss(true)
                    .positiveText("确认")
                    .negativeText("取消");
        }

        public void show(TableTransformer cant, ArrayList<String> datas, MaterialEditText value) {
            builder.items(datas)
                    .onPositive((dialog, which) -> activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int selectedIndex = builder.build().getSelectedIndex();
                            cant.setValue(datas.get(selectedIndex));
                            value.setText(cant.getValue());
                        }
                    }))
                    .show();
        }

    }

}
