package com.gr2.a2016.ease_l;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gr2.a2016.ease_l.classes.NetworkAdreses;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateProject extends AppCompatActivity implements View.OnClickListener{
RequestQueue queue;
    CreateProject context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        ((Button)findViewById(R.id.backpr)).setOnClickListener(this);
        ((Button)findViewById(R.id.sendpr)).setOnClickListener(this);
        queue = Volley.newRequestQueue(this);
        context = this;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backpr){
            finish();
        }else {
            ((Button)findViewById(R.id.sendpr)).setClickable(false);
            String name = ((EditText)findViewById(R.id.prname)).getText().toString();
            if(!name.equals("")){
                String id = getIntent().getStringExtra("Id");
                JSONObject object = new JSONObject();
                try {
                    object.put("Name",name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!id.equals(" ")){
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, NetworkAdreses.GET_PROJECT_BY_ID + id + "/project", object,
                            new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Toast.makeText(context,"успех!!",Toast.LENGTH_LONG).show();
                            context.setResult(RESULT_OK);
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            ((Button)findViewById(R.id.sendpr)).setClickable(true);
                            Toast.makeText(context, "что-то пошло не так( " + volleyError.toString() + " )", Toast.LENGTH_LONG).show();
                            ((ProgressBar)findViewById(R.id.progressBar2)).setVisibility(View.INVISIBLE);
                        }
                    });
                    request.setRetryPolicy(new DefaultRetryPolicy(
                            30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(request);
                    ((ProgressBar)findViewById(R.id.progressBar2)).setVisibility(View.VISIBLE);
                }else {
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, NetworkAdreses.GET_ALL_PROJECTS, object, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Toast.makeText(context,"успех!!",Toast.LENGTH_LONG).show();
                            context.setResult(RESULT_OK);
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            ((Button)findViewById(R.id.sendpr)).setClickable(true);
                            Toast.makeText(context, "что-то пошло не так( " + volleyError.toString() + " )", Toast.LENGTH_LONG).show();
                            ((ProgressBar)findViewById(R.id.progressBar2)).setVisibility(View.INVISIBLE);
                        }
                    });
                    request.setRetryPolicy(new DefaultRetryPolicy(
                            30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(request);
                    ((ProgressBar)findViewById(R.id.progressBar2)).setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this,"введите имя, пожалуйсто)",Toast.LENGTH_LONG).show();
            }
        }
    }
}
