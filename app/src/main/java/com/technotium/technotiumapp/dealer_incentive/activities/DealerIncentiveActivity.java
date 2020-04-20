package com.technotium.technotiumapp.dealer_incentive.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.technotium.technotiumapp.R;

import java.util.ArrayList;

import com.technotium.technotiumapp.after_sales.Activities.ScannerActivity;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.dealer_incentive.adapter.DealerPaymentAdapter;
import com.technotium.technotiumapp.dealer_incentive.model.DealerIncentive;
import com.technotium.technotiumapp.dealer_incentive.model.DealerPaymentPojo;
import com.technotium.technotiumapp.payment.activity.PaymentHistoryActivity;
import com.technotium.technotiumapp.workorder.activity.SearchOrderActivity;
import com.technotium.technotiumapp.workorder.adapter.SpinnerAdapter;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DealerIncentiveActivity extends AppCompatActivity {

    EditText txtOrderAmout,txtCompanyRate,txtPanelAmt,txtInverterAmt,txtStructureAmt,txtOtherAmt,txtTotalAmt,txtNetAmount,txtTdsAmount,txtGrossAmount,txtBalanceAmount,txtPaidAmount;
    Spinner spnTdsPercent;
    Button btnAddNew,btnAddPayment;
    double wo_amount,company_rate,panel_amt,inv_amt,structure_amt,other_amt,total_amt,net_amt,tds_amt,gross_amt,balance_amt,paid_amt;
    ArrayList<String> tdsPerArray=new ArrayList<>();
    SpinnerAdapter tds_adapter;
    Context context;
    DealerIncentiveActivity currentActivity;
    WorkOrderPojo workOrderPojo;
    ProgressDialog pDialog;
    DealerIncentive dealerIncentive;
    private static final int ADD_PAYMENT_REQUEST_CODE=101;
    private static final int ADD_PAYMENT_RESULT_CODE=102;
    RecyclerView lv_payment;
    ArrayList<DealerPaymentPojo> paymentList;
    DealerPaymentAdapter adapter;
    GridLayoutManager layoutManager;
    private Dialog zoomable_image_dialog;
    private SubsamplingScaleImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_incentive);
        Intent intent=getIntent();
        if(intent!=null){
            if(intent.getSerializableExtra("orderData")!=null){
                workOrderPojo=(WorkOrderPojo)intent.getSerializableExtra("orderData");
                generateId();
                createObj();
                onClick();
                getDealerIncentive();
            }
        }
    }

    private void createObj() {
        context= DealerIncentiveActivity.this;
        currentActivity=DealerIncentiveActivity.this;
        tdsPerArray.add(0,"5");
        for(int i=1;i<=10;i++){
            if(i!=5){
                tdsPerArray.add(i+"");
            }
        }
        pDialog = new ProgressDialog(currentActivity);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        tds_adapter = new SpinnerAdapter(currentActivity, tdsPerArray);
        spnTdsPercent.setAdapter(tds_adapter);

        txtOrderAmout.setText(workOrderPojo.getAmount());
        wo_amount=Double.parseDouble(workOrderPojo.getAmount());
        txtOrderAmout.setEnabled(false);
        paymentList=new ArrayList<>();

    }

    private void onClick(){
        txtCompanyRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtCompanyRate.getText().toString().trim().length()>0){
                    company_rate=Double.parseDouble(txtCompanyRate.getText().toString());
                    calculate();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtPanelAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtPanelAmt.getText().toString().trim().length()>0) {
                    panel_amt = Double.parseDouble(txtPanelAmt.getText().toString());
                    calculate();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txtInverterAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtInverterAmt.getText().toString().trim().length()>0) {
                    inv_amt = Double.parseDouble(s.toString());
                    calculate();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txtStructureAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtStructureAmt.getText().toString().trim().length()>0) {
                    structure_amt = Double.parseDouble(s.toString());
                    calculate();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txtOtherAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtOtherAmt.getText().toString().trim().length()>0) {
                    other_amt = Double.parseDouble(s.toString());
                    calculate();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnAddNew.getText().toString().equals("Update"))
                    addUpdateDealerIncentive("update");
                else
                    addUpdateDealerIncentive("insert");
            }
        });
        spnTdsPercent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculate();
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        btnAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dealerIncentive!=null) {
                    Intent intent = new Intent(currentActivity, DealerPaymentActivity.class);
                    intent.putExtra("dealer_incentive_id", dealerIncentive.getPkid() + "");
                    startActivityForResult(intent, ADD_PAYMENT_REQUEST_CODE);
                }
            }
        });
    }
    private void calculate(){
      total_amt=company_rate+panel_amt+inv_amt+structure_amt+other_amt;
      txtTotalAmt.setText(total_amt+"");
      net_amt=wo_amount-total_amt;
      txtNetAmount.setText(net_amt+"");
      tds_amt= net_amt*(Double.parseDouble(spnTdsPercent.getSelectedItem().toString())/100);
      txtTdsAmount.setText(tds_amt+"");
      gross_amt=net_amt-tds_amt;
      txtGrossAmount.setText(gross_amt+"");
      balance_amt=gross_amt;
      txtBalanceAmount.setText(balance_amt+"");
      paid_amt=gross_amt-balance_amt;
      txtPaidAmount.setText(paid_amt+"");
    }

    private void addUpdateDealerIncentive(String op){
        pDialog.show();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("op",op);
        jsonParserVolley.addParameter("order_id",workOrderPojo.getPkid());
        jsonParserVolley.addParameter("company_rate",txtCompanyRate.getText().toString());
        jsonParserVolley.addParameter("panel_ex_amt",txtPanelAmt.getText().toString());
        jsonParserVolley.addParameter("inverter_ex_amt",txtInverterAmt.getText().toString());
        jsonParserVolley.addParameter("structure_ex_amt", txtStructureAmt.getText().toString());
        jsonParserVolley.addParameter("other_ex_amt", txtOtherAmt.getText().toString());
        jsonParserVolley.addParameter("total_amt", txtTotalAmt.getText().toString());
        jsonParserVolley.addParameter("dealer_net_amt", txtNetAmount.getText().toString());
        jsonParserVolley.addParameter("tds_percent", spnTdsPercent.getSelectedItem().toString());
        jsonParserVolley.addParameter("tds_amt", txtTdsAmount.getText().toString());
        jsonParserVolley.addParameter("dealer_gross_amt", txtGrossAmount.getText().toString());
        jsonParserVolley.addParameter("balance_amt", txtBalanceAmount.getText().toString());
        jsonParserVolley.addParameter("paid_amt", txtPaidAmount.getText().toString());
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.ADD_UPDATE_DEALER_INCENTIVE ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        Log.d("iss",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                btnAddNew.setText("Update");
                                getDealerIncentive();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(ADD_PAYMENT_RESULT_CODE==resultCode){
            getDealerIncentive();
        }
    }

    private void getDealerIncentive(){
        pDialog.show();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("order_id",workOrderPojo.getPkid());
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.GET_DEALER_INCENTIVE ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        Log.d("iss",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                btnAddNew.setText("Update");
                                JSONArray jsonArray=jsonObject.getJSONArray("incentive_data");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonWO=jsonArray.getJSONObject(i);
                                    DealerIncentive dealerIncentive=new DealerIncentive();
                                    dealerIncentive.setPkid(jsonWO.getString("pkid"));
                                    dealerIncentive.setCompany_rate(jsonWO.getString("company_rate"));
                                    dealerIncentive.setPanel_ex_amt(jsonWO.getString("panel_ex_amt"));
                                    dealerIncentive.setInverter_ex_amt(jsonWO.getString("inverter_ex_amt"));
                                    dealerIncentive.setStructure_ex_amt(jsonWO.getString("structure_ex_amt"));
                                    dealerIncentive.setOther_ex_amt(jsonWO.getString("other_ex_amt"));
                                    dealerIncentive.setTotal_amt(jsonWO.getString("total_amt"));
                                    dealerIncentive.setDealer_net_amt(jsonWO.getString("dealer_net_amt"));
                                    dealerIncentive.setTds_percent(jsonWO.getString("tds_percent"));
                                    dealerIncentive.setTds_amt(jsonWO.getString("tds_amt"));
                                    dealerIncentive.setDealer_gross_amt(jsonWO.getString("dealer_gross_amt"));
                                    dealerIncentive.setBalance_amt(jsonWO.getString("balance_amt"));
                                    dealerIncentive.setPaid_amt(jsonWO.getString("paid_amt"));
                                    dealerIncentive.setOrder_id(jsonWO.getString("order_id"));
                                    dealerIncentive.setInserttimestamp(jsonWO.getString("inserttimestamp"));
                                    setData(dealerIncentive);
                                }
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
    private void generateId() {
        txtOrderAmout=findViewById(R.id.txtOrderAmout);
        txtCompanyRate=findViewById(R.id.txtCompanyRate);
        txtPanelAmt=findViewById(R.id.txtPanelAmt);
        txtInverterAmt=findViewById(R.id.txtInverterAmt);
        txtStructureAmt=findViewById(R.id.txtStructureAmt);
        txtOtherAmt=findViewById(R.id.txtOtherAmt);
        txtTotalAmt=findViewById(R.id.txtTotalAmt);
        txtNetAmount=findViewById(R.id.txtNetAmount);
        txtTdsAmount=findViewById(R.id.txtTdsAmount);
        txtGrossAmount=findViewById(R.id.txtGrossAmount);
        txtBalanceAmount=findViewById(R.id.txtBalanceAmount);
        spnTdsPercent=findViewById(R.id.spnTdsPercent);
        btnAddNew=findViewById(R.id.btnAddNew);
        txtPaidAmount=findViewById(R.id.txtPaidAmount);
        btnAddPayment=findViewById(R.id.btnAddPayment);

        if(SessionManager.getMyInstance(currentActivity).getEmpType().equals("Dealer")) {
            txtCompanyRate.setEnabled(false);
            txtPanelAmt.setEnabled(false);
            txtInverterAmt.setEnabled(false);
            txtStructureAmt.setEnabled(false);
            txtOtherAmt.setEnabled(false);
            btnAddNew.setVisibility(View.GONE);
            btnAddPayment.setVisibility(View.GONE);
            spnTdsPercent.setEnabled(false);
        }
    }

    private void setData(DealerIncentive dealerIncentive){
        this.dealerIncentive=dealerIncentive;
        txtCompanyRate.setText(dealerIncentive.getCompany_rate());
        txtPanelAmt.setText(dealerIncentive.getPaid_amt());
        txtInverterAmt.setText(dealerIncentive.getInverter_ex_amt());
        txtStructureAmt.setText(dealerIncentive.getStructure_ex_amt());
        txtOtherAmt.setText(dealerIncentive.getOther_ex_amt());
        txtTotalAmt.setText(dealerIncentive.getTotal_amt());
        txtNetAmount.setText(dealerIncentive.getDealer_net_amt());
        txtTdsAmount.setText(dealerIncentive.getTds_amt());
        txtGrossAmount.setText(dealerIncentive.getDealer_gross_amt());
        txtBalanceAmount.setText(dealerIncentive.getBalance_amt());
        txtPaidAmount.setText(dealerIncentive.getPaid_amt());
        int idx=0;
        for(String tds : tdsPerArray){
            if(tds.equals(dealerIncentive.getTds_percent())){
                break;
            }
            idx++;
        }
        spnTdsPercent.setSelection(idx);
        lv_payment=findViewById(R.id.lv_payment);
        getPayment();
    }

    public void getPayment(){
        pDialog.show();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("dealer_incentive_id",dealerIncentive.getPkid());

        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.GET_DEALER_PAYMENT_DETAIL_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        Log.d("iss","response="+response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");

                            if(success==1){
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonWO=jsonArray.getJSONObject(i);
                                    DealerPaymentPojo paymentPojo=new DealerPaymentPojo();
                                    paymentPojo.setAmount(jsonWO.getString("amount"));
                                    paymentPojo.setComment(jsonWO.getString("comment"));
                                    paymentPojo.setDealer_incentive_id(jsonWO.getString("dealer_incentive_id"));
                                    paymentPojo.setPay_image(jsonWO.getString("pay_image"));
                                    paymentPojo.setPayment_id(jsonWO.getString("pkid"));
                                    paymentPojo.setPayment_date(jsonWO.getString("inserttimestamp"));
                                    paymentPojo.setPayment_mode(jsonWO.getString("payment_mode"));
                                    paymentPojo.setActive(jsonWO.getString("active"));
                                    paymentList.add(paymentPojo);
                                }

                                layoutManager=new GridLayoutManager(currentActivity,1);
                                lv_payment.setLayoutManager(layoutManager);
                                lv_payment.setHasFixedSize(true);
                                adapter=new DealerPaymentAdapter(paymentList, currentActivity);
                                lv_payment.setAdapter(adapter);
                                adapter.setOnItemClickListener(new DealerPaymentAdapter.ClickListener() {
                                    @Override
                                    public void onItemClick(int position, View v) {

                                    }

                                    @Override
                                    public void onLongItemClick(int position, View v) {
                                      //  showDeleteAlertDialog(paymentList.get(position).getPayment_id());
                                    }
                                });
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
        final AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Exit");
        alertDialogBuilder.setMessage("Do you want to Exit Incentive calculation");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(currentActivity, SearchOrderActivity.class);
                intent.putExtra("modul","dealer_incentive");
                startActivity(intent);
                finish();
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

}
