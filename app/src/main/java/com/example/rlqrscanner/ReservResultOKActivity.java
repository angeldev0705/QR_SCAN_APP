package com.example.rlqrscanner;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;

public class ReservResultOKActivity extends AppCompatActivity {

    TextView reserveIDTV;
    TableLayout tableLayout;
    ImageView scanAgainIV;
    ImageView backImage;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserv_result_ok);

        reserveIDTV = (TextView) findViewById(R.id.reserve_id_tv);
        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        scanAgainIV = (ImageView) findViewById(R.id.scan_again_iv);
        backImage  = (ImageView) findViewById(R.id.backimage);
        showQRData();

        scanAgainIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent goToScanningViewActivity = new Intent(ReservResultOKActivity.this, ScannerViewActivity.class);
                startActivity(goToScanningViewActivity);
            }
        });
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent goToScanningViewActivity = new Intent(ReservResultOKActivity.this, ScannerViewActivity.class);
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
        TableRow thirdRow = (TableRow) tableLayoutRootView.findViewById(R.id.third_row);
        TableRow fourthRow = (TableRow) tableLayoutRootView.findViewById(R.id.fourth_row);

        // Table Rows View
        View secondRowView = secondRow.getRootView();
        View thirdRowView = thirdRow.getRootView();
        View fourthRowView = fourthRow.getRootView();

        // Cell view second row
        TextView dateTV = (TextView) secondRowView.findViewById(R.id.date_tv);
        TextView startTimeTV = (TextView) secondRowView.findViewById(R.id.start_time_tv);
        TextView endTimeTV = (TextView) secondRowView.findViewById(R.id.end_time_tv);

        // Cell view third row
        TextView guestCountTV = (TextView) thirdRowView.findViewById(R.id.guest_count_tv);

        // Cell view fourth row
        TextView eventIDTV = (TextView) fourthRowView.findViewById(R.id.event_id_tv);
        TextView empty2TV = (TextView) fourthRowView.findViewById(R.id.empty2_tv);
        TextView empty3TV = (TextView) fourthRowView.findViewById(R.id.empty3_tv);

        // Setting QR data
        reserveIDTV.setText(getIntent().getStringExtra("reserveID"));
        String date = getIntent().getStringExtra("Date");
        if (date != null && date.length() >= 4) dateTV.setText(date.substring(0,2) + "月" + date.substring(2,4)+ "日" );
        startTimeTV.setText(getIntent().getStringExtra("startTime"));
        //dateTV.setText(getIntent().getStringExtra("date_tv"));
        //endTimeTV.setText(getIntent().getStringExtra("endTime"));
        guestCountTV.setText(getIntent().getStringExtra("GuestCount"));
        eventIDTV.setText(getIntent().getStringExtra("eventID"));

    }
}