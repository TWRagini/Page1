package com.example.page1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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

public class MainActivity extends AppCompatActivity {
    private ImageView view;
  //  private int SLEEP_TIMER =4 ;
  private int SLEEP_TIMER = 4;
    RequestQueue requestQueue;
    TextView textView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  getSupportActionBar().hide();
       // final ImageView image = (ImageView) findViewById(R.id.img);
       ;
//            Animation animation1 =
//                    AnimationUtils.loadAnimation(getApplicationContext(),
//                            R.anim.blink);
//            image.startAnimation(animation1);
        textView=findViewById(R.id.version);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        logic logic = new logic();
        logic.start();
        
    }

    private class logic extends Thread {
        public void run() {
            try {
                textView.setText("OVOT-DEV-Ver-1.0.0(Build-27102021)");
                sleep(1000*SLEEP_TIMER);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //getVersionDetails();

            Intent i = new Intent(MainActivity.this, CheckPermission.class);
            startActivity(i);
            MainActivity.this.finish();
        }
    }
    public void getVersionDetails()
    {
        {
            try {
//                pd = new ProgressDialog(Agreement.this);
//
//                pd.setTitle("Request sending");
//                pd.setMessage("Please wait to get details..");
//                pd.show();
                //  String url = "http://ovotapplive.poshsmetal.com/index.asmx/GetISDDetails";
                String url = "http://ovotappdev.poshsmetal.com/index.asmx/GetVersionInfo";
                /////////////////////////////////////////
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String userId;

                        String message;

                        try {
                          //  pd.dismiss();
                            JSONObject j = new JSONObject(response);
                            // CustomerResult = j.getJSONArray("customers");
                            JSONArray versiondetail = j.getJSONArray("Version");
                            for (int i = 0; i < versiondetail.length(); i++) {
                                JSONObject isdDetails = versiondetail.getJSONObject(i);
                                //userId = userd.getString("UserId");
                                message = isdDetails.getString("message");

                                if (message.length()>1)
                                {
                                    textView.setText(message);
                                    Intent intent = new Intent(MainActivity.this, CheckPermission.class);
                                    startActivity(intent);
                                    MainActivity.this.finish();


                                }else {
                                    //pd.dismiss();
                                    Intent intent = new Intent(MainActivity.this, CheckPermission.class);
                                    startActivity(intent);
                                    MainActivity.this.finish();
                                }





                                ////////////////////////

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Intent intent = new Intent(MainActivity.this, CheckPermission.class);
                            startActivity(intent);
                            MainActivity.this.finish();
                            //pd.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // pd.dismiss();
                        new VolleyError();
                        // Toast.makeText(getActivity(), "error" + error.toString() + error.networkResponse, Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded";
                    }

//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("ISDCode",new_isd);
//                        params.put("AcceptanceStatus","yes");
//                        params.put("UserCode",new_isd);
//                        params.put("IPAdd","abc");
//
//                        return params;
//                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);


            } catch (Exception e) {
                Log.e("Register Activity ", ": Exception while " + "Sending data to server : " + e.getMessage());
                Intent intent = new Intent(MainActivity.this, CheckPermission.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        }
    }

}