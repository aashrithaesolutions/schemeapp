package com.example.scheme;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.scheme.classes.SharedPreferencesHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText registerpassET,registerIPET,winjwDSET,jwDSET,driveLetterET;
    Button registerBtn,checkConnBtn;
    CardView passwordCV;
    SharedPreferencesHelper sharedPreferencesHelper;
    private RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferencesHelper=new SharedPreferencesHelper(this);

        registerpassET=findViewById(R.id.registerpassET);
        registerIPET=findViewById(R.id.registerIPET);
        registerBtn=findViewById(R.id.registerBtn);
        passwordCV=findViewById(R.id.passwordCV);
        winjwDSET=findViewById(R.id.winjwDSET);
        jwDSET=findViewById(R.id.jwDSET);
        checkConnBtn=findViewById(R.id.checkConnBtn);
        driveLetterET=findViewById(R.id.driveLetterET);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!registerpassET.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Enter a valid password", Toast.LENGTH_SHORT).show();
                }else if(registerIPET.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Enter a valid IP address", Toast.LENGTH_SHORT).show();
                }else if(driveLetterET.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Enter a valid Drive Letter", Toast.LENGTH_SHORT).show();
                }
                else{
                    sharedPreferencesHelper.setScheme(driveLetterET.getText().toString()+getResources().getString(R.string.schemepath));
                    sharedPreferencesHelper.setIp(registerIPET.getText().toString());
                    //registerAcc();
                    sharedPreferencesHelper.setRegisterpassword("success");
                    Toast.makeText(MainActivity.this, "Activated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Home.class));
                    finish();
                }
            }
        });
    }



    private void registerAcc() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url1)+sharedPreferencesHelper.getIp()+ getResources().getString(R.string.url2)+ "registerScheme.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                sharedPreferencesHelper.setScheme(driveLetterET.getText().toString()+getResources().getString(R.string.schemepath));
                                sharedPreferencesHelper.setIp(registerIPET.getText().toString());
                                JSONObject jsonObject1 = jsonObject.getJSONObject("details");
                                String origpass=jsonObject1.getString("USER_BIOMETRICDEVICE4");
                                MessageDigest md = MessageDigest.getInstance("MD5");
                                byte[] messageDigest = md.digest(registerpassET.getText().toString().getBytes());
                                BigInteger no = new BigInteger(1, messageDigest);
                                String hashtext = no.toString(16);
                                while (hashtext.length() < 32) {
                                    hashtext = "0" + hashtext;
                                }
                                if(origpass.equals(hashtext)){
                                    sharedPreferencesHelper.setRegisterpassword("success");
                                    /*
                                    String winjw=winjwDSET.getText().toString();
                                    String jw=jwDSET.getText().toString();
                                    sharedPreferencesHelper.setWinjwDS(winjw);
                                    sharedPreferencesHelper.setJwDS(jw);

                                     */
                                    Toast.makeText(MainActivity.this, "Activated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, Home.class));
                                    finish();
                                }else{
                                    Toast.makeText(MainActivity.this, "Check Password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException | NoSuchAlgorithmException e) {
                            Toast.makeText(MainActivity.this, "Check IP and password again", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Check IP,wifi and xampp again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("datasource",sharedPreferencesHelper.getScheme());
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(stringRequest2);
    }
}