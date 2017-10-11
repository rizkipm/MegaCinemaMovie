package imastudio.rizki.com.cinemamovie.Fragment;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import imastudio.rizki.com.cinemamovie.BuildConfig;
import imastudio.rizki.com.cinemamovie.Network.SyncDataCinemaMovie;
import imastudio.rizki.com.cinemamovie.R;
import imastudio.rizki.com.cinemamovie.activity.DetailCinemaMovieActivity;
import imastudio.rizki.com.cinemamovie.activity.DetailFavActivity;
import imastudio.rizki.com.cinemamovie.adapter.CinemaMovieListAdapter;
import imastudio.rizki.com.cinemamovie.adapter.CinemaMovieListModel;
import imastudio.rizki.com.cinemamovie.contentdata.DbDataSourceMovie;
import imastudio.rizki.com.cinemamovie.contentdata.MovieContract;
import imastudio.rizki.com.cinemamovie.contentdata.MovieDbHelper;


public class FavMovieFragment extends Fragment {

    private static final String LOG_TAG = CinemaMovieFragment.class.getSimpleName();
    private static final boolean DEBUG = false;
    public static final String EXTRA_MOVIE_DATA = "EXTRA_MOVIE_DATA";
    private ArrayAdapter<CinemaMovieListModel> mAdapter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    private MovieDbHelper dbHelper;
    private SQLiteDatabase database;
    private DbDataSourceMovie dataSource;
    GridView gridView;
    Cursor mCursor;


    int position = 0;

    private ArrayList<String> movie_id = new ArrayList<String>();
    private ArrayList<String> poster_image = new ArrayList<String>();
    private ArrayList<String> overview = new ArrayList<String>();
    private ArrayList<String> average_rating = new ArrayList<String>();
    private ArrayList<String> release_date = new ArrayList<String>();
    private ArrayList<String> id = new ArrayList<String>();
    private ArrayList<String> title = new ArrayList<String>();
    private ArrayList <String> back_poster = new ArrayList<>();
    String data ;



    public FavMovieFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null){

            position = savedInstanceState.getInt("scroll_position", 0);
            Toast.makeText(getActivity(), "Pos1"+ position, Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void onStart() {
//        SyncDataCinemaMovie syncDataCinemaMovie = new SyncDataCinemaMovie(getActivity(),mAdapter);
//        SharedPreferences preference= PreferenceManager.getDefaultSharedPreferences(getActivity());
////        syncDataCinemaMovie.execute(sortOrder);
//        super.onStart();
//
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.frag_favmovie, container, false);


        setHasOptionsMenu(true);

        dataSource = new DbDataSourceMovie(getActivity());
        try {
            dataSource.open();
        } catch (SQLException e) {
            database.close();

        }
        dbHelper = new MovieDbHelper(getActivity());
        database = dbHelper.getWritableDatabase();
        mCursor = database.rawQuery("SELECT * FROM "
                + MovieContract.MovieEntry.TABLE_NAME, null);
        mCursor.moveToFirst();

        if (mCursor.moveToFirst()) {
            do {
                movie_id.add(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                poster_image.add(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_IMAGE)));
                overview.add(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
                average_rating.add(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_AVERAGE_RATING)));
                release_date.add(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
                title.add(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
                back_poster.add(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACK_POSTER)));
                id.add(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID)));


            } while (mCursor.moveToNext());

            Cursor mWilayah = database.rawQuery("SELECT "+MovieContract.MovieEntry.COLUMN_TITLE+
                    " from tb_movie;", null);
            if (mWilayah.moveToFirst()){
                do {
                    data  = mWilayah.getString(0);

                }while (mWilayah.moveToNext());


                mWilayah.close();
            }

        }else{

        }

//        Toast.makeText(getActivity(), "Data kursor : " + mCursor, Toast.LENGTH_LONG).show();
        gridView = (GridView) rootview.findViewById(R.id.grid_cinema);


        DisplayAdapter adapter = new DisplayAdapter(getActivity(), movie_id, poster_image, overview, average_rating, release_date, id, title,back_poster);
        gridView.setAdapter(adapter);
        mCursor.close();


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







    public class DisplayAdapter extends BaseAdapter {
        private Context mContext;


        private ArrayList<String> movie_id;
        private ArrayList<String> poster_image;
        private ArrayList<String> overview;
        private ArrayList<String> average_rating;
        private ArrayList<String> release_date;
        private ArrayList<String> id;
        private ArrayList<String> title;
        private ArrayList<String> back_poster;




        public DisplayAdapter(Context c, ArrayList<String> movie_id,ArrayList<String> poster_image, ArrayList<String> overview,
                              ArrayList<String> average_rating, ArrayList<String> release_date, ArrayList<String> id,
                              ArrayList<String> title, ArrayList<String> back_poster) {
            this.mContext = c;

            this.movie_id = movie_id;
            this.poster_image = poster_image;
            this.overview = overview;
            this.average_rating = average_rating;
            this.release_date = release_date;
            this.id = id;
            this.title = title;
            this.back_poster = back_poster;

        }
        @Override
        public int getCount() {
            return movie_id.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View v, ViewGroup parent) {
            Holder mHolder;
            LayoutInflater layoutInflater;
            if (v == null) {
                layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = layoutInflater.inflate(R.layout.item_cinemamovie, null);
                mHolder = new Holder();




                mHolder.txtTitle = (TextView) v.findViewById(R.id.titleMovie);
                mHolder.posterView = (ImageView) v.findViewById(R.id.flavor_image);
                v.setTag(mHolder);
            } else {
                mHolder = (Holder) v.getTag();
            }
            mHolder.txtTitle.setText(title.get(position));
            Picasso.with(getContext()).load(poster_image.get(position)).placeholder(R.drawable.movie_place).fit().into(mHolder.posterView);
            mHolder.posterView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//
                    Intent intent = new Intent(getActivity(), DetailFavActivity.class);
                    intent.putExtra("title", title.get(position));
                    intent.putExtra("poster_image", poster_image.get(position));
                    intent.putExtra("overview", overview.get(position));
                    intent.putExtra("average_rating", average_rating.get(position));
                    intent.putExtra("release_date", release_date.get(position));
                    intent.putExtra("back_poster", back_poster.get(position));
                    intent.putExtra("movie_id", movie_id.get(position));
                    startActivity(intent);


                }
            });
            return v;
        }
        public class Holder {
            TextView txtTitle;

            ImageView posterView;
        }
    }









}
