package com.app.android.popularmoviesstage1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class QueryUtils {

    private QueryUtils(){
    }

    public static ArrayList<Movie> parseMovies(String movieJSON){
        ArrayList<Movie> movies = new ArrayList<>();

        JSONObject root;
        try {
            root = new JSONObject(movieJSON);

            JSONArray jsonArray = root.optJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String title = item.getString("original_title");
                String thumbnail = item.getString("poster_path");
                String overview = item.getString("overview");
                double rating = item.getDouble("vote_count");
                String date = item.getString("release_date");

                movies.add(new Movie(title, thumbnail, overview, rating, date));
            }

            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
