package com.example.page1;

import static com.example.page1.Constants.isNetworkAvailable;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
 private Button  login,getOTP;
 private EditText mobNO,OTP;
 GlobalVariable gbl;
    JsonArrayRequest jsonarrayRequest;
  RequestQueue requestQueue;
 SessionManager sf;
    String userId = "ragini";
    String uDB = Environment.getExternalStorageDirectory().getPath() + "/TWAmstradDB/userCode.db";

 String mobileNo;
    int getOTPFromUser ;
 int otp1;
 ProgressDialog pd;
    String otpDB = Environment.getExternalStorageDirectory().getPath() + "/TWAmstradDB/otpInfo.db";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
  //WindowDecorActionBar=false;

       login = findViewById(R.id.login);
       // cancel = findViewById(R.id.cancel);
        getOTP = findViewById(R.id.otp_btn);
        mobNO = findViewById(R.id.mob_no);
        OTP = findViewById(R.id.otp_et);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
//        setSupportActionBar(toolbar);
//        	getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//		getSupportActionBar().setDisplayShowCustomEnabled(true);
//		getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
//		View view = getSupportActionBar().getCustomView();


        pd = new ProgressDialog(LoginActivity.this);
       gbl = (GlobalVariable) LoginActivity.this.getApplicationContext();

        sf = new SessionManager(LoginActivity.this);
       //gbl = (GlobalVariable) LoginActivity.this.getApplication();
        requestQueue = Volley.newRequestQueue(getApplicationContext());


        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (isNetworkAvailable(LoginActivity.this)) {


                    mobileNo = mobNO.getText().toString();
                    mobileNo = mobileNo.replaceAll("\\s", "%20");
                    if (mobileNo.length()!=0 && mobileNo.length()<=13)
                    {

                        pd.setTitle("Request sending..");
                        pd.setMessage("Please wait to get OTP...");
                        pd.show();

                        otp1 = generateRandomNumber();

                        Log.e("Login", "OTP1: "  + otp1 );
                        gbl.setUpdatedOTP(otp1);

                        Log.e("Login", "OTP1: "  + otp1 );


                        Runnable progressRunnable = new Runnable() {

                            @Override
                            public void run() {
                                pd.cancel();
                            }
                        };

                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 4000);

//                        try {
//                            File databaseExist = getApplicationContext().getDatabasePath(otpDB);
//                            if (databaseExist.exists()) {
//
//                                new DatabaseOperation(getApplicationContext()).updateOTP2(mobileNo,  otp1, "PENDING");
//                                //new MyLogger().storeMassage("Update OTP", "OTP Updated in Android DB");
//                            } else {
//                                new DatabaseOperation(getApplicationContext()).userDetail(mobileNo,  otp1, "PENDING");
//                                //new MyLogger().storeMassage("User Details:", "Save User info in Android DB");
//                            }
//
//                        }
//                        catch (Exception e)
//                        {
//                            Log.e("Exception ", "While Inserting data  : " + e.getMessage());
//                            Toast.makeText(LoginActivity.this, "Oops....OTP not received, Try Later.", Toast.LENGTH_LONG).show();
//                        }


                        new Connection().execute();


                    }
                    else {
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, "please enter valid mobile no", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please Check Internet Connectivity", Toast.LENGTH_SHORT).show();
                    Log.e("Register Activity", "Internet Off");
                   // new MyLogger().storeMassage(Tag, " User Internet Off");
                }
               // pd.dismiss();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int otp2 = gbl.getUpdatedOTP();
                Log.e("Login", "otp2... " + otp2);

               pd.setTitle("Please waite");
               pd.setMessage("We are sending request...");
               //pd.show();

                if (mobileNo!=null && mobileNo.length()==10)
                {
                    try {

                        getOTPFromUser = Integer.parseInt (OTP.getText().toString());
                        if (getOTPFromUser==otp2)
                        {
                            pd.show();
                            UserLogin();

//                            sf.createLoginSession(mobileNo, userId);
//
//                            Intent intent = new Intent(LoginActivity.this,DashboardAcitivity.class);
//                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "please enter valid OTP", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(LoginActivity.this, "please enter valid OTP", Toast.LENGTH_SHORT).show();

                    }


                }
                else {

                    Toast.makeText(LoginActivity.this, "please enter valid mobile no to get OTP", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }

    public int generateRandomNumber() {
        int randomNumber;
        int range = 9;  // to generate a single number with this range, by default its 0..9
        int length = 4; // by default length is 4
        SecureRandom secureRandom = new SecureRandom();
        String s = "";
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(range);
            if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                i = -1;
                continue;
            }
            s = s + number;
        }
        randomNumber = Integer.parseInt(s);
        return randomNumber;
    }

    private class Connection extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                String apiKey = "key=" + "ua9087wh32424UAWH39087";
                String numbers = "&ph=" + mobileNo;
                Log.e("Login", "mobile no: " + numbers );
                String sender = "&sndr=" + "TWOVOT";
                String message = "&text= " + otp1+" is your One Time Password for online Registration of Promax App Dont share this with anyone - TWOVOT";
                String data = apiKey + numbers + sender + message;
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) new URL("http://SMS.VRINFOSOFT.CO.IN/unified.php?").openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Login", "onClick:1 " + e.getMessage() );
                }

                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                conn.getOutputStream().write(data.getBytes("UTF-8"));
                final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                final StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = rd.readLine()) != null) {
                    stringBuffer.append(line);
                   // Toast.makeText(LoginActivity.this,line,Toast.LENGTH_LONG).show();
                }
                Log.e("Login", "SMS sent successfully.." );
                rd.close();
               // pd.dismiss();
            } catch (NetworkOnMainThreadException | IOException e) {
                //IOException e
                System.out.println("Error SMS "+e);
                Log.e("Login", "onClick:2 " + e.getMessage());
               // pd.dismiss();

            }
           // pd.dismiss();

            return null;
        }



    }

    public void UserLogin() {
        try {
          //  String url = "http://ovotapplive.poshsmetal.com/index.asmx/ValidateLogin";
            //String url = "http://ovotappdev.poshsmetal.com/index.asmx/ValidateLogin";
            String url = Url.URL_Login;
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
                        JSONArray customerdetail = j.getJSONArray("LoginDetails");
                        for (int i = 0; i < customerdetail.length(); i++) {
                            JSONObject userd = customerdetail.getJSONObject(i);
                            //userId = userd.getString("UserId");
                            message = userd.getString("message");


                            if(message.equals("Fail")){

                                Toast.makeText(LoginActivity.this, "You are not registered user..", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }else{


                                //////////////////////////
                                userId = userd.getString("UserId");
                                File databaseExist = getApplicationContext().getDatabasePath(uDB);
                                if (databaseExist.exists()) {
                                    new DatabaseOperation(getApplicationContext()).updateUserCode(userId);
                                    Log.e("name","updated");
                                }
                                else {
                                    new DatabaseOperation(getApplicationContext()).storeUserCode(userId);
                                    Log.e("name","store");
                                }
                                /////////////////
                                Toast.makeText(LoginActivity.this, "You are registered user..", Toast.LENGTH_SHORT).show();

                                pd.dismiss();
                                sf.createLoginSession(mobileNo, userId);

                                Intent intent = new Intent(LoginActivity.this,Intermediate.class);
                                startActivity(intent);
                            }
                            ////////////////////////

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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
                    params.put("MobileNo",mobileNo);

                    return params;
                }
            };
            requestQueue.add(stringRequest);


        } catch (Exception e) {
            Log.e("Register Activity ", ": Exception while " + "Sending data to server : " + e.getMessage());
            Toast.makeText(LoginActivity.this, "Oops....Registration Failed Try Later.", Toast.LENGTH_LONG).show();
        }
    }


    }