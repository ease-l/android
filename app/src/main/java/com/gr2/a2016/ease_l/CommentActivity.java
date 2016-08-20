package com.gr2.a2016.ease_l;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class CommentActivity extends AppCompatActivity implements View.OnTouchListener, View.OnLongClickListener{
    ImageView imageView;
    ImageView pointsView;
    Intent intent;
    CommentActivity context;
    Bitmap image;
    String imageid;
    Point finger1;
    Point finger2;
    Point finger3;
    Point finger1doun;
    Point finger2doun;
    Point finger3doun;
    boolean longclick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coment);
        context = this;
        intent = getIntent();
        image = BitmapFactory.decodeByteArray(intent.getByteArrayExtra("bytes"), 0, intent.getByteArrayExtra("bytes").length);

        if(image.getHeight()>=image.getWidth())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        imageView = (ImageView)findViewById(R.id.imageView3);
        imageView.setImageBitmap(image);

        imageid = intent.getStringExtra("id");

        longclick = false;

        pointsView = (ImageView)findViewById(R.id.imageView4);
        pointsView.setOnTouchListener(this);
        pointsView.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }
}
