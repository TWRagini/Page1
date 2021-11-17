package com.example.page1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Deepali Shinde on 15/4/18.
 */

public class SessionManager {
    // Shared Preferences
     public  SharedPreferences pref;
    // Editor for Shared preferences
    public SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "AndroidHivePref";
//    private static final String IS_REGISTER = "IsRegistered";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_AGREEMENT = "IsAgreement";
//    public static final String KEY_NAME = " name";
//    public static final String KEY_LASTNAME = " lastname";
//    public static final String KEY_EMAIL = "email";
//    public static final String KEY_PHONE = "phone";
//    public static final String KEY_UNAME = "username";
//    public static final String KEY_UPASS = "userpass";
//    public static final String KEY_UNITID = "unitid";
//    public static final String KEY_COMPCODE = "compCode";
//    public static final String IS_TRACK_ENABLE = "Track";
//    public static final String IS_TRIP_CANCELLED = "Cancel";
//    public static final String KEY_VEHCODE = "vehcode";
//    public static final String TAG_ACTIVITY = "session";
//    public static final String Trip_start_Session = "tripstart";
//    public static final String Trip_end_Session = "tripend";
//    public static final String KEY_DATE = "submitdate";
//    public static final String CHECKBOX1 = "checkbox1";
//    public static final String CHECKBOX2 = "checkbox2";
//    public static final String CHECKBOX3 = "checkbox3";
//    public static final String CHECKBOX4 = "checkbox4";
//    public static final String KEY_IMEINO = "imeino";
//    public static final String KEY_SUBMIT_STATUS = "submitstatus";


    public static final String KEY_MOBILE = "mobileNo";
    public static final String KEY_USERID = "userID";
    public static final String KEY_AGREEMENT = "agreement";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    /**
     * Create login session
     * */
//    public void createRegisterSession(String name, String lastname,String comCode, String email,String phone,String password,String unitId,String vehCode,String imeiNo){
//        editor.putBoolean(IS_REGISTER, true);
//        editor.putString(KEY_NAME, name);
//        editor.putString(KEY_LASTNAME, lastname);
//        editor.putString(KEY_EMAIL, email);
//        editor.putString(KEY_PHONE, phone);
//        editor.putString(KEY_UPASS, password);
//        editor.putString(KEY_COMPCODE, comCode);
//        editor.putString(KEY_UNITID,unitId);
//        editor.putString(KEY_VEHCODE,vehCode);
//        editor.putString(KEY_IMEINO,imeiNo);
//        editor.commit();
//
//    }
//
//    public void create_Track_Session(boolean status){
//        editor.putBoolean(IS_TRACK_ENABLE,status);
//        editor.commit();
//    }
//
//    public void create_health_session(String date){
//        editor.putString(KEY_DATE,date);
//       // editor.putBoolean(KEY_SUBMIT_STATUS,status);
//        editor.commit();
//    }
//
//    public void create_checkbox_Session(String checkbox1,String checkbox2,String checkbox3,String checkbox4){
//        editor.putString(CHECKBOX1,checkbox1);
//        editor.putString(CHECKBOX2,checkbox2);
//        editor.putString(CHECKBOX3,checkbox3);
//        editor.putString(CHECKBOX4,checkbox4);
//        editor.clear();
//        editor.commit();
//    }
//
//    public void show_Trip_Session(String startDate,String endDate){
//        editor.putString(Trip_start_Session,startDate);
//        editor.putString(Trip_end_Session,endDate);
//        editor.commit();
//    }
//
    public void createLoginSession(String mobNo, String userId){
        Log.e("createLoginSession ","method called");
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_MOBILE, mobNo);
        editor.putString(KEY_USERID, userId);
        Log.e("createLoginSession ","mobile and userId.." +mobNo + ", "+userId );
        editor.commit();
        Log.e("createLoginSession ","session created");
    }

    public void createAgreementSession(String status){
        Log.e("createLoginSession ","method called");
        editor.putBoolean(IS_AGREEMENT, true);
        editor.putString(KEY_AGREEMENT, status);
        Log.e("createAgreementSession ","mobile and userId.." +status );
        editor.commit();
        Log.e("createLoginSession ","session created");
    }


//    public void checkRegister(){
//        Log.e("Entered in ","checkRegister");
//        Log.e("IS Register", String.valueOf(isRegistered()));
//        String compnm="";
//        if(!this.isRegistered()){
//            //addAutoStartup();
//            Log.e("Inside if","If called");
//            String uDB= Environment.getExternalStorageDirectory().getPath() + "/TWDragondroidDB/UserInfo.db";
//            Log.e("uDB",uDB);
//            File fileDB=new File(uDB);
//            if (fileDB.exists()){
//                //Log.e("Database ","Exists");
//                try {
//                    String uData = new DatabaseOperation(_context).retrieveUserDetailsData();
//                    Log.e("User Data", "" + uData);
//                    String allUData[] = uData.split("%");
//                    String nm = allUData[0];
//                    String email = allUData[1];
//                    String phone = allUData[2];
//                    String address = allUData[3];
//                    String otpStatus = allUData[5];
//                    String lastnm = allUData[6];
//                    String password = allUData[8];
//                    if (compnm==null||compnm==""){
//                        compnm="";
//                        Log.e("IFFCOMP",compnm);
//                    }else {
//                        compnm = allUData[7];
//                        Log.e("ELSECOMP",compnm);
//                    }
//                    Log.e("OTP", otpStatus);
//                    //if (otpStatus.equals("PENDING")) {
//                    Log.e("Data After ", "after splitting " + nm + ", " + email + ", " + phone);
//                    Intent i = new Intent(_context, RegisterActivity.class);
//                    Log.e("Intent called", "Called");
//                    i.putExtra("name", nm);
//                    i.putExtra("lastname",lastnm);
//                    i.putExtra("email", email);
//                    i.putExtra("phone", phone);
//                    i.putExtra("address", address);
//                    i.putExtra("compCode", compnm);
//                    i.putExtra("password", password);
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    _context.startActivity(i);
//                    Log.e("IFF", "otpStatus");
//
//                }catch (Exception e){
//                     new MyLogger().storeMassage("SessionManager","retrieveUserDetailsData Ex "+e.getMessage());
//                    Log.e("Inside if catch",e.getMessage());
//                }
//
//            }else {
//                //Log.e("Entered in", " isRegistered");
//                Intent i = new Intent(_context, RegisterActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                _context.startActivity(i);
//                Log.e("Inside else","Called");
//            }
//
//        }else {
//
//           checkLogin();
//            Log.e("Inside else2","Called");
//        }
//    }


//    public boolean check_TrackStatus(){
//
//        Log.e("Check TrackStatus ",": "+isTrackEnable());
//        if(this.isTrackEnable()==true){
//            return true;
//        }else{
//            return false;
//        }
//    }

    public void checkLogin(){

        if(!this.isLoggedIn()){
            //Log.e("Entered in ","isLoggedIn");
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Log.e("createLoginSession ","Not logged in");
            _context.startActivity(i);


        }else {
            //Log.e("Entered in ","checkLoginElse");
            Intent inMain=new Intent(_context,Intermediate.class);
            inMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            inMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Log.e("createLoginSession ","Logged in");
            _context.startActivity(inMain);
        }
    }
    ////////////////////////////

    public void checkAgreement(){

        if(this.isAgreement()){
            //Log.e("Entered in ","isLoggedIn");
            Intent i = new Intent(_context, DashboardAcitivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Log.e("createLoginSession ","Not logged in");
            _context.startActivity(i);


      }
//        else {
//            Intent inMain=new Intent(_context,DashboardAcitivity.class);
//            inMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            inMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            Log.e("createLoginSession ","Logged in");
//            _context.startActivity(inMain);
//        }
    }

    /**
     * Get stored session data
     * */
//    public HashMap<String, String> getUserDetails(){
//        HashMap<String, String> user = new HashMap<String, String>();
//        // user name
//        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
//        user.put(KEY_LASTNAME, pref.getString(KEY_LASTNAME, null));
//        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
//        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));
//        user.put(KEY_UPASS, pref.getString(KEY_UPASS, null));
//        user.put(KEY_UNITID, pref.getString(KEY_UNITID, null));
//        user.put(KEY_COMPCODE, pref.getString(KEY_COMPCODE, null));
//        user.put(KEY_VEHCODE,pref.getString(KEY_VEHCODE,null));
//        user.put(KEY_IMEINO,pref.getString(KEY_IMEINO,null));
//        user.put(IS_TRIP_CANCELLED,pref.getString(IS_TRIP_CANCELLED,null));
//        user.put(Trip_start_Session,pref.getString(Trip_start_Session, null));
//        user.put(Trip_end_Session,pref.getString(Trip_end_Session,null));
//        return user;
//    }
//
//    public HashMap<String, String> getHeathSubmitDetails(){
//        HashMap<String, String> user = new HashMap<String, String>();
//        // user name
//        user.put(KEY_DATE, pref.getString(KEY_DATE, null));
//        //user.put(KEY_SUBMIT_STATUS, pref.getString(KEY_SUBMIT_STATUS, null));
//        return user;
//    }
//
//    public HashMap<String, String> getCheckboxAns(){
//        HashMap<String, String> user = new HashMap<String, String>();
//        // user name
//        user.put(CHECKBOX1, pref.getString(CHECKBOX1, null));
//        user.put(CHECKBOX2, pref.getString(CHECKBOX2, null));
//        user.put(CHECKBOX3, pref.getString(CHECKBOX3, null));
//        user.put(CHECKBOX4, pref.getString(CHECKBOX4, null));
//        return user;
//    }


    // Get Login State
//    public boolean isRegistered(){
//
//        return pref.getBoolean(IS_REGISTER, false);
//    }
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
//    public boolean isTrackEnable(){
//        return pref.getBoolean(IS_TRACK_ENABLE,false);
//    }

    public boolean isAgreement(){
        return pref.getBoolean(IS_AGREEMENT, false);
    }

}
