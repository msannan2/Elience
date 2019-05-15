package com.HCI.elience;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.HCI.elience.activities.GroupChatActivity;
import com.HCI.elience.activities.MainActivity;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MyGroupsAdapter extends RecyclerView.Adapter<MyGroupsAdapter.ViewHolder> {

    private Context mContext;
    private List<GroupsModel> mData;
    private String currentUser;

    public MyGroupsAdapter(List<GroupsModel> mData,String currentUser) {
        this.mData = mData;
        this.currentUser=currentUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.group_list_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final GroupsModel groupsModel = mData.get(i);
        viewHolder.groupName.setText(groupsModel.groupName);
        viewHolder.lastMessage.setText(groupsModel.lastMessage);
        viewHolder.date.setText(groupsModel.date);

        if(groupsModel.timestamp>groupsModel.lastseen)
        {
            viewHolder.unreadCount.setText(Integer.toString(1));

        }
        else {
            viewHolder.unreadCount.setVisibility(View.GONE);
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
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

                FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("Usergroups").child(groupsModel.groupName).updateChildren(data);
                viewHolder.unreadCount.setVisibility(View.GONE);
                Intent groupChatIntent = new Intent(mContext, GroupChatActivity.class);
                groupChatIntent.putExtra("groupName", groupsModel.groupName);
                mContext.startActivity(groupChatIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView groupName;
        public TextView lastMessage;
        public TextView date;
        public TextView unreadCount;
        public MaterialCardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.browsegroupname);
            lastMessage = itemView.findViewById(R.id.lastmessage);
            date = itemView.findViewById(R.id.date);
            unreadCount = itemView.findViewById(R.id.unread_notifier);
            cardView = itemView.findViewById(R.id.mygroupcard);
        }
    }
}
