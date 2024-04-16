package com.chw.application.widget.recyclerview;

import androidx.annotation.NonNull;

import java.util.List;

public abstract class BindDataImpl<T> implements BindData<T> {
    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position, @NonNull List<Object> payloads) {
        return;
    }
}
