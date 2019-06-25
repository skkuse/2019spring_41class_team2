package com.skkuseteam2.eatit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements LayoutFragment.OnMyListener {

    private NetworkService networkService;
    final private ApplicationController application = ApplicationController.getInstance();
    final int uid = application.getUserId();
    ArrayList<Integer> fid = new ArrayList<>();
    Cart cart;
    int price;
    int index = 0;
    boolean print = true;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // django network 연결
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("52.78.88.3",8080);
        networkService = ApplicationController.getInstance().getNetworkService();

        fragmentManager = getSupportFragmentManager();


        getSupportActionBar().setElevation(0);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setContentView(R.layout.activity_cart);

        // 사용자의 장바구니 데이터 읽어와 표시하기
        displayItems();
    }

    private void displayItems() {

        fid.clear();

        Call<Cart> cartCall = networkService.getUidCart(uid);
        cartCall.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) {
                    TextView textView = findViewById(R.id.textView_cart_no);
                    textView.setVisibility(View.GONE);
                    ScrollView scrollView = findViewById(R.id.scroll_cart);
                    scrollView.setVisibility(View.VISIBLE);

                    cart = response.body();
                    List<String> items = new ArrayList<>();

                    // cart의 아이템 정보를 읽은후 integer list에 넣음
                    String[] strArray = cart.getItems().split(",");
                    for (String string : strArray)
                        fid.add(Integer.parseInt(string));

                    //  리스트를 어레이로 변환
                    int n = fid.size();
                    int[] result = new int[n];
                    Integer[] temp = fid.toArray(new Integer[n]);
                    for (int i=0; i<n; ++i)
                        result[i] = temp[i];

                    updatePrice();

                    // 검색된 아이템들 프린트
                    if (print)
                        printItems(result);
                    else
                        print = true;

                } else {    // 장바구니가 존재하지 않는 경우
                    ScrollView scrollView = findViewById(R.id.scroll_cart);
                    scrollView.setVisibility(View.GONE);
                    TextView textView = findViewById(R.id.textView_cart_no);
                    textView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<Cart> call, Throwable t) {}
        });
    }

    private void printItems(int[] fid) {

        int n = fid.length;

        // 프래그먼트 추가
        int count = 0;
        while (count < n) {
            Bundle args = new Bundle();
            args.putInt("fid", fid[count]);
            args.putBoolean("isX", true);

            Fragment fragment = new LayoutFragment();
            fragment.setArguments(args);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.Cart_Linear, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            count++;
        }
    }

    private void updatePrice() {

        final int n = fid.size();
        price = 0;
        for (int i=0; i<n; i++) {
            Call<Food> foodCall = networkService.getIdFood(fid.get(i));
            foodCall.enqueue(new Callback<Food>() {
                @Override
                public void onResponse(Call<Food> call, Response<Food> response) {
                    if (response.isSuccessful()) {
                        price += response.body().getPrice();
                        System.out.println(index+" "+price);

                        index ++;

                        if (index == n) {
                            TextView PriceText = findViewById(R.id.textView_price);
                            String priceStr = Integer.toString(price);
                            priceStr = priceStr.concat(" 원");
                            PriceText.setText(priceStr);

                            index = 0;
                        }
                    }
                }
                @Override
                public void onFailure(Call<Food> call, Throwable t) {}
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_btn_cart:
                // playBtn();
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onRecievedData(Object data) {
        print = false;
        displayItems();
    }
}
