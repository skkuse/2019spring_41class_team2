package com.skkuseteam2.eatit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluationFragment extends Fragment {

    View view;

    private NetworkService networkService;
    private ApplicationController applicationController;

    // tcp 관련 선언
    private Socket clntSocket;
    private BufferedReader sockIn;
    private PrintWriter sockOut;
    private int port = 60728;
    private final String ip = "10.0.2.2";
    private ServerTestFragment.TCPThread tcpThread;

    Food food;
    ImageView imageView;
    String strImg;
    Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.evaluationfragment_layout,container,false);

        // ip, port 연결, network 연결
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("52.78.88.3",8080);
        networkService = ApplicationController.getInstance().getNetworkService();

        // 몇 번 아이템 표시할 건지 결정(랜덤)
        Random random = new Random();
        int fid;
        while ((fid = random.nextInt(301)) == 0)
            continue;

        final TextView textView = (TextView)view.findViewById(R.id.text_fname);

        Call<Food> foodCall = networkService.getIdFood(fid);
        foodCall.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (response.isSuccessful()) {
                    food = response.body();
                    textView.setText(food.getName());
                    strImg = food.getPhoto();
                    displayImage();
                    sendToServer();
                } else {
                    int statusCode = response.code();
                    Log.i("MyTag", "응답코드 : " + statusCode);
                }
            }
            @Override
            public void onFailure(Call<Food> call, Throwable t) { }
        });

        return view;
    }

    private void displayImage() {
        // 이미지뷰에 url로 이미지 표시
        imageView = (ImageView)view.findViewById(R.id.img_food);
        Thread mThread = new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL(strImg);
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

    private void sendToServer() {

        int uid = applicationController.getUserId();
    }
}
