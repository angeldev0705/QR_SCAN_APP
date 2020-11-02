package com.example.rlqrscanner;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ScannerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_view);

//        QRCodeScannerFragment scanner = (QRCodeScannerFragment)getFragmentManager().findFragmentById(R.id.scanner_fragment);

    }
}