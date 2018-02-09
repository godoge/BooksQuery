package com.example.administrator.books;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.example.administrator.books.model.Book;

import net.lzzy.bookfinder.ApiConstants;
import net.lzzy.bookfinder.ApiService;

import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/20.
 */
public class LoadAbookAsync extends AsyncTask<String, Nullable, Book> {


    @Override
    protected Book doInBackground(String... params) {
        Map<String, String> map;
        Book book = null;
        if (params[0] == null)
            return null;
        try {
            map = ApiService.getBookFromApi(params[0]);
            map.put(Book.MAP_LOCAL_IMAGE, ImageUtils.saveImageFromUrl(map.get(ApiConstants.JSON_IMAGE_LARGE)));
            book = new Book();
            book.setBookByMap(map);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return book;
    }


}
