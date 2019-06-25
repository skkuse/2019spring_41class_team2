package com.skkuseteam2.eatit;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LayoutFragment extends android.support.v4.app.Fragment {

    public interface OnMyListener {
        void onRecievedData(Object data);
    }

    private OnMyListener mOnMyListener;

    private NetworkService networkService;
    final private ApplicationController application = ApplicationController.getInstance();
    Food fooddata;
    String imgurl = null;
    int uid;

    ConstraintLayout thisLayout;

    TextView item, cost, ingredient1, ingredient2, ingredient3, ingredient4;
    ImageView food;
    Bitmap bitmap;
    ImageButton imgButton;
    int i = 1;

    Cart cart;
    int fid;
    boolean isX;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_layout, container, false);

        thisLayout = view.findViewById(R.id.constraintLayout_item);
        item = view.findViewById(R.id.textView);
        cost = view.findViewById(R.id.Cost);
        food = view.findViewById(R.id.imageView1);
        ingredient1 = view.findViewById(R.id.textView_M1);
        ingredient2 = view.findViewById(R.id.textView_M2);
        ingredient3 = view.findViewById(R.id.textView_M3);
        ingredient4 = view.findViewById(R.id.textView_M4);
        imgButton = view.findViewById(R.id.imageButton);

        Bundle args = getArguments();
        fid = args.getInt("fid");
        isX = args.getBoolean("isX");

        // ip, port 연결, network 연결

        application.buildNetworkService("52.78.88.3",8080);
        networkService = ApplicationController.getInstance().getNetworkService();

        Call<Food> foodCall = networkService.getIdFood(fid);
        foodCall.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (response.isSuccessful()) {
                    fooddata = response.body();
//                    System.out.println("이름: "+fooddata.getName());

                    item.setText(fooddata.getName());
                    cost.setText(String.valueOf(fooddata.getPrice()).concat("원"));
                    imgurl = fooddata.getPhoto();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    try{
                        URL url = new URL("http://52.78.88.3:8080/media/40.jpg");
                        if (imgurl != null) {
                            url = new URL(imgurl);
                        }

                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);

                        food.setImageBitmap(bitmap);
                    }catch(Exception e) {
                        e.printStackTrace();
                    }

                    final Call<List<Ingredient>> ingredientCall = networkService.getAllIngredient();
                    ingredientCall.enqueue(new Callback<List<Ingredient>>() {
                        @Override
                        public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                            List<Ingredient> ingredients = response.body();
                            int numing = 0;
                            for (Ingredient ingredient : ingredients) {
//                                System.out.println(ingredient.getName()+ingredient.getFid());
                                if (ingredient.getFid() == fooddata.getId()) {
                                    TextView curtext = ingredient1;
                                    switch(numing) {
                                        case 1:
                                            curtext = ingredient2;
                                            break;
                                        case 2:
                                            curtext = ingredient3;
                                            break;
                                        case 3:
                                            curtext = ingredient4;
                                            break;
                                    }
                                    curtext.setVisibility(View.VISIBLE);
                                    curtext.setText(ingredient.getName());
                                    numing++;
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<List<Ingredient>> call, Throwable t) {}
                    });

                } else {
                    int statusCode = response.code();
                    Log.i("MyTag", "응답코드 : " + statusCode);
                }
            }
            @Override
            public void onFailure(Call<Food> call, Throwable t) { }
        });

        if (isX) {
            imgButton.setImageResource(R.drawable.x);
            imgButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            imgButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view){
                    uid = application.getUserId();
                    deleteitem();
                }
            });
        } else {
            imgButton.setImageResource(R.drawable.cart);
            imgButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            imgButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view){

                    uid = application.getUserId();

                    if(i == 1){
                        insertitem();
                    }
                    else{
                        deleteitem();
                    }
                }
            });
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(getActivity() != null && getActivity() instanceof OnMyListener) {
            mOnMyListener = (OnMyListener) getActivity();

        }
    }

    private void insertitem() {
        Call<Cart> cartCall = networkService.getUidCart(uid);
        cartCall.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) {  //  이미 cart가 존재하는 경우 -> 그냥 추가
                    cart = response.body();

                    List<String> items = new ArrayList<>();

                    if (cart.getItems() != null) {
                        String[] itemArray = cart.getItems().split(",");
                        for (int i=0; i<itemArray.length; i++)
                            items.add(itemArray[i]);
                    }

                    String fid = Integer.toString(fooddata.getId());
                    if (!items.contains(fid)) {
                        cart.setSize(cart.getSize() + 1);
                        items.add(fid);

                        cart.setItems(String.join(",", items));
                        Call<Cart> cartPatchCall = networkService.patch_cart(uid, cart);
                        cartPatchCall.enqueue(new Callback<Cart>() {
                            @Override
                            public void onResponse(Call<Cart> call, Response<Cart> response) {
                                if (response.isSuccessful()) {
                                    imgButton.setImageResource(R.drawable.cart_clicked);
                                    i = 0;
                                    Toast.makeText(getActivity(), "장바구니에 추가했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Cart> call, Throwable t) {}
                        });
                    } else {
                        Toast.makeText(getActivity(), "이미 장바구니에 존재합니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {    // cart가 존재하지 않는 경우 -> 생성

                    cart = new Cart();
                    cart.setUid(uid);
                    cart.setSize(1);
                    String string = Integer.toString(fid);
                    cart.setItems(string);
                    Call<Cart> cartPostCall = networkService.post_cart(cart);
                    cartPostCall.enqueue(new Callback<Cart>() {
                        @Override
                        public void onResponse(Call<Cart> call, Response<Cart> response) {
                            if (response.isSuccessful()) {
                                imgButton.setImageResource(R.drawable.cart_clicked);
                                i = 0;
                                Toast.makeText(getActivity(), "장바구니에 추가했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Cart> call, Throwable t) {}
                    });
                }
            }
            @Override
            public void onFailure(Call<Cart> call, Throwable t) {}
        });
    }

    private void deleteitem() {
        Call<Cart> cartCall = networkService.getUidCart(uid);
        cartCall.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) {
                    cart = response.body();

                    List<String> items = new ArrayList<>();

                    String[] itemArray = cart.getItems().split(",");
                    for (int i=0; i<itemArray.length; i++)
                        items.add(itemArray[i]);

                    String fid = Integer.toString(fooddata.getId());
                    items.remove(fid);

                    String itemStr = String.join(",", items);
                    if (itemStr.isEmpty()) {
                        Call<Cart> cartDeleteCall = networkService.delete_cart(uid);
                        cartDeleteCall.enqueue(new Callback<Cart>() {
                            @Override
                            public void onResponse(Call<Cart> call, Response<Cart> response) {
                                if (response.isSuccessful()) {
                                    if (isX){
                                        Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                        thisLayout.setVisibility(View.GONE);
                                        mOnMyListener.onRecievedData("delete");
                                        return;
                                    }
                                    imgButton.setImageResource(R.drawable.cart);
                                    i = 1;
                                    Toast.makeText(getActivity(), "취소했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Cart> call, Throwable t) {}
                        });
                    } else {
                        cart.setSize(cart.getSize() - 1);
                        cart.setItems(itemStr);
                        Call<Cart> cartPatchCall = networkService.patch_cart(uid, cart);
                        cartPatchCall.enqueue(new Callback<Cart>() {
                            @Override
                            public void onResponse(Call<Cart> call, Response<Cart> response) {
                                if (response.isSuccessful()) {
                                    if (isX){
                                        Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                        thisLayout.setVisibility(View.GONE);
                                        mOnMyListener.onRecievedData("delete");
                                        return;
                                    }
                                    imgButton.setImageResource(R.drawable.cart);
                                    i = 1;
                                    Toast.makeText(getActivity(), "취소했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Cart> call, Throwable t) {}
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<Cart> call, Throwable t) {}
        });
    }
}