package com.example.scheme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arasthel.asyncjob.AsyncJob;
import com.example.scheme.classes.IntentIntegrator;
import com.example.scheme.classes.IntentResult;
import com.example.scheme.classes.SharedPreferencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class FragmentCardReceipt extends Fragment {
    View view;
    EditText groupIdET, rateET, amtET, narrationET,staffCodeET;
    TextView nameTV, lastInNoET, totInsET,insPayingET,installmentNoTV;
    Button grpOKBtn, scanBtn, clearBtn, saveReceiptBtn,cashBtn,chequeBtn,bankBtn;
    RequestQueue rQueue;
    SharedPreferencesHelper sharedPreferencesHelper;
    String statusType="CA";
    String insNo="";
    String originalAmount="";
    int newLastIno;
    DecimalFormat df;
    double enteredAmount;
     double origAmt;
    double instpaying;
     int instpayingInt;
     int lastInstNo;
     int tmpCurrentInst;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_card_receipt, container, false);
        //Start of address fragment class

        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity());
        groupIdET = view.findViewById(R.id.groupIdET);
        rateET = view.findViewById(R.id.rateET);
        amtET = view.findViewById(R.id.amtET);
        nameTV = view.findViewById(R.id.nameTV);
        grpOKBtn = view.findViewById(R.id.grpOKBtn);
        lastInNoET = view.findViewById(R.id.lastInNoET);
        insPayingET = view.findViewById(R.id.insPayingET);
        narrationET = view.findViewById(R.id.narrationET);
        totInsET = view.findViewById(R.id.totInsET);
        scanBtn = view.findViewById(R.id.scanBtn);
        clearBtn = view.findViewById(R.id.clearBtn);
        saveReceiptBtn = view.findViewById(R.id.saveReceiptBtn);
        cashBtn=view.findViewById(R.id.cashBtn);
        chequeBtn=view.findViewById(R.id.chequeBtn);
        bankBtn=view.findViewById(R.id.bankBtn);
        installmentNoTV=view.findViewById(R.id.installmentNoTV);
        staffCodeET=view.findViewById(R.id.staffCodeET);

        df = new DecimalFormat("0.00");


        amtET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(!amtET.getText().toString().equals("")) {
                        if(amtET.getText().toString()!=originalAmount){
                            enteredAmount=Double.parseDouble(amtET.getText().toString());
                            origAmt=Double.parseDouble(originalAmount);
                            instpaying= enteredAmount/origAmt;
                            instpayingInt = (int) instpaying;
                            lastInstNo=Integer.parseInt(lastInNoET.getText().toString());
                            tmpCurrentInst=lastInstNo+1;
                            if(instpayingInt>1){
                                double newAmt=instpayingInt*origAmt;
                                amtET.setText(df.format(newAmt));
                                insPayingET.setText(instpayingInt+"");
                                insNo=tmpCurrentInst+"";
                                for(int i=tmpCurrentInst+1;i<=(lastInstNo+instpayingInt);i++){
                                    insNo=insNo+","+i;
                                    newLastIno=i;
                                }
                            }else{
                                insPayingET.setText("1");
                                insNo=tmpCurrentInst+"";
                                newLastIno=tmpCurrentInst;
                            }
                            installmentNoTV.setText(insNo);
                        }
                    }
                }
            }
        });

        cashBtn.setBackgroundColor(Color.YELLOW);
        cashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashBtn.setBackgroundColor(Color.YELLOW);
                statusType="CA";
                chequeBtn.setBackgroundColor(Color.WHITE);
                bankBtn.setBackgroundColor(Color.WHITE);
            }
        });
        chequeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chequeBtn.setBackgroundColor(Color.YELLOW);
                statusType="CH";
                cashBtn.setBackgroundColor(Color.WHITE);
                bankBtn.setBackgroundColor(Color.WHITE);
            }
        });
        bankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankBtn.setBackgroundColor(Color.YELLOW);
                statusType="BA";
                chequeBtn.setBackgroundColor(Color.WHITE);
                cashBtn.setBackgroundColor(Color.WHITE);
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupIdET.setText("");
                rateET.setText("");
                amtET.setText("");
                nameTV.setText("");
                lastInNoET.setText("");
                insPayingET.setText("1");
                narrationET.setText("");
                totInsET.setText("");
                installmentNoTV.setText("");
                saveReceiptBtn.setText("save");
            }
        });


        grpOKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupIdET.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please enter group id", Toast.LENGTH_SHORT).show();
                } else {
                    getSchemeDetails();
                }
            }
        });

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
                scanIntegrator.initiateScan();
            }
        });

        saveReceiptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(staffCodeET.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Enter Staff Code", Toast.LENGTH_SHORT).show();
                }else {
                    saveReceipt();
                }
            }
        });


        //End of address class
        return view;
    }

    private void saveReceipt() {
        AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
            @Override
            public void doOnBackground() {
                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url1) + sharedPreferencesHelper.getIp() + getResources().getString(R.string.url2) + "schemeSaveReceipt.php",
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {
                                Log.i("Response from server : ",response);
                                rQueue.getCache().clear();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.optString("success").equals("1")) {
                                        String rno=jsonObject.getString("Rno");
                                        saveReceiptBtn.setText("saved R No: "+rno);
                                    } else {
                                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("datasource", sharedPreferencesHelper.getScheme());
                        String str=groupIdET.getText().toString();
                        String[] splitStr = str.split("\\s+");
                        params.put("Gname", splitStr[0]);
                        params.put("Mno",splitStr[1]);
                        params.put("Instno", insNo);
                        params.put("Amount", amtET.getText().toString());
                        params.put("Mode", statusType);
                        params.put("Rate",rateET.getText().toString());
                        params.put("Narration",narrationET.getText().toString());
                        params.put("Staff",staffCodeET.getText().toString());
                        params.put("newLastInsNo",newLastIno+"");
                        Log.i("Instno",insNo);
                        Log.i("newlastIno",newLastIno+"");
                        return params;
                    }
                };
                rQueue = Volley.newRequestQueue(getContext());
                rQueue.add(stringRequest2);
            }
        });
    }

    private void getSchemeDetails() {
        AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
            @Override
            public void doOnBackground() {
                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url1) + sharedPreferencesHelper.getIp() + getResources().getString(R.string.url2) + "schemegetSchemeDetails.php",
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {
                                rQueue.getCache().clear();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.optString("success").equals("1")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                                        String name = null, ino = null, rate = null, amt = null;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                            name = jsonObject3.getString("MName");
                                            ino = jsonObject3.getString("LastIno");
                                            amt = jsonObject3.getString("InsAmt");
                                            rate = jsonObject3.getString("R_G22BR");
                                        }
                                        nameTV.setText(name);
                                        lastInNoET.setText(ino);
                                        rateET.setText(rate);
                                        amtET.setText(amt);
                                        originalAmount=amt;

                                        enteredAmount=Double.parseDouble(amtET.getText().toString());
                                        origAmt=Double.parseDouble(originalAmount);
                                        instpaying= enteredAmount/origAmt;
                                        instpayingInt = (int) instpaying;
                                        lastInstNo=Integer.parseInt(lastInNoET.getText().toString());
                                        tmpCurrentInst=lastInstNo+1;
                                        insPayingET.setText("1");
                                        insNo=tmpCurrentInst+"";
                                        newLastIno=tmpCurrentInst;

                                    } else {
                                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), "Error :" + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("groupId", groupIdET.getText().toString());
                        params.put("datasource", sharedPreferencesHelper.getScheme());
                        return params;
                    }
                };
                rQueue = Volley.newRequestQueue(getContext());
                rQueue.add(stringRequest2);
            }
        });
    }

    //result of scanner
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String message = scanResult.getContents();
            groupIdET.setText(message);
            getSchemeDetails();
        }
    }
}
