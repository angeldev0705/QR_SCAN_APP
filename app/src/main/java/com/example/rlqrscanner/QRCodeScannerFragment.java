package com.example.rlqrscanner;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import androidx.fragment.app.Fragment;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCodeScannerFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    ProgressDialog progressDialog;
    String[] QRData;

    String serverURL = "https://rldm.jp/api/check_reserveId";
    String serverToken = "oVqEaPbwEGjcxgHGG9RtaBH0lKL3VA0rBbnXRq/UkjNeHAk6478MyczKIA5nOmnhnWmb2y8gTjbL1Px6qBSCgA==";

    JSONObject jsonObject;
    String date = "";
    String startTime = "";
    String endTime = "";

    int count = 0;
    CountDownTimer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mScannerView = new ZXingScannerView(getActivity());
        mScannerView.setAutoFocus(true);
        mScannerView.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));
        mScannerView.setAspectTolerance(0.5f);
        mScannerView.setSoundEffectsEnabled(true);


        timer = new CountDownTimer(100000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("AAAAAAAAAAAA", "BBBBBBBBB  " + String.valueOf(count));
                count ++;
                if (count >= 90) {
                    this.cancel();
                    getActivity().finish();
                }
            }
            @Override
            public void onFinish() {
            }
        };
        timer.start();
        return mScannerView;
    }
    private void noteservResultOKActivity()
    {
        Intent reservResultOKActivity = new Intent(getActivity(), ReservResultOKActivity.class);
        reservResultOKActivity.putExtra("reserveID", QRData[0]);
        reservResultOKActivity.putExtra("Date", QRData[1]);
        reservResultOKActivity.putExtra("startTime", QRData[2]);
        reservResultOKActivity.putExtra("endTime", endTime);
        reservResultOKActivity.putExtra("eventID", QRData[4]);
        reservResultOKActivity.putExtra("pattern", QRData[5]);
        reservResultOKActivity.putExtra("GuestCount", QRData[6]);
        startActivity(reservResultOKActivity);
    }
    @Override
    public void handleResult(Result rawResult)
    {
        count = 0;
        if (rawResult == null || rawResult.getText().isEmpty())
        {
            Toast.makeText(getContext(), "No Data Found!", Toast.LENGTH_LONG).show();
        }
        else
        {
            Log.i("QR result", rawResult.getText());

            QRData = rawResult.getText().split("-");
            //noteservResultOKActivity();
           checkReservationIDFromServer();

            timer.cancel();
        }
    }

    private void goToReservResultOKActivity()
    {
        Intent reservResultOKActivity = new Intent(getActivity(), ReservResultOKActivity.class);
        reservResultOKActivity.putExtra("reserveID", QRData[0]);
        reservResultOKActivity.putExtra("Date", date);
        reservResultOKActivity.putExtra("startTime", startTime);
        reservResultOKActivity.putExtra("endTime", endTime);
        reservResultOKActivity.putExtra("eventID", QRData[4]);
        reservResultOKActivity.putExtra("pattern", QRData[5]);
        reservResultOKActivity.putExtra("GuestCount", QRData[6]);
        startActivity(reservResultOKActivity);
    }

    private void goToReservResultErrorActivity()
    {

        Intent reservResultErrorActivity = new Intent(getActivity(), ReservResultErrorActivity.class);
        reservResultErrorActivity.putExtra("reserveID", QRData[0]);
        reservResultErrorActivity.putExtra("Date", date);
        reservResultErrorActivity.putExtra("startTime", startTime);
        reservResultErrorActivity.putExtra("endTime", endTime);
        startActivity(reservResultErrorActivity);
    }

    private void checkReservationIDFromServer()
    {
        progressDialog = new ProgressDialog(getActivity() , R.style.MyAlertDialogStyle);
        progressDialog.setMessage("確認中...");
        progressDialog.show();
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(getContext());


        StringRequest myReq = new StringRequest(Request.Method.POST, serverURL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                if(response.isEmpty() || response == null)
                {
                    return;
                }

                Log.i("response", response);

                try
                {
                    jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    Log.i("success", String.valueOf(success));

                    if (success == true)
                    {
                        JSONObject data = jsonObject.getJSONObject("data");
                        date = data.getString("date");
                        if (date.equals("111")) date = "1101";
                        if (date.equals("116")) date = "1106";
                        if (date.equals("117")) date = "1107";
                        startTime = data.getString("start_time");
                        endTime = data.getString("finish_time");

                        Log.i("data", data.toString());
                      // progressDialog.dismiss();
                        goToReservResultOKActivity();


                    }
                    else if (success == false)
                    {
                        String message = jsonObject.getString("msg");
                        Log.i("message", message);

                        if (message.equals("no exist"))
                        {

                            goToReservResultErrorActivity();
                            progressDialog.dismiss();
                        }
                    }
                }
                catch (JSONException e)
                {
                    progressDialog.dismiss();
                    Log.i("JSON Exception : ", e.getMessage());
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressDialog.dismiss();

                        if (error instanceof TimeoutError)
                        {
                            Toast.makeText(getActivity(), "Connection Timed Out Please Try Again", Toast.LENGTH_SHORT).show();

                        } else if (error instanceof NetworkError)
                        {
                            Toast.makeText(getActivity(), "Something Wrong On Your Network", Toast.LENGTH_SHORT).show();
                        }
                        else if (error instanceof AuthFailureError || error instanceof ServerError)
                        {
                            Toast.makeText(getActivity(), "Sorry Something Wrong On The Server", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reserve_id", QRData[0]);
                params.put("token", serverToken);
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        myReq.setShouldCache(false);
        requestQueue.add(myReq);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }
}