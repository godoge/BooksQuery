package com.example.administrator.books.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.books.AddBtnAnim;
import com.example.administrator.books.Constance;
import com.example.administrator.books.DbConstance;
import com.example.administrator.books.DbTools;
import com.example.administrator.books.ImageUtils;
import com.example.administrator.books.LoadAbookAsync;
import com.example.administrator.books.LoadBookAsyncTask;
import com.example.administrator.books.R;
import com.example.administrator.books.adapter.CategoryAdapter;
import com.example.administrator.books.adapter.TagAdapter;
import com.example.administrator.books.factory.BookFactory;
import com.example.administrator.books.factory.CategoryFactory;
import com.example.administrator.books.factory.TagsFactory;
import com.example.administrator.books.model.Book;
import com.example.administrator.books.model.BookTag;
import com.example.administrator.books.model.Category;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import net.lzzy.bookfinder.ApiConstants;
import net.lzzy.bookfinder.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/6/7.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, AdapterView.OnItemLongClickListener {
    private FloatingActionButton btn_addBooks;
    private FloatingActionButton btn_addBooksFromNet;
    private CoordinatorLayout layout_float;
    private FloatingActionButton btn_addBooksFromManual;
    private FloatingActionButton btn_addBooksFromQRCode;
    private List<BookTag> tagList;
    private GridView gv_tag;
    private TagAdapter myAdapter;
    Boolean operable = true;
    Boolean nextIsExpand = true;
    private ViewPager viewPager;
    private List<View> views;
    private View view_category;
    private View view_tag;
    private ListView lv_category;
    private Button btn_add_category;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    int currentPager;
    private ProgressDialog progressDialog;

    void updateCategoryAdapter() {
        CategoryFactory.getInstance(this).setCategoryFromDb();
        categoryAdapter.notifyDataSetChanged();

    }

    void updateTagAdapter() {
        TagsFactory.getInstance(this).setBookTags();
        myAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                if (currentPager == 1)
                    updateTagAdapter();
                if (currentPager == 0)
                    updateCategoryAdapter();
                break;
            case R.id.search:
                Intent intent = new Intent(this, SearchBookActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.context_action_bar, menu);
        menu.findItem(R.id.book_case_add).setVisible(false);
        menu.findItem(R.id.search).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initValue();

        initView();
        initListener();
        setViewAdapter();

    }

    private void operable() {
        if (!operable)
            return;
        operable = false;
        btn_addBooks.setClickable(false);
        if (nextIsExpand) {
            nextIsExpand = false;
            AddBtnAnim.expand(this, btn_addBooksFromQRCode, btn_addBooksFromNet, btn_addBooksFromManual);
            addBtnRotate(false);
        } else {
            AddBtnAnim.shrink(this, btn_addBooksFromQRCode, btn_addBooksFromNet, btn_addBooksFromManual);
            addBtnRotate(true);
            nextIsExpand = true;
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                btn_addBooks.setClickable(true);
                operable = true;
            }
        }, 300);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.activity_bookcase_btn_add:
                operable();
                break;
            case R.id.activity_bookcase_btn_importFromManual:
                intent = new Intent(this, EdtAndAddActivity.class);
                startActivity(intent);
                operable();
                break;
            case R.id.activity_bookcase_btn_importFromNet:
                operable();
                intent = new Intent(this, ViewActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_bookcase_btn_importFromQRCode:
                operable();
                intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.pager_main_btn_category:
                final EditText editText = new EditText(this);
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                new AlertDialog.Builder(this).setTitle("新增类别").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String edt_category = editText.getText().toString();
                        if (edt_category.length() == 0)
                            return;
                        Category category = new Category();
                        category.setName(edt_category);
                        CategoryFactory.getInstance(MainActivity.this).addCategory(category);
                        categoryAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();

                    }
                }).setView(editText).setCancelable(false).setNegativeButton("返回", null).show();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == 1) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("提示");
            progressDialog.setMessage("获取中...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Bundle bundle = data.getExtras();
            String isbn = bundle.getString("result");
            new LoadAbookAsync() {
                @Override
                protected void onPostExecute(Book book) {
                    super.onPostExecute(book);
                    progressDialog.dismiss();
                    if (book == null) {
                        Toast.makeText(MainActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    BookFactory.tempBook = book;
                    Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                    startActivity(intent);

                }
            }.execute(isbn);
        }


    }

    void addBtnRotate(boolean b) {
        if (b)
            btn_addBooks.setImageResource(R.drawable.ic_add_open);
        else
            btn_addBooks.setImageResource(R.drawable.btn_add_close);
    }


    void initListener() {
        lv_category.setOnTouchListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setTitle("类别");
                        currentPager = 0;
                        break;
                    case 1:
                        currentPager = 1;
                        setTitle("标签");
                        break;


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btn_add_category.setOnClickListener(this);
        btn_addBooks.setOnClickListener(this);
        layout_float.setOnTouchListener(this);
        btn_addBooksFromQRCode.setOnClickListener(this);
        btn_addBooksFromManual.setOnClickListener(this);
        btn_addBooksFromNet.setOnClickListener(this);
        lv_category.setOnItemLongClickListener(this);
        lv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, BookcaseActivity.class);
                intent.putExtra("style", BookcaseActivity.STYLE_CATEGORY);
                intent.putExtra(BookcaseActivity.INTENT_CATEGORY, categoryList.get(position).getName());
                startActivity(intent);
            }
        });
        gv_tag.setOnTouchListener(this);


        gv_tag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, BookcaseActivity.class);
                intent.putExtra("style", BookcaseActivity.STYLE_TAG);
                intent.putExtra(BookcaseActivity.INTENT_TAG, tagList.get(position).getName());
                startActivity(intent);
            }
        });

    }

    private void initValue() {
        setTitle("类别");
        Constance.dbTools = new DbTools(this, DbConstance.DB_NAME, null, 1);
        categoryList = CategoryFactory.getInstance(this).setCategoryFromDb().getList();
        categoryAdapter = new CategoryAdapter(this, categoryList);
        views = new ArrayList<>();
        view_category = getLayoutInflater().inflate(R.layout.pager_category, null);
        views.add(view_category);
        view_tag = getLayoutInflater().inflate(R.layout.pager_tag, null);
        views.add(view_tag);
        tagList = TagsFactory.getInstance(this).setBookTags().getBookTags();
        myAdapter = new TagAdapter(this, tagList);
    }

    private void setViewAdapter() {
        gv_tag.setAdapter(myAdapter);
        lv_category.setAdapter(categoryAdapter);

    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            container.removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    };

    private void initView() {
        gv_tag = (GridView) view_tag.findViewById(R.id.activity_main_gv_menu);
        lv_category = (ListView) view_category.findViewById(R.id.pager_main_lv_category);

        btn_add_category = (Button) view_category.findViewById(R.id.pager_main_btn_category);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        btn_addBooks = (FloatingActionButton) findViewById(R.id.activity_bookcase_btn_add);
        btn_addBooksFromNet = (FloatingActionButton) findViewById(R.id.activity_bookcase_btn_importFromNet);
        btn_addBooksFromManual = (FloatingActionButton) findViewById(R.id.activity_bookcase_btn_importFromManual);
        btn_addBooksFromQRCode = (FloatingActionButton) findViewById(R.id.activity_bookcase_btn_importFromQRCode);
        layout_float = (CoordinatorLayout) findViewById(R.id.layout_float);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            switch (v.getId()) {
                case R.id.activity_main_gv_menu:
                case R.id.layout_float:
                case R.id.pager_main_lv_category:
                    if (operable && !nextIsExpand) {
                        AddBtnAnim.shrink(this, btn_addBooksFromQRCode, btn_addBooksFromNet, btn_addBooksFromManual);
                        addBtnRotate(true);
                        nextIsExpand = true;
                    }
                    break;
            }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTagAdapter();
        updateCategoryAdapter();


    }

    @Override
    public void onBackPressed() {
        exitDialog();
    }

    void exitDialog() {
        new AlertDialog.Builder(this).setTitle("退出提示").setMessage("确认退出？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(1);
            }
        }).setNegativeButton("返回", null).show();


    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new AlertDialog.Builder(this).setTitle("选择").setItems(R.array.category_menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (position == 0) {
                            Toast.makeText(getApplicationContext(), "无法删除默认类别", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (Constance.dbTools.getKeyWordCount(DbConstance.BOOK_COLUMN_CATEGORY, categoryList.get(position).getName()) != 0) {
                            Toast.makeText(getApplicationContext(), "此类别还有书，无法删除", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        new AlertDialog.Builder(MainActivity.this).setTitle("是否删除?").setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CategoryFactory.getInstance(getApplicationContext()).delete(categoryList.get(position));
                                Toast.makeText(getApplicationContext(), "已删除", Toast.LENGTH_SHORT).show();
                                updateCategoryAdapter();
                            }
                        }).setNegativeButton("否", null).show();
                        break;
                }
            }
        }).show();


        return true;
    }


}
