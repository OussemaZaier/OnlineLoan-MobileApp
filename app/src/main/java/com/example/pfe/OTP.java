package com.example.pfe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OTP extends AppCompatActivity {
    private Button btnVerify;
    private EditText code;

    private String username;
    private String CIN;
    private String phone;
    private String PWD;
    private String random;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        btnVerify=(Button)findViewById(R.id.btnVerify);
        code=(EditText)findViewById(R.id.OTPcode);

        username=getIntent().getExtras().getString("signupUserName");
        CIN=getIntent().getExtras().getString("signupCIN");
        phone=getIntent().getExtras().getString("signupPhone");
        PWD=getIntent().getExtras().getString("signupPWD");
        random=getIntent().getExtras().getString("code");

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(code.getText().toString().equals("")){
                    new SweetAlertDialog(OTP.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Something went wrong!")
                            .setContentText("code field is empty!")
                            .show();
                }
                else{
                if(code.getText().toString().equals(random)){
                    sendAndRequestResponse();
                    new SweetAlertDialog(OTP.this)
                            .setTitleText("sign up successfully")
                            .show();
                }else{
                    new SweetAlertDialog(OTP.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Something went wrong!")
                            .setContentText("wrong code!")
                            .show();
                }
                }
            }
        });
    }
    private void sendAndRequestResponse() {
        String URL="http://192.168.1.16:8080/rest/webapi/myresource/create";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest objectRequest=new StringRequest(
                Request.Method.PUT,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ((CIN) getApplication()).setCIN(CIN);
                        //code.setText(((CIN) getApplication()).getCIN());
                        startActivity(new Intent(OTP.this,navActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        code.setText(error.toString());
                    }
                })
        {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();

                params.put("Username",username);
                params.put("CIN",CIN);
                params.put("TEL",phone);
                params.put("Password",hachage(PWD));
                return params;
            }
        };
        requestQueue.add(objectRequest);

    }
    private String hachage(String pwd){
        try {
            SHA sha= new SHA();
            String str1= SHA.toHexString(sha.getSHA(pwd));

            return str1;
        }
        catch (NoSuchAlgorithmException e) {
            return("Exception thrown for incorrect algorithm: " + e);
        }
    }
}