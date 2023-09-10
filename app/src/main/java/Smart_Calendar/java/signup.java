package Smart_Calendar.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity {


    private EditText msignupemail,msignuppassword, mconsignuppassword;
    private TextView mgotologin;


    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//        Hide the top navigation bar
        getSupportActionBar().hide();

//        XML id
        msignupemail=findViewById(R.id.signupemail);
        msignuppassword=findViewById(R.id.signuppassword);
        mconsignuppassword=findViewById(R.id.consignuppassword);
        mgotologin=findViewById(R.id.gotologin);
        ImageButton msignup = (ImageButton) findViewById(R.id.signup);

//        Firebase
        firebaseAuth= FirebaseAuth.getInstance();

//        Login
        mgotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signup.this,MainActivity.class);
                startActivity(intent);
            }
        });

//        Register
        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Get text input
                String mail=msignupemail.getText().toString().trim();
                String password=msignuppassword.getText().toString().trim();
                String conpassword=mconsignuppassword.getText().toString().trim();

                // Define a regex pattern for password validation
                String passwordPattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[(*)_@#$%^&+=!]).*$";

//                Check Empty Input
                if(mail.isEmpty() || password.isEmpty() || conpassword.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"All Fields are Required",Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<7)
                { // check length of password
                    Toast.makeText(getApplicationContext(),"Password Should Greater than 7 Digits",Toast.LENGTH_SHORT).show();
                }
                else if (!password.matches(passwordPattern)) {
//                    Check Password Pattern for strong security
                    Toast.makeText(getApplicationContext(), "Password must contain at least one uppercase letter, one number, and one symbol", Toast.LENGTH_SHORT).show();
                }
                else if(!conpassword.equals(password))
                { // Double Check with the password to confirm the password
                    Toast.makeText(getApplicationContext(),"Please enter the same password.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    /// registered the user to firebase
                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            { //Send verification email to their email registered
                                sendEmailVerification();
                            }

                        }
                    });

                }

            }
        });


    }

    //send email verification
    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        { //if it is an existed email, proceed
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // notify user to verify their email
                    Toast.makeText(getApplicationContext(),"Verification Email is Sent. Please verify and log in again",Toast.LENGTH_LONG).show();
                    firebaseAuth.signOut();
                }
            });
        }

        else
        {
            Toast.makeText(getApplicationContext(),"Failed To Send Verification Email",Toast.LENGTH_SHORT).show();
        }
    }


}