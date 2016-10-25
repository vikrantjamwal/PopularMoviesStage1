package com.app.android.popularmoviesstage1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Movie movie = getActivity().getIntent().getExtras().getParcelable("movie_object");

        TextView movieTitle = (TextView) rootView.findViewById(R.id.name_text);
        ImageView movieImage = (ImageView) rootView.findViewById(R.id.imageView);
        TextView summary = (TextView) rootView.findViewById(R.id.overview_text);
        TextView rating = (TextView) rootView.findViewById(R.id.rating_text);
        TextView date = (TextView) rootView.findViewById(R.id.date_text);

        movieTitle.setText(movie.getTitle());
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185" + movie.getThumbnail()).into(movieImage);
        summary.setText(movie.getOverview());
        rating.setText(String.valueOf(movie.getRating()));
        date.setText(movie.getDate());

        return rootView;
    }

}
