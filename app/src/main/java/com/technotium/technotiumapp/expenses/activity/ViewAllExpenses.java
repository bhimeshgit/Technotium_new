package com.technotium.technotiumapp.expenses.activity;

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
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.expenses.adapter.ExpenseAdapter;
import com.technotium.technotiumapp.expenses.model.Expense;
import com.technotium.technotiumapp.payment.activity.AddPaymentActivity;
import com.technotium.technotiumapp.payment.activity.PaymentHistoryActivity;
import com.technotium.technotiumapp.payment.adapter.PaymentAdapter;
import com.technotium.technotiumapp.payment.model.PaymentPojo;
import com.technotium.technotiumapp.workorder.activity.SearchOrderActivity;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewAllExpenses extends AppCompatActivity {

    TextView txtCustomerName,txtTotal,txtTotalPaid;
    WorkOrderPojo workOrderPojo;
    RecyclerView lv_expenseList;
    ViewAllExpenses currentActivity;
    ArrayList<Expense> paymentList;
    ExpenseAdapter adapter;
    Button btnAddNew;
    RecyclerView.LayoutManager layoutManager;
    private Dialog zoomable_image_dialog;
    SubsamplingScaleImageView imageView;
    AlertDialog alertDialog;
    int total_paid=0;
    ProgressDialog pDialog;
    LinearLayoutCompat payAmtLay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_expenses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("All Expenses");
        Intent intent=getIntent();
        if(intent!=null){
            if(intent.getSerializableExtra("orderData")!=null){
                workOrderPojo=(WorkOrderPojo)intent.getSerializableExtra("orderData");
                init();
            }
        }
    }
    public void init(){
        currentActivity= ViewAllExpenses.this;
        paymentList=new ArrayList<>();
        txtCustomerName=findViewById(R.id.txtCustomerName);
        lv_expenseList=findViewById(R.id.lv_expenseList);
        txtCustomerName.setText(workOrderPojo.getFullname());
        txtTotalPaid=findViewById(R.id.txtTotalPaid);
        payAmtLay=findViewById(R.id.payAmtLay);
        if(workOrderPojo.getAdded_by_type().equalsIgnoreCase("dealer") && SessionManager.getMyInstance(currentActivity).getEmpType().equalsIgnoreCase("employee")){
            payAmtLay.setVisibility(View.GONE);
        }
//        getExpenses();
        btnAddNew=findViewById(R.id.btnAddNew);
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(currentActivity, AddExpense.class);
                intent.putExtra("orderData",workOrderPojo);
                startActivity(intent);
                finish();
            }
        });
        pDialog = new ProgressDialog(currentActivity);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        getAllExpense();
    }

    public void getAllExpense(){
        pDialog.show();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("order_id", workOrderPojo.getPkid());
        jsonParserVolley.addParameter("empid",SessionManager.getMyInstance(currentActivity).getEmpid());
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.GET_ALL_EXPENSES ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        Log.d("iss","response="+response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            total_paid=Integer.parseInt(jsonObject.getString("total_paid").equals("null")? "0": jsonObject.getString("total_paid"));
                            txtTotalPaid.setText(total_paid+"");
                            if(success==1){
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonWO=jsonArray.getJSONObject(i);
                                    Expense paymentPojo=new Expense();
                                    paymentPojo.setAmount(Double.parseDouble(jsonWO.getString("amount")));
                                    paymentPojo.setComment(jsonWO.getString("comment"));
                                    paymentPojo.setOrder_id(jsonWO.getString("order_id"));
                                    paymentPojo.setExp_img(jsonWO.getString("exp_img"));
                                    paymentPojo.setExpense_id(jsonWO.getString("pkid"));
                                    paymentPojo.setInserttimestamp(jsonWO.getString("inserttimestamp"));
                                    paymentPojo.setActive(Integer.parseInt(jsonWO.getString("active")));
                                    paymentPojo.setInsertuserid(jsonWO.getString("fullname"));
                                    paymentList.add(paymentPojo);
                                }

                                layoutManager=new GridLayoutManager(currentActivity,1);
                                lv_expenseList.setLayoutManager(layoutManager);
                                lv_expenseList.setHasFixedSize(true);
                                adapter=new ExpenseAdapter(paymentList, currentActivity);
                                lv_expenseList.setAdapter(adapter);
                                adapter.setOnItemClickListener(new ExpenseAdapter.ClickListener() {
                                    @Override
                                    public void onLongItemClick(int position, View v) {
                                        showDeleteAlertDialog(paymentList.get(position).getExpense_id());
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
    public void showDeleteAlertDialog(final String paymentId){
        alertDialog=new AlertDialog.Builder(currentActivity)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this expense entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deactivatePayment(paymentId);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void deactivatePayment(String paymentId){
        pDialog.show();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("exp_id",paymentId);
        jsonParserVolley.executeRequest(Request.Method.POST,WebUrl.DELETE_EXPENSE_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.hide();
                        Log.d("iss",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(currentActivity,ViewAllExpenses.class);
                                intent.putExtra("orderData",workOrderPojo);
                                startActivity(intent);
                                finish();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent(currentActivity, SearchOrderActivity.class);
                intent.putExtra("modul","expense");
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
