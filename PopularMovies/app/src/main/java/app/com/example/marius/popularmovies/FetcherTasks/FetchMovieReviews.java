package app.com.example.marius.popularmovies.FetcherTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
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
import java.util.ArrayList;

import app.com.example.marius.popularmovies.Fragments.DetailFragment;
import app.com.example.marius.popularmovies.Adapters.ReviewAdapter;

/**
 * Created by Marius on 7/1/2016.
 */
public class FetchMovieReviews extends AsyncTask<Long, Void, ArrayList<ArrayList<String>>> {

    private ProgressDialog progressDialog;
    private Context context;
    private ListView reviews;

    public FetchMovieReviews(Context context, ListView reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    protected ArrayList<ArrayList<String>> doInBackground(Long... params) {

        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String data = null;

        try {
            final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] +
                    "/reviews?api_key=88ac9738e1138283396eff8b210de62b";

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
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = ProgressDialog.show(context, "Fetching Details", "Loading...");
        progressDialog.setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<String>> result) {

        progressDialog.dismiss();
        if (result != null) {
            DetailFragment.reviewAdapter = new ReviewAdapter(context, result);
            reviews.setAdapter(DetailFragment.reviewAdapter);
        } else {
            Toast.makeText(context, "Error occured! Please check your internet connection",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private ArrayList<ArrayList<String>> getDataFromJSON(String data) throws JSONException {


        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL = "url";

        if (data == null) {
            return null;
        }

        JSONObject object = new JSONObject(data);

        JSONArray results = object.getJSONArray("results");

        ArrayList<ArrayList<String>> resultStrings = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);

            String author = result.getString(AUTHOR);
            String content = result.getString(CONTENT);
            String url = result.getString(URL);

            ArrayList<String> thisResults = new ArrayList<>();

            thisResults.add(author);
            thisResults.add(content);
            thisResults.add(url);

            resultStrings.add(thisResults);
        }

        return resultStrings;

    }


}

