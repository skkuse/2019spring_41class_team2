package com.skkuseteam2.eatit;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerTestFragment extends android.support.v4.app.Fragment {

    private NetworkService networkService;

    // tcp 관련 선언
    private Socket clntSocket;
    private BufferedReader sockIn;
    private PrintWriter sockOut;
    private int port = 60728;
    private final String ip = "192.168.219.103";
    private TCPThread tcpThread;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // View 생성
        View view = inflater.inflate(R.layout.fragment_server_test, container, false);


        // ip, port 연결, network 연결
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("52.78.88.3",8080);
        networkService = ApplicationController.getInstance().getNetworkService();

        // db 연동 체크
        Button regButton = (Button)view.findViewById(R.id.regBtn);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Version version = new Version();
                version.setVersion("0.0.2.9");
                Call<Version> testCall = networkService.post_version(version);
                testCall.enqueue(new Callback<Version>() {
                    @Override
                    public void onResponse(Call<Version> call, Response<Version> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "등록되었습니다", Toast.LENGTH_SHORT).show();
                        } else {
                            int statusCode = response.code();
                            Toast.makeText(getContext(), "등록안됨", Toast.LENGTH_SHORT).show();
                            Log.i("MyTAg", "응답코드 : " + statusCode);
                        }
                    }

                    @Override
                    public void onFailure(Call<Version> call, Throwable t) {
                        Toast.makeText(getContext(), "등록실패", Toast.LENGTH_SHORT).show();
                        Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
                    }
                });
            }
        });

        // TCP 소켓 통신
        new TCPConnect().run();
        Button sendButton = (Button)view.findViewById(R.id.sendBtn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tcpThread = new TCPThread();
                tcpThread.start();
            }
        });

        return view;
    }

    class TCPConnect {
        public void run() {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                clntSocket = new Socket(ip, port);
                sockIn = new BufferedReader(new InputStreamReader(clntSocket.getInputStream()));
                sockOut = new PrintWriter(clntSocket.getOutputStream(), true);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class TCPThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    sockOut.print("hello");
                    // System.out.println("Message sented!");
                    sockOut.flush();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}