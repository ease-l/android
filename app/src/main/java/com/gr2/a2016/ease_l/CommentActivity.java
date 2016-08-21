package com.gr2.a2016.ease_l;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gr2.a2016.ease_l.classes.ImageCanvas;
import com.gr2.a2016.ease_l.classes.NetworkAdresses;

import org.json.JSONException;
import org.json.JSONObject;

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
    EditText name;
    RequestQueue queue;
    ProgressBar bar;
    public static final String MY_LOG_TAG = "MyLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coment);
        context = this;
        intent = getIntent();
        draw = false;
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

        text = (EditText) findViewById(R.id.editText2);

        name = (EditText) findViewById(R.id.editText3);

        queue = Volley.newRequestQueue(context);

        bar = (ProgressBar) findViewById(R.id.progressBar3);
    }

    @Override
    public boolean onLongClick(View v) {
        longclick = true;
        if (count == 1) {
            draw = true;
            commentpoint = new Point(finger1.x, finger1.y);
            canvas.drawNoBackground(finger1.x, finger1.y, -1, -1);
            Log.d(MY_LOG_TAG,"long1 " + commentpoint.x+ " "+ commentpoint.y);
        }
        if (count == 2) {
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
                        finger1 = null;
                        switcher.showNext();
                        count = 0;
                        Log.d(MY_LOG_TAG,"33t " + commentpoint.x+ " "+ commentpoint.y);
draw = false;
                    } else {
                        if (count == 1) {
                            count = 2;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP: //последний палец убран
                longclick = false;
                count = 0;
                draw = false;
                break;

            case MotionEvent.ACTION_POINTER_UP: //один из пальцев убран
                if (count == 2) {
                    count = 1;
                }
                break;

            case MotionEvent.ACTION_MOVE: //пальцы движутся
                if (count == 1) {
                    finger1 = new Point((int) event.getX(), (int) event.getY());
                    if (draw) {
                        commentpoint.x = (int) event.getX();
                        commentpoint.y = (int) event.getY();
                        canvas.drawNoBackground(finger1.x, finger1.y, -1, -1);
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
                if (!text.getText().toString().equals("") && !name.getText().toString().equals("")) {
                    JSONObject attachment = new JSONObject();
                    JSONObject object = new JSONObject();
                    try {
                        Log.d(MY_LOG_TAG,"send " + commentpoint.x+ " "+ commentpoint.y+" "+skrynsyze.x+" "+skrynsyze.y);
                        if (commentpoint != null) {
                            int x = commentpoint.x;
                            x*=1000;
                            x/=skrynsyze.x;
                            int y = commentpoint.y;
                            y*=1000;
                            y/=skrynsyze.y;
                            attachment.put("upleft", x );
                            attachment.put("upright", y);
                        } else {
                            attachment.put("upleft", -1);
                            attachment.put("upright", -1);
                        }
                        Log.d(MY_LOG_TAG," "+ attachment.toString());
                        attachment.put("downleft", -1);
                        attachment.put("downright", -1);
                        object.put("attachment", attachment);
                        object.put("name", name.getText().toString());
                        object.put("text", text.getText().toString());
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, NetworkAdresses.GET_All_IMAGES + "/" + imageid + "/comment", object, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject object) {
                                Toast.makeText(context, "success", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                bar.setVisibility(View.VISIBLE);
                                post.setClickable(false);
                                Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
                            }
                        });
                        queue.add(request);
                        bar.setVisibility(View.VISIBLE);
                        post.setClickable(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "enter name and just comment!!", Toast.LENGTH_LONG).show();
                }
            }
            case R.id.button4: {//back to image
                switcher.showPrevious();
            }
        }
    }
}
