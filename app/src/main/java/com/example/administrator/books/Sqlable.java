package com.example.administrator.books;

import java.util.Map;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public interface Sqlable {

    Map<String, Object> getSqlMap();

    String getTableName();

}
