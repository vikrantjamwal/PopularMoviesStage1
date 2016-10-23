package com.app.android.popularmoviesstage1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Activity context, ArrayList<Movie> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridItemView = convertView;
        if (gridItemView == null) {
            gridItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        Movie currentMovie = getItem(position);

        

        return gridItemView;
    }

}
