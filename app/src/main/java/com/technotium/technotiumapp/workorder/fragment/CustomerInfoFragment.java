package com.technotium.technotiumapp.workorder.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.workorder.Interface.DataUpdate;
import com.technotium.technotiumapp.workorder.adapter.SpinnerAdapter;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import java.util.ArrayList;

public class CustomerInfoFragment extends Fragment  implements DataUpdate {

    WorkOrderPojo workOrderPojo=new WorkOrderPojo();
    EditText txtfname,txtlname,txtmobile,txtaddr,txtcity,txtemail,txtdesignation,txtmname,txtconsumer_no,txtbu;
    Spinner spnState,spnProjectType;
    SpinnerAdapter projectType_adapter, state_adapter;
    ArrayList<String> stateArray=new ArrayList<>();
    ArrayList<String> projectTypeArray=new ArrayList<>();

    public CustomerInfoFragment() {
        // Required empty public constructor
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_customer_info, container, false);
        txtfname=view.findViewById(R.id.txtfname);
        txtlname=view.findViewById(R.id.txtlname);
        txtmobile=view.findViewById(R.id.txtmobile);
        txtaddr=view.findViewById(R.id.txtaddr);
        txtcity=view.findViewById(R.id.txtcity);
        spnState=view.findViewById(R.id.spnState);
        txtemail=view.findViewById(R.id.txtemail);
        txtmname=view.findViewById(R.id.txtmname);
        txtconsumer_no=view.findViewById(R.id.txtconsumer_no);
        txtbu=view.findViewById(R.id.txtbu);
        txtdesignation=view.findViewById(R.id.txtdesignation);
        state_adapter = new SpinnerAdapter(getActivity(), stateArray);
        spnState.setAdapter(state_adapter);
        spnProjectType=view.findViewById(R.id.spnProjectType);
        projectType_adapter = new SpinnerAdapter(getActivity(), projectTypeArray);
        spnProjectType.setAdapter(projectType_adapter);
        if(getArguments()!=null){
            if(getArguments().getSerializable("workorder")!=null){
                workOrderPojo=(WorkOrderPojo)getArguments().getSerializable("workorder");
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
                txtmobile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(contactPickerIntent,101);
                    }
                });
            }
        }
        return view;
    }


    @Override
    public void setData(WorkOrderPojo data) {
        workOrderPojo=data;
    }

    @Override
    public WorkOrderPojo getData() {
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
        return workOrderPojo;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==101){
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();
                ContentResolver cr = getActivity().getContentResolver();
                Cursor cur = cr.query(contactData, null, null, null, null);
                if (cur.getCount() > 0) {// thats mean some resutl has been found
                    if(cur.moveToNext()) {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                        Log.e("Names", name);
                        if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                        {
                            // Query phone here. Covered next
                            Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                            while (phones.moveToNext()) {
                                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                txtmobile.setText(phoneNumber);
                            }
                            phones.close();
                        }

                    }
                }
                cur.close();
            }
        }

    }

}
