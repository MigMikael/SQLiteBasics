package com.cpsudevelopers.mig.sqlitebasics;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MyHelper dbHelper;
    private SQLiteDatabase db;
    SimpleCursorAdapter adapter;

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

        Button btnInsert = (Button) findViewById(R.id.insertButton);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put(MyHelper.COL_NAME, "The dog");
                cv.put(MyHelper.COL_PHONE_NUMBER, "000-000-0000");
                db.insert(MyHelper.TABLE_NAME, null, cv);

                Cursor cursor = readAllData();
                adapter.changeCursor(cursor);
            }
        });

        Button btnDelete = (Button) findViewById(R.id.delete_button);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*db.delete(
                        MyHelper.TABLE_NAME,
                        MyHelper.COL_PHONE_NUMBER + " LIKE ? ",
                        new String[]{ "000%" }
                );*/

                db.execSQL("DELETE FROM contact WHERE phone_number LIKE '000%'");

                Cursor cursor = readAllData();
                adapter.changeCursor(cursor);
            }
        });
        /*int count = cursor.getCount();

        Log.i(TAG, "Number of row = " + count);

        String msg = "";
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(MyHelper.COL_NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(MyHelper.COL_PHONE_NUMBER));
            msg = String.format("Name: %s , Phone Number: %s\n", name, phoneNumber);
        }

        TextView text = (TextView) findViewById(R.id.text);
        text.setText(msg);
        */
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
