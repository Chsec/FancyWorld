package com.chw.application.widget.recyclerview;

import androidx.recyclerview.widget.DiffUtil;
import com.chw.application.model.table.Resource;

import java.util.List;

public class ResourceDiffCallBack extends DiffUtil.Callback {

    private final List<Resource> oldData;

    private final List<Resource> newData;

    public ResourceDiffCallBack(List<Resource> oldData, List<Resource> newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public int getOldListSize() {
        return oldData == null ? 0 : oldData.size();
    }

    @Override
    public int getNewListSize() {
        return newData == null ? 0 : newData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
