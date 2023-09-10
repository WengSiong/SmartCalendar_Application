package Smart_Calendar.java;

import androidx.annotation.Keep;

import com.google.firebase.Timestamp;

@Keep
public class firebasemodel {

    private String title;
    private String content;
    private boolean locked; // Changed from isLocked to locked
    private String pin;
    private Timestamp created_date_time, edited_date_time;

    public firebasemodel() {
        // Default constructor required for Firebase Firestore deserialization
    }

    public firebasemodel(String title, String content, boolean locked, String pin, Timestamp created_date_time, Timestamp edited_date_time) {
        this.title = title;
        this.content = content;
        this.locked = locked;
        this.pin = pin;
        this.created_date_time = created_date_time;
        this.edited_date_time = edited_date_time;

    }

    // Getters and setters for the fields
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public boolean isLocked() {
        return locked;
    }
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getPin() {
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }

    public Timestamp getCreated_date_time() {
        return created_date_time;
    }
    public void setCreated_date_time(Timestamp created_date_time) {
        this.created_date_time = created_date_time;
    }

    public Timestamp getEdited_date_time() {
        return edited_date_time;
    }
    public void setEdited_date_time(Timestamp edited_date_time) {
        this.edited_date_time = edited_date_time;
    }


}
