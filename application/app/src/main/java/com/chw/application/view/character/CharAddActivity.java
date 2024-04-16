package com.chw.application.view.character;

import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chw.application.R;
import com.chw.application.databinding.ActivityCharAddBinding;
import com.chw.application.model.bean.Result;
import com.chw.application.viewmodel.character.CharAddVM;
import com.chw.application.widget.recyclerview.CommonAdapter;
import com.chw.application.model.table.TableTemplate;
import com.chw.application.util.GlideEngine;
import com.chw.application.view.component.RvTableUtilizeBind1;
import com.chw.application.view.BaseActivity;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;

import java.util.ArrayList;

public class CharAddActivity extends BaseActivity<ActivityCharAddBinding> {

    private CharAddVM charAddVM;

    private MaterialEditText showA, showB, showC;

    private CommonAdapter<TableTemplate> rvAdapter;

    private LinearLayoutManager rvLineLayout;

    private RecyclerView rvView;

    private ImageView profileIV;

    private RoundButton save;

    private long clickTime = 0L;

    private TextView oriTitle, tabTitle;

    /**
     * 模板标志
     */
    private long tableFlag;

    private RvTableUtilizeBind1 rvBindData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ActivityCharAddBinding viewBinding() {
        return ActivityCharAddBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initPrepareFirst() {
        //获得视图组件
        profileIV = viewBinding.activityAddImage;
        oriTitle = viewBinding.activityAddOriginalTitle;
        showA = viewBinding.charAddShowF;
        showB = viewBinding.charAddShowS;
        showC = viewBinding.charAddShowT;
        tabTitle = viewBinding.activityAddTableTitle;
        rvView = viewBinding.activityAddRv;
        save = viewBinding.activityAddSave;

        charAddVM = new ViewModelProvider(this).get(CharAddVM.class);

        //其他对象
        rvLineLayout = new LinearLayoutManager(this);
        rvBindData = new RvTableUtilizeBind1(rvView, CharAddActivity.this, charAddVM.customDataContainer);
        rvAdapter = new CommonAdapter<>(charAddVM.parseTableTemplateCache(ADC.preCharacterTemp), rvBindData);
    }

    @Override
    protected void initViewSecond() {
        // 初始化ImageView
        useGlide(profileIV);

        // 初始化原始字段
        if (charAddVM.originalTempCache != null && !charAddVM.originalTempCache.isEmpty()) {
            viewBinding.charAddShowAk.setText(charAddVM.originalTempCache.get(0).getItemName());
            viewBinding.charAddShowBk.setText(charAddVM.originalTempCache.get(1).getItemName());
            viewBinding.charAddShowCk.setText(charAddVM.originalTempCache.get(2).getItemName());
        } else {
            viewBinding.charAddShowAk.setText(getResources().getString(R.string.character_one));
            viewBinding.charAddShowBk.setText(getResources().getString(R.string.character_two));
            viewBinding.charAddShowCk.setText(getResources().getString(R.string.character_three));
        }

        // 初始化RecyclerView
        rvLineLayout.setOrientation(LinearLayoutManager.VERTICAL);
        rvView.setLayoutManager(rvLineLayout);
        rvView.setAdapter(rvAdapter);
        rvView.setHasFixedSize(true);
    }

    @Override
    protected void initListenerThird() {
        profileIV.setOnClickListener(new IVListener());

        EditListener editListener = new EditListener();
        showA.addTextChangedListener(editListener.use(1));
        showB.addTextChangedListener(editListener.use(2));
        showC.addTextChangedListener(editListener.use(3));

        save.setOnClickListener(this);
    }

    @Override
    protected void initObserveFourth() {
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == save.getId()) {
            try {
                Result res = charAddVM.saveCharacter(tableFlag);
            } catch (PinyinException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //前后按键不能超过2秒，否则进行友好提示
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - clickTime > 2000) {
                XToastUtils.error("编辑内容未保存，再按返回键将退出！", 500);
                clickTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class IVListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            PictureSelector.create(CharAddActivity.this).openGallery(SelectMimeType.ofImage()).setImageEngine(GlideEngine.createGlideEngine()).setMaxSelectNum(1).forResult(new OnResultCallbackListener<LocalMedia>() {
                @Override
                public void onResult(ArrayList<LocalMedia> result) {
                    for (LocalMedia l : result) {
                        charAddVM.character.setCharProfile(l.getPath());
                        RequestOptions options = RequestOptions.circleCropTransform();
                        Glide.with(CharAddActivity.this).load(l.getPath()).apply(options).into(profileIV);
                    }
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    private class EditListener extends TextWatcherAdapter {
        private int FLAG;

        public EditListener use(int flag) {
            this.FLAG = flag;
            return this;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (FLAG) {
                case 1:
                    charAddVM.character.setCharShowA(editable.toString());
                    break;
                case 2:
                    charAddVM.character.setCharShowB(editable.toString());
                    break;
                case 3:
                    charAddVM.character.setCharShowC(editable.toString());
                    break;
                default:
                    break;
            }
        }
    }

}
