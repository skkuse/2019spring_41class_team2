package com.skkuseteam2.eatit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // frame layout에 각 메뉴의 fragment 표시
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment fr, fs, fm;
    //Bundle data = new Bundle();
    // 각 fragment들
    private ServerTestFragment serverTestFragment = new ServerTestFragment();
    private SearchFragment searchFragment = new SearchFragment();
    private MyPageFragment myPageFragment = new MyPageFragment();
    private RecommendationFragment recommendationFragment = new RecommendationFragment();
    private LayoutFragment layoutFragment = new LayoutFragment();

    private ApplicationController application;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    if (fr == null) {
                        fr = new RecommendationFragment();
                        fragmentManager.beginTransaction().add(R.id.contents, fr).commit();
                    }
                    if (fr != null) fragmentManager.beginTransaction().show(fr).commit();
                    if (fs != null) fragmentManager.beginTransaction().hide(fs).commit();
                    if (fm != null) fragmentManager.beginTransaction().hide(fm).commit();
                    //transaction.replace(R.id.contents, recommendationFragment).commit();
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_dashboard);
                    if (fs == null) {
                        fs = new SearchFragment();
                        fragmentManager.beginTransaction().add(R.id.contents, fs).commit();
                    }
                    if (fr != null) fragmentManager.beginTransaction().hide(fr).commit();
                    if (fs != null) fragmentManager.beginTransaction().show(fs).commit();
                    if (fm != null) fragmentManager.beginTransaction().hide(fm).commit();
                    //transaction.replace(R.id.contents, searchFragment).commit();
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                    if (fm == null) {
                        fm = new MyPageFragment();
                        fragmentManager.beginTransaction().add(R.id.contents, fm).commit();
                    }
                    if (fr != null) fragmentManager.beginTransaction().hide(fr).commit();
                    if (fs != null) fragmentManager.beginTransaction().hide(fs).commit();
                    if (fm != null) fragmentManager.beginTransaction().show(fm).commit();
                    //transaction.replace(R.id.contents, myPageFragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register Activity
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivityForResult(intent, 1);


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

    @Override
    protected void onStart() {
        super.onStart();

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // 메인 페이지 표시
        // set initial page
        fragmentManager = getSupportFragmentManager();
        fr = new RecommendationFragment();
        fragmentManager.beginTransaction().replace(R.id.contents, fr).commit();
        //FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.replace(R.id.contents, recommendationFragment).commit();

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private Boolean hasEvaluation(int uid) {

        return Boolean.FALSE;
    }
}