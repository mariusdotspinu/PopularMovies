package app.com.example.marius.popularmovies.FetcherTasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import app.com.example.marius.popularmovies.Fragments.GridFragment;
import app.com.example.marius.popularmovies.Adapters.PosterAdapter;

/**
 * Created by Marius on 6/23/2016.
 */
public class FetchMovies extends AsyncTask<String, Void, String[]> {

    private Context context;
    private GridView moviesGrid;

    public FetchMovies(Context context, GridView moviesGrid) {
        this.context = context;
        this.moviesGrid = moviesGrid;
    }

    @Override
    protected String[] doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        GridFragment.movieTask = this;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String data = null;

        try {
            final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] +
                    "?api_key=88ac9738e1138283396eff8b210de62b";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon().build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Reading the input into String

            InputStream stream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (stream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(stream));

            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            data = buffer.toString();
        } catch (IOException e1) {
            Log.e("Error", "Error");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("File error", "Error closing file");
                }
            }
            try {
                return getDataFromJSON(data);
            } catch (JSONException e) {
                Log.e("Json error", "Eror json parsing");
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {

        if (result != null) {
            GridFragment.adapter = new PosterAdapter(context, result);
            moviesGrid.setAdapter(GridFragment.adapter);
        } else {
            Toast.makeText(context, "Error occured! Please check your internet connection",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private String[] getDataFromJSON(String data) throws JSONException {

        final String ID = "id";
        final String RELEASE_DATE = "release_date";
        final String OVERVIEW = "overview";
        final String POSTER_PATH = "poster_path";
        final String ORIGINAL_TITLE = "original_title";
        final String RATING = "vote_average";

        if (data == null) {
            return null;
        }

        JSONObject object = new JSONObject(data);

        JSONArray results = object.getJSONArray("results");


        String[] resultStrings = new String[results.length()];

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);

            String originalTitle = result.getString(ORIGINAL_TITLE);
            String overview = result.getString(OVERVIEW);
            String releaseDate = result.getString(RELEASE_DATE);
            String posterPath = result.getString(POSTER_PATH);
            String rating = result.getString(RATING);
            long id = result.getLong(ID);

            resultStrings[i] = originalTitle + "^" + overview + "^" + releaseDate + "^" + posterPath + "^"
                    + rating + "^" + id;


        }

        return resultStrings;

    }
}