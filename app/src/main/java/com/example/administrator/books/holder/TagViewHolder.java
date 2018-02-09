package com.example.administrator.books.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.books.R;

/**
 * Created by Administrator on 2016/6/9 0009.
 */
public class TagViewHolder {
    private TextView tv_count;
    private TextView tv_tag;
    private ImageView iv_tag_back;

    public TextView getTv_count() {
        return tv_count;
    }

    public ImageView getIv_tag_back() {
        return iv_tag_back;
    }

    public TextView getTv_tag() {
        return tv_tag;
    }


    public TagViewHolder(View view) {
        tv_tag = (TextView) view.findViewById(R.id.tag_tv_name);
        tv_count = (TextView) view.findViewById(R.id.tag_tv_count);
        iv_tag_back = (ImageView) view.findViewById(R.id.tag_iv_back);
    }


}
