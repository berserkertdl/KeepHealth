package com.health.keephealth.activities;

import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.health.keephealth.R;
import com.health.keephealth.helper.database.DBManager;
import com.health.keephealth.helper.utils.L;
import com.health.keephealth.helper.vo.PackageItem;
import com.health.keephealth.helper.vo.WeightEntity;
import com.health.keephealth.ui.adapters.WeightAdapter;
import com.health.keephealth.ui.fragments.WeightEditDialogFragment;
import com.health.swipelistview.BaseSwipeListViewListener;
import com.health.swipelistview.SwipeListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.widget.AbsListView;


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

//        listView = (ListView) findViewById(R.id.list);

        DBManager.initManger(this);
        initToolBar();
//        initDrawer();
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
       /* mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        // 實作 drawer toggle 並放入 toolbar
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);*/
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

    private List<PackageItem> data;
    private ProgressDialog progressDialog;

    private void findView() {

        items = new ArrayList();
        weightAdapter = new WeightAdapter(MainActivity.this, items);
        mSwipeListView = (SwipeListView) findViewById(R.id.mSwipeListView);
        mSwipeListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mSwipeListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                      long id, boolean checked) {
                    mode.setTitle("Selected (" + mSwipeListView.getCountSelected() + ")");
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_delete:
                            mSwipeListView.dismissSelected();
                            mode.finish();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.menu_choice_items, menu);
                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    mSwipeListView.unselectedChoiceStates();
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }
            });
        }*/

        /*mSwipeListView.setSwipeListViewListener(new BaseSwipeListViewListener() {

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
        });*/

        mSwipeListView.setAdapter(weightAdapter);

        mSwipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_BOTH);
        mSwipeListView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
        mSwipeListView.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
        mSwipeListView.setOffsetLeft(convertDpToPixel(getResources().getDimension(R.dimen.left_offset)));
        mSwipeListView.setOffsetRight(convertDpToPixel(getResources().getDimension(R.dimen.right_offset)));
        mSwipeListView.setAnimationTime(0);
        mSwipeListView.setSwipeOpenOnLongPress(true);

        new ListAppTask().execute();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
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
