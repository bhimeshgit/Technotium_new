package com.technotium.technotiumapp.workorder.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.workorder.Interface.DataUpdate;
import com.technotium.technotiumapp.workorder.adapter.SpinnerAdapter;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OrderInfoFragment extends Fragment implements DataUpdate {

    EditText txtcapacity,txtlocation,txtsysdetails,txtpanel,txtinverter,txtamount,txtOrderDate,txtmobile_con_person;
    Spinner spnPhase,spnStructure,spnGridType;
    ArrayList<String> gridArray=new ArrayList<>();
    ArrayList<String> structureArray=new ArrayList<>();
    ArrayList<String> phaseArray=new ArrayList<>();
    WorkOrderPojo workOrderPojo=new WorkOrderPojo();
    String OrderDate;
    TextView txtamounttxt;
    String orderToset;
    public OrderInfoFragment() {
        // Required empty public constructor
        structureArray.add("ELEVATED");
        structureArray.add("ROOF TOP");
        structureArray.add("GROUND MOUNTED");
        structureArray.add("PLATFORM");
        phaseArray.add("1");
        phaseArray.add("3");
        gridArray.add("On Grid");
        gridArray.add("Off Grid");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("iss","On create View "+getClass().getName());
        View view=inflater.inflate(R.layout.fragment_order_info, container, false);
        txtcapacity=view.findViewById(R.id.txtcapacity);
        txtlocation=view.findViewById(R.id.txtlocation);
        txtsysdetails=view.findViewById(R.id.txtsysdetails);
        txtpanel=view.findViewById(R.id.txtpanel);
        txtinverter=view.findViewById(R.id.txtinverter);
        spnPhase=view.findViewById(R.id.spnPhase);
        spnStructure=view.findViewById(R.id.spnStructure);
        txtamount=view.findViewById(R.id.txtamount);
        txtamounttxt=view.findViewById(R.id.txtamounttxt);
        txtOrderDate=view.findViewById(R.id.txtOrderDate);
        txtmobile_con_person=view.findViewById(R.id.txtmobile_con_person);
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
        SpinnerAdapter adapter = new SpinnerAdapter(getActivity(), structureArray);
        spnStructure.setAdapter(adapter);
        adapter = new SpinnerAdapter(getActivity(), phaseArray);
        spnPhase.setAdapter(adapter);
        spnGridType=view.findViewById(R.id.spnGridType);
        adapter = new SpinnerAdapter(getActivity(), gridArray);
        spnGridType.setAdapter(adapter);
        if(SessionManager.getMyInstance(getActivity()).getEmpType().equalsIgnoreCase("Electrician")){
            txtamount.setVisibility(View.GONE);
            txtamounttxt.setVisibility(View.GONE);
        }
        setForUpdate();
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

    @Override
    public void setData(WorkOrderPojo data) {
        workOrderPojo=data;
    }

    @Override
    public WorkOrderPojo getData() {
        workOrderPojo.setCapacity(txtcapacity.getText().toString());
        workOrderPojo.setLocation(txtlocation.getText().toString());
        workOrderPojo.setSystemdetail(txtsysdetails.getText().toString());
        workOrderPojo.setPanel(txtpanel.getText().toString());
        workOrderPojo.setInverter(txtinverter.getText().toString());
        workOrderPojo.setAmount(txtamount.getText().toString());
        workOrderPojo.setPhase(spnPhase.getSelectedItem().toString());
        workOrderPojo.setStructure(spnStructure.getSelectedItem().toString());
        workOrderPojo.setOrder_date(orderToset);
        workOrderPojo.setGridType(spnGridType.getSelectedItem().toString());
        workOrderPojo.setContactPerson(txtmobile_con_person.getText().toString());
        return workOrderPojo;
    }

    public void setForUpdate(){
        if(getArguments()!=null) {
            if (getArguments().getSerializable("workorder") != null) {
                workOrderPojo = (WorkOrderPojo) getArguments().getSerializable("workorder");
                txtcapacity.setText(workOrderPojo.getCapacity());
                txtlocation.setText(workOrderPojo.getLocation());
                txtsysdetails.setText(workOrderPojo.getSystemdetail());
                txtpanel.setText(workOrderPojo.getPanel());
                txtinverter.setText(workOrderPojo.getInverter());
                txtamount.setText(workOrderPojo.getAmount());
                orderToset=workOrderPojo.getOrder_date();
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
            }
        }
    }
}
