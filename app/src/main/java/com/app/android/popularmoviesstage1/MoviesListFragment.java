package com.app.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MoviesListFragment extends Fragment {

    private static final String LOG_TAG = MoviesListFragment.class.getSimpleName();
    private static final String MOVIES_KEY = "movies_key";

    GridView mMoviesGridView;
    MovieAdapter mMovieAdapter;
    ArrayList<Movie> mMovies = new ArrayList<>();
    int mCurrentPage = 1;

    public MoviesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies_list, container, false);

        TextView emptyTextView = (TextView) rootView.findViewById(R.id.empty_textview);

        mMoviesGridView = (GridView) rootView.findViewById(R.id.grid_view);
        mMovieAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());
        mMoviesGridView.setAdapter(mMovieAdapter);

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (savedInstanceState != null) {
                mMovies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
                mMovieAdapter.addAll(mMovies);
                mCurrentPage = savedInstanceState.getInt("page_key");
            } else {
                showMovies();
            }
        } else {
            emptyTextView.setText(R.string.no_internet_text);
        }

        mMoviesGridView.setOnScrollListener(new EndlessScrollListener());

        mMoviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), MovieDetail.class);
                intent.putExtra("movie_object", mMovies.get(i));
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES_KEY, mMovies);
        outState.putInt("page_key", mCurrentPage);
    }

    private void showMovies() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String orderBy = sharedPref.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_default_value));
        FetchMovies fetchMovies = new FetchMovies();
        fetchMovies.execute(orderBy, "1");
    }

    public class EndlessScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 6;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    mCurrentPage++;
                }
            }
            if (!loading &&
                    (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String orderBy = sharedPref.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_default_value));
                new FetchMovies().execute(orderBy, String.valueOf(mCurrentPage + 1));
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }

    public class FetchMovies extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(strings[0])
                        .appendQueryParameter("api_key", BuildConfig.THE_MOVIEDB_KEY)
                        .appendQueryParameter("page", strings[1]);

                URL url = new URL(builder.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();
                return QueryUtils.parseMovies(forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                forecastJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            if (movies != null) {
                for (int i = 0; i < 20; i++) {
                    mMovies.add(movies.get(i));
                }
                mMovieAdapter.addAll(movies);
            }
        }
    }
}
