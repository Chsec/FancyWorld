package com.chw.application.view.component;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.chw.application.App;
import com.chw.application.R;
import com.chw.application.databinding.ItemTableTemplate1Binding;
import com.chw.application.model.bean.Result;
import com.chw.application.widget.recyclerview.BindDataVBImpl;
import com.chw.application.widget.recyclerview.CommonAdapter;
import com.chw.application.widget.recyclerview.CommonViewHolder;
import com.chw.application.model.table.TableTemplate;
import com.chw.application.widget.dialog.CustomDialog;
import com.chw.application.widget.toast.ToastTipWrapper;
import com.chw.application.viewmodel.MainVM;
import com.chw.application.util.bus.StickLiveDataBus;

import java.util.List;

public class RvTableTemplateBind1 extends BindDataVBImpl<TableTemplate> {

    private final String ITEM_EVENT = "RTTI1";

    private final RecyclerView mParent;

    private final ItemTouchHelper touchHelper;

    private final String[] itemType;

    private final AppCompatActivity activity;

    private MutableLiveData<Result> LCB = null;

    public RvTableTemplateBind1(RecyclerView mParent, AppCompatActivity activity, ItemTouchHelper touchHelper) {
        this.mParent = mParent;
        this.itemType = App.getInstance().getResources().getStringArray(R.array.table_template_item_type);
        this.activity = activity;
        this.touchHelper = touchHelper;
    }

    @Override
    public void onBindViewHolder(CommonAdapter<TableTemplate> adapter, CommonViewHolder viewHolder, TableTemplate model, int type) {
        ItemTableTemplate1Binding bind = (ItemTableTemplate1Binding) viewHolder.binding;
        // 初始化item视图数据
        bind.star.setVisibility(model.isItemFill() ? View.VISIBLE : View.INVISIBLE);
        bind.value.setText(model.getItemName());
        bind.fill.setChecked(model.isItemFill());
        bind.type.setText(itemType[model.getItemType()]);
        bind.rely.setText(MainVM.getGroupName(model.getItemRely()));
        // 编辑与浏览模式实现
        bind.value.setEnabled(false);
        bind.add.setVisibility(View.INVISIBLE);
        bind.del.setVisibility(View.INVISIBLE);
        bind.order.setVisibility(View.INVISIBLE);
        bind.set.setVisibility(View.INVISIBLE);
        bind.fill.setEnabled(false);

        // 监听事件
        valListener(adapter, viewHolder, bind, model);
        setListener(adapter, viewHolder, bind, model);
        addListener(adapter, viewHolder, bind, model);
        delListener(adapter, viewHolder, bind, model);
        ordListener(adapter, viewHolder, bind, model);
        filListener(adapter, viewHolder, bind, model);
    }

    @Override
    public ViewBinding getItemViewBinding(TableTemplate model, int position) {
        return ItemTableTemplate1Binding.inflate(LayoutInflater.from(mParent.getContext()), mParent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position, @NonNull List<Object> payloads) {
        ItemTableTemplate1Binding bind = (ItemTableTemplate1Binding) holder.binding;
        // 编辑与浏览模式实现
        bind.value.setEnabled(CommonViewHolder.available);
        bind.add.setVisibility(CommonViewHolder.visible);
        bind.del.setVisibility(CommonViewHolder.visible);
        bind.order.setVisibility(CommonViewHolder.visible);
        bind.set.setVisibility(CommonViewHolder.visible);
        bind.fill.setEnabled(CommonViewHolder.available);
    }

    private void valListener(CommonAdapter<TableTemplate> adapter, CommonViewHolder holder, ItemTableTemplate1Binding bind, TableTemplate model) {
        // 字段名字输入事件监听
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                model.setItemName(editable.toString());
            }
        };
        bind.value.setOnFocusChangeListener((view, b) -> {
            if (b) {
                bind.value.addTextChangedListener(textWatcher);
            } else {
                bind.value.removeTextChangedListener(textWatcher);
            }
        });
    }

    private void setListener(CommonAdapter<TableTemplate> adapter, CommonViewHolder holder, ItemTableTemplate1Binding bind, TableTemplate model) {
        // 设置图标点击事件监听
        bind.set.setOnClickListener(view -> {
            StickLiveDataBus.get()
                    .getChannel(ITEM_EVENT, Result.class)
                    .observe(activity, result -> {
                        CommonViewHolder viewHolder = (CommonViewHolder) mParent.findViewHolderForAdapterPosition(result.getResultSort());
                        assert viewHolder != null;
                        ItemTableTemplate1Binding binding = (ItemTableTemplate1Binding) viewHolder.binding;
                        binding.type.setText(itemType[model.getItemType()]);
                        binding.rely.setText(MainVM.getGroupName(model.getItemRely()));
                    });
            CustomDialog.itemTypeDialog(activity, ITEM_EVENT, holder.getAbsoluteAdapterPosition(), model);
        });
    }

    private void addListener(CommonAdapter<TableTemplate> adapter, CommonViewHolder holder, ItemTableTemplate1Binding bind, TableTemplate model) {
        bind.add.setOnClickListener(view -> {
            // 在后面插入
            adapter.insert(new TableTemplate("", 0, model.getTableOwner(), model.getTableFlag(), model.getTableName()), holder.getAbsoluteAdapterPosition());
        });
    }

    private void delListener(CommonAdapter<TableTemplate> adapter, CommonViewHolder holder, ItemTableTemplate1Binding bind, TableTemplate model) {
        bind.del.setOnClickListener(view -> {
            Result res;
            if (adapter.getMDatas().size() == 1) {
                res = new Result(false, "模板唯一字段，禁止删除");
            } else if (model.getItemRefer() != 0) {
                res = new Result(false, "模板字段在用，禁止删除");
            } else {
                adapter.remove(holder.getAbsoluteAdapterPosition());
                res = new Result(true, "字段删除成功");
            }
            if (res.isResultState()) {
                ToastTipWrapper.toastDialog(activity, res.getResultTip()).show();
            } else {
                ToastTipWrapper.toastDialog(activity, res.getResultTip()).show();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void ordListener(CommonAdapter<TableTemplate> adapter, CommonViewHolder holder, ItemTableTemplate1Binding bind, TableTemplate model) {
        bind.order.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    // startDrag:开始拖拽提供的ViewHolder。
                    // 默认情况下，ItemTouchHelper在视图被长按时开始拖动。
                    // 通过重写ItemTouchHelper.Callback.isLongPressDragEnabled()来禁用该行为。
                    touchHelper.startDrag(holder);
                }
                return false;
            }
        });
    }

    private void filListener(CommonAdapter<TableTemplate> adapter, CommonViewHolder holder, ItemTableTemplate1Binding bind, TableTemplate model) {
        bind.fill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bind.star.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }
}

