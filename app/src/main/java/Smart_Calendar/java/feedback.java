package Smart_Calendar.java;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class feedback extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription;
    private RatingBar ratingBar;
    private ImageButton buttonSubmit;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Add the "Back" button to the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        Set Title
        getSupportActionBar().setTitle("Feedback");

//        XML id
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        ratingBar = findViewById(R.id.ratingBar);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        db = FirebaseFirestore.getInstance();


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFeedbackToFirestore();
            }
        });
    }

    private void saveFeedbackToFirestore() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        float rating = ratingBar.getRating();

        // Create a new feedback document
        Map<String, Object> feedback = new HashMap<>();
        feedback.put("title", title);
        feedback.put("description", description);
        feedback.put("rating", rating); // Include the rating value

        // Add the feedback to Firestore
        db.collection("feedback")
                .add(feedback)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Task task) {
                        if (task.isSuccessful()) {
                            // Feedback saved successfully
                            editTextTitle.setText("");
                            editTextDescription.setText("");
                            ratingBar.setRating(0); // Reset the rating
                            showToast("Feedback submitted successfully!");
                        } else {
                            // Error saving feedback
                            showToast("Error submitting feedback. Please try again later.");
                        }
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
