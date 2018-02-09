package com.example.administrator.books.factory;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.administrator.books.Constance;
import com.example.administrator.books.DbConstance;
import com.example.administrator.books.DbTools;
import com.example.administrator.books.activities.MainActivity;
import com.example.administrator.books.model.Book;
import com.example.administrator.books.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class BookFactory {
    DbTools dbTools;
    List<Book> books;
    private static BookFactory bookFactory;
    Context context;
    public static Book tempBook;

    public DbTools getDbTools() {
        return dbTools;
    }

    public static BookFactory getInstance(Context context) {
        if (bookFactory == null)
            bookFactory = new BookFactory(context);
        return bookFactory;
    }

    //此方法获取到的是当前工厂的书本
    public BookFactory getBookFrom(String or_keyword, String[] or_cols, String[] and_cols, String and_keyword, boolean isAccurate) {
        books.clear();
        books.addAll(getByKeyWord(or_keyword, or_cols, and_keyword, and_cols, isAccurate));
        return this;

    }

    public boolean isRepeat(String isbn) {
        if (dbTools.getKeyWordCount(DbConstance.COLUMN_ISBN_13, isbn) == 0)
            return false;
        return true;
    }

    private BookFactory() {

    }

    private BookFactory(Context context) {
        this.context = context;
        dbTools = Constance.dbTools;
        books = new ArrayList<>();


    }

    public void update(Book book) {
        dbTools.update(book.getTableName(), book.getSqlMap(), DbConstance.COLUMN_UUID, book.getUuid().toString());

    }

    public void addBook(Book book) {
        dbTools.insert(book.getTableName(), book.getSqlMap());
    }

    public void deleteBook(Book book) {
        books.remove(book);
        dbTools.delete(book.getTableName(), DbConstance.COLUMN_UUID, book.getUuid().toString());

    }

    //此方法会新产生一个list集合
    public List<Book> getByKeyWord(String or_keyWord, String[] or_columns, String and_keyWord, String[] and_columns, boolean isAccurate) {
        String selection = null;
        if (or_columns != null && or_keyWord != null) {
            selection = "(";
            if (isAccurate) {
                for (String column : or_columns)
                    selection = selection.concat(column + " like '" + or_keyWord + "' or ");
            } else
                for (String column : or_columns)
                    selection = selection.concat(column + " like '%" + or_keyWord + "%' or ");
            selection = selection.substring(0, selection.length() - 3);
            selection = selection + ")";
        }
        if (and_keyWord != null && and_columns != null) {
            selection = selection + " and ";
            if (isAccurate) {
                for (String column : and_columns)
                    selection = selection.concat(column + " like '" + and_keyWord + "' and ");
            } else
                for (String column : and_columns)
                    selection = selection.concat(column + " like '%" + and_keyWord + "%' and ");
            selection = selection.substring(0, selection.length() - 4);
        }
        List<Book> books = new ArrayList<>();
        Cursor cursor;
        cursor = dbTools.getReadableDatabase().query(DbConstance.TABLE_BOOK, null, selection, null, null, null, null);
        while (cursor.moveToNext()) {
            Book book = new Book();
            book.setBookByCursor(cursor);
            books.add(book);
        }

        return books;
    }


    public List<Book> getBooks() {
        return books;
    }
}
