package com.technotium.technotiumapp.workorder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

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
        WebView webView = (WebView) findViewById(R.id.webview);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=http://192.168.0.5/technotium/demo.pdf");//+WebUrl.WORK_ORDER_REPORT);

        //myWebView.loadUrl("https://www.google.com/");
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
}
