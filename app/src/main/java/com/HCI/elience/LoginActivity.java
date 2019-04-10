package com.HCI.elience;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.FirebaseApp;
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
    //private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //FirebaseApp.initializeApp(this);
        //userAuth = FirebaseAuth.getInstance();
        setTransparency();

        email = findViewById(R.id.fname);
        password = findViewById(R.id.password);
        password_eye = findViewById(R.id.password_eye);
        signup=findViewById(R.id.btn_signup);

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
    /*set the status and navigation bar tranparent*/
    void setTransparency() {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }
}
