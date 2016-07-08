package app.com.example.marius.popularmovies.FavoritesDatabase;

/**
 * Created by Marius on 7/4/2016.
 */
public final class DatabaseModel {

    private DatabaseModel() {

    }

    public static final String TABLE_NAME = "favorites";
    public static final String COLUMN_NAME_FAV_ID = "fav_id";
    public static final String COLUMN_NAME_FAV_OVERVIEW = "fav_overview";
    public static final String COLUMN_NAME_FAV_POSTER = "fav_poster";
    public static final String COLUMN_NAME_FAV_RELEASE_DATE = "fav_date";
    public static final String COLUMN_NAME_FAV_SCORE = "fav_score";
    public static final String COLUMN_NAME_FAV_TITLE = "fav_title";
}
