package com.HCI.elience.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.HCI.elience.adapters.MyChatsAdapter;
import com.HCI.elience.models.GroupsModel;
import com.HCI.elience.adapters.MyGroupsAdapter;
import com.HCI.elience.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private View groupsFragmentView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MyChatsAdapter groupsAdapter;
    DatabaseReference dbRef;
    MyGroupsAdapter adapter;
    private String currentUser;

    private ArrayList<GroupsModel> groupList=new ArrayList<GroupsModel>();
    final Map<String,GroupsModel> map=new HashMap<>();

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupsFragmentView= inflater.inflate(R.layout.fragment_chats, container, false);

        Initialize();
        dbRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("Userchats");
        groupsAdapter=new MyChatsAdapter(groupList,FirebaseAuth.getInstance().getCurrentUser().getUid());
        recyclerView.setAdapter(groupsAdapter);
        RetrieveGroups();
        GetChatUpdates();
        return groupsFragmentView;
    }

    private void RetrieveGroups() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren())
                {
                    progressBar.setVisibility(View.INVISIBLE);
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    String date = ds.child("date").getValue().toString();
                    final long timestamp = ds.child("timestamp").getValue(Long.class);
                    String name = ds.child("name").getValue().toString();
                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Therapists").child(key).child("Questions").child(currentUser).child("chats");

                    final GroupsModel temp=new GroupsModel();
                    temp.timestamp=timestamp;
                    temp.lastseen=timestamp;
                    temp.date=date;
                    temp.unread=0;
                    temp.groupName= key;
                    temp.groupID=name;
                    temp.lastMessage="";


                    dbref.orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator iterator2 = dataSnapshot.getChildren().iterator();
                            while (iterator2.hasNext()) {
                                HashMap <String,Object> value = (HashMap<String, Object>) ((DataSnapshot) iterator2.next()).getValue();
                                String chatMessage = value.get("message").toString();
                                String chatName = value.get("name").toString();
                                String chatTime = value.get("time").toString();
                                long timeStamp = Long.parseLong(value.get("timestamp").toString());
                                String uid = value.get("uid").toString();
                                String chatDate = value.get("date").toString();

                                if(temp.timestamp>timestamp)
                                {
                                    temp.unread=1;
                                }

                                temp.lastMessage=chatMessage;
                                temp.date = chatDate+" "+chatTime;
                                temp.timestamp=timeStamp;

                                map.put(temp.groupName, temp);
                                groupList.clear();
                                groupList.addAll(map.values());
                                Collections.sort(groupList, new Comparator<GroupsModel>() {
                                    @Override
                                    public int compare(GroupsModel t1, GroupsModel t2) {
                                        return t2.timestamp.compareTo(t1.timestamp);
                                    }
                                });
                                groupsAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            map.put(temp.groupName, temp);
                            groupList.clear();
                            groupList.addAll(map.values());
                            Collections.sort(groupList, new Comparator<GroupsModel>() {
                                @Override
                                public int compare(GroupsModel t1, GroupsModel t2) {
                                    return t2.timestamp.compareTo(t1.timestamp);
                                }
                            });
                            groupsAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Handle possible errors.
                        }
                    });



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void GetChatUpdates(){
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren())
                {
                    progressBar.setVisibility(View.INVISIBLE);
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    String date = ds.child("date").getValue().toString();
                    final long timestamp = ds.child("timestamp").getValue(Long.class);
                    String name = ds.child("name").getValue().toString();
                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Therapists").child(key).child("Questions").child(currentUser);

                    final GroupsModel temp=new GroupsModel();
                    temp.timestamp=timestamp;
                    temp.lastseen=timestamp;
                    temp.date=date;
                    temp.unread=0;
                    temp.groupName=key;
                    temp.groupID=name;
                    temp.lastMessage="";

                    dbref.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if (dataSnapshot.child("message").exists()) {
                                HashMap <String,Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                                String chatMessage = value.get("message").toString();
                                String chatName = value.get("name").toString();
                                String chatTime = value.get("time").toString();
                                long timeStamp = Long.parseLong(value.get("timestamp").toString());
                                String uid = value.get("uid").toString();
                                String chatDate = value.get("date").toString();

                                if(temp.timestamp>timestamp)
                                {
                                    temp.unread=1;
                                }
                                temp.lastMessage=chatMessage;
                                temp.date = chatDate+" "+chatTime;
                                temp.timestamp=timeStamp;

                                map.put(temp.groupName, temp);
                                groupList.clear();
                                groupList.addAll(map.values());
                                Collections.sort(groupList, new Comparator<GroupsModel>() {
                                    @Override
                                    public int compare(GroupsModel t1, GroupsModel t2) {
                                        return t2.timestamp.compareTo(t1.timestamp);
                                    }
                                });
                                groupsAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.INVISIBLE);
                            }




                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Initialize() {
        recyclerView = groupsFragmentView.findViewById(R.id.recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        progressBar=groupsFragmentView.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}

