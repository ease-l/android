package com.gr2.a2016.ease_l.classes;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gr2.a2016.ease_l.BaseActivity;
import com.gr2.a2016.ease_l.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class CommentRequests {
    public void addComment(String name, String text, final Context context, String rootId) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("text", text);
        } catch (JSONException e) {
            Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://ease-l.apphb.com/Project" + /*getIntent().getStringExtra("rootId")*/rootId + "/comment", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    public void loadComments(ArrayList<String> commentIds, final Context context, final LinearLayout linear) {
        for (int i = 0; i < commentIds.size(); i++) {
            RequestQueue queue = Volley.newRequestQueue(context);
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
                                Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (key.equals("Version")) {
                            try {
                                version = jsonObject.getString(key);
                            } catch (JSONException e) {
                                Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (key.equals("Text")) {
                            try {
                                text = jsonObject.getString(key);
                            } catch (JSONException e) {
                                Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (key.equals("Id")) {
                            try {
                                id = jsonObject.getString(key);
                            } catch (JSONException e) {
                                Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    Comment comment = new Comment(text,null,null,id,null,version,name);
                    CommentView commentView = new CommentView(comment,linear, context);
                    commentView.addViews();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            queue.add(jsonObjectRequest);
        }
    }
}
