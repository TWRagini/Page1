package com.example.page1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Agreement extends AppCompatActivity {

    String ISDCOde = " ";
    ProgressDialog pd ;
    RequestQueue requestQueue;
    DatabaseOperation databaseOperation;
    boolean isvaild;
    String SENDMAIL = "Promax Agreement";
    String directory_path;
    String targetPdf;
    String dateWithTime;
    String name;
    SpannableString name2;


    private  Button IAgrree;

    private  TextView textView,textView2,textView3,textView4,textView5,textView6;
    SessionManager sf;

    Bitmap bitmap,bitmap2,bitmap3,bitmap4,bitmap5,bitmap6,bitmap7;
    private LinearLayout linearLayout;
//    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        databaseOperation = new DatabaseOperation(getApplicationContext());

        //////////////////////////


        /////////////////////////////

        ISDCOde = databaseOperation.retrieveUserCode();
        Log.e("ISDFromDB", "onCreate: "+ISDCOde );
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        sf = new SessionManager(getApplicationContext());

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        SimpleDateFormat dt = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.getDefault());

        String formattedDate = df.format(c);
        dateWithTime = dt.format(c);



        Log.e("Date", "onCreate: "+formattedDate );

     textView = findViewById(R.id.agreement_content);
        textView2 = findViewById(R.id.agreement_content2);
        textView3 = findViewById(R.id.agreement_content3);
        textView4 = findViewById(R.id.agreement_content4);
        textView5 = findViewById(R.id.agreement_content5);
        textView6 = findViewById(R.id.agreement_content6);
     IAgrree = findViewById(R.id.update_agreement);
     linearLayout =  findViewById(R.id.linear_layout2);
//     rl = findViewById(R.id.relative_annexure);




        try {
            pd = new ProgressDialog(Agreement.this);

            pd.setTitle("Request sending");
            pd.setMessage("Please wait to get details..");
            pd.show();
            //  String url = "http://ovotapplive.poshsmetal.com/index.asmx/GetISDDetails";
           // String url = "http://ovotappdev.poshsmetal.com/index.asmx/GetISDDetails";

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

                            if (message.equals("Success"))
                            {

                                 name = isdDetails.getString("Name");
                                String name3= "<b>"+name+"</b>";
//                                 name = "<Html><b>"+name+"</b></Html>";


                                Log.e("Name", String.valueOf(Html.fromHtml(name3)));
                                 //////////////////////////////
//                                name2=  new SpannableString(name);
//                                name2.setSpan(new RelativeSizeSpan(2f), 0,5, 0); // set size
//                                name2.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, 0);// set color
                                /////////////////////////////////
                                String mobileNo = isdDetails.getString("MobileNo");
                                String emailId = isdDetails.getString("EmailId");
                                String bankName = isdDetails.getString("BankName");
                                String bankACNo = isdDetails.getString("BankACNo");
                                String ifscCode = isdDetails.getString("IFSCCode");
                                String upiNo = isdDetails.getString("UPINo");
                                String panno = isdDetails.getString("PANNO");
                                String adharNo = isdDetails.getString("AdharNo");
//
//                                if (name.length()>1 && !name.equals(null) && !name.equals("null") &&  !name.equals(""))
//                                {
//                                    isdName.setText(name);
//                                }

                                textView.setText(
                                        " This Sales Consultant Agreement (\"Agreement\") is executed and effective date:- "+formattedDate+", by and between:\n" +
                                                "Sales Consultant: "+Html.fromHtml(name3)   +" bearing PAN Number:  "+panno+" Aadhaar Number: "+adharNo+ "\n" +
                                        "AND\n" +
                                        "Client: OVOT PRIVATE LIMITED, a Company registered under the Companies Act, 2013 bearing the Company Identification Number (\"CIN”) U31100PN2018PTC179173, PAN NO-AACCO7927M, GSTIN- 27AACCO7927M1ZV and having its registered office at 403, East Court, Near Phoenix Market City, Viman Nagar, Pune, Maharashtra 411014 India (\"Client\"/OVOT). which term includes its Affiliates) such expression, shall, unless it is repugnant to the meaning or context hereof, includes its successors and permitted assigns, on the OTHER PART.\n" +
                                        "WHEREAS, OVOT is engaged in the business of, trading and marketing of Consumer Durable Goods such as Air Conditioners, Washing Machines, Televisions etc.  \n"+

                                        "WHEREAS Consultant is engaged in providing services for selling the products at dealer counters to the consumers.\n" +
                                        "WHEREAS, OVOT and Consultant agrees to use their best efforts to establish, develop and maintain by faithful performance of transactions and longstanding business relationship based on the spirit of mutual trust and co-operation" +"\n"+

                                        "AND WHEREAS, the Parties have agreed upon certain terms, conditions and stipulations set forth herein, which would govern the provisions of this Agreement.\n" +

                                        "NOW, THEREFORE, in consideration of the mutual covenants hereinafter set forth, the Parties hereto agree as follows:\n" +

                                        "1. Services. Consultant agrees to sell OVOT products at dealer counter to customers.\n" );

                                textView2.setText("2. Term. This Consultancy Agreement shall commence from the date of acceptance of this agreement by the sales consultant. Either party may terminate this Agreement by providing 30 days’ notice.\n" +

                                        "3. Compensation. In consideration for the Services provided, the Consultant is to be paid Consultancy fees as per Annexure-I, which may be amended for time to time.\n" +
                                        "4. Payment. Consultant shall be paid, in 15 days Upon the Client receiving an Invoice from the Consultant.\n" +
                                        "5. Retainership Fees. The Client is not required to pay any fees to consultant, before the commencement of work by the consultant.\n"
                                        +
                                        "6. Escalation of Disputes\n" +
                                        "    a. Any dispute arising out of or in connection with this Agreement shall be referred by written notice first to the authorized representative of client and authorized representative endeavor should be to resolve the dispute within 5 business days of such notice.\n" +
                                        "    b. Failing resolution of the dispute, the matter shall be referred to a senior representative of the Client and consultant, who shall meet and endeavor to resolve the dispute between them within 10 business days of such notice.\n" +

                                        "    c. All disputes and differences that may arise between the parties hereto in respect of any of the covenants of this Agreement or any interpretation thereof and that are not resolved amicably shall be resolved by arbitration of a sole arbitrator appointed with the mutual consent of Consultant and the Client, who shall conduct the proceedings in accordance with the Arbitration and Conciliation Act, 1996 including any modification and re-enactment thereof in force from time to time. The seat of arbitration will be Pune.\n" +
                                        "    d. The governing laws shall be the laws prevailing in India.\n" );
                                        textView3.setText(
                                        "7. Legal Notice. All notices required or permitted under this Agreement shall be in writing and shall be deemed delivered when delivered in-person or deposited in the United States Postal Service via Certified Mail with return receipt. If different from the mailing address in Section I, enter below:\n" +
                                        "Client's Address: Office no 403, 4th Floor, East Court, Near Phoenix Market City, Viman Nagar Pune Maharashtra - 411014 India.\n" +
                                        "Attention\t:\tMr. Sanjeev Mittal\n" +
                                        "Telephone\t:\t9811039682\n" +
                                        "Email    \t:\tsanjeev.mittal@amstradworld.com\n" +
                                        "\n" +
                                        "Consultant's\n" +
                                                Html.fromHtml("Aadhaar  \t:\t"+"<b>"+adharNo+"</b>")+"\n" +  //"<b>" + id + "</b> " + name;'<b>' + substr + '</b>');
                                        "Attention\t:\t"+name+"\n" +
                                        "Telephone\t:\t"+mobileNo+"\n" +
                                        "Telephone\t:\t"+emailId+"\n"+"\n"+

                                        "8. Return of Records. Upon termination of this Agreement, the Consultant shall return all records, notes, and data of any nature that are in the Consultant's possession or under the Consultant's control and that are of the Client's property or relate to Client's business.\n" +

                                        "9. Waiver of Contractual Right. The failure of either party to enforce any provision of this Agreement shall not be construed as a waiver or limitation of that party's right to subsequently enforce and compel strict compliance with every provision of this Agreement.\n" +
                                               "10. Independent Status. The Consultant, is an independent consultant, shall not be deemed, the Client's employees.");  textView4.setText( "In its capacity as an independent Consultant, the Consultant agrees and represents:\n" +
                                        "a.) Consultant has the right to perform Services for others during the term of this Agreement;\n" +
                                        "b.) Consultant has the sole right to control and direct the means, manner, and method by which the Services required under this Agreement will be performed.\n" +
                                        "c.) Consultant shall be required to wear any uniforms provided by the Client.\n" +
                                        "11. Indemnification. Consultant shall release, defend, indemnify, and hold harmless Client and its officers, agents, and employees from all suits, actions, or claims of any character, name, or description including reasonable Consultant fees, brought on account of any injuries or damage, or loss (real or alleged) received or sustained by any person, persons, or property,arising out of services provided under this Agreement or Consultant's failure to perform or comply with any requirements of this Agreement including, but not limited to any claims for personal injury, property damage, or infringement of copyright, patent, or other proprietary rights. Client reserves the right to retain whatever funds which would be due to the Consultant under this Agreement until such suits, action or actions, claim or claims for injuries or damages as aforesaid shall have been settled and satisfactory evidence to that effect furnished."+
                                                "12. Confidentiality & Proprietary Information. The Consultant acknowledges that it will be necessary for the Client to disclose certain confidential and proprietary information to the Consultant in order for the Consultant to perform their duties under this Agreement. The Consultant acknowledges that disclosure to a third (3rd) party or misuse of this proprietary or confidential information would irreparably harm the Client.");
                                                textView5.setText("Accordingly, the Consultant will not disclose or use, either during or after the term of this Agreement, any proprietary or confidential information of the Client without the Client's prior written permission except to the extent necessary to perform the Services on the Client's behalf.\n" +
                                        "Proprietary or confidential information includes, but is not limited to:\n" +
                                        "a.) The written, printed, graphic, or electronically recorded materials furnished by Client for Consultant to use;\n" +
                                        "b.) Any written or tangible information stamped \"confidential,\" \"proprietary,\" or with a similar legend, or any information that Client makes reasonable efforts to maintain the secrecy of business or marketing plans or strategies, customer lists, inventories, sales projections and pricing information.\n"+


                                        "Upon termination of the Consultant's Services to the Client, or at the Client's request, the Consultant shall return all materials to the Client in the Consultant's possession relating to the Client's business. The Consultant acknowledges any breach or threatened breach of confidentiality under this Agreement will result in irreparable harm to the Client for which damages would be an inadequate remedy. Therefore, the Client shall be entitled to equitable relief, including an injunction, in the event of such breach or threatened breach of confidentiality. Such equitable relief shall be in addition to the Client's rights and remedies otherwise available at law.\n" +
                                        "13. Governing Law. This Agreement shall be governed under the laws in the State of Maharashtra Pune.\n");

                                        textView6.setText("14. Severability. This Agreement shall remain in effect in the event a section or provision is unenforceable or invalid. All remaining sections and provisions shall be deemed legally binding unless a court rules that any such provision or section is invalid or unenforceable, thus, limiting the effect of another provision or section. In such case, the affected provision or section shall be enforced as so limited.\n" +
                                        "\n" +
                                        "15. Entire Agreement. This Agreement, along with any attachments or addendums, represents the entire agreement between the parties. Therefore, this Agreement supersedes any prior agreements, promises, conditions, or understandings between the Client and Consultant. This Agreement may be modified or amended if the amendment is made in writing and is signed by both parties.\n" +
                                        "\n" +
                                        "\n" +
                                        "IN WITNESS WHEREOF, the Parties hereto have executed this Agreement on the dates written hereunder.\n" +
                                        "\n" +
                                        "\n" +
                                        "Digitally Signed Via OTP Print Name: "+name+"\n" +
                                        "\n" +
                                        "Date: "+formattedDate+"\n" +
                                        "\n" +
                                        "Print Name: "+name+"\n" +
                                        "\n" +
                                        "\n" +
                                        "Digitally Signed by Print Name: "+name+"\n" +
                                        "\n" +
                                        "Date: "+dateWithTime);
                                      //  "Company can modify Annexure-I at any time without giving any notice and the update will be available on Amstrad Promax Mobile Application. Once accepted by the consultant new rates will be applicable.");


                                pd.dismiss();


                            }else {
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
                    params.put("ISDCode",ISDCOde);

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
            Toast.makeText(Agreement.this, "Oops....Registration Failed Try Later.", Toast.LENGTH_LONG).show();
        }


        IAgrree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              updateAgreement(ISDCOde);
//                bitmap = loadBitmapFromView(textView, textView.getWidth(), textView.getHeight());
                bitmap = loadBitmapFromView(linearLayout, linearLayout.getWidth(), linearLayout.getHeight());
//                bitmap = loadBitmapFromView(linearLayout, 800,100);
//                createPdf(textView.getText().toString());
                bitmap2 = loadBitmapFromView(textView2, textView2.getWidth(), textView2.getHeight());
                bitmap3 = loadBitmapFromView(textView3, textView3.getWidth(), textView3.getHeight());
                bitmap4 = loadBitmapFromView(textView4, textView4.getWidth(), textView4.getHeight());
                bitmap5 = loadBitmapFromView(textView5, textView5.getWidth(), textView5.getHeight());
                bitmap6 = loadBitmapFromView(textView6, textView5.getWidth(), textView6.getHeight());
//                bitmap7 = loadBitmapFromView(rl,rl.getWidth(),rl.getHeight());

                createPdf(linearLayout);

//                bitmap = loadBitmapFromView(llScroll, llScroll.getWidth(), llScroll.getHeight());
//                createPdf();

            }
        });


    }


public void updateAgreement(String new_isd){
    try {
        pd = new ProgressDialog(Agreement.this);

        pd.setTitle("Request sending");
        pd.setMessage("Please wait to get details..");
        pd.show();
        //  String url = "http://ovotapplive.poshsmetal.com/index.asmx/GetISDDetails";
       // String url = "http://ovotappdev.poshsmetal.com/index.asmx/UpdateISDDAgreement";
        String url = Url.UPDATE_ISD_AGREEMENT_URL;
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
                    JSONArray customerdetail = j.getJSONArray("ISDCode");
                    for (int i = 0; i < customerdetail.length(); i++) {
                        JSONObject isdDetails = customerdetail.getJSONObject(i);
                        //userId = userd.getString("UserId");
                        message = isdDetails.getString("message");

                        if (message.equals("Success"))
                        {

                            new message1(SENDMAIL).execute();

                           sf.createAgreementSession("done");
                            Intent intent = new Intent(Agreement.this,DashboardAcitivity.class);
                            startActivity(intent);
                            finish();


                            pd.dismiss();


                        }else {
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
                params.put("ISDCode",new_isd);
                params.put("AcceptanceStatus","yes");
                params.put("UserCode",new_isd);
                params.put("IPAdd","abc");

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
        Toast.makeText(Agreement.this, "Oops....Registration Failed Try Later.", Toast.LENGTH_LONG).show();
    }
}


//    private void createPdf(String sometext){
      private void createPdf(View view){
        // create a new document
        PdfDocument document = new PdfDocument();


          //Document document1 = new Document(PageSize.A4);



//          PdfDocument.PageInfo pageInfo;
//          int noOfPages = (int)Math.floor(content.getHeight()/1000)+1;
//          for (int i=1;i<=noOfPages;i++) {
//              pageInfo = new PdfDocument.PageInfo.Builder
//                      (800,1000,i).create();
//              PdfDocument.Page page = document.startPage(pageInfo);
//              content.draw(page.getCanvas());
//              document.finishPage(page);
//          }
        // write the document content


//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(textView.getWidth(), textView.getHeight(), 1).create();
          PdfDocument.PageInfo pageInfo;

//           int noOfPages = (int)Math.floor(linearLayout.getHeight()/1000)+1;
//          for (int i = 1; i<=noOfPages;i++)
//          {
//              pageInfo = new PdfDocument.PageInfo.Builder(800, 1000, i).create();
              pageInfo = new PdfDocument.PageInfo.Builder(textView.getWidth(), textView.getHeight(), 1).create();
//          pageInfo = new PdfDocument.PageInfo.Builder(800, 1000, 1).create();


              PdfDocument.Page page = document.startPage(pageInfo);

              Canvas canvas = page.getCanvas();

              Paint paint = new Paint();
//        paint.setStrokeWidth(3);
              paint.setColor(Color.WHITE);
              paint.setTextSize(12.0f);

              // paint.setColor(ContextCompat.getColor(this, R.color.UIBlue));
              canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap,textView.getWidth(), textView.getHeight(), true);
//              bitmap = Bitmap.createScaledBitmap(bitmap,800, 1000, true);


              canvas.drawBitmap(bitmap, 0, 0 , paint);
              document.finishPage(page);

         // }
          ////////////////////////////////////////
          pageInfo = new PdfDocument.PageInfo.Builder(textView2.getWidth(),textView2.getHeight(), 2).create();
//          pageInfo = new PdfDocument.PageInfo.Builder(800,1000, 2).create();

           page = document.startPage(pageInfo);
           canvas = page.getCanvas();

           paint = new Paint();
//        paint.setStrokeWidth(3);
          paint.setColor(Color.WHITE);
          paint.setTextSize(12.0f);

          // paint.setColor(ContextCompat.getColor(this, R.color.UIBlue));
          canvas.drawPaint(paint);

          bitmap2 = Bitmap.createScaledBitmap(bitmap2,textView2.getWidth(), textView2.getHeight(), true);
//              bitmap = Bitmap.createScaledBitmap(bitmap,800, 1000, true);


          canvas.drawBitmap(bitmap2, 0, 0 , paint);
          document.finishPage(page);
        /////////////////////
          pageInfo = new PdfDocument.PageInfo.Builder(textView3.getWidth(),textView3.getHeight(), 3).create();
//          pageInfo = new PdfDocument.PageInfo.Builder(800,1000, 3).create();


          page = document.startPage(pageInfo);
          canvas = page.getCanvas();

          paint = new Paint();
//        paint.setStrokeWidth(3);
          paint.setColor(Color.WHITE);
          paint.setTextSize(12.0f);

          // paint.setColor(ContextCompat.getColor(this, R.color.UIBlue));
          canvas.drawPaint(paint);

          bitmap3 = Bitmap.createScaledBitmap(bitmap3,textView3.getWidth(), textView3.getHeight(), true);
//              bitmap = Bitmap.createScaledBitmap(bitmap,800, 1000, true);


          canvas.drawBitmap(bitmap3, 0, 0 , paint);
          document.finishPage(page);
          ////////////////////////////////
          pageInfo = new PdfDocument.PageInfo.Builder(textView4.getWidth(),textView4.getHeight(), 4).create();
//          pageInfo = new PdfDocument.PageInfo.Builder(800,1000, 4).create();

          page = document.startPage(pageInfo);
          canvas = page.getCanvas();

          paint = new Paint();
//        paint.setStrokeWidth(3);
          paint.setColor(Color.WHITE);
          paint.setTextSize(12.0f);

          // paint.setColor(ContextCompat.getColor(this, R.color.UIBlue));
          canvas.drawPaint(paint);

          bitmap4 = Bitmap.createScaledBitmap(bitmap4,textView4.getWidth(), textView4.getHeight(), true);
//              bitmap = Bitmap.createScaledBitmap(bitmap,800, 1000, true);


          canvas.drawBitmap(bitmap4, 0, 0 , paint);
          document.finishPage(page);
          //////////////////////////////
          pageInfo = new PdfDocument.PageInfo.Builder(textView5.getWidth(),textView5.getHeight(), 5).create();
//          pageInfo = new PdfDocument.PageInfo.Builder(800,1000, 5).create();

          page = document.startPage(pageInfo);
          canvas = page.getCanvas();

          paint = new Paint();
//        paint.setStrokeWidth(3);
          paint.setColor(Color.WHITE);
          paint.setTextSize(12.0f);

          // paint.setColor(ContextCompat.getColor(this, R.color.UIBlue));
          canvas.drawPaint(paint);

          bitmap5 = Bitmap.createScaledBitmap(bitmap5,textView5.getWidth(), textView5.getHeight(), true);
//              bitmap = Bitmap.createScaledBitmap(bitmap,800, 1000, true);


          canvas.drawBitmap(bitmap5, 0, 0 , paint);
          document.finishPage(page);
          /////////////////////////////
//          pageInfo = new PdfDocument.PageInfo.Builder(800,1000, 6).create();
          pageInfo = new PdfDocument.PageInfo.Builder(textView6.getWidth(),textView6.getHeight(), 6).create();

          page = document.startPage(pageInfo);
          canvas = page.getCanvas();

          paint = new Paint();
//        paint.setStrokeWidth(3);
          paint.setColor(Color.WHITE);
          paint.setTextSize(12.0f);

          // paint.setColor(ContextCompat.getColor(this, R.color.UIBlue));
          canvas.drawPaint(paint);

          bitmap6 = Bitmap.createScaledBitmap(bitmap6,textView6.getWidth(), textView6.getHeight(), true);
//              bitmap = Bitmap.createScaledBitmap(bitmap,800, 1000, true);


          canvas.drawBitmap(bitmap6, 0, 0 , paint);
          document.finishPage(page);


        directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
         targetPdf = directory_path+"test-2.pdf";
        File filePath = new File(targetPdf);
        try {

            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();

    }
    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }


    public class message1 extends AsyncTask
    {

        String MAIL;
        public message1(String MAIL)
        {

            this.MAIL = MAIL;
        }

        @Override
        protected Object doInBackground(Object[] params)

        {
            String subjectline = "Promax Agreement"+" , "+name+" , "+dateWithTime  ;
            // subjectline = "FTPR Checkpoints:" + " " + imagePath ;
//                bodyDetails = name.trim() + ","
//                        + vehicleId.trim() + ","
//                        + vehicleNo.trim() + ","
//                        + checkPointId.trim() + ","
//                        + checkPointName.trim() + "\n";
//                Log.e("bodydetails", bodyDetails);
//                Log.e("Subjectline", subjectline);

            TWsimpleMailSender mTWsimpleMailSender = new TWsimpleMailSender(Agreement.this, "75302", "transworld", "a.mobileeye.in", "2525");

            try
            {
                     // info@amstradworld.com
                boolean flag = mTWsimpleMailSender.sendMail(subjectline, MAIL, "8805", "info@amstradworld.com,tlsoft@twtech.in", targetPdf);
//                boolean flag = mTWsimpleMailSender.sendMail(subjectline, MAIL, "8805", "tlsoft@twtech.in", targetPdf);
                Log.e("Main2", "doInBackground: "+flag);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Main2", "Insidecatch: "+e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute()
        {



            Log.i("DD","IN THE PRE EXCECUTE ");

            super.onPreExecute();
        }



    }

}