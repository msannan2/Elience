package com.HCI.elience.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.HCI.elience.BrowseGroupsAdapter;
import com.HCI.elience.JoinGroupModel;
import com.HCI.elience.MessageListAdapter;
import com.HCI.elience.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import customfonts.EditText__SF_Pro_Display_Medium;

public class BrowseGroups extends AppCompatActivity {

    private EditText__SF_Pro_Display_Medium searchBar;
    private RecyclerView groupsRecycler;
    private BrowseGroupsAdapter groupsAdapter;
    private List<JoinGroupModel> joinGroupModelList=new ArrayList<>();
    private List<String> userGroupsList=new ArrayList<>();
    private String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_groups);
        setTransparency();
        searchBar=findViewById(R.id.search_bar);
        groupsRecycler=findViewById(R.id.browse_recyclerView);
        currentUserID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        groupsAdapter = new BrowseGroupsAdapter(this,joinGroupModelList,userGroupsList,currentUserID);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        groupsRecycler.setLayoutManager(layoutManager);
        groupsRecycler.setAdapter(groupsAdapter);

    }

    void updateUsergroupsList() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Usergroups");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userGroupsList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    userGroupsList.add(ds.getKey());
                }
                groupsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
            usersRef.addValueEventListener(valueEventListener);
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateUsergroupsList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUsergroupsList();
    }

    void setTransparency() {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }

}
