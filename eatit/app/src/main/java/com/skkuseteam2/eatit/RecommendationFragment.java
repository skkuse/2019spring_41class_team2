package com.skkuseteam2.eatit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationFragment extends android.support.v4.app.Fragment {

    private boolean isini = true;
    private int userid;
    private User user_temp = null;
    Button button;
    ImageButton evalBtn;
    Button rcmBtm;
    LinearLayout evalLayout;

    private ApplicationController applicationController;
    private NetworkService networkService;
    private CheckEvalThread checkEvalThread;

    private EvalHandler handler = new EvalHandler();

    // tcp 관련 선언
    private Socket clntSocket;
    private int port = 60728;
    private final String ip = "10.0.2.2";
    private ServerTestFragment.TCPThread tcpThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recommendation, container, false);

        // django network 연결
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("52.78.88.3",8080);
        networkService = ApplicationController.getInstance().getNetworkService();

        evalBtn = view.findViewById(R.id.evalButton);
        evalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), initialEvaluation.class);
                startActivity(intent);
            }
        });

        rcmBtm = view.findViewById(R.id.rcmBtm);
        rcmBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isini) {
            System.out.println("checkEval");
            checkEvalThread = new CheckEvalThread();
            checkEvalThread.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isini = false;
    }

    private void refresh() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this);
        ft.attach(this);
        ft.commit();
    }

    class CheckEvalThread extends Thread {
        @Override
        public void run() {
            applicationController = (ApplicationController)getActivity().getApplicationContext();
            do {
                userid = applicationController.getUserId();
            } while (userid == 0);
            System.out.printf("userid: %d\n", userid);

            Call<User> userCall = networkService.getIdUser(userid);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        user_temp = response.body();
                        boolean eval = user_temp.getEvaluate();
                        Message message = handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("eval", eval);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    } else {
                        int statusCode = response.code();
                        Log.i("MyTag", "응답코드 : " + statusCode);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

    class EvalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /*
            Bundle bundle = msg.getData();
            boolean eval = bundle.getBoolean("eval");*/
            boolean eval = applicationController.isHasEval();
            System.out.println(eval);
            if (eval == true) {
                evalLayout = getView().findViewById(R.id.evalLayout);
                evalLayout.setVisibility(View.GONE);
                printItems();
            } else {
                rcmBtm.setVisibility(View.GONE);
                evalLayout = getView().findViewById(R.id.evalLayout);
                evalLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void printItems() {

        int[] fid = new int[10];

        // tcp 통신: 표시할 아이템들 받아오기
        // TCP로 서버에 연결
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            clntSocket = new Socket(ip, port);
            // seTccpNoDelay: flush 제대로 작동되게함
            clntSocket.setTcpNoDelay(true);

            // 서버에 recommend, uid 전송
            sendData(0, "recommend");
            sendData(applicationController.getUserId(), null);

            BufferedReader sockIn = new BufferedReader(new InputStreamReader(clntSocket.getInputStream()));
            // 서버에게서 데이터 받아옴
            for (int i=0; i<10; ++i) {

                String data = sockIn.readLine();
                data = data.replaceAll("(\r\n|\r|\n|\n\r)", "");
                fid[i] = Integer.parseInt(data);
            }

            clntSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        int[] fid = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println(fid);
        // 프래그먼트 추가
        int count = 0;
        while (count < 10) {
            Bundle args = new Bundle();
            args.putInt("fid", fid[count]);
            Fragment fragment = new LayoutFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(args);
            fragmentTransaction.add(R.id.Sub_Linear, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            count++;
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