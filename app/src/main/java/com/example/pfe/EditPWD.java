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

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditPWD extends AppCompatActivity {

    private EditText currentPWD;
    private EditText newPWD1;
    private EditText newPWD2;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_p_w_d);

        currentPWD=(EditText)findViewById(R.id.currentPWD);
        newPWD1=(EditText)findViewById(R.id.newPWD1);
        newPWD2=(EditText)findViewById(R.id.newPWD2);
        submit=(Button)findViewById(R.id.btnSubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPWD.getText().toString().equals("")||newPWD1.getText().toString().equals("")||newPWD2.getText().toString().equals("")){
                    new SweetAlertDialog(EditPWD.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Something went wrong!")
                            .setContentText("one field is empty!")
                            .show();
                }
                else{
                    if(!(newPWD1.getText().toString().equals(newPWD2.getText().toString()))){
                        new SweetAlertDialog(EditPWD.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Something went wrong!")
                                .setContentText("password doesnt match")
                                .show();
                    }else{
                        update();
                    }
                }
            }
        });
    }
    private void update() {
        String URL="http://192.168.1.16:8080/rest/webapi/myresource/updatePWD";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest objectRequest=new StringRequest(
                Request.Method.PUT,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("1")){
                        new SweetAlertDialog(EditPWD.this)
                                .setTitleText("changed successfully")
                                .show();
                        startActivity(new Intent(EditPWD.this,navActivity.class));
                        }
                        else{
                            new SweetAlertDialog(EditPWD.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Something went wrong!")
                                    .setContentText("Wrong password!")
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        new SweetAlertDialog(EditPWD.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Something went wrong!")
                                .setContentText(error.toString())
                                .show();
                    }
                })
        {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("UserCIN",((CIN) getApplication()).getCIN());
                params.put("oldPWD",hachage(currentPWD.getText().toString()));
                params.put("newPWD",hachage(newPWD1.getText().toString()));
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