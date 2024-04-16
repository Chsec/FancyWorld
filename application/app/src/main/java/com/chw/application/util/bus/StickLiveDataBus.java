package com.chw.application.util.bus;

import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

/**
 * 粘性LivedataBus
 */
public class StickLiveDataBus {

    private final Map<String, MutableLiveData<Object>> bus;

    private StickLiveDataBus() {
        bus = new HashMap<>();
    }

    public static StickLiveDataBus get() {
        return SingletonHolder.DATA_BUS;
    }

    public <T> MutableLiveData<T> getChannel(String target, Class<T> type) {
        if (!bus.containsKey(target)) {
            bus.put(target, new MutableLiveData<>());
        }
        return (MutableLiveData<T>) bus.get(target);
    }

    public MutableLiveData<Object> getChannel(String target) {
        return getChannel(target, Object.class);
    }

    private static class SingletonHolder {
        private static final StickLiveDataBus DATA_BUS = new StickLiveDataBus();
    }
}
