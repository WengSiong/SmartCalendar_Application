package Smart_Calendar.java;

import static java.time.LocalDateTime.now;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class notesactivity extends AppCompatActivity {

    FloatingActionButton mcreatenotesfab;
    private FirebaseAuth firebaseAuth;

//    RecyclerView
    RecyclerView mrecyclerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;


//    Firebase
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter<firebasemodel,NoteViewHolder> noteAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notesactivity);

//        Set Title
        getSupportActionBar().setTitle("Memopad");

//        Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_memo:
                    // Handle "Memo" item click
                    Intent memoIntent = new Intent(notesactivity.this, notesactivity.class);
                    startActivity(memoIntent);
                    return true;
                case R.id.action_calendar:
                    // Handle "Calendar" item click
                    Intent calendarIntent = new Intent(notesactivity.this, DailyTask.class);
                    startActivity(calendarIntent);
                    return true;
                case R.id.action_setting:
                    // Handle "Setting" item click
                    Intent settingIntent = new Intent(notesactivity.this, SettingActivity.class);
                    startActivity(settingIntent);
                    return true;

            }
            return false;

        });


//        XML id
        mcreatenotesfab=findViewById(R.id.createnotefab);
        firebaseAuth=FirebaseAuth.getInstance();

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();


//        Go to CreatenoteActivity
        mcreatenotesfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(notesactivity.this,createnote.class));

            }
        });


//        Firebase
        Query query=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<firebasemodel> allusernotes= new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

//        Adapter
        noteAdapter= new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusernotes) {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull firebasemodel firebasemodel) {


//                Menu popup button (3 dots option button)
                ImageView popupbutton=noteViewHolder.itemView.findViewById(R.id.menupopbutton);

//                int colourcode=getRandomColor();
//                noteViewHolder.mnote.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colourcode,null));

                int isEdited = getIntent().getIntExtra("isEdited", 0);


//                Bind the layout on viewholder by adapter
                String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();
                if (firebasemodel.isLocked()) {
                    // Note is locked, hide content and title
                    noteViewHolder.notetitle.setVisibility(View.GONE);
                    noteViewHolder.notecontent.setVisibility(View.GONE);
                } else {
                    // Note is unlocked, show content and title
                    noteViewHolder.notetitle.setVisibility(View.VISIBLE);
                    noteViewHolder.notecontent.setVisibility(View.VISIBLE);

//                    Get data from firebasemodel
                    noteViewHolder.notetitle.setText(firebasemodel.getTitle());
                    noteViewHolder.notecontent.setText(firebasemodel.getContent());

//                    Get Created & Edited DateTime from firebasemodel
                    com.google.firebase.Timestamp timestamp = firebasemodel.getCreated_date_time();
                    com.google.firebase.Timestamp timestamp2 = firebasemodel.getEdited_date_time();

                    if (timestamp != null && timestamp2 == null) {
//                        Only has created datetime
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String formattedDateTime = dateFormat.format(timestamp.toDate());
                        noteViewHolder.created_date_time.setText(formattedDateTime);
                    } else if (timestamp != null && timestamp2 != null) {
//                        Have created & edited datetime
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String formattedDateTime = dateFormat.format(timestamp.toDate());
                        noteViewHolder.created_date_time.setText(formattedDateTime);
                        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String formattedDateTime2 = dateFormat2.format(timestamp2.toDate());
                        noteViewHolder.created_date_time.setText(formattedDateTime2);
                    } else {
//                        if both is null
                        noteViewHolder.created_date_time.setText("No DateTime");
                    }

                }

                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Check if the note is locked
                        if (firebasemodel.isLocked()) {
//                            Get from created & edited datetime firebasemodel
                            com.google.firebase.Timestamp timestamp = firebasemodel.getCreated_date_time();
                            com.google.firebase.Timestamp timestamp2 = firebasemodel.getEdited_date_time();
                            if (timestamp != null && timestamp2 == null) {
//                                only has created datetime
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                String formattedDateTime = dateFormat.format(timestamp.toDate());
                                Toast.makeText(v.getContext(), "Note for " + formattedDateTime + " is locked", Toast.LENGTH_LONG).show();
                            } else if (timestamp != null && timestamp2 != null) {
//                                have created & edited datetime
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                String formattedDateTime2 = dateFormat.format(timestamp2.toDate());
                                Toast.makeText(v.getContext(), "Note for " + formattedDateTime2 + " is locked", Toast.LENGTH_LONG).show();
                            } else {
//                                if both is null
                                Toast.makeText(v.getContext(), "Note is locked", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // The note is not locked, so open the notedetails activity & pass the parameter
                            Intent intent = new Intent(v.getContext(), notedetails.class);
                            intent.putExtra("title", firebasemodel.getTitle());
                            intent.putExtra("content", firebasemodel.getContent());
                            intent.putExtra("noteId", docId);

//                            Get created & edited datetime from firebasemodel
                            com.google.firebase.Timestamp timestamp = firebasemodel.getCreated_date_time();
                            com.google.firebase.Timestamp timestamp2 = firebasemodel.getEdited_date_time();

                            if (timestamp != null && timestamp2 == null) {
//                                only has created datetime
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                String formattedDateTime = dateFormat.format(timestamp.toDate());
                                intent.putExtra("datetime", formattedDateTime);

                            } else if (timestamp != null && timestamp2 != null) {
//                                have created & edited datetime
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                String formattedDateTime = dateFormat.format(timestamp.toDate());
                                intent.putExtra("datetime", formattedDateTime);
                                SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                String formattedDateTime2 = dateFormat2.format(timestamp2.toDate());
                                intent.putExtra("datetime2", formattedDateTime2);
                            } else {
//                                if both is null
                                intent.putExtra("datetime3", "No DateTime");
                            }


                            v.getContext().startActivity(intent);
                        }

                    }
                });

//                menu 3 dots options function
                popupbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PopupMenu popupMenu=new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.END);
//                        Edit
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                Intent intent=new Intent(v.getContext(), Smart_Calendar.java.editnoteactivity.class);
                                intent.putExtra("title",firebasemodel.getTitle());
                                intent.putExtra("content",firebasemodel.getContent());
                                intent.putExtra("noteId",docId);

                                com.google.firebase.Timestamp timestamp = firebasemodel.getCreated_date_time();
                                com.google.firebase.Timestamp timestamp2 = firebasemodel.getEdited_date_time();

                                if (timestamp != null && timestamp2 == null) {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    String formattedDateTime = dateFormat.format(timestamp.toDate());
                                    intent.putExtra("datetime", formattedDateTime);

                                } else if (timestamp != null && timestamp2 != null) {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    String formattedDateTime = dateFormat.format(timestamp.toDate());
                                    intent.putExtra("datetime", formattedDateTime);
                                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    String formattedDateTime2 = dateFormat2.format(timestamp2.toDate());
                                    intent.putExtra("datetime2", formattedDateTime2);
                                }
                                v.getContext().startActivity(intent);
                                return false;
                            }
                        });

//                        Delete
                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(notesactivity.this);
                                builder.setTitle("Delete");
                                builder.setMessage("Are you sure you want to delete?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);
                                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(v.getContext(),"This note is deleted",Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(v.getContext(),"Failed To Delete",Toast.LENGTH_SHORT).show();
                                            }

                                        });
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.create().show();
                                return false;
                            }
                        });
//                        Export PDF
                        popupMenu.getMenu().add("Export PDF").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                LocalDateTime datetime1 = now();
                                DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
                                String formatDateTime = datetime1.format(format);

                                String stringFilePath = Environment.getExternalStorageDirectory().getPath() + "/Download/Note" + formatDateTime +".pdf";
                                File file = new File(stringFilePath);

                                PdfDocument pdfDocument = new PdfDocument();
                                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
                                PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                                Paint paint = new Paint();
                                String stringPDF = firebasemodel.getTitle() + "\n\n" + firebasemodel.getContent();

                                int x = 10, y = 25;

                                for (String line:stringPDF.split("\n")){
                                    page.getCanvas().drawText(line,x,y, paint);

                                    y+=paint.descent()-paint.ascent();
                                }
                                pdfDocument.finishPage(page);
                                try {
                                    pdfDocument.writeTo(new FileOutputStream(file));
                                    Toast.makeText(v.getContext(),"PDF successfully created at " + stringFilePath,Toast.LENGTH_LONG).show();
                                }
                                catch (Exception e){
                                    Toast.makeText(v.getContext(),"PDF didn't create",Toast.LENGTH_SHORT).show();
                                }
                                pdfDocument.close();

                                return false;
                            }
                        });
//                        Export DOCX
                        popupMenu.getMenu().add("Export docx").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                LocalDateTime datetime1 = now();
                                DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
                                String formatDateTime = datetime1.format(format);

                                String stringFilePath = Environment.getExternalStorageDirectory().getPath() + "/Download/Note" + formatDateTime +".docx";
                                File file = new File(stringFilePath);

                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(v.getContext(),"file docx didn't create",Toast.LENGTH_SHORT).show();
                                }

                                try {
                                    XWPFDocument xwpfDocument = new XWPFDocument();
                                    XWPFParagraph xwpfParagraph = xwpfDocument.createParagraph();
                                    XWPFRun xwpfRuntt = xwpfParagraph.createRun();
                                    XWPFRun xwpfRunct = xwpfParagraph.createRun();

                                    xwpfRuntt.setText(firebasemodel.getTitle() + "\n");
                                    xwpfRuntt.addBreak();
                                    xwpfRuntt.setFontSize(24);
                                    xwpfRuntt.setBold(true);

                                    //xwpfRun1.setText(firebasemodel.getContent());
                                    String data = firebasemodel.getContent();
                                    if(data.contains("\n")){
                                        String[] lines = data.split("\n");
                                        xwpfRunct.setText(lines[0], 0);
                                        for(int i=1;i<lines.length;i++){
                                            xwpfRunct.addBreak();
                                            xwpfRunct.setText(lines[i]);
                                        }
                                    } else {
                                        xwpfRunct.setText(data, 0);
                                    }
                                    xwpfRunct.setFontSize(16);


                                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                                    xwpfDocument.write(fileOutputStream);

                                    xwpfDocument.close();
                                    Toast.makeText(v.getContext(),"DOCX successfully created at " + stringFilePath,Toast.LENGTH_LONG).show();
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    Toast.makeText(v.getContext(),"DOCX didn't create",Toast.LENGTH_SHORT).show();
                                }
                                return false;
                            }
                        });
//                        Lock
                        popupMenu.getMenu().add("Lock").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                // Handle Lock logic
                                showLockDialog(docId);
                                return false;
                            }
                        });
//                        Unlock
                        popupMenu.getMenu().add("Unlock").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                // Handle Unlock logic
                                showUnlockDialog(docId);
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
//                    For Lock pin & message
                    private void showLockDialog(String docId) {
                        // Show a dialog to enter and save the PIN
                        AlertDialog.Builder builder = new AlertDialog.Builder(notesactivity.this);
                        builder.setTitle("Lock Note");
                        builder.setMessage("Enter a PIN to lock the note:");

                        final EditText input = new EditText(notesactivity.this);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                        builder.setView(input);
//                        Lock
                        builder.setPositiveButton("Lock", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String pin = input.getText().toString();
                                lockNoteInFirestore(docId, true, pin);
                            }
                        });
//                        Cancel - Lock
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
//                    For unlock pin & message
                    private void showUnlockDialog(String docId) {
                        // Show a dialog to enter the PIN for unlocking
                        AlertDialog.Builder builder = new AlertDialog.Builder(notesactivity.this);
                        builder.setTitle("Unlock Note");
                        builder.setMessage("Enter the PIN to unlock the note:");

                        final EditText input = new EditText(notesactivity.this);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                        builder.setView(input);
//                        Unlock
                        builder.setPositiveButton("Unlock", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String enteredPin = input.getText().toString();
                                verifyAndUnlockNoteInFirestore(docId, enteredPin);
                            }
                        });
//                        Cancel - Unlock
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }

//                    Lock the content in firestore when it's locked
                    private void lockNoteInFirestore(String docId, boolean isLocked, String pin) {
                        // Update the lock status and PIN in Firestore
                        DocumentReference noteRef = firebaseFirestore.collection("notes")
                                .document(firebaseUser.getUid())
                                .collection("myNotes")
                                .document(docId);

                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("locked", isLocked);
                        updateData.put("pin", pin);

//                        Update message
                        noteRef.update(updateData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Note locked", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed to lock note", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

//                    Verify pin
                    private void verifyAndUnlockNoteInFirestore(String docId, String enteredPin) {
                        // Retrieve the stored PIN from Firestore and verify it
                        DocumentReference noteRef = firebaseFirestore.collection("notes")
                                .document(firebaseUser.getUid())
                                .collection("myNotes")
                                .document(docId);

//                        if success get content
                        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String storedPin = documentSnapshot.getString("pin");

                                    if (storedPin != null && storedPin.equals(enteredPin)) {
                                        // PIN matches, unlock the note
                                        lockNoteInFirestore(docId, false, "");
                                        Toast.makeText(getApplicationContext(), "Note unlocked", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Invalid PIN
                                        Toast.makeText(getApplicationContext(), "Invalid PIN", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to unlock note", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


            }
            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };

//        Put onto the recyclerview
        mrecyclerview=findViewById(R.id.recyclerview);
        mrecyclerview.setHasFixedSize(true);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerview.setAdapter(noteAdapter);

    }

//    viewholder
    public class NoteViewHolder extends RecyclerView.ViewHolder
    {
        private TextView notetitle;
        private TextView notecontent;
        private TextView created_date_time;
        LinearLayout mnote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            notetitle=itemView.findViewById(R.id.notetitle);
            notecontent=itemView.findViewById(R.id.notecontent);
            created_date_time = itemView.findViewById(R.id.datetime);
//            editedLabel = itemView.findViewById(R.id.editedLabel);

            mnote=itemView.findViewById(R.id.note);

        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter!=null)
        {
            noteAdapter.stopListening();
        }
    }

}