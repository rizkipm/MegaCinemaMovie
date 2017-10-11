package imastudio.rizki.com.cinemamovie.contentdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

import imastudio.rizki.com.cinemamovie.adapter.CinemaMovieListModel;

/**
 * Created by MAC on 8/6/17.
 */

public class DbDataSourceMovie {
    private SQLiteDatabase database;

    private  MovieDbHelper dbHelper;



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

    public DbDataSourceMovie(Context context){
        dbHelper = new MovieDbHelper(context);
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close(){
        database.close();
        dbHelper.close();
    }


    public ModelMovie  AddMovie(String movie_id, String poster_image, String overview, String average_rating,
                                                String release_date, String title, String back_poster){
        ContentValues values = new ContentValues();



        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie_id);
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_IMAGE, poster_image);
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);
        values.put(MovieContract.MovieEntry.COLUMN_AVERAGE_RATING, average_rating);
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, release_date);
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
        values.put(MovieContract.MovieEntry.COLUMN_BACK_POSTER, back_poster);


        long insertId = database.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);

        Cursor cursor = database.query(MovieContract.MovieEntry.TABLE_NAME, allColums, MovieContract.MovieEntry.COLUMN_ID +
                "=" + insertId, null, null, null, null);

        cursor.moveToFirst();

        ModelMovie transaksi = cursorToTransaksi(cursor);
        cursor.close();
        return  transaksi;
    }

    private ModelMovie cursorToTransaksi(Cursor cursor){
        ModelMovie trans = new ModelMovie();

        Log.v("info", "The getLong" + cursor.getLong(0));
        Log.v("info", "The setLatLong " + cursor.getString(1) + "," + cursor.getString(2));

        trans.setId(cursor.getLong(0));
        trans.setMovie_id(cursor.getString(1));
        trans.setPoster_image(cursor.getString(2));
        trans.setOverview(cursor.getString(3));
        trans.setAverage_rating(cursor.getString(4));
        trans.setRelease_date(cursor.getString(5));
        trans.setTitle(cursor.getString(6));
        trans.setBack_poster(cursor.getString(7));


        return trans;
    }
    //mengambil semua data barang
    public ArrayList<ModelMovie> getAllTransaksi() {
        ArrayList<ModelMovie> daftarTransaksi = new ArrayList<ModelMovie>();

        // select all SQL query
        Cursor cursor = database.query(MovieContract.MovieEntry.TABLE_NAME,
                allColums, null, null, null, null, null);

        // pindah ke data paling pertama
        cursor.moveToFirst();
        // jika masih ada data, masukkan data barang ke
        // daftar barang
        while (!cursor.isAfterLast()) {
            ModelMovie transaksi = cursorToTransaksi(cursor);
            daftarTransaksi.add(transaksi);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return daftarTransaksi;
    }
    //ambil satu barang sesuai id
    public ModelMovie getTransaksi(long id)
    {
        ModelMovie transaksi = new ModelMovie(); //inisialisasi barang
        //select query
        Cursor cursor = database.query(MovieContract.MovieEntry.TABLE_NAME, allColums, "_id ="+id, null, null, null, null);
        //ambil data yang pertama
        cursor.moveToFirst();
        //masukkan data cursor ke objek barang
        transaksi = cursorToTransaksi(cursor);
        //tutup sambungan
        cursor.close();
        //return barang
        return transaksi;
    }

    public void deleteFavMovie(long id)
    {
        String strFilter = "_id=" + id;
        database.delete(MovieContract.MovieEntry.TABLE_NAME, strFilter, null);
    }
}


