package com.example.administrator.books.factory;

import android.content.Context;
import android.database.Cursor;

import com.example.administrator.books.Constance;
import com.example.administrator.books.DbConstance;
import com.example.administrator.books.DbTools;
import com.example.administrator.books.Sort;
import com.example.administrator.books.activities.MainActivity;
import com.example.administrator.books.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/11 0011.
 */
public class CategoryFactory {
    DbTools dbTools;
    List<Category> list;
    private static CategoryFactory categoryFactory;
    Context context;

    private CategoryFactory(Context context) {
        list = new ArrayList<>();
        dbTools = Constance.dbTools;
        this.context = context;
    }

    public static CategoryFactory getInstance(Context context) {
        if (categoryFactory == null)
            categoryFactory = new CategoryFactory(context);
        return categoryFactory;
    }

    public void addCategory(Category category) {
        list.add(category);
        dbTools.insert(DbConstance.TABLE_CATEGORY, category.getSqlMap());

    }

    public void delete(Category category) {
        list.remove(category);
        dbTools.delete(category.getTableName(), DbConstance.CATEGORY_COLUMN_NAME, category.getName());

    }

    private Category getACategory(Cursor cursor) {
        int index_name = cursor.getColumnIndex(DbConstance.CATEGORY_COLUMN_NAME);
        Category category = new Category();
        String name = cursor.getString(index_name);
        category.setName(name);
        category.setCount(BookFactory.getInstance(context).getKeyWordCount(DbConstance.BOOK_COLUMN_CATEGORY, name));
        return category;
    }

    public CategoryFactory getCategoryFromDb() {
        list.clear();
        Cursor cursor = dbTools.getReadableDatabase().query(DbConstance.TABLE_CATEGORY, null, null, null, null, null, null, null);
        cursor.moveToNext();
        //获取到默认类别对象，里面包括名字和数量
        Category def = getACategory(cursor);
        while (cursor.moveToNext())
            list.add(getACategory(cursor));
        Sort.sort(list);
        //把默认类别放置到最前面
        list.remove(def);
        list.add(0, def);
        return this;
    }

    public String[] getCategoryArray() {
        String[] categoryArray = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            categoryArray[i] = list.get(i).getName();
        return categoryArray;
    }

    public int getCategoryNameIndex(String categoryName) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(categoryName))
                return i;
        }

        return -1;
    }


    public List<Category> getList() {
        return list;
    }
}
