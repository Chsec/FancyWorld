package com.chw.application.viewmodel.character;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import com.chw.application.App;
import com.chw.application.AppConfigCenter;
import com.chw.application.AppDataCenter;
import com.chw.application.comparator.TCharacterComparator;
import com.chw.application.comparator.TCharacterDigitComparator;
import com.chw.application.model.repository.CharacterRepo;
import com.chw.application.model.repository.TableTemplateRepo;
import com.chw.application.model.table.TCharacter;
import com.chw.application.model.table.TableTemplate;
import com.chw.application.viewmodel.BaseVM;
import com.chw.application.widget.AsyncThreadCallback;
import com.chw.application.widget.recyclerview.CommonAdapter;
import com.chw.application.widget.recyclerview.TCharacterDiffCallBack;

import java.util.*;

public class CharacterVM extends BaseVM {

    /**
     * Character类模板所有实体名称，由cacheData初始化
     */
    public final ArrayList<String> tempsNameCacheList = new ArrayList<>();

    /**
     * 页面多选模式下存储选中位置容器<对象id,对象在适配器中的索引>
     */
    public final HashMap<Integer, Integer> mulRecorder = new HashMap<>();

    /**
     * 模板种类标记（模板拥有者）
     */
    private final String tempOwner = App.TABLE_OWNER[0];

    /**
     * 当前页面表模板LiveData对象
     */
    public final LiveData<List<TableTemplate>> tableTempsLD = TableTemplateRepo.getTablesItemLD(tempOwner);

    /**
     * 当前页面表模板缓存
     */
    private final List<TableTemplate> tableTempsCache = new ArrayList<>();

    /**
     * RecyclerView多选模式标记
     */
    public boolean MUL_MARK = false;

    public CharacterVM() {
    }

    /**
     * 筛选栏一的初始化
     *
     * @param acc        应用配置中心引用
     * @param tableTemps 表模板集合
     * @return 筛选栏一的标题
     */
    public String filterBarInit(AppConfigCenter acc, List<TableTemplate> tableTemps) {
        TableTemplate entity = tableTemps.get(0);
        tableTempsCache.clear();
        tableTempsCache.addAll(tableTemps);

        String record = acc.getBookRecord(0);
        if (record != null && !record.isEmpty()) {
            //配置不为空时
            long tableFlag = Long.parseLong(record);
            for (TableTemplate t : tableTemps) {
                if (t.getTableFlag() == tableFlag) {
                    entity = t;
                    break;
                }
            }
        } else {
            //配置为空或模板被删除时
            acc.setBookRecord(0, String.valueOf(entity.getTableFlag()));
        }
        return entity.getTableName();
    }

    /**
     * 异步刷新RecyclerView列表
     *
     * @param adc       数据控制中心引用
     * @param adapter   RecyclerView适配器
     * @param tableName 待更新的表模板名称
     */
    public void asyncRefreshRV(AppDataCenter adc, CommonAdapter adapter, String tableName) {
        App.getInstance().getExecutor().async(() -> {
            long tableFlag = getTableTempFlag(tableName);

            //更新数据中心CharacterFragment的选中模板缓存
            adc.preCharacterTemp.clear();
            adc.preCharacterTemp.addAll(TableTemplateRepo.getTableByFlag(tableFlag));

            //更新RecyclerView列表数据
            List<TCharacter> characters = adc.characterCache.get(tableFlag);
            customSort(characters);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new TCharacterDiffCallBack(adapter.getMDatas(), characters), true);
            diffResult.dispatchUpdatesTo(adapter);
            return null;
        }, new AsyncThreadCallback());
    }

    public long getTableTempFlag(String tableName) {
        for (TableTemplate t : tableTempsCache) {
            if (Objects.equals(t.getTableName(), tableName)) {
                return t.getTableFlag();
            }
        }
        return tableTempsCache.get(0).getTableFlag();
    }

    /**
     * 置顶item操作
     *
     * @param characterBean 被置顶的数据
     */
    public void topItem(TCharacter characterBean) {
        CharacterRepo.createChar(characterBean);
    }

    /**
     * 单条数据删除
     */
    public void delItem(TCharacter charBean) {
        boolean delete = CharacterRepo.deleteChar(charBean);
    }

    /**
     * 多条数据删除
     */
    public void delItems(ArrayList<CharacterVM> charBeans) {

    }

    /**
     * 多选删除功能
     */
    public void delItems() {

    }



    /**
     * 使用自定义比较器进行CharacterBean数据集合排序
     *
     * @param datas 数据集合
     */
    public void customSort(List<TCharacter> datas) {
        Collections.sort(datas, new TCharacterComparator());
        Collections.sort(datas, new TCharacterDigitComparator());
    }

}
