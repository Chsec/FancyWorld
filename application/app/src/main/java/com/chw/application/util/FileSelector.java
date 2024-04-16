package com.chw.application.util;

import android.content.Context;
import android.view.View;
import com.molihuan.pathselector.PathSelector;
import com.molihuan.pathselector.entity.FileBean;
import com.molihuan.pathselector.fragment.BasePathSelectFragment;
import com.molihuan.pathselector.listener.CommonItemListener;
import com.molihuan.pathselector.service.IConfigDataBuilder;
import com.molihuan.pathselector.utils.MConstants;
import com.molihuan.pathselector.utils.Mtools;

import java.util.List;

public class FileSelector {

    // activity构建模式
    private final int ACTIVITY_MODEL = MConstants.BUILD_ACTIVITY;
    // fragment构建模式
    private final int FRAGMENT_MODEL = MConstants.BUILD_FRAGMENT;
    // dialog构建模式
    public static final int DIALOG_MODEL = MConstants.BUILD_DIALOG;
    private Context context;

    private IConfigDataBuilder iConfigDataBuilder;

    /**
     * <a href="https://gitee.com/molihuan/mlhfileselectorlib/tree/maste">项目官网</a>
     *
     * @param context 选择器挂载的位置
     * @param model 创建的选择器类型
     */
    public FileSelector(Context context, int model) {
        this.context = context;
        //如果没有权限会自动申请权限
        iConfigDataBuilder = PathSelector.build(context, model);
    }

    public void buildSelector(){
        iConfigDataBuilder.setRequestCode(635)
                .setMorePopupItemListeners(
                        new CommonItemListener("OK") {
                            @Override
                            public boolean onClick(View v, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                                StringBuilder builder = new StringBuilder();
                                builder.append("you selected:\n");
                                for (FileBean fileBean : selectedFiles) {
                                    builder.append(fileBean.getPath() + "\n");
                                }
                                Mtools.toast(builder.toString());

                                return false;
                            }
                        }
                );
    }


    public IConfigDataBuilder getSelector() {
        buildSelector();
        return iConfigDataBuilder;
    }

}
