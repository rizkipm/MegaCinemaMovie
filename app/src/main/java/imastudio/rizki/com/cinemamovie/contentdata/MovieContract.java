package imastudio.rizki.com.cinemamovie.contentdata;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;



public class MovieContract {
    public static final String CONTENT_AUTHORITY = "imastudio.rizki.com.cinemamovie";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movies";



    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "tb_movie";

        public static final String COLUMN_ID = "_id";


        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER_IMAGE = "poster_image";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_AVERAGE_RATING = "average_rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACK_POSTER = "back_poster";

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }


}
