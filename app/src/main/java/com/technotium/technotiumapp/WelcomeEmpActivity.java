package com.technotium.technotiumapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.technotium.technotiumapp.config.services.SyncingService;
import com.technotium.technotiumapp.employee.AddUpdateEmpActivity;
import com.technotium.technotiumapp.employee.ManageEmployeeActivity;
import com.technotium.technotiumapp.employee.MyProfileActivity;
import com.technotium.technotiumapp.employee.model.EmployeePojo;
import com.technotium.technotiumapp.model.HomeIcon;
import com.technotium.technotiumapp.payment.activity.PaymentListActivity;
import com.technotium.technotiumapp.workorder.activity.SearchOrderActivity;
import com.technotium.technotiumapp.workorder.activity.SendSmsActivity;
import com.technotium.technotiumapp.workorder.activity.WorkOrderActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    String[] permissions= new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CAMERA};
    public static final int MULTIPLE_PERMISSIONS = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

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
            menu.findItem(R.id.send_sms).setVisible(false);
        }
        if(SessionManager.getMyInstance(currentActivity).getEmpType().equals("Dealer")){
            menu.findItem(R.id.send_sms).setVisible(false);
        }

        if(!ApplicationGlobal.checkInternetConenction(currentActivity)){
            ApplicationGlobal.shownointernetconnectiondialog(currentActivity);
        }
        else{
            checkValidUser();
        }
        } catch (Exception e) {
        }
    }

    private void init() {
        currentActivity=WelcomeEmpActivity.this;
        icon_list=new ArrayList<HomeIcon>();
        int count=0;
        for (String Name:iconName){
            if(Name.equals("Dealer Incentive")) {
                if (SessionManager.getMyInstance(currentActivity).getEmpType().equals("Employee") || SessionManager.getMyInstance(currentActivity).getEmpType().equals("Electrician")) {
                    continue;
                }
            }
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
//                    Intent intent1 = new Intent(currentActivity, PaymentListActivity.class);
//                    startActivity(intent1);
//                    return;
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
                else if(position==7){
                    intent.putExtra("modul","dealer_incentive");
                }
                startActivity(intent);

            }
        });

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
        if(id==R.id.send_sms){
            startActivity(new Intent(currentActivity, SendSmsActivity.class));
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
        jsonParserVolley.addParameter("mobile",SessionManager.getMyInstance(currentActivity).getEmpMobile());
        jsonParserVolley.addParameter("pass",SessionManager.getMyInstance(currentActivity).getEmpPass());
        jsonParserVolley.executeRequest(Request.Method.POST,WebUrl.CHECK_AUTHORIZED_EMPLOYEE ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==2){
                                SessionManager.getMyInstance(currentActivity).clearPreference();
                                startActivity(new Intent(currentActivity, LoginActivity.class));
                                finishAffinity();
                                return;
                            }

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

                                checkPermissions();

                                if(!ApplicationGlobal.isMyServiceRunning(currentActivity,SyncingService.class)){
                                    Log.d("iss","service="+"service not running");
                                    SyncingService.startSyncing(currentActivity,new Intent());
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

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(currentActivity,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                            permissionsDenied += "\n" + per;

                        }

                    }
                    // Show permissionsDenied
                  //  updateViews();
                }
                return;
            }
        }
    }

}
