package imastudio.rizki.com.cinemamovie.Fragment;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import imastudio.rizki.com.cinemamovie.Network.SyncDataReview;
import imastudio.rizki.com.cinemamovie.Network.SyncDataTrailer;
import imastudio.rizki.com.cinemamovie.R;
import imastudio.rizki.com.cinemamovie.adapter.CinemaMovieListModel;
import imastudio.rizki.com.cinemamovie.contentdata.DbDataSourceMovie;
import imastudio.rizki.com.cinemamovie.contentdata.ModelMovie;
import imastudio.rizki.com.cinemamovie.contentdata.MovieContract;
import imastudio.rizki.com.cinemamovie.contentdata.MovieDbHelper;
import imastudio.rizki.com.cinemamovie.helper.Extras;


public class DetailFavCinema extends Fragment implements SyncDataTrailer.Event,SyncDataReview.Event,View.OnClickListener {
    View.OnClickListener monClickListener;
    private final String LOG_TAG = DetailFavCinema.class.getSimpleName();
    private static final boolean DEBUG = false;
    private Extras extras;

    private MovieDbHelper dbHelper;
    private SQLiteDatabase database;
    private DbDataSourceMovie dataSource;
    Cursor mCursor;

    String sTitle, poster_image,overview, average_rating, release_date, back_poster, movie_id ;

    private String[] allColums = {
            MovieContract.MovieEntry.COLUMN_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_POSTER_IMAGE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_AVERAGE_RATING,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_BACK_POSTER

    };



    public DetailFavCinema() {
        setHasOptionsMenu(true);
    }

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolBar;
    @BindView(R.id.title) TextView nCinemaMovieTitle;
    @BindView(R.id.poster) ImageView nCinemaMoviePoster;
    @BindView(R.id.backposter) ImageView nBackPoster;
    @BindView(R.id.release_date) TextView nReleaseDate;
    @BindView(R.id.rating) TextView nRatingAvg;
    @BindView(R.id.synopsis) TextView nSynopsis;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.share)
    FloatingActionButton favshare;
    @BindView(R.id.fav) FloatingActionButton favsave;

    @BindView(R.id.trailers_header)     TextView txtViewTrailersHeader;
    @BindView(R.id.trailers_container)
    HorizontalScrollView scrollViewTrailers;
    @BindView(R.id.trailers)            ViewGroup viewTrailers;
    @BindView(R.id.reviews_header) TextView textViewReviewHeader;
    @BindView(R.id.reviews) ViewGroup viewReviews;

//    FloatingActionButton favshare;
//    @BindView(R.id.fav) FloatingActionButton favsave;



    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_detailcinemamovie, container, false);

//        unbinder = ButterKnife.bind(this, rootView);

//        getComponent(getActivity()).inject(this);
        final CinemaMovieListModel movie = getActivity().getIntent().getParcelableExtra(CinemaMovieFragment.EXTRA_MOVIE_DATA);


        sTitle  = getActivity().getIntent().getExtras().getString("title");
        poster_image  = getActivity().getIntent().getExtras().getString("poster_image");
        overview  = getActivity().getIntent().getExtras().getString("overview");
        average_rating  = getActivity().getIntent().getExtras().getString("average_rating");
        release_date  = getActivity().getIntent().getExtras().getString("release_date");
        back_poster  = getActivity().getIntent().getExtras().getString("back_poster");
        movie_id  = getActivity().getIntent().getExtras().getString("movie_id");

        int id_mov = Integer.parseInt(movie_id);

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
        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)rootView.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(sTitle);
        ButterKnife.bind(this, rootView);

        nCinemaMovieTitle.setText(sTitle);
        Picasso.with(getContext())
                .load(poster_image)
                .placeholder(R.drawable.movie_place)
                .error(R.drawable.image_error2)
                .into(nCinemaMoviePoster);
        Picasso.with(getContext())
                .load(back_poster)
                .placeholder(R.drawable.movie_place)
                .error(R.drawable.image_error2)
                .into(nBackPoster);

        nReleaseDate.setText("Released :\n" + release_date);
        nRatingAvg.setText(String.valueOf("Rating :\n" + average_rating + "/10"));
        nSynopsis.setText("Synopsis :\n" + overview);
        SyncDataTrailer syncDataTrailer = new SyncDataTrailer(getContext(),this);
        SyncDataReview syncDataReview = new SyncDataReview(getActivity(),this);
        syncDataTrailer.execute(id_mov);
        syncDataReview.execute(id_mov);


        return rootView;
    }


    public void onTrailersFetch(Extras extras) {
        showTrailers(extras);
    }
    public void onReviewsFetch(Extras extras) {
        showReviews(extras);
    }

    private void showTrailers(Extras extras) {
        int numTrailers = extras.getTrailersNum();
        boolean hasTrailers = numTrailers>0;
//        favshare.setVisibility(hasTrailers ? View.VISIBLE : View.GONE);
        txtViewTrailersHeader.setVisibility(hasTrailers ? View.VISIBLE : View.GONE);
        scrollViewTrailers.setVisibility(hasTrailers ? View.VISIBLE : View.GONE);
        if (hasTrailers) {
            addTrailers(extras);
        }
    }

    private void addTrailers(Extras extras) {
        this.extras=extras;
        int numTrailers = extras.getTrailersNum();
        viewTrailers.removeAllViews();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Picasso picasso = Picasso.with(getActivity());
        for(int i=0; i<numTrailers;i++) {
            String TRAILER_THUMB_BASE_URL= getActivity().getString(R.string.trailer_thumb_base_url);
            String TRAILER_BASE_URL=getActivity().getString(R.string.trailer_base_url);
            String trailer_key=extras.getTrailerAtIndex((int) i).getSource();
            String trailer_url= TRAILER_BASE_URL + trailer_key;
            ViewGroup thumbContainer = (ViewGroup) inflater.inflate(R.layout.video, viewTrailers,
                    false);
            ImageView thumbView = (ImageView) thumbContainer.findViewById(R.id.video_thumb);
            thumbView.setTag(trailer_url);
            thumbView.setOnClickListener(this);

            picasso
                    .load(TRAILER_THUMB_BASE_URL + trailer_key + "/0.jpg")
                    .resizeDimen(R.dimen.video_width, R.dimen.video_height)
                    .placeholder(R.drawable.square_placeholder)
                    .centerCrop()
                    .into(thumbView);
            viewTrailers.addView(thumbContainer);
            favshare.setOnClickListener(this);
            favsave.setOnClickListener(this);
        }
    }
    private void showReviews(Extras extras){
        int numReviews = extras.getReviewsNum();
        boolean hasReviews = numReviews>0;
        textViewReviewHeader.setVisibility(hasReviews ? View.VISIBLE : View.GONE);
        viewReviews.setVisibility(hasReviews ? View.VISIBLE : View.GONE);
        if (hasReviews) {
            addReviews(extras);
        }
    }
    private void addReviews(final Extras extras) {
        int numReviews = extras.getReviewsNum();
        viewReviews.removeAllViews();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        for (int i = 0; i < numReviews; i++) {
            ViewGroup reviewContainer = (ViewGroup) inflater.inflate(R.layout.review_card, viewReviews,
                    false);
            TextView reviewAuthor = (TextView) reviewContainer.findViewById(R.id.review_author);
            TextView reviewContent = (TextView) reviewContainer.findViewById(R.id.review_content);
            reviewAuthor.setText(extras.getReviewAtIndex(i).getAuthor());
            reviewContent.setText(extras.getReviewAtIndex(i).getBody().replace("\n\n", " ").replace("\n", " "));
            reviewContainer.setOnClickListener(this);
            reviewContainer.setTag(extras.getReviewAtIndex(i));
            viewReviews.addView(reviewContainer);

        }
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.video_thumb){
            String videoUrl = (String) view.getTag();
            try{
                Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                startActivity(playVideoIntent);
            }catch (ActivityNotFoundException ex){
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                startActivity(intent);
            }
        }
        else if(view.getId()==R.id.share){
            CinemaMovieListModel movie = getActivity().getIntent().getParcelableExtra(CinemaMovieFragment.EXTRA_MOVIE_DATA);
            String trailer_key = extras.getTrailerAtIndex((int)0).getSource();
            String TRAILER_BASE_URL=getActivity().getString(R.string.trailer_base_url);
            String SHARE_TRAILER_TEXT=getActivity().getString(R.string.trailer_share_text);
            String APP_HASHTAG=getActivity().getString(R.string.app_hashtag);
            String trailer_url= TRAILER_BASE_URL+trailer_key;
            String movieName = movie.getTitle();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, SHARE_TRAILER_TEXT + " " + movieName + " | " + trailer_url + " "+APP_HASHTAG);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_trailer)));
        }
        else if (view.getId()==R.id.fav){
            CinemaMovieListModel mMovie = getActivity().getIntent().getParcelableExtra(CinemaMovieFragment.EXTRA_MOVIE_DATA);
            //Update favorite movie database accordingly
//            //If movie exists in fav db, delete it, Otherwise save it in db
//             UpdateFavdb favouriteMovieDBTask = new UpdateFavdb(getActivity(),mMovie, this);
//            favouriteMovieDBTask.execute();
//            int id_movie = mMovie.getId();
//            String m_idmovie = String.valueOf(id_movie);


            DbDataSourceMovie dataSource = new DbDataSourceMovie(getActivity());
            SQLiteDatabase database;
            MovieDbHelper dbHelper = new MovieDbHelper(getActivity());
            database = dbHelper.getWritableDatabase();
            try {
                dataSource.open();
            } catch (SQLException e) {
            }

            Cursor mFilter = database.query(MovieContract.MovieEntry.TABLE_NAME, allColums, "movie_id=?", new String[]{movie_id}, null, null, null);
            //Cursor mFilter = database.rawQuery("SELECT * from "+DBHelper.TABLE_NAME +"where "+DBHelper.COLUMN_ID_MENU + " = "+idMenu+";", null);
            if (mFilter.moveToFirst()) {
                do {

//                    dataSource.deleteFavMovie(mMovie.getId());
//                    Toast.makeText(getActivity(), "Movie not favourite", Toast.LENGTH_SHORT).show();
//
//                    favsave.setImageResource(R.drawable.ic_star_nfilled);
//                    getActivity().finish();
//                    startActivity(new Intent(getActivity(), MainActivity.class));

//                    dataSource.updateByIDMenu(Integer.parseInt(String.valueOf(idMenu)), jmlGabung, totalGabung);
//                    Toast.makeText(MenuKuActivity.this, "Barang berhasil ditambahkan", Toast.LENGTH_LONG).show();
//                    dialog.dismiss();
                }
                while (mFilter.moveToNext());
                mFilter.close();
            } else {
                ModelMovie transaksi = null;

                transaksi = dataSource.AddMovie(movie_id, poster_image, overview,
                        average_rating, release_date, release_date, back_poster);
////
                Toast.makeText(getActivity(), "Movie is favorite", Toast.LENGTH_LONG).show();
                favsave.setImageResource(R.drawable.ic_star);

            }
        }

    }

    @Override
    public void onStart() {
        if (DEBUG) Log.i(LOG_TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        if (DEBUG) Log.i(LOG_TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        if (DEBUG) Log.i(LOG_TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        if (DEBUG) Log.i(LOG_TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (DEBUG) Log.i(LOG_TAG, "onDestroy()");
        super.onDestroy();
    }



}

