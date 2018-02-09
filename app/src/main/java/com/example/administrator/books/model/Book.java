package com.example.administrator.books.model;

import android.database.Cursor;

import com.example.administrator.books.DbConstance;
import com.example.administrator.books.Sqlable;

import net.lzzy.bookfinder.ApiConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class Book implements Sqlable {
    public static final String MAP_LOCAL_IMAGE = "local_image";
    public static final String LOCAL_CATEGORY = "local_category";
    public static final String STR_DEFAULT = "默认";
    private String publish;
    private UUID uuid;
    private String title;
    private String isbn;
    private String author;
    private String price;
    private String tags = STR_DEFAULT;
    private String imageLarge;
    private String id;
    private String numRating;
    private String average;
    private String publishDate;
    private String translator;
    private String category = STR_DEFAULT;

    public static String getMapLocalImage() {
        return MAP_LOCAL_IMAGE;
    }

    public static String getLocalCategory() {
        return LOCAL_CATEGORY;
    }

    public static String getStrDefault() {
        return STR_DEFAULT;
    }

    public String getNumRating() {
        return numRating;
    }

    public void setNumRating(String numRating) {
        this.numRating = numRating;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getTranslator() {
        return translator;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageLarge() {
        return imageLarge;
    }

    public void setImageLarge(String imageLarge) {
        this.imageLarge = imageLarge;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    String summary;

    public Book() {
        this.uuid = UUID.randomUUID();
    }


    public void setBookByCursor(Cursor cursor) {
        uuid = UUID.fromString(cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_UUID)));
        numRating = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_RATING));
        average = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_AVERATE));
        title = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_TITLE));
        author = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_AUTHOR));
        category = cursor.getString(cursor.getColumnIndex(DbConstance.BOOK_COLUMN_CATEGORY));
        tags = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_TAGS));
        translator = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_TRANSLATOR));
        isbn = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_ISBN_13));
        summary = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_SUMMARY));
        imageLarge = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_IMAGE_LARGE));
        price = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_PRICE));
        uuid = UUID.fromString(cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_UUID)));
        id = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_ID));
        publishDate = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_PUBLISH_DATE));
        publish = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_PUBLISH));

    }

    public void setLocalMap(Map<String, Object> map) {
        if (map == null)
            return;
        if (map.get(DbConstance.COLUMN_TITLE) == null)
            average = "0";
        else
            average = (String) map.get(DbConstance.COLUMN_AVERATE);
        if (map.get(DbConstance.COLUMN_RATING) == null)
            numRating = "0";
        else
            numRating = (String) map.get(DbConstance.COLUMN_RATING);
        title = (String) map.get(DbConstance.COLUMN_TITLE);
        author = (String) map.get(DbConstance.COLUMN_AUTHOR);
        summary = (String) map.get(DbConstance.COLUMN_SUMMARY);
        imageLarge = (String) map.get(MAP_LOCAL_IMAGE);
        publish = (String) map.get(DbConstance.COLUMN_PUBLISH);
        isbn = (String) map.get(DbConstance.COLUMN_ISBN_13);
        if (map.get(DbConstance.COLUMN_ID) == null)
            id = "0";
        else
            id = (String) map.get(DbConstance.COLUMN_ID);
        if (map.get(DbConstance.COLUMN_PUBLISH_DATE) == null)
            publishDate = "未知";
        else
            publishDate = (String) map.get(DbConstance.COLUMN_PUBLISH_DATE);
        price = (String) map.get(DbConstance.COLUMN_PRICE);
        if (map.get(DbConstance.COLUMN_TRANSLATOR) == null)
            translator = "无";
        else
            translator = (String) map.get(DbConstance.COLUMN_TRANSLATOR);
        if (map.get(DbConstance.COLUMN_TAGS) == null)
            tags = "未知";
        else
            tags = (String) map.get(DbConstance.COLUMN_TAGS);
        category = (String) map.get(LOCAL_CATEGORY);

    }


    public void setBookByMap(Map<String, String> map) {
        if (map == null)
            return;
        average = map.get(ApiConstants.JSON_RATING_AVERAGE);
        numRating = map.get(ApiConstants.JSON_RATING_NUMRATERS);
        title = map.get(ApiConstants.JSON_TITLE);
        author = map.get(ApiConstants.JSON_AUTHOR);
        summary = map.get(ApiConstants.JSON_SUMMARY);
        imageLarge = map.get(MAP_LOCAL_IMAGE);
        publish = map.get(ApiConstants.JSON_PUBLISHER);
        isbn = map.get(ApiConstants.JSON_ISBN_13);
        id = map.get(ApiConstants.JSON_ID);
        publishDate = map.get(ApiConstants.JSON_PUBDATE);
        price = map.get(ApiConstants.JSON_PRICE);
        publishDate = map.get(ApiConstants.JSON_PUBDATE);
        translator = map.get(ApiConstants.JSON_TRANSLATOR);
        String temp_tag = map.get(ApiConstants.JSON_TAGS);
        String temp_category = map.get(LOCAL_CATEGORY);
        if (!(temp_tag == null))
            tags = temp_tag;
        if (!(temp_category == null))
            category = temp_category;

    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    @Override
    public Map<String, Object> getSqlMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(DbConstance.COLUMN_UUID, uuid.toString());
        map.put(DbConstance.COLUMN_TITLE, title);
        map.put(DbConstance.COLUMN_AUTHOR, author);
        map.put(DbConstance.COLUMN_RATING, numRating);
        map.put(DbConstance.COLUMN_AVERATE, average);
        map.put(DbConstance.COLUMN_ISBN_13, isbn);
        map.put(DbConstance.COLUMN_PUBLISH_DATE, publishDate);
        map.put(DbConstance.COLUMN_TRANSLATOR, translator);
        map.put(DbConstance.COLUMN_PUBLISH, publish);
        map.put(DbConstance.COLUMN_TAGS, tags);
        map.put(DbConstance.COLUMN_PRICE, price);
        map.put(DbConstance.COLUMN_SUMMARY, summary);
        map.put(DbConstance.COLUMN_IMAGE_LARGE, imageLarge);
        map.put(DbConstance.BOOK_COLUMN_CATEGORY, category);
        map.put(DbConstance.COLUMN_ID, id);
        return map;
    }

    @Override
    public String getTableName() {
        return DbConstance.TABLE_BOOK;
    }
}
