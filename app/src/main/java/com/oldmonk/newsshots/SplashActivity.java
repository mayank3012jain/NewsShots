package com.oldmonk.newsshots;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    private static final String LOG_TAG = SplashActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);

        SharedPreferences pref = this.getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        Boolean isLoggedIn = pref.getBoolean(getString(R.string.is_logged_in), false);
        Log.d(LOG_TAG, "Hello "+isLoggedIn);
        if(isLoggedIn){
            startActivity(new Intent(this, MainActivity.class));
        }else{
            startActivity(new Intent(this, LoginPageActivity.class));
        }
        finish();
    }
}
