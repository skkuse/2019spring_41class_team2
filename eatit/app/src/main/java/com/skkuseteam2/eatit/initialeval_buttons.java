package com.skkuseteam2.eatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class initialeval_buttons extends Fragment {

    Button finishButton;
    Button refreshButton;
    private NetworkService networkService;
    ApplicationController applicationController;

    Toast toast;

    // tcp 관련 선언
    private Socket clntSocket;
    private int port = 60728;
    private final String ip = "10.0.2.2";
    private ServerTestFragment.TCPThread tcpThread;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_initialeval_buttons, container, false);
        finishButton = view.findViewById(R.id.btnFinish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applicationController = (ApplicationController)getActivity().getApplicationContext();

                if (applicationController.isHasEval() || applicationController.getNumEval() >= 30) {
                    if (!applicationController.isHasEval()) {
                        applicationController.setHasEval(true);
                        // ip, port 연결, network 연결
                        applicationController.buildNetworkService("52.78.88.3",8080);
                        networkService = ApplicationController.getInstance().getNetworkService();

                        // db에 eval이 false면 true로 변경
                        Call<User> userCall = networkService.getIdUser(applicationController.getUserId());
                        userCall.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    User user = response.body();
                                    user.setEvaluate(true);
                                    Call<User> evalCall = networkService.patchEvalUser(user.getId(), user);
                                    evalCall.enqueue(new Callback<User>() {
                                        @Override
                                        public void onResponse(Call<User> call, Response<User> response) { }
                                        @Override
                                        public void onFailure(Call<User> call, Throwable t) { }
                                    });
                                } else {
                                    int statusCode = response.code();
                                    Log.i("MyTag", "응답코드 : " + statusCode);
                                }
                            }
                            @Override
                            public void onFailure(Call<User> call, Throwable t) { }
                        });
                    }

                    // TCP로 서버에 연결
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    try {
                        clntSocket = new Socket(ip, port);
                        // seTccpNoDelay: flush 제대로 작동되게함
                        clntSocket.setTcpNoDelay(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    applicationController = (ApplicationController)getActivity().getApplicationContext();
                    // tcp 서버에 'train', uid 전송
                    try {
                        sendData(0, "train");
                        sendData(applicationController.getUserId(), null);

                        String data = recvData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // 사용한 소켓 닫기
                    try {
                        clntSocket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    toast = Toast.makeText(view.getContext(), "평가가 완료되었습니다.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    getActivity().finish();
                }
                else {
                    toast = Toast.makeText(view.getContext(), "30개 이상 평가해주세요.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        refreshButton = view.findViewById(R.id.btnMore);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }
        });

        return view;
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

    private String recvData() throws Exception {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        BufferedReader sockIn = new BufferedReader(new InputStreamReader(clntSocket.getInputStream()));
        String data = sockIn.readLine();

        return data;
    }
}