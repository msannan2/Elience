package com.HCI.elience.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.HCI.elience.fragments.ChatFragment;
import com.HCI.elience.fragments.GroupsFragment;
import com.HCI.elience.fragments.MenuFragment;
import com.HCI.elience.fragments.MenuFragmentTH;
import com.HCI.elience.fragments.QuestionsFragment;

public class THTabsAccessorAdapter extends FragmentPagerAdapter {
    public THTabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                MenuFragmentTH menuFragment = new MenuFragmentTH();
                return menuFragment;
            case 1:
                QuestionsFragment questionsFragment = new QuestionsFragment();
                return questionsFragment;
            case 2:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int i) {
        switch (i) {
            case 0:
                return "Menu";
            case 1:
                return "My Questions";
            case 2:
                return "My Groups";

            default:
                return null;
        }

    }
}
