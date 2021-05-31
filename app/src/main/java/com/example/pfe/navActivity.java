package com.example.pfe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.InputStream;

public class navActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FragmentContainerView fragmentContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nav);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.Nav);
        fragmentContainerView=(FragmentContainerView)findViewById(R.id.fragment);
        Intent i = getIntent();
        String data = i.getStringExtra("FromMainActivity");
        if (data != null && data.contentEquals("1")) {
            //Toast.makeText(getApplicationContext(),"worked",Toast.LENGTH_LONG).show();
            //replace(new Help());
            //bottomNavigationView.setFragment(R.id.help);
//            setContentView(R.layout.activity_nav,new Help());
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment,new Help());
//            transaction.commit();

            //getSupportFragmentManager().beginTransaction().show(new Help()).commit();
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.account:
                        replace(new Account());
                        break;
                    case  R.id.help:
                        replace(new Help());
                        break;
                    case R.id.providerLocation:
                        replace(new ProviderLocation());
                        break;
                    case R.id.submitDemand:
                        replace(new SubmitDemand());
                        break;
                    case R.id.createProject:
                        replace(new CreateProject());
                        break;
                }
                return true;
            }
        });
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
//    {
//        if(requestCode==2)
//        {
//            replace(new CreateProject());
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        transaction.commit();
    }
}