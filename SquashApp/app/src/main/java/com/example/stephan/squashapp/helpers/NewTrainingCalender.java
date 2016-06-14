package com.example.stephan.squashapp.helpers;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.provider.CalendarContract;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.stephan.squashapp.models.Training;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Stephan on 8-6-2016.
 *
 */
public class NewTrainingCalender {

    private Context context;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener dateListener;
    private TimePickerDialog.OnTimeSetListener startTimeListener;
    private TimePickerDialog.OnTimeSetListener endTimeListener;
    private String date;
    private String start;
    private String end;
    private AsyncResponse delegate = null;       // initialize to null;

    /**
     * Function in the activity to give the information.
     * ! So these functions must be present!
     */
    public interface AsyncResponse{
        void newTrainingDateSelected(final String date, final String start, final String end);
    }

    public NewTrainingCalender(Context context, final AsyncResponse delegate){
        this.context = context;
        this.calendar = Calendar.getInstance();
        this.delegate = delegate;
        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,monthOfYear,dayOfMonth);
                date = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.US).format(calendar.getTime());
                initiate2();
            }
        };

        startTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                start = String.valueOf(hourOfDay) + ":" + String.format( Locale.US, "%02d", minute);
                initiate3();
            }
        };

        endTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                end = String.valueOf(hourOfDay) + ":" + String.format(Locale.US, "%02d", minute);
                delegate.newTrainingDateSelected(date, start, end);
            }
        };

        initiate();
    }

    private void initiate(){
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(context, AlertDialog.THEME_HOLO_DARK, dateListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Select date");
        datePickerDialog.show();
    }

    private void initiate2(){
        TimePickerDialog startTimePickerDialog =
                new TimePickerDialog(context, AlertDialog.THEME_HOLO_DARK, startTimeListener,
                        calendar.get(Calendar.HOUR_OF_DAY), 0, true);
        startTimePickerDialog.setTitle("Select starting time");
        startTimePickerDialog.show();
    }

    private void initiate3(){
        TimePickerDialog endTimePickerDialog =
                new TimePickerDialog(context, AlertDialog.THEME_HOLO_DARK, endTimeListener,
                        calendar.get(Calendar.HOUR_OF_DAY), 0, true);
        endTimePickerDialog.setTitle("Select ending time");
        endTimePickerDialog.show();
    }
}
