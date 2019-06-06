package com.skkuseteam2.eatit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class postEvaluation extends AppCompatActivity {
    ImageView imageView;
    Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_evaluation);

//        Button button = (Button) findViewById(R.id.button2);
//        button.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View v)
//            {
//                finish();
//            }
//        });

        ImageButton btn1 = (ImageButton)findViewById(R.id.imageButton_good1);
        btn1.setImageResource(R.drawable.good);
        btn1.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View view){

            }
        });

        ImageButton btn2 = (ImageButton)findViewById(R.id.imageButton_soso1);
        btn2.setImageResource(R.drawable.soso);
        btn2.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View view){

            }
        });

        ImageButton btn3 = (ImageButton)findViewById(R.id.imageButton_bad1);
        btn3.setImageResource(R.drawable.bad);
        btn3.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View view){

            }
        });

        imageView = (ImageView) findViewById(R.id.imageView_evalitem1);

        Thread mThread = new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL("https://ssl.pstatic.net/static/pwe/address/img_profile.png");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch(MalformedURLException e){
                    e.printStackTrace();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        };

        mThread.start();

        try{
            mThread.join();

            imageView.setImageBitmap(bitmap);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
