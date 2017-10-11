package imastudio.rizki.com.cinemamovie.contentdata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class MovieDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "db_megamovie";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate (SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "(" +
                        MovieContract.MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL," +
                        MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                        MovieContract.MovieEntry.COLUMN_POSTER_IMAGE + " TEXT NOT NULL," +
                        MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                        MovieContract.MovieEntry.COLUMN_AVERAGE_RATING + " REAL NOT NULL,"+
                        MovieContract.MovieEntry.COLUMN_BACK_POSTER + " REAL NOT NULL )";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }


    @Override
    public void onUpgrade (SQLiteDatabase sqLiteDatabase,int oldVersion, int newVersion){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
