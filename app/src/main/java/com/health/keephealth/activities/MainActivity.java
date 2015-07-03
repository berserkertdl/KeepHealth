package com.health.keephealth.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.health.keephealth.R;
import com.health.keephealth.ui.fragments.WeightEditDialogFragment;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private ActionBar actionBar;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private EditText etStartTime, etEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initDrawer();

        etStartTime = (EditText) this.findViewById(R.id.et_start_time);
        etEndTime = (EditText) this.findViewById(R.id.et_end_time);

        etStartTime.setOnTouchListener(this);
        etEndTime.setOnTouchListener(this);
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
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            weightDialogFragment.show(fragmentTransaction, "df");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            final Calendar calendar = Calendar.getInstance();

            switch (v.getId()) {
                case R.id.et_start_time:
                    final int year = calendar.get(Calendar.YEAR);
                    final int month = calendar.get(Calendar.MONTH);
                    final int day = calendar.get(Calendar.DAY_OF_MONTH);
                    final DatePickerDialog datePickerDialog = new DatePickerDialog(

                            this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    etStartTime.setText(String.valueOf(year) + "/" +
                                            String.valueOf(monthOfYear + 1) + "/" +
                                            String.valueOf(dayOfMonth));
                                    Toast.makeText(MainActivity.this,
                                            String.valueOf(year) + "/" +
                                                    String.valueOf(monthOfYear + 1) + "/" +
                                                    String.valueOf(dayOfMonth),
                                            Toast.LENGTH_SHORT).show();
                                }
                            },
                            year, month, day);
                    datePickerDialog.show();
                    break;

                case R.id.et_end_time:
                    final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    final int minute = calendar.get(Calendar.HOUR_OF_DAY);
                    final TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            etEndTime.setText(String.valueOf(hourOfDay) + "/" +
                                    String.valueOf(minute));
                        }
                    }, hour, minute, true);
                    timePickerDialog.show();
                    break;
            }
        }
        return true;
    }
}
