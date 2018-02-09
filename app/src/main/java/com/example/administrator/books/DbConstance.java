package com.example.administrator.books;

import com.example.administrator.books.model.Book;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class DbConstance {
    public static String DB_NAME = "books.db";
    public static String CREATE_TABLE_HEAD = "create table";
    public static String INSERT_TABLE_HEAD = "insert into";
    public static String TABLE_BOOK = "books";
    public static String TABLE_CATEGORY = "categories";
    public static String COLUMN_RATING = "rating";
    public static String COLUMN_AVERATE = "average";
    public static String IMAGE_SMALL = "image_small";
    public static String CATEGORY_COLUMN_NAME = "category_name";
    public static String COLUMN_TITLE = "title";
    public static String COLUMN_AUTHOR = "author";
    public static String BOOK_COLUMN_CATEGORY = "book_category";
    public static String COLUMN_UUID = "uuid";
    public static String COLUMN_PRICE = "price";
    public static String COLUMN_PUBLISH = "public";
    public static String COLUMN_ID = "id";
    public static String COLUMN_PUBLISH_DATE = "publish_date";
    public static String COLUMN_TRANSLATOR = "translator";
    public static String COLUMN_ISBN_13 = "isb_13";
    public static String COLUMN_TAGS = "tags";
    public static String COLUMN_IMAGE_LARGE = "image_large";
    public static String COLUMN_SUMMARY = "summary";
    private static String TYPE_INT = "int";
    private static String TYPE_DECIMAL = "decimal";

    private static String TYPE_STRING = "text";
    public static List<String> sqls = new ArrayList<>();

    static {
        StringBuilder table_book = new StringBuilder();
        table_book.append(CREATE_TABLE_HEAD).append(" ")
                .append(TABLE_BOOK).append(" ").append("(")
                .append(COLUMN_UUID).append(" ").append(TYPE_STRING).append(" ").append("NOT NULL").append(",")
                .append(COLUMN_AUTHOR).append(" ").append(TYPE_STRING).append(",")
                .append(COLUMN_TITLE).append(" ").append(TYPE_STRING).append(",")
                .append(COLUMN_TAGS).append(" ").append(TYPE_STRING).append(",")
                .append(COLUMN_PRICE).append(" ").append(TYPE_DECIMAL).append(",")
                .append(COLUMN_RATING).append(" ").append(TYPE_STRING).append(",")
                .append(COLUMN_AVERATE).append(" ").append(TYPE_STRING).append(",")
                .append(COLUMN_ID).append(" ").append(TYPE_STRING).append(",")
                .append(COLUMN_TRANSLATOR).append(" ").append(TYPE_STRING).append(",")
                .append(BOOK_COLUMN_CATEGORY).append(" ").append(TYPE_STRING).append(",")
                .append(COLUMN_ISBN_13).append(" ").append(TYPE_STRING).append(",")
                .append(COLUMN_PUBLISH).append(" ").append(TYPE_STRING).append(",")
                .append(COLUMN_PUBLISH_DATE).append(" ").append(TYPE_STRING).append(",")
                .append(COLUMN_SUMMARY).append(" ").append(TYPE_STRING).append(",")
                .append(COLUMN_IMAGE_LARGE).append(" ").append(TYPE_STRING)
                .append(")");
        sqls.add(table_book.toString());
        StringBuilder table_category = new StringBuilder();
        table_category.append(CREATE_TABLE_HEAD).append(" ").append(TABLE_CATEGORY).append(" ").append("(")
                .append(CATEGORY_COLUMN_NAME).append(" ").append(TYPE_STRING).append(")");
        StringBuilder default_category = new StringBuilder();
        Map<String, Object> map = new Hashtable<>();
        map.put(CATEGORY_COLUMN_NAME, Book.STR_DEFAULT);
        sqls.add(table_category.toString());
        default_category.append(SqlUtil.insert(TABLE_CATEGORY, map));
        sqls.add(default_category.toString());


    }

}
