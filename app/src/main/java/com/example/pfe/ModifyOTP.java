package com.example.pfe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ModifyOTP extends AppCompatActivity {

    private String username;
    private String CIN;
    private String phone;
    private String random;

    private Button send;
    private EditText code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_modify_o_t_p);
        setContentView(R.layout.activity_o_t_p);

        send=(Button)findViewById(R.id.btnVerify);
        code=(EditText)findViewById(R.id.OTPcode);

        username=getIntent().getExtras().getString("ModifyUsername");
        CIN=getIntent().getExtras().getString("ModifyCIN");
        phone=getIntent().getExtras().getString("ModifyPhone");
        random=getIntent().getExtras().getString("ModifyCode");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code.getText().toString().equals("")){
                    new SweetAlertDialog(ModifyOTP.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Something went wrong!")
                            .setContentText("code field is empty!")
                            .show();
                }
                else{
                    if(code.getText().toString().equals(random)){
                        sendAndRequestResponse();
                        new SweetAlertDialog(ModifyOTP.this)
                                .setTitleText("Information modified successfully")
                                .show();
                    }else{
                        new SweetAlertDialog(ModifyOTP.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Something went wrong!")
                                .setContentText("wrong code!")
                                .show();
                    }
                }
            }
        });

    }

    private void sendAndRequestResponse() {
        String URL="http://192.168.1.16:8080/rest/webapi/myresource/update";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest objectRequest=new StringRequest(
                Request.Method.PUT,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ((CIN) getApplication()).setCIN(CIN);
                        //code.setText(((CIN) getApplication()).getCIN());
                        startActivity(new Intent(ModifyOTP.this,navActivity.class));
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
                params.put("UserCIN",((CIN) getApplication()).getCIN());
                params.put("Username",username);
                params.put("CIN",CIN);
                params.put("TEL",phone);
                return params;
            }
        };
        requestQueue.add(objectRequest);

    }

}