package com.technotium.technotiumapp.payment.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.GsonBuilder;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.WelcomeEmpActivity;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.payment.adapter.PaymentAdapter;
import com.technotium.technotiumapp.payment.model.PaymentPojo;
import com.technotium.technotiumapp.workorder.activity.SearchOrderActivity;
import com.technotium.technotiumapp.workorder.activity.WorkOrderActivity_New;
import com.technotium.technotiumapp.workorder.adapter.SpinnerAdapter;
import com.technotium.technotiumapp.workorder.adapter.WorkOrderAdapter;
import com.technotium.technotiumapp.workorder.model.Dealer;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PaymentListActivity extends AppCompatActivity {

    RecyclerView lv_wo;
    EditText txtCustomerName;
    TextView txtStartDate,txtEndDate;
    String selectedStartDate, selectedToDate;
    Button addNewBtn;
    PaymentAdapter adapter;
    ArrayList<PaymentPojo> orderList;
    PaymentListActivity currentActivity;
    ArrayList<PaymentPojo> tempArrayList = new ArrayList<PaymentPojo>();
    GridLayoutManager layoutManager;
    Spinner dealer_sp;
    ArrayList<Dealer> dealerArrayList = new ArrayList<Dealer>();
    ArrayList<String> dealerNameArrayList = new ArrayList<String>();
    SpinnerAdapter dealerSpinnerAdapter;
    ArrayList<PaymentPojo> tempArrayList2 = new ArrayList<PaymentPojo>();
    private ShimmerFrameLayout mShimmerViewContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Payment List");
        init();
    }

    private void init(){
        orderList=new ArrayList<>();
        currentActivity=PaymentListActivity.this;
        lv_wo= (RecyclerView)findViewById(R.id.lv_wo);
        txtCustomerName=findViewById(R.id.txtCustomerName);
        addNewBtn=findViewById(R.id.btnAddNew);
        txtStartDate =findViewById(R.id.txtStartDate);
        txtEndDate =findViewById(R.id.txtEndDate);

        layoutManager=new GridLayoutManager(currentActivity,1);
        lv_wo.setLayoutManager(layoutManager);
        lv_wo.setHasFixedSize(true);
        adapter=new PaymentAdapter(tempArrayList,currentActivity);
        lv_wo.setAdapter(adapter);


        getPayment(SessionManager.getMyInstance(currentActivity).getEmpid());

        txtCustomerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = s.length();
                tempArrayList.clear();
                for(PaymentPojo c: orderList){
                    if (textlength <= c.getOrder_name().length()) {

                        if (c.getOrder_name().toLowerCase().contains(s.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
//                adapter=new WorkOrderAdapter(tempArrayList,currentActivity);
//                lv_wo.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addNewBtn.setVisibility(View.GONE);

        dealer_sp = findViewById(R.id.dealer_sp);
        dealerNameArrayList.add("All Dealer");
        dealerSpinnerAdapter = new SpinnerAdapter(currentActivity, dealerNameArrayList);

        dealer_sp.setAdapter(dealerSpinnerAdapter);
        dealer_sp.setSelection(0, false);
        if (SessionManager.getMyInstance(currentActivity).getEmpType().equals("Admin")){
            dealer_sp.setVisibility(View.VISIBLE);

            dealer_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String selected_name= dealerNameArrayList.get(position);
                    if(selected_name.equals("All Dealer")){
                        getPayment(SessionManager.getMyInstance(currentActivity).getEmpid());
                        return;
                    }

                    for(Dealer d : dealerArrayList){
                        if(d.fullname.equals(selected_name)){
                            mShimmerViewContainer.setVisibility(View.VISIBLE);
                            mShimmerViewContainer.startShimmerAnimation();
                            getPayment(d.id+"");
                            break;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
        }

        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFunction(1);
            }
        });
        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFunction(2);
            }
        });

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();
    }

    public void getPayment(String userid){
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("userid", userid);

        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.GET_ALL_PAYMENTS_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        Log.d("iss","response="+response);
                        try {
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                try {
                                    dealerArrayList.clear();
                                    dealerArrayList.addAll(new ArrayList<Dealer>(Arrays.asList(new GsonBuilder().create().fromJson(jsonObject.getString("dealer_data"), Dealer[].class))));
                                    if(dealerArrayList.size() > 0){
                                        dealerNameArrayList.clear();
                                        dealerNameArrayList.add("All Dealer");
                                        for(Dealer d : dealerArrayList){
                                            dealerNameArrayList.add(d.fullname);
                                        }
                                        dealerSpinnerAdapter.notifyDataSetChanged();
                                    }

                                }
                                catch (Exception e){}
                                orderList.clear();
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonWO=jsonArray.getJSONObject(i);
                                    PaymentPojo paymentPojo=new PaymentPojo();
                                    paymentPojo.setAmount(jsonWO.getString("amount"));
                                    paymentPojo.setComment(jsonWO.getString("comment"));
                                    paymentPojo.setOrder_id(jsonWO.getString("order_id"));
                                    paymentPojo.setPay_image(jsonWO.getString("pay_image"));
                                    paymentPojo.setPayment_id(jsonWO.getString("pkid"));
                                    paymentPojo.setPayment_date(jsonWO.getString("inserttimestamp"));
                                    paymentPojo.setPayment_mode(jsonWO.getString("payment_mode"));
                                    paymentPojo.setActive(jsonWO.getString("active"));
                                    paymentPojo.setPay_bank(jsonWO.getString("pay_bank"));
                                    paymentPojo.setFullName(jsonWO.getString("fullname"));
                                    paymentPojo.setOrder_name(jsonWO.getString("order_name"));
                                    //     paymentPojo.setTotal_paid(Integer.parseInt(jsonWO.getString("total_paid")));
                                    //    total_paid=Integer.parseInt(jsonWO.getString("total_paid"));
                                    orderList.add(paymentPojo);
                                }

                                tempArrayList.clear();
                                tempArrayList.addAll(orderList);
                                adapter.notifyDataSetChanged();
                            }
                            else{
                                Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);
                        }
                        SessionManager.getMyInstance(currentActivity).progressHide();
                    }
                }
        );
    }


    public void dateFunction(final int a){
        Calendar calendar= Calendar.getInstance();
        int year =calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int days=calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dg=new DatePickerDialog(currentActivity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int monthofyear=month+1;

                String date=String.format("%02d" , dayOfMonth)+"-"+String.format("%02d" , monthofyear)+"-"+year;
                if(a == 1){
                    txtStartDate.setText(date);
                    selectedStartDate = date;
                    if(selectedStartDate!= null && selectedToDate!=null){
                        showWoDateWise();
                    }
                    txtEndDate.setText("To Date");
                }
                else{  txtEndDate.setText(date);
                    selectedToDate = date;
                    if(selectedStartDate!= null && selectedToDate!=null){
                        showWoDateWise();
                    }
                }

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Date dt = null;
                try {
                    dt = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat your_format = new SimpleDateFormat("yyyy-MM-dd");

            }
        },year,month,days);
        dg.getDatePicker().setMaxDate(new Date().getTime());
        dg.show();

    }

    private void showWoDateWise(){
        try {
            tempArrayList2.clear();

            Date startDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(selectedStartDate);
            Date endDate = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH).parse(selectedToDate);

            if (startDate != null && endDate != null) {

                if (endDate.compareTo(startDate) >= 0) {
                    for (PaymentPojo workOrderPojo : orderList) {
                        Date woDate = new SimpleDateFormat("dd-MM-yyyy").parse(workOrderPojo.getPayment_date());

                        if (woDate.compareTo(startDate) >= 0 && woDate.compareTo(endDate) <= 0) {
                            tempArrayList2.add(workOrderPojo);
                        }
                    }
                } else {
                    Toast.makeText(currentActivity, "Invalid Date Range Selected", Toast.LENGTH_SHORT).show();
                }

            }

            if (tempArrayList2.size() > 0) {
                Log.d("iss","data hoafksd");
                tempArrayList.clear();
                tempArrayList.addAll(tempArrayList2);
                adapter.notifyDataSetChanged();
//                adapter=new WorkOrderAdapter(tempArrayList,currentActivity);
//                lv_wo.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//                Log.d("iss","tempArrayList="+tempArrayList.size());

//                adapter.notifyDataSetChanged();
            }
            else{
                tempArrayList.clear();
                adapter.notifyDataSetChanged();
            }

            selectedStartDate = null;
            selectedToDate = null;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}