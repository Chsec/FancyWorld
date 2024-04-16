//package com.chw.application.view.component;
//
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.CheckBox;
//import androidx.fragment.app.FragmentActivity;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewbinding.ViewBinding;
//import com.chw.application.R;
//import com.chw.application.databinding.ItemBeanDisplay1Binding;
//import com.chw.application.lib.recyclerview.BindDataVBImpl;
//import com.chw.application.lib.recyclerview.CommonAdapter;
//import com.chw.application.lib.recyclerview.CommonViewHolder;
//import com.chw.application.model.db.entity.TCharacter;
//import com.chw.application.util.ServiceLogicUtils;
//import com.chw.application.view.page.character.CharDetailActivity;
//
//import java.util.HashMap;
//import java.util.List;
//
//public class RVBeanDisplayBind1 extends BindDataVBImpl<TCharacter> {
//
//    private final RecyclerView mParent;
//
//    private final FragmentActivity activity;
//
//    /**
//     * 页面多选模式标记
//     */
//    public boolean MUL_MARK = false;
//
//    /**
//     * 页面多选模式下存储选中位置容器<对象id,对象在适配器中的索引>
//     */
//    public HashMap<Integer, Integer> mulRecorder = new HashMap<>();
//
//    private CommonAdapter<TCharacter> adapter;
//
//    public RVBeanDisplayBind1(RecyclerView mParent, FragmentActivity activity) {
//        this.mParent = mParent;
//        this.activity = activity;
//    }
//
//    @Override
//    public void onBindViewHolder(CommonAdapter<TCharacter> adapter, CommonViewHolder viewHolder, TCharacter model, int type) {
//        ItemBeanDisplay1Binding binding = (ItemBeanDisplay1Binding) viewHolder.binding;
//        CheckBox multiple = binding.display1Multiple;
//
//        binding.display1ShowF.setText(model.getCharShowF());
//        binding.display1ShowS.setText(model.getCharShowS());
//        binding.display1ShowT.setText(model.getCharShowT());
//        multiple.setChecked(false);
//        ServiceLogicUtils.loadImage(mParent.getContext(), binding.display1Image, model.getCharProfile());
//
//        if (this.adapter == null) clickListener(adapter);
//        if (MUL_MARK) {
//            // 打开多选条目图案
//            multiple.setVisibility(View.VISIBLE);
//            if (mulRecorder.containsKey(1)) {
//                // 全选
//                multiple.setChecked(true);
//            } else if (mulRecorder.containsKey(model.getCharId())) {
//                multiple.setChecked(true);
//            }
//        } else {
//            // 普通模式
//            multiple.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    @Override
//    public ViewBinding getItemViewBinding(TCharacter model, int position) {
//        return ItemBeanDisplay1Binding.inflate(LayoutInflater.from(mParent.getContext()), mParent, false);
//    }
//
//    /**
//     * 所有item共用一个事件监听,方法只需执行一次
//     */
//    private void clickListener(CommonAdapter<TCharacter> adapter) {
//        if (this.adapter == null) {
//            this.adapter = adapter;
//            this.adapter.setItemListener(new CommonAdapter.ItemClickListener() {
//                @Override
//                public void OnItemClickListener(View view, int viewId) {
//                    int index = mParent.getChildAdapterPosition(view);
//                    List<TCharacter> mDatas = adapter.getMDatas();
//                    // 多选模式处理逻辑
//                    if (MUL_MARK) {
//                        // 当前视图对应的model的适配器位置与引用
//                        CommonViewHolder preVH = (CommonViewHolder) mParent.getChildViewHolder(view);
//                        CheckBox mulCheckbox = preVH.mViews.findViewById(R.id.display1_multiple);
//                        if (!mulRecorder.containsKey(mDatas.get(index).getCharId())) {
//                            // 点击选中
//                            mulRecorder.put(mDatas.get(index).getCharId(), index);
//                            mulCheckbox.setChecked(true);
//                        } else {
//                            // 再次点击取消
//                            mulRecorder.remove(mDatas.get(index).getCharId());
//                            mulCheckbox.setChecked(false);
//                        }
//                    } else {
//                        // 非多选模式下，单击打开Char详情页面
//                        TCharacter cb = mDatas.get(index);
//                        Intent intent = new Intent(activity, CharDetailActivity.class);
//                        intent.putExtra("id", cb.getCharId());
//                        activity.startActivity(intent);
//                    }
//                }
//
//                @Override
//                public void OnItemLongClickListener(View view, int viewId) {
//
//                }
//            });
//        }
//    }
//
//}
