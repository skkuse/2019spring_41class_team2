package com.skkuseteam2.eatit;

import android.os.Bundle;
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

public class ServerTestFragment extends android.support.v4.app.Fragment {

    private NetworkService networkService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // View 생성
        View view = inflater.inflate(R.layout.fragment_server_test, container, false);

        // ip, port 연결, network 연결
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("10.0.2.2",8000);
        networkService = ApplicationController.getInstance().getNetworkService();

        Button regButton = (Button)view.findViewById(R.id.regBtn);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Version version = new Version();
                version.setVersion("0.0.2.4");
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

        return view;
    }

}