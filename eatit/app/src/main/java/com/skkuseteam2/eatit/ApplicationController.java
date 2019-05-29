// 서버와 통신하기 위해 instance를 생성하고 받아온 port, ip를 설정하는 클래스
package com.skkuseteam2.eatit;

import android.app.Application;
import android.util.Log;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationController extends Application {

    public final static String TAG = "NMR";
    private static ApplicationController instance;
    public static ApplicationController getInstance() {return instance;}

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this; // 처음 실행될때 인스턴트 생성
    }

    private NetworkService networkService;
    public NetworkService getNetworkService() {return networkService;}

    private String baseUrl;

    public void buildNetworkService(String ip, int port) {
        synchronized (ApplicationController.class) {
            if (networkService == null) {
                baseUrl = String.format("http://%s:%d/", ip, port);
                Log.i(TAG, baseUrl);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                networkService = retrofit.create(NetworkService.class);
            }
        }
    }

    public void buildNetworkService(String ip) {
        synchronized (ApplicationController.class) {
            if (networkService == null) {
                baseUrl = String.format("http://%s/", ip);
                Log.i(TAG, baseUrl);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                networkService = retrofit.create(NetworkService.class);
            }
        }
    }
}
