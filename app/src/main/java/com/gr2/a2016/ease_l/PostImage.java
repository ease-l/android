package com.gr2.a2016.ease_l;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gr2.a2016.ease_l.classes.NetworkAdresses;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.entity.ContentType;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;

public class PostImage extends Activity {

    private static final int REQUEST_IMAGE_GET = 1;
    EditText name;
    int i = 0;
    ImageView imageView;
    Uri fullPhotoUri = null;
    boolean image_selected = false;
    boolean image_update = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image);
        name = (EditText) findViewById(R.id.editText);
        if(getIntent().hasExtra("Image_id")){
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, NetworkAdresses.GET_IMAGE_BY_ID + getIntent().getStringExtra("Image_id"), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Iterator<String> keys = jsonObject.keys();
                    final ProgressDialog progressDialog = new ProgressDialog(PostImage.this);
                    progressDialog.setTitle("Downloading...");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    while (keys.hasNext()) {
                        String key = keys.next();
                        if (key.equals("Name")) {
                            try {
                                ((EditText) findViewById(R.id.editText)).setText(jsonObject.getString(key));
                            } catch (JSONException e) {
                                Toast.makeText(PostImage.this, "Error", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (key.equals("Url")) {
                            try {
                                ImageRequest imageRequest = new ImageRequest(jsonObject.getString(key), new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        image_update = true;
                                        imageView.setImageBitmap(bitmap);
                                        progressDialog.cancel();
                                        progressDialog.dismiss();
                                    }
                                }, 0, 0, null, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        image_update = true;
                                        progressDialog.cancel();
                                        progressDialog.dismiss();
                                        Toast.makeText(PostImage.this, "Error", Toast.LENGTH_LONG).show();
                                    }
                                });
                                RequestQueue queue = Volley.newRequestQueue(PostImage.this);
                                queue.add(imageRequest);
                            } catch (JSONException e) {
                                Toast.makeText(PostImage.this, "Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(PostImage.this,"Error",Toast.LENGTH_LONG).show();
                }
            });
            RequestQueue queue = Volley.newRequestQueue(PostImage.this);
            queue.add(jsonObjectRequest);
        }
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

    void postImage() {
        if ((image_selected && name.getText().toString().length() > 0) || (image_update && name.getText().toString().length() > 0)) {
            try {
                final ProgressDialog pg = new ProgressDialog(PostImage.this);
                pg.setTitle("Uploading...");
                pg.setCanceledOnTouchOutside(false);
                pg.setCancelable(false);
                pg.show();
                InputStream iStream = null;
                byte[] photoBytes = new byte[0];
                if (!image_selected && image_update) {

                } else {
                    iStream = getContentResolver().openInputStream(fullPhotoUri);
                    photoBytes = getBytes(iStream);
                }


                MultipartEntityBuilder builder = null;
                RequestQueue mQueue = Volley.newRequestQueue(this);
                if (!image_selected && image_update) {

                } else {
                    builder = MultipartEntityBuilder.create();
                    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                    builder.addBinaryBody("uploadImage", photoBytes, ContentType.create("image/png"), "image.png");
                }


                final HttpEntity httpEntity;

                if (!image_selected && image_update) {
                    httpEntity = null;
                } else {
                    httpEntity = builder.build();
                    builder = MultipartEntityBuilder.create();
                    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                    builder.addBinaryBody("uploadImage", photoBytes, ContentType.create("image/png"), "image.png");
                }

                StringRequest r = new StringRequest(Request.Method.POST, NetworkAdresses.POST_IMAGE, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(s);
                        } catch (JSONException e) {
                            Toast.makeText(PostImage.this, "Error", Toast.LENGTH_LONG).show();
                        }
                        JSONObject object = new JSONObject();
                        try {
                            object.put("Name", name.getText().toString());
                            object.put("Url", obj.get("Result"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (!getIntent().hasExtra("Image_id")) {
                            JsonObjectRequest postImage = new JsonObjectRequest(Request.Method.POST, NetworkAdresses.GET_PROJECT_BY_ID + getIntent().getStringExtra("Id") + "/image", object, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    pg.cancel();
                                    Toast.makeText(PostImage.this, "Success", Toast.LENGTH_LONG).show();
                                    PostImage.this.setResult(RESULT_OK);
                                    finish();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    pg.cancel();
                                    Toast.makeText(PostImage.this, "Error", Toast.LENGTH_LONG).show();
                                }
                            });
                            RequestQueue queue = Volley.newRequestQueue(PostImage.this);
                            queue.add(postImage);
                        } else {
                            JsonObjectRequest postImage = new JsonObjectRequest(Request.Method.PUT, NetworkAdresses.GET_IMAGE_BY_ID + getIntent().getStringExtra("Image_id"), object, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    pg.cancel();
                                    Toast.makeText(PostImage.this, "Success", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(PostImage.this, ChooseActivity.class);
                                    startActivity(intent);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    pg.cancel();
                                    Toast.makeText(PostImage.this, "Error", Toast.LENGTH_LONG).show();
                                }
                            });
                            RequestQueue queue = Volley.newRequestQueue(PostImage.this);
                            queue.add(postImage);


                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pg.cancel();
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
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
                if (!image_selected && image_update) {
                    JsonObjectRequest getImage = new JsonObjectRequest(Request.Method.GET, NetworkAdresses.GET_IMAGE_BY_ID + getIntent().getStringExtra("Image_id"), null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            JSONObject object = new JSONObject();
                            try {
                                object.put("Name", name.getText().toString());
                                object.put("Url",jsonObject.getString("Url"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JsonObjectRequest postImage = new JsonObjectRequest(Request.Method.PUT, NetworkAdresses.GET_IMAGE_BY_ID + getIntent().getStringExtra("Image_id"), object, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    pg.cancel();
                                    Toast.makeText(PostImage.this, "Success", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(PostImage.this, ChooseActivity.class);
                                    startActivity(intent);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    pg.cancel();
                                    Toast.makeText(PostImage.this, "Error", Toast.LENGTH_LONG).show();
                                }
                            });
                            RequestQueue queue = Volley.newRequestQueue(PostImage.this);
                            queue.add(postImage);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            pg.cancel();
                            Toast.makeText(PostImage.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    });

                    RequestQueue queue = Volley.newRequestQueue(PostImage.this);
                    queue.add(getImage);
                } else {
                    mQueue.add(r);
                }

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(PostImage.this, "Choose image", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            fullPhotoUri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
            String fileName = null;
            ContentResolver cr = getApplicationContext().getContentResolver();

            Cursor metaCursor = cr.query(fullPhotoUri,
                    projection, null, null, null);
            if (metaCursor != null) {
                try {
                    if (metaCursor.moveToFirst()) {
                        fileName = metaCursor.getString(0);
                    }
                } finally {
                    metaCursor.close();
                }
            }

            name.setText(fileName);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fullPhotoUri);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Bad image", Toast.LENGTH_LONG).show();
            }
            imageView.setImageBitmap(bitmap);
            image_selected = true;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
