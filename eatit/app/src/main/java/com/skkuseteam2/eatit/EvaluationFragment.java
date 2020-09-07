package com.skkuseteam2.eatit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private int port = 60728;
    private final String ip = "10.0.2.2";
    private ServerTestFragment.TCPThread tcpThread;

    int fid;
    Food food;
    ImageView imageView;
    String strImg;
    Bitmap bitmap;

    ImageButton buttonGood;
    ImageButton buttonSoso;
    ImageButton buttonBad;

    // numEval 중복 증가 방지용
    boolean hasSent = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.evaluationfragment_layout,container,false);

        // ip, port 연결, network 연결
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("52.78.88.3",8080);
        networkService = ApplicationController.getInstance().getNetworkService();

        buttonGood = (ImageButton)view.findViewById(R.id.btnGood);
        buttonGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFoodToServer(0);
                buttonGood.setImageResource(R.drawable.love);
                buttonSoso.setImageResource(R.drawable.winking_gray);
                buttonBad.setImageResource(R.drawable.sad_gray);
            }
        });
        buttonSoso = (ImageButton)view.findViewById(R.id.btnSoso);
        buttonSoso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFoodToServer(1);
                buttonGood.setImageResource(R.drawable.love_gray);
                buttonSoso.setImageResource(R.drawable.winking);
                buttonBad.setImageResource(R.drawable.sad_gray);
            }
        });
        buttonBad = (ImageButton)view.findViewById(R.id.btnBad);
        buttonBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFoodToServer(2);
                buttonGood.setImageResource(R.drawable.love_gray);
                buttonSoso.setImageResource(R.drawable.winking_gray);
                buttonBad.setImageResource(R.drawable.sad);
            }
        });

        // 몇 번 아이템 표시할 건지 결정(랜덤)
        Random random = new Random();
        while ((fid = random.nextInt(301)) == 0) { continue; }

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

    private void sendFoodToServer(int index) {

        // TCP로 서버에 연결
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            clntSocket = new Socket(ip, port);
            // seTccpNoDelay: flush 제대로 작동되게함
            clntSocket.setTcpNoDelay(true);
//            Toast.makeText(getContext(), "연결완료", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        applicationController = (ApplicationController)getActivity().getApplicationContext();
        int uid = applicationController.getUserId();

        int[] props = {food.getProp_hot(), food.getProp_sweet(), food.getProp_sour(),
                food.getProp_cal(), food.getProp_soup(), food.getProp_main(), food.getProp_temp()};

        try {
            sendData(0, "eval");
            sendData(uid, null);
            sendData(fid, null);

            int[] intarr = {0, 0, 0};
            intarr[index] = 1;
            for (int i = 0; i<3; ++i)
                sendData(intarr[i], null);

            sendData(0, "evalEnd");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 처음 평가하는 데이터면 numEval 증가
        if (!hasSent) {
            applicationController.incNumEval();
            hasSent = true;
        }

        // 사용한 소켓 닫기
        try {
            clntSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendData(int data, String string) throws Exception {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        while (true) {
            PrintWriter sockOut = new PrintWriter(clntSocket.getOutputStream());
            if (string != null)
                sockOut.print(string);
            else
                sockOut.print(data);
            sockOut.flush();
            return;
        }
    }
}
