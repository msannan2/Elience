package com.HCI.elience.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;

import com.HCI.elience.adapters.AskQuestionAdapter;
import com.HCI.elience.models.JoinGroupModel;
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

public class AskQuestionActivity extends AppCompatActivity {

    private EditText__SF_Pro_Display_Medium searchBar;
    private RecyclerView groupsRecycler;
    private AskQuestionAdapter groupsAdapter;
    private List<JoinGroupModel> joinGroupModelList=new ArrayList<>();
    private List<JoinGroupModel> filtered=new ArrayList<>();
    private String currentUserID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_groups);
        setTransparency();
        searchBar=findViewById(R.id.search_bar);
        groupsRecycler=findViewById(R.id.browse_recyclerView);
        currentUserID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        groupsAdapter = new AskQuestionAdapter(this,filtered,currentUserID);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        groupsRecycler.setLayoutManager(layoutManager);
        groupsRecycler.setAdapter(groupsAdapter);
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Therapists");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                filtered.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                String key=ds.getKey();
                String name = ds.child("name").getValue().toString();
                filtered.add(new JoinGroupModel(key,name));
                groupsAdapter.notifyDataSetChanged();
            }
        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String query=searchBar.getText().toString().trim();
                filtered.clear();
                for( JoinGroupModel j : joinGroupModelList){
                    if(j.groupID.contains(query))
                    {
                        filtered.add(new JoinGroupModel(j.groupName,j.groupID));
                        groupsAdapter.notifyDataSetChanged();
                    }
                }


            }
        });

    }

    void updateUsergroupsList() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("Therapists");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                joinGroupModelList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key=ds.getKey();
                    String name = ds.child("name").getValue().toString();
                    joinGroupModelList.add(new JoinGroupModel(key,name));
                }
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
