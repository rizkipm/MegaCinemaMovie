package imastudio.rizki.com.cinemamovie.Fragment;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import imastudio.rizki.com.cinemamovie.BuildConfig;
import imastudio.rizki.com.cinemamovie.Network.SyncDataCinemaMovie;
import imastudio.rizki.com.cinemamovie.R;
import imastudio.rizki.com.cinemamovie.activity.DetailCinemaMovieActivity;
import imastudio.rizki.com.cinemamovie.activity.FavCinemaActivity;
import imastudio.rizki.com.cinemamovie.adapter.CinemaMovieListModel;
import imastudio.rizki.com.cinemamovie.adapter.CinemaMovieListAdapter;



public class CinemaMovieFragment extends Fragment {

    private static final String LOG_TAG = CinemaMovieFragment.class.getSimpleName();
    private static final boolean DEBUG = false;
    public static final String EXTRA_MOVIE_DATA = "EXTRA_MOVIE_DATA";
    private ArrayAdapter<CinemaMovieListModel> mAdapter;
    @BindView(R.id.toolbar) Toolbar toolbar;
     GridView gridView;
    int position = 0;






    public CinemaMovieFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

//        int a = savedInstanceState.getInt("scroll_position");
//        Toast.makeText(getActivity(), "Pos1"+ a, Toast.LENGTH_SHORT).show();

        if (savedInstanceState != null){

            position = savedInstanceState.getInt("scroll_position", 0);
//            Toast.makeText(getActivity(), "Pos1"+ position, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {

        SyncDataCinemaMovie syncDataCinemaMovie = new SyncDataCinemaMovie(mAdapter);
        SharedPreferences preference= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = preference.getString(getString(R.string.pref_sort_key),
                getResources().getString(R.string.pref_order_pop));


        syncDataCinemaMovie.execute(sortOrder);
        super.onStart();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_cinemamovie, container, false);


        setHasOptionsMenu(true);


        mAdapter = new CinemaMovieListAdapter(getActivity(), R.layout.item_cinemamovie);

        gridView = (GridView) rootview.findViewById(R.id.grid_cinema);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CinemaMovieListModel movie = mAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailCinemaMovieActivity.class);
                intent.putExtra(CinemaMovieFragment.EXTRA_MOVIE_DATA, movie);
                startActivity(intent);
            }
        });



        return rootview;

    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        SyncDataCinemaMovie syncDataCinemaMovie = new SyncDataCinemaMovie(mAdapter);
//        SharedPreferences preference= PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String sortOrder = preference.getString(getString(R.string.pref_sort_key),
//                getResources().getString(R.string.pref_order_pop));

        position = gridView.getFirstVisiblePosition();

//        Toast.makeText(getActivity(), "Pos1save"+ position, Toast.LENGTH_SHORT).show();
        outState.putInt("scroll_position", position);

//        syncDataCinemaMovie.execute(sortOrder);
    }



    public class SyncDataCinemaMovie extends AsyncTask<String, Void, List<CinemaMovieListModel>> {
        private final String LOG_TAG = imastudio.rizki.com.cinemamovie.Network.SyncDataCinemaMovie.class.getSimpleName();
        private ArrayAdapter<CinemaMovieListModel> mAdapter;
//        public static Context mcontext;
        private CinemaMovieListModel mMovie;
//    private  int position ;

        public SyncDataCinemaMovie( ArrayAdapter<CinemaMovieListModel> MovieAdapter){
//            mcontext = context;
            mAdapter = MovieAdapter;
//        position = position;
        }





        public List<CinemaMovieListModel> getMoviesDataFromJson(String moviesJsonStr)
                throws JSONException {


            final String MOVIE_ID = "id";
            final String OWN_RESULT = "results";
            final String MOVIE_POSTER = "poster_path";
            final String BACK_POSTER = "backdrop_path";
            final String MOVIE_TITLE = "title";
            final String MOVIE_VOTE = "vote_average";
            final String MOVIE_SYNOPSIS = "overview";
            final String MOVIE_RELEASE_DATE = "release_date";

            List<CinemaMovieListModel> moviesdata = new ArrayList<>();


            JSONObject movieJson = new JSONObject(moviesJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWN_RESULT);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());
            String imageURl;
            String posterURl;


            for (int i = 0; i < movieArray.length(); i++) {


                JSONObject movies = movieArray.getJSONObject(i);

                String poster = movies.getString(MOVIE_POSTER);
                String backposter = movies.getString(BACK_POSTER);
                String title = movies.getString(MOVIE_TITLE);
                String release_date = movies.getString(MOVIE_RELEASE_DATE);
                String synopsis = movies.getString(MOVIE_SYNOPSIS);
                String rating = movies.getString(MOVIE_VOTE);
                int id = movies.getInt(MOVIE_ID);


                imageURl = "https://image.tmdb.org/t/p/w185" + poster;
                posterURl = "https://image.tmdb.org/t/p/w500" + backposter;


                CinemaMovieListModel movieList = new CinemaMovieListModel(imageURl, title, release_date, synopsis, rating, id,posterURl);
                moviesdata.add(movieList);


            }
            return moviesdata;
        }



        @Override
        protected List<CinemaMovieListModel> doInBackground(String... params) {
            String sortQuery = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            String movieJsonStr = null;

            int numMov = 0;
            try {

                switch(sortQuery)
                {   case "0":   sortQuery="popular";
                    break;

                    case "1":   sortQuery="top_rated";
                        break;


                }


                final String MOVIE_BASE_URL =
                        "https://api.themoviedb.org/3/movie";


                final String APPID_PARAM = "api_key";
                final String LANGUAGE_PARAM = "language";


                Uri builtUri = Uri.parse(MOVIE_BASE_URL)
                        .buildUpon()
                        .appendPath(sortQuery)
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                        .build();


                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }
                movieJsonStr = buffer.toString();

            } catch (IOException e) {

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }try {
                return getMoviesDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<CinemaMovieListModel> movies) {
            if (movies != null) {
                mAdapter.clear();
                mAdapter.addAll(movies);
                gridView.smoothScrollToPosition(position);
//                for(CinemaMovieListModel movieForecastStr : movies) {
//                    mAdapter.add(movieForecastStr);
//
//                }
            }
        }

    }












}
