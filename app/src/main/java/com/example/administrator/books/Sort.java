package com.example.administrator.books;

import com.example.administrator.books.model.Category;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2016/6/11 0011.
 */
public class Sort {

    public static void sort(List<Category> list) {
        Collections.sort(list, new Comparator<Category>() {
            @Override
            public int compare(Category lhs, Category rhs) {
                if (lhs.getCount() < rhs.getCount())
                    return 1;
                else if (lhs.getCount() > rhs.getCount())
                    return -1;
                else return 0;

            }
        });

    }

}
