package com.skkuseteam2.eatit;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import android.view.MotionEvent;
import android.view.View;
//import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;
//import android.widget.HorizontalScrollView;
//import android.widget.ScrollView;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.skkuseteam2.eatit.R.drawable.*;

public class itemDiaplay_proto extends AppCompatActivity {

    //ScrollView sv;
    //HorizontalScrollView hv;
    ImageView food;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_diaplay_proto);

        food = findViewById(R.id.imageView1);

        Thread mThread = new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL("http://52.78.88.3:8080/media/2.png");

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

        /*
        ImageView[] food;
        food = new ImageView[10];

        for(int i = 1; i < 11; i++){
            int k = getResources().getIdentifier("imageView"+i, "id", getPackageName());
            food[i] = findViewById(k);
            food[i].setImageResource(logo);
        }*/

        /*
        hv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP)
                    sv.requestDisallowInterceptTouchEvent(false);
                else
                    sv.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/
    }

    public void onButton1Clicked(View v){
        Toast.makeText(this, "장바구니에 추가되었습니다.", Toast.LENGTH_LONG).show();
    }


}