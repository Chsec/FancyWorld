package com.chw.application.viewmodel.resource;

import androidx.lifecycle.LiveData;
import com.chw.application.App;
import com.chw.application.model.bean.Result;
import com.chw.application.model.table.TableTemplate;
import com.chw.application.model.repository.TableTemplateRepo;
import com.chw.application.viewmodel.BaseVM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResoTablVM extends BaseVM {

    /**
     * 原始字段的表模板容器
     */
    public final ArrayList<TableTemplate> otArraylist = new ArrayList<>();

    /**
     * 原始字段的表模板容器副本
     */
    public final ArrayList<TableTemplate> otArraylistCache = new ArrayList<>();

    /**
     * 最新表模板种类存储容器
     */
    public final List<String> templateSort = new ArrayList<>();

    /**
     * 表模板种类的观察者对象
     */
    public final LiveData<List<String>> templateSortLD;

    /**
     * 表模板原始字段拥有者
     */
    public final String originalOwner;

    /**
     * 表模板拥有者
     */
    private final String templateOwner;

    /**
     * 模板数据副本
     */
    public List<TableTemplate> templateDate = new ArrayList<>();

    /**
     * 当前表模板名字，由更改模版选择器者维护。
     * 1、当表模板种类数据发生变化时（新建、删除、重命名），保证表模板选择器选中项为合适的item
     */
    public String PRESENT_TEMPLATE;

    /**
     * 页面编辑状态（true）与浏览状态（false）标志。
     */
    public boolean PAGE_STATUS = false;

    public ResoTablVM() {
        originalOwner = App.TABLE_OWNER[3];
        templateOwner = App.TABLE_OWNER[1];
        templateSortLD = TableTemplateRepo.getTablesNameLD(templateOwner);
    }

    /**
     * 获取页面数据（包括原始字段与自定义字段）
     * 结果不为空，数据库内已初始化默认表模板。
     *
     * @return 表模板数据集合
     */
    public List<TableTemplate> getPageData() {
        List<TableTemplate> tableTemplates = TableTemplateRepo.getTableByName(PRESENT_TEMPLATE, templateOwner);
        initOTArrayList(tableTemplates.get(0).getTableFlag());
        Collections.sort(tableTemplates);
        templateDate.clear();
        templateDate.addAll(tableTemplates);
        return tableTemplates;
    }

    /**
     * 初始化原始字段的表模板容器。
     * 1、数据库中已存在则使用数据库内容，不存在则初始化默认字段容器。
     * 2、依赖于PRESENT_TEMPLATE，调用前需保证PRESENT_TEMPLATE已经初始化。
     */
    public void initOTArrayList(long tempFlag) {
        List<TableTemplate> originals = TableTemplateRepo.getTableByName(PRESENT_TEMPLATE, originalOwner);
        otArraylistCache.clear();
        if (originals == null || originals.isEmpty()) {
            newOTArrayList(tempFlag);
        } else {
            otArraylist.clear();
            otArraylist.addAll(originals);
        }
        otArraylistCache.addAll(otArraylist);
    }

    /**
     * 新建原始字段容器
     */
    public void newOTArrayList(long tempFlag) {
        otArraylist.clear();
        otArraylist.add(new TableTemplate("", 0, originalOwner, tempFlag, PRESENT_TEMPLATE));
        otArraylist.add(new TableTemplate("", 1, originalOwner, tempFlag, PRESENT_TEMPLATE));
        otArraylist.add(new TableTemplate("", 2, originalOwner, tempFlag, PRESENT_TEMPLATE));
        otArraylist.add(new TableTemplate("", 3, originalOwner, tempFlag, PRESENT_TEMPLATE));
        otArraylist.add(new TableTemplate("", 4, originalOwner, tempFlag, PRESENT_TEMPLATE));
    }

    /**
     * 重置原始字段容器
     */
    public void resetOTArrayList() {
        otArraylist.clear();
        otArraylist.addAll(otArraylistCache);
    }

    /**
     * 新建表模板，表模板默认存在一条数据
     *
     * @return 返回false已存在，返回true表模板创建成功
     */
    public Result newTemplate(String tempName) {
        if (templateSort.contains(tempName)) {
            return new Result(false, "当前模板已存在");
        } else {
            TableTemplate entity = new TableTemplate("", 0, templateOwner, tempName);
            Long id = TableTemplateRepo.create(entity);
            entity.setTableFlag(id);
            TableTemplateRepo.create(entity);
            PRESENT_TEMPLATE = tempName;
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
        if (templateSort.contains(newName)) {
            return new Result(false, "当前模板已存在");
        }
        PRESENT_TEMPLATE = newName;
        TableTemplateRepo.reNameTable(PRESENT_TEMPLATE, newName, templateOwner);
        TableTemplateRepo.reNameTable(PRESENT_TEMPLATE, newName, originalOwner);
        return new Result(true, "已重命名为【" + newName + "】");
    }

    /**
     * 删除Spinner当前选中表模板
     *
     * @return ResultInformation的resultState表示能否删除，resultTip提示信息。
     */
    public Result delTemplate() {
        boolean flag = TableTemplateRepo.isDeleteTable(PRESENT_TEMPLATE, templateOwner);
        if (flag) {
            TableTemplateRepo.cleanTable(PRESENT_TEMPLATE, templateOwner);
            TableTemplateRepo.cleanTable(PRESENT_TEMPLATE, originalOwner);
            return new Result(true, PRESENT_TEMPLATE + "删除成功");
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
        datas.addAll(otArraylist);
        TableTemplateRepo.create(datas);
    }

}
