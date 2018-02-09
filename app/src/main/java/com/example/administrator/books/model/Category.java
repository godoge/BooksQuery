package com.example.administrator.books.model;

import com.example.administrator.books.Constance;
import com.example.administrator.books.DbConstance;
import com.example.administrator.books.DbTools;
import com.example.administrator.books.Sqlable;
import com.example.administrator.books.activities.MainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/11 0011.
 */
public class Category implements Sqlable {

    String name;
    int count = 0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDefaultCategory() {
        name = Book.STR_DEFAULT;
        count = Constance.dbTools.getKeyWordCount(DbConstance.BOOK_COLUMN_CATEGORY, name);
    }

    @Override
    public Map<String, Object> getSqlMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(DbConstance.CATEGORY_COLUMN_NAME, name);
        return map;
    }

    @Override
    public String getTableName() {
        return DbConstance.TABLE_CATEGORY;
    }
}
