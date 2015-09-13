package com.cpsudevelopers.mig.sqlitebasics;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private MyHelper dbHelper;
    private SQLiteDatabase db;
    SimpleCursorAdapter adapter;
    long deleteItmeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyHelper(this);
        db = dbHelper.getWritableDatabase();   // check if databese available

        Cursor cursor = readAllData();

        adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[]{MyHelper.COL_NAME, MyHelper.COL_PHONE_NUMBER},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteItmeId = id;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.execSQL("DELETE FROM contact WHERE _id =" + deleteItmeId);
                                Cursor cursor = readAllData();
                                adapter.changeCursor(cursor);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        Button btnInsert = (Button) findViewById(R.id.insertButton);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(i, 1);
            }
        });
    }

    private Cursor readAllData() {
        String[] columns = {
                MyHelper.COL_ID,
                MyHelper.COL_NAME,
                MyHelper.COL_PHONE_NUMBER
        };
        Cursor cursor = db.query(MyHelper.TABLE_NAME, columns, null, null, null, null, null);

        return cursor;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                ContentValues cv = new ContentValues();
                cv.put(MyHelper.COL_NAME, data.getStringExtra("name"));
                cv.put(MyHelper.COL_PHONE_NUMBER, data.getStringExtra("phone"));
                db.insert(MyHelper.TABLE_NAME, null, cv);

                Cursor cursor = readAllData();
                adapter.changeCursor(cursor);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
