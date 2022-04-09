package com.technotium.technotiumapp.workorder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.technotium.technotiumapp.payment.activity.PaymentListActivity;
import com.technotium.technotiumapp.status.OrderStatusPOJO;
import com.technotium.technotiumapp.status.activity.OrderStatusActivity;
import com.technotium.technotiumapp.status.activity.OrderStatusEntryActivity;
import com.technotium.technotiumapp.status.activity.OrderStatusListActivity;
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

public class SearchOrderActivity extends AppCompatActivity {

    RecyclerView lv_wo;
    EditText txtCustomerName;
    TextView txtStartDate,txtEndDate;
    String selectedStartDate, selectedToDate;
    Button addNewBtn;
    ArrayList<WorkOrderPojo> orderList;
    SearchOrderActivity currentActivity;
    GridLayoutManager layoutManager;
    WorkOrderAdapter adapter;
    String modul="";
    ProgressDialog pDialog;
    AlertDialog alertDialog;
    Button btnDelete,btnReport,btnPaymentList;
    ArrayList<WorkOrderPojo> tempArrayList = new ArrayList<WorkOrderPojo>();
    Spinner dealer_sp;
    ArrayList<Dealer> dealerArrayList = new ArrayList<Dealer>();
    ArrayList<String> dealerNameArrayList = new ArrayList<String>();
    SpinnerAdapter dealerSpinnerAdapter;
    ArrayList<WorkOrderPojo> tempArrayList2 = new ArrayList<WorkOrderPojo>();
    private ShimmerFrameLayout mShimmerViewContainer;
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
                switch (modul){
                    case "workorder":
                        setTitle("Work Order");
                        break;
                    case "payment":
                        setTitle("Payment");
                        btnPaymentList.setVisibility(View.VISIBLE);
                        break;
                    case "docscan":
                        setTitle("Documents");
                        break;
                    case "status":
                        setTitle("Status");
                        break;
                    case "material":
                        setTitle("Material");
                        break;
                    case "expense":
                        setTitle("Expense");
                        break;
                    case "after_sale":
                        setTitle("After Sale");
                        break;
                    case "dealer_incentive":
                        setTitle("Dealer Incentive");
                        break;
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
        txtStartDate =findViewById(R.id.txtStartDate);
        txtEndDate =findViewById(R.id.txtEndDate);
        btnPaymentList = findViewById(R.id.btnPaymentList);

        layoutManager=new GridLayoutManager(currentActivity,1);
        lv_wo.setLayoutManager(layoutManager);
        lv_wo.setHasFixedSize(true);
        adapter=new WorkOrderAdapter(tempArrayList,currentActivity);
        lv_wo.setAdapter(adapter);

        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(currentActivity, WorkOrderActivity_New.class));
                //startActivity(new Intent(currentActivity, WorkOrderActivity.class));
                finish();
            }
        });
        getAllWorkOrder(SessionManager.getMyInstance(currentActivity).getEmpid());

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
                       getAllWorkOrder(SessionManager.getMyInstance(currentActivity).getEmpid());
                       return;
                   }

                    for(Dealer d : dealerArrayList){
                       if(d.fullname.equals(selected_name)){
                           getAllWorkOrder(d.id+"");
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

        btnPaymentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(currentActivity, PaymentListActivity.class));
            }
        });
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
                    for (WorkOrderPojo workOrderPojo : orderList) {
                        Date woDate = new SimpleDateFormat("yyyy-MM-dd").parse(workOrderPojo.getOrder_date());

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

//    public static Date convertStringToDateForDisplay(String strDate) {
//        SimpleDateFormat sdf1 = new SimpleDateFormat(Utils.DATE_DISPLAY_FORMAT, Locale.ENGLISH);
//        Date convertDate = new Date();
//        try {
//            convertDate = sdf1.parse(strDate);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return convertDate;
//    }

    private void getAllWorkOrder(String userid){
        showProgressDialog();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("userid",userid);

        jsonParserVolley.executeRequest(Request.Method.POST,WebUrl.GET_ALL_WORK_ORDER_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        try {
//                            Log.d("iss",response);
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
                                    workOrderPojo.setPanel(jsonWO.getString("panel"));
                                    workOrderPojo.setInverter(jsonWO.getString("inverter"));
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
                                    workOrderPojo.setFirm_name(jsonWO.getString("firm_name"));
                                    workOrderPojo.setPhase(jsonWO.getString("phase"));
                                    workOrderPojo.setContact_person_name(jsonWO.getString("contact_person_name"));
                                    if (jsonWO.getString("totWoAmount")!=null && !jsonWO.getString("totWoAmount").equals("")) {
                                        workOrderPojo.setOrderAmountAfterGst(Double.parseDouble(jsonWO.getString("totWoAmount")));
                                    }
                                    if (jsonWO.getString("gstTaxRate")!=null && !jsonWO.getString("gstTaxRate").equals("")) {
                                        workOrderPojo.setGstRate(Double.parseDouble(jsonWO.getString("gstTaxRate")));
                                    }
                                    if(modul.equals("workorder")){
                                        workOrderPojo.setWo_activity(1);
                                    }
                                    orderList.add(workOrderPojo);
                                }

//                                layoutManager=new GridLayoutManager(currentActivity,1);
//                                lv_wo.setLayoutManager(layoutManager);
//                                lv_wo.setHasFixedSize(true);
//                                adapter=new WorkOrderAdapter(orderList, currentActivity);
//                                lv_wo.setAdapter(adapter);
                                tempArrayList.clear();
                                tempArrayList.addAll(orderList);
                                adapter.notifyDataSetChanged();


                                adapter.setOnItemClickListener(new WorkOrderAdapter.ClickListener() {
                                    @Override
                                    public void onItemClick(int position, View v) {
                                        Intent intent=null;
                                        if(modul.equals("workorder")){
                                            intent=new Intent(currentActivity, WorkOrderActivity_New.class);// intent=new Intent(currentActivity, WorkOrderActivity.class);
                                        }
                                        else if(modul.equals("payment")){
                                            intent=new Intent(currentActivity, PaymentHistoryActivity.class);
                                        }
                                        else if(modul.equals("docscan")){
                                            intent=new Intent(currentActivity, ViewAllDocsActivity.class);
                                        }
                                        else if(modul.equals("status")){
//                                            intent=new Intent(currentActivity, OrderStatusActivity.class);
                                            intent=new Intent(currentActivity, OrderStatusListActivity.class);
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
                                        intent.putExtra("orderData",tempArrayList.get(position));
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onLongItemClick(int position, View v) {
                                        showDeleteAlertDialog(Integer.parseInt(tempArrayList.get(position).getPkid()),position);
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
//                        Log.d("iss",response);
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