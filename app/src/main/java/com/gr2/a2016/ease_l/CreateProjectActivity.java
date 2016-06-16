package com.gr2.a2016.ease_l;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class CreateProjectActivity extends Activity {
    ImageView image;
    private int PICK_IMAGE_REQUEST = 1;
    ArrayList<TableRow> tableRows = new ArrayList<>();
    ArrayList<EditText> userNames = new ArrayList<>();
    LinearLayout linearUsers;
    ImageButton accept;

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
                RequestQueue queue = Volley.newRequestQueue(CreateProjectActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(StringRequest.Method.GET, "http://ease-l.apphb.com/Comment", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });

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
        EditText userName = new EditText(CreateProjectActivity.this);
        userName.setHint("User's name");
        userName.setTextSize(20);
        userNames.add(userName);

        Button search = new Button(CreateProjectActivity.this);
        search.setText("Search");
        search.setTextSize(15);

        LinearLayout linear = new LinearLayout(CreateProjectActivity.this);
        linear.setOrientation(LinearLayout.HORIZONTAL);
        linear.addView(userName);
        linear.addView(search);

        TableRow tableRow = new TableRow(CreateProjectActivity.this);
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
