package activityhelpers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;


/**
 * Class that manages the calendar view used during the signup activity
 */
public class DatePicker  implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    /**
     * Edit text used to access the calendar
     */
    EditText dateView;

    /**
     * Day selected
     */
    private int day;

    /**
     * Month selected
     */
    private int month;
    /**
     * Year selected
     */
    private int birthYear;

    /**
     * Context in which the calendar will pop up
     */
    private Context context;


    /**
     * Constructor for the date picker
     * @param context in which the calendar appears
     * @param textViewId id of the edit text bound to the calendar
     */
    public DatePicker(Context context, int textViewId) {
        Activity act = (Activity)context;
        this.dateView = act.findViewById(textViewId);
        this.dateView.setOnClickListener(this);
        this.context = context;
    }


    /**
     * {@inheritdoc}
     */
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        birthYear = year;
        this.month = month;
        day = dayOfMonth;
        updateDisplay();
    }


    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    /**
     * Updates the edit text with the new data
     */
    // updates the date in the birth date EditText
    private void updateDisplay() {

        dateView.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(day).append("/").append(month + 1).append("/").append(birthYear).append(" "));
    }
}