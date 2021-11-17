package com.example.page1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.page1.IncentiveAdapter;
import com.example.page1.IncentiveModel;
import com.example.page1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class IncentiveActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    IncentiveAdapter adapter;
    TextView txtfrmDate, txttoDate, userName;
    String incentiveDatabaseName = Environment.getExternalStorageDirectory().getPath() + "/TWAmstradDB/DDIncentiveData.db";
    String[]  retrieveIncentiveData;
    private ImageView LogOut,Home;
    private Button submit;
    private int year;
    private int month;
    private int day;
    private int toyear;
    private int tomonth;
    private int today;
    ProgressDialog pd;
    private static final String PREF_NAME = "AndroidHivePref";
    JsonArrayRequest jsonarrayRequest;
    RequestQueue requestQueue;
    GlobalVariable gbl;
    DatabaseOperation databaseOperation;
    String description = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incentive);
        pd = new ProgressDialog(IncentiveActivity.this);
        gbl = (GlobalVariable) IncentiveActivity.this.getApplicationContext();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        databaseOperation = new DatabaseOperation(getApplicationContext());
        txtfrmDate = findViewById(R.id.fromdateId);
        txttoDate = findViewById(R.id.toDateId);
        recyclerView=findViewById(R.id.incentive_recyclerview);
        LogOut = findViewById(R.id.logout);
        Home = findViewById(R.id.ibv_icon3);
        submit= findViewById(R.id.incentiveSubmit);
        userName = findViewById(R.id.tv_name);
        String incentiveDatabaseName = Environment.getExternalStorageDirectory().getPath() + "/TWAmstradDB/DDIncentiveData.db";
         //String userId = gbl.getUserId();
        String userId = databaseOperation.retrieveUserCode();
        userName.setText(userId);
        setRecycerview();

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences =getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                finish();

                Intent log_intent = new Intent(IncentiveActivity.this,LoginActivity.class);
                startActivity(log_intent);
                finish();
            }
        });

      Home.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              Intent home_intent = new Intent(IncentiveActivity.this,DashboardAcitivity.class);
              startActivity(home_intent);
              finish();
          }
      });



        txtfrmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(IncentiveActivity.this,R.style.DialogTheme,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txtfrmDate.setText(year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        String todat = txtfrmDate.getText().toString();
                        java.text.SimpleDateFormat sdf3 = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        java.text.SimpleDateFormat sdf4 = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        String tdate;
                        try {
                            tdate = sdf4.format(sdf3.parse(todat));
                            Log.e("Incentice", "From Date: "  + tdate );
                            txtfrmDate.setText(tdate);
                        } catch (Exception e) {


                        }
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        txttoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                toyear = c.get(Calendar.YEAR);
                tomonth = c.get(Calendar.MONTH);
                today= c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(IncentiveActivity.this,R.style.DialogTheme,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txttoDate.setText(year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        String todat = txttoDate.getText().toString();
                        java.text.SimpleDateFormat sdf3 = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        java.text.SimpleDateFormat sdf4 = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        String tdate;
                        try {
                            tdate = sdf4.format(sdf3.parse(todat));
                            Log.e("Incentice", "To Date: "  + tdate );
                            txttoDate.setText(tdate);
                        } catch (Exception e) {

                        }
                    }
                },toyear,tomonth,today);
                datePickerDialog.show();
            }
        });


        ////////////////////////////////////
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File fileDB = new File(incentiveDatabaseName);
                if (fileDB.exists()) {
                    databaseOperation.deleteIncentiveDB(fileDB);
                }

                pd.setTitle("Request sending..");
                pd.setMessage("Please wait to get details...");
                pd.show();

             try {
                 String frmDAte = txtfrmDate.getText().toString();
                 String toDate = txttoDate.getText().toString();
                 Log.e("Incentive", "to and from date:- "+toDate +", "+frmDAte );

                 if (frmDAte.length()>0&&toDate.length()>0)
                 {
                     getIncentiveDetails(frmDAte,toDate,userId);


                 }
                 else {
                     Toast.makeText(IncentiveActivity.this, "Please select date", Toast.LENGTH_SHORT).show();
                     pd.dismiss();
                 }
             }
             catch (Exception e)
             {

             }
                Log.e("Incentive", "userId:- "+userId );


            }
        });


    }
//
//    private void setRecycerview() {
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter=new IncentiveAdapter(this,getList());
//        recyclerView.setAdapter(adapter);
//    }


   /////////
   private void setRecycerview() {
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(this));
       adapter=new IncentiveAdapter(this, new ClickListener() {
           @Override
           public void onPositionClicked(int position) {


           }

           @Override
           public void onLongClicked(int position) {

           }
       }, getList());
       recyclerView.setAdapter(adapter);
   }
    /////////
    private List<IncentiveModel> getList() {
        List<IncentiveModel> incentive_List=new ArrayList<>();

        /////////////////////////////
        File dbFile = getApplicationContext().getDatabasePath(incentiveDatabaseName);


        try {
            if (dbFile.exists())
            {

                retrieveIncentiveData = databaseOperation.RetrieveIncentive();

                //int i =0;
                for (int i = 0;i<retrieveIncentiveData.length;i++)
                {
                    String[] str = retrieveIncentiveData[i].split(",");
                    // srNo+","+productSrNo+","+model+","+invoiceNo+","+saleDate+","+incentiveAmt+","+status  ;
                    String srNo = str[0];
                    String productSrNo = str[1];
                    String model = str[2];
                    String invoiceNo = str[3];
                    String saleDate = str[4];
                    /////////////////
//                    String[] parts = saleDate.split(" ");
//                    Log.e("Lastupdated parts", "parts" + parts);
//                    String dateonly = parts[0]; // 004
//                    Log.e("Lastupdated part1", String.valueOf(dateonly));
                    //String timeonly = parts[1]; // 034556
                    /////////////////
                    String incentiveAmt = str[5];
                    String status = str[6];
                    incentive_List.add(new IncentiveModel(srNo,productSrNo,model,invoiceNo,saleDate,incentiveAmt,status));

                }
            }
        }
        catch (SQLException e)
        {
            Toast.makeText(IncentiveActivity.this, "Table not exist", Toast.LENGTH_SHORT).show();
        }

        //////////////////////////////


        return incentive_List;
    }


    public  void  getIncentiveDetails(String fDAte,String tDate, String uId)
    {
      //  try {



           // String url = "http://ovotapplive.poshsmetal.com/index.asmx/IncentiveReport";
//            String url = "http://ovotappdev.poshsmetal.com/index.asmx/IncentiveReport";
        String url = Url.INCENTIVE_REPORT_URL;
            Log.e("Inentive", "getIncentiveDetails: url " +url );
            /////////////////////////////////////////
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        pd.dismiss();
                        JSONObject j = new JSONObject(response);
                        // CustomerResult = j.getJSONArray("customers");
                        JSONArray customerdetail = j.getJSONArray("IncentiveRPT");
                        for (int i = 0; i < customerdetail.length(); i++) {
                            JSONObject productd = customerdetail.getJSONObject(i);

                            //////////////////////////

                            String message = productd.optString("message");
                            if (message.equals("Fail"))
                            {
                                Toast.makeText(IncentiveActivity.this, "Data not available for selected date ", Toast.LENGTH_SHORT).show();

                            }else {

                             ///////////////////////////////

                                String productSrNo = productd.optString("ProductSrNo");
                                    String model = productd.optString("Model");
                                    String invoiceNo = productd.optString("InvoiceNo");
                                    String saleDate = productd.optString("CreatedDate");
                                    Integer IncentiveAmt = productd.optInt("IncentiveAmt");
                                    String incentiveAmt = String.valueOf(IncentiveAmt);
                                    String status = productd.optString("ApproveStatus");
                                    String reason = productd.optString("Reason");
                                    String customer = productd.optString("Customer");
                                    String approveBy = productd.optString("ApproveBy");
                                    String approveDate = productd.optString("ApproveDate");
                                    //String dealer = productd.optString("ProductSrNo");
                                    String dealer = "NA";
                                    String phone = productd.optString("MobileNo");

                                    if (status.equals("null")||status.length()==0)
                                    {
                                        status = "Pending";
                                    }

//                                    if (reason.length()<0 || reason.equals(null))
//                                    {
//                                       // description = reason;
//                                        description = "no data available";
//                                    }
//                                    else {
//                                        description = reason;
//                                    }

                                    Log.e("IncentiveDetails:--", "productDetails: " +productSrNo +", "+ ", "+customer+", "+status );
                                    databaseOperation.storeIncentiveData(productSrNo,model,invoiceNo,saleDate,incentiveAmt,status,reason,customer,approveBy,approveDate,dealer,phone);
                                    //String del = "X";
                                    setRecycerview();
                                    pd.dismiss();
                                }

//                                Intent intent = new Intent(IncentiveActivity.this,IncentiveActivity.class);
//                                startActivity(intent);
//                                finish();
                                ///////////////////////////////
                            }




//
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Incentive", "catch1: "+e.getMessage() );
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
                    params.put("ISDCODE",uId);
                    params.put("FromDt",fDAte);
                    params.put("ToDt",tDate);

                    return params;
                }
            };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

//            jsonarrayRequest = new JsonArrayRequest(
//                    Request.Method.GET,
//                    // "http://twtech.in:8080/GPSStatusMobileeyeV1/rest/UserRegistration?FirstName=" + urlUsername + "&LastName=" + urlLastName + "&Username=" + urlUsername + urlLastName + "&EmailId=" + urlEmail + "&MobileNo=" + urlMobile + "&EmergencyMobNo="+urlEmergencyNo+"&Address=" + urlAddress + "&CompanyCode=" + urlCompCode + "&imeiNo=" + imeiNo + "&OTP=" + OTP + "&format=json",
//                    //"http://twtech.in:8080/VehSummaryNew30Sep/rest/ValidUser?Verification=9637274083&format=json",
//                    "http://twtech.in:8080/VehSummaryNew30Sept/rest/IncentiveReport?UID=PS0071&FromDate=2021-10-05&ToDate=2021-10-05&format=json",
//                    new JSONArray(),
//                    new Response.Listener<JSONArray>() {
//                        @Override
//                        public void onResponse(JSONArray response) {
//                            try {
//                                     pd.dismiss();
//
//                                for (int i = 0; i < response.length(); i++) {
//                                    JSONObject getDatabject = response.getJSONObject(i);
//
//                                    String productSrNo = getDatabject.getString("ProductSrNo");
//                                    String model = getDatabject.getString("Model");
//                                    String invoiceNo = getDatabject.getString("InvoiceNo");
//                                    String saleDate = getDatabject.getString("CreatedDate");
//                                    String incentiveAmt = getDatabject.getString("IncentiveAmt");
//                                    String status = getDatabject.getString("ApproveStatus");
//                                    String reason = getDatabject.getString("RejectionReason");
//                                    String customer = getDatabject.getString("Customer");
//                                    String approveBy = getDatabject.getString("ApproveBy");
//                                    String approveDate = getDatabject.getString("ApproveDate");
//                                    String dealer = getDatabject.getString("Dealer");
//                                    String phone = getDatabject.getString("MobileNo");
//
//                                    if (reason.length()>0 && reason!=null)
//                                    {
//                                        description = reason;
//                                    }
//                                    else {
//                                        description = "no data available";
//                                    }
//
//                                    Log.e("IncentiveDetails:--", "productDetails: " +productSrNo +", "+ ", "+customer );
//                                    databaseOperation.storeIncentiveData(productSrNo,model,invoiceNo,saleDate,incentiveAmt,status,description,customer,approveBy,approveDate,dealer,phone);
//                                    //String del = "X";
//                                    setRecycerview();
//                                    pd.dismiss();
//                                }
//
////                                Intent intent = new Intent(IncentiveActivity.this,IncentiveActivity.class);
////                                startActivity(intent);
////                                finish();
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.e("Exception while 2", "Sending data to server : ");
//                            //Toast.makeText(getApplicationContext(), "Response REcieved @@ ", Toast.LENGTH_SHORT).show();
//                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                                Toast.makeText(IncentiveActivity.this, "Error Network Timeout", Toast.LENGTH_SHORT).show();
//                                Log.e("Time Out Error", String.valueOf(error instanceof TimeoutError));
//                            } else if (error instanceof AuthFailureError) {
//                                //TODO
//                                Toast.makeText(IncentiveActivity.this, "Please enter valid serial number", Toast.LENGTH_SHORT).show();
//                            } else if (error instanceof ServerError) {
//                                //TODO
//                                Toast.makeText(IncentiveActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                            } else if (error instanceof NetworkError) {
//                                //TODO
//                                Toast.makeText(IncentiveActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
//                            } else if (error instanceof ParseError) {
//                                //T
//                                Toast.makeText(IncentiveActivity.this, "Please enter valid date", Toast.LENGTH_SHORT).show();
//                                Log.e("Parsing Error", String.valueOf(error instanceof ParseError));
//                            }
//                            pd.dismiss();
//
//
//                        }
//
//                    }
//            );
//            jsonarrayRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    50000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//            requestQueue.add(jsonarrayRequest);


//        } catch (Exception e) {
//            Log.e("IncentiveActivity ", ": Exception while " + "Sending data to server : " + e.getMessage());
//            Toast.makeText(IncentiveActivity.this, "Oops....Failed Try Later.", Toast.LENGTH_LONG).show();
//        }

    }

}