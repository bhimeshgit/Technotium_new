package com.technotium.technotiumapp.material.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.material.adapter.MaterialAdapter;
import com.technotium.technotiumapp.material.model.MaterialPojo;
import com.technotium.technotiumapp.payment.activity.AddPaymentActivity;
import com.technotium.technotiumapp.workorder.activity.SearchOrderActivity;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddMaterialActivity extends AppCompatActivity {

    Button btnAdd;
    EditText txtquantity,txtMaterialName;
    WorkOrderPojo workOrderPojo;
    AddMaterialActivity currentActivity;
    ProgressDialog pDialog;
    RecyclerView lv_materialList;
    MaterialAdapter materialAdapter;
    ArrayList<MaterialPojo> materialList;
    RecyclerView.LayoutManager layoutManager;
    MaterialAdapter adapter;
    AlertDialog alertDialog;
    EditText txtMaterialDate;
    String OrderDate;
    String orderToset;
    ImageView date_img_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material);
        Intent intent=getIntent();
        if(intent!=null){
            if(intent.getSerializableExtra("orderData")!=null){
                workOrderPojo=(WorkOrderPojo)intent.getSerializableExtra("orderData");
                init();
            }
        }
    }
    private void init(){
        currentActivity=AddMaterialActivity.this;
        materialList=new ArrayList<MaterialPojo>();
        btnAdd=findViewById(R.id.btnAdd);
        txtquantity=findViewById(R.id.txtquantity);
        txtMaterialName=findViewById(R.id.txtMaterialName);
        lv_materialList=findViewById(R.id.lv_materialList);
        txtMaterialDate=findViewById(R.id.txtOrderDate);
        date_img_view=findViewById(R.id.date_img_view);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMaterial();
            }
        });

        pDialog = new ProgressDialog(currentActivity);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal=Calendar.getInstance();
        Date dt=cal.getTime();
        orderToset=sdf.format(dt);
        txtMaterialDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(dt));
        txtMaterialDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFunction();
            }
        });
        date_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFunction();
            }
        });
        getMaterial();
    }
    public void dateFunction(){
        Calendar calendar= Calendar.getInstance();
        int year =calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int days=calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dg=new DatePickerDialog(AddMaterialActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int monthofyear=month+1;
                String date=dayOfMonth+"-"+monthofyear+"-"+year;
                txtMaterialDate.setText(date);
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
    private void addMaterial(){
        if(txtMaterialName.getText().toString().trim().equals("")){
            Toast.makeText(currentActivity,"Enter material name",Toast.LENGTH_SHORT).show();
        }
        else if(txtquantity.getText().toString().trim().equals("")){
            Toast.makeText(currentActivity,"Enter the quantity",Toast.LENGTH_SHORT).show();
        }
        else{
            pDialog.show();
            final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
            jsonParserVolley.addParameter("material",txtMaterialName.getText().toString());
            jsonParserVolley.addParameter("quantity",txtquantity.getText().toString());
            jsonParserVolley.addParameter("order_id",workOrderPojo.getPkid());
            jsonParserVolley.addParameter("userid", SessionManager.getMyInstance(currentActivity).getEmpid());
            jsonParserVolley.addParameter("material_date", orderToset);
            jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.ADD_MATERIAL_URL ,new JsonParserVolley.VolleyCallback() {
                        @Override
                        public void getResponse(String response) {
                            pDialog.dismiss();
                            Log.d("iss",response);
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                int success=jsonObject.getInt("success");
                                if(success==1){
                                    Intent intent=new Intent(currentActivity,AddMaterialActivity.class);
                                    intent.putExtra("orderData",workOrderPojo);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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
    }

    private void getMaterial(){
        pDialog.show();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("order_id",workOrderPojo.getPkid());
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.GET_MATERIAL_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        Log.d("iss",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++) {
                                    JSONObject jsonWO = jsonArray.getJSONObject(i);
                                    MaterialPojo materialPojo=new MaterialPojo();
                                    materialPojo.setMaterial_id(Integer.parseInt(jsonWO.getString("pkid")));
                                    materialPojo.setActive(Integer.parseInt(jsonWO.getString("active")));
                                    materialPojo.setOrder_id(Integer.parseInt(jsonWO.getString("order_id")));
                                    materialPojo.setMaterial(jsonWO.getString("material"));
                                    materialPojo.setQuantity(jsonWO.getString("quantity"));
                                    materialPojo.setInserttimestamp(jsonWO.getString("inserttimestamp"));
                                    materialPojo.setInsertuserid(Integer.parseInt(jsonWO.getString("userid")));
                                    materialPojo.setInsertBy(jsonWO.getString("fullname"));
                                    materialList.add(materialPojo);
                                }

                                layoutManager=new GridLayoutManager(currentActivity,1);
                                lv_materialList.setLayoutManager(layoutManager);
                                lv_materialList.setHasFixedSize(true);
                                adapter=new MaterialAdapter(materialList, currentActivity);
                                lv_materialList.setAdapter(adapter);
                                adapter.setOnItemClickListener(new MaterialAdapter.ClickListener() {
                                    @Override
                                    public void onItemLongClick(int position, View v) {
                                        showDeleteAlertDialog(position);
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

    private void deleteMaterial(final int position){
        pDialog.show();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("material_id",materialList.get(position).getMaterial_id()+"");
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.DELETE_MATERIAL_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        Log.d("iss",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                materialList.get(position).setActive(2);
                                adapter.notifyItemChanged(position);
                                Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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

    public void showDeleteAlertDialog(final int position){
        alertDialog=new AlertDialog.Builder(currentActivity)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this material data?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMaterial(position);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(currentActivity, SearchOrderActivity.class);
        intent.putExtra("modul","material");
        startActivity(intent);
        finish();
    }
}
