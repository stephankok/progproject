package nl.mprog.stephan.squashapp.helpers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import nl.mprog.stephan.squashapp.models.Training;

/**
 * Create Date/Time Picker Dialog/
 */
public class CalenderPicker {

    // Context of activity.
    private Context context;
    private Calendar calendar;
    private AsyncResponse delegate = null;       // initialize to null

    // Date pickers.
    private DatePickerDialog.OnDateSetListener dateListener;
    private TimePickerDialog.OnTimeSetListener startTimeListener;
    private TimePickerDialog.OnTimeSetListener endTimeListener;

    // Items to give back.
    private Calendar date = Calendar.getInstance();
    private Calendar end = Calendar.getInstance();

    /**
     * Initialize date picker
     */
    public CalenderPicker(Context context){
        calendar = Calendar.getInstance();
        this.context = context;
    }

    /**
     * Function in the activity to give the information.
     * ! So these functions must be present!
     */
    public interface AsyncResponse{
        void newTrainingDateSelected(final Calendar date, final Calendar end);
    }

    /**
     * Change date of the training.
     * @param training: current date
     * @param datePicked: TextView to show datepicked or error.
     */
    public void changeDate(final Training training, final TextView datePicked){
        date.setTimeInMillis(training.getDate());

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                String dateFormatted =
                        new SimpleDateFormat("EEE, MMM d, ''yy", Locale.US).format(date.getTime());

                training.changeDate(date.getTimeInMillis());
                datePicked.setText(dateFormatted);
            }
        },date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setMessage("Starting time");
        datePickerDialog.show();
    }

    /**
     * Change start time of the training.
     *
     * @param training: current date
     * @param startPicked: TextView to show startPicked or error.
     */
    public void changeStart(final Training training, final TextView startPicked){
        date.setTimeInMillis(training.getDate());

        TimePickerDialog startTimePickerDialog =
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        String startFormatted =
                                String.valueOf(hourOfDay) + ":" +
                                        String.format( Locale.US, "%02d", minute);

                        training.changeDate(date.getTimeInMillis());
                        startPicked.setText(startFormatted);
                    }
                },date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), true);
        startTimePickerDialog.setMessage("Start time");
        startTimePickerDialog.show();
    }

    /**
     * Change end time of the training.
     *
     * @param training: current date
     * @param endPicked: TextView to show endPicked or error.
     */
    public void changeEnd(final Training training, final TextView endPicked){
        end.setTimeInMillis(training.getEnd());

        TimePickerDialog startTimePickerDialog =
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        end.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        end.set(Calendar.MINUTE, minute);
                        String startFormatted =
                                String.valueOf(hourOfDay) + ":" +
                                        String.format( Locale.US, "%02d", minute);

                        training.changeEnd(end.getTimeInMillis());
                        endPicked.setText(startFormatted);
                    }
                },end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE), true);
        startTimePickerDialog.setMessage("End time");
        startTimePickerDialog.show();
    }

    /**
     * Create a new training.
     *
     * @param delegate: Give selected date, start and end to this context.
     */
    public void newTraining(final AsyncResponse delegate){
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
        startTimePickerDialog.setMessage("Start time");
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
        // Date.
        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // add values

                date.set(year, monthOfYear, dayOfMonth);

                // now call start time.
                callStartTimePicker();
            }
        };

        startTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // set starting time.
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);

                // call end time
                callEndTimePicker();
            }
        };

        endTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Set ending time.
                end.setTime(date.getTime());
                end.set(Calendar.HOUR_OF_DAY, hourOfDay);
                end.set(Calendar.MINUTE, minute);

                // Done call back to UI.
                delegate.newTrainingDateSelected(date, end);
            }
        };
    }
}
