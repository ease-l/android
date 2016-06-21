package com.gr2.a2016.ease_l;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends Activity {
    ImageView image;
    private int PICK_IMAGE_REQUEST = 1;
    ArrayList<TableRow> tableRows = new ArrayList<>();
    ArrayList<EditText> userNames = new ArrayList<>();
    LinearLayout linearUsers;
    ImageButton accept;
    ImageButton cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        linearUsers = (LinearLayout) findViewById(R.id.users);
        image = (ImageView) findViewById(R.id.imageView2);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        createUserSearch();
        Handler h = new Handler();
        h.postDelayed(r, 1000);
        accept = (ImageButton) findViewById(R.id.imageButton);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                JsonArrayRequest jsonObjectRequest = new JsonArrayRequest("http://ease-l.apphb.com/Comment", new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Iterator<String> keys = object.keys();
                                while (keys.hasNext()) {
                                    Toast.makeText(getApplicationContext(), object.getString(keys.next()), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {

                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(),volleyError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(jsonObjectRequest);
                Intent intent = new Intent(MainActivity.this, BaseActivity.class);
                startActivity(intent);
            }
        });

        cancel = (ImageButton) findViewById(R.id.imageButton2);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                JsonArrayRequest jsonObjectRequest = new JsonArrayRequest("http://ease-l.apphb.com/Image", new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Iterator<String> keys = object.keys();
                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    Toast.makeText(getApplicationContext(), object.getString(key), Toast.LENGTH_LONG).show();
                                    if(key.equals("Url")){
                                        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                                        ImageRequest imageRequest = new ImageRequest(object.getString(key),
                                                new Response.Listener<Bitmap>() {
                                                    @Override
                                                    public void onResponse(Bitmap bitmap) {
                                                        image.setImageBitmap(bitmap);
                                                    }
                                                }, 0, 0, null,
                                                new Response.ErrorListener() {
                                                    public void onErrorResponse(VolleyError error) {

                                                    }
                                                });
                                        queue.add(imageRequest);
                                    }
                                }
                            } catch (JSONException e) {

                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(),volleyError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(jsonObjectRequest);
            }
        });
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < tableRows.size(); i++) {
                int filledUserNamesCounter = 0;
                for (int j = 0; j < tableRows.size(); j++) {
                    if (userNames.get(j).getText().length() != 0) {
                        filledUserNamesCounter++;
                    }
                }
                if (userNames.size() == filledUserNamesCounter) {
                    createUserSearch();
                    break;
                }
                filledUserNamesCounter = 0;
                for (int j = 0; j < tableRows.size(); j++) {
                    if (userNames.get(j).getText().length() != 0) {
                        filledUserNamesCounter++;
                    }
                }
                if (userNames.get(i).getText().length() == 0 && filledUserNamesCounter != userNames.size() - 1) {
                    linearUsers.removeView(tableRows.get(i));
                    tableRows.remove(i);
                    userNames.remove(i);
                    break;
                }
            }
            Handler h = new Handler();
            h.postDelayed(r, 1000);
        }
    };

    private void createUserSearch() {
        EditText userName = new EditText(MainActivity.this);
        userName.setHint("User's name");
        userName.setTextSize(20);
        userNames.add(userName);

        Button search = new Button(MainActivity.this);
        search.setText("Search");
        search.setTextSize(15);

        LinearLayout linear = new LinearLayout(MainActivity.this);
        linear.setOrientation(LinearLayout.HORIZONTAL);
        linear.addView(userName);
        linear.addView(search);

        TableRow tableRow = new TableRow(MainActivity.this);
        tableRow.addView(linear);
        linearUsers.addView(tableRow);
        tableRows.add(tableRow);
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "SELECT IMAGE"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                int height = bitmap.getHeight();
                int widht = bitmap.getWidth();
                Bitmap bmHalf;
                if (height > 2048 || widht > 2048) {
                    bmHalf = Bitmap.createScaledBitmap(bitmap, widht / 2, height / 2, false);
                    image.setImageBitmap(bmHalf);
                } else {
                    image.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
