package com.HCI.elience;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {

    private View groupsFragmentView;
    private RecyclerView recyclerView;
    DatabaseReference dbRef;
    FirebaseRecyclerOptions<GroupsModel> options;
    FirebaseRecyclerAdapter<GroupsModel,GroupsListViewHolder> adapter;


    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupsFragmentView= inflater.inflate(R.layout.fragment_groups, container, false);
        recyclerView=groupsFragmentView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        dbRef= FirebaseDatabase.getInstance().getReference().child("Groups");
        options = new FirebaseRecyclerOptions.Builder<GroupsModel>().setQuery(dbRef,GroupsModel.class).build();
        adapter = new FirebaseRecyclerAdapter<GroupsModel, GroupsListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GroupsListViewHolder holder, int position, @NonNull GroupsModel model) {
                holder.groupName.setText(model.groupName);
            }

            @NonNull
            @Override
            public GroupsListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
               View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_list_item, viewGroup, false);
                return new GroupsListViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        return groupsFragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null)
        {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        if(adapter!=null)
        {
            adapter.stopListening();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null) {
            adapter.startListening();
        }
    }
}
