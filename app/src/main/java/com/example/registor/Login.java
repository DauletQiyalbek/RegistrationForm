package com.example.registor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import static com.example.registor.StoreDatabase.COLUMN_EMAIL;
import static com.example.registor.StoreDatabase.COLUMN_PASSWORD;
import static com.example.registor.StoreDatabase.TABLE_USER;

public class Login extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout in_email , in_password;
    Button btn_SignIN;
    TextView tv_singUp;

    StoreDatabase storeDatabase;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    public void initViews(){
        in_email = findViewById(R.id.in_email);
        in_password = findViewById(R.id.in_password);

        btn_SignIN = findViewById(R.id.btn_signIN);
        tv_singUp = findViewById(R.id.tv_signUp);

        btn_SignIN.setOnClickListener(this);
        tv_singUp.setOnClickListener(this);
        storeDatabase = new StoreDatabase(this);
        sqLiteDatabase = storeDatabase.getWritableDatabase();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signIN:
                boolean LoginAccount = true;

                String userEmail = in_email.getEditText().getText().toString();
                String userPassword = in_password.getEditText().getText().toString();


                if (userEmail.isEmpty()){
                    in_email.setError("Try again!");
                    LoginAccount = false;
                }
                if (userPassword.isEmpty()){
                    in_password.setError("Try again!");
                    LoginAccount = false;
                }
                if (LoginAccount){
                    Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " +
                            COLUMN_EMAIL+ "=? AND "+COLUMN_PASSWORD+ "=? ",new String[]{userEmail,userPassword});
                    if (cursor!=null & cursor.getCount()>0){
                        cursor.moveToFirst();
                        Toast.makeText(this, "Welcome!"+userEmail, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this,Home.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.tv_signIn:
                Intent intent = new Intent(Login.this,Registration.class);
                startActivity(intent);
                break;
        }
    }
}