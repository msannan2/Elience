package com.HCI.elience;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.Date;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import customfonts.EditText__SF_Pro_Display_Medium;
import io.socket.emitter.Emitter;

/**
 * Todo // Validate all fields
 * Todo // Generate error message
 * Todo // Add user record via firebase
 *
 *
 * A signup screen
 */
public class SignupActivity extends AppCompatActivity {

    private EditText__SF_Pro_Display_Medium email,name,username;
    private EditText__SF_Pro_Display_Medium password;
    private ImageButton password_eye;
    private DatePickerDialog.OnDateSetListener date;
    private EditText__SF_Pro_Display_Medium dob;
    private TextView error;
    private Calendar myCalendar;
    private Spinner gender;
    private ProgressBar progressBar;
    private Button login,next;
    private FirebaseAuth userAuth;
    private String EmailKey;
    private String UserHash;
    private DatabaseReference dbRef;

    private List<String> genderList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparency();

        setContentView(R.layout.activity_signup);
        login=findViewById(R.id.btn_login);
        next=findViewById(R.id.btn_next);

        progressBar=findViewById(R.id.progressBar2);
        myCalendar = Calendar.getInstance();
        name = findViewById(R.id.fname);
        username = findViewById(R.id.nickname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password_eye = findViewById(R.id.password_eye);
        gender=findViewById(R.id.gender);
        error=findViewById(R.id.register_error);
        dob= findViewById(R.id.dob);


        userAuth=FirebaseAuth.getInstance();
        dbRef= FirebaseDatabase.getInstance().getReference();

        genderList=new ArrayList<String>();
        genderList.add("Gender");
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Other");

        ArrayAdapter adapter=new ArrayAdapter<String>(SignupActivity.this,R.layout.spinner_item,genderList);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        gender.setAdapter(adapter);

        error.setVisibility(View.GONE);

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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                EmailKey=email.getText().toString();
                final HashCode hashCode = Hashing.sha1().hashString(EmailKey, Charset.defaultCharset());
                username.setText("user@"+hashCode);

            }
        });
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SignupActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Name = name.getText().toString().trim();
                final String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();
                final String Username=username.getText().toString().trim();
                final String Gender=gender.getSelectedItem().toString().trim();
                final String DOB=dob.getText().toString().trim();
              /*  if (TextUtils.isEmpty(Email)) {
                    error.setText("Enter email address");
                    return;
                }

                if (TextUtils.isEmpty(Name)) {
                    error.setText("Enter your name");
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    error.setText("Enter a password");
                    return;
                }*/


                progressBar.setVisibility(View.VISIBLE);

                //create user
                userAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(SignupActivity.this, "User with this email already exist.", Toast.LENGTH_SHORT).show();
                                    }else
                                        Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                } else {
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("name", Name);
                                    user.put("email", Email);
                                    user.put("username", Username);
                                    user.put("gender", Gender);
                                    user.put("dob",DOB);

                                    dbRef.child("users").child(userAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(SignupActivity.this,"Account created successfully",Toast.LENGTH_SHORT);

                                            }
                                            else{
                                                Toast.makeText(SignupActivity.this,task.getException().toString(),Toast.LENGTH_SHORT);

                                            }
                                        }
                                    });
                                    //AppSocketListener.getInstance().addOnHandler(SocketEventConstants.registerResponse, AppContext.getEmitterListeners().onRegister);
                                    //AppSocketListener.getInstance().addOnHandler(SocketEventConstants.registerResponse, startActivity);
                                    //AppSocketListener.getInstance().emit(SocketEventConstants.register, userAuth.getCurrentUser().getUid(), email, name);
                                }
                            }
                        });

            }
        });
    }
    public Emitter.Listener startActivity = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            AppSocketListener.getInstance().off(SocketEventConstants.registerResponse);
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dob.setText(sdf.format(myCalendar.getTime()));
    }

    void gotoMain()
    {
        Intent i=new Intent(SignupActivity.this,MenuActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
    void setTransparency() {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }
}
