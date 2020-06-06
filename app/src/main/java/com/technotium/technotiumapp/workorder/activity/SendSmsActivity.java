package com.technotium.technotiumapp.workorder.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.WelcomeEmpActivity;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.workorder.adapter.SmsSendAdapter;
import com.technotium.technotiumapp.workorder.adapter.WorkOrderAdapter;
import com.technotium.technotiumapp.workorder.dialog.MessageDialog;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SendSmsActivity extends AppCompatActivity {

    RecyclerView lv_wo;
    EditText txtCustomerName;
    Button sendSmsBtn;
    ArrayList<WorkOrderPojo> orderList;
    SendSmsActivity currentActivity;
    GridLayoutManager layoutManager;
    SmsSendAdapter adapter;
    String modul="";
    ProgressDialog pDialog;
    ArrayList<String> alstMobile;
    String TAG=SendSmsActivity.class.getSimpleName();
    MessageDialog messageDialog;
    CheckBox selectAllChk;
    int allChkFlg=0,itemChkflg=0;
    ArrayList<WorkOrderPojo> tempArrayList = new ArrayList<WorkOrderPojo>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Send SMS");
        //Action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_send_msg_select_all);
        View view =getSupportActionBar().getCustomView();
        selectAllChk=view.findViewById(R.id.selectAllChk);

        init();
        Intent intent=getIntent();
        if(intent!=null){
            if(intent.getStringExtra("modul")!=null){
                modul=intent.getStringExtra("modul");
                if(modul.equals("workorder")){
                    sendSmsBtn.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private void init(){
        orderList=new ArrayList<>();
        currentActivity=SendSmsActivity.this;
        lv_wo= (RecyclerView)findViewById(R.id.lv_wo);
        txtCustomerName=findViewById(R.id.txtCustomerName);
        sendSmsBtn =findViewById(R.id.btnSendSms);
        alstMobile=new ArrayList<>();
        sendSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });
        getAllWorkOrder();

        txtCustomerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = s.length();
                tempArrayList.clear();
                for(WorkOrderPojo c: orderList){
                    if (textlength <= c.getFullname().length()) {
                        if (c.getFullname().toLowerCase().contains(s.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                adapter=new SmsSendAdapter(tempArrayList,currentActivity);
                lv_wo.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        selectAllChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if(orderList!=null){
                        if(isChecked){
                            alstMobile.clear();
                            allChkFlg=1;
                            for(WorkOrderPojo w : orderList){
                                w.setSelected(isChecked);
                                if(isChecked){
                                    alstMobile.add(w.getMobile());
                                }
                            }
                        }
                        else {
                            allChkFlg=0;
                            if(itemChkflg==0) {
                                alstMobile.clear();
                                for (WorkOrderPojo w : orderList) {
                                    w.setSelected(isChecked);
                                }
                            }
                            itemChkflg=0;
                        }

                        adapter.notifyDataSetChanged();
                    }
            }
        });

    }

    private void getAllWorkOrder(){
        showProgressDialog();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("userid", SessionManager.getMyInstance(currentActivity).getEmpid());

        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.GET_ALL_WORK_ORDER_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        try {
                            Log.d("iss",response);
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonWO=jsonArray.getJSONObject(i);
                                    WorkOrderPojo workOrderPojo=new WorkOrderPojo();
                                    workOrderPojo.setPkid(jsonWO.getString("pkid"));
                                    workOrderPojo.setFname(jsonWO.get("firstName").toString());
                                    workOrderPojo.setLname(jsonWO.getString("lastName"));
                                    workOrderPojo.setCity(jsonWO.getString("city"));
                                    workOrderPojo.setState(jsonWO.getString("state"));
                                    workOrderPojo.setMobile(jsonWO.getString("mobile1"));
                                    workOrderPojo.setEmail(jsonWO.getString("email"));
                                    workOrderPojo.setAddress(jsonWO.getString("address"));
                                    workOrderPojo.setCapacity(jsonWO.getString("capacity"));
                                    workOrderPojo.setLocation(jsonWO.getString("location"));
                                    workOrderPojo.setSystemdetail(jsonWO.getString("system_detail"));
                                    workOrderPojo.setInverter(jsonWO.getString("inverter"));
                                    workOrderPojo.setStructure(jsonWO.getString("structure"));
                                    workOrderPojo.setAmount(jsonWO.getString("amount"));
                                    workOrderPojo.setLoadextension(jsonWO.getString("load_extension"));
                                    workOrderPojo.setChangeofname(jsonWO.getString("change_of_name"));
                                    workOrderPojo.setSolarsanction(jsonWO.getString("solar_sanction"));
                                    workOrderPojo.setMeterinstallation(jsonWO.getString("meter_installation"));
                                    workOrderPojo.setPanelcapacity(jsonWO.getString("panel_capacity"));
                                    workOrderPojo.setInvertercapicity(jsonWO.getString("inverter_capacity"));
                                    workOrderPojo.setOrder_date(jsonWO.getString("order_date"));
                                    workOrderPojo.setDesignation(jsonWO.getString("designation"));
                                    workOrderPojo.setRateFromCompany(jsonWO.getString("ratefromcompany"));
                                    workOrderPojo.setGridType(jsonWO.getString("gridtype"));
                                    workOrderPojo.setSubsidy_approval(jsonWO.getString("subsidy_approval"));
                                    workOrderPojo.setCommissioning(jsonWO.getString("commissioning"));
                                    workOrderPojo.setAgreement(jsonWO.getString("agreement"));
                                    workOrderPojo.setFullname(jsonWO.getString("firstName")+" "+jsonWO.getString("lastName"));
                                    workOrderPojo.setMname(jsonWO.getString("middlename"));
                                    workOrderPojo.setContactPerson(jsonWO.getString("con_PersonMobile"));
                                    workOrderPojo.setProjectType(jsonWO.getString("project_type"));
                                    workOrderPojo.setMedasaction(jsonWO.getString("meda_sanction"));
                                    workOrderPojo.setAdded_by(jsonWO.getString("added_by"));
                                    workOrderPojo.setAdded_by_type(jsonWO.getString("added_by_type"));
                                    workOrderPojo.setConsumer_no(jsonWO.getString("consumer_no"));
                                    workOrderPojo.setBu(jsonWO.getString("bu"));
                                    workOrderPojo.setActive(jsonWO.getInt("active"));
                                    workOrderPojo.setGst_no(jsonWO.getString("gst_no"));
                                    workOrderPojo.setWo_report(jsonWO.getString("wo_report"));
                                    if(modul.equals("workorder")){
                                        workOrderPojo.setWo_activity(1);
                                    }
                                    orderList.add(workOrderPojo);
                                }

                                layoutManager=new GridLayoutManager(currentActivity,1);
                                lv_wo.setLayoutManager(layoutManager);
                                lv_wo.setHasFixedSize(true);
                                tempArrayList.addAll(orderList);
                                adapter=new SmsSendAdapter(tempArrayList, currentActivity);
                                lv_wo.setAdapter(adapter);
                                adapter.setOnItemClickListener(new SmsSendAdapter.ClickListener() {
                                    @Override
                                    public void onItemClick(int position, boolean b) {
                                        tempArrayList.get(position).setSelected(b);

                                        if(b) {
                                            if(allChkFlg==0){
                                                alstMobile.add(orderList.get(position).getMobile());
                                            }
                                        }
                                        else {
                                            if(allChkFlg==1){
                                                itemChkflg=1;
                                                selectAllChk.setChecked(false);
                                            }

                                            alstMobile.remove(orderList.get(position).getMobile());
                                        }
//                                        adapter.notifyItemChanged(position);
                                    }
                                    @Override
                                    public void onLongItemClick(int position, View v) {

                                    }
                                });
                            }
                            else{
                                Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                }
        );

    }

    private void sendSms(){
        if(alstMobile.size()>0){
            JSONArray jsonArray=new JSONArray(alstMobile);
            messageDialog = new MessageDialog(jsonArray.toString());
            messageDialog.show(getSupportFragmentManager(), TAG);
        }
        else{
            Toast.makeText(currentActivity,"Please select customer to send message",Toast.LENGTH_SHORT).show();
        }
    }


    public void showProgressDialog(){
        pDialog = new ProgressDialog(currentActivity);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void sendSmsBtnClick(){
        alstMobile.clear();
        for (WorkOrderPojo w : orderList) {
            w.setSelected(false);
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(currentActivity, WelcomeEmpActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
