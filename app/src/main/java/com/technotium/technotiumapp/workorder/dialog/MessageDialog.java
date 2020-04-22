package com.technotium.technotiumapp.workorder.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageDialog extends DialogFragment {
    private Context mContext;
    private Dialog mDialog;
    private EditText txtMsg;
    private Button btnSend;
    private ProgressDialog pDialog;
    private String msg,mobile;

    public MessageDialog(String mobile){
        this.mobile=mobile;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.setContentView(R.layout.dlg_send_msg);
        init();
        return mDialog;
    }
    private void init() {
        txtMsg=mDialog.findViewById(R.id.txtMsg);
        btnSend=mDialog.findViewById(R.id.btnSend);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg();
            }
        });
    }

    private void sendMsg() {
        if(txtMsg.getText().toString().trim().length()<=0){
            Toast.makeText(mContext,"Please enter the message",Toast.LENGTH_SHORT).show();
            return;
        }
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(mContext);
        pDialog.show();
        jsonParserVolley.addParameter("mobile", mobile);
        jsonParserVolley.addParameter("message", txtMsg.getText().toString());
        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.SEND_SMS_URL ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        pDialog.dismiss();
                        try {
//                            JSONObject jsonObject=new JSONObject(response);
//                            int success=jsonObject.getInt("success");
//                            if(success==1){
//                                Toast.makeText(mContext,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
//                            }
//                            else{
//                                Toast.makeText(mContext,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
//                            }
                                Toast.makeText(mContext,"Message sent successfully",Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}