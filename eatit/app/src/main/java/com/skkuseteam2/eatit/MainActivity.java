package com.skkuseteam2.eatit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // frame layout에 각 메뉴의 fragment 표시
    private FragmentManager fragmentManager = getSupportFragmentManager();
    //Bundle data = new Bundle();
    // 각 fragment들
    private ServerTestFragment serverTestFragment = new ServerTestFragment();
    private SearchFragment searchFragment = new SearchFragment();
    private MyPageFragment myPageFragment = new MyPageFragment();

    private ApplicationController application;
    private Intent intent;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    transaction.replace(R.id.contents, serverTestFragment).commit();
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_dashboard);
                    transaction.replace(R.id.contents, searchFragment).commit();
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                    transaction.replace(R.id.contents, myPageFragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register Activity
        intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivityForResult(intent, 1);

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // 메인 페이지 표시
        // set initial page
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.contents, serverTestFragment).commit();

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {

            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();

            application = (ApplicationController)this.getApplicationContext();
            int uid = application.getUserId();
            System.out.println(uid);

            /*
            // user의 평가 모델이 있는지 검사, 없으면 initialevaluation activity 실행
            if (uid != 0 && !hasEvaluation(uid)) {
                intent = new Intent(getApplicationContext(), initialEvaluation.class);
                startActivity(intent);
            }*/
            /*
            // 로그인후 실행할 액티비티
            // First Evaluation Activity
            Intent intent = new Intent(getApplicationContext(), initialEvaluation.class);
            startActivity(intent);
            */
        }
    }

    private Boolean hasEvaluation(int uid) {

        return Boolean.FALSE;
    }
}