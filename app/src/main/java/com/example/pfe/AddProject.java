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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pfe.databinding.ActivityAddProjectBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;

public class AddProject extends AppCompatActivity {
    //get the spinner from the xml.
    private AutoCompleteTextView dropdown;
    private ActivityAddProjectBinding binding;
    private Button choose,chooseLogo;
    private Bitmap bitmap;
    private ImageView img,imgLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddProjectBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_add_project);
        setContentView(binding.getRoot());
        dropdown = findViewById(R.id.autoCompleteTextView);
        String[] items =getResources().getStringArray(R.array.projectType);;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.projecttypeselector, items);
        dropdown.setAdapter(adapter);
        binding.surface.setVisibility(View.GONE);
        binding.pictureDiploma.setVisibility(View.GONE);
        binding.pictureLogo.setVisibility(View.GONE);
        dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = (String) adapter.getItem(position);

                switch (selectedValue){
                    case "Medical Cabinet":
                        Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();
                        binding.surface.setVisibility(View.GONE);
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
                        Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();
                        binding.surface.setVisibility(View.GONE);
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
                        Toast.makeText(getApplicationContext(),"3",Toast.LENGTH_LONG).show();
                        binding.surface.setVisibility(View.VISIBLE);
                        binding.pictureDiploma.setVisibility(View.GONE);
                        binding.pictureLogo.setVisibility(View.GONE);
                        break;

                }
            }});
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
}