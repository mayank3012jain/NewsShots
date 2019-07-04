package com.oldmonk.newsshots;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccountActivity extends AppCompatActivity {

    EditText etID, etPassword;
    Button buttonCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        if(getSupportActionBar()!= null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etID = (EditText)findViewById(R.id.et_ID_create);
        etPassword = (EditText)findViewById(R.id.et_password_create);
        buttonCreate = (Button)findViewById(R.id.b_create);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringID = etID.getText().toString();
                String stringPassword = etPassword.getText().toString();

                AppDatabase database = Room.databaseBuilder(CreateAccountActivity.this,AppDatabase.class, "db-user")
                        .allowMainThreadQueries()
                        .build();

                UserInfoDAO userDAO = database.getUserInfoDAO();

                UserInfo userCreated = new UserInfo();
                userCreated.setUserID(stringID);
                userCreated.setPassword(stringPassword);
                userCreated.setPrimaryLocation(getString(R.string.location_value_in));
                userCreated.setHasSecondaryLocation(false);
                userDAO.insert(userCreated);

                Toast.makeText(CreateAccountActivity.this, stringID + " ID created", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
