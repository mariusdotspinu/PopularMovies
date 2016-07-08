package app.com.example.marius.popularmovies.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import app.com.example.marius.popularmovies.FavoritesDatabase.DatabaseModel;
import app.com.example.marius.popularmovies.FavoritesDatabase.FavoritesDbHelper;


/**
 * Created by Marius on 7/4/2016.
 */
public class FavoritesProvider extends ContentProvider {
    private SQLiteDatabase db;
    private FavoritesDbHelper dbHelper;
    private static final String PROVIDER_NAME = "com.popularmovies.provider.favorites";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/favorites");

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new FavoritesDbHelper(context);

        db = dbHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return dbHelper.fetchFavorites();
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        if (!dbHelper.favExist(Long.parseLong((String) values.get(DatabaseModel.COLUMN_NAME_FAV_ID)))) {
            long rowId = db.insert(DatabaseModel.TABLE_NAME, "", values);

            //if record is added succesfully

            if (rowId > 0) {
                Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowId);
                return _uri;
            }
            throw new SQLException("Failed to add row");

        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
