package com.lab.mincheoulkim.mechef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class MainActivity extends Activity {

    private Menu menu;
    private boolean isListView;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    private MainListAdapter mAdapter;

    private Toolbar toolbar;

    public GoogleAds mGoogleAds;
    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpActionBar();

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

        mAdapter = new MainListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(onItemClickListener);

        isListView = true;

        mGoogleAds = new GoogleAds();

        //TODO Main Activity Remarketing
        mGoogleAds.RemarketingReport(getApplicationContext(), "activity_main", "activity_view");

        // TODO Handle Google analytics
        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        // Enable adevertising features
        if(mTracker != null)
            mTracker.enableAdvertisingIdCollection(true);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        if(mTracker == null)
            return;

        // TODO Send Google analytics ping for Mainactivity
        // Send ping to Google Analytics
        Log.i("MainActivity", "Setting screen name: " + "activity_main");

        mTracker.setScreenName("activity_main");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    MainListAdapter.OnItemClickListener onItemClickListener = new MainListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {

            /*
            //check if item click event is fired correctly
            Toast.makeText(MainActivity.this, "Clicked " + position, Toast.LENGTH_SHORT).show();

            // start new intent with EXTRA value
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_PARAM_ID, position);
            startActivity(intent);

            // transition starts here
            ImageView placeImage = (ImageView) v.findViewById(R.id.dishImage);
            LinearLayout placeNameHolder = (LinearLayout) v.findViewById(R.id.dishNameHolder);

            Pair<View, String> imagePair = Pair.create((View) placeImage, "tImage");
            Pair<View, String> holderPair = Pair.create((View) placeNameHolder, "tNameHolder");

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,imagePair, holderPair);
            ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
            */

            //check if item click event is fired correctly
            Toast.makeText(MainActivity.this, "Clicked " + position, Toast.LENGTH_SHORT).show();

            // Intent created with EXTRA value which will be transitioned to Detail Activity
            Intent transitionIntent = new Intent(MainActivity.this, DetailActivity.class);

            //TODO Add Main Activity's EXTRA before start new activity
            transitionIntent.putExtra(DetailActivity.EXTRA_PARAM_ID, position);

            // set up things for transitioning views
            ImageView placeImage = (ImageView) v.findViewById(R.id.dishImage);
            LinearLayout placeNameHolder = (LinearLayout) v.findViewById(R.id.dishNameHolder);

            View navigationBar = findViewById(android.R.id.navigationBarBackground);
            View statusBar = findViewById(android.R.id.statusBarBackground);

            Pair<View, String> imagePair = Pair.create((View) placeImage, "tImage");
            Pair<View, String> holderPair = Pair.create((View) placeNameHolder, "tNameHolder");

            Pair<View, String> navPair = Pair.create(navigationBar,
                    Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
            Pair<View, String> statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
            Pair<View, String> toolbarPair = Pair.create((View) toolbar, "tActionBar");

            // TODO Start Detail activity here
            // Start Activity
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,imagePair, holderPair, navPair, statusPair, toolbarPair);
            ActivityCompat.startActivity(MainActivity.this, transitionIntent, options.toBundle());
        }
    };


    private void setUpActionBar() {
        if (toolbar != null) {
            setActionBar(toolbar);
            //getActionBar().setDisplayHomeAsUpEnabled(false);
            //getActionBar().setDisplayShowTitleEnabled(true);
            //getActionBar().setElevation(7);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_toggle) {
            toggle();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        MenuItem item = menu.findItem(R.id.action_toggle);

        if (isListView) {
            mStaggeredLayoutManager.setSpanCount(2);
            item.setIcon(R.drawable.ic_action_list);
            item.setTitle("Show as list");
            isListView = false;
        } else {
            mStaggeredLayoutManager.setSpanCount(1);

            item.setIcon(R.drawable.ic_action_grid);
            item.setTitle("Show as grid");
            isListView = true;
        }
    }
}

