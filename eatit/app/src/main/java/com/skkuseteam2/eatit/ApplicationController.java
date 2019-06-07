// 서버와 통신하기 위해 instance를 생성하고 받아온 port, ip를 설정하는 클래스
package com.skkuseteam2.eatit;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationController extends Application {

    private static ApplicationController instance;
    public static ApplicationController getInstance() {return instance;}

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this; // 처음 실행될때 인스턴트 생성
        userId = 0;
    }

    private NetworkService networkService;
    public NetworkService getNetworkService() {return networkService;}

    private int userId;
    public int getUserId() {return userId;}
    public void setUserId(int i) {userId = i;}

    private String baseUrl;

    public void buildNetworkService(String ip, int port) {
        synchronized (ApplicationController.class) {
            if (networkService == null) {
                baseUrl = String.format("http://%s:%d", ip, port);
                Gson gson = new GsonBuilder().create();

                GsonConverterFactory factory = GsonConverterFactory.create(gson);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(factory)
                        .build();
                networkService = retrofit.create(NetworkService.class);
            }
        }
    }
}
