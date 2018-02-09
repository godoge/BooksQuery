package com.example.administrator.books.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.administrator.books.DbConstance;
import com.example.administrator.books.R;
import com.example.administrator.books.adapter.BookCaseAdapter;
import com.example.administrator.books.factory.BookFactory;
import com.example.administrator.books.model.Book;

import java.util.List;

/**
 * Created by Administrator on 2016/6/19.
 */
public class SearchBookActivity extends AppCompatActivity {

    private GridView gv_books;
    List<Book> books;
    private SearchView searchView;
    private BookCaseAdapter myAdapter;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("书籍搜索");
        setContentView(R.layout.activity_dialog_search);
        gv_books = (GridView) findViewById(R.id.activity_sv_book);
        layout = (LinearLayout) findViewById(R.id.activity_dialog_rootLayout);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (width * 0.8), (int) (height * 0.6));
        params.gravity = Gravity.CENTER_HORIZONTAL;
        layout.setLayoutParams(params);
        books = BookFactory.getInstance(this).getBooks();
        books.clear();
        myAdapter = new BookCaseAdapter(this, books, (int) (width * 0.8));
        gv_books.setAdapter(myAdapter);
        gv_books.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewActivity.class);
                intent.putExtra(ViewActivity.ACTION_BOOK_UUID, books.get(position).getUuid().toString());
                startActivityForResult(intent, 1);
            }
        });
        gv_books.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        gv_books.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            private boolean isAllSelected;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                mode.setTitle("已选择:" + gv_books.getCheckedItemCount());
                if (gv_books.getCheckedItemCount() == gv_books.getCount()) {
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
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        for (int i = books.size() - 1; i >= 0; i--) {
                            if (gv_books.isItemChecked(i))
                                BookFactory.getInstance(getApplicationContext()).deleteBook(books.get(i));
                        }
                        Toast.makeText(getApplicationContext(), "已删除", Toast.LENGTH_SHORT).show();
                        mode.finish();
                        myAdapter.notifyDataSetChanged();
                        break;
                    case R.id.menu_all_select:
                        if (!isAllSelected)
                            for (int i = 0; i < gv_books.getCount(); i++)
                                gv_books.setItemChecked(i, true);
                        else
                            for (int i = 0; i < gv_books.getCount(); i++)
                                gv_books.setItemChecked(i, false);
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case 1:
                if (data.getBooleanExtra(ViewActivity.IS_DATA, false)) {
                    String query = searchView.getQuery().toString();
                    BookFactory.getInstance(getApplicationContext()).getBookFrom(query, new String[]{DbConstance.COLUMN_AUTHOR, DbConstance.COLUMN_TITLE}, null, null, false);
                    myAdapter.notifyDataSetChanged();
                }
                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_to_top);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        overridePendingTransition(0, R.anim.slide_out_to_top);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem search = menu.findItem(R.id.activity_search_book);
        searchView = new SearchView(this);
        searchView.setQueryHint("请输入书名或作者");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    books.clear();
                    myAdapter.notifyDataSetChanged();
                    return false;
                }
                BookFactory.getInstance(getApplicationContext()).getBookFrom(newText, new String[]{DbConstance.COLUMN_AUTHOR, DbConstance.COLUMN_TITLE}, null, null, false);
                myAdapter.notifyDataSetChanged();
                return true;
            }
        });
        searchView.setIconifiedByDefault(false);
        search.setActionView(searchView);
        searchView.setIconifiedByDefault(false);
        search.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }


}
