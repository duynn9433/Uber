package com.duynn.uber.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.duynn.uber.fragment.FragmentChat;
import com.duynn.uber.fragment.FragmentHistory;
import com.duynn.uber.fragment.FragmentInfo;
import com.duynn.uber.fragment.FragmentUber;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentUber();
            case 1:
                return new FragmentHistory();
            case 2:
                return new FragmentChat();
            case 3:
                return new FragmentInfo();
            default:
                return new FragmentUber();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
