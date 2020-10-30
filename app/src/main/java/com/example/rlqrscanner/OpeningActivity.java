package com.example.rlqrscanner;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class OpeningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        goToScanCheckActivity();
    }

    private void goToScanCheckActivity()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent showCheckScreen = new Intent(OpeningActivity.this, ScanCheckActivity.class);
                startActivity(showCheckScreen);
                finish();
            }
        }, 1500);
    }
}