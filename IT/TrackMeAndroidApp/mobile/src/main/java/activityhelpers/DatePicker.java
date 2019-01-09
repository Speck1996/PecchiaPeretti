package activityhelpers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

public class DatePicker  implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    EditText dateView;
    private int day;
    private int month;
    private int birthYear;
    private Context context;

    public DatePicker(Context context, int textViewId) {
        Activity act = (Activity)context;
        this.dateView = act.findViewById(textViewId);
        this.dateView.setOnClickListener(this);
        this.context = context;
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        birthYear = year;
        this.month = month;
        day = dayOfMonth;
        updateDisplay();
    }



    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    // updates the date in the birth date EditText
    private void updateDisplay() {

        dateView.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(day).append("/").append(month + 1).append("/").append(birthYear).append(" "));
    }
}