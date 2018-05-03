package com.ibrahim.popularmovies_stage1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ibrahim.popularmovies_stage1.data.SharedPrefManager;
import com.ibrahim.popularmovies_stage1.model.Movies;
import com.ibrahim.popularmovies_stage1.utilities.NetworkUtils;
import com.ibrahim.popularmovies_stage1.utilities.OpenMoviesUtils;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

import static com.ibrahim.popularmovies_stage1.data.Contract.POPULAR_PART;
import static com.ibrahim.popularmovies_stage1.data.Contract.TOP_RATED_PART;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Movies";
    private static final String STATE_MOVIES = "state_movies";
    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private Button mRefresh;
    private ProgressBar mLoadingIndicator;
    private ArrayList<Movies> moviesArrayList;
    private MoviesAdapter mAdapter;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*make sure if  savedInstanceState not null after rotate or exit the application
         * snd it have the key that come from onSaveInstanceState with Bundle
         * */
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_MOVIES)) {
            //after check get the value of that key in  moviesArrayList
            moviesArrayList = savedInstanceState.getParcelableArrayList(STATE_MOVIES);
        }
        setContentView(R.layout.activity_main);

        moviesArrayList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recyclerview_movies);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        /*
         * GridLayoutManager get with two parameters context
         * & integer to make two horizontal raw every item in recyclerView
         */
        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        /*
         * The MoviesAdapter is responsible for linking our movies data with the Views that
         * will end up displaying our movies data.
         */
        mAdapter = new MoviesAdapter(this, moviesArrayList);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mAdapter);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading
         */
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mRefresh = findViewById(R.id.btn_refresh);

        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.updateMovies(null);
                ConnectivityManager cm =
                        (ConnectivityManager)MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
if(isConnected||isWiFi){
    loadMoviesData();

}else {
    Toast.makeText(MainActivity.this, getResources().getString(R.string.check_intenet),Toast.LENGTH_LONG).show();
}

            }
        });
        /* Once all of our views are setup, we can load the movies data. */
        loadMoviesData();

    }

    /**
     * background method to get the movies data in the background.
     */
    private void loadMoviesData() {
        showMoviesDataView();
        new FetchMoviesrTask(this).execute();
    }

    /**
     * Override method to put moviesArrayList and
     * key  STATE_MOVIES inside Bundle by ParcelableArrayList
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STATE_MOVIES, moviesArrayList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_last:
                //change title of menu  that show in  toolbar by sellected itme text
                setMenuName(TOP_RATED_PART);
                //save String==> top_rated   so will change the URL and therefore bring the json based on==> top_rated
                SharedPrefManager.getInstance(MainActivity.this).setPrefUrlSellected(TOP_RATED_PART);
                //recall method  loadMoviesData to get data from new url
                loadMoviesData();

                return true;
            case R.id.menu_popularity:
                //change title of menu  that show in  toolbar by sellected itme text
                setMenuName(POPULAR_PART);
                //save String==> popular   so will change the URL and therefore bring the json based on ==>popular
                SharedPrefManager.getInstance(MainActivity.this).setPrefUrlSellected(POPULAR_PART);
                //recall method  loadMoviesData to get data from new url
                loadMoviesData();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        try {
            getMenuInflater().inflate(R.menu.main_menu, menu);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "onCreateOptionsMenu: error: " + e.getMessage());
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void setMenuName(String set) {
        menu.findItem(R.id.selected).setTitle(set);
    }

    /**
     * This method will make the View for the movies data visible and
     * hide the error message.
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check Movies each view is currently visible or invisible.
     */
    private void showMoviesDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRefresh.setVisibility(View.INVISIBLE);
        /* Then, make sure the movies data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movies
     * View.
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check movies each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRefresh.setVisibility(View.VISIBLE);

    }


    //perform network requests by AsyncTask

    /**
     * @see <a href="https://stackoverflow.com/questions/44309241/warning-this-asynctask-class-should-be-static-or-leaks-might-occur">http://google.com</a>
     */
    private static class FetchMoviesrTask extends AsyncTask<String, Void, ArrayList<Movies>> {
        private final WeakReference<MainActivity> activityReference;

        // only retain a weak reference to the activity
        FetchMoviesrTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        //doInBackground method to perform  network requests
        @Override
        protected ArrayList<Movies> doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */

            /* url from methode NetworkUtils.buildUrl by parsing the selected sort of review Movie in path*/
            URL moviesRequestUrl = NetworkUtils.buildUrl(SharedPrefManager.getInstance(activityReference.get()).getPrefUrlSellected());

            try {
                /*get the value json data com from url
                  return value from  OpenMoviesUtils class
                   by parseing   json data  into it */
                return OpenMoviesUtils
                        .getMovies(NetworkUtils
                                .getResponseFromHttpUrl(moviesRequestUrl));

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movies> MoviesData) {
            // get a reference to the activity if it is still there
            /*{@link https://stackoverflow.com/questions/44309241/warning-this-asynctask-class-should-be-static-or-leaks-might-occur*/

            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            //after loading data Progress Bar will disappear
            activity.mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (MoviesData != null) {
                activity.showMoviesDataView();
                /*set the the of our moviesArrayList from the value that com from asyncTask ( onPostExecute  parameter
                 * to save it inside onSaveInstanceState
                 * */
                activity.moviesArrayList = MoviesData;
                /*ubdate the value of mAdapter by sending the value of arraylist inside it */
                activity.mAdapter.updateMovies(activity.moviesArrayList);
                activity.mAdapter.notifyDataSetChanged();
            } else {
                activity.showErrorMessage();
            }
        }
    }
}