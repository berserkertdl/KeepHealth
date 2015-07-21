package com.health.keephealth.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.health.keephealth.R;
import com.health.keephealth.helper.database.DBManager;
import com.health.keephealth.helper.utils.L;
import com.health.keephealth.helper.vo.WeightEntity;
import com.health.keephealth.ui.adapters.WeightAdapter;
import com.health.keephealth.ui.adapters.WeightAdapter2;
import com.health.keephealth.ui.fragments.WeightEditDialogFragment;
import com.health.swipemenulistview.SwipeMenu;
import com.health.swipemenulistview.SwipeMenuCreator;
import com.health.swipemenulistview.SwipeMenuItem;
import com.health.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListActivity extends AppCompatActivity {


    private SwipeMenuListView mListView;
    private WeightAdapter2 weightAdapter;
    private List<WeightEntity> entityList;
    private List items;
    private AlertDialog deleteDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DBManager.initManger(this);
        initList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_add:
                if (entityList == null || entityList.size() == 0) {
                    showWeightEditDialogFragMent(null);
                } else {
                    WeightEntity entity = entityList.get(0);
                    entity.setId(0);
                    entity.setComment("");
                    showWeightEditDialogFragMent(entity);
                }
                return true;

            case R.id.home:
            case R.id.homeAsUp:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void initList(){
        mListView = (SwipeMenuListView)findViewById(R.id.list);
        items = new ArrayList();
        weightAdapter = new WeightAdapter2(this,items);
        mListView.setAdapter(weightAdapter);
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Edit");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);


        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        showWeightEditDialogFragMent((WeightEntity)items.get(position));
                        break;
                    case 1:
                        // delete
//					delete(item);
                        items.remove(position);
                        weightAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        new ListAppTask().execute();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void showWeightEditDialogFragMent(WeightEntity entity) {
        WeightEditDialogFragment weightDialogFragment = WeightEditDialogFragment.getInstance(entity);
        weightDialogFragment.dataChangeListener = new WeightEditDialogFragment.DataChangeListener() {
            public void dataChange() {
                items.clear();
                entityList = DBManager.getAllWeigthInfos();
                items.addAll(parseWeightInfo(entityList));
                weightAdapter.notifyDataSetChanged();
            }
        };
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        weightDialogFragment.show(fragmentTransaction, "df");
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private List parseWeightInfo(List<WeightEntity> lists) {
        String year = "";
        String month = "";
        List items = new ArrayList();
        Calendar cd = Calendar.getInstance();
        for (WeightEntity entity : lists) {
            cd.setTime(entity.getAdd_time());
            if ("".equals(year) && "".equals(month) || !year.equals(cd.get(Calendar.YEAR) + "") || !month.equals(cd.get(Calendar.MONTH) + 1 + "")) {
                year = cd.get(Calendar.YEAR) + "";
                month = cd.get(Calendar.MONTH) + 1 + "";
                items.add(new String[]{year, month});
            }
            items.add(entity);
        }
        ;
        return items;
    }

    public class ListAppTask extends AsyncTask<Void, Void, List> {

        protected List doInBackground(Void... args) {
            return parseWeightInfo(DBManager.getAllWeigthInfos());
        }

        protected void onPostExecute(List result) {
            items.clear();
            items.addAll(result);
            weightAdapter.notifyDataSetChanged();
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }
}
