package com.example.pfe;

import androidx.appcompat.app.AppCompatActivity;
import java.security.MessageDigest;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.ClientError;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {

    private EditText etCIN;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCIN=(EditText) findViewById(R.id.etCIN);
        etPassword=(EditText) findViewById(R.id.etPassword);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        btnCreate=(Button) findViewById(R.id.btnCreate);




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this,navActivity.class));
                if(etCIN.getText().toString().equals("")||etPassword.getText().toString().equals("")){
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Something went wrong!")
                            .setContentText("one field is empty")
                            .show();
                }
                else {
                    sendAndRequestResponse();
                }

            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,register.class));
            }
        });

    }
    private void sendAndRequestResponse() {
        String URL="http://192.168.1.16:8080/rest/webapi/myresource/authenticate";
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        StringRequest objectRequest=new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("1"))
                        {
                            new SweetAlertDialog(MainActivity.this)
                                .setTitleText("login successfully")
                                .show();
                            //store CIN number
                            ((CIN) getApplication()).setCIN(etCIN.getText().toString());
                            //start activity
                            startActivity(new Intent(MainActivity.this,navActivity.class));
                        }
                        else
                        {
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Something went wrong!")
                                    .setContentText("CIN or password are wrong!")
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Something went wrong!")
                                .setContentText(error.toString())
                                .show();
                    }
                })
        {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
                Map<String,String> params=new HashMap<>();

                params.put("CIN",etCIN.getText().toString());
                params.put("Password",hachage(etPassword.getText().toString()));
                return params;
            }
        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
