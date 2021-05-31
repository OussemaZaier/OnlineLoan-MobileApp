package com.example.pfe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pfe.databinding.ActivityAddProjectBinding;
import com.example.pfe.databinding.ActivitySelectModifyProjectBinding;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SelectModifyProject extends AppCompatActivity {
    private Button btnGeneral,btnLocal,btnGuarantee,btnSubmit;
    private String id;
    private String fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_modify_project);
        id=getIntent().getExtras().getString("id");
        fragment=getIntent().getExtras().getString("fragment");
        btnSubmit=(Button)findViewById(R.id.btnSubmit);

        if(fragment.equals("1")){
            btnSubmit.setVisibility(View.GONE);
        }
        btnGeneral=(Button)findViewById(R.id.btnGeneral);
        btnLocal=(Button)findViewById(R.id.btnLocal);
        btnGuarantee=(Button)findViewById(R.id.btnGuarantee);
        btnGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectModifyProject.this,ModifyGeneralInfo.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectModifyProject.this,LocalInfo.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        btnGuarantee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectModifyProject.this,GuaranteeInfo.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }
    private void check(){
        String URL="http://192.168.1.16:8080/rest/webapi/projects/check";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest objectRequest=new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("2"))
                        {
                            Toast.makeText(getApplicationContext(),"Added successfully",Toast.LENGTH_LONG).show();
                            //start activity
                            Intent intent =new Intent(SelectModifyProject.this,navActivity.class);
//                            intent.putExtra("id",1);
                            startActivity(intent);
                        }
                        else
                        {
                            if (response.equals("1")){
                                Toast.makeText(getApplicationContext(),"you must fill all local information fields",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(SelectModifyProject.this,LocalInfo.class);
                                intent.putExtra("id",id);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),"you must fill all guarantee information fields",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(SelectModifyProject.this,GuaranteeInfo.class);
                                intent.putExtra("id",id);
                                startActivity(intent);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new SweetAlertDialog(SelectModifyProject.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Something went wrong!")
                                .setContentText(error.toString())
                                .show();
                    }
                })
        {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("ID",id);
                return params;
            }
        };
        requestQueue.add(objectRequest);
    }
}