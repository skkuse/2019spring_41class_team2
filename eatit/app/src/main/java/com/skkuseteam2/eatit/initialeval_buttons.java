package com.skkuseteam2.eatit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class initialeval_buttons extends Fragment {

    Button finishButton;
    Button refreshButton;
    private NetworkService networkService;
    ApplicationController applicationController;

    Toast toast;

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
}
