package com.gr2.a2016.ease_l;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.InputType;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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




public class ChuzActivity extends Activity implements ListView.OnItemClickListener, ListView.OnItemLongClickListener {
    RequestQueue requestQueue;
    ChuzActivity context;
    ArrayList<Project> projects;
    ArrayList<Image> images;
    ArrayList<String> idofprojects;
    ArrayList<String> idofimages;
    Intent root;
    ActionMode actionMode;
    int clicknam;
    int positionDialog;
    AlertDialog.Builder alert;
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuz);
        context = this;
        alert = new AlertDialog.Builder(this);
        requestQueue = Volley.newRequestQueue(context);
        root = getIntent();
        ((Button) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((Button) findViewById(R.id.add)).setOnClickListener(new View.OnClickListener() {
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
                    ((ListView) findViewById(R.id.projects)).setOnItemLongClickListener(context);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                    Toast.makeText(context, "что-то пошло не так( " + volleyError.toString() + " )", Toast.LENGTH_LONG).show();
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);
        } else {
            ((TextView) findViewById(R.id.root)).setText(root.getStringExtra("Name"));
            idofimages = new ArrayList<>();
            idofprojects = new ArrayList<>();
            projects = new ArrayList<>();
            images = new ArrayList<>();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, NetworkAdreses.GET_PROJECT_BY_ID + root.getStringExtra("Id"), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject object) {
                    try {
                        JSONArray cpi = object.getJSONArray("Projects");
                        for (int q = 0; q < cpi.length(); q++) {
                            idofprojects.add(cpi.getString(q));
                        }
                        cpi = object.getJSONArray("Images");
                        for (int q = 0; q < cpi.length(); q++) {
                            idofimages.add(cpi.getString(q));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    load();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                    Toast.makeText(context, "что-то пошло не так( " + volleyError.toString() + " )", Toast.LENGTH_LONG).show();
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (root == null) {
                root = new Intent(context, ChuzActivity.class);
                startActivity(root);
                finish();
            } else {

                JSONObject extra = new JSONObject();
                Intent intent = new Intent(context, ChuzActivity.class);
                intent.putExtra("Id", root.getStringExtra("Id"));
                intent.putExtra("Name", root.getStringExtra("Name"));
                startActivity(intent);
                finish();

            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        if (position < projects.size()) {
            intent = new Intent(context, ChuzActivity.class);
            intent.putExtra("Name", projects.get(position).getName());
            intent.putExtra("Id", projects.get(position).getId());
            intent.putExtra("ferst", false);
            startActivity(intent);
        } else {
            positionDialog = position;
            alert.setTitle("Image version");
            message = new EditText(ChuzActivity.this);
            message.setHint("Enter version");
            message.setInputType(InputType.TYPE_CLASS_NUMBER);
            alert.setView(message);
            alert.setPositiveButton("OK", myClickListener);
            alert.setNeutralButton("Last version", myClickListener);
            alert.setNegativeButton("Cancel", myClickListener);
            alert.show();
        }
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    Intent intent = new Intent(context, BaseActivity.class);
                    intent.putExtra("Id", images.get(positionDialog - projects.size()).getId());
                    intent.putExtra("Version", message.getText().toString());
                    startActivity(intent);
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    Intent intent2 = new Intent(context, BaseActivity.class);
                    intent2.putExtra("Id", images.get(positionDialog - projects.size()).getId());
                    startActivity(intent2);
                    break;
            }
        }
    };

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
            request.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                            Image imager = new Image(commentsid, object.getString("Url"), null, object.getString("Id"), null, object.getString("Version"), object.getString("Name"));
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
                request.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(request);
            } else {
                findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                PIAdapter piAdapter = new PIAdapter(context, projects, images);
                ((ListView) findViewById(R.id.projects)).setAdapter(piAdapter);
                ((ListView) findViewById(R.id.projects)).setOnItemClickListener(context);
                ((ListView) findViewById(R.id.projects)).setOnItemLongClickListener(this);
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
            if (item.getTitle().equals("проект")) {
                intent = new Intent(context, CreateProject.class);
                if (root == null) {
                    intent.putExtra("Id", " ");
                } else {
                    intent.putExtra("Id", root.getStringExtra("Id"));
                }
            } else {
                intent = new Intent(context, PostImage.class);
                if (root == null) {
                    Toast.makeText(context,"Щазъ!",Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    intent.putExtra("Id", root.getStringExtra("Id"));
                }
            }
            startActivityForResult(intent, 1);
            actionMode.finish();
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }

    };
    private ActionMode.Callback chang = new ActionMode.Callback() {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.change, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getTitle().equals("изменить")) {
                if (clicknam < projects.size()) {

                } else {
                    Intent intent = new Intent(context,PostImage.class);
                    intent.putExtra("Image_id",images.get(clicknam-projects.size()).getId());
                    startActivityForResult(intent, 1);
                    actionMode.finish();
                    return false;
                }
            } else {
                Toast.makeText(context,clicknam,Toast.LENGTH_LONG).show();
                if (clicknam < projects.size()) {
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, NetworkAdreses.GET_PROJECT_BY_ID + projects.get(clicknam), null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            Toast.makeText(context, "успех!", Toast.LENGTH_LONG).show();
                            context.onActivityResult(1, RESULT_OK, null);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                            Toast.makeText(context, "что-то пошло не так( " + volleyError.toString() + " )", Toast.LENGTH_LONG).show();
                        }
                    });
                    request.setRetryPolicy(new DefaultRetryPolicy(
                            30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(request);
                } else {
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, NetworkAdreses.GET_IMAGE_BY_ID + images.get(clicknam - projects.size()), null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            Toast.makeText(context, "успех!", Toast.LENGTH_LONG).show();
                            context.onActivityResult(1, RESULT_OK, null);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                            Toast.makeText(context, "что-то пошло не так" +
                                    "( " + volleyError.toString() + " )", Toast.LENGTH_LONG).show();
                        }
                    });
                    request.setRetryPolicy(new DefaultRetryPolicy(
                            30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(request);
                }
            }
            actionMode.finish();
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }

    };

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        clicknam = position;
        if (actionMode != null)
            actionMode.finish();
        actionMode = startActionMode(chang);
        return false;
    }
}
