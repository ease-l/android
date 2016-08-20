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

import com.gr2.a2016.ease_l.classes.ImageCanvas;

public class CommentActivity extends AppCompatActivity implements View.OnTouchListener, View.OnLongClickListener, View.OnClickListener {
    ImageView imageView;
    ImageView pointsView;
    Intent intent;
    CommentActivity context;
    Bitmap image;
    String imageid;
    Point finger1;
    Point skrynsyze;
    Point commentpoint;
    boolean draw;
    boolean longclick;
    Button back;
    Button backtoimg;
    Button post;
    EditText text;
    ViewSwitcher switcher;
    int count;
    ImageCanvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coment);
        context = this;
        intent = getIntent();
        draw =false;
        image = BitmapFactory.decodeByteArray(intent.getByteArrayExtra("bytes"), 0, intent.getByteArrayExtra("bytes").length);

        if (image.getHeight() >= image.getWidth())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        imageView = (ImageView) findViewById(R.id.imageView3);
        imageView.setImageBitmap(image);

        imageid = intent.getStringExtra("id");

        longclick = false;

        pointsView = (ImageView) findViewById(R.id.imageView4);
        pointsView.setOnTouchListener(this);
        pointsView.setOnLongClickListener(this);

        back = (Button) findViewById(R.id.button2);
        back.setOnClickListener(this);

        post = (Button) findViewById(R.id.button3);
        post.setOnClickListener(this);

        backtoimg = (Button) findViewById(R.id.button4);
        backtoimg.setOnClickListener(this);

        switcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
    }

    @Override
    public boolean onLongClick(View v) {
        longclick = true;
        if(count == 1){
            draw = true;
            commentpoint = new Point(finger1.x,finger1.y);
            canvas.drawNoBackground(finger1.x,finger1.y,-1,-1);
        }
        if(count == 2){
            draw = false;
            commentpoint = null;
            pointsView.setImageBitmap(null);
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int actionMask = event.getActionMasked();
        switch (actionMask) {
            case MotionEvent.ACTION_DOWN: //первый палец
                if (skrynsyze == null) {
                    skrynsyze = new Point(imageView.getWidth(), imageView.getHeight());
                    canvas = new ImageCanvas(pointsView);
                }
                finger1 = new Point((int) event.getX(), (int) event.getY());
                count = 1;
                break;


            case MotionEvent.ACTION_POINTER_DOWN: //еще палец
                if (!longclick) {
                    if (count == 2) {
                        Point finger1 = null;
                        switcher.showNext();
                        count = 0;
                        break;

                    } else {
                        if (count == 1) {
                            count = 2;
                            break;
                        }
                    }
                }

            case MotionEvent.ACTION_UP: //последний палец убран
                longclick = false;
                count = 0;
                break;

            case MotionEvent.ACTION_POINTER_UP: //один из пальцев убран
                if (count == 2) {
                    count = 1;
                }
                break;

            case MotionEvent.ACTION_MOVE: //пальцы движутся
                if(count == 1){
                    finger1 = new Point((int)event.getX(),(int)event.getY());
                    if(draw){
                        commentpoint = new Point(finger1.x,finger1.y);
                        canvas.drawNoBackground(finger1.x,finger1.y,-1,-1);
                    }
                }
                break;

        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2: {//back
                finish();
            }
            case R.id.button3: {//post

            }
            case R.id.button4: {//back to image
                switcher.showPrevious();
            }
        }
    }
}
