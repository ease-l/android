package com.gr2.a2016.ease_l;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.gr2.a2016.ease_l.classes.Comment;

public class ItemAdapter extends ArrayAdapter<Comment> {

    private ArrayList<Comment> objects;

    public ItemAdapter(Context context, int textViewResourceId, ArrayList<Comment> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }

        Comment i = objects.get(position);

        if (i != null) {
            TextView userName = (TextView) v.findViewById(R.id.userName);
            TextView commentName = (TextView) v.findViewById(R.id.commentName);
            TextView commentText = (TextView) v.findViewById(R.id.commentText);
            TextView version = (TextView) v.findViewById(R.id.version);
            if (userName != null){
                userName.setText(i.getAuthor().getName());
            }
            if (commentName != null){
                commentName.setText(i.getName());
            }
            if (commentText != null){
                commentText.setText(i.getText());
            }
            if (version != null){
                version.setText(i.getVersion());
            }
        }
        return v;

    }

}