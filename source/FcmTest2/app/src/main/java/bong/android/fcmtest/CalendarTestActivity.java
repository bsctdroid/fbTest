package bong.android.fcmtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Polarium on 2017-10-12.
 */
public class CalendarTestActivity extends Activity {
    CalendarView calendarView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        calendarView = (CalendarView)findViewById(R.id.calendar1);

        //리스너 등록
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                Toast.makeText(CalendarTestActivity.this, ""+year+"/"+(month+1)+"/" +dayOfMonth, 0).show();
            }

        });

        testCalendar();
    }

    public void testCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        calendar.set(Calendar.HOUR_OF_DAY, 23);//not sure this is needed
        long endOfMonth = calendar.getTimeInMillis();

        //may need to reinitialize calendar, not sure
        Calendar calendar2 = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.YEAR, 2017);
        calendar2.set(Calendar.MONTH, 3);
        calendar2.set(Calendar.DATE, 1);
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        long startOfMonth = calendar2.getTimeInMillis();

        //달력을 시작을 무슨 요일로 할지 결정
        calendarView.setFirstDayOfWeek(1);
        //deprecated - 선택한 달력 색 변경
        //calendarView.setFocusedMonthDateColor(0xff0000);
        //마지막 날짜 설정
        calendarView.setMaxDate(endOfMonth);
        calendarView.setMinDate(startOfMonth);
        //

    }
}
