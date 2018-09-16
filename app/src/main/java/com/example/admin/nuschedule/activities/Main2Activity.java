package com.example.admin.nuschedule.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.admin.nuschedule.R;
import com.example.admin.nuschedule.fragments.DayFragment;
import com.example.admin.nuschedule.fragments.MonthFragment;
import com.example.admin.nuschedule.fragments.ScheduleFragment;
import com.example.admin.nuschedule.fragments.WeekFragment;
import com.example.admin.nuschedule.other.CircleTransform;
import com.example.admin.nuschedule.other.Utils;
import com.example.admin.nuschedule.view.LessonEventView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.example.admin.nuschedule.other.Constants.MY_PREFS_NAME;
import static com.example.admin.nuschedule.other.Constants.dayOfWeek;


public class Main2Activity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private Menu menu;
    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "https://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://photos.app.goo.gl/gFAbZsf83P2a3Y5F7";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;
    Utils utils;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();
        utils = new Utils();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText("Daulet Issatayev");
        txtWebsite.setText("201599251");

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
        //navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                ScheduleFragment scheduleFragment = new ScheduleFragment();
                return scheduleFragment;
            case 1:
                DayFragment dayFragment = new DayFragment();
                return dayFragment;
            case 2:
                WeekFragment weekFragment = new WeekFragment();
                return weekFragment;
            case 3:
                MonthFragment monthFragment = new MonthFragment();
                return monthFragment;
            default:
                return new WeekFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_photos:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PHOTOS;
                        break;
                    case R.id.nav_movies:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MOVIES;
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.nav_about_us:
                        startActivity(new Intent(Main2Activity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 2;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        final MenuItem notifyBtn = menu.findItem(R.id.notify_btn);
        final MenuItem silenceBtn = menu.findItem(R.id.silence_btn);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Boolean notify_mode = prefs.getBoolean("notify_mode", true);
        if (!prefs.contains("notify_mode")) {
            notifyBtn.setVisible(false);
            silenceBtn.setVisible(true);
        } else{
            if(!notify_mode) {
                notifyBtn.setVisible(true);
                silenceBtn.setVisible(false);
            }else {
                notifyBtn.setVisible(false);
                silenceBtn.setVisible(true);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        WeekFragment weekFragment = (WeekFragment) getSupportFragmentManager().findFragmentById(R.id.weekFragment);
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }
        if (id == R.id.new_btn) {
            int[] colors = utils.getMainColors(getApplicationContext());
            Intent intent = new Intent(Main2Activity.this, AddEventActivity.class);
            intent.putExtra("START_TIME", "9:00 AM");
            intent.putExtra("END_TIME", "10:00 AM");
            intent.putExtra("COLOR", colors[new Random().nextInt(colors.length)] );
            intent.putExtra("DAY", dayOfWeek[utils.getTodayIndex()]);
            Log.d("mLog", "send day: "+dayOfWeek[utils.getTodayIndex()]);
            startActivityForResult(intent, 1);
        }
        if(id == R.id.notify_btn){
            utils.showNotif(Main2Activity.this);
            weekFragment.notificationOn();
        }
        if(id == R.id.silence_btn){
            weekFragment.notificationOff();

            Toast.makeText(this, "Notification is off", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("mLog", "onActivityResult: requestCode: "+requestCode);
        WeekFragment weekFragment = (WeekFragment) getSupportFragmentManager().findFragmentById(R.id.weekFragment);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                ArrayList<Integer> resultIds  = data.getIntegerArrayListExtra("resultIds");
                for (int i = 0; i < resultIds.size(); i++) {
                    weekFragment.addNewLesson(resultIds.get(i));
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.w("mLog", "no data inserted :(");
            }
        } else
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                int lessonId  = data.getIntExtra("resultId", -1);
                int btnType = data.getIntExtra("btnType", -1);
                Log.d("mLog", "onActivityResult btnType: "+ btnType);
                if(btnType == 0){
                    weekFragment.deleteLesson(lessonId);
                }else if(btnType == 1){
                    Intent intent = new Intent(getApplicationContext(), EditLessonActivity.class);
                    intent.putExtra("lessonId",lessonId);
                    startActivityForResult(intent, 3);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.w("mLog", "no menu button pressed");
            }
        }
        if (requestCode == 3) {
            if(resultCode == Activity.RESULT_OK){
                int lessonId  = data.getIntExtra("lessonId", -1);
                if(lessonId > 0){
                    Log.d("mLog", "btnType delete ");
                    LinearLayout tableLayout = (LinearLayout) findViewById(R.id.calendarSplitterRelativeLayout);
                    LessonEventView lessonView = (LessonEventView) tableLayout.findViewWithTag(lessonId);
                    if( lessonView != null){
                        ((RelativeLayout) lessonView.getParent()).removeView(lessonView);
                        int dayIndex = Arrays.asList(dayOfWeek).indexOf(data.getStringExtra("day"));
                        //weekFragment.days.get(dayIndex).deleteLesson(lessonId);
                    } else{
                        Log.d("mLog", "deleteLesson: couldn't find view with tag "+ lessonId);
                    }
                    weekFragment.addNewLesson(lessonId);
                }
                Log.d("mLog", "got result : "+ lessonId);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.w("mLog", "no menu button pressed");
            }
        }
    }*/

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }
}