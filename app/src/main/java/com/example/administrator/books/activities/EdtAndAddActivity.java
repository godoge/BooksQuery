package com.example.administrator.books.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.books.DbConstance;
import com.example.administrator.books.LoadImageAsync;
import com.example.administrator.books.R;
import com.example.administrator.books.factory.BookFactory;
import com.example.administrator.books.factory.CategoryFactory;
import com.example.administrator.books.model.Book;
import com.example.administrator.books.model.Category;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2016/6/6 0006.
 * 当有uuid传过来时，就用uuid从数据库查找
 * 书籍并且显示出来在编辑框中，若无UUID传过来，则查找临时书籍有没有书，
 * 若有就拿临时书来编辑,否则视为手动添加界面
 * PS:有uuid说明是从BookCaseActivity进来的，
 * 从添加isbn进来的用isbn输入界面传过来的book显示出来
 */
public class EdtAndAddActivity extends AppCompatActivity implements View.OnClickListener {


    private Spinner spinner;
    private String[] categories;
    private FloatingActionButton btn_save;
    private EditText edt_title;
    private EditText edt_author;
    private EditText edt_summary;
    private EditText edt_isbn;
    private EditText edt_publish;
    private EditText edt_price;
    String selectedCategory;
    private ImageView iv_cover;
    private String path;
    private String filePath = "";
    private String fileName;
    public static final String BOOK_UUID = "uuid";
    private String uuid;
    private boolean isManualNewAdd = false;
    private Book book;
    public final static String INTENT_CATEGORY = "intent category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_add);
        initValue();
        initView();
        if (uuid != null) {
            book = BookFactory.getInstance(this).getByKeyWord(uuid, new String[]{DbConstance.COLUMN_UUID}, null, null, true).get(0);
            edt_isbn.setEnabled(false);
            displayBook(book);
            selectedCategory = book.getCategory();
        } else if (BookFactory.tempBook != null) {
            book = BookFactory.tempBook;
            displayBook(book);
            edt_isbn.setEnabled(false);
        } else {
            book = new Book();
            selectedCategory = getIntent().getStringExtra(INTENT_CATEGORY);
            isManualNewAdd = true;
        }
        initViewListener();
        setViewAdapter();
    }


    private void displayBook(Book book) {
        edt_title.setText(book.getTitle());
        edt_author.setText(book.getAuthor());
        edt_publish.setText(book.getPublish());
        edt_price.setText(book.getPrice());
        edt_summary.setText(book.getSummary());
        edt_isbn.setText(book.getIsbn());
        filePath = book.getImageLarge();
        displayImage();

    }

    private void displayImage() {
        new LoadImageAsync((int) (getResources().getDisplayMetrics().widthPixels * 0.3)) {
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                iv_cover.setImageBitmap(bitmap);
            }
        }.execute(filePath);

    }

    private void initViewListener() {
        iv_cover.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    void initValue() {
        uuid = getIntent().getStringExtra(BOOK_UUID);
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/pic/";
        File file_path = new File(path);
        if (!file_path.exists())
            file_path.mkdirs();
        List<Category> list = CategoryFactory.getInstance(this).getList();
        categories = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            categories[i] = list.get(i).getName();

    }


    void setViewAdapter() {
        int index = CategoryFactory.getInstance(this).getCategoryNameIndex(selectedCategory);
        Log.i("Info_INDEX", selectedCategory + " at " + index);
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories));
        spinner.setSelection(index);
    }

    void initView() {
        spinner = (Spinner) findViewById(R.id.activity_spinner_category);
        btn_save = (FloatingActionButton) findViewById(R.id.activity_addbook_btn_save);
        edt_title = (EditText) findViewById(R.id.addBook_edt_bookName);
        edt_author = (EditText) findViewById(R.id.addBook_edt_author);
        edt_isbn = (EditText) findViewById(R.id.activity_add_edt_ISBN);
        edt_summary = (EditText) findViewById(R.id.activity_add_edt_introduction);
        edt_publish = (EditText) findViewById(R.id.activity_add_edt_public);
        edt_price = (EditText) findViewById(R.id.activity_add_edt_price);
        iv_cover = (ImageView) findViewById(R.id.addbook_iv_cover);


    }

    String getEdtContent(EditText edt) {
        if (edt == null)
            return "null";
        return edt.getText().toString();

    }

    Boolean checkIsInputCompletion() {
        if (getEdtContent(edt_title).equals("") | getEdtContent(edt_author).equals("") | getEdtContent(edt_isbn).equals("") | getEdtContent(edt_price).equals("")
                | getEdtContent(edt_publish).equals("") | getEdtContent(edt_summary).equals(""))
            return false;

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_addbook_btn_save:

                if (!checkIsInputCompletion()) {
                    Toast.makeText(this, "你还没有输入完整！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (filePath.equals("")) {
                    Toast.makeText(this, "请设置一个封面！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> map = new HashMap<>();
                map.putAll(book.getSqlMap());
                map.put(DbConstance.COLUMN_TITLE, getEdtContent(edt_title));
                map.put(DbConstance.COLUMN_AUTHOR, getEdtContent(edt_author));
                map.put(DbConstance.COLUMN_ISBN_13, getEdtContent(edt_isbn));
                map.put(DbConstance.COLUMN_PRICE, getEdtContent(edt_price));
                map.put(DbConstance.COLUMN_PUBLISH, getEdtContent(edt_publish));
                map.put(DbConstance.COLUMN_SUMMARY, getEdtContent(edt_summary));
                map.put(Book.LOCAL_CATEGORY, selectedCategory);
                map.put(Book.MAP_LOCAL_IMAGE, filePath);
                book.setLocalMap(map);
                if (isManualNewAdd) {
                    BookFactory.getInstance(this).addBook(book);
                    Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra(ViewActivity.IS_DATA, true);
                    setResult(RESULT_OK, intent);
                } else if (uuid != null) {
                    book.setUuid(UUID.fromString(uuid));
                    BookFactory.getInstance(this).update(book);
                    Intent intent = new Intent();
                    intent.putExtra(ViewActivity.IS_DATA, true);
                    setResult(RESULT_OK, intent);
                    Toast.makeText(this, "已更新", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent();
                intent.putExtra(ViewActivity.IS_DATA, true);
                setResult(RESULT_OK, intent);
                finish();
                break;


            case R.id.addbook_iv_cover:
                new AlertDialog.Builder(this).setTitle("选择").setItems(R.array.menu, new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        switch (which) {
                            case 0:
                                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                fileName = System.currentTimeMillis() + ".jpg";
                                filePath = path + fileName;
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));
                                startActivityForResult(intent, 1);
                                break;
                            case 1:
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, 2);
                                break;
                        }
                    }
                }).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setTitle("提示").setMessage("您还没有保存，确定退出？").setNegativeButton("返回", null).show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!(resultCode == RESULT_OK)) {
            filePath = "";
            return;
        }
        if (requestCode == 2) {

            try {
                filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath().concat("/pics/" + System.currentTimeMillis() + ".jpg");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(data.getDataString()));
                File file = new File(filePath);
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        displayImage();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
