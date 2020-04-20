package com.technotium.technotiumapp.after_sales.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.technotium.technotiumapp.R;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class ScannerActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener  {

    private BarcodeReader barcodeReader;
    private static final int BARCODE_SCANNER_RESULT_CODE=202;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);
    }

    @Override
    public void onScanned(Barcode barcode) {
        barcodeReader.playBeep();
        Intent intent=new Intent();
        intent.putExtra("barcode_value",barcode.displayValue);
        setResult(BARCODE_SCANNER_RESULT_CODE,intent);
        finish();
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getApplicationContext(), "Camera permission denied!", Toast.LENGTH_LONG).show();
    }
}
