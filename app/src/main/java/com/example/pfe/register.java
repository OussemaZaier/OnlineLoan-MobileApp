package com.example.pfe;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.graphics.Color;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class register extends AppCompatActivity {



    private Button btnSign;
    private EditText username;
    private EditText CIN;
    private EditText phone;
    private EditText PWD1;
    private EditText PWD2;
    private String username1;
    private String CIN1;
    private String phone1;
    private String PWD11;
    private int randomNumber;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username=(EditText) findViewById(R.id.username);
        CIN=(EditText) findViewById(R.id.CIN);
        phone=(EditText) findViewById(R.id.phone);
        PWD1=(EditText) findViewById(R.id.PWD1);
        PWD2=(EditText) findViewById(R.id.PWD2);
        btnSign=(Button) findViewById(R.id.btnSignUp);



        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(PWD1.getText().toString().equals(PWD2.getText().toString()))){
                    new SweetAlertDialog(register.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Something went wrong!")
                            .setContentText("password doesnt match")
                            .show();
                }
                else{
                    if (username.getText().toString().equals("")||CIN.getText().toString().equals("")||phone.getText().toString().equals("")||PWD1.getText().toString().equals("")||PWD2.getText().toString().equals(""))
                    {
                        new SweetAlertDialog(register.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Something went wrong!")
                                .setContentText("one field is empty")
                                .show();
                    }
                    else{
                        checkIfUserExist();
                    }
                }
            }
        });
    }

    public void openActivity(){
        Intent intent = new Intent(this,OTP.class);

        username1=username.getText().toString();
        CIN1=CIN.getText().toString();
        phone1=phone.getText().toString();
        PWD11=PWD1.getText().toString();

        intent.putExtra("code",code);
        intent.putExtra("signupUserName",username1);
        intent.putExtra("signupCIN",CIN1);
        intent.putExtra("signupPhone",phone1);
        intent.putExtra("signupPWD",PWD11);

        startActivity(intent);

    }

    private void checkIfUserExist() {
        String URL="http://192.168.1.16:8080/rest/webapi/myresource/exist";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest objectRequest=new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("1"))
                        {
                            new SweetAlertDialog(register.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Something went wrong!")
                                    .setContentText("User with this CIN number already exist")
                                    .show();
                        }
                        else
                        {
                            //generate random number for OTP
                            Random random=new Random();
                            randomNumber=random.nextInt(999999);
                            code=String.format("%06d", randomNumber);

                            //sendCode(code);
                            Toast.makeText(getApplicationContext(),code,Toast.LENGTH_LONG).show();
                            openActivity();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new SweetAlertDialog(register.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Something went wrong!")
                                .setContentText(error.toString())
                                .show();
                    }
                })
        {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("CIN",CIN.getText().toString());
                return params;
            }
        };
        requestQueue.add(objectRequest);

    }

    private void sendCode(String code) {
        String URL="http://192.168.1.16:8080/rest/webapi/myresource/sendOTP";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest objectRequest=new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("succed"))
                        {
                            new SweetAlertDialog(register.this)
                                    .setTitleText("message sent to your phone successfully")
                                    .show();
                        }
                        else
                        {
                            new SweetAlertDialog(register.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Something went wrong!")
                                    .setContentText(response.toString())
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new SweetAlertDialog(register.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Something went wrong!")
                                .setContentText(error.toString())
                                .show();
                    }
                })
        {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();

                params.put("phone",phone.getText().toString());
                params.put("code",code);
                return params;
            }
        };
        requestQueue.add(objectRequest);

    }

}