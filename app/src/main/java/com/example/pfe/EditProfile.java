package com.example.pfe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditProfile extends AppCompatActivity {

    private Button btnSubmit;


    private EditText username;
    private EditText CIN;
    private EditText phone;
    private String username1;
    private String CIN1;
    private String phone1;
    private int randomNumber;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnSubmit=(Button) findViewById(R.id.btnSubmit);
        username=(EditText) findViewById(R.id.username);
        CIN=(EditText) findViewById(R.id.CIN);
        phone=(EditText) findViewById(R.id.phone);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("")||CIN.getText().toString().equals("")||phone.getText().toString().equals(""))
                {
                    new SweetAlertDialog(EditProfile.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Something went wrong!")
                            .setContentText("one field is empty")
                            .show();
                }
                else{
                    //generate random number for OTP
                    Random random=new Random();
                    randomNumber=random.nextInt(999999);
                    code=String.format("%06d", randomNumber);

                    //sendCode(code);
                    Toast.makeText(getApplicationContext(),code,Toast.LENGTH_LONG).show();
                    openActivity();
                }
            }
        });
    }
    public void openActivity(){
        Intent intent = new Intent(this,ModifyOTP.class);

        username1=username.getText().toString();
        CIN1=CIN.getText().toString();
        phone1=phone.getText().toString();

        intent.putExtra("code",code);
        intent.putExtra("signupUserName",username1);
        intent.putExtra("signupCIN",CIN1);
        intent.putExtra("signupPhone",phone1);

        startActivity(intent);

    }
}