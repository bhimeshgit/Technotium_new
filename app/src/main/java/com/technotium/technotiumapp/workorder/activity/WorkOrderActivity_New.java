package com.technotium.technotiumapp.workorder.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.workorder.adapter.SpinnerAdapter;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WorkOrderActivity_New extends AppCompatActivity {
    WorkOrderPojo workOrderPojo=new WorkOrderPojo();
    EditText txtfname,txtlname,txtmobile,txtaddr,txtcity,txtemail,txtdesignation,txtmname,txtconsumer_no,txtbu;
    Spinner spnState,spnProjectType;
    SpinnerAdapter projectType_adapter, state_adapter;
    ArrayList<String> stateArray=new ArrayList<>();
    ArrayList<String> projectTypeArray=new ArrayList<>();

    EditText txtcapacity,txtsysdetails,txtpanel,txtinverter,txtamount,txtOrderDate,txtmobile_con_person,txtgst_no;
    Spinner spnPhase,spnStructure,spnGridType;
    ArrayList<String> gridArray=new ArrayList<>();
    ArrayList<String> structureArray=new ArrayList<>();
    ArrayList<String> phaseArray=new ArrayList<>();
    String OrderDate;
    TextView txtamounttxt;
    String orderToset;

    CheckBox load_extension,changeofname,solarsanction,meterinstall,medasaction,subsidy_approval,commissioning,agreement;
    EditText txtpanelcapacity,txtinvertercapacity,txtRate,txtContactPerson,txtFirmName;
    Button btnSave,btnUpdate;
    ProgressDialog pDialog ;
    TextView txtRatetxt;

    WorkOrderActivity_New currentActivity;
    LinearLayoutCompat finance_head_lay;
    ImageView contact_pick_img,contact2_pick_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order__new);
        generateId();
        createObj();
        onClick();
        if(getIntent().getSerializableExtra("orderData") != null) {
            workOrderPojo =(WorkOrderPojo) getIntent().getSerializableExtra("orderData");
            btnSave.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
            setDataForUpdate();
        }
        else{
            btnSave.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.GONE);
        }
    }

    private void setDataForUpdate() {
        txtfname.setText(workOrderPojo.getFname());
        txtlname.setText(workOrderPojo.getLname());
        txtmobile.setText(workOrderPojo.getMobile());
        txtaddr.setText(workOrderPojo.getAddress());
        txtcity.setText(workOrderPojo.getCity());
        txtemail.setText(workOrderPojo.getEmail());
        txtdesignation.setText(workOrderPojo.getDesignation());
        txtmname.setText(workOrderPojo.getMname());
        txtconsumer_no.setText(workOrderPojo.getConsumer_no());
        txtbu.setText(workOrderPojo.getBu());
        if(!workOrderPojo.getState().equals("")){
            int position=0;
            for (int i = 0; i < stateArray.size(); i++) {
                if (stateArray.get(i).equalsIgnoreCase(workOrderPojo.getState())) {
                    position = i;
                }
            }
            spnState.setSelection(position);
        }
        if(!workOrderPojo.getProjectType().equals("")){
            int position=0;
            for (int i = 0; i < projectTypeArray.size(); i++) {
                if (projectTypeArray.get(i).equalsIgnoreCase(workOrderPojo.getProjectType())) {
                    position = i;
                }
            }
            spnProjectType.setSelection(position);
        }
        txtcapacity.setText(workOrderPojo.getCapacity());
        txtsysdetails.setText(workOrderPojo.getSystemdetail());
        txtpanel.setText(workOrderPojo.getPanel());
        txtinverter.setText(workOrderPojo.getInverter());
        txtamount.setText(workOrderPojo.getAmount());
        orderToset=workOrderPojo.getOrder_date();
        txtgst_no.setText(workOrderPojo.getGst_no());
        try {
            Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(orderToset);
            txtOrderDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(dt));
        } catch (ParseException e) {
            e.printStackTrace();
            txtOrderDate.setText(workOrderPojo.getOrder_date() );
        }

        txtmobile_con_person.setText(workOrderPojo.getContactPerson());

        if (!workOrderPojo.getPhase().equals("")) {
            int position = 0;
            for (int i = 0; i < phaseArray.size(); i++) {
                if (phaseArray.get(i).equalsIgnoreCase(workOrderPojo.getPhase())) {
                    position = i;
                }
            }
            spnPhase.setSelection(position);
        }
        if (!workOrderPojo.getStructure().equals("")) {
            int position = 0;
            for (int i = 0; i < structureArray.size(); i++) {
                if (structureArray.get(i).equalsIgnoreCase(workOrderPojo.getStructure())) {
                    position = i;
                }
            }
            spnStructure.setSelection(position);
        }
        if (!workOrderPojo.getGridType().equals("")) {
            int position = 0;
            for (int i = 0; i < gridArray.size(); i++) {
                if (gridArray.get(i).equalsIgnoreCase(workOrderPojo.getGridType())) {
                    position = i;
                }
            }
            spnGridType.setSelection(position);
        }

        load_extension.setChecked(workOrderPojo.getLoadextension().equals("1") ? true : false);
        changeofname.setChecked(workOrderPojo.getChangeofname().equals("1") ? true : false);
        solarsanction.setChecked(workOrderPojo.getSolarsanction().equals("1") ? true : false);
        meterinstall.setChecked(workOrderPojo.getMeterinstallation().equals("1") ? true : false);
        medasaction.setChecked(workOrderPojo.getMedasaction().equals("1") ? true : false);
        subsidy_approval.setChecked(workOrderPojo.getSubsidy_approval().equals("1") ? true : false);
        commissioning.setChecked(workOrderPojo.getCommissioning().equals("1") ? true : false);
        agreement.setChecked(workOrderPojo.getAgreement().equals("1") ? true : false);
        txtpanelcapacity.setText(workOrderPojo.getPanelcapacity());
        txtinvertercapacity.setText(workOrderPojo.getInvertercapicity());
        txtRate.setText(workOrderPojo.getRateFromCompany());
        if(SessionManager.getMyInstance(currentActivity).getEmpType().equals("Employee") || SessionManager.getMyInstance(currentActivity).getEmpType().equals("Dealer")){
            txtRate.setEnabled(false);
        }
        txtContactPerson.setText(workOrderPojo.getContact_person_name());
        txtFirmName.setText(workOrderPojo.getFirm_name());
    }

    private void onClick() {
        contact_pick_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent,101);
            }
        });
        contact2_pick_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent,102);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workOrderPojo=getData();
//                SessionManager.getMyInstance(getActivity()).progressShow();
                pDialog.setMessage("Please Wait...");
                pDialog.setCancelable(false);
                pDialog.show();
                final JsonParserVolley jsonParserVolley = setAllparameter();

                jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.ADD_WORK_ORDER_URL ,new JsonParserVolley.VolleyCallback() {
                            @Override
                            public void getResponse(String response) {
                                // SessionManager.getMyInstance(getActivity()).progressHide();
                                pDialog.dismiss();
                                try {
                                    Log.d("iss","response="+response);
                                    JSONObject jsonObject=new JSONObject(response);
                                    int success=jsonObject.getInt("success");
                                    if(success==1){
                                        Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(currentActivity, SearchOrderActivity.class);
                                        intent.putExtra("modul","workorder");
                                        startActivity(intent);
                                        currentActivity.finish();
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
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workOrderPojo=getData();
                if( validate()==0){
                    return;
                }
                pDialog.setMessage("Please Wait...");
                pDialog.setCancelable(false);
                pDialog.show();
                final JsonParserVolley jsonParserVolley = setAllparameter();
                jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.UPDATE_WORK_ORDER_URL ,new JsonParserVolley.VolleyCallback() {
                            @Override
                            public void getResponse(String response) {
                                // SessionManager.getMyInstance(getActivity()).progressHide();
                                pDialog.dismiss();
                                try {
                                    Log.d("iss","response="+response);
                                    JSONObject jsonObject=new JSONObject(response);
                                    int success=jsonObject.getInt("success");
                                    if(success==1){
                                        Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(currentActivity, SearchOrderActivity.class);
                                        intent.putExtra("modul","workorder");
                                        startActivity(intent);
                                        currentActivity.finish();
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
        });
    }

    private void createObj() {
        currentActivity=WorkOrderActivity_New.this;
        state_adapter = new SpinnerAdapter(currentActivity, stateArray);
        spnState.setAdapter(state_adapter);
        projectType_adapter = new SpinnerAdapter(currentActivity, projectTypeArray);
        spnProjectType.setAdapter(projectType_adapter);
        stateArray.add("Maharashtra");
        stateArray.add("Madhya Pradesh");
        stateArray.add("Gujrat");
        stateArray.add("Andhra Pradesh");
        stateArray.add("Arunachal Pradesh");
        stateArray.add("Assam");
        stateArray.add("Bihar");
        stateArray.add("Chhattisgarh");
        stateArray.add("Delhi");
        stateArray.add("Goa");
        stateArray.add("Haryana");
        stateArray.add("Himachal Pradesh");
        stateArray.add("Jammu and Kashmir");
        stateArray.add("Jharkhand");
        stateArray.add("Karnataka");
        stateArray.add("Kerala");
        stateArray.add("Manipur");
        stateArray.add("Meghalaya");
        stateArray.add("Mizoram");
        stateArray.add("Nagaland");
        stateArray.add("Orissa");
        stateArray.add("Punjab");
        stateArray.add("Rajasthan");
        stateArray.add("Sikkim");
        stateArray.add("Tamil Nadu");
        stateArray.add("Telangana");
        stateArray.add("Tripura");
        stateArray.add("Uttar Pradesh");
        stateArray.add("Uttarakhand");
        stateArray.add("West Bengal");

        projectTypeArray.add("--Select--");
        projectTypeArray.add("Residential");
        projectTypeArray.add("Commercial");
        projectTypeArray.add("Industrial");
        projectTypeArray.add("Public Service");

        structureArray.add("--Select--");
        structureArray.add("ELEVATED");
        structureArray.add("ROOF TOP");
        structureArray.add("GROUND MOUNTED");
        structureArray.add("PLATFORM");
        phaseArray.add("--Select--");
        phaseArray.add("1");
        phaseArray.add("3");
        gridArray.add("--Select--");
        gridArray.add("On Grid");
        gridArray.add("Off Grid");

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal=Calendar.getInstance();
        Date dt=cal.getTime();
        orderToset=sdf.format(dt);
        txtOrderDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(dt));
        txtOrderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFunction();
            }
        });
        SpinnerAdapter adapter = new SpinnerAdapter(currentActivity, structureArray);
        spnStructure.setAdapter(adapter);
        adapter = new SpinnerAdapter(currentActivity, phaseArray);
        spnPhase.setAdapter(adapter);

        adapter = new SpinnerAdapter(currentActivity, gridArray);
        spnGridType.setAdapter(adapter);
        if(SessionManager.getMyInstance(currentActivity).getEmpType().equalsIgnoreCase("Electrician")){
            txtamount.setVisibility(View.GONE);
            txtamounttxt.setVisibility(View.GONE);
            finance_head_lay.setVisibility(View.GONE);
        }
        pDialog = new ProgressDialog(currentActivity);

        state_adapter = new SpinnerAdapter(currentActivity, stateArray);
        spnState.setAdapter(state_adapter);
        projectType_adapter = new SpinnerAdapter(currentActivity, projectTypeArray);
        spnProjectType.setAdapter(projectType_adapter);

    }

    private void generateId() {
        txtfname=findViewById(R.id.txtfname);
        txtlname=findViewById(R.id.txtlname);
        txtmobile=findViewById(R.id.txtmobile);
        txtaddr=findViewById(R.id.txtaddr);
        txtcity=findViewById(R.id.txtcity);
        spnState=findViewById(R.id.spnState);
        txtemail=findViewById(R.id.txtemail);
        txtmname=findViewById(R.id.txtmname);
        txtconsumer_no=findViewById(R.id.txtconsumer_no);
        txtbu=findViewById(R.id.txtbu);
        txtdesignation=findViewById(R.id.txtdesignation);
        spnProjectType=findViewById(R.id.spnProjectType);

        txtcapacity=findViewById(R.id.txtcapacity);
        txtsysdetails=findViewById(R.id.txtsysdetails);
        txtpanel=findViewById(R.id.txtpanel);
        txtinverter=findViewById(R.id.txtinverter);
        spnPhase=findViewById(R.id.spnPhase);
        spnStructure=findViewById(R.id.spnStructure);
        txtamount=findViewById(R.id.txtamount);
        txtamounttxt=findViewById(R.id.txtamounttxt);
        txtOrderDate=findViewById(R.id.txtOrderDate);
        txtgst_no=findViewById(R.id.txtgst_no);
        txtmobile_con_person=findViewById(R.id.txtmobile_con_person);
        spnGridType=findViewById(R.id.spnGridType);

        load_extension=findViewById(R.id.load_extension);
        changeofname=findViewById(R.id.changeofname);
        solarsanction=findViewById(R.id.solarsanction);
        meterinstall=findViewById(R.id.meterinstall);
        medasaction=findViewById(R.id.medasaction);
        txtRatetxt=findViewById(R.id.txtRatetxt);
        subsidy_approval=findViewById(R.id.subsidy_approval);
        commissioning=findViewById(R.id.commissioning);
        agreement=findViewById(R.id.agreement);
        txtpanelcapacity=findViewById(R.id.txtpanelcapacity);
        txtinvertercapacity=findViewById(R.id.txtinvertercapacity);
        txtRate=findViewById(R.id.txtRate);
        txtbu=findViewById(R.id.txtbu);

        btnSave=findViewById(R.id.btnSave);
        btnUpdate=findViewById(R.id.btnUpdate);
        if(SessionManager.getMyInstance(currentActivity).getEmpType().equalsIgnoreCase("Electrician")){
            txtRate.setVisibility(View.GONE);
            txtRatetxt.setVisibility(View.GONE);
        }
        finance_head_lay=findViewById(R.id.finance_head_lay);

        txtContactPerson=findViewById(R.id.txtContactPerson);
        txtFirmName=findViewById(R.id.txtFirmName);
        contact_pick_img=findViewById(R.id.contact_pick_img);
        contact2_pick_img=findViewById(R.id.contact2_pick_img);
    }

    public void dateFunction(){
        Calendar calendar= Calendar.getInstance();
        int year =calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int days=calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dg=new DatePickerDialog(currentActivity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int monthofyear=month+1;
                String date=dayOfMonth+"-"+monthofyear+"-"+year;
                txtOrderDate.setText(date);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Date dt = null;
                try {
                    dt = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat your_format = new SimpleDateFormat("yyyy-MM-dd");
                OrderDate = your_format.format(dt);
                orderToset=OrderDate;


            }
        },year,month,days);
        dg.getDatePicker().setMaxDate(new Date().getTime());
        dg.show();

    }

    private  WorkOrderPojo getData(){
        workOrderPojo.setFname(txtfname.getText().toString());
        workOrderPojo.setLname(txtlname.getText().toString());
        workOrderPojo.setMobile(txtmobile.getText().toString());
        workOrderPojo.setAddress(txtaddr.getText().toString());
        workOrderPojo.setCity(txtcity.getText().toString());
        workOrderPojo.setState(spnState.getSelectedItem().toString());
        workOrderPojo.setEmail(txtemail.getText().toString());
        workOrderPojo.setDesignation(txtdesignation.getText().toString());
        workOrderPojo.setMname(txtmname.getText().toString());
        workOrderPojo.setProjectType(spnProjectType.getSelectedItem().toString());
        workOrderPojo.setBu(txtbu.getText().toString());
        workOrderPojo.setConsumer_no(txtconsumer_no.getText().toString());
        workOrderPojo.setCapacity(txtcapacity.getText().toString());
        workOrderPojo.setSystemdetail(txtsysdetails.getText().toString());
        workOrderPojo.setPanel(txtpanel.getText().toString());
        workOrderPojo.setInverter(txtinverter.getText().toString());
        workOrderPojo.setAmount(txtamount.getText().toString());
        workOrderPojo.setPhase(spnPhase.getSelectedItem().toString());
        workOrderPojo.setStructure(spnStructure.getSelectedItem().toString());
        workOrderPojo.setOrder_date(orderToset);
        workOrderPojo.setGridType(spnGridType.getSelectedItem().toString());
        workOrderPojo.setContactPerson(txtmobile_con_person.getText().toString());
        workOrderPojo.setFirm_name(txtFirmName.getText().toString());
        workOrderPojo.setGst_no(txtgst_no.getText().toString());
        workOrderPojo.setLoadextension(load_extension.isChecked()? "1" : "0");
        workOrderPojo.setChangeofname(changeofname.isChecked()? "1" : "0");
        workOrderPojo.setSolarsanction(solarsanction.isChecked()? "1" : "0");
        workOrderPojo.setMedasaction(medasaction.isChecked()? "1" : "0");
        workOrderPojo.setMeterinstallation(meterinstall.isChecked()? "1" : "0");
        workOrderPojo.setSubsidy_approval(subsidy_approval.isChecked()? "1" : "0");
        workOrderPojo.setCommissioning(commissioning.isChecked()? "1" : "0");
        workOrderPojo.setAgreement(agreement.isChecked()? "1" : "0");
        workOrderPojo.setInvertercapicity(txtinvertercapacity.getText().toString());
        workOrderPojo.setPanelcapacity(txtpanelcapacity.getText().toString());
        workOrderPojo.setRateFromCompany(txtRate.getText().toString());
        workOrderPojo.setContact_person_name(txtContactPerson.getText().toString());
        return workOrderPojo;
    }
    public JsonParserVolley setAllparameter(){
        JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("pkid",workOrderPojo.getPkid());
        jsonParserVolley.addParameter("firstName",workOrderPojo.getFname());
        jsonParserVolley.addParameter("lastName",workOrderPojo.getLname());
        jsonParserVolley.addParameter("city",workOrderPojo.getCity());
        jsonParserVolley.addParameter("state",workOrderPojo.getState());
        jsonParserVolley.addParameter("mobile1",workOrderPojo.getMobile());
        jsonParserVolley.addParameter("email",workOrderPojo.getEmail());
        jsonParserVolley.addParameter("address",workOrderPojo.getAddress());
        jsonParserVolley.addParameter("capacity",workOrderPojo.getCapacity());
        jsonParserVolley.addParameter("location",workOrderPojo.getLocation());
        jsonParserVolley.addParameter("phase",workOrderPojo.getPhase());
        jsonParserVolley.addParameter("system_detail",workOrderPojo.getSystemdetail());
        jsonParserVolley.addParameter("panel",workOrderPojo.getPanel());
        jsonParserVolley.addParameter("inverter",workOrderPojo.getInverter());
        jsonParserVolley.addParameter("structure",workOrderPojo.getStructure());
        jsonParserVolley.addParameter("amount",workOrderPojo.getAmount());
        jsonParserVolley.addParameter("load_extension",workOrderPojo.getLoadextension());
        jsonParserVolley.addParameter("change_of_name",workOrderPojo.getChangeofname());
        jsonParserVolley.addParameter("solar_sanction",workOrderPojo.getSolarsanction());
        jsonParserVolley.addParameter("meter_installation",workOrderPojo.getMeterinstallation());
        jsonParserVolley.addParameter("panel_capacity",workOrderPojo.getPanelcapacity());
        jsonParserVolley.addParameter("inverter_capacity",workOrderPojo.getInvertercapicity());
        jsonParserVolley.addParameter("order_date",workOrderPojo.getOrder_date());
        jsonParserVolley.addParameter("designation",workOrderPojo.getDesignation());
        jsonParserVolley.addParameter("txtRate",workOrderPojo.getRateFromCompany());
        jsonParserVolley.addParameter("gridtype",workOrderPojo.getGridType());
        jsonParserVolley.addParameter("commissioning",workOrderPojo.getCommissioning());
        jsonParserVolley.addParameter("project_type",workOrderPojo.getProjectType());
        jsonParserVolley.addParameter("con_person_mobile",workOrderPojo.getContactPerson());
        jsonParserVolley.addParameter("mname",workOrderPojo.getMname());
        jsonParserVolley.addParameter("meda_sanction",workOrderPojo.getMedasaction());
        jsonParserVolley.addParameter("subsidy_approval",workOrderPojo.getSubsidy_approval());
        jsonParserVolley.addParameter("agreement",workOrderPojo.getAgreement());
        jsonParserVolley.addParameter("consumer_no",workOrderPojo.getConsumer_no());
        jsonParserVolley.addParameter("bu",workOrderPojo.getBu());
        jsonParserVolley.addParameter("gst_no",workOrderPojo.getGst_no());
        jsonParserVolley.addParameter("firm_name",workOrderPojo.getFirm_name());
        jsonParserVolley.addParameter("contact_person_name",workOrderPojo.getContact_person_name());
        jsonParserVolley.addParameter("insertuserid",SessionManager.getMyInstance(currentActivity).getEmpid());
        return jsonParserVolley;
    }
    private int validate() {
        if(workOrderPojo.getFname().trim().length()==0){
            Toast.makeText(currentActivity,"Enter the first name",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getMname().trim().length()==0){
            Toast.makeText(currentActivity,"Enter the middle name",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getLname().trim().length()==0){
            Toast.makeText(currentActivity,"Enter the last name",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getMobile().trim().length()!=10){
            Toast.makeText(currentActivity,"Enter the valid mobile no.",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getConsumer_no().trim().length()==0){
            Toast.makeText(currentActivity,"Enter the consumer no.",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getDesignation().trim().length()==0){
            Toast.makeText(currentActivity,"Enter the designation",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getCapacity().trim().length()==0){
            Toast.makeText(currentActivity,"Enter the capacity",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getPanel().trim().length()==0){
            Toast.makeText(currentActivity,"Enter the panel make",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getInverter().trim().length()==0){
            Toast.makeText(currentActivity,"Enter the inverter make",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getBu().trim().length()==0){
            Toast.makeText(currentActivity,"Enter the billing unit",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getInvertercapicity().trim().length()==0){
            Toast.makeText(currentActivity,"Enter the inverter capacity",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getPanelcapacity().trim().length()==0){
            Toast.makeText(currentActivity,"Enter the panel capacity",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getRateFromCompany().trim().length()==0){
            Toast.makeText(currentActivity,"Enter the rate from company",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getProjectType().equals("--Select--")){
            Toast.makeText(currentActivity,"Select project type",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getGridType().equals("--Select--")){
            Toast.makeText(currentActivity,"Select Grid type",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getPhase().equals("--Select--")){
            Toast.makeText(currentActivity,"Select Phase",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getEmail().trim().length()==0){
            Toast.makeText(currentActivity,"Enter the email id",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getAddress().trim().length()==0){
            Toast.makeText(currentActivity,"Enter the address",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getStructure().equals("--Select--")){
            Toast.makeText(currentActivity,"Select structure",Toast.LENGTH_SHORT).show();
            return 0;
        }

        if(workOrderPojo.getGst_no().trim().length()!=0 && workOrderPojo.getGst_no().trim().length()!=11){
            Toast.makeText(currentActivity,"Invalid GST No. It should be 11 character.",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(txtamount.getVisibility()!=View.GONE && workOrderPojo.getAmount().trim().length()==0){
            Toast.makeText(currentActivity,"Enter the work order amount",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(txtRate.getVisibility()!=View.GONE && workOrderPojo.getRateFromCompany().trim().length()==0){
            Toast.makeText(currentActivity,"Enter the rate from company",Toast.LENGTH_SHORT).show();
            return 0;
        }
        if(workOrderPojo.getContactPerson().trim().length()>0 && workOrderPojo.getContactPerson().trim().length()!=10){
            Toast.makeText(currentActivity,"Invalid contact person mobile no.",Toast.LENGTH_SHORT).show();
            return 0;
        }
        return 1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 || requestCode==102){
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();
                ContentResolver cr = currentActivity.getContentResolver();
                Cursor cur = cr.query(contactData, null, null, null, null);
                if (cur.getCount() > 0) {// thats mean some resutl has been found
                    if(cur.moveToNext()) {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                        Log.e("Names", name);
                        if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                        {
                            // Query phone here. Covered next
                            Cursor phones = currentActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                            while (phones.moveToNext()) {
                                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                if(requestCode==101){
                                    txtmobile.setText(phoneNumber);
                                }
                                else if(requestCode==102){
                                    txtmobile_con_person.setText(phoneNumber);
                                }
                            }
                            phones.close();
                        }

                    }
                }
                cur.close();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(currentActivity, SearchOrderActivity.class);
        intent.putExtra("modul","workorder");
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent(currentActivity, SearchOrderActivity.class);
                intent.putExtra("modul","workorder");
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
