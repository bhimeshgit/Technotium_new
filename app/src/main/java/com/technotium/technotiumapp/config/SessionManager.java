package com.technotium.technotiumapp.config;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class SessionManager {

    // Shared Preferences
    SharedPreferences pref,prefnoti;
    SharedPreferences.Editor editor,editornoti;
    ProgressDialog pDialog;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    private static  SessionManager myInstance;
    private static Context myContext;
    public SessionManager(Context context){
        myContext=context;
        pref = context.getSharedPreferences("TechotiumApp", PRIVATE_MODE);
        editor = pref.edit();
        pDialog = new ProgressDialog(myContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
    }

    public static synchronized SessionManager getMyInstance(Context context){
        if (myInstance==null) {
            myInstance=new SessionManager( context);
        }
        return  myInstance;
    }

    public void setEmpName(String name) {
        editor.putString("ename", name);
        editor.commit();
    }

    public String getEmpName() {
        return pref.getString("ename", "");
    }

    public void setEmpImage(String name) {
        editor.putString("emp_image", name);
        editor.commit();
    }

    public String getEmpImage() {
        return pref.getString("emp_image", "");
    }

    public void setEmpid(String id) {
        editor.putString("id", id);
        editor.commit();
    }
    public String getEmpid() {
        return pref.getString("id", "");
    }

    public void setFirebaseId(String id) {
        editor.putString("fid", id);
        editor.commit();
    }
    public String getFirebaseId() {
        return pref.getString("fid", "");
    }


    public void setEmpType(String id) {
        editor.putString("empType", id);
        editor.commit();
    }
    public String getEmpType() {
        return pref.getString("empType", "");
    }


    public void setEmpMobile(String mobile) {
        editor.putString("empMobile", mobile);
        editor.commit();
    }
    public String getEmpMobile() {
        return pref.getString("empMobile", "");
    }
    public void setEmpPass(String pass) {
        editor.putString("empPass", pass);
        editor.commit();
    }
    public String getEmpPass() {
        return pref.getString("empPass", "");
    }
    //******************* CLEAR ALL SHARED PREFERENCE IN THIS CLASS***************//

    public void clearPreference()
    {
        Map<String,?> prefs = pref.getAll();
        for(Map.Entry<String,?> prefToReset : prefs.entrySet()){
            editor.remove(prefToReset.getKey()).commit();
        }
    }

    public  void progressShow(){
        pDialog.show();
    }
    public  void progressHide(){
        pDialog.dismiss();
    }

} // SessionManager
