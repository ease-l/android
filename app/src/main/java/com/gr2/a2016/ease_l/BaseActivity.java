package com.gr2.a2016.ease_l;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.ActionMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gr2.a2016.ease_l.classes.Comment;
import com.gr2.a2016.ease_l.classes.CommentRequests;
import com.gr2.a2016.ease_l.classes.ImageCanvas;
import com.gr2.a2016.ease_l.classes.NetworkAdresses;
import com.gr2.a2016.ease_l.classes.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class BaseActivity extends ListActivity implements ListView.OnItemLongClickListener{

    String name;
    String imageId;
    Bitmap imageBitmap;
    EditText message;
    ArrayList<Comment> m_parts = new ArrayList<>();
    ItemAdapter m_adapter;
    int position2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ((ListView) findViewById(android.R.id.list)).setOnItemLongClickListener(this);
        m_adapter = new ItemAdapter(BaseActivity.this, R.layout.list_item, m_parts);
        setListAdapter(m_adapter);
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
                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(BaseActivity.this);
                    ImageRequest imageRequest = new ImageRequest(jsonObject.getString("Url"), new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(final Bitmap bitmap) {
                            imageView.setImageBitmap(bitmap);
                            imageBitmap = bitmap;
                            pg.cancel();
                            for (int i = 0; i < commentIds.size(); i++) {
                                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, NetworkAdresses.GET_COMMENT + commentIds.get(i), null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        Iterator<String> keys1= jsonObject.keys();
                                        while(keys1.hasNext()) {
                                            String key = keys1.next();
                                            if (key.equals("attachment")) {
                                                try {
                                                    JSONObject attachment = jsonObject.getJSONObject(key);
                                                    ImageCanvas imageCanvas = new ImageCanvas(bitmap, imageView);
                                                    // imageCanvas.draw(attachment.getInt("x1"),attachment.getInt("y1"),attachment.getInt("x2"),attachment.getInt("y2"));
                                                    imageCanvas.draw(100, 100, 150, 130);
                                                } catch (JSONException e) {
                                                    Toast.makeText(BaseActivity.this, "error", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                        try {
                                            m_parts.add(new Comment(jsonObject.getString("Text"), null, new Person("User", null), jsonObject.getString("Id"), null, jsonObject.getString("Version"), jsonObject.getString("Name")));
                                            m_adapter = new ItemAdapter(BaseActivity.this, R.layout.list_item, m_parts);
                                            setListAdapter(m_adapter);
                                        } catch (JSONException e) {
                                            Toast.makeText(BaseActivity.this, "Error", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Toast.makeText(BaseActivity.this, "Error", Toast.LENGTH_LONG).show();
                                    }
                                });
                                RequestQueue queue1 = Volley.newRequestQueue(BaseActivity.this);
                                queue1.add(objectRequest);
                            }
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
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder alert = new AlertDialog.Builder(BaseActivity.this);
        alert.setPositiveButton("Change", myclicklistener2);
        alert.setTitle("Change comment");
        alert.show();
        m_parts.add(new Comment("text", null, new Person("User name", "id"), "id", null, "version", "Name of comment"));
        m_adapter = new ItemAdapter(BaseActivity.this, R.layout.list_item, m_parts);
        setListAdapter(m_adapter);
        position2 = position;
        return false;
    }

    DialogInterface.OnClickListener myclicklistener2 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    final String[] id = new String[1];
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://ease-l.xyz/Image/"+imageId, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            JSONArray comments = new JSONArray();
                            try {
                                comments = jsonObject.getJSONArray("Comments");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                id[0] = comments.getString(position2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(BaseActivity.this,CommentPost.class);
                            intent.putExtra("Name",m_parts.get(position2).getName());
                            intent.putExtra("Text", m_parts.get(position2).getText());
                            intent.putExtra("Id",id[0]);
                            startActivity(intent);
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(BaseActivity.this,"Error",Toast.LENGTH_LONG).show();
                        }
                    });
                    RequestQueue queue = Volley.newRequestQueue(BaseActivity.this);
                    queue.add(jsonObjectRequest);


                    break;
            }
        }
    };

}
