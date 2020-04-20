package com.technotium.technotiumapp.workorder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.WelcomeEmpActivity;
import com.technotium.technotiumapp.after_sales.Activities.AfterSalesActivity;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.dealer_incentive.activities.DealerIncentiveActivity;
import com.technotium.technotiumapp.docscan.activity.ViewAllDocsActivity;
import com.technotium.technotiumapp.expenses.activity.AddExpense;
import com.technotium.technotiumapp.expenses.activity.ViewAllExpenses;
import com.technotium.technotiumapp.material.activity.AddMaterialActivity;
import com.technotium.technotiumapp.payment.activity.PaymentHistoryActivity;
import com.technotium.technotiumapp.status.activity.OrderStatusActivity;
import com.technotium.technotiumapp.workorder.adapter.WorkOrderAdapter;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchOrderActivity extends AppCompatActivity {

    RecyclerView lv_wo;
    EditText txtCustomerName;
    Button addNewBtn;
    ArrayList<WorkOrderPojo> orderList;
    SearchOrderActivity currentActivity;
    GridLayoutManager layoutManager;
    WorkOrderAdapter adapter;
    String modul="";
    ProgressDialog pDialog;
    AlertDialog alertDialog;
    Button btnDelete,btnReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        Intent intent=getIntent();
        addNewBtn.setVisibility(View.GONE);
        if(intent!=null){
            if(intent.getStringExtra("modul")!=null){
                modul=intent.getStringExtra("modul");
                if(modul.equals("workorder")){
                    addNewBtn.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private void init(){
        orderList=new ArrayList<>();
        currentActivity=SearchOrderActivity.this;
        lv_wo= (RecyclerView)findViewById(R.id.lv_wo);
        txtCustomerName=findViewById(R.id.txtCustomerName);
        addNewBtn=findViewById(R.id.btnAddNew);

        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(currentActivity, WorkOrderActivity.class));
                finish();
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
                ArrayList<WorkOrderPojo> tempArrayList = new ArrayList<WorkOrderPojo>();
                for(WorkOrderPojo c: orderList){
                    if (textlength <= c.getFullname().length()) {
                        if (c.getFullname().toLowerCase().contains(s.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                adapter=new WorkOrderAdapter(tempArrayList,currentActivity);
                lv_wo.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addNewBtn.setVisibility(View.GONE);
    }

    private void getAllWorkOrder(){
        showProgressDialog();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("userid",SessionManager.getMyInstance(currentActivity).getEmpid());

        jsonParserVolley.executeRequest(Request.Method.POST,WebUrl.GET_ALL_WORK_ORDER_URL ,new JsonParserVolley.VolleyCallback() {
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
                                adapter=new WorkOrderAdapter(orderList, currentActivity);
                                lv_wo.setAdapter(adapter);

                                adapter.setOnItemClickListener(new WorkOrderAdapter.ClickListener() {
                                    @Override
                                    public void onItemClick(int position, View v) {
                                        Intent intent=null;
                                        if(modul.equals("workorder")){
                                            intent=new Intent(currentActivity, WorkOrderActivity.class);
                                        }
                                        else if(modul.equals("payment")){
                                            intent=new Intent(currentActivity, PaymentHistoryActivity.class);
                                        }
                                        else if(modul.equals("docscan")){
                                            intent=new Intent(currentActivity, ViewAllDocsActivity.class);
                                        }
                                        else if(modul.equals("status")){
                                            intent=new Intent(currentActivity, OrderStatusActivity.class);
                                        }
                                        else if(modul.equals("material")){
                                            intent=new Intent(currentActivity, AddMaterialActivity.class);
                                        }
                                        else if(modul.equals("expense")){
                                            intent=new Intent(currentActivity, ViewAllExpenses.class);
                                        }
                                        else if(modul.equals("after_sale")){
                                            intent=new Intent(currentActivity, AfterSalesActivity.class);
                                        }
                                        else if(modul.equals("dealer_incentive")){
                                            intent=new Intent(currentActivity, DealerIncentiveActivity.class);
                                        }
                                        intent.putExtra("orderData",orderList.get(position));
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onLongItemClick(int position, View v) {
                                        showDeleteAlertDialog(Integer.parseInt(orderList.get(position).getPkid()),position);
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

    public void showDeleteAlertDialog(final int orderid,final int index){
        alertDialog=new AlertDialog.Builder(currentActivity)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this work order?")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deactivateOrder(orderid,index);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void deactivateOrder(int orderid,final int index){
        showProgressDialog();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("orderid",orderid+"");
        jsonParserVolley.executeRequest(Request.Method.POST,WebUrl.DELETE_WORK_ORDER ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        Log.d("iss",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                orderList.get(index).setActive(2);
                                adapter.notifyDataSetChanged();
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


    public void showProgressDialog(){
        pDialog = new ProgressDialog(currentActivity);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
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