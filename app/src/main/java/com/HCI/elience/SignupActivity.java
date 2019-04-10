package com.HCI.elience;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import customfonts.EditText__SF_Pro_Display_Medium;

/**
 * Todo // Validate all fields
 * Todo // Generate error message
 * Todo // Add user record via firebase
 *
 *
 * A signup screen
 */
public class SignupActivity extends AppCompatActivity {

    private EditText__SF_Pro_Display_Medium email;
    private EditText__SF_Pro_Display_Medium password;
    private ImageButton password_eye;
    private DatePickerDialog.OnDateSetListener date;
    private EditText__SF_Pro_Display_Medium dob;
    private Calendar myCalendar;
    private Spinner gender;

    private List<String> genderList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparency();
        setContentView(R.layout.activity_signup);
        myCalendar = Calendar.getInstance();
        email = findViewById(R.id.fname);
        password = findViewById(R.id.password);
        password_eye = findViewById(R.id.password_eye);
        gender=findViewById(R.id.gender);
        genderList=new ArrayList<String>();
        genderList.add("Gender");
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Other");
        ArrayAdapter adapter=new ArrayAdapter<String>(SignupActivity.this,R.layout.spinner_item,genderList);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        gender.setAdapter(adapter);

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
        dob= findViewById(R.id.dob);
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
    }
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dob.setText(sdf.format(myCalendar.getTime()));
    }
    void setTransparency() {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }
}
