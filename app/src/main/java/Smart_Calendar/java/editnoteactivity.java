package Smart_Calendar.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editnoteactivity extends AppCompatActivity {


    Intent data;
    EditText medittitleofnote,meditcontentofnote;
    FloatingActionButton msaveeditnote;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    // Declare noteAdapter as a field
    private FirestoreRecyclerAdapter<firebasemodel, notesactivity.NoteViewHolder> noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnoteactivity);

        // Add the "Back" button to the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        Set Title
        getSupportActionBar().setTitle("");


//        XML id
        medittitleofnote=findViewById(R.id.edittitleofnote);
        meditcontentofnote=findViewById(R.id.editcontentofnote);
        msaveeditnote=findViewById(R.id.saveeditnote);

        data=getIntent();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();


        Toolbar toolbar=findViewById(R.id.toolbarofeditnote);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        msaveeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Convert to String
                String newtitle=medittitleofnote.getText().toString();
                String newcontent=meditcontentofnote.getText().toString();

//                Check Empty Input
                if(newtitle.isEmpty()||newcontent.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Something is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
//                    Store into the Firebase
                    DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                    Map<String,Object> note=new HashMap<>();
                    note.put("title",newtitle);
                    note.put("content",newcontent);
                    note.put("edited_date_time",com.google.firebase.Timestamp.now());

                    documentReference.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Note is updated",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(editnoteactivity.this,notesactivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed To update",Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });


//        Set Text
        String notetitle=data.getStringExtra("title");
        String notecontent=data.getStringExtra("content");
        meditcontentofnote.setText(notecontent);
        medittitleofnote.setText(notetitle);

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