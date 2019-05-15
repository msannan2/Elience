package com.HCI.elience.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import android.view.WindowManager;
import android.widget.Toast;

import com.HCI.elience.ProfanityChecker;
import com.HCI.elience.R;
import com.HCI.elience.adapters.THTabsAccessorAdapter;
import com.HCI.elience.adapters.TabsAccessorAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import customfonts.EditText__SF_Pro_Display_Medium;

public class MainActivity extends AppCompatActivity {

    private TabsAccessorAdapter mPagerAdapter;
    private THTabsAccessorAdapter mPagerAdapter2;
    private ViewPager mViewPager;
    private FirebaseUser currentUser;
    private FirebaseAuth userAuth;
    private DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTransparency();
        userAuth=FirebaseAuth.getInstance();
        currentUser=userAuth.getCurrentUser();
        dbRef= FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(currentUser==null)
        {
            gotoLogin();
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        FirebaseDatabase.getInstance().getReference().child("Therapists").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(currentUser!=null) {
                    String user = currentUser.getUid();
                    if (dataSnapshot.hasChild(user)) {
                        mPagerAdapter2 = new THTabsAccessorAdapter(getSupportFragmentManager());

                        // Set up the ViewPager with the sections adapter.
                        mViewPager = (ViewPager) findViewById(R.id.container);
                        mViewPager.setAdapter(mPagerAdapter2);

                        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                        tabLayout.getTabAt(1).setText("My Questions");

                        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
                    } else {
                        mPagerAdapter = new TabsAccessorAdapter(getSupportFragmentManager());

                        // Set up the ViewPager with the sections adapter.
                        mViewPager = (ViewPager) findViewById(R.id.container);
                        mViewPager.setAdapter(mPagerAdapter);

                        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

                        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            userAuth.signOut();
            finish();
            gotoLogin();
            return true;
        }
        if (id == R.id.action_create_group) {
            requestNewGroup();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestNewGroup() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Enter Group Name");
        final EditText__SF_Pro_Display_Medium groupName=new EditText__SF_Pro_Display_Medium(MainActivity.this);
        groupName.setHint("Support group name");
        groupName.setBackgroundResource(R.drawable.rect_bg);
        groupName.setGravity(Gravity.CENTER);
        groupName.setPadding(20,10,20,10);
        builder.setView(groupName,50,50,50,0);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String gName= groupName.getText().toString().trim();
                if(TextUtils.isEmpty(gName) || !ProfanityChecker.CheckforProfanity(gName))
                {
                    Toast.makeText(MainActivity.this,"Please enter a valid group name", Toast.LENGTH_SHORT);
                }
                else
                {
                    createNewGroup(gName);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void createNewGroup(final String gName) {
        dbRef.child("Groups").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(gName))
                {
                    Toast.makeText(MainActivity.this,gName+" already exists",Toast.LENGTH_SHORT);

                }
                else
                {
                    dbRef.child("Groups").child(gName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                HashMap<String,Object> data=new HashMap<>();
                                Calendar calForDate = Calendar.getInstance();
                                SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
                                String currentDate=currentDateFormat.format(calForDate.getTime());

                                Calendar calForTime = Calendar.getInstance();
                                SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
                                String currentTime=currentTimeFormat.format(calForTime.getTime());

                                data.put("timestamp",System.currentTimeMillis());
                                data.put("date",currentDate+" "+currentTime);

                                dbRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Usergroups").child(gName).setValue(data);
                                Toast.makeText(MainActivity.this,gName+" group created successfully",Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void setTransparency() {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            gotoLogin();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            gotoLogin();
        }
    }
    void gotoLogin()
    {
        Intent i=new Intent(MainActivity.this,LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
    }
}
