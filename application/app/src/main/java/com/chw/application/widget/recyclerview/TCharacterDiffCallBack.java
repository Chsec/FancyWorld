package com.chw.application.widget.recyclerview;

import androidx.recyclerview.widget.DiffUtil;
import com.chw.application.model.table.TCharacter;

import java.util.List;
import java.util.Objects;

public class TCharacterDiffCallBack extends DiffUtil.Callback {

    private final List<TCharacter> oldData;

    private final List<TCharacter> newData;

    public TCharacterDiffCallBack(List<TCharacter> oldData, List<TCharacter> newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    /**
     * 判断旧数据集中的某个元素和新数据集中的某个元素是否代表同一个实体
     */
    @Override
    public int getOldListSize() {
        return oldData == null ? 0 : oldData.size();
    }

    @Override
    public int getNewListSize() {
        return newData == null ? 0 : newData.size();
    }

    /**
     * 判断旧数据集中的某个元素和新数据集中的某个元素是否代表同一个实体
     *
     * @param oldItemPosition item在旧列表中的位置
     * @param newItemPosition item在新列表中的位置
     * @return true相同，false不同
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        if (getNewListSize() == 0 || getNewListSize() == 0) {
            return false;
        } else {
            return oldData.get(oldItemPosition).getCharId() == newData.get(newItemPosition).getCharId();
        }
    }

    /**
     * 判断旧数据集中的某个元素和新数据集中的某个元素的内容是否相同
     *
     * @param oldItemPosition item在旧列表中的位置
     * @param newItemPosition 替换旧列表项在新列表中的位置
     * @return true相同，false不同
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        TCharacter oldChar = oldData.get(oldItemPosition);
        TCharacter newChar = newData.get(newItemPosition);
        return Objects.equals(oldChar.getCharShowA(), newChar.getCharShowA())
                && Objects.equals(oldChar.getCharShowB(), newChar.getCharShowB())
                && Objects.equals(oldChar.getCharShowC(), newChar.getCharShowC())
                && Objects.equals(oldChar.getCharProfile(), newChar.getCharProfile());
    }

    /**
     * 获取旧数据集中的某个元素和新数据集中的某个元素之间的差异信息。
     * 如果这两个元素相同，但是内容发生改变，可以通过这个方法获取它们之间的差异信息，从而只更新需要改变的部分，减少不必要的更新操作。
     *
     * @param oldItemPosition item在旧列表中的位置
     * @param newItemPosition item在新列表中的位置
     * @return 发生改变的内容
     */
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        TCharacter oldChar = oldData.get(oldItemPosition);
        TCharacter newChar = newData.get(newItemPosition);
        if (!Objects.equals(oldChar.getCharProfile(), newChar.getCharProfile())) {
            return 1;
        }
        return null;
    }

}
