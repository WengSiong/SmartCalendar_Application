package Smart_Calendar.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {

    Button feedback;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

//        Set Title
        getSupportActionBar().setTitle("Setting");

//        Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_memo:
                    // Handle "Memo" item click
                    Intent memoIntent = new Intent(SettingActivity.this, Smart_Calendar.java.notesactivity.class);
                    startActivity(memoIntent);
                    return true;
                case R.id.action_calendar:
                    // Handle "Calendar" item click
                    Intent calendarIntent = new Intent(SettingActivity.this, DailyTask.class);
                    startActivity(calendarIntent);
                    return true;
                case R.id.action_setting:
                    // Handle "Setting" item click
                    Intent settingIntent = new Intent(SettingActivity.this, SettingActivity.class);
                    startActivity(settingIntent);
                    return true;

            }
            return false;
        });


//        Feedback Page
        feedback = findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent feedbackIntent = new Intent(SettingActivity.this, Smart_Calendar.java.feedback.class);
                startActivity(feedbackIntent);
            }
        });



        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Find the "Logout" button and set a click listener
        Button logoutButton = findViewById(R.id.logoutbtn);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the user
                firebaseAuth.signOut();

                // Navigate to the MainActivity
                Intent mainIntent = new Intent(SettingActivity.this, Smart_Calendar.java.MainActivity.class);
                startActivity(mainIntent);

                // Finish the current activity to prevent going back
                finish();
            }
        });
    }



}