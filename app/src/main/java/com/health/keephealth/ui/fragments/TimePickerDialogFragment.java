package com.health.keephealth.ui.fragments;


import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.health.keephealth.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerDialogFragment extends DialogFragment{

    private OnTimeSetListener mTimeSetCallback;

    public interface OnTimeSetListener extends TimePickerDialog.OnTimeSetListener {
    }

    public void setOnTimeSetListener(OnTimeSetListener l) {
        mTimeSetCallback = l;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        return new TimePickerDialog(getActivity(), mTimeSetCallback, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
    }
}
