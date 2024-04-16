package com.chw.application.viewmodel.home.setter;

import com.chw.application.App;
import com.chw.application.R;
import com.chw.application.model.bean.Result;
import com.chw.application.model.table.TableTemplate;
import com.chw.application.model.repository.TableTemplateRepo;
import com.chw.application.widget.recyclerview.CommonAdapter;
import com.chw.application.viewmodel.BaseVM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelTableVM extends BaseVM {

    private final String templateOwner;

    public String PRESENT_TEMPLATE = "";

    public List<TableTemplate> templateDate = new ArrayList<>();

    public LevelTableVM() {
        templateOwner = App.getInstance().getResources().getStringArray(R.array.table_template_owner)[4];
    }

    /**
     * 获取tempName表模板数据。
     * 结果不为空，数据库内已初始化默认表模板。
     *
     * @return 表模板数据集合
     */
    public List<TableTemplate> getTemplateData() {
        List<TableTemplate> tableTemplates = TableTemplateRepo.getTableByName(PRESENT_TEMPLATE, templateOwner);
        List<TableTemplate> temp = new ArrayList<>();
        if (tableTemplates == null || tableTemplates.isEmpty()) {
//            temp.add(new TableTemplate(PRESENT_TEMPLATE, templateOwner, 0));
        } else {
            temp.addAll(tableTemplates);
        }
        Collections.sort(temp);
        templateDate.clear();
        templateDate.addAll(temp);
        return temp;
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
        TableTemplateRepo.create(datas);
    }

    /**
     * 添加表模板条目，直接操作适配器数据
     *
     * @param adapter  适配器
     * @param position 添加项在适配器中的位置
     */
//    public void addTemplateItem(CommonAdapter<TableTemplate> adapter, int position) {
//        TableTemplate defaultItem = new TableTemplate(PRESENT_TEMPLATE, templateOwner, 0);
//        adapter.insert(defaultItem, position);
//    }

    /**
     * 删除表模板条目，直接操作适配器数据
     * 1、表模板唯一字段不能删除
     * 2、字段被引用时不能删除
     *
     * @param adapter  适配器
     * @param position 删除项在适配器中的位置
     * @return 删除结果提示[状态，状态提示]
     */
    public Result delTemplateItem(CommonAdapter<TableTemplate> adapter, int position) {
        TableTemplate tableTemplate = adapter.getMDatas().get(position);
        if (adapter.getMDatas().size() == 1) {
            return new Result(false, "模板唯一字段，禁止删除");
        } else if (tableTemplate.getItemRefer() != 0) {
            return new Result(false, "模板字段在用，禁止删除");
        } else {
            adapter.remove(position);
            return new Result(true, "字段删除成功");
        }
    }
}
