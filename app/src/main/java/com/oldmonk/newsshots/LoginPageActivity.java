package com.oldmonk.newsshots;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPageActivity extends AppCompatActivity {

    Button buttonLogin, buttonNewAcc;
    EditText etID, etPassword;
    SharedPreferences prefer;
    private static final String LOG_TAG = LoginPageActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        buttonLogin = (Button) findViewById(R.id.b_login);
        buttonNewAcc = (Button) findViewById((R.id.b_create_new_account));
        etID = (EditText)findViewById((R.id.et_ID));
        etPassword = (EditText)findViewById(R.id.et_password);

        prefer = this.getSharedPreferences(Utils.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String stringID = etID.getText().toString();
                String stringPass = etPassword.getText().toString();

                AppDatabase database = Room.databaseBuilder(LoginPageActivity.this, AppDatabase.class, "db-user")
                        .allowMainThreadQueries()
                        .build();
                UserInfoDAO userDAO = database.getUserInfoDAO();

                UserInfo user = userDAO.getUserWithID(stringID);
                if(user != null) {
                    String passCheck = user.getPassword();

                    if (stringPass.equals(passCheck)) {

                        SharedPreferences.Editor editor = prefer.edit();
                        editor.putBoolean(Utils.SP_IS_LOGGED_IN,true);
                        editor.putString(Utils.SP_LOGGED_IN_ID, stringID);
                        editor.apply();
                        Log.d(LOG_TAG, "hi dear");

                        Intent startLoggedIn = new Intent(LoginPageActivity.this, MainActivity.class);
                        startLoggedIn.putExtra(Intent.EXTRA_TEXT, etID.getText().toString());
                        startActivity(startLoggedIn);
                        finish();
                    }
                }else{
                    Toast.makeText(LoginPageActivity.this, "User not found. Please try again.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        buttonNewAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startCreateAccount = new Intent(LoginPageActivity.this, CreateAccountActivity.class);
                startActivity(startCreateAccount);
            }
        });
    }
}
