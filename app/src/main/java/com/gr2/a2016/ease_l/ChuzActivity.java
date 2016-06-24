package com.gr2.a2016.ease_l;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gr2.a2016.ease_l.classes.Image;
import com.gr2.a2016.ease_l.classes.NetworkAdreses;
import com.gr2.a2016.ease_l.classes.PIAdapter;
import com.gr2.a2016.ease_l.classes.Project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ChuzActivity extends Activity implements ListView.OnItemClickListener {
    RequestQueue requestQueue;
    ChuzActivity context;
    ArrayList<Project> projects;
    ArrayList<Image> images;
    ArrayList<String> idofprojects;
    ArrayList<String> idofimages;
    Intent root;
    ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuz);
        context = this;
        requestQueue = Volley.newRequestQueue(context);
        root = getIntent();
        ((Button)findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((Button)findViewById(R.id.add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionMode == null)
                    actionMode = startActionMode(callback);
                else
                    actionMode.finish();
            }
        });
        if (root.getBooleanExtra("ferst", true)) {
            root = null;
            JsonArrayRequest request = new JsonArrayRequest(NetworkAdreses.GET_ALL_PROJECTS, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                    projects = new ArrayList<>();
                    JSONArray cpi;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object = (JSONObject) jsonArray.get(i);
                            ArrayList<String> commentsid = new ArrayList<String>();
                            ArrayList<String> projectsid = new ArrayList<String>();
                            ArrayList<String> imagesid = new ArrayList<String>();
                            cpi = object.getJSONArray("Projects");
                            for (int q = 0; q < cpi.length(); q++) {
                                projectsid.add(cpi.getString(q));
                            }
                            cpi = object.getJSONArray("Images");
                            for (int q = 0; q < cpi.length(); q++) {
                                imagesid.add(cpi.getString(q));
                            }
                            cpi = object.getJSONArray("Comments");
                            for (int q = 0; q < cpi.length(); q++) {
                                commentsid.add(cpi.getString(q));
                            }
                            Project project = new Project(null, object.getString("Id"), null, object.getString("Version"), commentsid, projectsid, imagesid, object.getString("Name"));
                            projects.add(project);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    PIAdapter piAdapter = new PIAdapter(context, projects, new ArrayList<Image>());
                    ((ListView) findViewById(R.id.projects)).setAdapter(piAdapter);
                    ((ListView) findViewById(R.id.projects)).setOnItemClickListener(context);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                    Toast.makeText(context, "что-то пошло не так( " + volleyError.toString() + " )", Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(request);
        } else {
            ((TextView)findViewById(R.id.root)).setText(root.getStringExtra("Name"));
            idofimages = new ArrayList<>();
            idofprojects = new ArrayList<>();
            projects = new ArrayList<>();
            images = new ArrayList<>();
            try {
                JSONObject extra = new JSONObject(root.getStringExtra("Idm"));
                JSONArray cpi = extra.getJSONArray("Projects");
                for (int q = 0; q < cpi.length(); q++) {
                    idofprojects.add(cpi.getString(q));
                }
                cpi = extra.getJSONArray("Images");
                for (int q = 0; q < cpi.length(); q++) {
                    idofimages.add(cpi.getString(q));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            load();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            startActivity(root);
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        if (position < projects.size()) {
            JSONArray pridarray = new JSONArray();
            ArrayList<String> prids = projects.get(position).getProjects();
            for (int i = 0; i < prids.size(); i++) {
                pridarray.put(prids.get(i));
            }
            JSONArray imidarray = new JSONArray();
            prids = projects.get(position).getImages();
            for (int i = 0; i < prids.size(); i++) {
                imidarray.put(prids.get(i));
            }
            JSONObject extra = new JSONObject();
            try {
                extra.put("Projects", pridarray);
                extra.put("Images", imidarray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            intent = new Intent(context, ChuzActivity.class);
            intent.putExtra("Idm", extra.toString());
            intent.putExtra("Name",projects.get(position).getName());
            intent.putExtra("Id",projects.get(position).getId());
            intent.putExtra("ferst", false);
        } else {
            intent = new Intent(context,BaseActivity.class);
            intent.putExtra("Id",images.get(position-projects.size()).getId());
        }
        startActivity(intent);
    }

    private void load() {
        if (idofprojects.size() > 0) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, NetworkAdreses.GET_PROJECT_BY_ID + idofprojects.get(0), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject object) {
                    idofprojects.remove(0);
                    JSONArray cpi;
                    try {
                        ArrayList<String> commentsid = new ArrayList<String>();
                        ArrayList<String> projectsid = new ArrayList<String>();
                        ArrayList<String> imagesid = new ArrayList<String>();
                        cpi = object.getJSONArray("Projects");
                        for (int q = 0; q < cpi.length(); q++) {
                            projectsid.add(cpi.getString(q));
                        }
                        cpi = object.getJSONArray("Images");
                        for (int q = 0; q < cpi.length(); q++) {
                            imagesid.add(cpi.getString(q));
                        }
                        cpi = object.getJSONArray("Comments");
                        for (int q = 0; q < cpi.length(); q++) {
                            commentsid.add(cpi.getString(q));
                        }
                        Project project = new Project(null, object.getString("Id"), null, object.getString("Version"), commentsid, projectsid, imagesid, object.getString("Name"));
                        projects.add(project);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    load();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(context, "что-то пошло не так( " + volleyError.toString() + " )", Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(request);
        } else {
            if (idofimages.size() > 0) {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, NetworkAdreses.GET_IMAGE_BY_ID + idofimages.get(0), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject object) {
                        idofimages.remove(0);
                        JSONArray cpi = null;
                        try {
                            cpi = object.getJSONArray("Comments");
                            ArrayList<String> commentsid = new ArrayList<String>();
                            for (int q = 0; q < cpi.length(); q++) {
                                commentsid.add(cpi.getString(q));
                            }
                            Image imager = new Image(commentsid,object.getString("Url"),null,object.getString("Id"),null,object.getString("Version"),object.getString("Name"));
                            images.add(imager);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        load();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, "что-то пошло не так( " + volleyError.toString() + " )", Toast.LENGTH_LONG).show();
                    }
                });
                requestQueue.add(request);
            } else {
                findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                PIAdapter piAdapter = new PIAdapter(context, projects, images);
                ((ListView) findViewById(R.id.projects)).setAdapter(piAdapter);
                ((ListView) findViewById(R.id.projects)).setOnItemClickListener(context);
            }
        }
    }
    private ActionMode.Callback callback = new ActionMode.Callback() {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.projectorimg, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Intent intent;
            if(item.getTitle().equals("проект")){
                intent = new Intent(context,CreateProject.class);
                if(root == null){
                    intent.putExtra("Id"," ");
                } else {
                    intent.putExtra("Id",root.getStringExtra("Id"));
                }
            }else {
                intent = new Intent(context,CreateImage.class);
                if(root == null){
                    intent.putExtra("Id"," ");
                } else {
                    intent.putExtra("Id",root.getStringExtra("Id"));
                }
            }
            startActivityForResult(intent,1);
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }

    };

}
