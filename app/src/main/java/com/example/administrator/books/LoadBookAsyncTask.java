package com.example.administrator.books;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.administrator.books.activities.MainActivity;
import com.example.administrator.books.factory.BookFactory;
import com.example.administrator.books.model.Book;

import net.lzzy.bookfinder.ApiConstants;
import net.lzzy.bookfinder.ApiService;

import java.util.Map;

/**
 * Created by Administrator on 2016/6/7.
 */
public class LoadBookAsyncTask extends AsyncTask<String, Nullable, Integer> {
    Context context;

    public LoadBookAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String[] params) {

        int count = 0;
        if (params == null)
            return 0;
        for (int i = 0; i < params.length; i++)
            if (download(params[i]))
                count++;
        return count;
    }


    boolean download(String isbn) {
        Map<String, String> map;
        try {
            map = ApiService.getBookFromApi(isbn);
            map.put(Book.MAP_LOCAL_IMAGE, ImageUtils.saveImageFromUrl(map.get(ApiConstants.JSON_IMAGE_LARGE)));
        } catch (Exception e) {
            return false;
        }
        Book book = new Book();
        book.setBookByMap(map);
        BookFactory.getInstance(context).addBook(book);
        return true;
    }


    @Override
    protected void onPostExecute(Integer count) {
        super.onPostExecute(count);
        Toast.makeText(context, "已添加" + count + "个", Toast.LENGTH_SHORT).show();

    }
}
