package com.oldmonk.newsshots;

import android.arch.persistence.room.Room;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar actionBar;
    TabLayout tabLayout;
    ViewPager viewPager;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    String mLocation;
    String mUserID;
    AppDatabase database;
    boolean locationChanged;
    public static String TAG = "locationcrash";
    Button mLogOutNv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        actionBar = (android.support.v7.widget.Toolbar) findViewById(R.id.tb_action_bar_main);
        setSupportActionBar(actionBar);

        mUserID = this.getSharedPreferences(Utils.SHARED_PREFERENCES_NAME, MODE_PRIVATE).getString(Utils.SP_LOGGED_IN_ID, "NoUserpassed");
        database = Room.databaseBuilder(this, AppDatabase.class, "db-user")
                .allowMainThreadQueries()
                .build();
        UserInfoDAO userDAO = database.getUserInfoDAO();
        UserInfo currentUser = userDAO.getUserWithID(mUserID);
        mLocation = currentUser.getPrimaryLocation();
        locationChanged = false;

        drawerLayout = findViewById(R.id.drawer_nv);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, actionBar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        //navigationView.setNavigationItemSelectedListener(this);
        setupNavigationDrawer(currentUser);

        viewPager = (ViewPager)findViewById(R.id.vp_categories);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));


        tabLayout= (TabLayout)findViewById(R.id.tl_categories);
        tabLayout.setupWithViewPager(viewPager);

        mLogOutNv = findViewById(R.id.b_log_out_nv);
        mLogOutNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = MainActivity.this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(getString(R.string.is_logged_in), false);
                editor.apply();

                Intent intentLogOut = new Intent(MainActivity.this, LoginPageActivity.class);
                startActivity(intentLogOut);
                finish();
            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        FragmentManager mFragmentManager;

        public ViewPagerAdapter(FragmentManager fm) {

            super(fm);
            mFragmentManager = fm;
        }

        @Override
        public Fragment getItem(int i) {
            CategoryPageFragment item = CategoryPageFragment.newInstance(i, mLocation);
            item.setmPosition(i);
            item.setmLocation(mLocation);
            return item;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position){
            String title;
            if(position == 0){
                title = "Top Headlines";
            }else if(position ==1){
                title = "Tech News";
            }else if(position ==2){
                title = "Business";
            }else{
                title = null;
            }
            return title;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            if(locationChanged){//kya karu
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            CategoryPageFragment created = (CategoryPageFragment)super.instantiateItem(container, position);
            if(mLocation!=created.getmLocation()){
                //List<Fragment> fragmentList = mFragmentManager.getFragments();
                //CategoryPageFragment temp = (CategoryPageFragment)fragmentList.get(position);
                created.setmLocation(mLocation);
            }
            return created;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_logged_in_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        if(item.getItemId() == R.id.action_log_out){
//            SharedPreferences pref = MainActivity.this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putBoolean(getString(R.string.is_logged_in), false);
//            editor.apply();
//
//            Intent intentLogOut = new Intent(MainActivity.this, LoginPageActivity.class);
//            startActivity(intentLogOut);
//            finish();;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    private void setupNavigationDrawer(UserInfo currentUser){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int selectedId = menuItem.getItemId();
                if(selectedId == R.id.item_location_in){
                    mLocation = getString(R.string.location_value_in);
                    locationChanged = true;
                    viewPager.getAdapter().notifyDataSetChanged();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if(selectedId == R.id.item_location_us){
                    mLocation = getString(R.string.location_value_us);
                    locationChanged = true;
                    viewPager.getAdapter().notifyDataSetChanged();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if(selectedId == R.id.item_location_fr){
                    mLocation = getString(R.string.location_value_fr);
                    locationChanged = true;
                    viewPager.getAdapter().notifyDataSetChanged();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if(selectedId == R.id.item_location_jp){
                    mLocation = getString(R.string.location_value_jp);
                    locationChanged = true;
                    viewPager.getAdapter().notifyDataSetChanged();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if(selectedId == R.id.item_settings){
                    startActivity(new Intent(getBaseContext(), SettingsActivity.class));
                }else if(selectedId == R.id.item_top_headlines){
                    viewPager.setCurrentItem(0);
                }else if(selectedId == R.id.item_tech) {
                    viewPager.setCurrentItem(1);
                }else if(selectedId == R.id.item_business) {
                    viewPager.setCurrentItem(2);
                }
                onBackPressed();
                return true;
            }
        });



        String secLoc = currentUser.getSecondaryLocation();
        String primLoc = currentUser.getPrimaryLocation();
        Log.d(TAG, "setupNavigationDrawer: "+primLoc);
        String rIdSecLoc = "item_location_" + secLoc;
        String rIdPrimLoc = "item_location_" + primLoc;

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.getMenu().findItem(R.id.item_location_in).setVisible(false);
        navigationView.getMenu().findItem(R.id.item_location_us).setVisible(false);
        navigationView.getMenu().findItem(R.id.item_location_fr).setVisible(false);
        navigationView.getMenu().findItem(R.id.item_location_jp).setVisible(false);

        navigationView.getMenu().findItem(getResources().getIdentifier(rIdPrimLoc, "id", getPackageName())).setVisible(true);
        if (currentUser.hasSecondaryLocation()) {
            navigationView.getMenu().findItem(getResources().getIdentifier(rIdSecLoc, "id", getPackageName())).setVisible(true);
        }
    }


}
