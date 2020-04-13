package com.technotium.technotiumapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.technotium.technotiumapp.adapter.HomeIconAdapter;
import com.technotium.technotiumapp.config.ApplicationGlobal;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.employee.AddUpdateEmpActivity;
import com.technotium.technotiumapp.employee.ManageEmployeeActivity;
import com.technotium.technotiumapp.employee.MyProfileActivity;
import com.technotium.technotiumapp.employee.model.EmployeePojo;
import com.technotium.technotiumapp.model.HomeIcon;
import com.technotium.technotiumapp.workorder.activity.SearchOrderActivity;
import com.technotium.technotiumapp.workorder.activity.WorkOrderActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WelcomeEmpActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener  {

    RecyclerView recyclerView;
    ProgressDialog pDialog;
    GridLayoutManager layoutManager;
    ArrayList<HomeIcon> icon_list;
    int[] imageId={R.drawable.home_work_order,R.drawable.home_payment,R.drawable.home_doc_scan,R.drawable.home_whats_go_on,R.drawable.home_material,R.drawable.expense,R.drawable.after_sale,R.drawable.incentive};
    String[] iconName={"Work Order","Payment","Doc Scan","Status","Material","Expenses","After Sale","Dealer Incentive"};
    HomeIconAdapter adapter;
    WelcomeEmpActivity currentActivity;
    ImageView empProfileImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_emp);
        init();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.welcome_txt);
        empProfileImg=(ImageView) headerView.findViewById(R.id.imageView);
        Glide.with(currentActivity).load(WebUrl.BASE_URL+SessionManager.getMyInstance(currentActivity).getEmpImage())
                 .apply(RequestOptions.skipMemoryCacheOf(false))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(empProfileImg);

        navUsername.setText("Welcome "+ SessionManager.getMyInstance(currentActivity).getEmpName());
        Menu menu=navigationView.getMenu();
        if(SessionManager.getMyInstance(currentActivity).getEmpType().equals("Employee") || SessionManager.getMyInstance(currentActivity).getEmpType().equals("Electrician")){
            menu.findItem(R.id.manage_employee).setVisible(false);
        }

        if(!ApplicationGlobal.checkInternetConenction(currentActivity)){
            ApplicationGlobal.shownointernetconnectiondialog(currentActivity);
        }
        else{
            checkValidUser();
        }

    }

    private void init() {
        currentActivity=WelcomeEmpActivity.this;
        icon_list=new ArrayList<HomeIcon>();
        int count=0;
        for (String Name:iconName){
            HomeIcon homeIcon=new HomeIcon(imageId[count],Name);
            count++;
            icon_list.add(homeIcon);
        }
        recyclerView= (RecyclerView)findViewById(R.id.recyclerView);
        layoutManager=new GridLayoutManager(WelcomeEmpActivity.this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new HomeIconAdapter(icon_list, WelcomeEmpActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new HomeIconAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent=new Intent(currentActivity,SearchOrderActivity.class);
                if(position==0){
                    intent.putExtra("modul","workorder");
                }
                else if(position==1){
                    if(SessionManager.getMyInstance(currentActivity).getEmpType().equalsIgnoreCase("Electrician")){
                           return;
                    }
                    intent.putExtra("modul","payment");
                }
                else if(position==2){
                    intent.putExtra("modul","docscan");
                }
                else if(position==3){
                    intent.putExtra("modul","status");
                }
                else if(position==4){
                    intent.putExtra("modul","material");
                }
                else if(position==5){
                    intent.putExtra("modul","expense");
                }
                else if(position==6){
                    intent.putExtra("modul","after_sale");
                }
                startActivity(intent);

            }
        });
        isCameraPermissionGranted();
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public  boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:
                   isWriteStoragePermissionGranted();
                   break;
            case 2:
                isReadStoragePermissionGranted();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_welcome_employee_drawer, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.manage_employee){
            startActivity(new Intent(currentActivity, ManageEmployeeActivity.class));
        }
        else if(id==R.id.my_profile){
            startActivity(new Intent(currentActivity, MyProfileActivity.class));
        }
        else if(id==R.id.logout){
            SessionManager.getMyInstance(currentActivity).clearPreference();
            startActivity(new Intent(currentActivity, LoginActivity.class));
            finishAffinity();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //          super.onBackPressed();
            int pid=android.os.Process.myPid();

            final AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(this);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setTitle("Exit");
            alertDialogBuilder.setMessage("Do you really want to Exit App");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialogBuilder.create();
            alertDialogBuilder.show();
        }

    }

    public void checkValidUser(){
        showProgressDialog();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("empid",SessionManager.getMyInstance(currentActivity).getEmpid());

        jsonParserVolley.executeRequest(Request.Method.POST,WebUrl.CHECK_AUTHORIZED_EMPLOYEE ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==0){
                                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                                if (drawer.isDrawerOpen(GravityCompat.START)) {
                                    drawer.closeDrawer(GravityCompat.START);
                                } else {

                                    final AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(currentActivity);
                                    alertDialogBuilder.setCancelable(false);
                                    alertDialogBuilder.setTitle("Exit");
                                    alertDialogBuilder.setMessage(jsonObject.getString("message"));
                                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finishAffinity();
                                        }
                                    });
                                    alertDialogBuilder.create();
                                    alertDialogBuilder.show();
                                }
                            }
                            else{
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++) {
                                    JSONObject jsonWO = jsonArray.getJSONObject(i);
                                    EmployeePojo empPojo = new EmployeePojo();
                                    empPojo.setEmp_image(jsonWO.getString("emp_image"));
                                    empPojo.setFname(jsonWO.getString("fname"));
                                    SessionManager.getMyInstance(currentActivity).setEmpName(empPojo.getFname());
                                    if(!empPojo.getEmp_image().equals("") && !empPojo.getEmp_image().equals("null") ){
                                        SessionManager.getMyInstance(currentActivity).setEmpImage(empPojo.getEmp_image());
                                        Glide.with(currentActivity).load(WebUrl.BASE_URL+SessionManager.getMyInstance(currentActivity).getEmpImage())
                                                .apply(RequestOptions.skipMemoryCacheOf(false))
                                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(empProfileImg);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
    public void showProgressDialog(){
        pDialog = new ProgressDialog(currentActivity);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

}