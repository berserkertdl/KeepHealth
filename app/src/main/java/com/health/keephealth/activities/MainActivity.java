package com.health.keephealth.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.health.keephealth.R;
import com.health.keephealth.helper.database.DBManager;
import com.health.keephealth.helper.utils.L;
import com.health.keephealth.helper.vo.WeightEntity;
import com.health.keephealth.ui.adapters.WeightAdapter;
import com.health.keephealth.ui.fragments.WeightEditDialogFragment;
import com.health.swipelistview.BaseSwipeListViewListener;
import com.health.swipelistview.SwipeListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ActionBar actionBar;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private EditText etStartTime, etEndTime;
    private ListView listView;
    private SimpleCursorAdapter cursorAdapter;
    private Cursor cursor = null;

    private SwipeListView mSwipeListView;
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);

        DBManager.initManger(this);
        initToolBar();
        initDrawer();
//        initWeightData();
        findView();

    }

    private WeightAdapter weightAdapter;
    private List<WeightEntity> entityList;
    private List items;

    private void initWeightData() {
        entityList = DBManager.getAllWeigthInfos();
        items = parseWeightInfo(entityList);
        weightAdapter = new WeightAdapter(MainActivity.this, items);
        listView.setAdapter(weightAdapter);
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

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        // 實作 drawer toggle 並放入 toolbar
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void initToolBar() {
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.actionbar_title);

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
        if (id == R.id.menu_add) {
            WeightEditDialogFragment weightDialogFragment = new WeightEditDialogFragment();
            weightDialogFragment.dataChangeListener = new WeightEditDialogFragment.DataChangeListener() {
                public void dataChange() {
                    items.removeAll(items);
                    items.addAll(parseWeightInfo(DBManager.getAllWeigthInfos()));
                    weightAdapter.notifyDataSetChanged();
                }
            };
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            weightDialogFragment.show(fragmentTransaction, "df");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void findView() {
        items = parseWeightInfo(DBManager.getAllWeigthInfos());
        weightAdapter = new WeightAdapter(MainActivity.this, items);
        mSwipeListView = (SwipeListView) findViewById(R.id.mSwipeListView);
        mSwipeListView.setSwipeListViewListener(new BaseSwipeListViewListener() {

            @Override
            public void onStartOpen(int position, int action,
                                    boolean right) {

                L.d(TAG, "onStartOpen");
            }

            @Override
            public void onStartClose(int position, boolean right) {

                L.d(TAG, "onStartClose");
            }

            @Override
            public void onClickFrontView(int position) {

                L.d(TAG, "onClickFrontView");
            }

            @Override
            public void onClickBackView(int position) {

                L.d(TAG, "onClickBackView");
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {

                L.d(TAG, "onDismiss");
                for (int position : reverseSortedPositions) {
                    items.remove(position);
                }
                weightAdapter.notifyDataSetChanged();
            }
        });

        mSwipeListView.setAdapter(weightAdapter);

        mSwipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_BOTH);
        mSwipeListView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
        mSwipeListView.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
        mSwipeListView.setOffsetLeft(getResources().getDimension(R.dimen.left_offset));
        mSwipeListView.setOffsetRight(getResources().getDimension(R.dimen.right_offset));
        mSwipeListView.setAnimationTime(200);
        mSwipeListView.setSwipeOpenOnLongPress(true);
    }
}
