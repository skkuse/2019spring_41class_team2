package com.skkuseteam2.eatit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LayoutFragment extends android.support.v4.app.Fragment {

    TextView item, ingredient1, ingredient2, ingredient3, ingredient4;
    ImageView food;
    Bitmap bitmap;
    ImageButton cart;
    int i = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_layout, container, false);

        item = view.findViewById(R.id.textView);
        food = view.findViewById(R.id.imageView1);
        ingredient1 = view.findViewById(R.id.textView_M1);
        ingredient2 = view.findViewById(R.id.textView_M2);
        ingredient3 = view.findViewById(R.id.textView_M3);
        ingredient4 = view.findViewById(R.id.textView_M4);
        cart = view.findViewById(R.id.imageButton);


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

        cart.setImageResource(R.drawable.cart);
        cart.setScaleType(ImageButton.ScaleType.FIT_CENTER);
        cart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                i  = 1 - i;
                if(i == 1){
                    cart.setImageResource(R.drawable.cart_click);
                    Toast.makeText(getActivity(), "장바구니에 추가했습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    cart.setImageResource(R.drawable.cart);
                    Toast.makeText(getActivity(), "취소했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    /*
    public void onClick(View view){
        switch(view.getId()){
            case R.id.imageButton:
                cart.setSelected(true);
                break;
        }
    }
    */
}