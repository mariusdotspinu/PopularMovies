package app.com.example.marius.popularmovies.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import app.com.example.marius.popularmovies.Adapters.PosterAdapter;
import app.com.example.marius.popularmovies.FavoritesDatabase.DatabaseModel;
import app.com.example.marius.popularmovies.FetcherTasks.FetchMovies;
import app.com.example.marius.popularmovies.R;

/**
 * Created by Marius on 7/6/2016.
 */
public class GridFragment extends Fragment {

    public static FetchMovies movieTask;
    public static PosterAdapter adapter;
    private OnItemSelectedListener listener;
    private GridView moviesGrid;
    private final String POPULAR = "popular";
    private final String TOP_RATED = "top_rated";
    private static final String PROVIDER_NAME = "com.popularmovies.provider.favorites";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/favorites");
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); //it won't show the menu otherwise!!
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF6600")));
        View view = inflater.inflate(R.layout.fragment_grid_layout, container, false);
        context = getActivity();
        listener = (OnItemSelectedListener) getActivity();

        moviesGrid = (GridView) view.findViewById(R.id.posterGrid);

        new FetchMovies(context, moviesGrid).execute(POPULAR);

        moviesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = adapter.getItem(position);
                listener.onMovieClickListener(data);
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("PopularMovies");

    }

    public interface OnItemSelectedListener {
        void onMovieClickListener(String data);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    public String[] fetchFavorites(Cursor c) {

        try {

            String[] data = new String[c.getCount()];

            int i = 0;
            while (c.moveToNext()) {
                String title = c.getString(c.getColumnIndex(DatabaseModel.COLUMN_NAME_FAV_TITLE));
                String overview = c.getString(c.getColumnIndex(DatabaseModel.COLUMN_NAME_FAV_OVERVIEW));
                String rel_date = c.getString(c.getColumnIndex(DatabaseModel.COLUMN_NAME_FAV_RELEASE_DATE));
                String poster = c.getString(c.getColumnIndex(DatabaseModel.COLUMN_NAME_FAV_POSTER));
                String rating = c.getString(c.getColumnIndex(DatabaseModel.COLUMN_NAME_FAV_SCORE));
                String id = c.getString(c.getColumnIndex(DatabaseModel.COLUMN_NAME_FAV_ID));

                data[i] = title + "^" + overview + "^" + rel_date + "^" + poster + "^" + rating + "^" +
                        id;

                i++;
            }
            return data;
        } finally {
            c.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sort_pop:
                new FetchMovies(context, moviesGrid).execute(POPULAR);
                return true;
            case R.id.sort_rating:
                new FetchMovies(context, moviesGrid).execute(TOP_RATED);
                return true;
            case R.id.favorites: //best practice would have been an CursorLoader<AsyncTask> but for our example we don't really need it here
                Cursor dataC = context.getContentResolver().query(CONTENT_URI, null, null, null, null); //convetion I used
                adapter = new PosterAdapter(context, fetchFavorites(dataC));
                moviesGrid.setAdapter(adapter);
                return true;
            case R.id.about:
                startAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startAboutDialog() {
        AlertDialog dialog = new AlertDialog.Builder(context).create();

        dialog.setTitle("About");
        dialog.setMessage("This app was built for learning purposes . \n " +
                "This product uses the TMDb API but is not endorsed or certified by TMDb.\n" +
                " Picasso library was also used.");
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
