package com.example.administrator.books.holder;

import android.view.View;
import android.widget.ImageView;

import com.example.administrator.books.R;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class ViewHolder {
    private ImageView iv_cover;

    public ImageView getIv_cover() {
        return iv_cover;
    }

    public ViewHolder(View view) {

        iv_cover = (ImageView) view.findViewById(R.id.item_book_gv_item_cover);
    }
}
