package com.example.myproject22.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    public static String getNetworkState(Context context){
        String status = null;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null){

            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                status = "Đã kết nối wifi";
            }
            else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                status = "Đã kết nối dữ liệu di động";
            }

        } else {
            //1 means Chưa kết nối internet
            status = "1";
        }

        return status;
    }
}
