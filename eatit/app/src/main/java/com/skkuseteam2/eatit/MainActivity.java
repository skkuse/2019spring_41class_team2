package com.skkuseteam2.eatit;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final ServerTestFragment serverTestFragment = new ServerTestFragment();
    final RecommendationFragment recommendationFragment = new RecommendationFragment();
    final SearchFragment searchFragment = new SearchFragment();
    final MyPageFragment myPageFragment = new MyPageFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = recommendationFragment;

    private ApplicationController application;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register Activity
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivityForResult(intent, 1);

        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

        fm.beginTransaction().add(R.id.contents, myPageFragment, "3").hide(myPageFragment).commit();
        fm.beginTransaction().add(R.id.contents, searchFragment, "2").hide(searchFragment).commit();
        fm.beginTransaction().add(R.id.contents, recommendationFragment, "1").commit();

        BottomNavigationView navView = (BottomNavigationView)findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        fm.beginTransaction().hide(active).show(recommendationFragment).commit();
                        active = recommendationFragment;
                        return true;
                    case R.id.navigation_dashboard:
                        fm.beginTransaction().hide(active).show(searchFragment).commit();
                        active = searchFragment;
                        return true;
                    case R.id.navigation_notifications:
                        fm.beginTransaction().hide(active).show(myPageFragment).commit();
                        active = myPageFragment;
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {

            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();

            /*
            // 로그인후 실행할 액티비티
            // First Evaluation Activity
            Intent intent = new Intent(getApplicationContext(), initialEvaluation.class);
            startActivity(intent);
            */
        }
    }
}