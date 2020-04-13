package com.technotium.technotiumapp.after_sales.dialog;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.technotium.technotiumapp.BuildConfig;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.after_sales.Activities.AfterSalesActivity;
import com.technotium.technotiumapp.after_sales.adapter.SerialNoAdapter;
import com.technotium.technotiumapp.after_sales.model.SerialNoPojo;
import com.technotium.technotiumapp.config.ImageProcessing;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.payment.activity.AddPaymentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddSerailNoDlgFrag extends DialogFragment {
    public static final String TAG = AddSerailNoDlgFrag.class.getSimpleName();
    private Context mContext;
    private Dialog mDialog;
    private EditText txtSerialNo,txtDate,txtPass;
    private TextView txtTitle;
    String OrderDate,type,order_id;
    String orderToset;
    private Button btnAdd;
    private ProgressDialog pDialog;
    private ArrayList<SerialNoPojo> panelno_list;
    SerialNoAdapter serialNoAdapter;
    private TextView txtviewSerial;

    private LinearLayout portal_lay,upload_nac_img,serial_no_lay;

    //upload Image
    Button btnBrowse_AttachDocument,btnCapture_AttachDocument;
    ImageView imgPreview_AttachDocument ;
    Button btnRotate_AttachDocument ,btnRotate1_AttachDocument,btnCrop_AttachDocument,btnSave_AttachDocument ;
    AddPaymentActivity currentActivity;
    public static final int BROWSE_IMAGE_REQUEST_CODE=101,CAMERA_CAPTURE_IMAGE_REQUEST_CODE=102,MEDIA_TYPE_IMAGE = 1,CROP_IMAGE_REQUEST_CODE = 4;;
    private static Uri fileUri;
    static String filePath = "",filename = "",IMAGE_DIRECTORY_NAME = "Technotium",encodedPhotoString="";
    Bitmap bitmap;
    public AddSerailNoDlgFrag(String type, String order_id,  ArrayList<SerialNoPojo> panelno_list,SerialNoAdapter serialNoAdapter) {
        this.type=type;
        this.order_id=order_id;
        this.panelno_list=panelno_list;
        this.serialNoAdapter=serialNoAdapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.setContentView(R.layout.dlg_add_serial_no);
        init();
        return mDialog;
    }

    private void init() {
        txtSerialNo=mDialog.findViewById(R.id.txtSerialNo);
        txtDate=mDialog.findViewById(R.id.txtDate);
        txtPass=mDialog.findViewById(R.id.txtPass);
        btnAdd=mDialog.findViewById(R.id.btnAdd);
        txtviewSerial=mDialog.findViewById(R.id.txtviewSerial);
        txtTitle=mDialog.findViewById(R.id.txtTitle);
        portal_lay=mDialog.findViewById(R.id.portal_lay);
        upload_nac_img=mDialog.findViewById(R.id.upload_nac_img);
        serial_no_lay=mDialog.findViewById(R.id.serial_no_lay);

        btnBrowse_AttachDocument=mDialog.findViewById(R.id.btnBrowse_AttachDocument);
        btnCapture_AttachDocument=mDialog.findViewById(R.id.btnCapture_AttachDocument);
        imgPreview_AttachDocument=mDialog.findViewById(R.id.imgPreview_AttachDocument);
        btnRotate_AttachDocument=mDialog.findViewById(R.id.btnRotate_AttachDocument) ;
        btnRotate1_AttachDocument=mDialog.findViewById(R.id.btnRotate1_AttachDocument)  ;
        btnCrop_AttachDocument=mDialog.findViewById(R.id.btnCrop_AttachDocument) ;
        btnSave_AttachDocument=mDialog.findViewById(R.id.btnSave_AttachDocument);


        if(type.equals("portal")){
            portal_lay.setVisibility(View.VISIBLE);
            txtviewSerial.setText("Portal ID");
            txtTitle.setText("Portal Details");
        }
        else if(type.equals("gen_meter_reading") || type.equals("net_meter_reading")){
            serial_no_lay.setVisibility(View.GONE);
            upload_nac_img.setVisibility(View.VISIBLE);
            if(type.equals("gen_meter_reading")){ txtTitle.setText("Generation Meter Reading");}
            if(type.equals("net_meter_reading")){txtTitle.setText("Net Meter Reading"); }
        }
        else if(type.equals("panel")){txtTitle.setText("Panel Details"); }
        else if(type.equals("inverter")){txtTitle.setText("Inverter Details"); }
        else if(type.equals("wifi_stick")){txtTitle.setText("Wifi Stick Details"); }
        else if(type.equals("get_meter_serial")){ txtTitle.setText("Generation Meter Serial No.");}
        else if(type.equals("net_meter_serial")){txtTitle.setText("Net Meter Serial No."); }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addSerialNo();

            }
        });
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal=Calendar.getInstance();
        Date dt=cal.getTime();
        orderToset=sdf.format(dt);
        txtDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(dt));
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFunction();
            }
        });
    }

    private void addSerialNo() {
        showProgressDialog();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(getActivity());
        jsonParserVolley.addParameter("serial_no",txtSerialNo.getText().toString());
        jsonParserVolley.addParameter("add_date",orderToset);
        jsonParserVolley.addParameter("userid", SessionManager.getMyInstance(getActivity()).getEmpid());
        jsonParserVolley.addParameter("type",type);
        jsonParserVolley.addParameter("order_id",order_id);
        if(type.equals("portal")){
            jsonParserVolley.addParameter("password",txtPass.getText().toString());
        }
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.ADD_PANEL_SERIAL_NO ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                int pkid=jsonObject.getInt("pkid");
                                Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                SerialNoPojo serialNoPojo=new SerialNoPojo();
                                serialNoPojo.setSerial_no(txtSerialNo.getText().toString());
                                serialNoPojo.setActive(1);
                                serialNoPojo.setInserttimestamp(orderToset);
                                serialNoPojo.setAdded_by(SessionManager.getMyInstance(getActivity()).getEmpName());
                                serialNoPojo.setOrder_id(order_id);
                                serialNoPojo.setPkid(pkid+"");
                                if(type.equals("portal")){
                                    serialNoPojo.setPassword(txtPass.getText().toString());
                                }
                                panelno_list.add(serialNoPojo);
                                serialNoAdapter.notifyDataSetChanged();
                                mDialog.dismiss();
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
                txtDate.setText(date);
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

    public void showProgressDialog(){
        pDialog.show();
    }



}
