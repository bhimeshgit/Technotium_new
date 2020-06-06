package com.technotium.technotiumapp.status.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.status.adapter.StatusPagerAdapter;
import com.technotium.technotiumapp.workorder.activity.SearchOrderActivity;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

public class OrderStatusActivity extends AppCompatActivity {
    ViewPager viewPager;
    WorkOrderPojo workOrderPojo;
    StatusPagerAdapter adapter;
    OrderStatusActivity currentActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        setTitle("Status");
        currentActivity=OrderStatusActivity.this;
        if(getIntent().getSerializableExtra("orderData") != null) {
            workOrderPojo =(WorkOrderPojo) getIntent().getSerializableExtra("orderData");
        }
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("ADMIN"),0);
        tabLayout.addTab(tabLayout.newTab().setText("TECHNICAL"),1);

        adapter= new StatusPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),Integer.parseInt(workOrderPojo.getPkid()));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(currentActivity, SearchOrderActivity.class);
        intent.putExtra("modul","status");
        startActivity(intent);
        finish();
    }
}
