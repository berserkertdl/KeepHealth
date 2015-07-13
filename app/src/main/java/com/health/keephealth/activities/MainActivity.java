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
import com.health.keephealth.helper.vo.WeightEntity;
import com.health.keephealth.ui.adapters.WeightAdapter;
import com.health.keephealth.ui.fragments.WeightEditDialogFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBManager.initManger(this);

        listView = (ListView) findViewById(R.id.list);
        cursor = DBManager.getAllWeigthInfo();
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.weight_list_item_layout, cursor, new String[]{"weight", "add_time"}, new int[]{R.id.weight, R.id.add_time}, 0);
        listView.setAdapter(cursorAdapter);

        initToolBar();
        initDrawer();

    }

    private WeightAdapter weightAdapter;
    private List<WeightEntity> entityList;

    private void initWeightData(){
        entityList = DBManager.getAllWeigthInfos();
        weightAdapter = new WeightAdapter(MainActivity.this,entityList);
        

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
                    cursor = DBManager.getAllWeigthInfo();
                    cursorAdapter.changeCursor(cursor);
                    cursorAdapter.notifyDataSetChanged();
                }
            };
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            weightDialogFragment.show(fragmentTransaction, "df");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
