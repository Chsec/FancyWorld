package com.chw.application.view.character;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.chw.application.AppConfigCenter;
import com.chw.application.R;
import com.chw.application.databinding.FragmentCharacterBinding;
import com.chw.application.databinding.ItemCharacterBinding;
import com.chw.application.model.table.TCharacter;
import com.chw.application.util.ServiceLogicUtils;
import com.chw.application.util.bus.StickLiveDataBus;
import com.chw.application.view.BaseFragment;
import com.chw.application.viewmodel.character.CharacterVM;
import com.chw.application.widget.bar.LetterSidebarView;
import com.chw.application.widget.popwindow.FilterPopWindow;
import com.chw.application.widget.recyclerview.BindDataVBImpl;
import com.chw.application.widget.recyclerview.CommonAdapter;
import com.chw.application.widget.recyclerview.CommonViewHolder;
import com.chw.application.widget.recyclerview.ItemDecorationWrap;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.widget.popupwindow.popup.XUIListPopup;
import com.xuexiang.xui.widget.popupwindow.popup.XUIPopup;
import com.xuexiang.xui.widget.progress.loading.ARCLoadingView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CharacterFragment extends BaseFragment<FragmentCharacterBinding> {

    public FragmentCharacterBinding binding;

    private LinearLayoutManager rvLayoutManager;

    private RecyclerView rvView;

    private CharacterVM characterVM;

    private CommonAdapter<TCharacter> rvAdapter;

    private CheckBox filter1, filter2, filter3;

    private FilterPopWindow filterWindow;

    private TextView letterTip;

    private LetterSidebarView letterBar;

    private ItemDecorationWrap.LetterBarItem letterDecoration;

    private LinearLayout multiple, filterBar;

    private Button mulInvert, mulDel, mulCancel, mulAll;

    private ImageView addChar;

    private ARCLoadingView load;

    private RvDataBind rvDatabind;

    public static CharacterFragment newInstance() {
        Bundle args = new Bundle();
        CharacterFragment fragment = new CharacterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return binding.getRoot();
    }

    @Override
    protected FragmentCharacterBinding viewBinding() {
        binding = FragmentCharacterBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    protected void initPrepareFirst() {

        rvView = binding.characterRv;
        filterBar = binding.characterFiltrate;
        filter1 = binding.characterFit1;
        filter2 = binding.characterFit2;
        filter3 = binding.characterFit3;
        addChar = binding.characterAdd;
        letterTip = binding.characterLetterTip;
        letterBar = binding.characterLetterBar;
        multiple = binding.characterMul;
        mulAll = binding.characterMulAll;
        mulCancel = binding.characterMulCancel;
        mulDel = binding.characterMulDel;
        mulInvert = binding.characterMulInvert;
        load = binding.characterWait;
        letterDecoration = new ItemDecorationWrap().getLetterBarItem(requireContext());

        // 其他对象
        characterVM = new ViewModelProvider(this).get(CharacterVM.class);
        rvLayoutManager = new LinearLayoutManager(requireContext());
        rvDatabind = new RvDataBind();
        rvAdapter = new CommonAdapter<>(null, rvDatabind);
    }

    @Override
    protected void initViewSecond() {
        // 初始化RecyclerView
        rvLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvView.setLayoutManager(rvLayoutManager);
        rvView.setItemAnimator(new DefaultItemAnimator());
        rvView.addItemDecoration(letterDecoration);
        rvView.setAdapter(rvAdapter);
    }

    @Override
    protected void initListenerThird() {
        // 为筛选栏注册监听
        filter1.setOnCheckedChangeListener(new FilterBarListener());
        filter2.setOnCheckedChangeListener(new FilterBarListener());
        filter3.setOnCheckedChangeListener(new FilterBarListener());

        addChar.setOnClickListener(this);
        letterBar.setListener(new LetterSideBarListener());
        mulDel.setOnClickListener(this);
        mulCancel.setOnClickListener(this);
        mulInvert.setOnClickListener(this);
        mulAll.setOnClickListener(this);
        rvAdapter.setItemListener(new RvClickListener());
    }

    /**
     * 需要同步更新数据的视图
     */
    @Override
    protected void initObserveFourth() {
        LifecycleOwner lifecycle = getViewLifecycleOwner();
        // 初始化筛选框一
        StickLiveDataBus.get()
                .getChannel(AppConfigCenter.BOOK_CHANGE, String.class)
                .observe(lifecycle, s -> {
                    characterVM.tableTempsLD.removeObservers(lifecycle);
                    characterVM.tableTempsLD.observe(lifecycle, tablesItem -> {
                        String filterText = characterVM.filterBarInit(ACC, tablesItem);
                        filter1.setText(filterText);
                        StickLiveDataBus.get().getChannel(AppConfigCenter.CHARACTER_REFRESH).setValue("");
                    });
                });

        // 页面数据
        StickLiveDataBus.get()
                .getChannel(AppConfigCenter.CHARACTER_REFRESH)
                .observe(this, s -> {
                    String tempName = filter1.getText().toString();
                    if (!tempName.isEmpty()){
                        characterVM.asyncRefreshRV(ADC, rvAdapter, filter1.getText().toString());
                    }
                });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == addChar.getId()) {
            // 添加character
            Intent intent = new Intent(requireContext(), CharAddActivity.class);
            intent.putExtra("tableFlag", Long.parseLong(ACC.getBookRecord(0)));
            startActivity(intent);
        } else if (id == mulAll.getId()) {
            characterVM.mulRecorder.clear();
            characterVM.mulRecorder.put(1, 1);
            rvAdapter.refresh();
        } else if (id == mulCancel.getId()) {
            characterVM.mulRecorder.clear();
            offMultiple();
        } else if (id == mulDel.getId()) {
//            String result = characterVM.removeCharacterBeans();
            characterVM.mulRecorder.clear();
            offMultiple();
        } else if (id == mulInvert.getId()) {
            characterVM.mulRecorder.clear();
            rvAdapter.refresh();
        }
    }

    /**
     * 开启多选模式
     * 1、设置多选标志位true
     * 2、调整多选模式下视图可见性
     * 3、更新RecyclerView，根据多选模式标志刷新视图状态。
     */
    public void onMultiple() {
        characterVM.MUL_MARK = true;
        letterBar.setVisibility(View.INVISIBLE);    //字母导航栏
        multiple.setVisibility(View.VISIBLE);       //多选功能栏
        filterBar.setVisibility(View.INVISIBLE);       //筛选栏
        rvAdapter.refresh();
    }

    /**
     * 关闭多选模式
     * 1、设置多选标志位false
     * 2、调整多选模式下视图可见性
     * 3、更新RecyclerView，根据多选模式标志刷新视图状态。
     */
    private void offMultiple() {
        characterVM.MUL_MARK = false;
        letterBar.setVisibility(View.VISIBLE);    //字母导航栏
        multiple.setVisibility(View.INVISIBLE);   //多选功能栏
        filterBar.setVisibility(View.VISIBLE);       //筛选栏
        rvAdapter.refresh();
    }

    public class RvDataBind extends BindDataVBImpl<TCharacter> {

        @Override
        public void onBindViewHolder(CommonAdapter<TCharacter> adapter, CommonViewHolder viewHolder, TCharacter model, int type) {
            ItemCharacterBinding binding = (ItemCharacterBinding) viewHolder.binding;
            CheckBox multiple = binding.display1Multiple;
            // 头像设置
            binding.display1ShowF.setText(model.getCharShowA());
            binding.display1ShowS.setText(model.getCharShowB());
            binding.display1ShowT.setText(model.getCharShowC());
            multiple.setChecked(false);
            ServiceLogicUtils.loadImage(rvView.getContext(), binding.display1Image, model.getCharProfile());
            multiple.setVisibility(View.INVISIBLE);
        }

        @Override
        public ViewBinding getItemViewBinding(TCharacter model, int position) {
            return ItemCharacterBinding.inflate(LayoutInflater.from(rvView.getContext()), rvView, false);
        }

        @Override
        public void onBindViewHolder(@NonNull CommonViewHolder holder, int position, @NonNull List<Object> payloads) {
            ItemCharacterBinding binding = (ItemCharacterBinding) holder.binding;
            CheckBox multiple = binding.display1Multiple;
            TCharacter model = rvAdapter.getMDatas().get(position);
            // 打开多选条目图案
            multiple.setVisibility(View.VISIBLE);
            if (characterVM.mulRecorder.containsKey(1)) {
                // 全选
                multiple.setChecked(true);
            } else if (characterVM.mulRecorder.containsKey(rvAdapter.getMDatas().get(position).getCharId())) {
                multiple.setChecked(true);
            }
        }
    }

    /**
     * RecyclerView的item点击、长按事件处理
     */
    private class RvClickListener implements CommonAdapter.ItemClickListener {

        @Override
        public void OnItemClickListener(View view, int viewId) {
            // 多选模式处理逻辑
            if (characterVM.MUL_MARK) {
                // 当前视图对应的model的适配器位置与引用
                int index = rvView.getChildAdapterPosition(view);
                int key = rvAdapter.getMDatas().get(index).getCharId();
                CheckBox mulCheckbox = view.findViewById(R.id.display1_multiple);
                if (!characterVM.mulRecorder.containsKey(key)) {
                    // 点击选中
                    characterVM.mulRecorder.put(key, index);
                    mulCheckbox.setChecked(true);
                } else {
                    // 再次点击取消
                    characterVM.mulRecorder.remove(key);
                    mulCheckbox.setChecked(false);
                }
            } else {
                // 非多选模式下，单击打开Char详情页面
                TCharacter cb = rvAdapter.getMDatas().get(rvView.getChildAdapterPosition(view));
                Intent intent = new Intent(requireContext(), CharDetailActivity.class);
                intent.putExtra("id", cb.getCharId());
                startActivity(intent);
            }
        }

        public void OnItemLongClickListener(View view, int viewId) {
            int index = rvView.getChildAdapterPosition(view);
            List<TCharacter> mDatas = rvAdapter.getMDatas();
            TCharacter preData = mDatas.get(index);

            ArrayList<String> popList = new ArrayList<>();
            popList.add("设为置顶");
            popList.add("添加标记");
            popList.add("删除当前");
            popList.add("多选操作");

            // 判断触发长按监听的itemView是否置顶，置顶则修改菜单选项
            if (Objects.equals(mDatas.get(index).getCharLetter(), "☆")) {
                popList.set(0, "取消置顶");
            }

            XUIListPopup popupMenu = new XUIListPopup(requireContext(), XUISimpleAdapter.create(getContext(), popList));
            popupMenu.create(DensityUtils.dp2px(requireContext(), 150), DensityUtils.dp2px(requireContext(), 200), new AdapterView.OnItemClickListener() {
                /**
                 * item的PopupMenu菜单
                 *
                 * @param adapterView   适配器
                 * @param pView          视图
                 * @param i             点击条目索引
                 * @param l             未知
                 */
                @Override
                public void onItemClick(AdapterView<?> adapterView, View pView, int i, long l) {
                    switch (i) {
                        case 0:
                            // 置顶或取消置顶(LivaData自动刷新列表)
                            boolean model = Objects.equals(popList.get(0), "设为置顶");
                            preData.markTop(model);
                            characterVM.topItem(preData);
                            break;
                        case 1:
                            // 标记
                            break;
                        case 2:
                            // 删除
                            if (Objects.equals(preData.getCharRefer(), 0)) {
                                characterVM.delItem(preData);
                            }
                            break;
                        case 3:
                            // 多选
                            onMultiple();
                            break;
                    }
                    popupMenu.dismiss();
                }
            });
            popupMenu.setAnimStyle(XUIPopup.ANIM_GROW_FROM_CENTER);
            popupMenu.setPreferredDirection(XUIPopup.DIRECTION_TOP);
            popupMenu.show(view);
        }

    }

    /**
     * 筛选栏事件分流及CommonPopup样式、数据、监听处理
     */
    private class FilterBarListener implements CompoundButton.OnCheckedChangeListener {

        /**
         * 解决恢复筛选栏选中状态时再次触发点击事件问题的标志
         */
        private boolean INTERVAL = true;

        /**
         * 筛序栏监听事件，处理CommonPopup初始化
         *
         * @param compoundButton 不知道
         * @param b              不知道
         */
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int id = compoundButton.getId();
            if (INTERVAL) {
                if (id == filter1.getId()) {
                    // 筛选栏之模板
                    filter1.setChecked(true);
                    filter2.setChecked(false);
                    filter3.setChecked(false);
                    showPop(characterVM.tempsNameCacheList, filter1);
                } else if (id == filter2.getId()) {
                    filter1.setChecked(false);
                    filter2.setChecked(true);
                    filter3.setChecked(false);
                    showPop(new ArrayList<>(), filter2);
                } else if (id == filter3.getId()) {
                    filter1.setChecked(false);
                    filter2.setChecked(false);
                    filter3.setChecked(true);
                    showPop(new ArrayList<>(), filter3);
                }
            } else {
                INTERVAL = true;
            }
        }

        public void showPop(List<String> datas, CheckBox click) {
            // 判断当前是否显示
            if (filterWindow != null && filterWindow.isShowing()) {
                filterWindow.dismiss();
                filterWindow = null;
            }

            filterWindow = new FilterPopWindow(requireActivity());
            filterWindow.create(datas, 0.6f, filterBar.getWidth(), DensityUtils.dp2px(requireContext(), 200));
            filterWindow.setOnClickListener(new FilterPopWindow.ItemClickAdapter() {
                @Override
                public void OnItemClickListener(View view, int viewId) {
                    TextView tableNameTV = view.findViewById(R.id.pop_item_title);
                    String tableName = tableNameTV.getText().toString();
                    click.setText(tableName);
                    long tableTempFlag = characterVM.getTableTempFlag(tableName);
                    ACC.setBookRecord(0, String.valueOf(tableTempFlag));
                    StickLiveDataBus.get().getChannel(AppConfigCenter.CHARACTER_REFRESH).setValue("");
                    CharacterFragment.this.filterWindow.dismiss();
                }
            });
            filterWindow.setOnDismissListener(() -> {
                FilterBarListener.this.INTERVAL = false;
                click.setChecked(false);
            });
            filterWindow.showAsDropDown(filterBar);
        }
    }

    /**
     * 字母侧边栏的字母更新监听
     */
    private class LetterSideBarListener implements LetterSidebarView.OnLetterUpdateListener {
        @Override
        public void onDownListener(String letter) {
            // 点击向上箭头迅速跳转至列表头部
            if (letter.equals("↑")) {
                rvLayoutManager.scrollToPositionWithOffset(0, 0);
            } else {
                moveRVItem(letter);
            }
        }

        @Override
        public void onMoveListener(String letter) {
            letterTip.setVisibility(View.VISIBLE);
            letterTip.setText(letter);
            moveRVItem(letter);
        }

        @Override
        public void onUpListener(String letter) {
            letterTip.setVisibility(View.INVISIBLE);
        }

        private void moveRVItem(String letter) {
            // 获取字母，遍历adapter位置后将当前视图滚动到目标位置
            List<TCharacter> mDatas = rvAdapter.getMDatas();
            if (mDatas == null || mDatas.isEmpty()) {
                return;
            }
            for (int i = 0; i < mDatas.size(); i++) {
                String l = mDatas.get(i).getCharLetter().substring(0, 1);
                if (l.equals(letter)) {
                    rvLayoutManager.scrollToPositionWithOffset(i, 0);
                    return;
                }
            }
        }

    }

}