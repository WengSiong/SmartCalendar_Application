package Smart_Calendar.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class fogotpassword extends AppCompatActivity {

    private EditText mforgotpassword;
    private Button mpasswordrecoverbutton;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fogotpassword);

//        Set Title
        setTitle(" Forgot Password");

        // Add the "Back" button to the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mforgotpassword=findViewById(R.id.forgotpassword);
        mpasswordrecoverbutton=findViewById(R.id.passwordrecoverbutton);

        firebaseAuth= FirebaseAuth.getInstance();


        mpasswordrecoverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=mforgotpassword.getText().toString().trim();
                if(mail.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter your mail first",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //send password recover email
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            { //if send sucess
                                Toast.makeText(getApplicationContext(),"Mail Sent , You can recover your password using mail",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(fogotpassword.this, Smart_Calendar.java.MainActivity.class));
                            }
                            else
                            { //if failed to send
                                Toast.makeText(getApplicationContext(),"Email is Wrong or Account Not Exist",Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

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