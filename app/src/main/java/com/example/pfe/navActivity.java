package com.example.pfe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class navActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        //bottom nav
        BottomNavigationView btnNav=findViewById(R.id.Nav);
        NavController navController= Navigation.findNavController(this,R.id.fragment);

        //AppBarConfiguration appBarConfiguration =
        //        new AppBarConfiguration.Builder(R.id.help,R.id.providerLocation,R.id.account,R.id.submitDemand,R.id.createProject).build();
       // NavigationUI.setupActionBarWithNavController(navController);

        NavigationUI.setupWithNavController(btnNav, navController);

    }
}