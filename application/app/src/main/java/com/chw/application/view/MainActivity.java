package com.chw.application.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.chw.application.AppConfigCenter;
import com.chw.application.R;
import com.chw.application.databinding.ActivityMainBinding;
import com.chw.application.util.bus.StickLiveDataBus;
import com.chw.application.view.character.CharTableActivity;
import com.chw.application.view.character.CharacterFragment;
import com.chw.application.view.home.HomeFragment;
import com.chw.application.view.nation.NationFragment;
import com.chw.application.view.organization.OrganizationFragment;
import com.chw.application.view.resource.ResoTableActivity;
import com.chw.application.view.resource.ResourceFragment;
import com.chw.application.viewmodel.MainVM;
import com.chw.application.widget.fragment.ViewPaper2FragmentAdapter;

import java.util.ArrayList;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private final ArrayList<Fragment> fragments = new ArrayList<>();

    public MainVM mainVM;

    private ActivityMainBinding binding;

    private ViewPager2 vp;

    private RadioGroup radioGroup;

    private Toolbar toolbar;

    private ViewPaper2FragmentAdapter fgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ActivityMainBinding viewBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    protected void initPrepareFirst() {
        // 初始化视图变量
        radioGroup = binding.activityRadioMain;
        vp = binding.activityViewpagerMain;
        toolbar = binding.activityInclude.toolbar;
        //将fragment组装进容器
        fragments.add(CharacterFragment.newInstance());
        fragments.add(ResourceFragment.newInstance());
        fragments.add(HomeFragment.newInstance());
        fragments.add(NationFragment.newInstance());
        fragments.add(OrganizationFragment.newInstance());

        mainVM = new ViewModelProvider(this).get(MainVM.class);
    }

    @Override
    protected void initViewSecond() {
        // 初始化ViewPaper2
        fgAdapter = new ViewPaper2FragmentAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        vp.setAdapter(fgAdapter);
        vp.setCurrentItem(2, false);// 无动画加载ViewPaper2第三页
        // 初始化toolbar
        setCurrentPageMenu(vp.getCurrentItem());
    }

    @Override
    protected void initListenerThird() {
        // ViewPaper2滑动事件
        vp.registerOnPageChangeCallback(new ViewPaperListener());
        // 设置ViewPaper2内的Fragment跟随RadioGroup点击翻页。
        radioGroup.setOnCheckedChangeListener(new RadioGroupListener());
        toolbar.setOnMenuItemClickListener(new ToolbarListener());
    }

    @Override
    protected void initObserveFourth() {
        //通知应用初始化数据
        StickLiveDataBus.get().getChannel(AppConfigCenter.BOOK_CHANGE, String.class).setValue("");
        //树组缓存
        StickLiveDataBus.get()
                .getChannel(AppConfigCenter.BOOK_CHANGE, String.class)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        mainVM.treeGroupsItem().removeObservers(MainActivity.this);
                        mainVM.treeGroupsItem().observe(MainActivity.this, datas -> {
                            MainVM.treeGroupsItemCache.clear();
                            MainVM.treeGroupsItemCache.addAll(datas);
                        });
                    }
                });
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * 设置不同页面的toolbar
     *
     * @param index 页面索引
     */
    private void setCurrentPageMenu(int index) {
        if (index > 4) {
            throw new IndexOutOfBoundsException("菜单索引越界");
        }
        toolbar.getMenu().clear();
        switch (index) {
            case 0:
                toolbar.inflateMenu(R.menu.menu_character);
                toolbar.setOverflowIcon(AppCompatResources.getDrawable(this, R.drawable.vector_service));
                break;
            case 1:
                toolbar.inflateMenu(R.menu.menu_resource);
                break;
            case 2:
                toolbar.inflateMenu(R.menu.menu_home);
                break;
            case 3:
            case 4:
                break;
        }
    }

    /**
     * ViewPaper滑动翻页监听
     */
    private class ViewPaperListener extends ViewPager2.OnPageChangeCallback {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    radioGroup.check(binding.activityRb1.getId());
                    break;
                case 1:
                    radioGroup.check(binding.activityRb2.getId());
                    break;
                case 2:
                    radioGroup.check(binding.activityRb3.getId());
                    break;
                case 3:
                    radioGroup.check(binding.activityRb4.getId());
                    break;
                case 4:
                    radioGroup.check(binding.activityRb5.getId());
                    break;
                default:
                    break;
            }
            setCurrentPageMenu(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    }

    /**
     * 底部导航栏监听
     */
    private class RadioGroupListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                if (radioGroup.getChildAt(j).getId() == i) {
                    vp.setCurrentItem(j, false);
                    setCurrentPageMenu(j);
                }
            }
        }
    }

    /**
     * 工具栏点击事件监听
     */
    private class ToolbarListener implements Toolbar.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_character_search) {

            } else if (itemId == R.id.new_character_template) {
                // 添加character模板页面
                startActivity(new Intent(MainActivity.this, CharTableActivity.class));
            } else if (itemId == R.id.new_resource_template) {
                // 添加resource模板页面
                startActivity(new Intent(MainActivity.this, ResoTableActivity.class));
            } else if (itemId == R.id.menu_change_user) {

            } else if (itemId == R.id.menu_exit_login) {

            } else if (itemId == R.id.menu_software_lock) {

            }
            return true;
        }
    }

}