package com.example.androidlabs;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<Message>
{
    Context context;
    int resource;
    ArrayList<Message> messageList = null;

    public ChatAdapter(Context c, int r, ArrayList<Message> ml){
        super(c,r,ml);
        context = c;
        resource = r;
        messageList = ml;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Message message = messageList.get(position);
        if (convertView == null){
            if (getItemViewType(position) == 1) {
                convertView = LayoutInflater.from(context).inflate(R.layout.chatrecieve, parent, false);
            }else{
                convertView = LayoutInflater.from(context).inflate(R.layout.chatsend, parent, false);
            }
        }
        TextView messageContents = convertView.findViewById(R.id.messageContent);
        ImageView profileImage = convertView.findViewById(R.id.profilePicture);

        messageContents.setText(message.getMessageContents());
        profileImage.setImageResource(message.getImageID());
        return convertView;
    }
    @Override
    public int getItemViewType(int position){
        Message message = messageList.get(position);
        if(message.isFake()) {
            return 1;
        }else{
            return 0;
        }
    }
    @Override
    public int getViewTypeCount(){
        return 2;
    }
}
