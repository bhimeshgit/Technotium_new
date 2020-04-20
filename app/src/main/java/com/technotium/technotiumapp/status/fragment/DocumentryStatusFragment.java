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
import com.technotium.technotiumapp.expenses.activity.AddExpense;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DocumentryStatusFragment extends Fragment {


    CheckBox load_extension,change_of_name,solar_sanction,subsidy_sanction,meter_installation,agreement;
    CheckBox Application_submission,Site_Visit,JIR_application,Final_submission,Meter_Testing,Invoice_generation,Documents_handover_to_client
    ,Insurance;
    EditText txtOther;
    int order_id;
    TextView exname_txt;
    EditText txtEname,txtLastUpdateDate,txtUpdateDate;
    TextView status_txt;
    Button btn;
    ProgressDialog pDialog;
    LinearLayout date_lay,ename_lay;
    String OrderDate;
    String orderToset="";
    public DocumentryStatusFragment() {
        // Required empty public constructor

    }

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
        View view=inflater.inflate(R.layout.fragment_documentry_status, container, false);
        load_extension=view.findViewById(R.id.load_extension);
        change_of_name=view.findViewById(R.id.change_of_name);
        solar_sanction=view.findViewById(R.id.solar_sanction);
        subsidy_sanction=view.findViewById(R.id.subsidy_sanction);
        meter_installation=view.findViewById(R.id.meter_installation);
        agreement=view.findViewById(R.id.agreement);
        txtOther=view.findViewById(R.id.txtOther);
        status_txt=view.findViewById(R.id.status_txt);
        btn=view.findViewById(R.id.btnSetStatus);
        exname_txt=view.findViewById(R.id.exname_txt);
        Application_submission=view.findViewById(R.id.Application_submission);
        Site_Visit=view.findViewById(R.id.Site_Visit);
        JIR_application=view.findViewById(R.id.JIR_application);
        Final_submission=view.findViewById(R.id.Final_submission);
        Meter_Testing=view.findViewById(R.id.Meter_Testing);
        Invoice_generation=view.findViewById(R.id.Invoice_Generation);
        Documents_handover_to_client=view.findViewById(R.id.Documents_handover_to_client);
        Insurance=view.findViewById(R.id.Insurance);
        txtEname=view.findViewById(R.id.txtEname);
        txtLastUpdateDate=view.findViewById(R.id.txtLastUpdateDate);
        txtEname.setEnabled(false);
        txtLastUpdateDate.setEnabled(false);
        txtUpdateDate=view.findViewById(R.id.txtUpdateDate);
        date_lay=view.findViewById(R.id.date_lay);
        ename_lay=view.findViewById(R.id.ename_lay);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus();
            }
        });
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        getOrderStatus();
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//        Calendar cal=Calendar.getInstance();
//        Date dt=cal.getTime();
//        orderToset=sdf.format(dt);
//        txtUpdateDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(dt));
        txtUpdateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFunction();
            }
        });
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
    public void updateStatus(){
        int flg=0;
        StringBuilder status=new StringBuilder("");
        if(load_extension.isChecked()){
            status.append("Load Extension, ");
            flg=1;
        }
        if(change_of_name.isChecked()){
            status.append("Change of Name, ");
            flg=1;
        }
        if(solar_sanction.isChecked()){
            status.append("Solar Sanction, ");
            flg=1;
        }
        if(subsidy_sanction.isChecked()){
            status.append("Subsidy Sanction, ");
            flg=1;
        }
        if(meter_installation.isChecked()){
            status.append("Meter Installation, ");
            flg=1;
        }
        if(agreement.isChecked()){
            status.append("Agreement, ");
            flg=1;
        }
        if(Application_submission.isChecked()){
            status.append("Application Submission, ");flg=1;
        }
        if(Site_Visit.isChecked()){
            status.append("Site Visit, ");flg=1;
        }
        if(JIR_application.isChecked()){
            status.append("JIR Application, ");flg=1;
        }
        if(Final_submission.isChecked()){
            status.append("Final Submission, ");flg=1;
        }
        if(Meter_Testing.isChecked()){
            status.append("Meter Testing, ");flg=1;
        }
        if(Invoice_generation.isChecked()){
            status.append("Invoice Generation, ");flg=1;
        }
        if(Documents_handover_to_client.isChecked()){
            status.append("Documents Handover To Client, ");flg=1;
        }
        if(Insurance.isChecked()){
            status.append("Insurance, ");flg=1;
        }
        if(status_txt.getText().toString().length()>0){
            status.append(status_txt.getText().toString()+" ");
        }

        if(!txtOther.getText().toString().equals("")){
            status.append(txtOther.getText().toString());
            flg=1;
        }

        if(flg==0){
            Toast.makeText(getActivity(),"Please select or enter status",Toast.LENGTH_SHORT).show();
            return;
        }
//        status.setLength(status.length()-2);
        pDialog.show();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(getActivity());
        jsonParserVolley.addParameter("order_id", order_id+"");
        jsonParserVolley.addParameter("status", status.toString());
        jsonParserVolley.addParameter("update_date", orderToset);
        jsonParserVolley.addParameter("userid", SessionManager.getMyInstance(getActivity()).getEmpid());
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.UPDATE_ORDER_STATTUS_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        Log.d("iss","response="+response);
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                txtOther.setText("");
                                getOrderStatus();
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

    public void getOrderStatus(){
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(getActivity());
        jsonParserVolley.addParameter("order_id", order_id+"");

        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.GET_ORDER_STATTUS_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        Log.d("iss","response="+response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");

                            if(success==1){
                                String status_data=jsonObject.getString("data");
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
                                String[] status_array=status_data.split(",");
                                StringBuilder other_status=new StringBuilder("");
                                for(String s : status_array){
                                    if(s.trim().equalsIgnoreCase(load_extension.getText().toString().trim())){
                                        load_extension.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(change_of_name.getText().toString().trim())){
                                        change_of_name.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(solar_sanction.getText().toString().trim())){
                                        solar_sanction.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(subsidy_sanction.getText().toString().trim())){
                                        subsidy_sanction.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(meter_installation.getText().toString().trim())){
                                        meter_installation.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(agreement.getText().toString().trim())){
                                        agreement.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(Application_submission.getText().toString().trim())){
                                        Application_submission.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(Site_Visit.getText().toString().trim())){
                                        Site_Visit.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(Meter_Testing.getText().toString().trim())){
                                        Meter_Testing.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(JIR_application.getText().toString().trim())){
                                        JIR_application.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(Final_submission.getText().toString().trim())){
                                        Final_submission.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(Invoice_generation.getText().toString().trim())){
                                        Invoice_generation.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(Documents_handover_to_client.getText().toString().trim())){
                                        Documents_handover_to_client.setChecked(true);
                                    }
                                    else if(s.trim().equalsIgnoreCase(Insurance.getText().toString().trim())){
                                        Insurance.setChecked(true);
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
}
