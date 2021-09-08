package com.technotium.technotiumapp.workorder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Request;
import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.JsonParserVolley;
import com.technotium.technotiumapp.config.SessionManager;
import com.technotium.technotiumapp.config.WebUrl;
import com.technotium.technotiumapp.payment.adapter.PaymentAdapter;
import com.technotium.technotiumapp.payment.model.PaymentPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WorkOrderPdfReportActivity extends AppCompatActivity {

    WorkOrderPdfReportActivity currentActivity;
    String order_id = "";
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order_pdf_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        currentActivity=this;
        if(getIntent().getStringExtra("order_id")!=null){
            order_id = getIntent().getStringExtra("order_id");
//            WebView webView = (WebView) findViewById(R.id.webview);
//            webView.getSettings().setJavaScriptEnabled(true);
//            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//            webView.loadUrl("http://docs.google.com/gview?embedded=true&url="+WebUrl.WORK_ORDER_REPORT+getIntent().getStringExtra("pdf_name"));//+WebUrl.WORK_ORDER_REPORT);
            //finish();
        }
        getPdfReport();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent(currentActivity, SearchOrderActivity.class);
                intent.putExtra("modul","workorder");
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
        intent.putExtra("modul","workorder");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void showProgressLoad(){
        pDialog = new ProgressDialog(currentActivity);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
    }
    private void hideProgressLoad(){
        if (pDialog != null){
            pDialog.dismiss();
        }
    }

    public void getPdfReport(){
        showProgressLoad();
        final JsonParserVolley jsonParserVolley = new JsonParserVolley(currentActivity);
        jsonParserVolley.addParameter("order_id", order_id);

        jsonParserVolley.executeRequest(Request.Method.POST, WebUrl.GET_WO_PDF_REPORT_NAME ,new JsonParserVolley.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        Log.d("iss","response="+response);
                        try {
//                            WebView webView = (WebView) findViewById(R.id.webview);
//                            webView.getSettings().setJavaScriptEnabled(true);
//                            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//                            webView.loadUrl("https://docs.google.com/viewerng/viewer?url="+WebUrl.WORK_ORDER_REPORT+response.trim());//+WebUrl.WORK_ORDER_REPORT);

                            Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(WebUrl.WORK_ORDER_REPORT+response.trim()));
                            startActivity(intent);

//                            Intent intent2=new Intent(currentActivity, SearchOrderActivity.class);
//                            intent2.putExtra("modul","workorder");
//                            startActivity(intent2);
                            finish();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        hideProgressLoad();
                    }
                }
        );
    }

}
