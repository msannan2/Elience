package com.HCI.elience.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.HCI.elience.activities.ChatActivity;
import com.HCI.elience.activities.GroupChatActivity;
import com.HCI.elience.models.JoinGroupModel;
import com.HCI.elience.R;
import com.HCI.elience.activities.MainActivity;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class BrowseGroupsAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<JoinGroupModel> mGroupsList;
    private List<String> userGroups;
    private String currentUser;

    public BrowseGroupsAdapter(Context context, List<JoinGroupModel> groupsList, List<String> userGroups,String currentUser) {
        mContext = context;
        mGroupsList = groupsList;
        this.userGroups=userGroups;
        this.currentUser = currentUser;
    }

    @Override
    public int getItemCount() {
        return mGroupsList.size();
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.browse_group_item, parent, false);
        return new BrowseGroupsHolder(view);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        JoinGroupModel group = (JoinGroupModel) mGroupsList.get(position);
        ((BrowseGroupsHolder)holder).bind(group);
    }

    public class BrowseGroupsHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        ImageButton btnJoin;
        BrowseGroupsHolder(View itemView) {
            super(itemView);

            groupName = itemView.findViewById(R.id.browsegroupname);
            groupName.setTextColor(Color.WHITE);
            btnJoin = itemView.findViewById(R.id.btn_join);

        }

        public void bind(final JoinGroupModel group) {
            groupName.setText(group.groupName);
            if(userGroups.contains(group.groupName)) {
                btnJoin.setEnabled(false);
                btnJoin.setImageResource(R.drawable.check_white);

            }
            else
            {
                btnJoin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String,Object> data=new HashMap<>();
                        Calendar calForDate = Calendar.getInstance();
                        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
                        String currentDate=currentDateFormat.format(calForDate.getTime());

                        Calendar calForTime = Calendar.getInstance();
                        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
                        String currentTime=currentTimeFormat.format(calForTime.getTime());

                        data.put("timestamp",System.currentTimeMillis());
                        data.put("date",currentDate+" "+currentTime);


                        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("Usergroups").child(group.groupName).updateChildren(data);
                        Intent groupChatIntent = new Intent(mContext, GroupChatActivity.class);
                        groupChatIntent.putExtra("groupName", group.groupName);
                        mContext.startActivity(groupChatIntent);
                    }
                });
            }

            // Format the stored timestamp into a readable String using method.
        }

    }



}