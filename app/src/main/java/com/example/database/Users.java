package com.example.database;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Users extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        dbHelper = new DBHelper(this);
        database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(DBHelper.TABLE_AUTO, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int stampIndex = cursor.getColumnIndex(DBHelper.KEY_STAMP);
            int modelIndex = cursor.getColumnIndex(DBHelper.KEY_MODEL);
            int yearIndex = cursor.getColumnIndex(DBHelper.KEY_YEAR);
            int mileageIndex = cursor.getColumnIndex(DBHelper.KEY_MILEAGE);
            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();

            TableRow dbOutputRowUp = new TableRow(this);
            dbOutputRowUp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TableRow.LayoutParams paramsUp = new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            TextView outputIDUp = new TextView(this);
            paramsUp.weight = 1.0f;
            outputIDUp.setLayoutParams(paramsUp);
            outputIDUp.setText("№\n");
            dbOutputRowUp.addView(outputIDUp);

            TextView outputStampUp = new TextView(this);
            paramsUp.weight = 3.0f;
            outputStampUp.setLayoutParams(paramsUp);
            outputStampUp.setText("Марка\n");
            dbOutputRowUp.addView(outputStampUp);

            TextView outputModelUp= new TextView(this);
            paramsUp.weight = 3.0f;
            outputModelUp.setLayoutParams(paramsUp);
            outputModelUp.setText("Модель\n");
            dbOutputRowUp.addView(outputModelUp);

            TextView outputYearUp= new TextView(this);
            paramsUp.weight = 3.0f;
            outputYearUp.setLayoutParams(paramsUp);
            outputYearUp.setText("Год\n");
            dbOutputRowUp.addView(outputYearUp);

            TextView outputMileageUp= new TextView(this);
            paramsUp.weight = 3.0f;
            outputMileageUp.setLayoutParams(paramsUp);
            outputMileageUp.setText("Пробег\n");
            dbOutputRowUp.addView(outputMileageUp);

            dbOutput.addView(dbOutputRowUp);

            do{
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TableRow.LayoutParams params = new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight = 1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                dbOutputRow.addView(outputID);

                TextView outputStamp = new TextView(this);
                params.weight = 3.0f;
                outputStamp.setLayoutParams(params);
                outputStamp.setText(cursor.getString(stampIndex));
                dbOutputRow.addView(outputStamp);

                TextView outputModel= new TextView(this);
                params.weight = 3.0f;
                outputModel.setLayoutParams(params);
                outputModel.setText(cursor.getString(modelIndex));
                dbOutputRow.addView(outputModel);

                TextView outputYear= new TextView(this);
                params.weight = 3.0f;
                outputYear.setLayoutParams(params);
                outputYear.setText(cursor.getString(yearIndex));
                dbOutputRow.addView(outputYear);

                TextView outputMileage= new TextView(this);
                params.weight = 3.0f;
                outputMileage.setLayoutParams(params);
                outputMileage.setText(cursor.getString(mileageIndex));
                dbOutputRow.addView(outputMileage);

                dbOutput.addView(dbOutputRow);
            } while(cursor.moveToNext());
        }
        cursor.close();
    }
}