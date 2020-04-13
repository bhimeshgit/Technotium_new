package com.technotium.technotiumapp.status.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TechnicalStatus extends Fragment {


    CheckBox rccb,structure,panel_installation,grounding,earthing,inverter,dcdb,meter_installation,acdb,la;
    CheckBox Service_wire_installation,Walkway,Structure_painting,AC_Wiring,DC_Wiring;
    CheckBox Commissioning_of_project,Remote_Monitoring,Structure_installation;
    EditText txtOther;
    int order_id;
    TextView status_txt,txtEname,txtLastUpdateDate,txtUpdateDate;;
    Button btn;
    ProgressDialog pDialog;
    LinearLayout date_lay,ename_lay;
    String OrderDate;
    String orderToset="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if(getArguments().getInt("order_id")!=0) {
                order_id=getArguments().getInt("order_id");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_technical_status, container, false);
        rccb=view.findViewById(R.id.rccb);

        panel_installation=view.findViewById(R.id.panel_installation);
        grounding=view.findViewById(R.id.grounding);
        earthing=view.findViewById(R.id.earthing);
        inverter=view.findViewById(R.id.inverter);
        dcdb=view.findViewById(R.id.dcdb);
        acdb=view.findViewById(R.id.acdb);
        la=view.findViewById(R.id.la);
        meter_installation=view.findViewById(R.id.meter_installation);
        Service_wire_installation=view.findViewById(R.id.Service_wire_installation);
        Commissioning_of_project=view.findViewById(R.id.Commissioning_of_project);
        Remote_Monitoring=view.findViewById(R.id.Remote_Monitoring);
        Walkway=view.findViewById(R.id.Walkway);
        Structure_painting=view.findViewById(R.id.Structure_painting);
        Structure_installation=view.findViewById(R.id.Structure_installation);
        AC_Wiring=view.findViewById(R.id.AC_Wiring);
        DC_Wiring=view.findViewById(R.id.DC_Wiring);
        txtOther=view.findViewById(R.id.txtOther);
        status_txt=view.findViewById(R.id.status_txt);
        txtEname=view.findViewById(R.id.txtEname);
        txtLastUpdateDate=view.findViewById(R.id.txtLastUpdateDate);
        txtEname.setEnabled(false);
        txtLastUpdateDate.setEnabled(false);
        txtUpdateDate=view.findViewById(R.id.txtUpdateDate);
        date_lay=view.findViewById(R.id.date_lay);
        ename_lay=view.findViewById(R.id.ename_lay);
        btn=view.findViewById(R.id.btnSetStatus);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus();
            }
        });
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        txtUpdateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFunction();
            }
        });
        getOrderTechnicalStatus();
        return view;
    }
    public void dateFunction(){
        Calendar calendar= Calendar.getInstance();
        int year =calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int days=calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dg=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int monthofyear=month+1;
                String date=dayOfMonth+"-"+monthofyear+"-"+year;
                txtUpdateDate.setText(date);
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
    public void getOrderTechnicalStatus(){
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(getActivity());
        jsonParserVolley.addParameter("order_id", order_id+"");
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.GET_TECHNICAL_ORDER_STATUS_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        Log.d("iss","response="+response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){

                                String[] status_array=jsonObject.getString("data").split(",");
                                String update_date=jsonObject.getString("update_date");
                                String ename=jsonObject.getString("ename");
                                if(!update_date.equals("") && !update_date.equals("null")){
                                    date_lay.setVisibility(View.VISIBLE);
                                    txtLastUpdateDate.setText(update_date);
                                }
                                if(!ename.equals("") && !ename.equals("null")){
                                    ename_lay.setVisibility(View.VISIBLE);
                                    txtEname.setText(ename);
                                }
                                StringBuilder other_status=new StringBuilder("");
                                for(String s : status_array){
                                    if(s.trim().equalsIgnoreCase(rccb.getText().toString().trim())){
                                        rccb.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(panel_installation.getText().toString().trim())){
                                        panel_installation.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(grounding.getText().toString().trim())){
                                        grounding.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(earthing.getText().toString().trim())){
                                        earthing.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(inverter.getText().toString().trim())){
                                        inverter.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(dcdb.getText().toString().trim())){
                                        dcdb.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(acdb.getText().toString().trim())){
                                        acdb.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(meter_installation.getText().toString().trim())){
                                        meter_installation.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(la.getText().toString().trim())){
                                        la.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(Service_wire_installation.getText().toString().trim())){
                                        Service_wire_installation.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(Walkway.getText().toString().trim())){
                                        Walkway.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(Structure_painting.getText().toString().trim())){
                                        Structure_painting.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(AC_Wiring.getText().toString().trim())){
                                        AC_Wiring.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(DC_Wiring.getText().toString().trim())){
                                        DC_Wiring.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(Commissioning_of_project.getText().toString().trim())){
                                        Commissioning_of_project.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(Remote_Monitoring.getText().toString().trim())){
                                        Remote_Monitoring.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(Structure_installation.getText().toString().trim())){
                                        Structure_installation.setChecked(true);
                                    }
                                    else{
                                        other_status.append(s+",");
                                    }
                                }
                                if(other_status.length()>0){
                                    other_status.setLength(other_status.length()-1);
                                }
                                if(!other_status.toString().trim().equals("")){
                                    status_txt.setText(other_status);
                                //    txtOther.setText(other_status);
                                }

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
    public void updateStatus(){
        StringBuilder status=new StringBuilder("");
        int flg=0;
        if(rccb.isChecked()){
            status.append("RCCB/ELCB Installation, ");
            flg=1;
        }
        if(Structure_installation.isChecked()){
            status.append("Structure Installation, ");
            flg=1;
        }
        if(panel_installation.isChecked()){
            status.append("Panel Installation, ");
            flg=1;
        }
        if(grounding.isChecked()){
            status.append("Grounding, ");
            flg=1;
        }
        if(earthing.isChecked()){
            status.append("Earthing, ");
            flg=1;
        }
        if(inverter.isChecked()){
            status.append("Inverter Installation, ");
            flg=1;
        }
        if(dcdb.isChecked()){
            status.append("DCDB, ");
            flg=1;
        }
        if(acdb.isChecked()){
            status.append("ACDB, ");
            flg=1;
        }
        if(meter_installation.isChecked()){
            status.append("Meter Installation, ");
            flg=1;
        }
        if(la.isChecked()){
            status.append("LA Installation, ");
            flg=1;
        }

        if(Service_wire_installation.isChecked()){
            status.append("Service Wire Installation, ");flg=1;
        }
        if(Commissioning_of_project.isChecked()){
            status.append("Commissioning of Project, ");flg=1;
        }
        if(Remote_Monitoring.isChecked()){
            status.append("Remote Monitoring, ");flg=1;
        }
        if(Walkway.isChecked()){
            status.append("Walkway, ");flg=1;
        }
        if(Structure_painting.isChecked()){
            status.append("Structure Painting, ");flg=1;
        }
        if(AC_Wiring.isChecked()){
            status.append("AC Wiring, ");flg=1;
        }
        if(DC_Wiring.isChecked()){
            status.append("DC Wiring, ");flg=1;
        }
        if(Structure_installation.isChecked()){
            status.append("Structure Installation, ");flg=1;
        }
        if(status_txt.getText().toString().length()>0){
            status.append(status_txt.getText().toString()+" ");
        }

        if(!txtOther.getText().toString().equals("")){
            status.append(txtOther.getText().toString());
            flg=1;
        }
//        status.setLength(status.length()-2);
        if(flg==0){
            Toast.makeText(getActivity(),"Please select or enter status",Toast.LENGTH_SHORT).show();
            return;
        }
        pDialog.show();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(getActivity());
        jsonParserVolley.addParameter("order_id", order_id+"");
        jsonParserVolley.addParameter("status", status.toString());
        jsonParserVolley.addParameter("userid", SessionManager.getMyInstance(getActivity()).getEmpid());
        jsonParserVolley.addParameter("update_date", orderToset);
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.UPDATE_TECHNICAL_ORDER_STATUS_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        Log.d("iss","response="+response);
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");

                            if(success==1){
                                Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                getOrderTechnicalStatus();
                                txtOther.setText("");
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


}
