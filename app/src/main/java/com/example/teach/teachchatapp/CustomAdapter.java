package com.example.teach.teachchatapp;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by TeacH on 5/19/2016.
 */
public class CustomAdapter extends ArrayAdapter<Messages> {
    public CustomAdapter(Context context, List<Messages> messages) {
        super(context, R.layout.custom_row, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_row, parent, false);
        TextView etNicknameRow = (TextView)customView.findViewById(R.id.etNicknameRow);
        TextView etMessageRow = (TextView)customView.findViewById(R.id.etMessageRow);
        TextView etTimestampRow = (TextView)customView.findViewById(R.id.etTimestampRow);

        Messages singleMessage = getItem(position);

        if(singleMessage.getNickname() != null) {
            etNicknameRow.setText(singleMessage.getNickname());
            etMessageRow.setText(singleMessage.getMessage());
            etTimestampRow.setText(singleMessage.getTimestamp());
        }
        else{
            etNicknameRow.setText("");
            etMessageRow.setText("");
            etTimestampRow.setText("");
        }

        return customView;
    }
}
