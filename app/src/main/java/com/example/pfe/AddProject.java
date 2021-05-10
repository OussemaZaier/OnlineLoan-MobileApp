package com.example.pfe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddProject extends AppCompatActivity {
    //get the spinner from the xml.
    private AutoCompleteTextView dropdown;
    private ActivityAddProjectBinding binding;
    private Button choose,chooseLogo;
    private Bitmap bitmap;
    private ImageView img,imgLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding=ActivityAddProjectBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_add_project);
        setContentView(binding.getRoot());
        dropdown = findViewById(R.id.autoCompleteTextView);
        String[] items =getResources().getStringArray(R.array.projectType);;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.projecttypeselector, items);
        dropdown.setAdapter(adapter);
        binding.Surface.setVisibility(View.GONE);
        binding.pictureDiploma.setVisibility(View.GONE);
        binding.pictureLogo.setVisibility(View.GONE);
        dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = (String) adapter.getItem(position);

                switch (selectedValue){
                    case "Medical Cabinet":
                        binding.Surface.setVisibility(View.GONE);
                        binding.pictureLogo.setVisibility(View.GONE);
                        binding.pictureDiploma.setVisibility(View.VISIBLE);
                        img=(ImageView)findViewById(R.id.img);
                        choose=(Button)findViewById(R.id.choosePic);
                        choose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Dexter.withActivity(AddProject.this)
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

                    case "Commercial":
                        binding.Surface.setVisibility(View.GONE);
                        binding.pictureDiploma.setVisibility(View.GONE);
                        binding.pictureLogo.setVisibility(View.VISIBLE);
                        imgLogo=(ImageView)findViewById(R.id.imgLogo);
                        chooseLogo=(Button)findViewById(R.id.chooseLogoPic);
                        chooseLogo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Dexter.withActivity(AddProject.this)
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

                    case "Agriculture":
                        binding.Surface.setVisibility(View.VISIBLE);
                        binding.pictureDiploma.setVisibility(View.GONE);
                        binding.pictureLogo.setVisibility(View.GONE);
                        break;

                }
            }});
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.name.getText().toString().equals("")||binding.description.getText().toString().equals("")||binding.autoCompleteTextView.getText().toString().equals(getResources().getString(R.string.projectType))){
                    new SweetAlertDialog(AddProject.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Something went wrong!")
                            .setContentText("one field is empty")
                            .show();
                }
                else {
                    if(binding.autoCompleteTextView.getText().toString().equals("Agriculture")&&binding.surface.getText().toString().equals("")){
                        new SweetAlertDialog(AddProject.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Something went wrong!")
                                .setContentText("one field is empty")
                                .show();
                }else{
                         addProject();
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
    private void addProject() {
        String URL="http://192.168.1.16:8080/rest/webapi/projects/add";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest objectRequest=new StringRequest(
                Request.Method.PUT,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        new SweetAlertDialog(AddProject.this)
//                                .setTitleText("login successfully")
//                                .show();
                        //getFragmentManager().popBackStack();
                        startActivity(new Intent(AddProject.this,navActivity.class));

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new SweetAlertDialog(AddProject.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Something went wrong!")
                                .setContentText(error.toString())
                                .show();
                    }
                })
        {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();

                params.put("name",binding.name.getText().toString());
                params.put("description",binding.description.getText().toString());
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
                params.put("user", ((CIN) getApplication()).getCIN());
                return params;
            }
        };
        requestQueue.add(objectRequest);
    }
}