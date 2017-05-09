package com.example.user.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private  static final String NOTEPOS = "NOTEPOS";

    Button btn_addnote;
    ListView lv_note;
    SQLiteDatabase db;
    ArrayList<String> titlelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_addnote = (Button)findViewById(R.id.btn_addnote);
        btn_addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddNoteActivity.class);
                intent.putExtra(NOTEPOS, -1);
                startActivity(intent);
            }
        });

        lv_note = (ListView)findViewById(R.id.lv_note);
        lv_note.setOnItemClickListener(iclick);
        lv_note.setOnItemLongClickListener(ilclick);
    }


    @Override
    protected void onPause(){
        super.onPause();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

        DBOpenHelper openhelper = new DBOpenHelper(this);
        db = openhelper.getWritableDatabase();

        titlelist = NoteDB.getTitleList(db);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titlelist);
        lv_note.setAdapter(adapter);
    }

    AdapterView.OnItemClickListener iclick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> av, View v,
                                int position, long id) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, AddNoteActivity.class);
            intent.putExtra("NOTEPOS", position);
            startActivity(intent);
        }
    };

    AdapterView.OnItemLongClickListener ilclick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> av, View v, int position, long id) {
            String title = titlelist.get(position);
            NoteDB.delNote(db, title);
            titlelist = NoteDB.getTitleList(db);
            ArrayAdapter<String> adapter = new ArrayAdapter<String> (MainActivity.this, android.R.layout.simple_list_item_1, titlelist);
            lv_note.setAdapter(adapter);
            return false;
        }

    };

}