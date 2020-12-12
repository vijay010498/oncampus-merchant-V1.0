package com.svijayr007.oncampuspartner.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.svijayr007.oncampuspartner.R;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton login_Button;
    private TextView phone_number_edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        login_Button = findViewById(R.id.button_login);
        phone_number_edt = findViewById(R.id.edit_phone_login);
        setListener();
    }

    private void setListener() {
        phone_number_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                if(phone_number_edt.getText().toString().length() == 10) {
                    if(TextUtils.isDigitsOnly(phone_number_edt.getText().toString())) {
                        String phone_number = phone_number_edt.getText().toString();
                        Intent otpIntent = new Intent(LoginActivity.this,OtpActivity.class);
                        otpIntent.putExtra("CUSTOMER_MOBILE","+91"+phone_number);
                        startActivity(otpIntent);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Invalid Number", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = phone_number_edt.getText().toString();
                if(!phone_number.isEmpty() && phone_number.length()==10){
                    Intent otpIntent = new Intent(LoginActivity.this,OtpActivity.class);
                    otpIntent.putExtra("CUSTOMER_MOBILE","+91"+phone_number);
                    startActivity(otpIntent);
                }else {
                    Toast.makeText(LoginActivity.this, "Invalid Phone Number!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}