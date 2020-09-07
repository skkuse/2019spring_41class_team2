package com.skkuseteam2.eatit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyPageFragment extends android.support.v4.app.Fragment {

    Bitmap bitmap;
    View view;

    private NetworkService networkService;
    final private ApplicationController application = ApplicationController.getInstance();
    int uid;

    FragmentManager fragmentManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // View 생성
        view = inflater.inflate(R.layout.fragment_my_page, container, false);

        // django network 연결
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("52.78.88.3",8080);
        networkService = ApplicationController.getInstance().getNetworkService();

        fragmentManager = getFragmentManager();

        Button orderListButton = view.findViewById(R.id.button_orderlist);
        Button updateButton = view.findViewById(R.id.button_update);

        orderListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
                displayOrderList();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), initialEvaluation.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        displayProfile();
    }

    @Override
    public void onStart() {
        super.onStart();

        displayProfile();
    }

    protected void displayProfile() {

        uid = application.getUserId();

        Call<User> userCall = networkService.getIdUser(uid);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {

                    User user = response.body();

                    // 이름표시
                    String name = user.getName();
                    name = name.concat("님");
                    TextView textViewName = view.findViewById(R.id.textView_profile_name);
                    textViewName.setText(name);

                    displayImage(user.getProfile_image());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {}
        });
    }

    protected void displayImage(final String imgStr) {
        // 이미지뷰에 url로 이미지 표시
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView_profile);
        Thread mThread = new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL(imgStr);

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
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            imageView.setClipToOutline(true);
            imageView.setImageBitmap(bitmap);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    protected void displayOrderList() {
        Call<List<Order>> orderCall = networkService.getAllOrder();
        orderCall.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    List<Order> orders = response.body();
                    for (Order order : orders) {
                        Bundle args = new Bundle();
                        args.putInt("oid", order.getId());

                        Fragment fragment = new OrderListFragment();
                        fragment.setArguments(args);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.add(R.id.OrderListLinear, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {}
        });
    }

    private void refresh() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.detach(this);
        ft.attach(this);
        ft.commit();
    }
}