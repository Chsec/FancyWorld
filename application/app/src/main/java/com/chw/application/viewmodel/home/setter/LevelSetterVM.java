package com.chw.application.viewmodel.home.setter;

import androidx.lifecycle.LiveData;
import com.chw.application.AppConfigCenter;
import com.chw.application.model.table.Setting;
import com.chw.application.model.table.SettingSort;
import com.chw.application.model.repository.SetterRepo;
import com.chw.application.viewmodel.BaseVM;

import java.util.ArrayList;
import java.util.List;

public class LevelSetterVM extends BaseVM {

    /**
     * 等级设定器名称
     */
    public final String SETTER = AppConfigCenter.setterList.get(0);

    /**
     * 页面当前选中种类
     */
    public long SELECT_SORT = -1;

    /**
     * 等级设定器所有设定
     */
    public List<Setting> settingList;

    /**
     * 等级设定器设定种类
     */
    public List<SettingSort> settingSortList;

    public LevelSetterVM() {

    }

    /**
     * 获取等级设定器设定种类LiveData
     */
    public LiveData<List<SettingSort>> getSettingSortList() {
        return SetterRepo.getSettingSort(SETTER);
    }

    /**
     * 获取等级设定器设定LiveData
     */
    public LiveData<List<Setting>> getSetting() {
        return SetterRepo.getSetting(SETTER);
    }

    /**
     * 根据设定种类处理设定
     */
    public ArrayList<Setting> parseSetting(long sortFlag) {
        ArrayList<Setting> temp = new ArrayList<>();
        for (Setting s : settingList) {
            if (s.getSettingSort() == sortFlag) {
                temp.add(s);
            }
        }
        return temp;
    }

}
