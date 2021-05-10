package com.example.pfe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class SelectModifyProject extends AppCompatActivity {
    private Button btnGeneral,btnLocal,btnGuarantee,btnSubmit;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_modify_project);
        id=getIntent().getExtras().getString("id");

        btnGeneral=(Button)findViewById(R.id.btnGeneral);
        btnLocal=(Button)findViewById(R.id.btnLocal);
        btnGuarantee=(Button)findViewById(R.id.btnGuarantee);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);

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
    }
}