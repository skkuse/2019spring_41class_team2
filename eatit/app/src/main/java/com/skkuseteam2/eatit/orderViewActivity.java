package com.skkuseteam2.eatit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class orderViewActivity extends AppCompatActivity {

    ImageView imageView;
    Bitmap bitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                finish();
            }
        });

        imageView = (ImageView)findViewById(R.id.imageView);

        Thread mThread = new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL("https://phinf.pstatic.net/contact/20180801_22/1533111377968ioAX8_JPEG/image.jpg");

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
