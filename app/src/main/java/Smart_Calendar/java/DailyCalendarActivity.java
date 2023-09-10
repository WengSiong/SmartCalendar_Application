package Smart_Calendar.java;

import static Smart_Calendar.java.CalendarUtils.selectedDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class DailyCalendarActivity extends AppCompatActivity
{
    private TextView monthDayText;
    private TextView dayOfWeekTV;
    private ListView hourListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_calendar);
        initWidgets();
    }

    private void initWidgets()
    {
        monthDayText = findViewById(R.id.monthDayText);
        dayOfWeekTV = findViewById(R.id.dayOfWeekTV);
        hourListView = findViewById(R.id.hourListView);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setDayView();
    }

    private void setDayView()
    {
        monthDayText.setText(Smart_Calendar.java.CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
        setHourAdapter();
    }

    private void setHourAdapter()
    {
        Smart_Calendar.java.HourAdapter hourAdapter = new Smart_Calendar.java.HourAdapter(getApplicationContext(), hourEventList());
        hourListView.setAdapter(hourAdapter);
    }

    private ArrayList<HourEvent> hourEventList()
    {
        ArrayList<HourEvent> list = new ArrayList<>();
        for(int hour = 0; hour < 24; hour++)
        {
            LocalTime time = LocalTime.of(hour, 0);
            ArrayList<Smart_Calendar.java.Event> events = Smart_Calendar.java.Event.eventsForDateAndTime(selectedDate, time);
            HourEvent hourEvent = new HourEvent(time, events);
            list.add(hourEvent);
        }

        return list;
    }

    public void previousDayAction(View view)
    {
        Smart_Calendar.java.CalendarUtils.selectedDate = Smart_Calendar.java.CalendarUtils.selectedDate.minusDays(1);
        setDayView();
    }

    public void nextDayAction(View view)
    {
        Smart_Calendar.java.CalendarUtils.selectedDate = Smart_Calendar.java.CalendarUtils.selectedDate.plusDays(1);
        setDayView();
    }

    public void newEventAction(View view)
    {
        startActivity(new Intent(this, Smart_Calendar.java.EventEditActivity.class));
    }
}