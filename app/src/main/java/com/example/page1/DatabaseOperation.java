package com.example.page1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by twtech on 5/2/18.
 */

public class DatabaseOperation {
    Context mContext;
    SQLiteDatabase database; //getApplicationContext().getExternalFilesDir("")
    String otpDB = Environment.getExternalStorageDirectory().getPath() + "/TWAmstradDB/otpInfo.db";
    String ProductDatabaseName = Environment.getExternalStorageDirectory().getPath() + "/TWAmstradDB/DDProductData.db";

    String incentiveDatabaseName = Environment.getExternalStorageDirectory().getPath() + "/TWAmstradDB/DDIncentiveData.db";



    String uDB = Environment.getExternalStorageDirectory().getPath() + "/TWAmstradDB/userCode.db";

    public DatabaseOperation(Context context) {
        this.mContext = context;
    }

    public void userDetail(String mNo, int getOTP, String otpStatus) {
        // new MyLogger().storeMassage("userDetails method", "Called");
        try {
            SQLiteDatabase database = mContext.openOrCreateDatabase(otpDB, mContext.MODE_PRIVATE, null);
            String sqlQuery1 = "create table if not exists otp (mobileNo integer(20),userotp varchar(10),otpStatus varchar(10))";
            database.execSQL(sqlQuery1);
            database.execSQL("insert into otp (mobileNo,userotp,otpStatus) values('" + mNo + "','" + getOTP + "','" + otpStatus + "')");
            database.close();
            Log.e("OTP successfully ", "Inserted ! ! ! !" + getOTP);
            // new MyLogger().storeMassage("insert details", "Successfully");
        } catch (SQLException e) {
            //new MyLogger().storeMassage(Tag,"Exception in userDetail() "+e.getMessage());

            Log.e("Exception while OTP ", e.getMessage());
        }
    }

    public void updateOTP2(String mNO, int otp, String otpStatus) {
        //Log.e("Data to be post",""+nm+", "+email+", "+phone+", "+compC+", "+address+", "+otp+","+otpStatus);
        // try {
        SQLiteDatabase database = mContext.openOrCreateDatabase(otpDB, mContext.MODE_PRIVATE, null);
        database.execSQL("update otp set  mobileNo='" + mNO + "', userotp='" + otp + "', otpStatus='" + otpStatus + "'");
        database.close();
        Log.e("OTP  updated  ", "successfully ! ! ! !");
//        }catch (Exception e){
//           // new MyLogger().storeMassage(Tag,"Exception in updateOTP2() "+e.getMessage());
//
//            Log.e("Exception while ","Updating OTP");
//        }
    }
///////////////////////////

    //////////////////////////////////////


    public void storeProductData(String productCat, String distributerName, String model, String productCode, String subCat, String incentiveAmt, String dealerCode, String serialNo) {
        //  String ProductDatabaseName = Environment.getExternalStorageDirectory().getPath() + "/TWAmstradDB/DDProductData.db";
        try {
            String tableName = "productTB";
            database = mContext.openOrCreateDatabase(ProductDatabaseName, mContext.MODE_PRIVATE, null);
            String sqlQuery1 = "create table if not exists " + tableName + "(SrNo INTEGER PRIMARY KEY AUTOINCREMENT, ProductCat varchar,  DistributerName varchar,Model varchar,ProductCode varchar,SubCat varchar,IncentiveAmt varchar,DealerCode varchar,SerialNo varchar, Actn varchar DEFAULT 'X')";
            database.execSQL(sqlQuery1);
            database.execSQL("insert into " + tableName + "(ProductCat,DistributerName,Model,ProductCode,SubCat,IncentiveAmt,DealerCode,SerialNo) values('" + productCat + "','" + distributerName + "','" + model + "','" + productCode + "','" + subCat + "','" + incentiveAmt + "','" + dealerCode + "','" + serialNo + "')");
            database.close();
        } catch (Exception e) {
            Log.e("not store Product data", e.getMessage());
            // new MyLogger().storeMassage(Tag,"Exception in storeIncidentData() "+e.getMessage());

        }
    }

///////////////////////////////////////////////////////


    public String[] RetrieveRegData() {


       // String vehicleData = Environment.getExternalStorageDirectory().getPath() + "/VehicleFTPRDB/VehicleInfo.db";
      //  String productDatabaseName = Environment.getExternalStorageDirectory().getPath() + "/TWAmstradDB/DDProductData.db";

        String returnData = "finished";
        StringBuffer sb, sb1;

       // File dbFile = mContext.getDatabasePath(ProductDatabaseName);



        database = mContext.openOrCreateDatabase(ProductDatabaseName, Context.MODE_PRIVATE, null);


        // if (totalCount != 0) {

        // String sqlQuery = "select vehiclName from  vehicle  where Status='Null' order by SrNo desc limit 100";
        String sqlQuery = "select * from  productTB limit 100";

        Cursor cr = database.rawQuery(sqlQuery, null);
////////////////////////////////
        String[] array = new String[cr.getCount()];
        int i = 0;

            while (cr.moveToNext()) {

                @SuppressLint("Range") String srNo = cr.getString(cr.getColumnIndex("SrNo"));
                @SuppressLint("Range") String productCat = cr.getString(cr.getColumnIndex("ProductCat"));
                @SuppressLint("Range") String model = cr.getString(cr.getColumnIndex("Model"));
                @SuppressLint("Range") String subCat = cr.getString(cr.getColumnIndex("SubCat"));
                @SuppressLint("Range") String 	incentiveAmt = cr.getString(cr.getColumnIndex("IncentiveAmt"));
                @SuppressLint("Range") String 	serialNo = cr.getString(cr.getColumnIndex("SerialNo"));
                @SuppressLint("Range") String 	actn = cr.getString(cr.getColumnIndex("Actn"));

                array[i] = srNo+","+productCat+","+model+","+subCat+","+incentiveAmt+","+serialNo+","+actn ;
                i++;

        }
//////////////////////////////////////

        Log.e("Database", "RetrieveRegData: " + array );
        return array;

    }
    //?????????????????????????????????????????????????//


    public void deleteRow(String sr_num) {

        Log.e("deleteOLD Record method", "Called");
        //String tableName = "productTB";

        File dbFile2 = mContext.getDatabasePath(ProductDatabaseName);

        if (dbFile2.exists()) {

            try {
                Log.e("deleteOLD Record method", "Called .."+sr_num );
                SQLiteDatabase db2 = mContext.openOrCreateDatabase(ProductDatabaseName, mContext.MODE_PRIVATE, null);
                String deleteQuery = "DELETE FROM productTB WHERE SerialNo = '" + sr_num + "'";
                db2.execSQL(deleteQuery);
                db2.close();
                Log.e("Records Deleted ", "successfully!!!!");
               // return true;
            } catch (Exception e) {

                Log.e("Exception While ", "Deleting records : " + e.getMessage());
                // new MyLogger().storeMassage("Exception while ","Deleting Record : "+e.getMessage());
            }
        }
       // return false;

    }


    public void deleteUser(File productDB) {
        // SQLiteDatabase database = mContext.openOrCreateDatabase(uDB, mContext.MODE_PRIVATE, null);
        SQLiteDatabase.deleteDatabase(productDB);

    }


    public void deleteIncentiveDB(File incentiveDB) {
        // SQLiteDatabase database = mContext.openOrCreateDatabase(uDB, mContext.MODE_PRIVATE, null);
        Log.e("DatabaseOperation", "deleteIncentiveDB: "+ "successfully" );
        SQLiteDatabase.deleteDatabase(incentiveDB);

    }



    public String[] RetrieveProductCode() {

        database = mContext.openOrCreateDatabase(ProductDatabaseName, Context.MODE_PRIVATE, null);

        String sqlQuery = "select * from  productTB limit 100";

        Cursor cr = database.rawQuery(sqlQuery, null);
        String[] array = new String[cr.getCount()];
        int i = 0;

        while (cr.moveToNext()) {

            @SuppressLint("Range") String productCode = cr.getString(cr.getColumnIndex("ProductCode"));


            array[i] = productCode ;
            i++;

        }
//////////////////////////////////////

        Log.e("Database", "RetrieveproductCode: " + array );
        return array;

    }



    public void storeIncentiveData(String productSrNo,String model,String invoiceNo,String saleDate,String incentiveAmt,String status,String reason,String customer,String approveBy,String approveDate,String dealer,String phone) {
        //  String ProductDatabaseName = Environment.getExternalStorageDirectory().getPath() + "/TWAmstradDB/DDProductData.db";
        try {
            String tbName = "incentiveTB";
            database = mContext.openOrCreateDatabase(incentiveDatabaseName, mContext.MODE_PRIVATE, null);
            String sqlQuery1 = "create table if not exists " + tbName + "(SrNo INTEGER PRIMARY KEY AUTOINCREMENT, ProductSrNo varchar,  Model varchar,InvoiceNo varchar,SaleDate varchar,IncentiveAmt varchar,Status varchar,Reason varchar,Customer varchar, ApproveBy varchar,ApproveDate varchar,Dealer varchar,Phone varchar )";
            database.execSQL(sqlQuery1);
            database.execSQL("insert into " + tbName + "(ProductSrNo,Model,InvoiceNo,SaleDate,IncentiveAmt,Status,Reason,Customer,ApproveBy,ApproveDate,Dealer,Phone) values('" + productSrNo + "','" + model + "','" + invoiceNo + "','" + saleDate + "','" + incentiveAmt + "','" + status + "','" + reason + "','" + customer + "','" + approveBy + "','" + approveDate + "','" + dealer + "','" + phone + "')");
            database.close();
        } catch (Exception e) {
            Log.e("IncentiveDatabase", e.getMessage());
            // new MyLogger().storeMassage(Tag,"Exception in storeIncidentData() "+e.getMessage());

        }
    }

    ///////////////////////////////////////////////////////
    public String[] RetrieveIncentive() {

        database = mContext.openOrCreateDatabase(incentiveDatabaseName, Context.MODE_PRIVATE, null);


        String sqlQuery = "select * from incentiveTB limit 100";

        Cursor cr = database.rawQuery(sqlQuery, null);
////////////////////////////////
        String[] array = new String[cr.getCount()];
        int i = 0;

        //ProductSrNo,Model,InvoiceNo,SaleDate,IncentiveAmt,Status,Reason,Customer,ApproveBy,ApproveDate,Dealer,Phone
        while (cr.moveToNext()) {

            @SuppressLint("Range") String srNo = cr.getString(cr.getColumnIndex("SrNo"));
            @SuppressLint("Range") String productSrNo = cr.getString(cr.getColumnIndex("ProductSrNo"));
            @SuppressLint("Range") String model = cr.getString(cr.getColumnIndex("Model"));
            @SuppressLint("Range") String invoiceNo = cr.getString(cr.getColumnIndex("InvoiceNo"));
            @SuppressLint("Range") String saleDate = cr.getString(cr.getColumnIndex("SaleDate"));
            @SuppressLint("Range") String 	incentiveAmt = cr.getString(cr.getColumnIndex("IncentiveAmt"));
            @SuppressLint("Range") String 	status = cr.getString(cr.getColumnIndex("Status"));


            array[i] = srNo+","+productSrNo+","+model+","+invoiceNo+","+saleDate+","+incentiveAmt+","+status ;
            i++;

        }
//////////////////////////////////////

        Log.e("Database", "RetrieveRegData: " + array );
        return array;

    }


    @SuppressLint("Range")
    public String getReason(String a) {
        String details = "";
        SQLiteDatabase database = mContext.openOrCreateDatabase(incentiveDatabaseName, mContext.MODE_PRIVATE, null);

        // String sqlQuery = "select vehicleCode from  vehicle where vehiclName = " +vehicleNo+ " ";

        String sqlQuery = "SELECT Reason FROM 'incentiveTB' where ProductSrNo ='" +a+ "'LIMIT 0,1";

        // SELECT vehicleCode FROM 'vehicle' where vehiclName = 'GJ 27 TT 0258' LIMIT 0,1

        Cursor cr = database.rawQuery(sqlQuery, null);
        cr.moveToFirst();
        details = cr.getString(cr.getColumnIndex("Reason"));
        cr.close();
        return details;
    }



    public void storeUserCode(String uCode) {
        // new MyLogger().storeMassage("userDetails method", "Called");
      //  try {
            String uDB = Environment.getExternalStorageDirectory().getPath() + "/TWAmstradDB/userCode.db";
            SQLiteDatabase database = mContext.openOrCreateDatabase(uDB, mContext.MODE_PRIVATE, null);
            String sqlQuery1 = "create table if not exists user (SrNo INTEGER PRIMARY KEY AUTOINCREMENT,userCode varchar(20))";
            database.execSQL(sqlQuery1);
            database.execSQL("insert into user (userCode) values('" + uCode + "')");
            database.close();
            // new MyLogger().storeMassage("insert details", "Successfully");
//        } catch (Exception e) {
//            // new MyLogger().storeMassage(Tag,"Exception in userDetail() "+e.getMessage());
//
//            Log.e("Exception while insert ", e.getMessage());
//        }
    }


    public void updateUserCode(String uCode) {
        try {
            SQLiteDatabase database = mContext.openOrCreateDatabase(uDB, mContext.MODE_PRIVATE, null);
            database.execSQL("update user set userCode='" + uCode + "'");
            database.close();
            Log.e("OTP  updated  ", "successfully ! ! ! !");
        } catch (Exception e) {
           // new MyLogger().storeMassage(Tag, "Exception in updateOTP2() " + e.getMessage());

            Log.e("Exception while ", "Updating userCode");
        }
    }


    @SuppressLint("Range")
    public String retrieveUserCode() {
        String uCode;
        SQLiteDatabase database = mContext.openOrCreateDatabase(uDB, mContext.MODE_PRIVATE, null);

        String sqlQuery = "select userCode from  user ";

        Cursor cr = database.rawQuery(sqlQuery, null);
        cr.moveToFirst();
        uCode = cr.getString(cr.getColumnIndex("userCode"));
        cr.close();
        return uCode;
    }

}