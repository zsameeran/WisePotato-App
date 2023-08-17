package com.wisepotato.wp_app;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    private Activity activity;

    private  int images[];
    public List selectedPositions;

    private ArrayList<String> names;
    private ArrayList<String> IDS;

    public GridViewAdapter(ArrayList<String> names, ArrayList<String> ids, Activity activity) {
        this.names = names;
        this.IDS=ids;
        this.activity = activity;


        selectedPositions = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    public Object getInterestId(int position){ return IDS.get(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridItemView customView = (convertView == null) ? new GridItemView(activity) : (GridItemView) convertView;
        customView.display(names.get(position),selectedPositions.contains(position));

        return customView;
    }
}
