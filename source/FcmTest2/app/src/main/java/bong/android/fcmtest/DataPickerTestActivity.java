package bong.android.fcmtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;

/**
 * Created by Polarium on 2017-10-13.
 */

public class DataPickerTestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.date_picker_activity);

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);

        Button getDateButton = (Button) findViewById(R.id.getDateButton);
        getDateButton.setOnClickListener(v ->
                Toast.makeText(getApplicationContext(),
                        calendarView.getSelectedDate().getTime().toString(),
                        Toast.LENGTH_LONG).show());
    }
}