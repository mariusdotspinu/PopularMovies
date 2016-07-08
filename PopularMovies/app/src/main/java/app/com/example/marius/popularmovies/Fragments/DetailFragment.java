package app.com.example.marius.popularmovies.Fragments;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import app.com.example.marius.popularmovies.Adapters.ReviewAdapter;
import app.com.example.marius.popularmovies.Adapters.TrailerAdapter;
import app.com.example.marius.popularmovies.FavoritesDatabase.DatabaseModel;
import app.com.example.marius.popularmovies.FetcherTasks.FetchMovieReviews;
import app.com.example.marius.popularmovies.FetcherTasks.FetchMovieTrailers;
import app.com.example.marius.popularmovies.R;

/**
 * Created by Marius on 7/6/2016.
 */
public class DetailFragment extends Fragment {

    private FetchMovieReviews reviewTask;
    private FetchMovieTrailers trailerTask;
    public static ReviewAdapter reviewAdapter;
    public static TrailerAdapter trailerAdapter;
    private final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    private String movieData;
    private TextView releaseDate, title, overview, score, reviewsTitle;
    private ImageView img, reviewsIcon, trailersIcon;
    private ListView reviews, trailers;
    private final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/w185/";
    private Context context;
    private Button markFavorite;


    private static final String PROVIDER_NAME = "com.popularmovies.provider.favorites";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/favorites");


    @Override
    public void onDestroy() {
        super.onDestroy();

        //we must cancel the tasks in case of orientation change
        if (reviewTask != null) {
            reviewTask.cancel(true);
            reviewTask = null;
        }

        if (trailerTask != null) {
            trailerTask.cancel(true);
            trailerTask = null;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.footer_section, container, false);
        context = getActivity();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF6600")));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Details");

        if (getArguments() != null && getArguments().getString("data") != null) {


            movieData = getArguments().getString("data");
            String[] intentData = movieData.split("\\^");


            reviews = (ListView) view.findViewById(R.id.reviews_list_view);
            View header = LayoutInflater.from(context).inflate(R.layout.fragment_detail_layout, reviews, false);
            reviews.addHeaderView(header);
            //never use a listview in a scrollview #reference

            markFavorite = (Button) view.findViewById(R.id.markFavorite);
            reviewTask = new FetchMovieReviews(context, reviews);
            reviewTask.execute(Long.parseLong(intentData[5]));

            //the activity details main xml data


            releaseDate = (TextView) view.findViewById(R.id.release_date);
            releaseDate.setTextColor(Color.parseColor("#0f7cf8"));
            String releasePholder = getString(R.string.release_date) + intentData[2];
            releaseDate.setText(releasePholder);

            overview = (TextView) view.findViewById(R.id.overview);
            String overviewPholder = getString(R.string.overview_plot) + intentData[1];
            overview.setText(overviewPholder);

            title = (TextView) view.findViewById(R.id.original_title);
            title.setTextColor(Color.parseColor("#f44a5c"));
            title.setText(intentData[0]);

            score = (TextView) view.findViewById(R.id.score);
            score.setTextColor(Color.parseColor("#6e0ff8"));
            String scorePholder = getString(R.string.user_score) + intentData[4];
            score.setText(scorePholder);

            img = (ImageView) view.findViewById(R.id.poster);

            Picasso.with(context)
                    .load(IMAGE_BASE_PATH + intentData[3])
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(img);

            trailers = (ListView) view.findViewById(R.id.trailers_list_view);

            trailersIcon = (ImageView) view.findViewById(R.id.trailerLogo);

            Picasso.with(context)
                    .load(R.drawable.trailer_icon)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(trailersIcon);

            trailerTask = new FetchMovieTrailers(context, trailers);
            trailerTask.execute(Long.parseLong(intentData[5]));


            reviewsTitle = (TextView) view.findViewById(R.id.reviews_title);
            reviewsTitle.setTextColor(Color.parseColor("#9e6c5a"));

            reviewsIcon = (ImageView) view.findViewById(R.id.review_icon);

            Picasso.with(context)
                    .load(R.drawable.review_icon)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(reviewsIcon);


            //start watching the trailer video
            trailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String key = trailerAdapter.getItemKey(position);
                    Intent playVideo = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + key));
                    startActivity(playVideo);
                }
            });


            markFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues values = new ContentValues();

                    String[] data = movieData.split("\\^");

                    values.put(DatabaseModel.COLUMN_NAME_FAV_ID, data[5]);
                    values.put(DatabaseModel.COLUMN_NAME_FAV_SCORE, data[4]);
                    values.put(DatabaseModel.COLUMN_NAME_FAV_POSTER, data[3]);
                    values.put(DatabaseModel.COLUMN_NAME_FAV_RELEASE_DATE, data[2]);
                    values.put(DatabaseModel.COLUMN_NAME_FAV_OVERVIEW, data[1]);
                    values.put(DatabaseModel.COLUMN_NAME_FAV_TITLE, data[0]);


                    Uri uri = context.getContentResolver().insert(CONTENT_URI, values);

                    if (uri != null) {
                        Toast.makeText(context, "Added to Favorites ! ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Already exists in favorites ! ", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

        return view;
    }

}
