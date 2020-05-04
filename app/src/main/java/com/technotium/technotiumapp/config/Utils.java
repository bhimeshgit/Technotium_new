package com.technotium.technotiumapp.config;


import android.content.Context;
import android.os.Build;
import android.widget.Toast;

public class Utils {

    public static void showToastLongMsg(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
    public static void showToastShortMsg(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static int getPostDataLimit() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return InAppConstant.VALUE_POST_DATA_LIMITS_BELOW_LOLLIPOP;
        return InAppConstant.VALUE_POST_DATA_LIMITS;
    }
}
