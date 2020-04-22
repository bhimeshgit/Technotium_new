package com.technotium.technotiumapp.workorder.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.google.android.material.tabs.TabLayout;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.workorder.Interface.DataUpdate;
import com.technotium.technotiumapp.workorder.adapter.PagerAdapter;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

public class WorkOrderActivity extends AppCompatActivity {
    ViewPager viewPager;
    WorkOrderPojo workOrderPojo=new WorkOrderPojo();
    PagerAdapter adapter;
    DataUpdate dataUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("CUSTOMER"),0);
        tabLayout.addTab(tabLayout.newTab().setText("ORDER"),1);
        tabLayout.addTab(tabLayout.newTab().setText("COMMITMENTS"),2);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);
        if(getIntent().getSerializableExtra("orderData") != null) {
            workOrderPojo =(WorkOrderPojo) getIntent().getSerializableExtra("orderData");
            adapter= new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),workOrderPojo);
        }
        else{
            adapter= new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),null);
        }
        viewPager.setAdapter(adapter);
        dataUpdate = (DataUpdate) adapter.getItem(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                dataUpdate = (DataUpdate) adapter.getItem(tab.getPosition());
                dataUpdate.setData(workOrderPojo);
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                dataUpdate = (DataUpdate) adapter.getItem(tab.getPosition());
                workOrderPojo=dataUpdate.getData();
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                dataUpdate = (DataUpdate) adapter.getItem(tab.getPosition());
                dataUpdate.setData(workOrderPojo);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(WorkOrderActivity.this,SearchOrderActivity.class);
        intent.putExtra("modul","workorder");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(dataUpdate!=null){
            dataUpdate.onActivityResult(requestCode, resultCode, data);
        }

    }
}
