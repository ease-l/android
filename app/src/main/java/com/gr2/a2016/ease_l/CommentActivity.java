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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class CommentActivity extends AppCompatActivity implements View.OnTouchListener, View.OnLongClickListener, View.OnClickListener {
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
    Button back;
    Button backtoimg;
    Button post;
    EditText text;
    ViewSwitcher switcher;
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

        back = (Button)findViewById(R.id.button2);
        back.setOnClickListener(this);

        post = (Button)findViewById(R.id.button3);
        post.setOnClickListener(this);

        backtoimg = (Button)findViewById(R.id.button4);
        backtoimg.setOnClickListener(this);

        switcher = (ViewSwitcher)findViewById(R.id.viewSwitcher);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button2:{//back
                finish();
            }
            case R.id.button3:{//post

            }
            case R.id.button4:{//back to image
                switcher.showPrevious();
            }
        }
    }
}
