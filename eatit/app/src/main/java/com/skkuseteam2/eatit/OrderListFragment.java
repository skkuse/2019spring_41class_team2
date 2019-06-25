package com.skkuseteam2.eatit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListFragment extends android.support.v4.app.Fragment {

    View view;

    private NetworkService networkService;
    final private ApplicationController application = ApplicationController.getInstance();
    int uid = application.getUserId();

    int oid;

    RelativeLayout thisLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_orderlist, container, false);
        thisLayout = view.findViewById(R.id.relativeLayout_orderlist);

        // ip, port 연결, network 연결

        application.buildNetworkService("52.78.88.3",8080);
        networkService = ApplicationController.getInstance().getNetworkService();

        Bundle args = getArguments();
        oid = args.getInt("oid");

        displayInfo();

        Button buttonCancel = view.findViewById(R.id.button_cancelOrder);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOrder();
            }
        });

        return view;
    }

    protected void displayInfo() {

        final Call<Order> orderCall = networkService.getIdOrder(oid);
        orderCall.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    String price = Integer.toString(response.body().getPrice());
                    price = price.concat(" 원");
                    TextView orderPrice = view.findViewById(R.id.textView_orderPrice);
                    orderPrice.setText(price);

                    TextView orderNumber = view.findViewById(R.id.textView_orderNumber);
                    String oidStr = Integer.toString(oid);
                    orderNumber.setText(oidStr);
                }
            }
            @Override
            public void onFailure(Call<Order> call, Throwable t) {}
        });
    }

    protected void cancelOrder() {

        Call<Order> orderDeleteCall = networkService.delete_order(oid);
        orderDeleteCall.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "주문이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                    thisLayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<Order> call, Throwable t) {}
        });
    }
}