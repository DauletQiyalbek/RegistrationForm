package com.example.registor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import static com.example.registor.StoreDatabase.COLUMN_EMAIL;
import static com.example.registor.StoreDatabase.COLUMN_INFO;
import static com.example.registor.StoreDatabase.COLUMN_PASSWORD;
import static com.example.registor.StoreDatabase.TABLE_USER;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout up_username, up_email, up_password , up_conPass;
    Button btn_SignUP;
    TextView tvSignIN;
    StoreDatabase storeDatabase;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initViews();

    }

    public void initViews(){
        up_username = findViewById(R.id.up_username);
        up_email = findViewById(R.id.up_email);
        up_password = findViewById(R.id.up_password);
        up_conPass = findViewById(R.id.up_confirmPassord);

        btn_SignUP = findViewById(R.id.btn_signUP);
        tvSignIN = findViewById(R.id.tv_signIn);

        btn_SignUP.setOnClickListener(this);
        tvSignIN.setOnClickListener(this);

        storeDatabase = new StoreDatabase(this);
        sqLiteDatabase = storeDatabase.getWritableDatabase();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signUP:

                boolean createAccount = true;

                String username = up_username.getEditText().getText().toString();
                String email = up_email.getEditText().getText().toString();
                String password = up_password.getEditText().getText().toString();
                String conPass = up_conPass.getEditText().getText().toString();

                if(username.isEmpty()){
                    up_username.setError("Try again");
                    createAccount = false;
                }

                if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    up_email.setError("Try again");
                    createAccount = false;
                }

                if(password.isEmpty() || !password.equals(conPass) || password.length() != 8){
                    up_password.setError("Try again");
                    createAccount = false;
                }

                if(createAccount){

                    // database insert

                    ContentValues userValue = new ContentValues();
                    userValue.put(COLUMN_INFO, up_username.getEditText().getText().toString());
                    userValue.put(COLUMN_EMAIL, up_email.getEditText().getText().toString());
                    userValue.put(COLUMN_PASSWORD, up_password.getEditText().getText().toString());

                    sqLiteDatabase.insert(TABLE_USER, null, userValue);

                    Toast.makeText(this, "Create account success!", Toast.LENGTH_SHORT).show();
                    ShowDatabaseData();
                }else{
                    Toast.makeText(this, "Fill all info, try again!", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.tv_signIn:
                Intent intent = new Intent(Registration.this,Login.class);
                startActivity(intent);
                break;
        }
    }

    public void ShowDatabaseData(){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_USER,null);

        if ((cursor != null && cursor.getCount() > 0)){
            while (cursor.moveToNext()){
                String fName = cursor.getString(cursor.getColumnIndex(COLUMN_INFO));
                String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));

                Log.i("Database","username:"+fName);
                Log.i("Database","email:"+email);
                Log.i("Database","password:"+password);
            }
        }
    }
}