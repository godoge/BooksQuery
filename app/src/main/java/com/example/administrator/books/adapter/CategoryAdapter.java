package com.example.administrator.books.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.administrator.books.R;
import com.example.administrator.books.holder.CategoryHolder;
import com.example.administrator.books.model.Category;

import java.util.List;

/**
 * Created by Administrator on 2016/6/11 0011.
 */
public class CategoryAdapter extends BaseAdapter {
    Context context;
    List<Category> list;

    public CategoryAdapter(Context context, List<Category> list) {

        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Category getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_category, null);
            holder = new CategoryHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (CategoryHolder) convertView.getTag();
        holder.tv_name.setText(getItem(position).getName());
        holder.tv_count.setText(getItem(position).getCount() + "");
        return convertView;
    }
}
