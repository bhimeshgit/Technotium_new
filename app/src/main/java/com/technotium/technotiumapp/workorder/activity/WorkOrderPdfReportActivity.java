package com.technotium.technotiumapp.workorder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.WebUrl;

public class WorkOrderPdfReportActivity extends AppCompatActivity {

    WorkOrderPdfReportActivity currentActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order_pdf_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        currentActivity=this;
        if(getIntent().getStringExtra("pdf_name")!=null){
            WebView webView = (WebView) findViewById(R.id.webview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.loadUrl("http://docs.google.com/gview?embedded=true&url="+WebUrl.WORK_ORDER_REPORT+getIntent().getStringExtra("pdf_name"));//+WebUrl.WORK_ORDER_REPORT);
            //finish();
        }
        else {
            Toast.makeText(currentActivity,"Report is not availble. Try to generate by updating Work Order",Toast.LENGTH_SHORT);
            finish();
        }

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
}
