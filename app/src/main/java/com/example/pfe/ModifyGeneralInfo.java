package com.example.pfe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pfe.databinding.ActivityAddProjectBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ModifyGeneralInfo extends AppCompatActivity {
    //get the spinner from the xml.
    private AutoCompleteTextView dropdown;
    private ActivityAddProjectBinding binding;
    private Button choose,chooseLogo;
    private Bitmap bitmap;
    private ImageView img,imgLogo;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        binding=ActivityAddProjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dropdown = findViewById(R.id.autoCompleteTextView);
        id=getIntent().getExtras().getString("id");
        getGI(id);
        binding.btnAdd.setText("modify");
        binding.textView2.setText("MODIFY PROJECT");
        binding.surface.setVisibility(View.GONE);
        binding.pictureDiploma.setVisibility(View.GONE);
        binding.pictureLogo.setVisibility(View.GONE);
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.name.getText().toString().equals("")||binding.description.getText().toString().equals("")||binding.autoCompleteTextView.getText().toString().equals(getResources().getString(R.string.projectType))){
                    new SweetAlertDialog(ModifyGeneralInfo.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Something went wrong!")
                            .setContentText("one field is empty")
                            .show();
                }
                else {
                    if(binding.autoCompleteTextView.getText().toString().equals("Agriculture")&&binding.surface.getText().toString().equals("")){
                        new SweetAlertDialog(ModifyGeneralInfo.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Something went wrong!")
                                .setContentText("one field is empty")
                                .show();
                    }else{
                        Toast.makeText(getApplicationContext(),binding.name.getText().toString(),Toast.LENGTH_LONG).show();
                        ModifyProject();
                    }
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
        if(requestCode==2 && resultCode==RESULT_OK)
        {
            Uri filepath=data.getData();
            try
            {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                imgLogo.setImageBitmap(bitmap);
            }catch (Exception ex)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void getGI(String pos) {
        String URL = "http://192.168.1.16:8080/rest/webapi/projects/getGI";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            binding.name.setText(response.getString("name"));
                            binding.description.setText(response.getString("description"));
                            switch (response.getString("type")){
                                case "1":
                                    dropdown.setText("Medical Cabinet");
                                    Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();
                                    binding.surface.setVisibility(View.GONE);
                                    binding.pictureLogo.setVisibility(View.GONE);
                                    binding.pictureDiploma.setVisibility(View.VISIBLE);
                                    img=(ImageView)findViewById(R.id.img);
                                    choose=(Button)findViewById(R.id.choosePic);
                                    choose.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Dexter.withActivity(ModifyGeneralInfo.this)
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
                                    break;

                                case "2":
                                    dropdown.setText("Commercial");
                                    Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();
                                    binding.surface.setVisibility(View.GONE);
                                    binding.pictureDiploma.setVisibility(View.GONE);
                                    binding.pictureLogo.setVisibility(View.VISIBLE);
                                    imgLogo=(ImageView)findViewById(R.id.imgLogo);
                                    chooseLogo=(Button)findViewById(R.id.chooseLogoPic);
                                    chooseLogo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Dexter.withActivity(ModifyGeneralInfo.this)
                                                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                                    .withListener(new PermissionListener() {
                                                        @Override
                                                        public void onPermissionGranted(PermissionGrantedResponse response)
                                                        {
                                                            Intent intent=new Intent(Intent.ACTION_PICK);
                                                            intent.setType("image/*");
                                                            startActivityForResult(Intent.createChooser(intent,"Browse Image"),2);
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
                                    break;

                                case "3":
                                    dropdown.setText("Agriculture");
                                    Toast.makeText(getApplicationContext(),"3",Toast.LENGTH_LONG).show();
                                    binding.surface.setVisibility(View.VISIBLE);
                                    binding.pictureDiploma.setVisibility(View.GONE);
                                    binding.pictureLogo.setVisibility(View.GONE);
                                    binding.surface.setText(response.getString("surface"));
                                    break;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SweetAlertDialog(ModifyGeneralInfo.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Something went wrong!")
                        .setContentText(error.toString())
                        .show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID", pos);
                return params;
            }
        };
        requestQueue.add(objectRequest);
    }
    private void ModifyProject() {
        String URL="http://192.168.1.16:8080/rest/webapi/projects/updateGI";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest objectRequest=new StringRequest(
                Request.Method.PUT,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"Added successfully",Toast.LENGTH_LONG).show();
                        Intent intent =new Intent(ModifyGeneralInfo.this,navActivity.class);
//                        intent.putExtra("fragement","Account");
//                        finish();
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        new SweetAlertDialog(ModifyGeneralInfo.this, SweetAlertDialog.ERROR_TYPE)
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
                params.put("name",binding.name.getText().toString());
                params.put("desc",binding.description.getText().toString());
                if(binding.autoCompleteTextView.getText().toString().equals("Agriculture")){
                    params.put("surface",binding.surface.getText().toString());
                    params.put("type","3");
                }
                else{
                    params.put("surface","-1");
                    if(binding.autoCompleteTextView.getText().toString().equals("Medical Cabinet")){
                        params.put("type","1");
                    }else{
                        params.put("type","2");
                    }
                }
                return params;
            }
        };
        requestQueue.add(objectRequest);
    }
}

