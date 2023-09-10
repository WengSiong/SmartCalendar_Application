package Smart_Calendar.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class notedetails extends AppCompatActivity {


    private TextView mtitleofnotedetail,mcontentofnotedetail;
    FloatingActionButton mgotoeditnote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetails);

        // Add the "Back" button to the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        Set Title
        getSupportActionBar().setTitle("");

//        XML id
        mtitleofnotedetail=findViewById(R.id.titleofnotedetail);
        mcontentofnotedetail=findViewById(R.id.contentofnotedetail);
        mgotoeditnote=findViewById(R.id.gotoeditnote);
        Toolbar toolbar=findViewById(R.id.toolbarofnotedetail);

        Intent data=getIntent();


        mgotoeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Passing the parameter to editnoteactivity
                Intent intent=new Intent(v.getContext(), Smart_Calendar.java.editnoteactivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteId",data.getStringExtra("noteId"));
                v.getContext().startActivity(intent);
            }
        });

//        Set Text
        mcontentofnotedetail.setText(data.getStringExtra("content"));
        mtitleofnotedetail.setText(data.getStringExtra("title"));


        // Retrieve and display the created datetime
        String datetime = getIntent().getStringExtra("datetime");
        // Retrieve and display the edited datetime
        String datetime2 = getIntent().getStringExtra("datetime2");

        if (datetime != null && datetime2 == null) {
            // Retrieve and display the created datetime
            TextView datetimeTextView = findViewById(R.id.datetimeview);
            datetimeTextView.setText("Created By : " + datetime);
            // Retrieve and display the edited datetime
            TextView datetimeTextView2 = findViewById(R.id.datetimeview2);
            datetimeTextView2.setText("");

        } else if (datetime != null && datetime2 != null) {
            // Retrieve and display the created datetime
            TextView datetimeTextView = findViewById(R.id.datetimeview);
            datetimeTextView.setText("Created By : " + datetime);
            // Retrieve and display the edited datetime
            TextView datetimeTextView2 = findViewById(R.id.datetimeview2);
            datetimeTextView2.setText("Edited By : " + datetime2);
        } else {
            // Retrieve and display the created datetime
            TextView datetimeTextView = findViewById(R.id.datetimeview);
            datetimeTextView.setText("Created By : No DateTime");
            // Retrieve and display the edited datetime
            TextView datetimeTextView3 = findViewById(R.id.datetimeview2);
            datetimeTextView3.setText("Edited By : No DateTime");
        }
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