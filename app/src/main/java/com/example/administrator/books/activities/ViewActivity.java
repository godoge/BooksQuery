package com.example.administrator.books.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.books.BitmapUtils;
import com.example.administrator.books.Constance;
import com.example.administrator.books.DbConstance;
import com.example.administrator.books.ImageUtils;
import com.example.administrator.books.LoadImageAsync;
import com.example.administrator.books.R;
import com.example.administrator.books.factory.BookFactory;
import com.example.administrator.books.factory.CategoryFactory;
import com.example.administrator.books.model.Book;

import net.lzzy.bookfinder.ApiConstants;
import net.lzzy.bookfinder.ApiService;

import java.util.Map;

/**
 * Created by Administrator on 2016/6/10 0010.
 * 当有uuid传过来时用uuid获取数据库的书籍并显示出来，
 * 且隐藏底部的确定按钮、编辑按钮和拍照按钮。若无uuid
 * 则查询临时书籍，若有则显示出来，不隐藏底部的确定按钮
 */
public class ViewActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_cover;
    private TextView tv_info;
    private TextView tv_tag;
    private TextView tv_summary;
    private TextView tv_score;
    private Map<String, String> map;
    private String isbn;
    Thread thread;
    private TextView tv_title;
    private Button btn_save;
    CoordinatorLayout layout;
    String selectedCategory;
    public static final String INTENT_CATEGORY = "intent category";
    private FloatingActionButton fbtn_edit;
    private String uuid;
    public static final int FROM_VIEW = 0;
    public static final int FROM_ADD = 1;
    private Book book;
    public static final String ACTION_BOOK_UUID = "uuid";
    private FloatingActionButton fbtn_camera;
    private LoadImageAsync imageAsync;
    public static final String IS_DATA = "is data";
    private ProgressDialog progressDialog;

    private synchronized void threadStop() {
        if (thread == null)
            return;
        thread.interrupt();
        thread = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        selectedCategory = getIntent().getStringExtra(INTENT_CATEGORY);
        uuid = getIntent().getStringExtra("uuid");
        initView();
        if (uuid != null) {
            updateBook(uuid);
            btn_save.setVisibility(View.GONE);
            fbtn_edit.setVisibility(View.GONE);
            fbtn_camera.setVisibility(View.GONE);
        } else if (BookFactory.tempBook != null) {
            book = BookFactory.tempBook;
            displayBookByModel(book);
        }
        btn_save.setOnClickListener(this);
        fbtn_edit.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageAsync = null;
        BookFactory.tempBook = null;
    }

    private void startDownBook() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("等待获取...");
        progressDialog.show();
        thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    map = ApiService.getBookFromApi(isbn);
                    map.put(Book.MAP_LOCAL_IMAGE, ImageUtils.saveImageFromUrl(map.get(ApiConstants.JSON_IMAGE_LARGE)));
                    book = new Book();
                    book.setBookByMap(map);
                    Log.i("MyInfo", "thread_1");
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("MyInfo", "thread_0");
                    handler.sendEmptyMessage(0);

                }
                threadStop();

            }
        };
        thread.start();


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();
            Log.i("MyInfo", "handler");
            switch (msg.what) {
                case 0:
                    Toast.makeText(ViewActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                    layout.setVisibility(View.GONE);
                    break;
                case 1:
                    Toast.makeText(ViewActivity.this, "获取成功", Toast.LENGTH_SHORT).show();
                    displayBookByModel(book);
                    break;
            }


        }


    };


    private void displayBookByModel(Book book) {
        if (book == null)
            return;
        layout.setVisibility(View.VISIBLE);
        iv_cover.setLayoutParams(new LinearLayout.LayoutParams((int) (getResources().getDisplayMetrics().widthPixels * 0.4), (int) (getResources().getDisplayMetrics().widthPixels * 0.4)));
        imageAsync = new LoadImageAsync((int) (getResources().getDisplayMetrics().widthPixels * 0.3)) {
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                iv_cover.setImageBitmap(bitmap);
            }
        };
        imageAsync.execute(book.getImageLarge());
        tv_summary.setText(book.getSummary());
        tv_tag.setText(book.getTags());
        tv_score.setText("共" + book.getNumRating() + "人评分, 平均得分：" + book.getAverage() + "/10");
        tv_title.setText(book.getTitle());
        tv_info.setText(
                "出版社:" + book.getPublish() + "\n" +
                        "ISBN:" + book.getIsbn() + "\n" +
                        "作者:" + book.getAuthor() + "\n" +
                        "翻译:" + book.getTranslator() + "\n" +
                        "出版时间:" + book.getPublishDate() + "\n" +
                        "价格:" + book.getPrice()
        );
    }

    String[] categories = CategoryFactory.getInstance(this).getCategoryArray();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preview_save:
                if (BookFactory.getInstance(this).isRepeat(book.getIsbn())) {
                    Toast.makeText(ViewActivity.this, "库已有此书，不能重复添加", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedCategory != null) {
                    new AlertDialog.Builder(this).setTitle("提示").setMessage("当前选中的类别为\"" + selectedCategory + "\"需要更改类别吗？").setPositiveButton("更改", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            save();
                        }
                    }).setNegativeButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            book.setCategory(selectedCategory);
                            BookFactory.getInstance(ViewActivity.this).addBook(book);
                            Toast.makeText(ViewActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra(IS_DATA, true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }).show();
                } else
                    save();
                break;
            case R.id.preview_fbtn_edit:
                Intent intent = new Intent(this, EdtAndAddActivity.class);
                BookFactory.tempBook = book;
                startActivityForResult(intent, 0);
                break;
        }

    }

    private void save() {
        new AlertDialog.Builder(this).setTitle("请选择一个类别").setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (map != null) {
                    map.put(Book.LOCAL_CATEGORY, categories[which]);
                    book.setBookByMap(map);
                } else if (book != null) {
                    book.setCategory(categories[which]);
                }
                BookFactory.getInstance(ViewActivity.this).addBook(book);
                Toast.makeText(ViewActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra(IS_DATA, true);
                setResult(RESULT_OK, intent);
                finish();
            }
        }).show();


    }

    void initView() {

        iv_cover = (ImageView) findViewById(R.id.preview_cover);
        tv_info = (TextView) findViewById(R.id.preview_info);
        tv_tag = (TextView) findViewById(R.id.preview_tag);
        tv_summary = (TextView) findViewById(R.id.preview_summary);
        tv_score = (TextView) findViewById(R.id.preview_score);
        layout = (CoordinatorLayout) findViewById(R.id.preview_layout);
        tv_title = (TextView) findViewById(R.id.preview_title);
        btn_save = (Button) findViewById(R.id.preview_save);
        fbtn_edit = (FloatingActionButton) findViewById(R.id.preview_fbtn_edit);
        fbtn_camera = (FloatingActionButton) findViewById(R.id.preview_fbtn_camera);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().getStringExtra("uuid") == null)
            getMenuInflater().inflate(R.menu.isbn, menu);
        else
            getMenuInflater().inflate(R.menu.book_view, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final EditText edt_add;
        switch (item.getItemId()) {
            //以下两项为仅有uuid显示
            case R.id.akey_add:
                startActivity(new Intent(this, AKeyISBNActivity.class));
                break;
            case R.id.isbn_add:
                edt_add = new EditText(this);
                edt_add.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                edt_add.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                new AlertDialog.Builder(this).setView(edt_add).setTitle("提示").setMessage("请输入ISBN码").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isbn = edt_add.getText().toString();
                        startDownBook();
                    }
                }).setCancelable(false).setNegativeButton("返回", null).show();
                break;
            //以下三项仅有uuid存在才显示
            case R.id.book_view_refresh:
                updateBook(uuid);
                break;
            case R.id.book_view_edit:
                Intent intent = new Intent(this, EdtAndAddActivity.class);
                intent.putExtra(EdtAndAddActivity.BOOK_UUID, book.getUuid().toString());
                startActivityForResult(intent, 0);
                break;
            case R.id.book_view_delete:
                new AlertDialog.Builder(this).setTitle("提示").setMessage("确定删除？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BookFactory.getInstance(ViewActivity.this).deleteBook(book);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(BookcaseActivity.IS_DATA, true);
                        setResult(RESULT_OK, resultIntent);
                        Toast.makeText(ViewActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).setNegativeButton("返回", null).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void updateBook(String uuid) {
        book = BookFactory.getInstance(this).getByKeyWord(uuid, new String[]{DbConstance.COLUMN_UUID}, null, null, false).get(0);
        displayBookByModel(book);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data.getBooleanExtra(IS_DATA, false)) {
            Intent intent = new Intent();
            intent.putExtra(BookcaseActivity.IS_DATA, true);
            setResult(RESULT_OK, intent);
            if (uuid != null)
                updateBook(uuid);
            else
                displayBookByModel(BookFactory.tempBook);
        }

    }


}
