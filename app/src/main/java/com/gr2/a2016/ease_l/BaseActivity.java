package com.gr2.a2016.ease_l;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
import com.gr2.a2016.ease_l.classes.Comment;
import com.gr2.a2016.ease_l.classes.CommentView;
import com.gr2.a2016.ease_l.classes.NetworkAdreses;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        String image_id = getIntent().getStringExtra("Id");
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        RequestQueue queue = Volley.newRequestQueue(BaseActivity.this);
        final ProgressDialog pg = new ProgressDialog(BaseActivity.this);
        pg.setTitle("Downloading");
        pg.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, NetworkAdreses.GET_IMAGE_BY_ID + image_id, null, new Response.Listener<JSONObject>() {
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
                            Toast.makeText(BaseActivity.this, "error at downloading", Toast.LENGTH_LONG).show();
                        }
                        continue;
                    }
                    ArrayList<String> commentIds = new ArrayList<>();
                    if (s.equals("Comments")) {
                        String commentBase = "";
                        try {
                            commentBase = jsonObject.getString(s);
                        } catch (JSONException e) {
                            Toast.makeText(BaseActivity.this, "error at downloading", Toast.LENGTH_LONG).show();
                        }
                        if (commentBase.length() > 1) {
                            for (int i = 0; i < commentBase.length(); i++) {
                                if (commentBase.charAt(i) == '"') {
                                    i++;
                                    int firstChar = i;
                                    while (i < commentBase.length() && commentBase.charAt(i) != '"') {
                                        i++;
                                    }
                                    String commmentId = commentBase.substring(firstChar, i);
                                    commentIds.add(commmentId);
                                }
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
                        Toast.makeText(BaseActivity.this, "error at downloading", Toast.LENGTH_LONG).show();
                    }
                    TextView key = new TextView(BaseActivity.this);
                    key.setText(s + " : ");
                    key.setTextSize(25);
                    key.setTextColor(Color.RED);
                    linearHorizontal.addView(key);
                    linearHorizontal.addView(value);
                    ((LinearLayout) findViewById(R.id.linear)).addView(linearHorizontal);
                    loadComments(commentIds);
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
                            Toast.makeText(BaseActivity.this, "error in image", Toast.LENGTH_LONG).show();
                        }
                    });
                    requestQueue.add(imageRequest);
                } catch (JSONException e) {
                    Toast.makeText(BaseActivity.this, "Bad Image", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(BaseActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    public void loadComments(ArrayList<String> commentIds) {
        for (int i = 0; i < commentIds.size(); i++) {
            RequestQueue queue = Volley.newRequestQueue(BaseActivity.this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, NetworkAdreses.GET_COMMENT + commentIds.get(i), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Iterator<String> keys = jsonObject.keys();
                    String name = "";
                    String version = "";
                    String text = "";
                    String id = "";
                    while (keys.hasNext()) {
                        String key = keys.next();
                        if (key.equals("Name")) {
                            try {
                                name = jsonObject.getString(key);
                            } catch (JSONException e) {
                                Toast.makeText(BaseActivity.this, "error", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (key.equals("Version")) {
                            try {
                                version = jsonObject.getString(key);
                            } catch (JSONException e) {
                                Toast.makeText(BaseActivity.this, "error", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (key.equals("Text")) {
                            try {
                                text = jsonObject.getString(key);
                            } catch (JSONException e) {
                                Toast.makeText(BaseActivity.this, "error", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (key.equals("Id")) {
                            try {
                                id = jsonObject.getString(key);
                            } catch (JSONException e) {
                                Toast.makeText(BaseActivity.this, "error", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    Comment comment = new Comment(text,null,null,id,null,version,name);
                    CommentView commentView = new CommentView(comment,((LinearLayout) findViewById(R.id.linear)), BaseActivity.this);
                    commentView.addViews();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(BaseActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            queue.add(jsonObjectRequest);
        }
    }

    public void addComment(String name, String text) {
        RequestQueue queue = Volley.newRequestQueue(BaseActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("text", text);
        } catch (JSONException e) {
            Toast.makeText(BaseActivity.this, "error", Toast.LENGTH_LONG).show();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://ease-l.apphb.com/Project" + getIntent().getStringExtra("rootId") + "/comment", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Toast.makeText(BaseActivity.this, jsonObject.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(BaseActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonObjectRequest);
    }
}
