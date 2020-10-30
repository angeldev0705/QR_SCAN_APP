package com.example.rlqrscanner;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ReservResultErrorActivity extends AppCompatActivity {

    TextView reserveIDTV;
    TableLayout tableLayout;
    ImageView scanAgainIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserv_result_error);

        reserveIDTV = (TextView) findViewById(R.id.reserve_id_tv);
        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        scanAgainIV = (ImageView) findViewById(R.id.scan_again_iv);

        showQRData();

        scanAgainIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent goToScanningViewActivity = new Intent(ReservResultErrorActivity.this, ScannerViewActivity.class);
                startActivity(goToScanningViewActivity);
            }
        });
    }

    private void showQRData()
    {
        // Table View
        View tableLayoutRootView = tableLayout.getRootView();

        // Table Rows
        TableRow secondRow = (TableRow) tableLayoutRootView.findViewById(R.id.second_row);

        // Table Rows View
        View secondRowView = secondRow.getRootView();

        // Cell view second row
        TextView dateTV = (TextView) secondRowView.findViewById(R.id.date_tv);
        TextView startTimeTV = (TextView) secondRowView.findViewById(R.id.start_time_tv);
        TextView endTimeTV = (TextView) secondRowView.findViewById(R.id.end_time_tv);

        // Setting QR data
        reserveIDTV.setText(getIntent().getStringExtra("reserveID"));
        if (getIntent().getStringExtra("Date").isEmpty() || getIntent().getStringExtra("Date").equals(null))
        {
            dateTV.setText(getIntent().getStringExtra("Date"));
        }
        else
        {
            dateTV.setText(getIntent().getStringExtra("Date").substring(0,2) + "-" + getIntent().getStringExtra("Date").substring(2,4));
        }
        startTimeTV.setText(getIntent().getStringExtra("startTime"));
        endTimeTV.setText(getIntent().getStringExtra("endTime"));
    }
}