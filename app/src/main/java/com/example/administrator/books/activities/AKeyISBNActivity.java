package com.example.administrator.books.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.books.LoadBookAsyncTask;
import com.example.administrator.books.R;
import com.example.administrator.books.factory.BookFactory;
import com.example.administrator.books.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class AKeyISBNActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edt_inputISBN;
    private Button btn_load;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isbn);
        initView();
        initListener();
    }

    void initListener() {
        btn_load.setOnClickListener(this);
    }

    void initView() {
        edt_inputISBN = (EditText) findViewById(R.id.activity_isbn_edt);
        btn_load = (Button) findViewById(R.id.activity_isbn_btn_load);
    }

    void download(List<String> isbns) {
        String[] str_isbn = new String[isbns.size()];
        for (int i = 0; i < str_isbn.length; i++)
            str_isbn[i] = isbns.get(i);
        new LoadBookAsyncTask(this).execute(str_isbn);
    }

    List<String> getIsbnArray() {
        String isbn = edt_inputISBN.getText().toString();
        if (isbn.length() == 0)
            return null;
        //格式化处理
        List<String> temp_list = new ArrayList<>();
        isbn = isbn.trim();
        isbn = isbn.replaceAll("\n\n", "\n");
        while (isbn.indexOf("\n") != -1) {
            temp_list.add(isbn.substring(0, isbn.indexOf('\n')));
            isbn = isbn.substring(isbn.indexOf('\n') + 1, isbn.length());
            if (isbn.indexOf("\n") == -1)
                break;
        }
        temp_list.add(isbn);
        //过滤重复项
        List<String> list = new ArrayList();
        BookFactory bookFactory = BookFactory.getInstance(this);
        for (String aisbn : temp_list) {
            if (list.contains(aisbn)||bookFactory.isRepeat(aisbn))
                break;
            list.add(aisbn);
        }

        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_isbn_btn_load:
                List<String> list = getIsbnArray();
                if (list == null) {
                    Toast.makeText(this, "输入不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                download(list);
                break;


        }
    }
}
