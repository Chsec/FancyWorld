package com.chw.application.viewmodel.character;

import androidx.lifecycle.LiveData;
import com.chw.application.App;
import com.chw.application.model.bean.Result;
import com.chw.application.model.repository.TableTemplateRepo;
import com.chw.application.model.table.TableTemplate;
import com.chw.application.viewmodel.BaseVM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharTableVM extends BaseVM {
    /**
     * 原始字段模板数据缓存，保存原始数据。
     */
    public final List<TableTemplate> originalCache = new ArrayList<>();

    /**
     * 原始字段模板数据容器，保存用户输入。
     */
    public final List<TableTemplate> originalContainer = new ArrayList<>();

    /**
     * 自定义字段模板数据缓存，保存原始数据。
     */
    public final List<TableTemplate> customCache = new ArrayList<>();

    /**
     * 最新表模板种类存储容器
     */
    public final List<String> tableSortCache = new ArrayList<>();

    /**
     * 原始字段模板拥有者
     */
    private final String originalOwner = App.TABLE_OWNER[2];

    /**
     * 自定义字段模板拥有者
     */
    private final String customOwner = App.TABLE_OWNER[0];

    /**
     * 当前表模板名字，由更改模版选择器者维护。
     * 1、当表模板种类数据发生变化时（新建、删除、重命名），保证表模板选择器选中项为合适的item
     */
    public String PRESENT_TABLE;

    /**
     * 页面编辑状态（true）与浏览状态（false）标志。
     */
    public boolean PAGE_STATUS = false;

    public CharTableVM() {
    }

    public LiveData<List<String>> getTableSortLD() {
        return TableTemplateRepo.getTablesNameLD(customOwner);
    }

    /**
     * 获取tempName表模板数据及初始化原始字段容器。
     * 结果不为空，数据库内已初始化默认表模板。
     *
     * @return 表模板数据集合
     */
    public List<TableTemplate> getTemplateData() {
        customCache.clear();
        originalCache.clear();
        originalContainer.clear();
        List<TableTemplate> originalTemplate = TableTemplateRepo.getTableByName(PRESENT_TABLE, originalOwner);
        List<TableTemplate> tableTemplate = TableTemplateRepo.getTableByName(PRESENT_TABLE, customOwner);

        long tableFlag = tableTemplate.get(0).getTableFlag();
        if (originalTemplate == null || originalTemplate.isEmpty()) {
            originalContainer.add(new TableTemplate("", 0, originalOwner, tableFlag, PRESENT_TABLE));
            originalContainer.add(new TableTemplate("", 1, originalOwner, tableFlag, PRESENT_TABLE));
            originalContainer.add(new TableTemplate("", 2, originalOwner, tableFlag, PRESENT_TABLE));
        } else {
            Collections.sort(originalTemplate);
            deepCopy(originalContainer, originalTemplate);
        }
        Collections.sort(tableTemplate);
        deepCopy(tableTemplate, customCache);
        deepCopy(originalContainer, originalCache);
        return tableTemplate;
    }

    /**
     * 集合深度拷贝
     */
    private void deepCopy(List<TableTemplate> source, List<TableTemplate> target) {
        for (TableTemplate t : source) {
            target.add(t.clone());
        }
    }

    /**
     * 重置原始字段容器为未更改时状态
     */
    public void resetOriginalContainer() {
        originalContainer.clear();
        deepCopy(originalCache, originalContainer);
    }

    /**
     * 新建表模板，表模板默认存在一条数据
     *
     * @return 返回false已存在，返回true表模板创建成功
     */
    public Result newTemplate(String tempName) {
        if (tableSortCache.contains(tempName)) {
            return new Result(false, "当前模板已存在");
        } else {
            TableTemplate entity = new TableTemplate("", 0, customOwner, tempName);
            Long id = TableTemplateRepo.create(entity);
            entity.setTableFlag(id);
            TableTemplateRepo.create(entity);
            PRESENT_TABLE = tempName;
            return new Result(true, "新建模板成功");
        }
    }

    /**
     * 重命名表模板名称,重命名后更新PRESENT_TEMPLATE
     *
     * @param newName 新表模板名称
     * @return 重命名结果
     */
    public Result reNameTemp(String newName) {
        if (tableSortCache.contains(newName)) {
            return new Result(false, "当前模板已存在");
        }
        PRESENT_TABLE = newName;
        TableTemplateRepo.reNameTable(PRESENT_TABLE, newName, customOwner);
        TableTemplateRepo.reNameTable(PRESENT_TABLE, newName, originalOwner);
        return new Result(true, "已重命名为【" + newName + "】");
    }

    /**
     * 删除Spinner当前选中表模板
     *
     * @return ResultInformation的resultState表示能否删除，resultTip提示信息。
     */
    public Result delTemplate() {
        boolean flag = TableTemplateRepo.isDeleteTable(PRESENT_TABLE, customOwner);
        if (flag) {
            TableTemplateRepo.cleanTable(PRESENT_TABLE, customOwner);
            TableTemplateRepo.cleanTable(PRESENT_TABLE, originalOwner);
            return new Result(true, PRESENT_TABLE + "删除成功");
        } else {
            return new Result(false, "模板使用中，不可删除");
        }
    }

    /**
     * 保存表模板数据,并同步表模板项的顺序字段值
     *
     * @param datas 最新模板数据
     */
    public void saveTempData(ArrayList<TableTemplate> datas) {
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setItemOrder(i);
        }
        datas.addAll(originalContainer);
        TableTemplateRepo.create(datas);
    }

}
