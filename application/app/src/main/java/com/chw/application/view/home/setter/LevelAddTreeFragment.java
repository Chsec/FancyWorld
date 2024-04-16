package com.chw.application.view.home.setter;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.chw.application.R;
import com.chw.application.databinding.FragmentLevelAddTableBinding;
import com.chw.application.databinding.ItemSetAddBinding;
import com.chw.application.databinding.ItemSetEmptyBinding;
import com.chw.application.viewmodel.home.setter.LevelTreeVM;
import com.chw.application.widget.recyclerview.BindDataVBImpl;
import com.chw.application.widget.recyclerview.CommonAdapter;
import com.chw.application.widget.recyclerview.CommonViewHolder;
import com.chw.application.model.table.TreeTemplate;
import com.chw.application.view.BaseFragment;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.edittext.ClearEditText;

import java.util.Objects;

public class LevelAddTreeFragment extends BaseFragment<FragmentLevelAddTableBinding> {

    /**
     * 新建节点时辅助光标跳转,true新建
     */
    private static boolean focus = false;

    private LevelTreeVM setVM;

    private CommonAdapter<TreeTemplate> rvAdapter;

    private RecyclerView rvView;

    private LinearLayoutManager rvLinerLayout;

    private ClearEditText nodeGroup;

    /**
     * 工具栏
     */
    private LinearLayout bottomToolbar, keyboardToolbar;

    /**
     * 工具栏功能按钮
     */
    private ImageButton copy, create, delete, up, down, edit, save, tableTemplate, cancel;

    /**
     * 当前选中节点索引,-1代表未选中任何节点
     */
    private int focusNode;

    /**
     * 层级结构布局参数对象
     */
    private LinearLayout.LayoutParams rvLP;

    /**
     * 层级结构竖线资源
     */
    private Drawable drawable;

    private RotateAnimation rotate;

    /**
     * 状态栏高度
     */
    private int navigationBarHeight;

    /**
     * 软键盘工具栏布局参数对象
     */
    private ConstraintLayout.LayoutParams keyboardLP;

    private ImageButton table;

    @Override
    protected FragmentLevelAddTableBinding viewBinding() {
        return FragmentLevelAddTableBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initPrepareFirst() {

        nodeGroup = viewBinding.nodeGroup;
        table = viewBinding.table;

        keyboardToolbar = viewBinding.keyboardToolBar;
        edit = viewBinding.edit;
        save = viewBinding.save;
        cancel = viewBinding.cancel;
        tableTemplate = viewBinding.tableTemplate;

        rvView = viewBinding.tree;

        bottomToolbar = viewBinding.bottomToolBar;
        copy = viewBinding.copy;
        create = viewBinding.create;
        delete = viewBinding.delete;
        up = viewBinding.up;
        down = viewBinding.down;

        setVM = new ViewModelProvider(this).get(LevelTreeVM.class);
        rvLinerLayout = new LinearLayoutManager(requireContext());

        focusNode = -1;

        rvLP = new LinearLayout.LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT);
        rvLP.setMargins(3, 0, 10, 0);
        drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.shape_set_line, null);

        keyboardLP = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
        keyboardLP.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        navigationBarHeight = DensityUtils.getNavigationBarHeight(requireContext());
    }

    @Override
    protected void initViewSecond() {
        rvAdapter = new CommonAdapter<>(setVM.getGroup(""), new FoldViewHolder());
        rvLinerLayout.setOrientation(LinearLayoutManager.VERTICAL);
        rvView.setLayoutManager(rvLinerLayout);
        rvView.setAdapter(rvAdapter);
        rvView.setItemAnimator(null);
        rvView.setHasFixedSize(false);

        keyboardToolbar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initListenerThird() {
        KeyboardUtils.addKeyboardToggleListener(requireActivity(), new KeyboardListener());
        copy.setOnClickListener(this);
        create.setOnClickListener(this);
        delete.setOnClickListener(this);
        up.setOnClickListener(this);
        down.setOnClickListener(this);
        table.setOnClickListener(this);
    }

    @Override
    protected void initObserveFourth() {
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == edit.getId()) {
            rvAdapter.updateData(setVM.getGroup(""));
        } else if (id == cancel.getId()) {
            rvAdapter.updateData(setVM.getGroup(""));
        } else if (id == save.getId()) {
            rvAdapter.refresh();
            setVM.save(rvAdapter.getMDatas(), "");
        } else if (id == down.getId()) {
            TreeTemplate t = rvAdapter.getMDatas().get(focusNode);
            int depth = t.getNodeDepth();
            // 1、未选中不执行。2、第一个根节点不执行。3、同级第一个节点不执行
            if (focusNode != -1 && focusNode != 0 && rvAdapter.getMDatas().get(focusNode - 1).getNodeDepth() != depth) {
                TreeTemplate clone = t.clone();
                clone.setNodeDepth(depth + 1);
                rvAdapter.replace(clone, focusNode);
            }
        } else if (id == up.getId()) {

        } else if (id == table.getId()) {
            if (nodeGroup.getText() == null) {
                XToastUtils.error("请输入设定名称");
            } else {
                Intent intent = new Intent(requireContext(), LevelAddTableFragment.class);
                intent.putExtra("tempName", nodeGroup.getText().toString());
                startActivity(intent);
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setVM.save(rvAdapter.getMDatas(), "");
        KeyboardUtils.removeAllKeyboardToggleListeners();
    }

    /**
     * 层级结构图标动画
     *
     * @param view 层级结构图标
     */
    private void nodeRotate(View view) {
        if (rotate == null) {
            rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setInterpolator(new LinearInterpolator());
            rotate.setDuration(2000);//设置动画持续周期
            rotate.setRepeatCount(-1);//设置重复次数
            rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
            rotate.setStartOffset(10);//执行前的等待时间
        }
        //        view.clearAnimation();
        view.setAnimation(rotate);
    }

    private class FoldViewHolder extends BindDataVBImpl<TreeTemplate> {

        private final int EMPTY_DATA = -1;

        private com.chw.application.databinding.ItemSetEmptyBinding emptyHolder;

        private com.chw.application.databinding.ItemSetAddBinding nodeHolder;

        @Override
        public void onBindViewHolder(CommonAdapter<TreeTemplate> adapter, CommonViewHolder viewHolder, TreeTemplate model, int type) {
            if (model.getNodeId() == EMPTY_DATA) {
                emptyHolder.empty.setOnClickListener(view -> {
                    rvAdapter.remove(0);
                    rvAdapter.add(setVM.createNodeData(true, -1, 0));
                });
            } else {
                LinearLayout itemLine = nodeHolder.item;
                int nodeDepth = model.getNodeDepth();
                int count = itemLine.getChildCount();

                if (isDisplayParentNode(model)) {
                    itemLine.getLayoutParams().height = 40;
                    // 设置监听
                    createNodeView(nodeHolder, model, viewHolder);
                    modifyNodeData(nodeHolder, model, viewHolder);
                    removeNodeView(nodeHolder, model, viewHolder);
                    iconRotate(nodeHolder, model, viewHolder);

                    // 初始化层级关系
                    if (count != 3) itemLine.removeViews(0, count - 3);
                    for (int i = 0; i < nodeDepth; i++) {
                        View im = new View(itemLine.getContext());
                        im.setBackground(drawable);
                        im.setLayoutParams(rvLP);
                        itemLine.addView(im, i);
                    }

                    // 初始化数据
                    nodeHolder.value.setText(model.getNodeValue());
                    nodeHolder.state.setClickable(true);

                    // 新建节点:控制光标跳转、位置
                    if (focus) {
                        nodeHolder.value.requestFocus();
                        nodeHolder.value.setSelection(model.getNodeValue().length());
                        focus = false;
                    }

                } else {
                    itemLine.getLayoutParams().height = 0;
                }
            }
        }

        @Override
        public ViewBinding getItemViewBinding(TreeTemplate model, int position) {

            if (model.getNodeId() == EMPTY_DATA) {
                emptyHolder = ItemSetEmptyBinding.inflate(LayoutInflater.from(rvView.getContext()), rvView, false);
                return emptyHolder;
            } else {
                nodeHolder = ItemSetAddBinding.inflate(LayoutInflater.from(rvView.getContext()), rvView, false);
                return nodeHolder;
            }
        }

        /**
         * 创建新的视图节点
         */
        public void createNodeView(ItemSetAddBinding binding, TreeTemplate model, CommonViewHolder viewHolder) {
            binding.value.setOnEditorActionListener((textView, i, keyEvent) -> {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_NULL) {
                    binding.value.clearFocus();
                    focus = true;
                    int position = viewHolder.getAbsoluteAdapterPosition();
                    TreeTemplate newTree;
                    if (position > 0) {
                        newTree = rvAdapter.getMDatas().get(position - 1).clone();
                    } else {
                        newTree = setVM.createNodeData(true, -1, 0);
                    }
                    rvAdapter.insert(newTree, position + 1);
                    return true;
                }
                return false;
            });
        }

        /**
         * 修改节点数据
         */
        public void modifyNodeData(ItemSetAddBinding binding, TreeTemplate model, CommonViewHolder viewHolder) {
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    model.setNodeValue(editable.toString());
                }
            };
            binding.value.setOnFocusChangeListener((view, b) -> {
                if (b) {
                    focusNode = viewHolder.getAbsoluteAdapterPosition();
                    binding.value.setCursorVisible(true);
                    binding.value.addTextChangedListener(textWatcher);
                } else {
                    focusNode = -1;
                    binding.value.setCursorVisible(false);
                    binding.value.removeTextChangedListener(textWatcher);
                }
            });
        }

        /**
         * 删除当前节点
         */
        public void removeNodeView(ItemSetAddBinding binding, TreeTemplate model, CommonViewHolder viewHolder) {
            binding.value.setOnBackSpaceListener(() -> {
                if (Objects.equals(model.getNodeValue(), "")) {
                    // 删除时光标上移
                    int position = viewHolder.getAbsoluteAdapterPosition();
                    if (position >= 1) {
                        rvAdapter.getMDatas().remove(position);
                        rvAdapter.notifyItemChanged(position - 1);
                        focus = true;
                        rvAdapter.notifyItemRemoved(position);
                    } else if (position == 0) {
                        rvAdapter.remove(0);
                    }
                    if (rvAdapter.getItemCount() == 0) {
                        TreeTemplate flag = new TreeTemplate();
                        flag.setNodeId(EMPTY_DATA);
                        rvAdapter.add(flag);
                    }
                    return true;
                } else {
                    return false;
                }
            });
        }

        /**
         * 处理层级图标
         */
        private void iconRotate(ItemSetAddBinding binding, TreeTemplate model, CommonViewHolder viewHolder) {
            binding.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nodeRotate(binding.icon);
                    model.setNodeExpend(!model.isNodeExpend());
                    int position = viewHolder.getAbsoluteAdapterPosition();
                    int count = 0;
                    for (int i = position; i < rvAdapter.getMDatas().size(); i++) {
                        if (rvAdapter.getMDatas().get(i).getNodeDepth() - 1 == model.getNodeDepth()) {
                            ++count;
                        }
                    }
                    rvAdapter.notifyItemRangeChanged(position, count);
                }
            });
        }

        /**
         * 当前节点是否显示
         *
         * @param presentNode 当前节点
         * @return 返回当前节点父节点展开状态，如果当前节点为根节点则直接返回true
         */
        private boolean isDisplayParentNode(TreeTemplate presentNode) {
            if (!presentNode.isNodeRoot()) {
                int nodeParent = presentNode.getNodeParent();
                for (TreeTemplate t : rvAdapter.getMDatas()) {
                    if (t.getNodeId() == nodeParent) {
                        return t.isNodeExpend();
                    }
                }

            }
            return true;
        }

    }

    private class KeyboardListener implements KeyboardUtils.SoftKeyboardToggleListener {
        @Override
        public void onToggleSoftKeyboard(boolean isVisible) {
            if (isVisible && focusNode != -1) {
                int screenHeight = DensityUtils.getDisplaySize(requireContext(), true).y;
                //获取View可见区域的bottom
                Rect rect = new Rect();
                viewBinding.getRoot().getWindowVisibleDisplayFrame(rect);
                int space = screenHeight - rect.bottom - navigationBarHeight;
                keyboardToolbar.setVisibility(View.VISIBLE);
                keyboardLP.setMargins(0, 0, 0, space);
                keyboardToolbar.setLayoutParams(keyboardLP);
            } else {
                keyboardToolbar.setVisibility(View.INVISIBLE);
            }
        }
    }
}
