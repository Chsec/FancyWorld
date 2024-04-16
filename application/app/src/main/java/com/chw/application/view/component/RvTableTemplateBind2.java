package com.chw.application.view.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.MotionEventCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.chw.application.App;
import com.chw.application.R;
import com.chw.application.databinding.ItemTableTemplate2Binding;
import com.chw.application.model.bean.Result;
import com.chw.application.widget.recyclerview.BindDataVBImpl;
import com.chw.application.widget.recyclerview.CommonAdapter;
import com.chw.application.widget.recyclerview.CommonViewHolder;
import com.chw.application.model.table.TableTemplate;
import com.chw.application.widget.toast.ToastTipWrapper;

public class RvTableTemplateBind2 extends BindDataVBImpl<TableTemplate> {

    private final RecyclerView mParent;

    private final ItemTouchHelper touchHelper;

    private final String[] itemType;

    private final Activity activity;

    private MutableLiveData<Result> LCB = null;

    public RvTableTemplateBind2(RecyclerView mParent, Activity activity, ItemTouchHelper touchHelper) {
        this.mParent = mParent;
        this.itemType = App.getInstance().getResources().getStringArray(R.array.table_template_item_type);
        this.activity = activity;
        this.touchHelper = touchHelper;
    }

    @Override
    public void onBindViewHolder(CommonAdapter<TableTemplate> adapter, CommonViewHolder viewHolder, TableTemplate model, int type) {
        ItemTableTemplate2Binding bind = (ItemTableTemplate2Binding) viewHolder.binding;
        bind.value.setEnabled(CommonViewHolder.available);
        bind.value.setText(model.getItemName());
        bind.add.setVisibility(CommonViewHolder.visible);
        bind.del.setVisibility(CommonViewHolder.visible);
        bind.order.setVisibility(CommonViewHolder.visible);

        addListener(adapter, viewHolder, bind, model);
        delListener(adapter, viewHolder, bind, model);
        ordListener(adapter, viewHolder, bind, model);
    }

    @Override
    public ViewBinding getItemViewBinding(TableTemplate model, int position) {
        return ItemTableTemplate2Binding.inflate(LayoutInflater.from(mParent.getContext()), mParent, false);
    }

    private void addListener(CommonAdapter<TableTemplate> adapter, CommonViewHolder holder, ItemTableTemplate2Binding bind, TableTemplate model) {
        bind.add.setOnClickListener(view -> {
            // 在后面插入
            TableTemplate defaultItem = new TableTemplate("", 0, model.getTableOwner(), model.getTableName());
            adapter.insert(defaultItem, holder.getAbsoluteAdapterPosition() + 1);
        });
    }

    private void delListener(CommonAdapter<TableTemplate> adapter, CommonViewHolder holder, ItemTableTemplate2Binding bind, TableTemplate model) {
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
    private void ordListener(CommonAdapter<TableTemplate> adapter, CommonViewHolder holder, ItemTableTemplate2Binding bind, TableTemplate model) {
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
}
