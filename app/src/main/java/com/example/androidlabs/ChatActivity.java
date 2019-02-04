package com.example.androidlabs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        addToChat("Hi, feel free to start messaging at your leisure.",R.drawable.row_recieve,true);
        adapter = new ChatAdapter(getApplicationContext(), R.layout.chatrecieve,messages);
        chat.setAdapter(adapter);

        send.setOnClickListener( c -> {
            addToChat(chatIn.getText().toString(),R.drawable.row_send,false);
            chatIn.setText("");
            adapter.notifyDataSetChanged();
        });

        recieve.setOnClickListener( c -> {
            addToChat(responses[random.nextInt(5 - 0)],R.drawable.row_recieve,true);
            adapter.notifyDataSetChanged();
        });
    }

    void addToChat(String messageIn, int img, boolean fakeID){
        Message newMsg = new Message(messageIn,img,fakeID);
        messages.add(newMsg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}