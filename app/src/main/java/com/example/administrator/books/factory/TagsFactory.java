package com.example.administrator.books.factory;

import android.content.Context;
import android.database.Cursor;

import com.example.administrator.books.Constance;
import com.example.administrator.books.DbConstance;
import com.example.administrator.books.DbTools;
import com.example.administrator.books.activities.MainActivity;
import com.example.administrator.books.model.BookTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/9 0009.
 */
public class TagsFactory {
    List<BookTag> bookTags;
    List<String> tags;
    DbTools dbTools;
    private static TagsFactory tagsFactory;

    public static TagsFactory getInstance(Context context) {
        if (tagsFactory == null)
            tagsFactory = new TagsFactory(context);
        return tagsFactory;
    }

    public TagsFactory(Context context) {
        dbTools = Constance.dbTools;
        tags = new ArrayList<>();
        bookTags = new ArrayList<>();

    }

    public List<BookTag> getBookTags() {
        return bookTags;
    }

    void getTagsFormDatabase() {
        Cursor cursor = dbTools.getReadableDatabase().query(DbConstance.TABLE_BOOK, new String[]{DbConstance.COLUMN_TAGS}, null, null, null, null, null);

        int tag = cursor.getColumnIndex(DbConstance.COLUMN_TAGS);
        while (cursor.moveToNext()) {
            getTagsByString(cursor.getString(tag));
        }

    }

    public TagsFactory setBookTags() {
        getTagsFormDatabase();
        List<BookTag> temp = new ArrayList<>();
        for (String tag : tags) {
            BookTag booktag = new BookTag();
            booktag.setCount(dbTools.getKeyWordCount(DbConstance.COLUMN_TAGS, tag));
            booktag.setName(tag);
            temp.add(booktag);
        }
        bookTags.clear();
        bookTags.addAll(temp);

        return this;
    }


    boolean isRespect(String tag) {
        for (int i = 0; i < tags.size(); i++)
            if (tags.get(i).equals(tag))
                return true;
        return false;
    }

    void getTagsByString(String str_tags) {

        str_tags = str_tags.replaceAll("，", ",");
        str_tags = str_tags.replaceAll(" ", "");
        if (str_tags.indexOf(",") == -1) {
            if (!isRespect(str_tags))
                tags.add(str_tags);
            return;
        }
        while (true) {
            String tag = str_tags.substring(0, str_tags.indexOf(','));
            if (!isRespect(tag))
                tags.add(tag);
            str_tags = str_tags.substring(str_tags.indexOf(",") + 1, str_tags.length());
            if (str_tags.indexOf(',') == -1)
                break;
        }
        if (!isRespect(str_tags))
            tags.add(str_tags);

    }


}
