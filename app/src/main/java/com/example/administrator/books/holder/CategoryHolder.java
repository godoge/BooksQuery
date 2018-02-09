package com.example.administrator.books.holder;

import android.view.View;
import android.widget.TextView;

import com.example.administrator.books.R;

/**
 * Created by Administrator on 2016/6/11 0011.
 */
public class CategoryHolder {
    public TextView tv_name;
    public TextView tv_count;



    public CategoryHolder(View view) {
        tv_name = (TextView) view.findViewById(R.id.category_tv_name);
        tv_count = (TextView) view.findViewById(R.id.category_tv_count);
    }
}
