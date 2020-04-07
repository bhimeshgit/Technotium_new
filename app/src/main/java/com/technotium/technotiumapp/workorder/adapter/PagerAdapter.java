package com.technotium.technotiumapp.workorder.adapter;

import android.os.Bundle;
import android.util.Log;

import com.technotium.technotiumapp.workorder.fragment.CustomerInfoFragment;
import com.technotium.technotiumapp.workorder.fragment.OrderInfoFragment;
import com.technotium.technotiumapp.workorder.fragment.WorkcommitFragment;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Bundle bundle;
    private Fragment[] tabList = new Fragment[3];
    public PagerAdapter(FragmentManager fm, int NumOfTabs,WorkOrderPojo workOrderPojo) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        if(workOrderPojo!=null){
            setWorkorderData(workOrderPojo);
        }
    }
    @Override
    public Fragment getItem(int position) {

        if (tabList[ position] != null) {
            // Return a tab we created earlier..
            return tabList[position];
        } else {
            switch (position) {
                case 0:
                    tabList[0] = new CustomerInfoFragment();
                    if(bundle!=null){
                        tabList[0].setArguments(bundle);
                    }
                    return tabList[0];
                case 1:
                    tabList[1] = new OrderInfoFragment();
                    if(bundle!=null){
                        tabList[1].setArguments(bundle);
                    }
                    return tabList[1];
                case 2:
                    tabList[2] = new WorkcommitFragment();
                    if(bundle!=null){
                        tabList[2].setArguments(bundle);
                    }
                    return tabList[2];
            }
        }
        return null;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public void setWorkorderData(WorkOrderPojo workorder){
        bundle=new Bundle();
        bundle.putSerializable("workorder",workorder);
    }
}