package com.example.page1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

import ru.nikartm.support.ImageBadgeView;

public class DashboardAcitivity extends AppCompatActivity {
  private ImageView cm_invoice, incentive,pd_manual,LogOut;
    ImageBadgeView imageBadgeView;
    int count=0;
    GlobalVariable gbl;
    String ProductDatabaseName = Environment.getExternalStorageDirectory().getPath() + "/TWAmstradDB/DDProductData.db";
    DatabaseOperation databaseOperation;
    private static final String PREF_NAME = "AndroidHivePref";
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_acitivity);
        cm_invoice = findViewById(R.id.cm_invoice);
        incentive = findViewById(R.id.incentive);
        pd_manual=findViewById(R.id.product_btn);
        LogOut = findViewById(R.id.logout);

        databaseOperation = new DatabaseOperation(getApplicationContext());
        ////////////////////////////////////
        new GetVersionCode().execute();
        try{
            if(gbl.getUpdateCount()==0){
                Log.e("Iff","Block Executed");
                imageBadgeView.setBadgeValue(0);
            }else {
                Log.e("Else","Block Executed");
            }
        } catch (Exception e) {
            Log.e("ElsestatEx", e.getMessage());
        }
        Log.e("countt", String.valueOf(count));
        imageBadgeView=(ImageBadgeView)findViewById(R.id.ibv_icon2);
        ///////////////////////////////////////

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences preferences =getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                finish();

                Intent log_intent = new Intent(DashboardAcitivity.this,LoginActivity.class);
                startActivity(log_intent);
                finish();
            }
        });


//        // Find the toolbar view and set as ActionBar
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
//        setSupportActionBar(toolbar);
//// Display icon in the toolbar
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);


        gbl = (GlobalVariable) DashboardAcitivity.this.getApplicationContext();

//        new GetVersionCode().execute();
//        try{
//            if(gbl.getUpdateCount()==0){
//                Log.e("Iff","Block Executed");
//                imageBadgeView.setBadgeValue(0);
//            }else {
//                Log.e("Else","Block Executed");
//            }
//        } catch (Exception e) {
//            Log.e("ElsestatEx", e.getMessage());
//        }
//        Log.e("countt", String.valueOf(count));
//        imageBadgeView=findViewById(R.id.notification);


        cm_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File fileDB = new File(ProductDatabaseName);
                if (fileDB.exists()) {
                    databaseOperation.deleteUser(fileDB);

                    Intent i = new Intent(DashboardAcitivity.this, CustomeInvoice.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(DashboardAcitivity.this, CustomeInvoice.class);
                    startActivity(i);
                    finish();
                }
//
            }
        });

        incentive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardAcitivity.this,IncentiveActivity.class);
                startActivity(i);
            }
        });

        pd_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(DashboardAcitivity.this,ProductManual.class);
                startActivity(i2);
            }
        });



    }


    private class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String newVersion = null;
            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                Log.d("document", String.valueOf(document));
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            Log.d("sibElemets", String.valueOf(sibElemets));
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                                Log.d("newVersion", String.valueOf(newVersion));
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;

        }


        @Override

        protected void onPostExecute(String onlineVersion) {

            super.onPostExecute(onlineVersion);

            //Log.e("onlineVersion",onlineVersion);
            //Log.e("currentVersion",Constants.appVersion);

            if (onlineVersion != null && !onlineVersion.isEmpty()) {

                if (onlineVersion.equals(Constants.appVersion)) {

                } else {
                    count++;
                    Log.e("counttt", String.valueOf(count));
                    imageBadgeView.setBadgeValue(count);
                    imageBadgeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog alertDialog = new AlertDialog.Builder(DashboardAcitivity.this).create();
                            alertDialog.setTitle("Update");
                            alertDialog.setIcon(R.drawable.logo1);
                            alertDialog.setMessage("New Update is available");

                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        imageBadgeView.setBadgeValue(0);
                                        gbl.setUpdateCount(0);
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                                    }
                                }
                            });

                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            try {
                                alertDialog.show();
                            } catch (Exception e) {
                                Log.e("Exxc", e.getMessage());
                            }
                        }
                    });


                }

            }

            Log.d("update", "Current version playstore version " + onlineVersion);

        }
    }
@Override
public void onBackPressed() {
    new AlertDialog.Builder(this)
            .setTitle("Really Exit?")
            .setMessage("Are you sure you want to exit?")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    DashboardAcitivity.super.onBackPressed();
                }
            }).create().show();
}

}