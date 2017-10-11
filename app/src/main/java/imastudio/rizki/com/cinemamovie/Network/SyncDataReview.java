package imastudio.rizki.com.cinemamovie.Network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import imastudio.rizki.com.cinemamovie.BuildConfig;
import imastudio.rizki.com.cinemamovie.helper.Extras;
import imastudio.rizki.com.cinemamovie.helper.MovieList;
import imastudio.rizki.com.cinemamovie.helper.Review;

/**
 * Created by user on 2/23/2017.
 */


public class SyncDataReview extends AsyncTask<Integer,Void, Extras> {


    List<MovieList> movieItems;
    private Event event;
    private final Context mContext;
    private ArrayAdapter<Extras> adapter;
    private final String LOG_TAG = SyncDataReview.class.getSimpleName();

    public SyncDataReview(Context context, Event event) {
        super();
        this.event=event;
        this.mContext=context;
    }

    private Extras getExtraDataFromJson(String movieJsonStr)
            throws JSONException {

        final String MOVIE_ID = "id";
        final String REVIEW_RESULTS = "results";
        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray moviesReviewsArray = movieJson.getJSONArray(REVIEW_RESULTS);
        Review[] reviews = new Review[moviesReviewsArray.length()];
        for (int i = 0; i < moviesReviewsArray.length(); i++) {
            JSONObject review = moviesReviewsArray.getJSONObject(i);
            reviews[i] = new Review(review.getString("author"), review.getString("content"));
        }
        Extras extras = new Extras(reviews);
        return extras;
    }

    @Override
    protected Extras doInBackground(Integer... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonStr = null;


        try {

            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/";
            final String MOVIE_REVIEW_URL=MOVIE_BASE_URL+"reviews"+"?";
            final String API_PARAM = "api_key";
            Uri builtUri = Uri.parse(MOVIE_REVIEW_URL).buildUpon()
                    .appendQueryParameter(API_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Built uri " + builtUri.toString());


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {

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
            Log.e(LOG_TAG, "Error ", e);

            return null;

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error in closing stream", e);
                }
            }
        }

        try {
            Log.v(LOG_TAG, movieJsonStr);
            return getExtraDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Extras extras) {
        if (extras != null) {
            super.onPostExecute(extras);
            event.onReviewsFetch(extras);
        }
    }


    public interface Event {
        void onReviewsFetch(Extras extras);
    }
}