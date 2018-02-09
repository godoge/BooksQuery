package com.example.administrator.books.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.administrator.books.R;
import com.example.administrator.books.holder.TagViewHolder;
import com.example.administrator.books.model.BookTag;

import java.util.List;

/**
 * Created by Administrator on 2016/6/9 0009.
 */
public class TagAdapter extends BaseAdapter {
    List<BookTag> list;
    Context context;

    public TagAdapter(Context context, List<BookTag> list) {
        this.list = list;
        this.context = context;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BookTag getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TagViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.tag_item, null);
            holder = new TagViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (TagViewHolder) convertView.getTag();

        holder.getTv_tag().setText(getItem(position).getName());
        holder.getTv_count().setText(getItem(position).getCount() + "");
        holder.getIv_tag_back().setImageResource(getItem(position).getColorId());
        return convertView;
    }
}
