package com.example.page1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Intermediate extends AppCompatActivity {

    RequestQueue requestQueue;
    String ISDCOde = " ";
    DatabaseOperation databaseOperation;
    ProgressDialog pd ;
    TextView tvName,tvStatus,tvSubmit;
    SessionManager sf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate);
        databaseOperation = new DatabaseOperation(getApplicationContext());

        tvName = findViewById(R.id.tv_isd_name);
        tvStatus = findViewById(R.id.tv_kyc_status);
        tvSubmit = findViewById(R.id.tv_isd_btn);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        ISDCOde = databaseOperation.retrieveUserCode();
        sf = new SessionManager(getApplicationContext());


        if (sf.isAgreement()) {
            Intent intent = new Intent(Intermediate.this, DashboardAcitivity.class);
            startActivity(intent);
            finish();
        } else {

       // }

        try {
            pd = new ProgressDialog(Intermediate.this);

            pd.setTitle("Request sending");
            pd.setMessage("Please wait to get details..");
            pd.show();
            //  String url = "http://ovotapplive.poshsmetal.com/index.asmx/GetISDDetails";
//            String url = "http://ovotappdev.poshsmetal.com/index.asmx/GetISDDetails";
            String url = Url.GET_ISD_URL;
            /////////////////////////////////////////
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String userId;

                    String message;

                    try {
                        pd.dismiss();
                        JSONObject j = new JSONObject(response);
                        // CustomerResult = j.getJSONArray("customers");
                        JSONArray customerdetail = j.getJSONArray("ISDDt");
                        for (int i = 0; i < customerdetail.length(); i++) {
                            JSONObject isdDetails = customerdetail.getJSONObject(i);
                            //userId = userd.getString("UserId");
                            message = isdDetails.getString("message");

                            if (message.equals("Success")) {

                                String name = isdDetails.getString("Name");
                                String kycStatus = isdDetails.getString("KYCApproved");

                                tvName.setText("Dear " + name);

                                if (kycStatus.equals("Pending")) {
                                    tvStatus.setText("\n" +
                                            "Your KYC Details are submitted and are currently pending for Approval.\n" +
                                            "\n" +
                                            "Once Approved, you will be taken to the Agreement Section for agreement");
                                    tvStatus.setTextColor(Color.parseColor("#FEA95E"));
                                    tvStatus.setMovementMethod(new ScrollingMovementMethod());
                                    tvSubmit.setVisibility(View.INVISIBLE);

//                                    Intent approveIntent = new Intent(Intermediate.this,Agreement.class);
//                                    startActivity(approveIntent);

                                } else if (kycStatus.equals("Approved")) {

                                    tvStatus.setText("Your KYC Details are successfully Approved. Please click on below on the Agreement Section and complete your agreement.");
                                    tvStatus.setTextColor(Color.parseColor("#136d15"));
                                    tvStatus.setMovementMethod(new ScrollingMovementMethod());
                                    tvSubmit.setText("Agreement Section");

                                    tvSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent approveIntent = new Intent(Intermediate.this, Agreement.class);
                                            startActivity(approveIntent);
                                            finish();
                                        }
                                    });

                                } else if (kycStatus.equals("Rejected")) {
                                    String reason = isdDetails.getString("Reason");
                                    tvStatus.setText(
                                            "KYC Details submitted by you are rejected for the reason :- " + reason + ".\n" +
                                                    "\n" +
                                                    "Please provide your KYC Details once again with correct details and attachments.");

                                    tvSubmit.setText("Re-enter your KYC details");
                                    tvSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent reIntent = new Intent(Intermediate.this, ISDReg.class);
                                            startActivity(reIntent);
                                            //finish();
                                        }
                                    });

                                } else {
                                    tvStatus.setText("Welcome to ISD Registration. You are requested herewith to kindly provide KYC Details, Proof Document and register your agreement.\n" +
                                            "\n" +
                                            "Kindly go to KYC Data Entry and provide your KYC Details along with supporting Document Proofs");
                                    tvStatus.setTextColor(Color.parseColor("#01579b"));
                                    tvStatus.setMovementMethod(new ScrollingMovementMethod());
                                    tvSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(Intermediate.this, ISDReg.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }


                                pd.dismiss();


                            } else {
                                pd.dismiss();
                            }

                            ////////////////////////

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        pd.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    new VolleyError();
                    // Toast.makeText(getActivity(), "error" + error.toString() + error.networkResponse, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("ISDCode", ISDCOde);

                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);


        } catch (Exception e) {
            Log.e("Register Activity ", ": Exception while " + "Sending data to server : " + e.getMessage());
            Toast.makeText(Intermediate.this, "Oops....Registration Failed Try Later.", Toast.LENGTH_LONG).show();
        }

    }
    }
}