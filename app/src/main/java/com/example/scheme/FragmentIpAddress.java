package com.example.scheme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.example.scheme.classes.SharedPreferencesHelper;

public class FragmentIpAddress extends Fragment {
    SharedPreferencesHelper sharedPreferencesHelper;
    EditText ipaddressET,driveLetterET,schemeDSET,driveLET;
    Button ipBtn,photosPathBtn,checkConnectionBtn,driveBtn;
    View view;
    private RequestQueue rQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ipadress, container, false);

        sharedPreferencesHelper=new SharedPreferencesHelper(getContext());
        ipaddressET=view.findViewById(R.id.ipaddressET);
        ipBtn=view.findViewById(R.id.setIPbtn);
        driveLetterET=view.findViewById(R.id.driveLetterET);
        photosPathBtn=view.findViewById(R.id.setPhotosPathBtn);
        schemeDSET=view.findViewById(R.id.jwDSET);
        checkConnectionBtn=view.findViewById(R.id.checkConnBtn);
        driveLET=view.findViewById(R.id.driveLET);
        driveBtn=view.findViewById(R.id.driveBtn);

        driveLET.setText(sharedPreferencesHelper.getScheme().substring(0,1));

        driveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(driveLET.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Enter Valid Drive Letter", Toast.LENGTH_SHORT).show();
                }else{
                    sharedPreferencesHelper.setScheme(driveLET.getText().toString()+getResources().getString(R.string.schemepath));
                    sharedPreferencesHelper.setScheme(driveLET.getText().toString()+getResources().getString(R.string.schemepath));
                    Toast.makeText(getContext(), "Set", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ipaddressET.setText(sharedPreferencesHelper.getIp());
        schemeDSET.setText(sharedPreferencesHelper.getScheme());

        ipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ipaddressET.getText().equals("")){
                    Toast.makeText(getContext(), "Enter the IP address", Toast.LENGTH_SHORT).show();
                }else{
                    String ipaddress=ipaddressET.getText().toString().trim();
                    sharedPreferencesHelper.setIp(ipaddress);
                    Toast.makeText(getContext(), "IP Address SET", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
