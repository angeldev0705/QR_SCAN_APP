package com.example.rlqrscanner;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class ScanCheckActivity extends AppCompatActivity {

    ImageView cameraIV;
    TextView scanQRCodeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_check);

        cameraIV = (ImageView) findViewById(R.id.camera_iv);
        scanQRCodeTV = (TextView) findViewById(R.id.scan_qr_code_tv);

        cameraIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                requestCameraPermission();
            }
        });
        scanQRCodeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                requestCameraPermission();
            }
        });
    }

    private void requestCameraPermission()
    {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener()
                {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        Intent showScannerView = new Intent(ScanCheckActivity.this, ScannerViewActivity.class);
                        startActivity(showScannerView);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response)
                    {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied())
                        {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token)
                    {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public void showSettingsDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    // Navigating user to app settings
    public void openSettings()
    {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}