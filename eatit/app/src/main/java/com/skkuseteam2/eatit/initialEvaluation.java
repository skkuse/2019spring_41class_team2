package com.skkuseteam2.eatit;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class initialEvaluation extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_evaluation);
        //final TextView text = (TextView)findViewById(R.id.textView1);
        int count=0;
        while ( count<30){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.LinearLayout,new EvaluationFragment());

            fragmentTransaction.commit();
            count++;
        }
    }
}
