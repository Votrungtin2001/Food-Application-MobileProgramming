package com.example.foodapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class OrderAdapter extends FragmentStatePagerAdapter {
final int pageCount = 3;
private String tabTitles[] = new String[]{"Đang đến","Lịch sử","Đã hủy"};
    public OrderAdapter(@NonNull FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new OrderComingFragment();
            case 1:
                return new OrderHistoryFragment();
            case 2:
                return new OrderDraftFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
