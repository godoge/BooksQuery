package com.example.administrator.books;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class SqlUtil {

    static String insert(String table, Map<String, Object> column_value) {

        StringBuilder sb_column = new StringBuilder();
        StringBuilder sb_value = new StringBuilder();
        Iterator<Map.Entry<String, Object>> iterator = column_value.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            sb_column.append(entry.getKey() + ",");
            if (entry.getValue() instanceof Double)
                sb_value.append(entry.getValue() + ",");
            else
                sb_value.append("'" + entry.getValue() + "'" + ",");
        }
        return DbConstance.INSERT_TABLE_HEAD + " " + table + "(" + sb_column.substring(0, sb_column.length() - 1) + ")values(" + sb_value.substring(0, sb_value.length() - 1) + ")";
    }

    static String update(String table, Map<String, Object> column_value, String col, Object val) {
        String value;
        String field = "";
        Iterator<Map.Entry<String, Object>> iterator = column_value.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String cur_val;
            if (entry.getValue() instanceof String)
                cur_val = "'" + entry.getValue() + "'";
            else
                cur_val = entry.getValue() + "";
            field = field + entry.getKey() + "=" + cur_val + ",";
        }
        field = field.substring(0, field.length() - 1);
        if (val instanceof String)
            value = "'" + val + "'";
        else
            value = val + "";
        return "update " + table + " set " + field + " where " + col + "=" + value;

    }


    public static String delete(String table, String column, Object value) {
        String v;
        if (value instanceof String)
            v = "'" + value + "'";
        else
            v = value + "";
        return "delete from " + table + " where " + column + "=" + v;
    }

}
