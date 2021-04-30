package com.example.pfe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.util.Base64.DEFAULT;
import static android.util.Base64.NO_WRAP;
import static android.util.Base64.encodeToString;

public class Upload extends AppCompatActivity {

    //upload
    EditText t1,t2;
    Button browse,upload;
    ImageView img;
    String encodedImage;
    Bitmap bitmap;
    String encodeImageString;
    String result;
    byte[] bytesofimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        img=(ImageView)findViewById(R.id.img);
        upload=(Button)findViewById(R.id.upload);
        browse=(Button)findViewById(R.id.browse);
        t1=(EditText)findViewById(R.id.t2);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(Upload.this)
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
    upload.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            encodeBitmapImage(bitmap);
            sendAndRequestResponse();
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
    private void encodeBitmapImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
         bytesofimage=byteArrayOutputStream.toByteArray();
        encodedImage=Base64.encodeToString(bytesofimage, Base64.DEFAULT);
        t1.setText(encodedImage);
        //return encodeImageString;
         result = encodedImage.replace("\r*\n*", "");

        t1.setText(result);
        //Toast.makeText(getApplicationContext(),encodeImageString,Toast.LENGTH_LONG).show();
    }

    private void sendAndRequestResponse() {
        String URL="http://192.168.1.16:8080/rest/webapi/projects/create";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest objectRequest=new StringRequest(
                Request.Method.PUT,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                            new SweetAlertDialog(Upload.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Something went wrong!")
                                    .setContentText("CIN or password are wrong!")
                                    .show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new SweetAlertDialog(Upload.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Something went wrong!")
                                .setContentText(error.toString())
                                .show();
                    }
                })
        {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("image", result);

                return params;
            }
//            @Override
//            protected Map<String, DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//                // file name could found file base or direct access from real path
//                // for now just get bitmap data from ImageView
////                params.put("doc_file", new DataPart("file_avatar.jpg", Common.getFileDataFromDrawable(context, context.getDrawable(R.drawable.ic_app_icon)), "image/png"));
////                params.put("upload_file", new DataPart("screens.zip",file,"application/zip"));
//
//
//                return file;
//            }
        };
//        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                9000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(objectRequest);

    }

}