package com.chw.application.model.repository;

import androidx.lifecycle.LiveData;
import com.chw.application.App;
import com.chw.application.model.ApplicationDataBase;
import com.chw.application.model.dao.BookDao;
import com.chw.application.model.table.Book;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class BookEntityRepo extends BaseRepo {

    private static final BookDao bookDao = ApplicationDataBase.getInstance().bookDao();

    /**
     * 创建多条BookEntity数据,主键重复则覆盖。
     */
    public static void create(List<Book> datas) {
        executor.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                bookDao.insert(datas);
                return null;
            }
        });
    }

    /**
     * 创建单条BookEntity数据,并返回数据id（主键重复则覆盖）。
     */
    public static long create(Book data) {
        try {
            return executor.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return bookDao.inset(data);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取用户id所属的所有z对象
     */
    public static List<Book> getBooks(long userId) {
        try {
            return executor.submit(new Callable<List<Book>>() {
                @Override
                public List<Book> call() throws Exception {
                    return bookDao.findBooksByFlag(App.PRE_USER_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取用户id所属的所有z对象
     */
    public static LiveData<List<Book>> getBooksLD() {
        try {
            return executor.submit(new Callable<LiveData<List<Book>>>() {
                @Override
                public LiveData<List<Book>> call() throws Exception {
                    return bookDao.findBooksByFlagLD(App.PRE_USER_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
