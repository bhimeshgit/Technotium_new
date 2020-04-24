package com.technotium.technotiumapp.config;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.technotium.technotiumapp.LoginActivity;
import com.technotium.technotiumapp.R;


import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ApplicationGlobal extends Application {
    JSONObject json_response=null;
    public static String string_response=null;
    HashMap<String,String> parameters;

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    public JSONObject callApi(HashMap<String,String> param, String apiUrl, Context context){
        parameters=param;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("iss",json_response.toString());
                        json_response=response;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("iss",error.getMessage());
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                return parameters;
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getMyInstance(context).addToRequestQueue(jsonObjReq);
        return json_response;
    }

    public String callApiStringReq(HashMap<String,String> param, String apiUrl, Context context){
        SessionManager.getMyInstance(context).progressShow();
        parameters=param;
        StringRequest stringRequest=new StringRequest(Request.Method.POST,WebUrl.LOGIN_URL , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    string_response=response;
                }
                catch (Exception e) {
                    Log.e("Volley", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("iss",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return  parameters;
            }
        };
        MySingleton.getMyInstance(context).addToRequestQueue(stringRequest);
        SessionManager.getMyInstance(context).progressHide();
        return string_response;
    }

    public static boolean checkInternetConenction(Context context){
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        // Check for network connections
        if (connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }//checkInternetConenction

    public static void shownointernetconnectiondialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dg_nointernet);
        TextView tvretry = (TextView) dialog.findViewById(R.id.tvretry);

        tvretry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   new Utils(context).Goto(context, SplashActivity.class, R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        dialog.show();
    }//shownointernetconnectiondialog



}
