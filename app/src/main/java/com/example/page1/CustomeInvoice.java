package com.example.page1;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomeInvoice extends AppCompatActivity{
//    public class CustomeInvoice extends AppCompatActivity  implements
//            AdapterView.OnItemSelectedListener {
    RecyclerView recyclerView;
 //   ClickListener listener;
  //  ClickListener.onclicklistner listner;
    GlobalVariable gbl;
    private ImageView LogOut,Home,InvoicePic;
    Customer_invoice_adapter adapter;
    private Spinner spinner;
    String odometerpicturePath1;
    static final int CAPTURE_IMAGE_REQUEST = 1;
    File photoFile = null;
    Uri photoURI=null;
    private String mCurrentPhotoPath="";
    private Button addProduct,submit, cancelBTN;
    String MobilePattern = "[0-9]{10}";
    String serialNo,modelNo;
    String scannedSerialNo ;
    JsonArrayRequest jsonarrayRequest;
    RequestQueue requestQueue;
    private ProgressDialog pd;
    DatabaseOperation databaseOperation;
    private MyFTPClientFunctions ftpclient = null;
    String retrieveData[];
    String ProductDatabaseName = Environment.getExternalStorageDirectory().getPath() + "/TWAmstradDB/DDProductData.db";
    EditText ETname,ETmob,ETinvoice;

    String name,mob,invoice;
    private static final String PREF_NAME = "AndroidHivePref";
    String insertionMessage;

    public CustomeInvoice() {
        //ClickListener listener
       // this.listener = listener;
    }

    //    String[]retrieveData = { "Select","ISD0000002", "ISD0000003", "ISD0000004", "ISD0000004", "ISD0000005"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_invoice);

        pd = new ProgressDialog(CustomeInvoice.this);
        databaseOperation = new DatabaseOperation(getApplicationContext());
        ftpclient = new MyFTPClientFunctions();
        gbl = (GlobalVariable) CustomeInvoice.this.getApplicationContext();

        LogOut = findViewById(R.id.logout);
        Home = findViewById(R.id.ibv_icon3);
        addProduct = findViewById(R.id.add_product_btn);
        InvoicePic = findViewById(R.id.img_invoice);
        ETname = findViewById(R.id.cust_name);
        ETmob = findViewById(R.id.mob_num);
        ETinvoice = findViewById(R.id.invoice_num);
        submit = findViewById(R.id.invoice_submit);
        cancelBTN = findViewById(R.id.cancel_btn);
        recyclerView=findViewById(R.id.incentive_recyclerview);
        setRecycerview();


        requestQueue = Volley.newRequestQueue(getApplicationContext());
//        spinner= (Spinner) findViewById(R.id.spinner1);
//        spinner.setOnItemSelectedListener(this);

//        //Creating the ArrayAdapter instance having the country list
//        ArrayAdapter aa = new ArrayAdapter(CustomeInvoice.this,android.R.layout.simple_spinner_item,retrieveData);
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //Setting the ArrayAdapter data on the Spinner
//        spinner.setAdapter(aa);




        InvoicePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageforInvoice();

            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showOtpAlertDialog();
            }
        });



        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences =getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                finish();

                Intent log_intent = new Intent(CustomeInvoice.this,LoginActivity.class);
                startActivity(log_intent);
                finish();
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent home_intent = new Intent(CustomeInvoice.this,DashboardAcitivity.class);
                startActivity(home_intent);
                finish();
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    name = ETname.getText().toString();
                    mob = ETmob.getText().toString();
                    invoice = ETinvoice.getText().toString();

//                    if (name.length()<0 && mob.length()<0 && invoice.length()<0){
                    if (name.length()<1){

                        Toast.makeText(CustomeInvoice.this, "Please enter Customer name..", Toast.LENGTH_SHORT).show();
                    }else if (invoice.length()<1)
                    {
                        Toast.makeText(CustomeInvoice.this, "Please enter invoice number..", Toast.LENGTH_SHORT).show();

                    }
                    else if (!mob.matches(MobilePattern))
                    {
                        Toast.makeText(CustomeInvoice.this, "Please enter valid mobile no..", Toast.LENGTH_SHORT).show();

                    }
                    else if (mob.length()<10){
                        Toast.makeText(CustomeInvoice.this, "Please enter valid mobile no..", Toast.LENGTH_SHORT).show();

                    }else if (mob.length()>13)
                    {
                        Toast.makeText(CustomeInvoice.this, "Please enter valid mobile no..", Toast.LENGTH_SHORT).show();

                    }else {
                        //serialNo = scannedSerialNo;
                        pd.setTitle("Request sending..");
                        pd.setMessage("Please wait...");
                        pd.show();

                        sendInvoiceDetails(name,mob,invoice);
                        sendImageDetails(mCurrentPhotoPath,invoice);

                    }

//                        sendInvoiceDetails(name,mob,invoice);
//                        sendImageDetails(mCurrentPhotoPath);
//                    }
//                   else
//                    {
//                        Toast.makeText(CustomeInvoice.this, "Empty field is not allowed", Toast.LENGTH_SHORT).show();
//
//                    }




                }
                catch (Exception e)
                {
                    Toast.makeText(CustomeInvoice.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File fileDB = new File(ProductDatabaseName);
                if (fileDB.exists()) {
                    databaseOperation.deleteUser(fileDB);

                    Intent cancelIntent = new Intent(CustomeInvoice.this,CustomeInvoice.class);
                    startActivity(cancelIntent);
                    finish();
                }
                else {
                    Intent cancelIntent = new Intent(CustomeInvoice.this,CustomeInvoice.class);
                    startActivity(cancelIntent);
                    finish();
                }

            }
        });

    }
    private void setRecycerview() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new Customer_invoice_adapter(this, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {

               Intent intent = new Intent(CustomeInvoice.this,CustomeInvoice.class);
               startActivity(intent);
               //finish();
            }

            @Override
            public void onLongClicked(int position) {

            }
        }, getList());
        recyclerView.setAdapter(adapter);
    }
    private List<InvoiceModel> getList() {
        List<InvoiceModel> customer_List=new ArrayList<>();

        File dbFile = getApplicationContext().getDatabasePath(ProductDatabaseName);


        try {
            if (dbFile.exists())
            {

                retrieveData = databaseOperation.RetrieveRegData();

                //int i =0;
                for (int i = 0;i<retrieveData.length;i++)
                {
                    String[] str = retrieveData[i].split(",");
                   // srNo+","+productCat+","+model+","+subCat+","+incentiveAmt+","+serialNo+","+actn ;
                    String srNo = str[0];
                    String prodCat = str[1];
                    String model = str[2];
                    String subCat = str[3];
                    String incentiveAmt = str[4];
                    String serial = str[5];
                    String actn = str[6];
                    customer_List.add(new InvoiceModel(srNo,prodCat,subCat,model,serial,incentiveAmt,actn));

                }
            }
        }
        catch (SQLException e)
        {
            Toast.makeText(CustomeInvoice.this, "Table not exist", Toast.LENGTH_SHORT).show();
        }

//
//        retrieveData = databaseOperation.RetrieveRegData();
//
//        //int i =0;
//        for (int i = 0;i<=retrieveData.length;i++)
//        {
//           String[] str = retrieveData[i].split(",");
//
//           String srNo = str[0];
//           String prodCat = str[1];
//           String model = str[2];
//           String subCat = str[3];
//           String incentiveAmt = str[4];
//           String serial = str[5];
//           String actn = str[6];
//            customer_List.add(new InvoiceModel(srNo,prodCat,model,subCat,incentiveAmt,serial,actn));
//        }

       // Log.e("Database", "RetrieveRegData: " + retrieveData[0] );
        return customer_List;
    }




//    @Override
//    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//        Toast.makeText(getApplicationContext(),retrieveData[position] , Toast.LENGTH_LONG).show();
//    }
//    @Override
//    public void onNothingSelected(AdapterView<?> arg0) {
//        // TODO Auto-generated method stub
//    }


    private void selectImageforInvoice() {
        final CharSequence[] options = {"Take Photo"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomeInvoice.this);
        builder.setIcon(R.drawable.icon);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(options[item].equals("Take Photo")) {
                    if (ContextCompat.checkSelfPermission(CustomeInvoice.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CustomeInvoice.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    }
                    else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


                            try {
                                photoFile = createImageFile();
                                displayMassage(getBaseContext(),photoFile.getAbsolutePath());
                                Log.e("Ragini", photoFile.getAbsolutePath());



                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    Log.e("filepro", "filepro1");
                                    photoURI = FileProvider.getUriForFile(CustomeInvoice.this,"com.example.page1.fileprovider",photoFile);

                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                                    Log.e("filepro", "filepro2");
                                }
                            } catch (IOException ex) {
                                displayMassage(getBaseContext(),ex.getMessage().toString());
                            }
                        }

                    }


                }

            }

        });

        builder.show();

    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "FV_Vehicle No_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e("File Capture ", ":" + mCurrentPhotoPath);
        return image;
    }

    protected void onActivityResult(int requestCode,  int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//
        if(requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK)
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            Toast.makeText(CustomeInvoice.this, "Invoice image captured", Toast.LENGTH_SHORT).show();
           // imageView.setImageBitmap(myBitmap);
        }
        /////////////////////////////////////
        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (Result != null) {
            if (Result.getContents() == null) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned -> " + Result.getContents(), Toast.LENGTH_SHORT).show();
                scannedSerialNo = Result.getContents().toString();

                Toast.makeText(this, "Scanned successfully", Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                // checkCameraPresent("1");
                selectImageforInvoice();
            }
            else
            {
                displayMassage(getBaseContext(),"This app is not working");
            }
        }
    }

    private void displayMassage(Context context, String message)
    {
        //Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }




    public void showOtpAlertDialog() {
        // flag=false;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final Dialog dialog = new Dialog(CustomeInvoice.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custum_dialog, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b;
        final EditText serial = (EditText) dialogView.findViewById(R.id.edit1);
       final ImageView scanner = (ImageView) dialogView.findViewById(R.id.scan);
       // final EditText model = (EditText) dialogView.findViewById(R.id.edit2);

        dialogBuilder.setCancelable(false);
        Button  btnSubmit,btncancel;
        btnSubmit=(Button) dialogView.findViewById(R.id.btnSubmit);
        btncancel=(Button) dialogView.findViewById(R.id.btncancel);
        b = dialogBuilder.create();
        b.show();

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(CustomeInvoice.this);
                intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setPrompt("SCAN");
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();
                  // setting result of scanned bar code, that is getting from on Activity result

                //        serial.setText(scannedSerialNo);
                // Log.e("CustomerInvoice", "scannedSerialNo" + scannedSerialNo );

            }

        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // pd.show();
                try {
                   // Log.e("OTP from U"," : " + edt.getText().toString());
//                    pd.setTitle("Request sending..");
//                    pd.setMessage("Please wait...");
//                    pd.show();

                    if (scannedSerialNo!=null)
                    {
                        serialNo = scannedSerialNo;
                        pd.setTitle("Request sending..");
                        pd.setMessage("Please wait...");
                        pd.show();
                        Log.e("CustomerInvoice", "scannedSerialNo2 " + serialNo );
                        getProductDetails(serialNo);
                       //  b.dismiss();
//                        Intent intent = new Intent(CustomeInvoice.this,CustomeInvoice.class);
//                        startActivity(intent);
//                        finish();

                    }
                    else {
                        if (serial.getText().toString().length()>0) {
                            serialNo = serial.getText().toString();
                            pd.setTitle("Request sending..");
                            pd.setMessage("Please wait...");
                            pd.show();
                            Log.e("CustomerInvoice", "scannedSerialNo3 " + serialNo);
                             getProductDetails(serialNo);
                            // b.dismiss();
//                            Intent intent = new Intent(CustomeInvoice.this, CustomeInvoice.class);
//                            startActivity(intent);
//                            finish();
                        } else {
                            Toast.makeText(CustomeInvoice.this, "Please enter product serial No", Toast.LENGTH_SHORT).show();
                        }

                    }



                } catch (Exception e) {
                    Log.e("Exception ", " : " + e.getMessage());
                }

            }
        });
//        b = dialogBuilder.create();
//        b.show();


        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                b.dismiss();
                Intent intent = new Intent(CustomeInvoice.this, CustomeInvoice.class);
                            startActivity(intent);
                            finish();

            }
        });
    }

//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (Result != null) {
//            if (Result.getContents() == null) {
//                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
//            } else {
//                Log.d("MainActivity", "Scanned");
//                Toast.makeText(this, "Scanned -> " + Result.getContents(), Toast.LENGTH_SHORT).show();
//                .setText(String.format("Scanned Result: %s", Result));
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    public void getProductDetails(String srNo)
    {


     //   try {
      //  String url = "http://ovotapplive.poshsmetal.com/index.asmx/GetProductDetails";
            //String url = "http://ovotappdev.poshsmetal.com/index.asmx/GetProductDetails";
        String url = Url.GET_PRODUCT_DETAILS_URL;
            /////////////////////////////////////////
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        pd.dismiss();
                        JSONObject j = new JSONObject(response);
                        // CustomerResult = j.getJSONArray("customers");
                        JSONArray customerdetail = j.getJSONArray("ProductDt");
                        for (int i = 0; i < customerdetail.length(); i++) {
                            JSONObject productd = customerdetail.getJSONObject(i);

                            //////////////////////////

                            String message = productd.optString("message");
                            if (message.equals("Serial No Already Exist in Invoice"))
                            {
                                Toast.makeText(CustomeInvoice.this, "Product Already Exist in Invoice", Toast.LENGTH_SHORT).show();
                            }else if (message.equals("Fail"))
                            {
                                Toast.makeText(CustomeInvoice.this, "Product serial not valid", Toast.LENGTH_SHORT).show();

                            }else {

                                String ProductCat = productd.optString("ProductCat");
                                String DistributerName = productd.optString("DistributerName");
                                String Model = productd.optString("Model");
                                String ProductCode = productd.optString("ProductCode");
                                String SubCat = productd.optString("SubCat");
                                //String IncentiveAmt = productd.getString("IncentiveAmt");
                                Integer  incentiveAmt =  productd.optInt("IncentiveAmt");
                                String IncentiveAmt = String.valueOf(incentiveAmt);
                                //String DealerCode = getDatabject.getString("DealerCode");
                                String DealerCode = "NA";
                                String SerialNo = productd.optString("SerialNo");
                                ////////////////////////
                                Log.e("CustomeInvoice", "productDetails: " +ProductCat +", "+ ", "+DistributerName );
                                databaseOperation.storeProductData(ProductCat,DistributerName,Model,ProductCode,SubCat,IncentiveAmt,DealerCode,SerialNo);
                                //setRecycerview();


                                //String del = "X";


                                Intent intent = new Intent(CustomeInvoice.this, CustomeInvoice.class);
                                startActivity(intent);
                                finish();
                            }



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
                    params.put("ProductSRNO",srNo);

                    return params;
                }
            };
            requestQueue.add(stringRequest);

//        } catch (Exception e) {
//            Log.e("Register Activity ", ": Exception while " + "Sending data to server : " + e.getMessage());
//            Toast.makeText(CustomeInvoice.this, "Oops....Failed Try Later.", Toast.LENGTH_LONG).show();
//        }
    }


    public void sendInvoiceDetails(String getName,String getMob, String getInvoice){

        Log.e("CustomInvoice", "sendInvoiceDetails:method called " );

        File dbFile = getApplicationContext().getDatabasePath(ProductDatabaseName);
       // String userId = gbl.getUserId();
        String userId = databaseOperation.retrieveUserCode();

        try {

            if (dbFile.exists())
            {
                String []  productdata = databaseOperation.RetrieveProductCode();
                    int i ;
                for (i = 0;i<productdata.length;i++) {
                    String productCode = productdata[i];

                    Log.e("CustomInvoice", "sendInvoiceDetails:length- " +productdata.length );

                   // String url = "http://ovotapplive.poshsmetal.com/index.asmx/InsertCustomerInvoice";
//                    String url = "http://ovotappdev.poshsmetal.com/index.asmx/InsertCustomerInvoice";
                    String url = Url.INVOICE_INSERTION_URL;
                    Log.e("CustomInvoice", "sendInvoiceDetails:URL- " +url );
                    /////////////////////////////////////////
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                               // pd.dismiss();
                                JSONObject j = new JSONObject(response);
                                // CustomerResult = j.getJSONArray("customers");
                                JSONArray customerdetail = j.getJSONArray("InvoiceUploadCode");
                                JSONObject productd = customerdetail.getJSONObject(0);
                                insertionMessage = productd.optString("message");

                               // Log.e("New Response", "onResponse: "+ message.toString() );
//
//                                for (int i = 0; i < customerdetail.length(); i++) {
//                                    Log.e("InvoiceUpload", "onResponse: " + "Seccess" );
//                                    JSONObject productd = customerdetail.getJSONObject(i);
//
//                                    //////////////////////////
//                                     insertionMessage = productd.optString("message");
//                                    Log.e("InsertionMessage", "onResponse: "+insertionMessage );
//
//                                    ///////////////////////////////////////////////
//                                }

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
                            params.put("InvoiceNo", getInvoice);
                            params.put("ProductCode", productCode);
                            params.put("Customer", getName);
                            params.put("MobileNo", getMob);
                            params.put("ISDCode", userId);
                            params.put("FileName", "abc");
                            params.put("IPAdd", "xyz");
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);


//                for (int i = 0;i<productdata.length;i++)
//                {
//                    String productCode = productdata[i];
//
//                    Log.e("CustomInvoice", "sendInvoiceDetails URL:- " +"http://twtech.in:8080/VehSummaryNew30Sep/rest/InsertInvoice?Name="+getName+"&MobNo="+getMob+"&Invoice="+getInvoice+"&ProductCode="+productCode+"&Path="+mCurrentPhotoPath+"&UID="+userId+"&format=json" );
//
//                    jsonarrayRequest = new JsonArrayRequest(
//                            Request.Method.GET,
//                            // "http://twtech.in:8080/GPSStatusMobileeyeV1/rest/UserRegistration?FirstName=" + urlUsername + "&LastName=" + urlLastName + "&Username=" + urlUsername + urlLastName + "&EmailId=" + urlEmail + "&MobileNo=" + urlMobile + "&EmergencyMobNo="+urlEmergencyNo+"&Address=" + urlAddress + "&CompanyCode=" + urlCompCode + "&imeiNo=" + imeiNo + "&OTP=" + OTP + "&format=json",
//                            //"http://twtech.in:8080/VehSummaryNew30Sep/rest/ValidUser?Verification=9637274083&format=json",
//                            "http://twtech.in:8080/VehSummaryNew30Sep/rest/InsertInvoice?Name="+getName+"&MobNo="+getMob+"&Invoice="+getInvoice+"&ProductCode="+productCode+"&Path="+mCurrentPhotoPath+"&UID="+userId+"&format=json",
//                            new JSONArray(),
//                            new Response.Listener<JSONArray>() {
//                                @Override
//                                public void onResponse(JSONArray response) {
//                                    try {
//                                        //  pd.dismiss();
//                                        for (int i = 0; i < response.length(); i++) {
//                                            JSONObject objectjsn = response.getJSONObject(i);
//                                            String outPut = objectjsn.getString("Name");
//                                            Log.e("Response", "onResponse: " + outPut );
//                                        }
//
//
//                                    } catch (JSONException e ) {
//                                        e.printStackTrace();
//                                        Log.e("Exception ", " : " + e.getMessage());
//                                    }
//                                }
//                            },
//                            new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    Log.e("Exception while 2", "Sending data to server : ");
//                                    //Toast.makeText(getApplicationContext(), "Response REcieved @@ ", Toast.LENGTH_SHORT).show();
//                                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                                        Toast.makeText(CustomeInvoice.this, "Error Network Timeout", Toast.LENGTH_SHORT).show();
//                                        Log.e("Time Out Error", String.valueOf(error instanceof TimeoutError));
//                                    } else if (error instanceof AuthFailureError) {
//                                        //TODO
//                                        Toast.makeText(CustomeInvoice.this, "Authentication Failure Error", Toast.LENGTH_SHORT).show();
//                                    } else if (error instanceof ServerError) {
//                                        //TODO
//                                        Toast.makeText(CustomeInvoice.this, "Server Error", Toast.LENGTH_SHORT).show();
//                                    } else if (error instanceof NetworkError) {
//                                        //TODO
//                                        Toast.makeText(CustomeInvoice.this, "Network Error", Toast.LENGTH_SHORT).show();
//                                    } else if (error instanceof ParseError) {
//                                        //T
//                                        Toast.makeText(CustomeInvoice.this, "Parsing Error", Toast.LENGTH_SHORT).show();
//                                        Log.e("Parsing Error", String.valueOf(error instanceof ParseError));
//                                    }
//                                    // pd.dismiss();
//
//
//                                }
//
//                            }
//                    );
//                    jsonarrayRequest.setRetryPolicy(new DefaultRetryPolicy(
//                            50000,
//                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//                    requestQueue.add(jsonarrayRequest);
//
//
//
//                }
//
//                Toast.makeText(CustomeInvoice.this, "Data sent successfully", Toast.LENGTH_SHORT).show();
//                Intent mainIntent  = new Intent(CustomeInvoice.this,DashboardAcitivity.class);
//                startActivity(mainIntent);
//                finish();

                }
                if (i==productdata.length)
                {
                    pd.dismiss();
                    Toast.makeText(CustomeInvoice.this, "Data sent successfully", Toast.LENGTH_SHORT).show();
                    Intent mainIntent  = new Intent(CustomeInvoice.this,DashboardAcitivity.class);
                    startActivity(mainIntent);
                    finish();
                }
//                pd.dismiss();
//                Toast.makeText(CustomeInvoice.this, "Data sent successfully", Toast.LENGTH_SHORT).show();
//               Intent mainIntent  = new Intent(CustomeInvoice.this,DashboardAcitivity.class);
//               startActivity(mainIntent);
//                 finish();

            }

            else {
                pd.dismiss();
                Toast.makeText(CustomeInvoice.this, "Please add product..", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {
            pd.dismiss();
            Toast.makeText(CustomeInvoice.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(CustomeInvoice.this, "Oops....Data not sent to server Try Later.", Toast.LENGTH_LONG).show();


        }



    }


   public void sendImageDetails(String nCurrentPhotoPath,String invoice_new)
    {

        final String host = "202.66.172.185";
        //final String username = "ovot";
        // final String password = "F568*dcb3@gh908";
        final String username = "ovotdev";
        final String password =  "E56$ssd3@gh54";


        if (host.length() < 1) {
            Toast.makeText(CustomeInvoice.this, "Please Enter Host Address!",
                    Toast.LENGTH_LONG).show();
        } else if (username.length() < 1) {
            Toast.makeText(CustomeInvoice.this, "Please Enter User Name!",
                    Toast.LENGTH_LONG).show();
        } else if (password.length() < 1) {
            Toast.makeText(CustomeInvoice.this, "Please Enter Password!",
                    Toast.LENGTH_LONG).show();
        } else {

//            pd = ProgressDialog.show(CustomeInvoice.this, "", "Connecting...",
//                    true, false);

            File compressFile = saveBitmapToFile(photoFile);

            new Thread(new Runnable() {
                public void run() {
                    boolean status = false;
                    status = ftpclient.ftpConnect(host, username, password, 21);
                    if (status == true) {
                        Log.e("Main1", "Connection Success");



                       // pd = ProgressDialog.show(CustomeInvoice.this, "", "Uploading...",
                              //  true, false);
                        new Thread(new Runnable() {
                            public void run() {
                                boolean status = false;
                                status = ftpclient.ftpUpload(
                                        compressFile,
                                        invoice_new);
                                 pd.dismiss();
                        if (status == true) {
                            Log.e("FTPIMAGE status", "Upload success");
                           ftpclient.ftpDisconnect();
                            pd.dismiss();
                        } else {
                            Log.e("FTPIMAGE status", "Upload failed");
                            ftpclient.ftpDisconnect();
                                pd.dismiss();
                        }
                            }
                        }).start();
                        pd.dismiss();
                    } else {
                        Log.e("Main2", "Connection failed");
                    }
                }
            }).start();
        }
    }

////////////////////////////////////////////


    public File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

}
