package Smart_Calendar.java;

import static Smart_Calendar.java.CalendarUtils.daysInMonthArray;
import static Smart_Calendar.java.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.ArrayList;

public class DailyTask extends AppCompatActivity implements Smart_Calendar.java.CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_task);
        eventListView = findViewById(R.id.eventListView);
        getSupportActionBar().setTitle("Calendar");

//      Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_memo:
                    // Handle "Memo" item click
                    Intent memoIntent = new Intent(DailyTask.this, Smart_Calendar.java.notesactivity.class);
                    startActivity(memoIntent);
                    return true;
                case R.id.action_calendar:
//                    // Handle "Calendar" item click
                    Intent calendarIntent = new Intent(DailyTask.this, DailyTask.class);
                    startActivity(calendarIntent);
                    return true;
                case R.id.action_setting:
                    // Handle "Setting" item click
                    Intent settingIntent = new Intent(DailyTask.this, Smart_Calendar.java.SettingActivity.class);
                    startActivity(settingIntent);
                    return true;

            }
            return false;
        });

        initWidgets();
        Log.i("##### Tag ######", "after initWidgets");

        Smart_Calendar.java.CalendarUtils.selectedDate = LocalDate.now();
        Log.i("##### Tag ######", "local date: " + Smart_Calendar.java.CalendarUtils.selectedDate.toString());

        setMonthView();
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView()
    {

        monthYearText.setText(monthYearFromDate(Smart_Calendar.java.CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInMonthArray(Smart_Calendar.java.CalendarUtils.selectedDate);

        Smart_Calendar.java.CalendarAdapter calendarAdapter = new Smart_Calendar.java.CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view)
    {
        Smart_Calendar.java.CalendarUtils.selectedDate = Smart_Calendar.java.CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view)
    {
        Smart_Calendar.java.CalendarUtils.selectedDate = Smart_Calendar.java.CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    private void setEventAdapter()
    {
        ArrayList<Smart_Calendar.java.Event> dailyEvents = Smart_Calendar.java.Event.eventsForDate(Smart_Calendar.java.CalendarUtils.selectedDate);
        Smart_Calendar.java.EventAdapter eventAdapter = new Smart_Calendar.java.EventAdapter(getApplicationContext(), dailyEvents){

        };
        eventListView.setAdapter(eventAdapter);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date)
    {
        if(date !=null)
        {
            Smart_Calendar.java.CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }
    public void newEventAction(View view)
    {
        startActivity(new Intent(this, Smart_Calendar.java.EventEditActivity.class));
    }


}

