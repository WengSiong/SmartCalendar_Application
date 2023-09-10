package Smart_Calendar.java;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class EventEditActivity extends AppCompatActivity
{
    private EditText eventNameET,eventTimeET;
    private TextView eventDateTV;
    private FirebaseFirestore db;
    private FloatingActionButton save;

    private LocalTime time;
    private LocalDate date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        getSupportActionBar().setTitle("");

        // Add the "Back" button to the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initWidgets();
        time = LocalTime.now();
        date = LocalDate.now();
        eventDateTV.setText("Date : " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        save = findViewById(R.id.buttonSave);
        db = FirebaseFirestore.getInstance();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savEventAction(view);
            }
        });
    }


//    Let user choose the time
    public void showTimePickerDialog(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Update the EditText with the selected time
                LocalTime selectedTime = LocalTime.of(hourOfDay, minute);
                eventTimeET.setText(CalendarUtils.formattedTime(selectedTime));
                time = selectedTime;
            }
        }, time.getHour(), time.getMinute(), true);

        timePickerDialog.show();
    }

    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        eventTimeET = findViewById(R.id.eventTimeET);
//        eventDateET = findViewById(R.id.eventDateET);
        eventDateTV = findViewById(R.id.eventDateTV);
//        eventTimeTV = findViewById(R.id.eventTimeTV);
    }

    public void savEventAction(View view)
    {
        String eventName = eventNameET.getText().toString();
        // Get the time from the EditText
        String timeString = eventTimeET.getText().toString();
        //LocalTime selectedTime = CalendarUtils.parseTime(timeString);
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate,time);
        Event.eventsList.add(newEvent);
        saveEventToFirestore(newEvent);
        finish();
    }

//    Store into the Firebase
    private void saveEventToFirestore(Event newEvent) {
        // Initialize Firebase Firestore (you should have this initialized somewhere)

        // Create a Map from the EventRecord object
        Map<String, Object> event = new HashMap<>();
        event.put("EventName", eventNameET.getText().toString());
        event.put("EventDate", eventDateTV.getText().toString());
        event.put("EventTime", eventTimeET.getText().toString());

        // Add the event to the Firestore collection
        db.collection("Event")
                .add(event)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EventEditActivity.this,"Save Success!",Toast.LENGTH_SHORT).show();
                        } else {
                            // Error saving feedback
                            Toast.makeText(EventEditActivity.this,"Save Failure!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

//    "Back" button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
