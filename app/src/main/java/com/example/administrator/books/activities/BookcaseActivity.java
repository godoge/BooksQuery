package com.example.administrator.books.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.books.AnyEvent;
import com.example.administrator.books.DbConstance;
import com.example.administrator.books.LoadAbookAsync;
import com.example.administrator.books.R;
import com.example.administrator.books.adapter.BookCaseAdapter;
import com.example.administrator.books.factory.BookFactory;
import com.example.administrator.books.model.Book;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class BookcaseActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {


    private GridView gv_main;
    private BookCaseAdapter bookCaseAdapter;
    private List<Book> books;
    Boolean isAllSelected = false;
    private SearchView searchView;
    public static final int STYLE_TAG = 0;
    public static final int STYLE_CATEGORY = 1;
    private TextView tv_title;
    private String selectedCategory;
    private int style;
    boolean isSearching = false;
    public static final String INTENT_CATEGORY = "intent category";
    public static final String IS_DATA = "is data";
    private AlertDialog.Builder dialog;
    private ProgressDialog progressDialog;
    private String selectedTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookcase);
        initValue();
        initView();
        setViewListener();
        setViewAdapter();
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.context_action_bar, menu);
        if (selectedTag != null)
            return true;
        MenuItem item_search = menu.findItem(R.id.search);
        searchView = new SearchView(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals(""))
                    isSearching = false;
                else
                    isSearching = true;
                Log.d("MyInfo_isSearching", isSearching + "");
                BookFactory.getInstance(BookcaseActivity.this).getBookFrom(newText, new String[]{DbConstance.COLUMN_TITLE, DbConstance.COLUMN_AUTHOR}, new String[]{DbConstance.BOOK_COLUMN_CATEGORY}, selectedCategory,
                        false);
                bookCaseAdapter.notifyDataSetChanged();
                return true;
            }
        });
        item_search.setActionView(searchView);
        item_search.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    void updateBookcase() {
        switch (style) {
            case STYLE_TAG:
                BookFactory.getInstance(this).getBookFrom(selectedCategory, new String[]{DbConstance.COLUMN_TAGS}, null, null, false);
                break;
            case STYLE_CATEGORY:
                BookFactory.getInstance(this).getBookFrom(selectedCategory, new String[]{DbConstance.BOOK_COLUMN_CATEGORY}, null, null, false);
                break;
        }
        bookCaseAdapter.notifyDataSetChanged();
    }

    void initValue() {
        selectedCategory = getIntent().getStringExtra(INTENT_CATEGORY);
        selectedTag = getIntent().getStringExtra(INTENT_TAG);
        style = getIntent().getIntExtra("style", 0);
        switch (style) {
            case STYLE_CATEGORY:
                setTitle("类别");
                BookFactory.getInstance(this).getBookFrom(selectedCategory, new String[]{DbConstance.BOOK_COLUMN_CATEGORY}, null, null, false);
                break;
            case STYLE_TAG:
                setTitle("标签");
                BookFactory.getInstance(this).getBookFrom(selectedTag, new String[]{DbConstance.COLUMN_TAGS}, null, null, false);
                break;

        }
        books = BookFactory.getInstance(this).getBooks();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                if (!isSearching)
                    updateBookcase();
                break;
            case R.id.book_case_add:
                new AlertDialog.Builder(this).setItems(R.array.addOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;

                        switch (which) {
                            case 0:
                                intent = new Intent(getApplicationContext(), EdtAndAddActivity.class);
                                intent.putExtra(EdtAndAddActivity.INTENT_CATEGORY, selectedCategory);
                                startActivityForResult(intent, 0);
                                break;
                            case 1:
                                intent = new Intent(getApplicationContext(), CaptureActivity.class);
                                startActivityForResult(intent, 1);
                                break;
                            case 2:
                                intent = new Intent(getApplicationContext(), ViewActivity.class);
                                intent.putExtra(ViewActivity.INTENT_CATEGORY, selectedCategory);
                                startActivityForResult(intent, 2);
                                break;
                        }
                    }
                }).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    void setViewAdapter() {
        Log.i("Info_width", gv_main.getWidth() + "");
        bookCaseAdapter = new BookCaseAdapter(this, BookFactory.getInstance(this).getBooks(), getResources().getDisplayMetrics().widthPixels);
        gv_main.setAdapter(bookCaseAdapter);
    }


    @Subscribe
    public void onEvent(AnyEvent anyEvent) {
        bookCaseAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void setViewListener() {

        gv_main.setOnTouchListener(this);
        gv_main.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gv_main.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {


            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                mode.setTitle("已选择:" + gv_main.getCheckedItemCount());
                if (gv_main.getCheckedItemCount() == gv_main.getCount()) {
                    isAllSelected = true;
                    mode.getMenu().findItem(R.id.menu_all_select).setTitle("取消");
                } else {
                    isAllSelected = false;
                    mode.getMenu().findItem(R.id.menu_all_select).setTitle("全选");
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.gv_action_bar, menu);
                mode.setTitle("选择");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete:

                        if (gv_main.getCheckedItemCount() < 1)
                            return false;

                        new AlertDialog.Builder(BookcaseActivity.this).setTitle("提示").setMessage("确定删除？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int size = books.size();
                                for (int i = size - 1; i >= 0; i--)
                                    if (gv_main.isItemChecked(i))
                                        BookFactory.getInstance(BookcaseActivity.this).deleteBook(books.get(i));
                                Toast.makeText(BookcaseActivity.this, "已删除", Toast.LENGTH_SHORT).show();
                                mode.finish();
                                bookCaseAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("返回", null).show();
                        break;
                    case R.id.menu_all_select:
                        if (!isAllSelected)
                            for (int i = 0; i < gv_main.getCount(); i++)
                                gv_main.setItemChecked(i, true);
                        else
                            for (int i = 0; i < gv_main.getCount(); i++)
                                gv_main.setItemChecked(i, false);
                        break;


                }


                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        gv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BookcaseActivity.this, ViewActivity.class);
                intent.putExtra("action", ViewActivity.FROM_VIEW);
                intent.putExtra(ViewActivity.ACTION_BOOK_UUID, books.get(position).getUuid().toString());
                startActivityForResult(intent, 0);
            }
        });


    }

    void initView() {
        gv_main = (GridView) findViewById(R.id.activity_gv_main);
        tv_title = (TextView) findViewById(R.id.bookcase_title);
        if (selectedCategory != null)
            tv_title.setText(selectedCategory);
        else if (getIntent().getStringExtra(INTENT_TAG) != null)
            tv_title.setText(getIntent().getStringExtra(INTENT_TAG));

    }

    public static final String INTENT_TAG = "intent tag";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case 1:
                progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("提示");
                progressDialog.setMessage("获取中...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                Bundle bundle = data.getExtras();
                String isbn = bundle.getString("result");
                new LoadAbookAsync() {
                    @Override
                    protected void onPostExecute(Book book) {
                        super.onPostExecute(book);
                        progressDialog.dismiss();
                        if (book == null) {
                            Toast.makeText(getApplicationContext(), "获取失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        BookFactory.tempBook = book;
                        Intent intent = new Intent(getApplicationContext(), ViewActivity.class);
                        intent.putExtra(ViewActivity.INTENT_CATEGORY, selectedCategory);
                        startActivityForResult(intent, 0);

                    }
                }.execute(isbn);
                break;
        }
        if (data.getBooleanExtra(IS_DATA, false))
            updateBookcase();

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
