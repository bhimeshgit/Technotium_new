package com.technotium.technotiumapp.after_sales.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.after_sales.adapter.MeterReadingAdapter;
import com.technotium.technotiumapp.after_sales.adapter.SerialNoAdapter;
import com.technotium.technotiumapp.after_sales.dialog.AddMeterReadingDlgFrag;
import com.technotium.technotiumapp.after_sales.dialog.AddSerailNoDlgFrag;
import com.technotium.technotiumapp.after_sales.model.MeterReadingPojo;
import com.technotium.technotiumapp.after_sales.model.SerialNoPojo;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.payment.activity.PaymentHistoryActivity;
import com.technotium.technotiumapp.workorder.activity.SearchOrderActivity;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AfterSalesActivity extends AppCompatActivity {

    private WorkOrderPojo workOrderPojo;
    private AfterSalesActivity currentActivity;
    private ArrayList<SerialNoPojo> panelno_list,inverter_list,wifi_stick_list,gen_meter_serial_list,net_meter_serial_list,portal_list;
    private ArrayList<MeterReadingPojo> gen_meter_read_list,net_meter_read_list;
    private RecyclerView lv_panel,lv_inverter,lv_wifi_stick,lv_gen_meter_serial,lv_net_meter_serial,lv_portal,lv_gen_meter_reading,lv_net_meter_reading;
    private ProgressDialog pDialog;
    private Button btnAddPanel,btnAddInverter,btnAddWifiStick,btnAddGenMeterSerial,btnAddNetMeterSerial,btnAddPortal,btnAddGenMeterRead,btnAddNetMeterRead;
    private String TAG=AfterSalesActivity.class.getSimpleName();
    private LinearLayoutManager gridLayoutManager;
    private AddSerailNoDlgFrag addSerailNoDlgFrag;
    private SerialNoAdapter serialNoAdapter,serialNoAdapter_inverter,serialNoAdapter_wifi_stick,serialNoAdapter_gen_meter_serial,serialNoAdapter_net_meter_serial,serialNoAdapter_portal;
    private MeterReadingAdapter gen_meterReadingAdapter,net_meterReadingAdapter;
    private AlertDialog alertDialog;
    private String[] type_array={"portal","gen_meter_reading","net_meter_reading","panel","inverter","wifi_stick","get_meter_serial","net_meter_serial"};
    private Dialog zoomable_image_dialog;
    private SubsamplingScaleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sales);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        if(intent!=null){
            if(intent.getSerializableExtra("orderData")!=null){
                workOrderPojo=(WorkOrderPojo)intent.getSerializableExtra("orderData");
            }
        }
        generateId();
        createObj();
        onclick();
        getAllDataFromServer();
    }

    private void getAllDataFromServer() {
        pDialog.show();
        panelno_list.clear();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("order_id", workOrderPojo.getPkid());
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.GET_SERIAL_NO ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        try {
                            Log.d("iss",response);
                            pDialog.dismiss();
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                if(jsonObject.has("panel_data")){
                                    JSONArray jsonArray=jsonObject.getJSONArray("panel_data");
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonWO=jsonArray.getJSONObject(i);
                                        SerialNoPojo serialNoPojo=new SerialNoPojo();
                                        serialNoPojo.setPkid(jsonWO.getString("pkid"));
                                        serialNoPojo.setActive(jsonWO.getInt("active"));
                                        serialNoPojo.setAdded_by(jsonWO.getString("added_by"));
                                        serialNoPojo.setSerial_no(jsonWO.getString("serialno"));
                                        serialNoPojo.setOrder_id(jsonWO.getString("order_id"));
                                        serialNoPojo.setInserttimestamp(jsonWO.getString("inserttimestamp"));
                                        panelno_list.add(serialNoPojo);
                                    }
                                    serialNoAdapter.notifyDataSetChanged();

                                }

                                if(jsonObject.has("inverter_data")){
                                    JSONArray inverter_jsonArray=jsonObject.getJSONArray("inverter_data");
                                    for(int i=0;i<inverter_jsonArray.length();i++){
                                        JSONObject jsonWO=inverter_jsonArray.getJSONObject(i);
                                        SerialNoPojo serialNoPojo=new SerialNoPojo();
                                        serialNoPojo.setPkid(jsonWO.getString("pkid"));
                                        serialNoPojo.setActive(jsonWO.getInt("active"));
                                        serialNoPojo.setAdded_by(jsonWO.getString("added_by"));
                                        serialNoPojo.setSerial_no(jsonWO.getString("serialno"));
                                        serialNoPojo.setOrder_id(jsonWO.getString("order_id"));
                                        serialNoPojo.setInserttimestamp(jsonWO.getString("inserttimestamp"));
                                        inverter_list.add(serialNoPojo);
                                    }
                                    serialNoAdapter_inverter.notifyDataSetChanged();
                                    serialNoAdapter_inverter.setOnItemClickListener(new SerialNoAdapter.ClickListener() {
                                        @Override
                                        public void onItemClick(int position, View v) {
                                        }
                                        @Override
                                        public void onLongItemClick(int position, View v) {
                                            showDeleteAlertDialog(inverter_list.get(position).getPkid(),type_array[4],position);
                                        }
                                    });
                                }

                                if(jsonObject.has("wifi_data")){
                                    JSONArray wifi_jsonArray=jsonObject.getJSONArray("wifi_data");
                                    for(int i=0;i<wifi_jsonArray.length();i++){
                                        JSONObject jsonWO=wifi_jsonArray.getJSONObject(i);
                                        SerialNoPojo serialNoPojo=new SerialNoPojo();
                                        serialNoPojo.setPkid(jsonWO.getString("pkid"));
                                        serialNoPojo.setActive(jsonWO.getInt("active"));
                                        serialNoPojo.setAdded_by(jsonWO.getString("added_by"));
                                        serialNoPojo.setSerial_no(jsonWO.getString("serialno"));
                                        serialNoPojo.setOrder_id(jsonWO.getString("order_id"));
                                        serialNoPojo.setInserttimestamp(jsonWO.getString("inserttimestamp"));
                                        wifi_stick_list.add(serialNoPojo);
                                    }
                                    serialNoAdapter_wifi_stick.notifyDataSetChanged();

                                }

                                if(jsonObject.has("gen_meter_serial")){
                                    JSONArray gen_meter_serial_jsonArray=jsonObject.getJSONArray("gen_meter_serial");
                                    for(int i=0;i<gen_meter_serial_jsonArray.length();i++){
                                        JSONObject jsonWO=gen_meter_serial_jsonArray.getJSONObject(i);
                                        SerialNoPojo serialNoPojo=new SerialNoPojo();
                                        serialNoPojo.setPkid(jsonWO.getString("pkid"));
                                        serialNoPojo.setActive(jsonWO.getInt("active"));
                                        serialNoPojo.setAdded_by(jsonWO.getString("added_by"));
                                        serialNoPojo.setSerial_no(jsonWO.getString("serialno"));
                                        serialNoPojo.setOrder_id(jsonWO.getString("order_id"));
                                        serialNoPojo.setInserttimestamp(jsonWO.getString("inserttimestamp"));
                                        gen_meter_serial_list.add(serialNoPojo);
                                    }
                                    serialNoAdapter_gen_meter_serial.notifyDataSetChanged();

                                }

                                if(jsonObject.has("net_meter_serial")){
                                    JSONArray net_meter_serial_jsonArray=jsonObject.getJSONArray("net_meter_serial");
                                    for(int i=0;i<net_meter_serial_jsonArray.length();i++){
                                        JSONObject jsonWO=net_meter_serial_jsonArray.getJSONObject(i);
                                        SerialNoPojo serialNoPojo=new SerialNoPojo();
                                        serialNoPojo.setPkid(jsonWO.getString("pkid"));
                                        serialNoPojo.setActive(jsonWO.getInt("active"));
                                        serialNoPojo.setAdded_by(jsonWO.getString("added_by"));
                                        serialNoPojo.setSerial_no(jsonWO.getString("serialno"));
                                        serialNoPojo.setOrder_id(jsonWO.getString("order_id"));
                                        serialNoPojo.setInserttimestamp(jsonWO.getString("inserttimestamp"));
                                        net_meter_serial_list.add(serialNoPojo);
                                    }
                                    serialNoAdapter_net_meter_serial.notifyDataSetChanged();

                                }

                                if(jsonObject.has("portal")){
                                    JSONArray portal_jsonArray=jsonObject.getJSONArray("portal");
                                    for(int i=0;i<portal_jsonArray.length();i++){
                                        JSONObject jsonWO=portal_jsonArray.getJSONObject(i);
                                        SerialNoPojo serialNoPojo=new SerialNoPojo();
                                        serialNoPojo.setPkid(jsonWO.getString("pkid"));
                                        serialNoPojo.setActive(jsonWO.getInt("active"));
                                        serialNoPojo.setAdded_by(jsonWO.getString("added_by"));
                                        serialNoPojo.setSerial_no(jsonWO.getString("portal_id"));
                                        serialNoPojo.setPassword(jsonWO.getString("password"));
                                        serialNoPojo.setOrder_id(jsonWO.getString("order_id"));
                                        serialNoPojo.setInserttimestamp(jsonWO.getString("inserttimestamp"));
                                        portal_list.add(serialNoPojo);
                                    }
                                    serialNoAdapter_portal.notifyDataSetChanged();

                                }

                                if(jsonObject.has("gen_meter_reading")){
                                    JSONArray gen_meter_reading_jsonArray=jsonObject.getJSONArray("gen_meter_reading");
                                    for(int i=0;i<gen_meter_reading_jsonArray.length();i++){
                                        JSONObject jsonWO=gen_meter_reading_jsonArray.getJSONObject(i);
                                        MeterReadingPojo serialNoPojo=new MeterReadingPojo();
                                        serialNoPojo.setPkid(jsonWO.getString("pkid"));
                                        serialNoPojo.setActive(jsonWO.getInt("active"));
                                        serialNoPojo.setAdded_by(jsonWO.getString("added_by"));
                                        serialNoPojo.setReading_img(jsonWO.getString("meter_img"));
                                        serialNoPojo.setOrder_id(jsonWO.getString("order_id"));
                                        serialNoPojo.setInserttimestamp(jsonWO.getString("inserttimestamp"));
                                        gen_meter_read_list.add(serialNoPojo);
                                    }
                                    gen_meterReadingAdapter.notifyDataSetChanged();

                                }

                                if(jsonObject.has("net_meter_reading")){
                                    JSONArray net_meter_reading_jsonArray=jsonObject.getJSONArray("net_meter_reading");
                                    for(int i=0;i<net_meter_reading_jsonArray.length();i++){
                                        JSONObject jsonWO=net_meter_reading_jsonArray.getJSONObject(i);
                                        MeterReadingPojo serialNoPojo=new MeterReadingPojo();
                                        serialNoPojo.setPkid(jsonWO.getString("pkid"));
                                        serialNoPojo.setActive(jsonWO.getInt("active"));
                                        serialNoPojo.setAdded_by(jsonWO.getString("added_by"));
                                        serialNoPojo.setReading_img(jsonWO.getString("meter_img"));
                                        serialNoPojo.setOrder_id(jsonWO.getString("order_id"));
                                        serialNoPojo.setInserttimestamp(jsonWO.getString("inserttimestamp"));
                                        net_meter_read_list.add(serialNoPojo);
                                    }
                                    net_meterReadingAdapter.notifyDataSetChanged();

                                }

                            }
                            else{
                                Toast.makeText(currentActivity,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                }
        );
    }

    public void showDeleteAlertDialog(final String pkid,final String type,final int position){
        alertDialog=new AlertDialog.Builder(currentActivity)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deactivatePayment(pkid,type,position);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void deactivatePayment(String pkid, final String type, final int position){
        pDialog.show();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("pkid",pkid);
        jsonParserVolley.addParameter("type",type);
        jsonParserVolley.executeRequest(Request.Method.POST,WebUrl.DELETE_SERIAL_NO ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.hide();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                               if(type.equals(type_array[3])){
                                    panelno_list.get(position).setActive(2);
                                    serialNoAdapter.notifyItemChanged(position);
                                }
                                else if(type.equals(type_array[0])){
                                    portal_list.get(position).setActive(2);
                                    serialNoAdapter_portal.notifyItemChanged(position);
                                }
                                else if(type.equals(type_array[1])){
                                    gen_meter_read_list.get(position).setActive(2);
                                    gen_meterReadingAdapter.notifyItemChanged(position);
                                }
                                else if(type.equals(type_array[2])){
                                    net_meter_read_list.get(position).setActive(2);
                                    net_meterReadingAdapter.notifyItemChanged(position);
                                }
                                else if(type.equals(type_array[4])){
                                    inverter_list.get(position).setActive(2);
                                    serialNoAdapter_inverter.notifyItemChanged(position);
                                }
                                else if(type.equals(type_array[5])){
                                    wifi_stick_list.get(position).setActive(2);
                                    serialNoAdapter_wifi_stick.notifyItemChanged(position);
                                }
                                else if(type.equals(type_array[6])){
                                    gen_meter_serial_list.get(position).setActive(2);
                                    serialNoAdapter_gen_meter_serial.notifyItemChanged(position);
                                }
                                else if(type.equals(type_array[7])){
                                    net_meter_serial_list.get(position).setActive(2);
                                    serialNoAdapter_net_meter_serial.notifyItemChanged(position);
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
    private void createObj() {
        currentActivity=this;
        pDialog = new ProgressDialog(currentActivity);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        panelno_list=new ArrayList<>();
        inverter_list=new ArrayList<>();
        wifi_stick_list=new ArrayList<>();
        gen_meter_serial_list=new ArrayList<>();
        net_meter_serial_list=new ArrayList<>();
        gen_meter_read_list=new ArrayList<>();
        net_meter_read_list=new ArrayList<>();
        portal_list=new ArrayList<>();
        serialNoAdapter=new SerialNoAdapter(panelno_list, currentActivity,"panel");
        lv_panel.setAdapter(serialNoAdapter);
        serialNoAdapter.setOnItemClickListener(new SerialNoAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
            }
            @Override
            public void onLongItemClick(int position, View v) {
                showDeleteAlertDialog(panelno_list.get(position).getPkid(),type_array[3],position);
            }
        });

        serialNoAdapter_inverter=new SerialNoAdapter(inverter_list, currentActivity,"inverter");
        lv_inverter.setAdapter(serialNoAdapter_inverter);


        serialNoAdapter_wifi_stick=new SerialNoAdapter(wifi_stick_list, currentActivity,"wifi_stick");
        lv_wifi_stick.setAdapter(serialNoAdapter_wifi_stick);
        serialNoAdapter_wifi_stick.setOnItemClickListener(new SerialNoAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
            }
            @Override
            public void onLongItemClick(int position, View v) {
                showDeleteAlertDialog(wifi_stick_list.get(position).getPkid(),type_array[5],position);
            }
        });


        serialNoAdapter_gen_meter_serial=new SerialNoAdapter(gen_meter_serial_list, currentActivity,"get_meter_serial");
        lv_gen_meter_serial.setAdapter(serialNoAdapter_gen_meter_serial);
        serialNoAdapter_gen_meter_serial.setOnItemClickListener(new SerialNoAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
            }
            @Override
            public void onLongItemClick(int position, View v) {
                showDeleteAlertDialog(gen_meter_serial_list.get(position).getPkid(),type_array[6],position);
            }
        });

        serialNoAdapter_net_meter_serial=new SerialNoAdapter(net_meter_serial_list, currentActivity,"net_meter_serial");
        lv_net_meter_serial.setAdapter(serialNoAdapter_net_meter_serial);
        serialNoAdapter_net_meter_serial.setOnItemClickListener(new SerialNoAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
            }
            @Override
            public void onLongItemClick(int position, View v) {
                showDeleteAlertDialog(net_meter_serial_list.get(position).getPkid(),type_array[7],position);
            }
        });

        serialNoAdapter_portal=new SerialNoAdapter(portal_list, currentActivity,"portal");
        lv_portal.setAdapter(serialNoAdapter_portal);
        serialNoAdapter_portal.setOnItemClickListener(new SerialNoAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
            }
            @Override
            public void onLongItemClick(int position, View v) {
                showDeleteAlertDialog(portal_list.get(position).getPkid(),type_array[0],position);
            }
        });

        gen_meterReadingAdapter=new MeterReadingAdapter(gen_meter_read_list, currentActivity,"gen_meter_reading");
        lv_gen_meter_reading.setAdapter(gen_meterReadingAdapter);
        gen_meterReadingAdapter.setOnItemClickListener(new MeterReadingAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                showZoomImageDialog(WebUrl.BASE_URL+gen_meter_read_list.get(position).getReading_img());
            }
            @Override
            public void onLongItemClick(int position, View v) {
                showDeleteAlertDialog(gen_meter_read_list.get(position).getPkid(),type_array[1],position);
            }
        });

        net_meterReadingAdapter=new MeterReadingAdapter(net_meter_read_list, currentActivity,"net_meter_reading");
        lv_net_meter_reading.setAdapter(net_meterReadingAdapter);
        net_meterReadingAdapter.setOnItemClickListener(new MeterReadingAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                showZoomImageDialog(WebUrl.BASE_URL+net_meter_read_list.get(position).getReading_img());
            }
            @Override
            public void onLongItemClick(int position, View v) {
                showDeleteAlertDialog(net_meter_read_list.get(position).getPkid(),type_array[2],position);
            }
        });

    }
    private void generateId() {
        lv_panel=findViewById(R.id.lv_panel);
        gridLayoutManager=new LinearLayoutManager(currentActivity);
        lv_panel.setLayoutManager(gridLayoutManager);
        lv_panel.setHasFixedSize(true);
        lv_panel.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        lv_inverter=findViewById(R.id.lv_inverter);
        lv_inverter.setLayoutManager(new LinearLayoutManager(currentActivity));
        lv_inverter.setHasFixedSize(true);
        lv_inverter.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        lv_wifi_stick=findViewById(R.id.lv_wifi_stick);
        lv_wifi_stick.setLayoutManager(new LinearLayoutManager(currentActivity));
        lv_wifi_stick.setHasFixedSize(true);
        lv_wifi_stick.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        lv_gen_meter_serial=findViewById(R.id.lv_gen_meter_serial);
        lv_gen_meter_serial.setLayoutManager(new LinearLayoutManager(currentActivity));
        lv_gen_meter_serial.setHasFixedSize(true);
        lv_gen_meter_serial.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        lv_net_meter_serial=findViewById(R.id.lv_net_meter_serial);
        lv_net_meter_serial.setLayoutManager(new LinearLayoutManager(currentActivity));
        lv_net_meter_serial.setHasFixedSize(true);
        lv_net_meter_serial.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));


        lv_portal=findViewById(R.id.lv_portal);
        lv_portal.setLayoutManager(new LinearLayoutManager(currentActivity));
        lv_portal.setHasFixedSize(true);
        lv_portal.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        lv_gen_meter_reading=findViewById(R.id.lv_gen_meter_reading);
        lv_gen_meter_reading.setLayoutManager(new LinearLayoutManager(currentActivity));
        lv_gen_meter_reading.setHasFixedSize(true);
        lv_gen_meter_reading.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        lv_net_meter_reading=findViewById(R.id.lv_net_meter_reading);
        lv_net_meter_reading.setLayoutManager(new LinearLayoutManager(currentActivity));
        lv_net_meter_reading.setHasFixedSize(true);
        lv_net_meter_reading.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        btnAddPanel=findViewById(R.id.btnAddPanel);
        btnAddInverter=findViewById(R.id.btnAddInverter);
        btnAddWifiStick=findViewById(R.id.btnAddWifiStick);
        btnAddGenMeterSerial=findViewById(R.id.btnAddGenMeterSerial);
        btnAddNetMeterSerial=findViewById(R.id.btnAddNetMeterSerial);
        btnAddPortal=findViewById(R.id.btnAddPortal);
        btnAddGenMeterRead=findViewById(R.id.btnAddGenMeterRead);
        btnAddNetMeterRead=findViewById(R.id.btnAddNetMeterRead);
    }
    private void onclick(){
        btnAddPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSerailNoDlgFrag = new AddSerailNoDlgFrag("panel",workOrderPojo.getPkid(),panelno_list,serialNoAdapter);
                addSerailNoDlgFrag.show(getSupportFragmentManager(), TAG);
            }
        });
        btnAddInverter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSerailNoDlgFrag = new AddSerailNoDlgFrag("inverter",workOrderPojo.getPkid(),inverter_list,serialNoAdapter_inverter);
                addSerailNoDlgFrag.show(getSupportFragmentManager(), TAG);
            }
        });
        btnAddWifiStick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSerailNoDlgFrag = new AddSerailNoDlgFrag("wifi_stick",workOrderPojo.getPkid(),wifi_stick_list,serialNoAdapter_wifi_stick);
                addSerailNoDlgFrag.show(getSupportFragmentManager(), TAG);
            }
        });
        btnAddGenMeterSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSerailNoDlgFrag = new AddSerailNoDlgFrag("get_meter_serial",workOrderPojo.getPkid(),gen_meter_serial_list,serialNoAdapter_gen_meter_serial);
                addSerailNoDlgFrag.show(getSupportFragmentManager(), TAG);
            }
        });
        btnAddNetMeterSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSerailNoDlgFrag = new AddSerailNoDlgFrag("net_meter_serial",workOrderPojo.getPkid(),net_meter_serial_list,serialNoAdapter_net_meter_serial);
                addSerailNoDlgFrag.show(getSupportFragmentManager(), TAG);
            }
        });
        btnAddPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSerailNoDlgFrag = new AddSerailNoDlgFrag("portal",workOrderPojo.getPkid(),portal_list,serialNoAdapter_portal);
                addSerailNoDlgFrag.show(getSupportFragmentManager(), TAG);
            }
        });
        btnAddGenMeterRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMeterReadingDlgFrag  addMeterReadingDlgFrag = new AddMeterReadingDlgFrag("gen_meter_reading",workOrderPojo.getPkid(),gen_meter_read_list,gen_meterReadingAdapter);
                addMeterReadingDlgFrag.show(getSupportFragmentManager(), TAG);
            }
        });
        btnAddNetMeterRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMeterReadingDlgFrag  addMeterReadingDlgFrag = new AddMeterReadingDlgFrag("net_meter_reading",workOrderPojo.getPkid(),net_meter_read_list,net_meterReadingAdapter);
                addMeterReadingDlgFrag.show(getSupportFragmentManager(), TAG);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if(fragment!=null)
                fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent(currentActivity, SearchOrderActivity.class);
                intent.putExtra("modul","after_sale");
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(currentActivity, SearchOrderActivity.class);
        intent.putExtra("modul","after_sale");
        startActivity(intent);
        finish();
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
