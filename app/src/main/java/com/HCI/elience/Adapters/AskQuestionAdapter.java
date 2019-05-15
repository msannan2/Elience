package com.HCI.elience;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.HCI.elience.JoinGroupModel;
import com.HCI.elience.R;
import com.HCI.elience.activities.MainActivity;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AskQuestionAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<JoinGroupModel> mGroupsList;

    private String currentUser;

    public AskQuestionAdapter(Context context, List<JoinGroupModel> groupsList,String currentUser) {
        mContext = context;
        mGroupsList = groupsList;
        this.currentUser=currentUser;
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
        final JoinGroupModel group = (JoinGroupModel) mGroupsList.get(position);
        ((BrowseGroupsHolder)holder).cardView.setOnClickListener(new View.OnClickListener() {
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


                FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("Userchats").child(group.groupName).updateChildren(data);
                Intent i=new Intent(mContext,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        });
    }

    public class BrowseGroupsHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        MaterialCardView cardView;

        BrowseGroupsHolder(View itemView) {
            super(itemView);

            groupName = itemView.findViewById(R.id.browsegroupname);
            groupName.setTextColor(Color.WHITE);
            cardView=itemView.findViewById(R.id.myquestioncard);


        }
    }
}