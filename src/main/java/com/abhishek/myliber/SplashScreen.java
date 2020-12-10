package com.abhishek.myliber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        //Toast.makeText(this, "isFirstRun: "+isFirstRun, Toast.LENGTH_SHORT).show();

        if (isFirstRun) {
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFirstRun", false).commit();
            //show sign up activity
            startActivity(new Intent(this, FirstProfileActivity.class));
            finish();
        }
        else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }


    }
}