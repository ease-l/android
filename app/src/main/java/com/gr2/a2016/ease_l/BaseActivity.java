package com.gr2.a2016.ease_l;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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
import com.gr2.a2016.ease_l.classes.NetworkAdresses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Intent intent = getIntent();
        String image_id = intent.getStringExtra("Id");
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final RequestQueue queue = Volley.newRequestQueue(BaseActivity.this);
        final ProgressDialog pg = new ProgressDialog(BaseActivity.this);
        pg.setTitle("Uploading...");
        pg.setCanceledOnTouchOutside(false);
        pg.setCancelable(false);
        pg.show();

        String test_url = NetworkAdresses.GET_IMAGE_BY_ID + image_id + "/" + intent.getStringExtra("Version");
        JsonObjectRequest testRequest = new JsonObjectRequest(Request.Method.GET, test_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(testRequest);
        String url;
        if (getIntent().hasExtra("Version")) {
            url = NetworkAdresses.GET_IMAGE_BY_ID + image_id + "/" + intent.getStringExtra("Version");
        } else {
            url = NetworkAdresses.GET_IMAGE_BY_ID + image_id;
        }
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
                        } catch (JSONException e) {
                            Toast.makeText(BaseActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                        continue;
                    }
                    JSONArray commentIdsArray = new JSONArray();
                    ArrayList<String> commentIds = new ArrayList<>();

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
                        CommentRequests commentRequests = new CommentRequests();
                        commentRequests.loadComments(commentIds, BaseActivity.this, (LinearLayout) findViewById(R.id.linear));
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
                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(BaseActivity.this);
                    ImageRequest imageRequest = new ImageRequest(jsonObject.getString("Url"), new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            imageView.setImageBitmap(bitmap);
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
                Toast.makeText(BaseActivity.this, "Bas file", Toast.LENGTH_LONG).show();
                Intent root = new Intent(BaseActivity.this, ChooseActivity.class);
                startActivity(root);
                finish();
            }
        });
        queue.add(jsonObjectRequest);
    }


}
