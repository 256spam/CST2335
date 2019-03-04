package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {
    ListView chat;
    Button send;
    Button recieve;
    EditText chatIn;
    ArrayList<Message> messages;
    String[] responses = new String[] {"Hello", "Reminder, I am not a real person.", "Is there anything you need?", "Yes", "No"};
    ChatAdapter adapter;
    final static String TABLE_NAME = "Messages";
    final static String COL_CONTENTS = "CONTENTS";
    final static String COL_IMAGE = "IMAGE";
    final static String COL_SIDE = "SIDE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chat=findViewById(R.id.List_Chat);
        send=findViewById(R.id.buttonSend);
        recieve=findViewById(R.id.buttonReceieve);
        chatIn=findViewById(R.id.chatInput);
        Random random = new Random();
        messages = new ArrayList<Message>();

        DatabaseOpener dataOpener = new DatabaseOpener(this);
        SQLiteDatabase db = dataOpener.getWriteableDatabase();

        adapter = new ChatAdapter(getApplicationContext(), R.layout.chatrecieve,messages);
        chat.setAdapter(adapter);

        Cursor csr = db.rawQuery("SELECT * from " + TABLE_NAME, null);
        csr.moveToFirst();
        for(int i = 0; i < csr.getCount(); i++){
            String cnts = csr.getString(csr.getColumnIndex( COL_CONTENTS ));
            int img = csr.getInt(csr.getColumnIndex( COL_IMAGE ));
            int side = csr.getInt(csr.getColumnIndex( COL_SIDE ));
            long idnum = csr.getLong(csr.getColumnIndex( "id"));
            addToChat(cnts,img,side,idnum);
            csr.moveToNext();
        }
        printCursor(csr,db);

        send.setOnClickListener( c -> {
            ContentValues cv = new ContentValues();
            cv.put(dataOpener.COL_CONTENTS, chatIn.getText().toString());
            cv.put(dataOpener.COL_IMAGE, R.drawable.row_send);
            cv.put(dataOpener.COL_SIDE, 0);
            long id = db.insert(TABLE_NAME, "NullColumnName", cv);
            addToChat(chatIn.getText().toString(),R.drawable.row_send,0, id);

            chatIn.setText("");
            adapter.notifyDataSetChanged();
        });

        recieve.setOnClickListener( c -> {
            int temp = random.nextInt(5 - 0);

            ContentValues cv = new ContentValues();
            cv.put(dataOpener.COL_CONTENTS, responses[temp]);
            cv.put(dataOpener.COL_IMAGE, R.drawable.row_recieve);
            cv.put(dataOpener.COL_SIDE, 1);
            long id = db.insert(TABLE_NAME, "NullColumnName", cv);

            addToChat(responses[temp],R.drawable.row_recieve,1, id);
            adapter.notifyDataSetChanged();
        });
    }

    void addToChat(String messageIn, int img, int side, long id){
        Message newMsg = new Message(messageIn,img,side,id);
        messages.add(newMsg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    protected void printCursor(Cursor c, SQLiteDatabase db){
        Log.d("cursorDebug",("Version: " +db.getVersion()));
        Log.d("cursorDebug",("Result Count:" + c.getColumnCount()));
        for(int i = 0; i < c.getColumnNames().length; i++) {
            String[] temp = c.getColumnNames();
            Log.d("cursorDebug", ("Column Name:" + temp[i]));
        }
        Log.d("cursorDebug",("Result Count:" + c.getCount()));
        c.moveToFirst();
        for(int i = 0; i < c.getCount(); i++){
            Log.d("cursorDebug",("Message Contents:" + c.getString(c.getColumnIndex( COL_CONTENTS ))));
            Log.d("cursorDebug",("Message Image:" + c.getInt(c.getColumnIndex( COL_IMAGE ))));
            Log.d("cursorDebug",("Message Side:" + c.getInt(c.getColumnIndex( COL_SIDE ))));
            Log.d("cursorDebug",("Message ID:" + c.getLong(c.getColumnIndex( "id"))));
            c.moveToNext();
        }
    }

    public class DatabaseOpener extends SQLiteOpenHelper{
        final static String DATABASE_NAME = "chatDB";
        final static int VERSION = 2;
        final static String TABLE_NAME = "Messages";
        final static String COL_CONTENTS = "CONTENTS";
        final static String COL_IMAGE = "IMAGE";
        final static String COL_SIDE = "SIDE";

        public DatabaseOpener(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " +TABLE_NAME+ "( id INTEGER PRIMARY KEY AUTOINCREMENT, CONTENTS text, IMAGE int, SIDE INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
            onCreate(db);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
        }

        public SQLiteDatabase getWriteableDatabase() {
            return super.getWritableDatabase();
        }
    }
}