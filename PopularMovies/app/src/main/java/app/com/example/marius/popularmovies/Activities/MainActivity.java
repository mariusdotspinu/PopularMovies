package app.com.example.marius.popularmovies.Activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import app.com.example.marius.popularmovies.FetcherTasks.FetchMovies;
import app.com.example.marius.popularmovies.Fragments.DetailFragment;
import app.com.example.marius.popularmovies.Fragments.GridFragment;
import app.com.example.marius.popularmovies.R;

public class MainActivity extends AppCompatActivity implements GridFragment.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check whether the Activity is using the layout verison with the fragment_container
        // FrameLayout and if so we must add the first fragment

        if (findViewById(R.id.fragment_container) != null) {

            // However if we are being restored from a previous state, then we don't
            // need to do anything and should return or we could end up with overlapping Fragments
            if (savedInstanceState != null) {
                return;
            }

            GridFragment gridFragment = new GridFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, gridFragment);
            ft.commit();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    public void onMovieClickListener(String data) {
        DetailFragment fragment = (DetailFragment) getFragmentManager()
                .findFragmentById(R.id.detailFragment);

        Bundle bundle = new Bundle();
        bundle.putString("data", data);

        if (fragment != null) {
            //we are in the two pane layout
            fragment.getArguments().clear();
            fragment.getArguments().putAll(bundle);
        } else {

            DetailFragment newFrag = new DetailFragment();
            newFrag.setArguments(bundle);
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            ft.replace(R.id.fragment_container, newFrag);
            ft.addToBackStack(null);
            ft.commit();

        }
    }
}
