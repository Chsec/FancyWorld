package com.chw.application.viewmodel.character;

import com.alibaba.fastjson.JSON;
import com.chw.application.App;
import com.chw.application.model.bean.Result;
import com.chw.application.model.bean.TableTransformer;
import com.chw.application.model.repository.CharacterRepo;
import com.chw.application.model.repository.TableTemplateRepo;
import com.chw.application.model.table.TCharacter;
import com.chw.application.model.table.TCharacterData;
import com.chw.application.model.table.TableTemplate;
import com.chw.application.util.FileEncryptUtils;
import com.chw.application.util.PinyinUtils;
import com.chw.application.util.StrUtil;
import com.chw.application.viewmodel.BaseVM;
import com.github.stuxuhai.jpinyin.PinyinException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CharAddVM extends BaseVM {
    /**
     * 用户自定义字段模板的数据保存容器
     */
    public final List<TableTransformer> customDataContainer = new ArrayList<>();

    /**
     * Character数据容器
     */
    public final TCharacter character = new TCharacter();

    /**
     * 原始字段模板数据缓存
     */
    public final List<TableTemplate> originalTempCache = new ArrayList<>();

    /**
     * 自定义字段模板拥有者
     */
    private final String CUSTOM_OWNER = App.TABLE_OWNER[0];

    /**
     * 原始字段模板拥有者
     */
    private final String ORIGINAL_OWNER = App.TABLE_OWNER[2];

    public CharAddVM() {
    }

    /**
     * 解析模板缓存,并初始化原始字段模板容器
     *
     * @param datas 数据缓存集合
     */
    public List<TableTemplate> parseTableTemplateCache(List<TableTemplate> datas) {
        List<TableTemplate> temp = new ArrayList<>();
        originalTempCache.clear();
        for (TableTemplate t : datas) {
            if (Objects.equals(t.getTableOwner(), CUSTOM_OWNER)) {
                temp.add(t);
            } else if (Objects.equals(t.getTableOwner(), ORIGINAL_OWNER)) {
                originalTempCache.add(t);
                Collections.sort(originalTempCache);
            }
        }
        Collections.sort(temp);
        for (TableTemplate t : temp) {
            customDataContainer.add(new TableTransformer(t.getItemId(), "", t.isItemFill()));
        }
        return temp;
    }

    /**
     * 保存数据时进行检测，返回true可以保存，返回false不可以保存
     */
    private boolean saveCheck() {
        if (character.getCharShowA() == null || character.getCharShowA().isEmpty()) {
            return false;
        }
        for (TableTemplate t : originalTempCache) {
            if (t.isItemFill()) {
                switch (t.getItemOrder()) {
                    case 1:
                        if (character.getCharShowB() == null || character.getCharShowB().isEmpty()) {
                            return false;
                        }
                        break;
                    case 2:
                        if (character.getCharShowC() == null || character.getCharShowC().isEmpty()) {
                            return false;
                        }
                        break;
                }
            }
        }
        for (TableTransformer t : customDataContainer) {
            if (t.isFill()) {
                if (t.getValue() == null || t.getValue().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 保存数据
     *
     * @return Result
     */
    public Result saveCharacter(Long tableFlag) throws PinyinException {
        // 入库前进行检查
        if (saveCheck()) {
            // 数据转换
            String dataContent = JSON.toJSONString(customDataContainer);

            // 保存时移动图片至应用缓存目录，并对内容进行混淆
            String newPath = "";
            if (!character.getCharProfile().isEmpty()) {
                String profileDir = App.getInstance().getAOC().getProfileDir().getAbsolutePath();
                newPath = profileDir + StrUtil.generalNam();
                FileEncryptUtils.fileMove(character.getCharProfile(), newPath);
            }
            // 处理排序字段
            String letter = PinyinUtils.getSortLetter(character.getCharShowA());
            // 组装bean
            character.setCharGroup("");
            character.setCharState("");
            character.setCharTop("");
            character.setCharLetter(letter);
            character.setCharRefer(0);
            character.setCharProfile(newPath);
            character.setTableFlag(tableFlag);
            Long id = CharacterRepo.createChar(character);
            CharacterRepo.createData(new TCharacterData(id, dataContent));
            // 数据保存完毕，重置容器状态
            tempItemReferSync();
            return new Result(true, "保存成功");
        } else {
            return new Result(false, "首字段为必填字段，请输入内容后进行保存");
        }
    }

    /**
     * 更新模板项引用字段值,控制不加引用
     */
    private void tempItemReferSync() {
        ArrayList<Long> temp = new ArrayList<>();
        for (TableTransformer t : customDataContainer) {
            if (t.isFill()) temp.add(t.getId());
        }
        TableTemplateRepo.syncTableItemRefer(temp, true);
    }

}
