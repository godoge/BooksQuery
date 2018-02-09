package com.example.administrator.books;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.administrator.books.activities.AKeyISBNActivity;
import com.example.administrator.books.activities.MainActivity;
import com.example.administrator.books.factory.BookFactory;
import com.example.administrator.books.factory.CategoryFactory;
import com.example.administrator.books.model.Book;

import net.lzzy.bookfinder.ApiConstants;
import net.lzzy.bookfinder.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/7.
 */
public class LoadBookAsyncTask extends AsyncTask<String, Integer, List<Map<String, String>>> {
    Context context;
    private ProgressDialog dialog;

    public LoadBookAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Map<String, String>> doInBackground(String[] params) {
        ;
        List<Map<String, String>> books = new ArrayList<>();
        for (int i = 0; i < params.length; i++) {
            publishProgress(i, params.length);
            Map<String, String> book = download(params[i]);
            if (book != null) {
                books.add(book);
            }

        }
        return books;
    }


    Map<String, String> download(String isbn) {
        Map<String, String> map;
        try {
            map = ApiService.getBookFromApi(isbn);
            map.put(Book.MAP_LOCAL_IMAGE, ImageUtils.saveImageFromUrl(map.get(ApiConstants.JSON_IMAGE_LARGE)));
        } catch (Exception e) {
            return null;
        }

        return map;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setTitle("正在下载...");
        dialog.setMessage("获取信息中...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        dialog.setMessage("进度：" + values[0] + "/" + values[1]);
    }

    @Override
    protected void onPostExecute(List<Map<String, String>> maps) {
        super.onPostExecute(maps);
        dialog.dismiss();
        int count = maps.size();
        Toast.makeText(context, "获取到 " + count + " 本书", Toast.LENGTH_SHORT).show();
        if (count == 0)
            return;
        saveDialog(maps);

    }

    private void saveDialog(final List<Map<String, String>> maps) {

        final String[] categories = CategoryFactory.getInstance(context).getCategoryArray();
        new AlertDialog.Builder(context).setTitle("请选择类别").setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String category = categories[which];
                for (int i = 0; i < maps.size(); i++) {
                    Book book = new Book();
                    Map<String, String> map_book = maps.get(i);
                    map_book.put(Book.LOCAL_CATEGORY, category);
                    book.setBookByMap(map_book);
                    BookFactory.getInstance(context).addBook(book);
                }
                Toast.makeText(context, "有 " + maps.size() + " 本书保存成功啦！", Toast.LENGTH_SHORT).show();
                ((Activity) context).finish();
            }
        }).setCancelable(false).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new AlertDialog.Builder(context).setTitle("真的要取消？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "已取消", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveDialog(maps);
                    }
                }).setCancelable(false).show();
            }
        }).show();
    }

}
