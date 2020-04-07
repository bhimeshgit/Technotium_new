package com.technotium.technotiumapp.status.adapter;

import android.os.Bundle;

import com.technotium.technotiumapp.status.fragment.DocumentryStatusFragment;
import com.technotium.technotiumapp.status.fragment.TechnicalStatus;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class StatusPagerAdapter  extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Bundle bundle;
    private Fragment[] tabList = new Fragment[2];
    int order_id;

    public StatusPagerAdapter(FragmentManager fm, int NumOfTabs,int order_id) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.order_id=order_id;
        bundle=new Bundle();
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (tabList[ position] != null) {
            // Return a tab we created earlier..
            return tabList[position];
        } else {
            switch (position) {
                case 0:
                    tabList[0] = new DocumentryStatusFragment();
                    bundle.putInt("order_id",order_id);
                    tabList[0].setArguments(bundle);

                    return tabList[0];
                case 1:
                    tabList[1] = new TechnicalStatus();
                    bundle.putInt("order_id",order_id);
                    tabList[1].setArguments(bundle);

                    return tabList[1];

            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
