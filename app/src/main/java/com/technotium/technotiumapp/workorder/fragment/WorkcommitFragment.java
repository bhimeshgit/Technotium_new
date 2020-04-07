package com.technotium.technotiumapp.workorder.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.WelcomeEmpActivity;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.workorder.Interface.DataUpdate;
import com.technotium.technotiumapp.workorder.activity.SearchOrderActivity;
import com.technotium.technotiumapp.workorder.activity.WorkOrderActivity;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import org.json.JSONException;
import org.json.JSONObject;

public class WorkcommitFragment extends Fragment implements DataUpdate {

    CheckBox load_extension,changeofname,solarsanction,meterinstall,medasaction,subsidy_approval,commissioning,agreement;
    EditText txtpanelcapacity,txtinvertercapacity,txtRate;
    WorkOrderPojo workOrderPojo=new WorkOrderPojo();
    Button btnSave,btnUpdate;
    ProgressDialog pDialog ;
    TextView txtRatetxt;
    public WorkcommitFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("iss","On create View "+getClass().getName());
        View view=inflater.inflate(R.layout.fragment_workcommit, container, false);
        load_extension=view.findViewById(R.id.load_extension);
        changeofname=view.findViewById(R.id.changeofname);
        solarsanction=view.findViewById(R.id.solarsanction);
        meterinstall=view.findViewById(R.id.meterinstall);
        medasaction=view.findViewById(R.id.medasaction);
        txtRatetxt=view.findViewById(R.id.txtRatetxt);
        subsidy_approval=view.findViewById(R.id.subsidy_approval);
        commissioning=view.findViewById(R.id.commissioning);
        agreement=view.findViewById(R.id.agreement);
        txtpanelcapacity=view.findViewById(R.id.txtpanelcapacity);
        txtinvertercapacity=view.findViewById(R.id.txtinvertercapacity);
        txtRate=view.findViewById(R.id.txtRate);
        pDialog = new ProgressDialog(getActivity());
        btnSave=view.findViewById(R.id.btnSave);
        btnUpdate=view.findViewById(R.id.btnUpdate);

        if(SessionManager.getMyInstance(getActivity()).getEmpType().equalsIgnoreCase("Electrician")){
            txtRate.setVisibility(View.GONE);
            txtRatetxt.setVisibility(View.GONE);
        }
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
                                        Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(getActivity(), SearchOrderActivity.class);
                                        intent.putExtra("modul","workorder");
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                    else{
                                        Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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
//                SessionManager.getMyInstance(getActivity()).progressShow();
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
                                        Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(getActivity(), SearchOrderActivity.class);
                                        intent.putExtra("modul","workorder");
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                    else{
                                        Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
            }
        });
        setForUpdate();
        return view;
    }

    @Override
    public void setData(WorkOrderPojo data) {
        workOrderPojo=data;
    }

    @Override
    public WorkOrderPojo getData() {
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
        return workOrderPojo;
    }

    public void setForUpdate(){
        if(getArguments()!=null) {
            if (getArguments().getSerializable("workorder") != null) {
                btnSave.setVisibility(View.GONE);
                workOrderPojo = (WorkOrderPojo) getArguments().getSerializable("workorder");
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
                if(SessionManager.getMyInstance(getActivity()).getEmpType().equals("Employee") || SessionManager.getMyInstance(getActivity()).getEmpType().equals("Dealer")){
                    txtRate.setEnabled(false);
                }
            }
        }
        else {
            btnUpdate.setVisibility(View.GONE);
        }
    }

    public JsonParserVolley setAllparameter(){
        JsonParserVolley jsonParserVolley = new JsonParserVolley(getActivity());
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
        jsonParserVolley.addParameter("insertuserid",SessionManager.getMyInstance(getActivity()).getEmpid());
        return jsonParserVolley;
    }
}
