package com.example.pfe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocalInfo extends AppCompatActivity {
    private Button SetLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView Address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_info);

        SetLocation=(Button)findViewById(R.id.SetLocation);
        Address=(TextView)findViewById(R.id.Address);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        SetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if(location!=null){
                                    Geocoder geocoder=new Geocoder(LocalInfo.this, Locale.getDefault());
                                    try {
                                        List<Address> addresses=geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                                        Address.setText(addresses.get(0).getAddressLine(0));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }else{
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                    }
                }
//                if(ActivityCompat.checkSelfPermission(LocalInfo.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
//                    //getLocation();
//                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Location> task) {
//                            Location location=task.getResult();
//                            if(location!=null){
//                                Geocoder geocoder=new Geocoder(LocalInfo.this, Locale.getDefault());
//                                try {
//                                    List<Address> addresses=geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
//                                    Address.setText(String.valueOf(addresses.get(0).getLatitude()));
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }else{Address.setText("s");}
//                        }
//                    });
//                }else{
//                    ActivityCompat.requestPermissions(LocalInfo.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
//                }
            }
        });
    }

    private void getLocation() {
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//                Location location=task.getResult();
//                if(location!=null){
//                    Geocoder geocoder=new Geocoder(LocalInfo.this, Locale.getDefault());
//                    try {
//                        List<Address> addresses=geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
//                        Address.setText(addresses.get(0).getLocality());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
    }
}