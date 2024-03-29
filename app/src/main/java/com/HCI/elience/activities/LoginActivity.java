package com.HCI.elience.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.HCI.elience.R;
import com.HCI.elience.utils.validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import customfonts.EditText__SF_Pro_Display_Medium;


/**
 * Todo // Validate email and password
 * Todo // Generate error message
 * Todo // Authenticate via firebase
 *

 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText__SF_Pro_Display_Medium email;
    private EditText__SF_Pro_Display_Medium password;
    private ImageButton password_eye;
    private Button login,signup;
    private FirebaseAuth userAuth;
    private ProgressBar progressBar;
    private TextView authError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //FirebaseApp.initializeApp(this);
        InitializeView();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email=email.getText().toString().trim();
                String Password=password.getText().toString().trim();
                if (email.getText().toString().trim().equalsIgnoreCase("") || !validator.isValidEmail(Email)) {
                    email.setError("Enter an email");
                    return;
                }
                if (password.getText().toString().trim().equalsIgnoreCase("")) {
                    password.setError("Enter a password");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                userAuth.signInWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    authError.setVisibility(View.VISIBLE);
                                } else {
                                        gotoMain();
                                }
                            }
                        });

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(i);
            }
        });

        password_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getInputType() == 129)
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                else
                    password.setInputType(129);/*code for password input type short for InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD*/

            }
        });
    }


    void InitializeView()
    {
        setTransparency();
        userAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.fname);
        password = findViewById(R.id.password);
        password_eye = findViewById(R.id.password_eye);
        signup=findViewById(R.id.btn_signup);
        login=findViewById(R.id.btn_login);
        progressBar=findViewById(R.id.progressBar2);
        authError=findViewById(R.id.auth_error);
        authError.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    /*set the status and navigation bar tranparent*/
    void setTransparency() {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }

    void gotoMain()
    {
        Intent i=new Intent(LoginActivity.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

}
