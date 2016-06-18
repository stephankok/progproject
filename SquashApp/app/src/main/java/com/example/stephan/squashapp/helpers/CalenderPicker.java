package com.example.stephan.squashapp.helpers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Stephan on 8-6-2016.
 *
 */
public class CalenderPicker {

    // Context of activity.
    private Context context;
    private Calendar calendar;
    private AsyncResponse delegate = null;       // initialize to null;

    // Date pickers.
    private DatePickerDialog.OnDateSetListener dateListener;
    private TimePickerDialog.OnTimeSetListener startTimeListener;
    private TimePickerDialog.OnTimeSetListener endTimeListener;

    // Items to give back.
    private List<Integer> date = new ArrayList<>();
    private List<Integer> start= new ArrayList<>();
    private List<Integer> end= new ArrayList<>();

    /**
     * Initialize date picker
     */
    public CalenderPicker(Context context){
        calendar = Calendar.getInstance();
        this.context = context;
    }

    public void changeDate(final List<Integer> date, final TextView datePicked){

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.clear();
                date.add(year);
                date.add(monthOfYear);
                date.add(dayOfMonth);

                calendar.set(date.get(0), date.get(1), date.get(2));
                String dateFormatted =
                        new SimpleDateFormat("EEE, MMM d, ''yy", Locale.US).format(calendar.getTime());
                datePicked.setText(dateFormatted);
            }
        }, date.get(0), date.get(1), date.get(2));

        datePickerDialog.setMessage("Starting time");
        datePickerDialog.show();
    }

    public void changeStart(final List<Integer> start, final TextView startPicked){

        TimePickerDialog startTimePickerDialog =
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        start.clear();
                        start.add(hourOfDay);
                        start.add(minute);
                        String startFormatted =
                                String.valueOf(hourOfDay) + ":" +
                                        String.format( Locale.US, "%02d", minute);
                        startPicked.setText(startFormatted);
                    }
                },start.get(0), start.get(1), true);
        startTimePickerDialog.setMessage("Starting time");
        startTimePickerDialog.show();
    }

    public void changeEnd(final List<Integer> end, final TextView endPicked){

        TimePickerDialog startTimePickerDialog =
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        end.clear();
                        end.add(hourOfDay);
                        end.add(minute);
                        String startFormatted =
                                String.valueOf(hourOfDay) + ":" +
                                        String.format( Locale.US, "%02d", minute);
                        endPicked.setText(startFormatted);
                    }
                },end.get(0), end.get(1), true);
        startTimePickerDialog.setMessage("End time");
        startTimePickerDialog.show();
    }

    /**
     * Function in the activity to give the information.
     * ! So these functions must be present!
     */
    public interface AsyncResponse{
        void newTrainingDateSelected(final List<Integer> date, final List<Integer> start, final List<Integer> end);
    }

    public void newTraining(final AsyncResponse delegate){
        // Make sure they are empty
        date.clear();
        start.clear();
        end.clear();

        // Set response
        this.delegate = delegate;

        // Set listeners that will check if you have set the date.
        setListeners();

        // Call first DatePicker
        callDatePicker();
    }

    /**
     * Set a DatePicker to UI.
     */
    private void callDatePicker(){
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(context, dateListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setMessage("Select date");
        datePickerDialog.show();
    }

    /**
     * Set a TimePicker to UI.
     */
    private void callStartTimePicker(){
        TimePickerDialog startTimePickerDialog =
                new TimePickerDialog(context,startTimeListener,
                        calendar.get(Calendar.HOUR_OF_DAY), 0, true);
        startTimePickerDialog.setMessage("Starting time");
        startTimePickerDialog.show();
    }

    /**
     * Set a TimePicker to UI.
     */
    private void callEndTimePicker(){
        TimePickerDialog endTimePickerDialog =
                new TimePickerDialog(context, endTimeListener,
                        calendar.get(Calendar.HOUR_OF_DAY), 0, true);
        endTimePickerDialog.setMessage("End time");
        endTimePickerDialog.show();
    }

    /**
     * Set listeners to Date- and Time- PickerDialog
     */
    private void setListeners(){
        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // add values
                date.add(year);
                date.add(monthOfYear);
                date.add(dayOfMonth);

                // now call start time.
                callStartTimePicker();
            }
        };

        startTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // set starting time.
                start.add(hourOfDay);
                start.add(minute);

                // call end time
                callEndTimePicker();
            }
        };

        endTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Set ending time.
                end.add(hourOfDay);
                end.add(minute);

                // Done call back to UI.
                delegate.newTrainingDateSelected(date, start, end);
            }
        };
    }
}
