package com.lab.mincheoulkim.mechef;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;


/**
 * Created by mincheoulkim on 11/10/15.
 */
public class DetailActivity extends Activity implements View.OnClickListener {

    public GoogleAds mGoogleAds;
    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;

    public static final String EXTRA_PARAM_ID = "place_id";

    public static final String NAV_BAR_VIEW_NAME = Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME;

    int mIndex = 0; // variable to hold index of which item (from main list) was selected

    private ListView mList;
    private ImageView mImageView;
    private TextView mTitle;
    private LinearLayout mTitleHolder;
    private Palette mPalette;
    private ImageButton mAddButton;
    private Animatable mAnimatable;
    private LinearLayout mRevealView;
    private EditText mEditTextTodo;
    private boolean isEditTextVisible;
    private InputMethodManager mInputManager;
    private Dish mDish;
    private ArrayList<String> mTodoList;
    private ArrayAdapter mToDoAdapter;
    int defaultColorForRipple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // TODO Handle Google analytics
        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        // Enable adevertising features
        if(mTracker != null)
            mTracker.enableAdvertisingIdCollection(true);

        // TODO handle deep link situation here
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        // need to check if this is selected from deep link or manual click
        if( data != null && action == "android.intent.action.VIEW") // from deep link
            mIndex = getDeeplinkId(data.getPath().toString());
        else                                                        // from user's selection
            mIndex = getIntent().getIntExtra(EXTRA_PARAM_ID, 0);

        mDish = DishData.dishList().get(mIndex);

        // TODO Send Adwords Converison and remarketing ping
        mGoogleAds = new GoogleAds();
        if(mGoogleAds != null) {
            // TODO Detail Page conversion to be added
            mGoogleAds.ConversionReport(getApplicationContext(), "fxSiCOvHrmEQ6IWsrwM");

            //TODO Remarketing report at detail activity
            mGoogleAds.RemarketingReport(getApplicationContext(), "activity_detail", "activity_view");
        }

        mList = (ListView) findViewById(R.id.list);
        mImageView = (ImageView) findViewById(R.id.dishImage);
        mTitle = (TextView) findViewById(R.id.textView);
        mTitleHolder = (LinearLayout) findViewById(R.id.dishNameHolder);
        mAddButton = (ImageButton) findViewById(R.id.btn_add);
        mRevealView = (LinearLayout) findViewById(R.id.llEditTextHolder);
        mEditTextTodo = (EditText) findViewById(R.id.etTodo);

        mAddButton.setImageResource(R.drawable.icn_morph_reverse);
        mAddButton.setOnClickListener(this);
        defaultColorForRipple = getResources().getColor(R.color.colorPrimaryDark);
        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mRevealView.setVisibility(View.INVISIBLE);
        isEditTextVisible = false;

        mTodoList = new ArrayList<>();
        mToDoAdapter = new ArrayAdapter(this, R.layout.row_todo, mTodoList);
        mList.setAdapter(mToDoAdapter);

        loadDish();
        windowTransition();
        getPhoto();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        if(mTracker == null)
            return;

        // TODO Send Google analytics ping for Detailactivity
        // Send ping to Google Analytics
        Log.i("DetailActivity", "Setting screen name: " + "activity_detail");

        mTracker.setScreenName("activity_detail" + mIndex);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void loadDish() {
        mTitle.setText(mDish.name);
        mImageView.setImageResource(mDish.getImageResourceId(this));
    }

    private void windowTransition() {
        getWindow().setEnterTransition(makeEnterTransition());
        getWindow().getEnterTransition().addListener(new TransitionAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                mAddButton.animate().alpha(1.0f);
                getWindow().getEnterTransition().removeListener(this);
            }
        });
    }

    public static Transition makeEnterTransition() {
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        return fade;
    }

    private void addToDo(String todo) {
        mTodoList.add(todo);
    }

    private void getPhoto() {
        Bitmap photo = BitmapFactory.decodeResource(getResources(), mDish.getImageResourceId(this));
        colorize(photo);
    }

    private void colorize(Bitmap photo) {
        mPalette = Palette.generate(photo);
        applyPalette();
    }

    private void applyPalette() {
        getWindow().setBackgroundDrawable(new ColorDrawable(mPalette.getDarkMutedColor(defaultColorForRipple)));
        mTitleHolder.setBackgroundColor(mPalette.getMutedColor(defaultColorForRipple));
        applyRippleColor(mPalette.getVibrantColor(defaultColorForRipple),
                mPalette.getDarkVibrantColor(defaultColorForRipple));
        mRevealView.setBackgroundColor(mPalette.getLightVibrantColor(defaultColorForRipple));
    }


    private void applyRippleColor(int bgColor, int tintColor) {
        colorRipple(mAddButton, bgColor, tintColor);
    }

    private void colorRipple(ImageButton id, int bgColor, int tintColor) {
        View buttonView = id;
        RippleDrawable ripple = (RippleDrawable) buttonView.getBackground();
        GradientDrawable rippleBackground = (GradientDrawable) ripple.getDrawable(0);
        rippleBackground.setColor(bgColor);
        ripple.setColor(ColorStateList.valueOf(tintColor));
    }

    private void revealEditText(LinearLayout view) {
        int cx = view.getRight() - 30;
        int cy = view.getBottom() - 60;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        view.setVisibility(View.VISIBLE);
        isEditTextVisible = true;
        anim.start();
    }

    private void hideEditText(final LinearLayout view) {
        int cx = view.getRight() - 30;
        int cy = view.getBottom() - 60;
        int initialRadius = view.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        isEditTextVisible = false;
        anim.start();
    }

    @Override
    public void onClick(View v) {

        // TODO check if write comment button on confirmation button is clicked here
        switch (v.getId()) {
            case R.id.btn_add:
                if (!isEditTextVisible) {
                    revealEditText(mRevealView);
                    mEditTextTodo.requestFocus();
                    mInputManager.showSoftInput(mEditTextTodo, InputMethodManager.SHOW_IMPLICIT);
                    mAddButton.setImageResource(R.drawable.icn_morp);
                    mAnimatable = (Animatable) (mAddButton).getDrawable();
                    mAnimatable.start();
                    applyRippleColor(getResources().getColor(R.color.light_green), getResources().getColor(R.color.dark_green));
                } else {
                    //check if item click event is fired correctly
                    // Toast.makeText(DetailActivity.this, "Add the contents ", Toast.LENGTH_SHORT).show();

                    // check if any contents was added by user
                    String sComments = mEditTextTodo.getText().toString();
                    if(!sComments.isEmpty()) { // if any of input was added by user, do conversion & remarketing report
                        // TODO Conversion(add_comment) handling
                        mGoogleAds.ConversionReport(getApplicationContext(), "7f_JCL7ErmEQ6IWsrwM");

                        // TODO Remarketing(add_comment) handling
                        mGoogleAds.RemarketingReport(getApplicationContext(), "activity_detail", "add_comment");

                        // TODO Send an event of Google Analytics
                        // we will track the event and also this will be checked as Goal (conversion) in GA
                        if(mTracker != null){
                            mTracker.send(new HitBuilders.EventBuilder()
                                    .setCategory("Action")
                                    .setAction("Add_Comment")
                                    .setLabel(Integer.toString(mIndex))
                                    .build());
                        }
                    }
                    // addToDo(mEditTextTodo.getText().toString());
                    addToDo(sComments);
                    mToDoAdapter.notifyDataSetChanged();
                    mInputManager.hideSoftInputFromWindow(mEditTextTodo.getWindowToken(), 0);
                    hideEditText(mRevealView);
                    mAddButton.setImageResource(R.drawable.icn_morph_reverse);
                    mAnimatable = (Animatable) (mAddButton).getDrawable();
                    mAnimatable.start();
                    applyRippleColor(mPalette.getVibrantColor(defaultColorForRipple),
                            mPalette.getDarkVibrantColor(defaultColorForRipple));

                    // make mEditTextTodo empty after the input is added
                    mEditTextTodo.setText("");
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(100);
        mAddButton.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAddButton.setVisibility(View.GONE);
                finishAfterTransition();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private int getDeeplinkId(String sDeeplink) {
        int nIndex = 0;
        switch (sDeeplink) {
            case "/koreanbeef":
                nIndex = 0;
                break;
            case "/chinesecoldnoodle":
                nIndex = 1;
                break;
            case "/porkcutlet":
                nIndex = 2;
                break;
            case "/sweetpotatonoodle":
                nIndex = 3;
                break;
            case "/checkcake":
                nIndex = 4;
                break;
            case "/squidricecake":
                nIndex = 5;
                break;
            case "/redbeannoodle":
                nIndex = 6;
                break;
            case "/chokolatpie":
                nIndex = 7;
                break;
            case "/tofusandwitch":
                nIndex = 8;
                break;
            case "/prawnkimbab":
                nIndex = 9;
                break;
            default:
                break;
        }
        return nIndex;
    }
}

