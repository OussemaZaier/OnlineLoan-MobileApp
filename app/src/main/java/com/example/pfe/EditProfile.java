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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditProfile extends AppCompatActivity {

    private Button btnSubmit;


    private EditText username;
    private EditText CIN;
    private EditText phone;
    private String username1;
    private String CIN1;
    private String phone1;
    private int randomNumber;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnSubmit=(Button) findViewById(R.id.btnSubmit);
        username=(EditText) findViewById(R.id.username);
        CIN=(EditText) findViewById(R.id.CIN);
        phone=(EditText) findViewById(R.id.phone);

        getUserData();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("")||CIN.getText().toString().equals("")||phone.getText().toString().equals(""))
                {
                    new SweetAlertDialog(EditProfile.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Something went wrong!")
                            .setContentText("one field is empty")
                            .show();
                }
                else{
                    if(CIN.getText().toString().equals(((CIN) getApplication()).getCIN())){
                        //generate random number for OTP
                        Random random=new Random();
                        randomNumber=random.nextInt(999999);
                        code=String.format("%06d", randomNumber);

                        //sendCode(code);
                        Toast.makeText(getApplicationContext(),code,Toast.LENGTH_LONG).show();
                        openActivity();
                    }else{
                        UserExist();
                    }
                }
            }
        });
    }
    private void UserExist(){
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
                            new SweetAlertDialog(EditProfile.this, SweetAlertDialog.ERROR_TYPE)
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
                        new SweetAlertDialog(EditProfile.this, SweetAlertDialog.ERROR_TYPE)
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
    private void getUserData() {
        String URL = "http://192.168.1.16:8080/rest/webapi/myresource/get";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            username.setText(response.getString("name"));
                            CIN.setText(response.getString("CIN"));
                            phone.setText(response.getString("phoneNumber"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SweetAlertDialog(EditProfile.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Something went wrong!")
                        .setContentText(error.toString())
                        .show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("CIN", ((CIN) getApplication()).getCIN());

                return params;
            }
        };
        requestQueue.add(objectRequest);
    }
    public void openActivity(){
        Intent intent = new Intent(this,ModifyOTP.class);

        username1=username.getText().toString();
        CIN1=CIN.getText().toString();
        phone1=phone.getText().toString();

        intent.putExtra("ModifyCode",code);
        intent.putExtra("ModifyUsername",username1);
        intent.putExtra("ModifyCIN",CIN1);
        intent.putExtra("ModifyPhone",phone1);

        startActivity(intent);

    }
}