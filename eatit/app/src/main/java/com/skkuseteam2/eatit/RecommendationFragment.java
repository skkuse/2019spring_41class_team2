package com.skkuseteam2.eatit;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationFragment extends android.support.v4.app.Fragment {

    private boolean isini = true;
    private int userid;
    private User user_temp = null;
    int count = 0;
    Button button;
    ImageButton evalBtn;
    LinearLayout evalLayout;

    private ApplicationController applicationController;
    private NetworkService networkService;
    private CheckEvalThread checkEvalThread;

    private EvalHandler handler = new EvalHandler();

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

            Bundle bundle = msg.getData();
            boolean eval = bundle.getBoolean("eval");

            if (eval == true) {
                // 프래그먼트 추가
                while (count < 10) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.Sub_Linear, new LayoutFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    count++;
                }
            } else {
                evalLayout = getView().findViewById(R.id.evalLayout);
                evalLayout.setVisibility(View.VISIBLE);
            }
        }
    }
}