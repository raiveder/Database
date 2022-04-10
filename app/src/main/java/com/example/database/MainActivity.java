package com.example.database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnEntry, btnReg;
    EditText login, password;

    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEntry = findViewById(R.id.btnEntry);
        btnEntry.setOnClickListener(this);
        btnReg = findViewById(R.id.btnReg);
        btnReg.setOnClickListener(this);
        login = findViewById(R.id.login);
        login.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                login.setHint("");
            else
                login.setHint("Логин");
        });
        password = findViewById(R.id.password);
        password.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                password.setHint("");
            else
                password.setHint("Пароль");
        });

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEntry:
                boolean logged = true;
                if(login.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    startActivity(new Intent(this, Administrators.class));
                    break;
                }
                Cursor logCursor = database.query(DBHelper.TABLE_AUTHORIZATION,null,null,null,null,null,null);
                if(logCursor.moveToFirst()){
                    int loginIndex = logCursor.getColumnIndex(DBHelper.KEY_LOGIN);
                    int passwordIndex = logCursor.getColumnIndex(DBHelper.KEY_PASSWORD);
                    do{
                        if(login.getText().toString().equals(logCursor.getString(loginIndex)) && password.getText().toString().equals(logCursor.getString(passwordIndex))){
                            logged = false;
                            startActivity(new Intent(this, Users.class));
                            break;
                        }
                    }while(logCursor.moveToNext());
                }
                logCursor.close();
                if(logged) {
                    Toast.makeText(this, "Введённая комбинация логина и пароля не была найдена", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnReg:

                if(login.getText().toString().equals("") || login.getText().toString().equals("")){
                    Toast.makeText(this,"Заполните все поля", Toast.LENGTH_LONG).show();
                    break;
                }
                Cursor regCursor = database.query(DBHelper.TABLE_AUTHORIZATION,null,null,null,null,null,null);
                boolean reg = true;
                if(regCursor.moveToFirst()){
                    int loginIndex = regCursor.getColumnIndex(DBHelper.KEY_LOGIN);
                    do{
                        if(login.getText().toString().equals(regCursor.getString(loginIndex)) || login.getText().toString().equals("admin")){
                            reg = false;
                            Toast.makeText(this,"Пользователь уже зарегистрирован", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }while(regCursor.moveToNext());
                }
                if(reg) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_LOGIN, login.getText().toString());
                    contentValues.put(DBHelper.KEY_PASSWORD, password.getText().toString());
                    database.insert(DBHelper.TABLE_AUTHORIZATION, null, contentValues);
                    regCursor.close();
                    Toast.makeText(this, "Регистрация прошла успешно", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}