package com.example.administrator.books.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.books.BitmapUtils;
import com.example.administrator.books.LoadImageAsync;
import com.example.administrator.books.R;
import com.example.administrator.books.holder.ViewHolder;
import com.example.administrator.books.model.Book;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class BookCaseAdapter extends BaseAdapter {
    List<Book> books;
    Context context;
    int viewWidth;
    private LoadImageAsync loadImageAsync;


    public BookCaseAdapter(Context context, List<Book> books, int viewWidth) {
        this.context = context;
        this.books = books;
        this.viewWidth = viewWidth;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Book getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_book, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.getTv_title().setText(getItem(position).getTitle());
        holder.getTv_author().setText(getItem(position).getAuthor());
        String imagePath = books.get(position).getImageLarge();
        final ImageView iv = holder.getIv_cover();
        if (new File(imagePath).exists()) {
            iv.setLayoutParams(new LinearLayout.LayoutParams(viewWidth / 3, viewWidth / 3));
            loadImageAsync = new LoadImageAsync(viewWidth / 3) {
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    iv.setImageBitmap(bitmap);

                }
            };
            loadImageAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imagePath);
        } else
            iv.setImageResource(R.drawable.blank_cover);

        return convertView;
    }
}
