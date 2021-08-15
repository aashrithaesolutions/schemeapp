package com.example.scheme.classes;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private SharedPreferences sharedPreferences;
    private Context context;
    private String ip = "192.168.1.100";
    private String registerpassword="pass";
    private String scheme="scheme";

    public SharedPreferencesHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences("login_session",
                Context.MODE_PRIVATE);
        this.context = context;
    }

    public String getIp() {
        return sharedPreferences.getString(ip, "one");
    }
    public String getRegisterpassword(){return  sharedPreferences.getString(registerpassword,"two");}
    public String getScheme() { return sharedPreferences.getString(scheme,"three"); }

    public void setIp(String ip) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(this.ip, ip);
        edit.commit();
    }
    public void setRegisterpassword(String registerpassword){
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString(this.registerpassword,registerpassword);
        edit.commit();
    }
    public void setScheme(String scheme){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(this.scheme,scheme);
        editor.commit();
    }
}

