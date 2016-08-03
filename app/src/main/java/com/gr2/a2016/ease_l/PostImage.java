package com.gr2.a2016.ease_l;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.entity.ContentType;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;

public class PostImage extends AppCompatActivity {

    private static final int REQUEST_IMAGE_GET = 1;
    EditText name;
    int i = 0;
    ImageView imageView;
    Uri fullPhotoUri= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image);
        Button post = (Button) findViewById(R.id.button);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postImage();
            }
        });
        imageView = (ImageView) findViewById(R.id.imageView2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                name = (EditText) findViewById(R.id.editText);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }
            }
        });
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    void postImage (){
        try {
            InputStream iStream = getContentResolver().openInputStream(fullPhotoUri);
            final byte[] photoBytes = getBytes(iStream);

            RequestQueue mQueue = Volley.newRequestQueue(this);
            final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addBinaryBody("uploadImage", photoBytes, ContentType.create("image/png"), "image.png");

            final HttpEntity httpEntity = builder.build();

            StringRequest r = new StringRequest(Request.Method.POST, "http://ease-l.apphb.com/Image/Download", new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(s);
                    } catch (JSONException e) {
                        Toast.makeText(PostImage.this, "што таъ пошло не так", Toast.LENGTH_LONG).show();
                    }
                    JSONObject object = new JSONObject();
                    try {
                        object.put("Name", name.getText().toString());
                        object.put("Url", obj.get("Result"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest postImage = new JsonObjectRequest(Request.Method.POST, "http://ease-l.apphb.com/project/id" + getIntent().getStringExtra("Id") + "/image", object, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Toast.makeText(PostImage.this, jsonObject.toString(), Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(PostImage.this, "error", Toast.LENGTH_LONG).show();
                        }
                    });
                    RequestQueue queue = Volley.newRequestQueue(PostImage.this);
                    queue.add(postImage);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "што таъ пошло не так", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", httpEntity.getContentType().getValue());
                    return params;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    try {
                        httpEntity.writeTo(byteArrayOutputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return byteArrayOutputStream.toByteArray();

                }

                @Override
                public String getBodyContentType() {
                    return httpEntity.getContentType().getValue();
                }
            };

            mQueue.add(r);

        } catch (IOException e) {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            fullPhotoUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fullPhotoUri);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Bad image", Toast.LENGTH_LONG).show();
            }
            imageView.setImageBitmap(bitmap);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
