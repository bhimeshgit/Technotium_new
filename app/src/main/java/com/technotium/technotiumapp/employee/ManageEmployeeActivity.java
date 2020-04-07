package com.technotium.technotiumapp.employee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.WelcomeEmpActivity;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.employee.adapter.EmployeeAdapter;
import com.technotium.technotiumapp.employee.model.EmployeePojo;
import com.technotium.technotiumapp.workorder.activity.SearchOrderActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManageEmployeeActivity extends AppCompatActivity {

    Button btnAddNew;
    ManageEmployeeActivity currentActivity;
    RecyclerView.LayoutManager layoutManager;
    EmployeeAdapter adapter;
    ArrayList<EmployeePojo> emp_list;
    RecyclerView lv_empList;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_employee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }
    private void init(){
        btnAddNew=findViewById(R.id.btnAddNew);
        currentActivity=ManageEmployeeActivity.this;
        lv_empList=findViewById(R.id.lv_empList);
        emp_list=new ArrayList<>();
        pDialog = new ProgressDialog(currentActivity);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        onClick();
        getAllEmployeeList();
    }

    private void onClick(){
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(currentActivity,AddUpdateEmpActivity.class));
            }
        });
    }

    private void getAllEmployeeList(){
        emp_list.clear();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("mgr_id", SessionManager.getMyInstance(currentActivity).getEmpid());

        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.GET_ALL_EMP_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        Log.d("iss","response="+response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonWO=jsonArray.getJSONObject(i);
                                    EmployeePojo empPojo=new EmployeePojo();
                                    empPojo.setAddress(jsonWO.getString("address"));
                                    empPojo.setEmpid(jsonWO.getString("id"));
                                    empPojo.setEmpType(jsonWO.getString("type"));
                                    empPojo.setFname(jsonWO.getString("fname"));
                                    empPojo.setFullname(jsonWO.getString("fullname"));
                                    empPojo.setLname(jsonWO.getString("lname"));
                                    empPojo.setMobile(jsonWO.getString("mobile"));
                                    empPojo.setEmail(jsonWO.getString("email"));
                                    empPojo.setActive(Integer.parseInt(jsonWO.getString("active")));
                                    emp_list.add(empPojo);
                                }

                                layoutManager=new GridLayoutManager(currentActivity,1);
                                lv_empList.setLayoutManager(layoutManager);
                                lv_empList.setHasFixedSize(true);
                                adapter=new EmployeeAdapter(emp_list, currentActivity);
                                lv_empList.setAdapter(adapter);

                            }
                            else{
                                Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SessionManager.getMyInstance(currentActivity).progressHide();
                    }
                }
        );
    }

    public void deleteEmployee(EmployeePojo emp,final int position){
        pDialog.show();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("emp_id", emp.getEmpid());
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.DEL_EMP_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        Log.d("iss","response="+response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                emp_list.get(position).setActive(2);
                                adapter.notifyItemChanged(position);
                            }
                            else {
                                Toast.makeText(currentActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SessionManager.getMyInstance(currentActivity).progressHide();
                    }
                }
        );
    }

    public void activateEmployee(final int position){
        pDialog.show();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("emp_id", emp_list.get(position).getEmpid());
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.ACTIVATE_EMP_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        Log.d("iss","response="+response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                emp_list.get(position).setActive(1);
                                adapter.notifyItemChanged(position);
                            }
                            else{
                                Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SessionManager.getMyInstance(currentActivity).progressHide();
                    }
                }
        );
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        getAllEmployeeList();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent(currentActivity, WelcomeEmpActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(currentActivity, WelcomeEmpActivity.class);
        startActivity(intent);
        finish();
    }
}
