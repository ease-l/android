package com.gr2.a2016.ease_l.classes;


import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentView {
    private String authorName2;
    private String version;
    private String text;
    private String creationData;
    private LinearLayout vertical;
    private Context context;
    private String name;

    public CommentView(String author, String version, String text, String creationData, LinearLayout vertical, Context context, String name) {
        this.authorName2 = author;
        this.version = version;
        this.text = text;
        this.creationData = creationData;
        this.vertical = vertical;
        this.context = context;
        this.name = name;
    }

    public void addViews(){
        TextView authorName = new TextView(context);
        authorName.setText(authorName2);
        authorName.setTextSize(18);
        authorName.setTextColor(Color.RED);

        TextView commentName = new TextView(context);
        commentName.setText(name);
        commentName.setTextSize(18);
        commentName.setTextColor(Color.BLACK);

        TextView comment = new TextView(context);
        comment.setText(text);
        comment.setTextSize(15);
        comment.setTextColor(Color.BLACK);

        TextView version2 = new TextView(context);
        version2.setText(version);
        version2.setTextSize(10);
        version2.setTextColor(Color.GRAY);

        LinearLayout horizontal = new LinearLayout(context);
        horizontal.setOrientation(LinearLayout.HORIZONTAL);
        horizontal.addView(authorName);
        horizontal.addView(comment);
        vertical.addView(commentName);
        vertical.addView(horizontal);
        vertical.addView(version2);
    }
}
