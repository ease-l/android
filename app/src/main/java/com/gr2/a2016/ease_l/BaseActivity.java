package com.gr2.a2016.ease_l;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gr2.a2016.ease_l.classes.CommentRequests;
import com.gr2.a2016.ease_l.classes.ImageCanvas;
import com.gr2.a2016.ease_l.classes.NetworkAdresses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class BaseActivity extends Activity {

    String name;
    String imageId;
    Bitmap imageBitmap;
    EditText message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        final Intent intent = getIntent();
        imageId = intent.getStringExtra("Id");
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final RequestQueue queue = Volley.newRequestQueue(BaseActivity.this);
        final ProgressDialog pg = new ProgressDialog(BaseActivity.this);
        pg.setTitle("Downloading...");
        pg.setCanceledOnTouchOutside(false);
        pg.setCancelable(false);
        pg.show();
        Button back = (Button) findViewById(R.id.button2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button newVersion = (Button) findViewById(R.id.button3);
        newVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(BaseActivity.this);
                alert.setTitle("Image version");
                message = new EditText(BaseActivity.this);
                message.setHint("Enter version");
                message.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(message);
                alert.setPositiveButton("OK", myClickListener);
                alert.setNegativeButton("Cancel", myClickListener);
                alert.show();
                Intent intent1 = new Intent(BaseActivity.this,BaseActivity.class);
                intent1.putExtra("Version", message.getText().toString());
            }
        });


        String url;
        if (getIntent().hasExtra("Version")) {
            url = NetworkAdresses.GET_IMAGE_BY_ID + imageId + "/" + intent.getStringExtra("Version");
        } else {
            url = NetworkAdresses.GET_IMAGE_BY_ID + imageId;
        }
        final ArrayList<String> commentIds = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String s = keys.next();
                    if (s.equals("CreationelData") || s.equals("Url") || s.equals("Id")) {
                        continue;
                    }
                    if (s.equals("Name")) {
                        try {
                            ((TextView) findViewById(R.id.textView7)).setText(jsonObject.getString(s));
                            name = jsonObject.getString(s);
                        } catch (JSONException e) {
                            Toast.makeText(BaseActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                        continue;
                    }
                    JSONArray commentIdsArray = new JSONArray();
                    if (s.equals("Comments")) {
                        try {
                            commentIdsArray = jsonObject.getJSONArray(s);
                        } catch (JSONException e) {
                            Toast.makeText(BaseActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                        for (int i = 0; i < commentIdsArray.length(); i++) {
                            try {
                                commentIds.add(commentIdsArray.getString(i));
                            } catch (JSONException e) {
                                Toast.makeText(BaseActivity.this, "Error", Toast.LENGTH_LONG).show();
                            }

                        }

                        continue;
                    }
                    LinearLayout linearHorizontal = new LinearLayout(BaseActivity.this);
                    linearHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                    TextView value = new TextView(BaseActivity.this);
                    value.setTextSize(20);
                    value.setTextColor(Color.BLACK);
                    try {
                        value.setText(jsonObject.getString(s));
                    } catch (JSONException e) {
                        Toast.makeText(BaseActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                    TextView key = new TextView(BaseActivity.this);
                    key.setText(s + " : ");
                    key.setTextSize(25);
                    key.setTextColor(Color.RED);
                    linearHorizontal.addView(key);
                    linearHorizontal.addView(value);
                    ((LinearLayout) findViewById(R.id.linear)).addView(linearHorizontal);
                }
                Button button = new Button(BaseActivity.this);
                button.setText("Add comment");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(BaseActivity.this, CommentActivity.class);
                        intent1.putExtra("name", name);
                        intent1.putExtra("id", imageId);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        intent1.putExtra("bytes", byteArray);
                        startActivity(intent1);
                    }
                });
                ((LinearLayout) findViewById(R.id.linear)).addView(button);
                CommentRequests commentRequests = new CommentRequests();
                commentRequests.loadComments(commentIds, BaseActivity.this, (LinearLayout) findViewById(R.id.linear),(ImageView) findViewById(R.id.imageView));
                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(BaseActivity.this);
                    ImageRequest imageRequest = new ImageRequest(jsonObject.getString("Url"), new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            imageView.setImageBitmap(bitmap);
                            imageBitmap = bitmap;
                            pg.cancel();
                        }
                    }, 0, 0, null, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            pg.cancel();
                            Toast.makeText(BaseActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    });
                    requestQueue.add(imageRequest);
                } catch (JSONException e) {
                    Toast.makeText(BaseActivity.this, "Bad image", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pg.cancel();
                Toast.makeText(BaseActivity.this, "Bad file", Toast.LENGTH_LONG).show();
                Intent root = new Intent(BaseActivity.this, ChooseActivity.class);
                startActivity(root);
                finish();
            }
        });
        queue.add(jsonObjectRequest);
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    if (message.getText().toString().length() > 0) {
                        Intent intent = new Intent(BaseActivity.this, BaseActivity.class);
                        intent.putExtra("Id", imageId);
                        intent.putExtra("Version", message.getText().toString());
                        startActivity(intent);
                    }
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
}
