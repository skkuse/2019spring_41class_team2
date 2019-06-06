package com.skkuseteam2.eatit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View.OnClickListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class RecommendationFragment extends android.support.v4.app.Fragment {
    /*
    TextView item, ingredient1, ingredient2, ingredient3, ingredient4;
    ImageView food;
    Bitmap bitmap;
    ImageButton cart;
    */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recommendation, container, false);

        /*
        item = view.findViewById(R.id.textView1);
        food = view.findViewById(R.id.imageView1);
        ingredient1 = view.findViewById(R.id.textView_M1);
        ingredient2 = view.findViewById(R.id.textView_M2);
        ingredient3 = view.findViewById(R.id.textView_M3);
        ingredient4 = view.findViewById(R.id.textView_M4);
        cart = view.findViewById(R.id.imageButton1);


        item.setText("햄버거");
        ingredient1.setText("빵");
        ingredient2.setText("햄");
        ingredient3.setText("야채");
        ingredient4.setText("캐찹");

        Thread mThread = new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL("http://52.78.88.3:8080/media/40.jpg");

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                }catch(MalformedURLException e) {
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };

        mThread.start();
        try{
            mThread.join();
            food.setImageBitmap(bitmap);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        */

        /* //프래그먼트 추가
        LayoutFragment add_fragment = new LayoutFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.addfragment, add_fragment);
        fragmentTransaction.commit();
        */

        return view;
    }

    //cart.setOn

}