package com.example.administrator.books.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.books.R;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class ViewHolder {
    private ImageView iv_cover;
    private TextView tv_title;
    private TextView tv_author;

    public ImageView getIv_cover() {
        return iv_cover;
    }

    public TextView getTv_title() {
        return tv_title;
    }

    public TextView getTv_author() {
        return tv_author;
    }

    public ViewHolder(View view) {
        tv_author= (TextView) view.findViewById(R.id.item_book_tv_item_author);
        tv_title = (TextView) view.findViewById(R.id.item_book_tv_item_title);
        iv_cover = (ImageView) view.findViewById(R.id.item_book_gv_item_cover);
    }
}
