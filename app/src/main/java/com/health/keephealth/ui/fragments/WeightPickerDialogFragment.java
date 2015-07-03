package com.health.keephealth.ui.fragments;


import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.health.keephealth.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeightPickerDialogFragment extends DialogFragment implements NumberPicker.OnValueChangeListener,AlertDialog.OnClickListener{

    private NumberPicker firstNumberPicker,lastNumberPicker;

    private final int f_maxValue = 500;
    private final int l_maxValue = 99;
    private final int f_minValue = 0;
    private final int l_minValue = 0;
    private int f_default = 50;
    private int l_default = 50;
    private int f_old_val,f_new_val,l_old_val,l_new_val;
    private double old_val,new_val;

    private OnValueChangeListener mValueChangeCallback;


    public static WeightPickerDialogFragment newInstance(String val){
        WeightPickerDialogFragment weightPickerDialogFragment = new WeightPickerDialogFragment();
        if(val!=null&&"".equals(val)){
            String[] arr = val.split(".");
            weightPickerDialogFragment.f_default = Integer.parseInt(arr[0]);
            weightPickerDialogFragment.l_default = Integer.parseInt(arr[1]);
        }
        return weightPickerDialogFragment;
    }
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//        switch (picker.getId()){
//            case R.id.first_num_picker:
//                f_old_val = oldVal;
//                f_new_val = newVal;
//                old_val = Double.parseDouble(f_old_val+"."+l_old_val);
//                new_val = Double.parseDouble(f_new_val+"."+l_new_val);
//                break;
//
//            case R.id.last_num_picker:
//                l_old_val = oldVal;
//                l_new_val = newVal;
//                old_val = Double.parseDouble(f_old_val+"."+l_old_val);
//                new_val = Double.parseDouble(f_new_val+"."+l_new_val);
//                break;
//        }

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(mValueChangeCallback!=null){
            f_new_val = firstNumberPicker.getValue();
            l_new_val = lastNumberPicker.getValue();
            new_val = Double.parseDouble(f_new_val + "." + l_new_val);
            mValueChangeCallback.onValueChange(old_val,new_val);
        }
    }

    public interface OnValueChangeListener {
        void onValueChange(double oldVal, double newVal);
    }

    public void setOnValueChangeListener(OnValueChangeListener l) {
        mValueChangeCallback = l;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.weight_dialog,null);
        firstNumberPicker = (NumberPicker)view.findViewById(R.id.first_num_picker);
        lastNumberPicker = (NumberPicker)view.findViewById(R.id.last_num_picker);
        firstNumberPicker.setMaxValue(f_maxValue);
        firstNumberPicker.setMinValue(f_minValue);
        firstNumberPicker.setValue(f_default);
        lastNumberPicker.setMaxValue(l_maxValue);
        lastNumberPicker.setMinValue(l_minValue);
        lastNumberPicker.setValue(l_default);
//        lastNumberPicker.setOnValueChangedListener(this);
//        firstNumberPicker.setOnValueChangedListener(this);
        builder.setView(view);
        builder.setTitle(R.string.dialog_choose_weight);
        builder.setNegativeButton(R.string.dialog_ok,this);
        return builder.create();
    }
}
