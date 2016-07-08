package app.com.example.marius.popularmovies.FavoritesDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.GridView;

import app.com.example.marius.popularmovies.Adapters.PosterAdapter;

/**
 * Created by Marius on 7/4/2016.
 */
public class FavoritesDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "favorites.db";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + DatabaseModel.TABLE_NAME + " (" +
                    DatabaseModel.COLUMN_NAME_FAV_ID + " INTEGER PRIMARY KEY," +
                    DatabaseModel.COLUMN_NAME_FAV_SCORE + " TEXT," +
                    DatabaseModel.COLUMN_NAME_FAV_POSTER + " TEXT," +
                    DatabaseModel.COLUMN_NAME_FAV_RELEASE_DATE + " TEXT," +
                    DatabaseModel.COLUMN_NAME_FAV_OVERVIEW + " TEXT," +
                    DatabaseModel.COLUMN_NAME_FAV_TITLE + " TEXT )";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseModel.TABLE_NAME;

    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    //checking if the movie already exists or not in the db
    public boolean favExist(long id) {

        if (this.getReadableDatabase() != null) {
            Cursor c = this.getReadableDatabase().query(
                    DatabaseModel.TABLE_NAME,
                    null,
                    DatabaseModel.COLUMN_NAME_FAV_ID + " LIKE ?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    null
            );

            try {
                if (c.getCount() != 0) {
                    return true;
                }

            } finally {
                c.close();
            }
        }
        return false;
    }

    public Cursor fetchFavorites() {
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor c = db.query(
                DatabaseModel.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        return c;
    }
}
