package com.chw.application.viewmodel.home;

import androidx.lifecycle.LiveData;
import com.chw.application.App;
import com.chw.application.model.repository.BookEntityRepo;
import com.chw.application.model.table.Book;
import com.chw.application.viewmodel.BaseVM;

import java.util.List;

public class HomeVM extends BaseVM {

    public List<Book> bookCache;


    public LiveData<List<Book>> refreshBooksCont() {
        return BookEntityRepo.getBooks(App.PRE_USER_ID);
    }
}
