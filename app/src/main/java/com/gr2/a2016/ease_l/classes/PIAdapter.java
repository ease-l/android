package com.gr2.a2016.ease_l.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gr2.a2016.ease_l.R;

import java.util.ArrayList;

/**
 * Created by Usre on 19.06.2016.
 */
public class PIAdapter extends BaseAdapter {
    public static final int IMAGE = 1;
    public static final int PROJECT = 0;

    private Context context;
    private ArrayList<Project> projects;
    private ArrayList<Image> images;
    private LayoutInflater lInflater;

    public PIAdapter(Context context, ArrayList<Project> projects, ArrayList<Image> images) {
        this.context = context;
        this.projects = projects;
        this.images = images;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return projects.size() + images.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= projects.size())
            return images.get(position - projects.size());
        else
            return projects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (position >= projects.size()) {
                view = lInflater.inflate(R.layout.i_adapter_item, parent, false);
                ((TextView) view.findViewById(R.id.p_i_name)).setText(images.get(position-projects.size()).getName());
        } else{
            view = lInflater.inflate(R.layout.p_adapter_item, parent, false);
            ((TextView) view.findViewById(R.id.p_i_name)).setText(projects.get(position).getName());
        }
        return view;
    }
}
