package com.oldmonk.newsshots;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar actionBar;
    TabLayout tabLayout;
    ViewPager viewPager;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    int mLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        actionBar = (android.support.v7.widget.Toolbar) findViewById(R.id.tb_action_bar_main);
        setSupportActionBar(actionBar);

        drawerLayout = findViewById(R.id.drawer_nv);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, actionBar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        //navigationView.setNavigationItemSelectedListener(this);
        setupNavigationDrawer();



        viewPager = (ViewPager)findViewById(R.id.vp_categories);
        mLocation = CategoryPageFragment.LOCATION_INDIA;
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        tabLayout= (TabLayout)findViewById(R.id.tl_categories);
        tabLayout.setupWithViewPager(viewPager);

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
            item.setmLocation("in");
            return item;
            //yahan pe kya daalu
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
            if(mLocation==CategoryPageFragment.LOCATION_USA){
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            CategoryPageFragment created = (CategoryPageFragment)super.instantiateItem(container, position);
            if(mLocation==CategoryPageFragment.LOCATION_USA){
                //List<Fragment> fragmentList = mFragmentManager.getFragments();
                //CategoryPageFragment temp = (CategoryPageFragment)fragmentList.get(position);
                created.setmLocation("us");
            }
            return created;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_logged_in_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.action_log_out){
            SharedPreferences pref = MainActivity.this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(getString(R.string.is_logged_in), false);
            editor.apply();

            Intent intentLogOut = new Intent(MainActivity.this, LoginPageActivity.class);
            startActivity(intentLogOut);
            finish();;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    private void setupNavigationDrawer(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int selectedId = menuItem.getItemId();
                if(selectedId == R.id.item_location_USA){
                    //Intent intentToChangeToUS = new Intent(MainActivity.this, )
                    mLocation = CategoryPageFragment.LOCATION_USA;
                    //viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
                    viewPager.getAdapter().notifyDataSetChanged();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if(selectedId == R.id.item_settings){
                    startActivity(new Intent(getBaseContext(), SettingsActivity.class));
                }
                return true;
            }
        });
    }
}
