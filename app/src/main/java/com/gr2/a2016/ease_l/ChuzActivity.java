package com.gr2.a2016.ease_l;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gr2.a2016.ease_l.classes.Image;
import com.gr2.a2016.ease_l.classes.NetworkAdreses;
import com.gr2.a2016.ease_l.classes.PIAdapter;
import com.gr2.a2016.ease_l.classes.Project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ChuzActivity extends Activity {
    RequestQueue requestQueue;
    Context context;
    ArrayList<Project> projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuz);
        context = this;
        requestQueue = Volley.newRequestQueue(context);
        Intent intent = getIntent();
        boolean ferst = false;
        try {
            JSONObject rootProject = new JSONObject(intent.getStringExtra("rootProgect"));
        } catch (JSONException e) {
        } catch (NullPointerException e) {
            ferst = true;
        }
        if (ferst){
            JsonArrayRequest request =  new JsonArrayRequest(NetworkAdreses.GET_ALL_PROJECTS, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                    projects = new ArrayList<>();
                    JSONArray cpi;
                    for(int i = 0;i<jsonArray.length();i++){
                        try {
                            JSONObject object = (JSONObject)jsonArray.get(i);
                            ArrayList<String> commentsid = new ArrayList<String>();
                            ArrayList<String> projectsid = new ArrayList<String>();
                            ArrayList<String> imagesid = new ArrayList<String>();
                            cpi = object.getJSONArray("Projects");
                            for (int q = 0; q<cpi.length();q++){
                                projectsid.add(cpi.getString(q));
                            }
                            cpi = object.getJSONArray("Images");
                            for (int q = 0; q<cpi.length();q++){
                                imagesid.add(cpi.getString(q));
                            }
                            cpi = object.getJSONArray("Comments");
                            for (int q = 0; q<cpi.length();q++){
                                commentsid.add(cpi.getString(q));
                            }
                            Project project = new Project(null,object.getString("Id"),null,object.getString("Version"),commentsid,projectsid,imagesid,object.getString("Name"));
                            projects.add(project);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    PIAdapter piAdapter = new PIAdapter(context,projects,new ArrayList<Image>());
                    ((ListView)findViewById(R.id.projects)).setAdapter(piAdapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                    Toast.makeText(context,"что-то пошло не так( "+volleyError.toString()+ " )",Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(request);
        }
    }
}
