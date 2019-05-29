package com.skkuseteam2.eatit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ServerTestFragment extends android.support.v4.app.Fragment {

    private NetworkService networkService;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ip, port 연결, network 연결
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("localhost",8000);
        networkService = ApplicationController.getInstance().getNetworkService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_server_test, container, false);
    }

}