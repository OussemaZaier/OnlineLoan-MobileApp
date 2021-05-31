package com.example.pfe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GuaranteeInfo extends AppCompatActivity {
    private String id;
    private Button choose,add;
    private Bitmap bitmap;
    private ImageView img;
    private TextView TypeGuarantee, DescGuarantee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        id=getIntent().getExtras().getString("id");
        getGuI();
        setContentView(R.layout.activity_guarantee_info);
        choose=(Button)findViewById(R.id.choosePic);
        add=(Button)findViewById(R.id.btnAdd);
        img=(ImageView)findViewById(R.id.img);
        TypeGuarantee=(TextView)findViewById(R.id.type);
        DescGuarantee=(TextView)findViewById(R.id.description);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(GuaranteeInfo.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response)
                            {
                                Intent intent=new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TypeGuarantee.getText().toString().equals("")||DescGuarantee.getText().toString().equals("")){
                    new SweetAlertDialog(GuaranteeInfo.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Something went wrong!")
                            .setContentText("one field is empty")
                            .show();
                }else{
                    addGuaranteeInfo();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==1 && resultCode==RESULT_OK)
        {
            Uri filepath=data.getData();
            try
            {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
            }catch (Exception ex)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void addGuaranteeInfo() {
        String URL="http://192.168.1.16:8080/rest/webapi/projects/updateGuI";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest objectRequest=new StringRequest(
                Request.Method.PUT,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                        Intent intent =new Intent(GuaranteeInfo.this,navActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        new SweetAlertDialog(GuaranteeInfo.this, SweetAlertDialog.ERROR_TYPE)
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
                params.put("type",TypeGuarantee.getText().toString());
                params.put("desc",DescGuarantee.getText().toString());
                return params;
            }
        };
        requestQueue.add(objectRequest);
    }
    private void getGuI(){
        String URL = "http://192.168.1.16:8080/rest/webapi/projects/getGuI";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(response.length()!=0){
                                TypeGuarantee.setText(response.getString("type"));
                                DescGuarantee.setText(response.getString("desc"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SweetAlertDialog(GuaranteeInfo.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Something went wrong!")
                        .setContentText(error.toString())
                        .show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID",id);
                return params;
            }
        };
        requestQueue.add(objectRequest);
    }
}