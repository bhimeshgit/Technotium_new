package com.technotium.technotiumapp.docscan.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.technotium.technotiumapp.BuildConfig;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.docscan.adapter.DocAdapter;
import com.technotium.technotiumapp.docscan.model.DocPojo;
import com.technotium.technotiumapp.payment.adapter.PaymentAdapter;
import com.technotium.technotiumapp.payment.model.PaymentPojo;
import com.technotium.technotiumapp.workorder.activity.SearchOrderActivity;
import com.technotium.technotiumapp.workorder.activity.WorkOrderActivity;
import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ViewAllDocsActivity extends AppCompatActivity {
    RecyclerView lv_doc_list;
    ViewAllDocsActivity currentActivity;
    ArrayList<DocPojo> docList;
    DocAdapter adapter;
    Button btnAddNew;
    RecyclerView.LayoutManager layoutManager;
    private Dialog zoomable_image_dialog;
    SubsamplingScaleImageView imageView;
    AlertDialog alertDialog;
    WorkOrderPojo workOrderPojo;
    ProgressDialog pDialog;
    Bitmap mbitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_docs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        if(intent!=null){
            if(intent.getSerializableExtra("orderData")!=null){
                workOrderPojo=(WorkOrderPojo)intent.getSerializableExtra("orderData");
                init();
            }
        }
    }

    public void init(){
        currentActivity= ViewAllDocsActivity.this;
        docList=new ArrayList<>();
        lv_doc_list=findViewById(R.id.lv_docList);

        btnAddNew=findViewById(R.id.btnAddNew);
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(currentActivity,UploadDocActivity.class);
                intent.putExtra("orderData",workOrderPojo);
                startActivity(intent);
                finish();
            }
        });
        pDialog = new ProgressDialog(currentActivity);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        getAllDocs();
    }

    private void getAllDocs() {
        pDialog.show();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("order_id", workOrderPojo.getPkid());
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.GET_ALL_DOC_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonWO=jsonArray.getJSONObject(i);
                                    DocPojo docPojo=new DocPojo();
                                    docPojo.setDoc_path(jsonWO.getString("doc_path"));
                                    docPojo.setDoc_name(jsonWO.getString("doc_name"));
                                    docPojo.setOrder_id(jsonWO.getString("order_id"));
                                    docPojo.setDoc_id(jsonWO.getString("pkid"));
                                    docPojo.setActive(jsonWO.getString("active"));
                                    docList.add(docPojo);
                                }

                                layoutManager=new GridLayoutManager(currentActivity,2);
                                lv_doc_list.setLayoutManager(layoutManager);
                                lv_doc_list.setHasFixedSize(true);
                                adapter=new DocAdapter(docList, currentActivity);
                                lv_doc_list.setAdapter(adapter);
                                adapter.setOnItemClickListener(new DocAdapter.ClickListener() {
                                    @Override
                                    public void onItemClick(int position, View v) {
                                        showZoomImageDialog(WebUrl.BASE_URL+docList.get(position).getDoc_path());
                                    }

                                    @Override
                                    public void onLongItemClick(int position, View v) {
                                        showDeleteAlertDialog(docList.get(position).getDoc_id());
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

    public void deactivateDoc(String docId){
        pDialog.show();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("docId",docId);
        jsonParserVolley.executeRequest(Request.Method.POST,WebUrl.DELETE_DOC_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success=jsonObject.getInt("success");
                            if(success==1){
                                Toast.makeText(currentActivity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(currentActivity,ViewAllDocsActivity.class);
                                intent.putExtra("orderData",workOrderPojo);
                                startActivity(intent);
                                finish();
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

    public void showDeleteAlertDialog(final String docId){
        alertDialog=new AlertDialog.Builder(currentActivity)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this document?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deactivateDoc(docId);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(currentActivity, SearchOrderActivity.class);
        intent.putExtra("modul","docscan");
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent(currentActivity, SearchOrderActivity.class);
                intent.putExtra("modul","docscan");
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
