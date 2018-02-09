package com.example.administrator.books.model;

import com.example.administrator.books.R;

import java.util.Random;

/**
 * Created by Administrator on 2016/6/9 0009.
 */
public class BookTag {
    static int[] paperId = new int[]{R.drawable.pink, R.drawable.bule, R.drawable.green, R.drawable.purpe, R.drawable.oringe,R.drawable.yello};


    String name;
    int count;
    int colorId;

    public int getColorId() {
        return colorId;
    }

    public BookTag() {
        int v = paperId[new Random().nextInt(paperId.length)];
        colorId = v;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
