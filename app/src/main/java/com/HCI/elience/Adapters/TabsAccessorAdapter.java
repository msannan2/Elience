package com.HCI.elience;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.HCI.elience.fragments.ChatFragment;
import com.HCI.elience.fragments.GroupsFragment;
import com.HCI.elience.fragments.MenuFragment;

public class TabsAccessorAdapter extends FragmentPagerAdapter {
    public TabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                MenuFragment menuFragment = new MenuFragment();
                return menuFragment;
            case 1:
                ChatFragment chatsFragment = new ChatFragment();
                return chatsFragment;
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
                return "My Chats";
            case 2:
                return "My Groups";

            default:
                return null;
        }

    }
}
