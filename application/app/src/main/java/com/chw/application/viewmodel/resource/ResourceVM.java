package com.chw.application.viewmodel.resource;

import androidx.lifecycle.LiveData;
import com.chw.application.App;
import com.chw.application.model.repository.TableTemplateRepo;
import com.chw.application.model.table.Resource;
import com.chw.application.model.table.TableTemplate;
import com.chw.application.viewmodel.BaseVM;

import java.util.ArrayList;
import java.util.List;

public class ResourceVM extends BaseVM {

    /**
     * 资源种类缓存
     */
    public final List<TableTemplate> resoSortCache = new ArrayList<>();

    /**
     * TableTemplate表中当前功能类所有模板实体名称集合
     */
    public final ArrayList<String> tempNamesCache;

    /**
     * 页面当前模板数据类型
     */
    private final String tableOwner = App.TABLE_OWNER[1];

    /**
     * 页面当前原始字段数据类型
     */
    private final String originalOwner = App.TABLE_OWNER[3];

    /**
     * 次级联动列表数据Livedata对象
     */
    public LiveData<List<Resource>> secondaryDataLD;

    /**
     * 当前选中种类的标志
     */
    public long preSelectedSortFlag = -1;

    /**
     * 当前选中种类的索引
     */
    public int preSelectedSortIndex;

    /**
     * 资源种类LiveData对象
     */
    public LiveData<List<TableTemplate>> resoSortLD;

    public ResourceVM() {
        tempNamesCache = new ArrayList<>();
    }

    /**
     * 获取资源种类LiveData对象
     */
    public LiveData<List<TableTemplate>> getResoSortLD() {
        resoSortLD = TableTemplateRepo.getTablesItemLD(tableOwner);
        return resoSortLD;
    }

    /**
     * 缓存必要数据
     */
    public List<String> cacheData(List<TableTemplate> datas) {
        resoSortCache.clear();
        resoSortCache.addAll(datas);

        tempNamesCache.clear();
        for (TableTemplate t : datas) {
            tempNamesCache.add(t.getTableName());
        }
        return tempNamesCache;
    }

}
