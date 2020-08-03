package com.technotium.technotiumapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.technotium.technotiumapp.config.ApplicationGlobal;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.MySingleton;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText txtMobile,txtPass;
    Button btnLogin;
    int success=0;
    LoginActivity currentActivity;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        login();
    }

    private void login() {
         btnLogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
//                 throw new RuntimeException("Creash testing ");
                 showProgressDialog();
                 if(SessionManager.getMyInstance(currentActivity).getFirebaseId().equalsIgnoreCase("")) {
                     FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(currentActivity, new OnSuccessListener<InstanceIdResult>() {
                         @Override
                         public void onSuccess(InstanceIdResult instanceIdResult) {
                             SessionManager.getMyInstance(currentActivity).setFirebaseId(instanceIdResult.getToken());
                             checkLogin();
                         }
                     });
                 }
                 else{
                     checkLogin();
                 }
             }
         });
    }
    private void checkLogin(){
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("mobile",txtMobile.getText().toString());
        jsonParserVolley.addParameter("pass",txtPass.getText().toString());
        jsonParserVolley.addParameter("fid",SessionManager.getMyInstance(currentActivity).getFirebaseId());

        jsonParserVolley.executeRequest(Request.Method.POST,WebUrl.LOGIN_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        Log.d("iss",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                SessionManager.getMyInstance(currentActivity).setEmpid(jsonObject.getString("empid"));
                                SessionManager.getMyInstance(currentActivity).setEmpName(jsonObject.getString("empname"));
                                SessionManager.getMyInstance(currentActivity).setEmpImage(jsonObject.getString("emp_image"));
                                SessionManager.getMyInstance(currentActivity).setEmpType(jsonObject.getString("emp_type"));
                                SessionManager.getMyInstance(currentActivity).setEmpMobile(txtMobile.getText().toString());
                                SessionManager.getMyInstance(currentActivity).setEmpPass(txtPass.getText().toString());
                                Intent intent=new Intent(currentActivity,WelcomeEmpActivity.class);
                                startActivity(intent);
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

    private void init() {
        currentActivity=LoginActivity.this;
        txtMobile=findViewById(R.id.txtMobile);
        txtPass=findViewById(R.id.txtPass);
        btnLogin=findViewById(R.id.btnLogin);
        if(!SessionManager.getMyInstance(currentActivity).getEmpid().equalsIgnoreCase("")) {
                startActivity(new Intent(currentActivity, WelcomeEmpActivity.class));
                finish();
        }

    }
    public void showProgressDialog(){
        pDialog = new ProgressDialog(currentActivity);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
