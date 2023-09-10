package Smart_Calendar.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mloginemail,mloginpassword;
    private  TextView mgotoforgotpassword, mgotosignup;
    private ImageButton mlogin;

    private FirebaseAuth firebaseAuth;

    ProgressBar mprogressbarofmainactivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Hide Top Title Bar
        getSupportActionBar().hide();

//        XML id
        mloginemail=findViewById(R.id.loginemail);
        mloginpassword=findViewById(R.id.loginpassword);
        mlogin=findViewById(R.id.login);
        mgotoforgotpassword=findViewById(R.id.gotoforgotpassword);
        mgotosignup=findViewById(R.id.gotosignup);
        mprogressbarofmainactivity=findViewById(R.id.progressbarofmainactivity);


//        Firebase
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();


//        if the user is not exist
        if(firebaseUser!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this, Smart_Calendar.java.notesactivity.class));
        }

//        Go to Register instead of MainAcitivty
        mgotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Smart_Calendar.java.signup.class));
            }
        });

//        Go to ForgotPassword instead of MainAcitivty
        mgotoforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,fogotpassword.class));
            }
        });

//        Go to Login instead of MainAcitivty
        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Get Input data
                String mail=mloginemail.getText().toString().trim();
                String password=mloginpassword.getText().toString().trim();

//                Check Empty Input
                if(mail.isEmpty()|| password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"All Field Are Required",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    // login the user and go to MainActivity
                    mprogressbarofmainactivity.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if(task.isSuccessful())
                            { // if success, verify email for register for security & privacy
                                checkmailverfication();
                            }
                            else
                            { // if failed, it is not the existed account
                                Toast.makeText(getApplicationContext(),"Account Doesn't Exist",Toast.LENGTH_SHORT).show();
                                mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
                            }


                        }
                    });
                }
            }
        });

    }


//    Send Email Verification
    private void checkmailverfication()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser.isEmailVerified()==true)
        { // if user has verified
            Toast.makeText(getApplicationContext(),"Logged In",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, Smart_Calendar.java.notesactivity.class));
        }
        else
        { // if user has not verified
            mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"Please verify your email first.",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }




}