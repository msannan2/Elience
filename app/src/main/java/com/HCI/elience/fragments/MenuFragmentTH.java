package com.HCI.elience.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.card.MaterialCardView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.HCI.elience.ForumActivity;
import com.HCI.elience.R;
import com.HCI.elience.activities.BrowseGroupsActivity;
import com.HCI.elience.activities.LoginActivity;
import com.HCI.elience.activities.MainActivity;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragmentTH extends Fragment {
    private MaterialCardView browseGroupsCard,browseForumsCard ;
    private View view;
    public MenuFragmentTH() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_menu_th, container, false);
        browseGroupsCard= view.findViewById(R.id.materialCardView);
        browseGroupsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoBrowseGroups();
            }
        });
        browseGroupsCard= view.findViewById(R.id.materialCardView2);
        browseGroupsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoBrowseForums();
            }
        });
        return view;
    }

    void gotoBrowseGroups()
    {
        Intent i=new Intent(getContext(), BrowseGroupsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(i);
    }

    void gotoBrowseForums()
    {
        Intent i=new Intent(getContext(), ForumActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(i);
    }


}
