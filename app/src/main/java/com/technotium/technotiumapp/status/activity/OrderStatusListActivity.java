package com.technotium.technotiumapp.status.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.payment.activity.AddPaymentActivity;
import com.technotium.technotiumapp.payment.activity.PaymentHistoryActivity;
import com.technotium.technotiumapp.payment.adapter.PaymentAdapter;
import com.technotium.technotiumapp.payment.model.PaymentPojo;
import com.technotium.technotiumapp.status.OrderStatusPOJO;
import com.technotium.technotiumapp.status.adapter.OrderStatusListAdapter;
import com.technotium.technotiumapp.workorder.activity.SearchOrderActivity;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class OrderStatusListActivity extends AppCompatActivity {
    TextView txtCustomerName;
    WorkOrderPojo workOrderPojo;
    RecyclerView lv_statusList;
    OrderStatusListActivity currentActivity;
    ArrayList<OrderStatusPOJO> statusList;
    OrderStatusListAdapter adapter;
    Button btnAddNew;
    RecyclerView.LayoutManager layoutManager;
    private Dialog zoomable_image_dialog;
    SubsamplingScaleImageView imageView;
    AlertDialog alertDialog;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Status Detail");
        Intent intent=getIntent();
        if(intent!=null){
            if(intent.getSerializableExtra("orderData")!=null){
                workOrderPojo=(WorkOrderPojo)intent.getSerializableExtra("orderData");
                init();
            }
        }
    }

    public void init(){
        currentActivity= OrderStatusListActivity.this;
        statusList=new ArrayList<>();
        txtCustomerName=findViewById(R.id.txtCustomerName);
        lv_statusList=findViewById(R.id.lv_statusList);
        txtCustomerName.setText(workOrderPojo.getFullname());


        getStatusList();
        btnAddNew=findViewById(R.id.btnAddNew);
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(currentActivity, OrderStatusEntryActivity.class);
                intent.putExtra("statusList", statusList);
                intent.putExtra("orderData",workOrderPojo);
                startActivity(intent);
                finish();
            }
        });
        pDialog = new ProgressDialog(currentActivity);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
    }

    public void getStatusList(){
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("order_id", workOrderPojo.getPkid());

        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.GET_WORK_ORDER_STATUS_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        Log.d("iss","response="+response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
//                            new Gson().fromJson(jsonObject.getString("data"),);

                            if(success==1){
                                statusList.clear();
                                statusList.addAll(new ArrayList<OrderStatusPOJO>(Arrays.asList(new GsonBuilder().create().fromJson(jsonObject.getString("data"), OrderStatusPOJO[].class))));

//                                JSONArray jsonArray=jsonObject.getJSONArray("data");
//
//                                for(int i=0;i<jsonArray.length();i++){
//                                    JSONObject jsonWO=jsonArray.getJSONObject(i);
//                                    PaymentPojo paymentPojo=new PaymentPojo();
//                                    paymentPojo.setAmount(jsonWO.getString("amount"));
//                                    paymentPojo.setComment(jsonWO.getString("comment"));
//                                    paymentPojo.setOrder_id(jsonWO.getString("order_id"));
//                                    paymentPojo.setPay_image(jsonWO.getString("pay_image"));
//                                    paymentPojo.setPayment_id(jsonWO.getString("pkid"));
//                                    paymentPojo.setPayment_date(jsonWO.getString("inserttimestamp"));
//                                    paymentPojo.setPayment_mode(jsonWO.getString("payment_mode"));
//                                    paymentPojo.setActive(jsonWO.getString("active"));
//                                    paymentPojo.setPay_bank(jsonWO.getString("pay_bank"));
//                                    //     paymentPojo.setTotal_paid(Integer.parseInt(jsonWO.getString("total_paid")));
//                                    //    total_paid=Integer.parseInt(jsonWO.getString("total_paid"));
//                                    paymentList.add(paymentPojo);
//                                }
//
                                layoutManager=new GridLayoutManager(currentActivity,1);
                                lv_statusList.setLayoutManager(layoutManager);
                                lv_statusList.setHasFixedSize(true);
                                adapter=new OrderStatusListAdapter(statusList, currentActivity);
                                lv_statusList.setAdapter(adapter);
                                adapter.setOnItemClickListener(new OrderStatusListAdapter.ClickListener() {
                                    @Override
                                    public void onItemClick(int position, View v) {

                                    }

                                    @Override
                                    public void onLongItemClick(int position, View v) {
                                        Log.d("iss","statusList.get(position).getPkid()="+statusList.get(position).getPkid()+" "+statusList.get(position).getStatus_type());
                                        showDeleteAlertDialog(statusList.get(position).getPkid(),position);
                                    }
                                });
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
    public void showZoomImageDialog(String image_url){
        zoomable_image_dialog=new Dialog(currentActivity, R.style.AlertDialogTheme);
        zoomable_image_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        zoomable_image_dialog.setContentView(R.layout.zoomable_image_dialog);
        zoomable_image_dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        zoomable_image_dialog.setCancelable(true);
        imageView = (SubsamplingScaleImageView) zoomable_image_dialog.findViewById(R.id.imageView);
        Glide.with(currentActivity).asBitmap().load(image_url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                imageView.setImage(ImageSource.bitmap(bitmap)); //For SubsampleImage
            }
        });
        zoomable_image_dialog.show();
    }

    public void showDeleteAlertDialog(final String paymentId,final int position){
        alertDialog=new AlertDialog.Builder(currentActivity)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this status?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deactivatePayment(paymentId,position);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void deactivatePayment(String paymentId, final int position){

        pDialog.show();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("pkid",paymentId);
        jsonParserVolley.executeRequest(Request.Method.POST,WebUrl.DEL_WORK_ORDER_STATUS_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        Log.d("iss",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                statusList.remove(position);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(currentActivity, SearchOrderActivity.class);
        intent.putExtra("modul","status");
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent(currentActivity, SearchOrderActivity.class);
                intent.putExtra("modul","status");
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}