package com.chw.application;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import com.chw.application.model.bean.Result;
import com.chw.application.model.repository.CharacterRepo;
import com.chw.application.model.repository.ResourceRepo;
import com.chw.application.model.table.Resource;
import com.chw.application.model.table.TCharacter;
import com.chw.application.model.table.TableTemplate;
import com.chw.application.util.bus.StickLiveDataBus;
import com.chw.application.widget.AsyncThreadCallback;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AppDataCenter extends AndroidViewModel {

    /**
     * 数据库内所有的TCharacter数据缓存
     */
    public final ConcurrentHashMap<Long, List<TCharacter>> characterCache = new ConcurrentHashMap<>();

    /**
     * 数据库内所有的Resource数据缓存
     */
    public final ConcurrentHashMap<Long, List<Resource>> resourceCache = new ConcurrentHashMap<>();

    /**
     * CharacterFragment当前选中的模板
     */
    public final List<TableTemplate> preCharacterTemp = new ArrayList<>();

    /**
     * ResourceFragment当前选中的模板
     */
    public final List<TableTemplate> preResourceTemp = new ArrayList<>();

    private Observer<List<Resource>> resoOB;

    private LiveData<List<Resource>> resoLD;

    private LiveData<List<TCharacter>> charLD;

    private Observer<List<TCharacter>> charOB;


    public AppDataCenter(@NotNull Application application) {
        super(application);
    }

    public void initObserve() {
        //资源的事件观察者
        resoOB = datas -> App.getInstance()
                .getExecutor()
                .async(() -> {
                    resourceCache.clear();
                    for (Resource r : datas) {
                        if (resourceCache.containsKey(r.getTableFlag())) {
                            Objects.requireNonNull(resourceCache.get(r.getTableFlag())).add(r);
                        } else {
                            List<Resource> temp = new ArrayList<>();
                            temp.add(r);
                            resourceCache.put(r.getTableFlag(), temp);
                        }
                    }
                    StickLiveDataBus.get().getChannel(AppConfigCenter.RESOURCE_REFRESH).setValue("");
                    return new Result(true, "成功刷新Resource缓存数据");
                }, new AsyncThreadCallback());
        //角色的事件观察者
        charOB = datas -> App.getInstance()
                .getExecutor()
                .async(() -> {
                    characterCache.clear();
                    for (TCharacter t : datas) {
                        if (characterCache.containsKey(t.getTableFlag())) {
                            Objects.requireNonNull(characterCache.get(t.getTableFlag())).add(t);
                        } else {
                            List<TCharacter> temp = new ArrayList<>();
                            temp.add(t);
                            characterCache.put(t.getTableFlag(), temp);
                        }
                    }
                    StickLiveDataBus.get().getChannel(AppConfigCenter.CHARACTER_REFRESH).setValue("");
                    return new Result(true, "成功刷新TCharacter缓存数据");
                }, new AsyncThreadCallback());
        setObserve();
    }

    private void setObserve() {

        StickLiveDataBus.get()
                .getChannel(AppConfigCenter.BOOK, String.class)
                .observeForever(s -> {
                    if (resoLD != null) {
                        resoLD.removeObserver(resoOB);
                    }
                    if (charLD != null) {
                        charLD.removeObserver(charOB);
                    }
                    resoLD = ResourceRepo.getAllResourceLD();
                    resoLD.observeForever(resoOB);
                    charLD = CharacterRepo.getAllCharacterLD();
                    charLD.removeObserver(charOB);
                });
    }
}
