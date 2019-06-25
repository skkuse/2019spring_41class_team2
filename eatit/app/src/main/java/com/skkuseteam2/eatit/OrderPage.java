package com.skkuseteam2.eatit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.Button;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderPage extends AppCompatActivity {

    private NetworkService networkService;
    final private ApplicationController application = ApplicationController.getInstance();
    final int uid = application.getUserId();
    EditText editText;
    Button button;
    int price;
    String address;
    String phone;
    String items;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        getSupportActionBar().setElevation(0);

        // django network 연결
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("52.78.88.3",8080);
        networkService = ApplicationController.getInstance().getNetworkService();

        setPrice();
        setUser();

        // 버튼 클릭으로 주소찾기 불러오기
        editText = findViewById(R.id.addressEdit);
        button = findViewById(R.id.addressButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentout = new Intent(getApplicationContext(), DaumWebViewActivity.class);
                startActivityForResult(intentout, 1);
            }
        });

        // 결제 버튼
        Button buttonPay = findViewById(R.id.button_pay);
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address = editText.getText().toString();
                EditText editPhone = findViewById(R.id.phoneEdit);
                phone = editPhone.getText().toString();

                orderItem();
            }
        });
    }

    // DaumWebViewActivity로부터 주소찾기를 통해 얻은 data 받아오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data){

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String a = data.getStringExtra("address");
                editText.setText(a);

                orderItem();
            }
        }
    }

    protected void setPrice() {
        Intent intent = getIntent();
        price = intent.getExtras().getInt("price");
        TextView textView_price = findViewById(R.id.textView_price);
        String priceString = Integer.toString(price);
        textView_price.setText(priceString.concat(" 원"));
    }

    protected void setUser() {

        Call<User> userCall = networkService.getIdUser(uid);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();

                    // 이름 필드 셋팅
                    TextView textView_name = findViewById(R.id.textView_name);
                    textView_name.setText(user.getName());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) { }
        });
    }

    protected void orderItem() {

        Call<Cart> cartCall = networkService.getUidCart(uid);
        cartCall.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) {
                    Cart curCart = response.body();
                    items = curCart.getItems();

                    postOrder();
                }
            }
            @Override
            public void onFailure(Call<Cart> call, Throwable t) {}
        });
    }

    protected void postOrder() {

        Order order = new Order();
        order.setUid(uid);
        order.setItems(items);
        order.setPrice(price);
        order.setPhone(phone);
        order.setAddress(address);

        Call<Order> orderCall = networkService.post_order(order);
        orderCall.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "주문 완료", Toast.LENGTH_SHORT).show();

                    deleteCart();

                    Intent outIntent = new Intent(getApplicationContext(), super.getClass());
                    setResult(RESULT_OK, outIntent);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<Order> call, Throwable t) {}
        });
    }

    protected void deleteCart() {

        Call<Cart> cartDeleteCall = networkService.delete_cart(uid);
        cartDeleteCall.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) {
                    System.out.println("cart 삭제");
                }
            }
            @Override
            public void onFailure(Call<Cart> call, Throwable t) {}
        });
    }
}
