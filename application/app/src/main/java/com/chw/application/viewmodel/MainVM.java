package com.chw.application.viewmodel;

import androidx.lifecycle.LiveData;
import com.chw.application.model.table.TreeTemplate;
import com.chw.application.model.repository.TreeTemplateRepo;
import com.chw.application.viewmodel.BaseVM;

import java.util.ArrayList;
import java.util.List;

public class MainVM extends BaseVM {

    /**
     * 树组缓存容器
     */
    public static final ArrayList<TreeTemplate> treeGroupsItemCache = new ArrayList<>();

    public List<TreeTemplate> settingCont;

    public MainVM() {

    }

    /**
     * 根据缓存数据，获取树组名称。（可能为null）
     */
    public static String getGroupName(long groupFlag) {
        if (!treeGroupsItemCache.isEmpty()) {
            for (TreeTemplate t : treeGroupsItemCache) {
                if (t.getGroupFlag() == groupFlag) {
                    return t.getGroupName();
                }
            }
        }
        return null;
    }

    public LiveData<List<TreeTemplate>> treeGroupsItem() {
        return TreeTemplateRepo.getGroupsItemLD();
    }


}
