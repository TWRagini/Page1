package com.example.page1;

import android.app.Application;
import android.location.GpsStatus;
import android.location.LocationListener;
import android.location.LocationManager;

import java.util.ArrayList;

/**
 * Created by twtech on 3/2/18.
 */

public class GlobalVariable extends Application {

    private int updatedOTP;
    private int updateCount;
    private String userId;


    public int getUpdatedOTP() {
        return updatedOTP;
    }

    public void setUpdatedOTP(int updatedOTP) {
        this.updatedOTP = updatedOTP;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

}
