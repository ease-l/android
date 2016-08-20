package com.gr2.a2016.ease_l.classes;


import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentView {
    private LinearLayout vertical;
    private Context context;
    private Comment comment;

    public CommentView(Comment comment, LinearLayout vertical, Context context) {
        this.vertical = vertical;
        this.context = context;
        this.comment = comment;
    }

    public void addViews(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,0);
        //params.gravity = Gravity.RIGHT;
        TextView authorName = new TextView(context);
        authorName.setText(/*comment.getAuthor().getName()*/"User");
        authorName.setTextSize(18);
        authorName.setTextColor(Color.RED);

        TextView commentName = new TextView(context);
        commentName.setText(comment.getName());
        commentName.setTextSize(18);
        commentName.setTextColor(Color.BLACK);

        TextView commentText = new TextView(context);
        commentText.setText(comment.getText());
        commentText.setTextSize(15);
        commentText.setTextColor(Color.BLACK);

        TextView version2 = new TextView(context);
        version2.setText(comment.getVersion());
        version2.setTextSize(10);
        version2.setTextColor(Color.BLUE);

        LinearLayout horizontal = new LinearLayout(context);
        horizontal.setOrientation(LinearLayout.HORIZONTAL);
        horizontal.addView(authorName);
        horizontal.addView(commentText);
    //    horizontal.setLayoutParams(params);
        vertical.addView(commentName);
        vertical.addView(horizontal);
        vertical.addView(version2);
    }
}
