package com.HCI.elience.activities;

import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.HCI.elience.adapters.MessageListAdapter;
import com.HCI.elience.models.MessageModel;
import com.HCI.elience.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageButton sendMessageBtn;
    private EditText userInput;
    private RecyclerView mMessageRecycler;
    private TextView headerText;
    private ImageButton backButton;
    private MessageListAdapter mMessageAdapter;
    private DatabaseReference userRef, groupsRef, groupMessageKeyRef;
    private String currentGroupName, currentUserID, currentUserName, currentDate, currentTime,currentGroupID;
    private List<MessageModel> messageList = new ArrayList<MessageModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        getWindow().setStatusBarColor(Color.BLACK);
        currentGroupName = getIntent().getExtras().get("groupName").toString();
        currentGroupID = getIntent().getExtras().get("groupID").toString();
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        groupsRef = FirebaseDatabase.getInstance().getReference().child("Therapists").child(currentGroupName).child(currentUserID).child("chats");
        //setTransparency();
        Initialize();
        GetUserInfo();
        mMessageRecycler.setAdapter(mMessageAdapter);
        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveMessagetoDB();
                userInput.setText("");
            }

        });
        groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DisplayAllMessages(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });
    }

    private void SaveMessagetoDB() {
        String message = userInput.getText().toString();

        if (!TextUtils.isEmpty(message)) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Therapists").child(currentGroupName).child("chats");
            String messageKey = ref.push().getKey();
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            String currentDate = currentDateFormat.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            String currentTime = currentTimeFormat.format(calForTime.getTime());

            HashMap<String, Object> groupMessageKey = new HashMap<>();
            ref.updateChildren(groupMessageKey);

            HashMap<String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
            messageInfoMap.put("name", currentUserName);
            messageInfoMap.put("message", message);
            messageInfoMap.put("date", currentDate);
            messageInfoMap.put("time", currentTime);
            messageInfoMap.put("timestamp", System.currentTimeMillis());

            ref.child(messageKey).updateChildren(messageInfoMap);


        }
    }

    private void GetUserInfo() {
        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentUserName = dataSnapshot.child("username").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void Initialize() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        headerText = findViewById(R.id.header_text);
        headerText.setText(currentGroupID);
        backButton = findViewById(R.id.btn_back);
        sendMessageBtn = findViewById(R.id.send_button);
        userInput = findViewById(R.id.input_message);
        mMessageRecycler = (RecyclerView) findViewById(R.id.chat_recycler_view);
        mMessageAdapter = new MessageListAdapter(this, messageList, currentUserID);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMessageRecycler.setLayoutManager(layoutManager);


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
        groupsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                    UpdateLastSeen();
                    playNotification();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                    UpdateLastSeen();
                    playNotification();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        groupsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                    UpdateLastSeen();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                    UpdateLastSeen();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    private void UpdateLastSeen() {
        HashMap<String, Object> data = new HashMap<>();
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String currentDate = currentDateFormat.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
        String currentTime = currentTimeFormat.format(calForTime.getTime());

        data.put("timestamp", System.currentTimeMillis());
        data.put("date", currentDate + " " + currentTime);

        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Userchats").child(currentGroupName).updateChildren(data);
    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        if (dataSnapshot.child("message").exists()) {
            HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
            String chatMessage = value.get("message").toString();
            String chatName = value.get("name").toString();
            String chatTime = value.get("time").toString();
            long timeStamp = Long.parseLong(value.get("timestamp").toString());
            String uid = value.get("uid").toString();
            String chatDate = value.get("date").toString();
            MessageModel messageModel = new MessageModel(uid, chatName, chatDate, chatTime, chatMessage);
            messageList.add(messageModel);
            mMessageAdapter.notifyDataSetChanged();


            //displayTextMessage.append(chatName + " :\n" +chatMessage + "\n" + chatTime + "   " + chatDate +"\n\n\n");

        }
    }

    private void DisplayAllMessages(DataSnapshot dataSnapshot) {
        Iterator iterator2 = dataSnapshot.getChildren().iterator();
        while (iterator2.hasNext()) {
            HashMap<String, Object> value = (HashMap<String, Object>) ((DataSnapshot) iterator2.next()).getValue();
            String chatMessage = value.get("message").toString();
            String chatName = value.get("name").toString();
            String chatTime = value.get("time").toString();
            long timeStamp = Long.parseLong(value.get("timestamp").toString());
            String uid = value.get("uid").toString();
            String chatDate = value.get("date").toString();

            MessageModel messageModel = new MessageModel(uid, chatName, chatDate, chatTime, chatMessage);
            messageList.add(messageModel);
            mMessageAdapter.notifyDataSetChanged();
        }
    }
    void playNotification(){
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
