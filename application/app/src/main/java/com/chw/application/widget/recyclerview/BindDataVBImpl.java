package com.chw.application.widget.recyclerview;

import androidx.annotation.NonNull;

import java.util.List;

public abstract class BindDataVBImpl<T> implements BindDataVB<T> {

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position, @NonNull List<Object> payloads) {
        return;
    }
}
