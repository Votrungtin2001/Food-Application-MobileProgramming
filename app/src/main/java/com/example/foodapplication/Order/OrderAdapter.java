package com.example.foodapplication.Order;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class OrderAdapter extends FragmentStatePagerAdapter {
final int pageCount = 3;
private String tabTitles[] = new String[]{"Đang đến","Lịch sử","Đơn nháp"};

    public OrderAdapter( FragmentManager fm, int tabCount) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }


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
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
