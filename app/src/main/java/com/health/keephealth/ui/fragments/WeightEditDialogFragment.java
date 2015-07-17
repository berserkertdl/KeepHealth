package com.health.keephealth.ui.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.health.keephealth.R;
import com.health.keephealth.helper.database.DBThread;
import com.health.keephealth.helper.vo.WeightEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeightEditDialogFragment extends DialogFragment implements DialogInterface.OnClickListener, View.OnTouchListener {


    private EditText datetxt, timeTxt, weightTxt, commentTxt;

    public DataChangeListener dataChangeListener;
    private WeightEntity mEntity;
    private Handler handler;

    public static WeightEditDialogFragment getInstance(WeightEntity entity){
        WeightEditDialogFragment dialogFragment =  new WeightEditDialogFragment();
        if(entity==null){
            entity = new WeightEntity();
        }
        dialogFragment.mEntity = entity;
        return dialogFragment;
    }

    public interface DataChangeListener{
        public void dataChange();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.weight_edit_dialog, null);
        View view1 = view.findViewById(R.id.date);
        View view2 = view.findViewById(R.id.time);
        datetxt = (EditText) view.findViewById(R.id.date_txt);
        timeTxt = (EditText) view.findViewById(R.id.time_txt);
        weightTxt = (EditText) view.findViewById(R.id.weight_txt);
        commentTxt = (EditText) view.findViewById(R.id.comment_txt);
        weightTxt.setText(mEntity.getWeight()+"");
        commentTxt.setText(mEntity.getComment());
        Date date = mEntity.getAdd_time();
        if(date==null){
            date = new Date();
        }
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        datetxt.setText(String.valueOf(cd.get(Calendar.YEAR)) + "年" +
                String.valueOf(cd.get(Calendar.MONTH) + 1) + "月" +
                String.valueOf(cd.get(Calendar.DAY_OF_MONTH)) + "日");

        timeTxt.setText(String.valueOf(cd.get(Calendar.HOUR_OF_DAY)) + ":" +
                String.valueOf(cd.get(Calendar.MINUTE)));

        datetxt.setOnTouchListener(this);
        timeTxt.setOnTouchListener(this);
        weightTxt.setOnTouchListener(this);
        builder.setPositiveButton(R.string.dialog_ok, this);
        builder.setNegativeButton(R.string.dialog_cancel, this);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            // ok
            case -1:
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 0:
                                dataChangeListener.dataChange();
                                break;
                        }
                    }
                };
                new DBThread().new WeightSaveOrUpdateThread(handler, getWeight()).start();
                //cancel
            case -2:
                break;
            //ignore
            case -3:
                break;
        }
    }

    private WeightEntity getWeight() {
        float weight = Float.parseFloat(weightTxt.getText().toString());
        String comment = commentTxt.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date add_time = null;
        try {
            add_time = dateFormat.parse(datetxt.getText().toString() + " " + timeTxt.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        WeightEntity entity = new WeightEntity(weight, add_time, comment);
        entity.setId(mEntity.getId());
        return entity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.date_txt:
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                datePickerDialogFragment.setOnDateSetListener(new DatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        datetxt.setText(String.valueOf(year) + "年" +
                                String.valueOf(monthOfYear + 1) + "月" +
                                String.valueOf(dayOfMonth) + "日");
                    }
                });
                datePickerDialogFragment.show(ft, "dateDialog");
                break;

            case R.id.time_txt:
                TimePickerDialogFragment timePickerDialogFragment = new TimePickerDialogFragment();
                timePickerDialogFragment.setOnTimeSetListener(new TimePickerDialogFragment.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeTxt.setText(String.valueOf(hourOfDay) + ":" +
                                String.valueOf(minute));
                    }
                });
                timePickerDialogFragment.show(ft, "timeDialog");
                break;

            case R.id.weight_txt:
                WeightPickerDialogFragment weightPickerDialogFragment = WeightPickerDialogFragment.newInstance(weightTxt.getText().toString());
                weightPickerDialogFragment.setOnValueChangeListener(new WeightPickerDialogFragment.OnValueChangeListener() {
                    @Override
                    public void onValueChange(double oldVal, double newVal) {
                        weightTxt.setText(newVal + "");
                    }
                });
                weightPickerDialogFragment.show(ft, "weightDialog");
                break;
        }
        return true;
    }
}
