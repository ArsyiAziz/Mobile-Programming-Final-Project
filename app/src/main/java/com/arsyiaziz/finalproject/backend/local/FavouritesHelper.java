package com.arsyiaziz.finalproject.backend.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.arsyiaziz.finalproject.backend.models.ListedMovieModel;
import com.arsyiaziz.finalproject.backend.models.MovieModel;
import com.arsyiaziz.finalproject.misc.ImageSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FavouritesHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "favourite.db";
    private static final int VERSION = 1;
    private static final String TABLE_NAME = "movie";
    private static final String MOVIE_ID = "id";
    private static final String MOVIE_TITLE = "title";
    private static final String POSTER_PATH = "poster_path";

    public FavouritesHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY NOT NULL, %s TEXT, %s TEXT)",
                TABLE_NAME, MOVIE_ID, MOVIE_TITLE, POSTER_PATH
        );
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(MovieModel movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(MOVIE_ID, movie.getId());
        val.put(MOVIE_TITLE, movie.getTitle());
        val.put(POSTER_PATH, movie.getPosterPath(ImageSize.W200));
        db.insert(TABLE_NAME, null, val);
        db.close();
    }

    public boolean isFavourite(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format(Locale.getDefault(),
                "SELECT * FROM %s WHERE %s = %d",
                TABLE_NAME, MOVIE_ID, id
        );
        Cursor cursor = db.rawQuery(query, null);
        boolean isSuccess = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isSuccess;
    }

    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, String.format("%s = ?", MOVIE_ID), new String[]{String.valueOf(id)});
        db.close();
    }

    private List<ListedMovieModel> selectHelper(String queryExtra) {
        List<ListedMovieModel> favouritesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format(Locale.getDefault(), "SELECT * FROM %s %s;", TABLE_NAME, queryExtra);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int movieId = cursor.getInt(cursor.getColumnIndex(MOVIE_ID));
                String movieTitle = cursor.getString(cursor.getColumnIndex(MOVIE_TITLE));
                String posterPath = cursor.getString(cursor.getColumnIndex(POSTER_PATH));
                ListedMovieModel favourite = new ListedMovieModel(movieId, movieTitle, posterPath);
                favouritesList.add(favourite);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return favouritesList;
    }

    public List<ListedMovieModel> selectAll() {
        return selectHelper("");
    }

    public List<ListedMovieModel> select(String query) {
        String queryExtra = String.format("WHERE %s LIKE '%%%s%%'", MOVIE_TITLE, query);
        return selectHelper(queryExtra);
    }

}
