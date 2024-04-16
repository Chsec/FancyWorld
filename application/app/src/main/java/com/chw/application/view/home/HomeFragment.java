package com.chw.application.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.lifecycle.ViewModelProvider;
import com.chw.application.App;
import com.chw.application.AppConfigCenter;
import com.chw.application.databinding.FragmentHomeBinding;
import com.chw.application.model.repository.BookEntityRepo;
import com.chw.application.model.table.Book;
import com.chw.application.util.FileSelector;
import com.chw.application.util.bus.StickLiveDataBus;
import com.chw.application.view.BaseFragment;
import com.chw.application.view.home.setter.SetterActivity;
import com.chw.application.viewmodel.home.HomeVM;
import com.molihuan.pathselector.service.IConfigDataBuilder;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    private ImageButton setter;

    private ImageView profile;

    private MaterialSpinner books;

    private HomeVM homeVM;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FragmentHomeBinding viewBinding() {

        return FragmentHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        return viewBinding.getRoot();
    }

    @Override
    protected void initPrepareFirst() {

        profile = viewBinding.profileImage;

        books = viewBinding.book;
        setter = viewBinding.setter;

        homeVM = new ViewModelProvider(this).get(HomeVM.class);
    }

    @Override
    protected void initViewSecond() {

    }

    @Override
    protected void initListenerThird() {

        profile.setOnClickListener(this);
        setter.setOnClickListener(this);
        books.setOnItemSelectedListener(new BooksListener());
    }

    @Override
    protected void initObserveFourth() {

        StickLiveDataBus.get().getChannel(AppConfigCenter.USER_CHANGE).observe(this, s -> {
            BookEntityRepo.getBooksLD().observe(this, bookBeans -> {
                homeVM.bookCache = bookBeans;
                books.setItems(bookBeans);
            });
        });
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == setter.getId()) {
            startActivity(new Intent(requireContext(), SetterActivity.class));
        } else if (id == books.getId()) {
            StickLiveDataBus.get().getChannel("").setValue("");
        } else if (id == profile.getId()) {
            FileSelector fs = new FileSelector(requireContext(), FileSelector.DIALOG_MODEL);
            IConfigDataBuilder selector = fs.getSelector();
            selector.show();
        }
    }

    private class BooksListener implements MaterialSpinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
            for (Book b : homeVM.bookCache) {
                if (Objects.equals(b.getBookName(), item.toString())) {
                    App.PRE_BOOK_ID = b.getBookId();
                    StickLiveDataBus.get().getChannel(AppConfigCenter.BOOK).setValue("");
                }
            }
        }
    }
}