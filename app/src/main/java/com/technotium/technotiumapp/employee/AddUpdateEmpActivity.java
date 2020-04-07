package com.technotium.technotiumapp.employee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.employee.model.EmployeePojo;
import com.technotium.technotiumapp.workorder.adapter.SpinnerAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddUpdateEmpActivity extends AppCompatActivity {

    EditText txtfname,txtlname,txtmobile,txtaddr,txtemail;
    Button btnAddNewEmp;
    EmployeePojo emp;
    AddUpdateEmpActivity currentActivity;
    String mgrid,empid;
    ProgressDialog pDialog;
    Spinner spnEmpType;
    ArrayList<String> empTypeList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_emp);
        init();
        Intent intent=getIntent();
        if(intent.getStringExtra("emp")!=null){
            Gson gson = new Gson();
            emp=gson.fromJson(intent.getStringExtra("emp"), EmployeePojo.class);
            setData(emp);
        }

    }

    private void init() {
        currentActivity=AddUpdateEmpActivity.this;
        txtfname=findViewById(R.id.txtfname);
        txtlname=findViewById(R.id.txtlname);
        txtmobile=findViewById(R.id.txtmobile);
        txtaddr=findViewById(R.id.txtaddr);
        txtemail=findViewById(R.id.txtemail);
        btnAddNewEmp=findViewById(R.id.btnAddNewEmp);
        spnEmpType=findViewById(R.id.spnEmpType);
        btnAddNewEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmp();
            }
        });
        empTypeList.add("Employee");
        empTypeList.add("Electrician");
        if(SessionManager.getMyInstance(currentActivity).getEmpType().equals("Admin")){
            empTypeList.add("Dealer");
        }
        SpinnerAdapter spinnerAdapter=new SpinnerAdapter(currentActivity, empTypeList);
        spnEmpType.setAdapter(spinnerAdapter);
    }

    private void setData(EmployeePojo emp){
        btnAddNewEmp.setText("Update");
        txtfname.setText(emp.getFname());
        txtlname.setText(emp.getLname());
        txtmobile.setText(emp.getMobile());
        txtaddr.setText(emp.getAddress());
        txtemail.setText(emp.getEmail());
        if(emp.getEmpType()=="2"){
            spnEmpType.setSelection(1);
        }
        else{
            spnEmpType.setSelection(0);
        }
    }
    public void showProgressDialog(){
        pDialog = new ProgressDialog(currentActivity);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
    }
    private boolean validate(){
        if(txtfname.getText().toString().trim().equals("")){
            Toast.makeText(currentActivity,"Enter first name",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtlname.getText().toString().trim().equals("")){
            Toast.makeText(currentActivity,"Enter last name",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtmobile.getText().toString().trim().equals("")){
            Toast.makeText(currentActivity,"Enter last name",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtmobile.getText().toString().length()!=10){
            Toast.makeText(currentActivity,"Invalid Mobile Number",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtemail.getText().toString().equals("")){
            Toast.makeText(currentActivity,"Enter email id",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtaddr.getText().toString().equals("")){
            Toast.makeText(currentActivity,"Enter address",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void addEmp() {
        if(!validate()){
            return;
        }
        showProgressDialog();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("firstName",txtfname.getText().toString());
        jsonParserVolley.addParameter("lastName",txtlname.getText().toString());
        jsonParserVolley.addParameter("mobile",txtmobile.getText().toString());
        jsonParserVolley.addParameter("email",txtemail.getText().toString());
        jsonParserVolley.addParameter("address",txtaddr.getText().toString());
        jsonParserVolley.addParameter("mgrid",SessionManager.getMyInstance(currentActivity).getEmpid());
        jsonParserVolley.addParameter("empType",spnEmpType.getSelectedItem().toString());
        if(emp!=null){
            jsonParserVolley.addParameter("empid",emp.getEmpid());
        }
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.ADD_UPDATE_EMP_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        Log.d("iss",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
