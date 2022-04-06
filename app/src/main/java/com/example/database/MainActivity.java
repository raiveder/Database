package com.example.database;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnClear;
    EditText dbStamp, dbModel, dbYear, dbMileage;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        dbStamp = (EditText) findViewById(R.id.stamp);
        dbModel = (EditText) findViewById(R.id.model);
        dbYear = (EditText) findViewById(R.id.year);
        dbMileage = (EditText) findViewById(R.id.mileage);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        UpdateTable();
    }

    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int stampIndex = cursor.getColumnIndex(DBHelper.KEY_STAMP);
            int modelIndex = cursor.getColumnIndex(DBHelper.KEY_MODEL);
            int yearIndex = cursor.getColumnIndex(DBHelper.KEY_YEAR);
            int mileageIndex = cursor.getColumnIndex(DBHelper.KEY_MILEAGE);
            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();
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

                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight = 1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("Удалить");
                deleteBtn.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(deleteBtn);

                dbOutput.addView(dbOutputRow);
            } while(cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        String stamp = dbStamp.getText().toString();
        String model = dbModel.getText().toString();
        String year = dbYear.getText().toString() + "г";
        String mileage = dbMileage.getText().toString() + "км";

        switch (v.getId()) {

            case R.id.btnAdd:
                contentValues = new ContentValues();

                contentValues.put(DBHelper.KEY_STAMP, stamp);
                contentValues.put(DBHelper.KEY_MODEL, model);
                contentValues.put(DBHelper.KEY_YEAR, year);
                contentValues.put(DBHelper.KEY_MILEAGE, mileage);

                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);

                UpdateTable();

                dbStamp.setText(null);
                dbModel.setText(null);
                dbYear.setText(null);
                dbMileage.setText(null);
                break;

            case R.id.btnClear:
                database.delete(DBHelper.TABLE_CONTACTS, null, null);
                TableLayout dbOutput = findViewById(R.id.dbOutput);
                dbOutput.removeAllViews();
                UpdateTable();

                dbStamp.setText(null);
                dbModel.setText(null);
                dbYear.setText(null);
                dbMileage.setText(null);
                break;

            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();

                database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + " = ?", new String[]{String.valueOf((v.getId()))});

                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                if (cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID);
                    int stampIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_STAMP);
                    int modelIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_MODEL);
                    int yearIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_YEAR);
                    int mileageIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_MILEAGE);
                    int realID = 1;
                    do {
                        if(cursorUpdater.getInt(idIndex) > realID) {
                            contentValues.put(DBHelper.KEY_ID, realID);
                            contentValues.put(DBHelper.KEY_STAMP, cursorUpdater.getString(stampIndex));
                            contentValues.put(DBHelper.KEY_MODEL, cursorUpdater.getString(modelIndex));
                            contentValues.put(DBHelper.KEY_YEAR, cursorUpdater.getString(yearIndex));
                            contentValues.put(DBHelper.KEY_MILEAGE, cursorUpdater.getString(mileageIndex));
                            database.replace(DBHelper.TABLE_CONTACTS, null, contentValues);
                        }
                        realID++;
                    }while(cursorUpdater.moveToNext());
                    if(cursorUpdater.moveToLast()) {
                    database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();
                }
                break;
        }
    }
}