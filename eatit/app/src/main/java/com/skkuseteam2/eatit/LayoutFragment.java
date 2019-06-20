package com.skkuseteam2.eatit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LayoutFragment extends android.support.v4.app.Fragment {

    private NetworkService networkService;
    private ApplicationController applicationController;
    Food fooddata;
    String imgurl = null;

    TextView item, cost, ingredient1, ingredient2, ingredient3, ingredient4;
    ImageView food;
    Bitmap bitmap;
    ImageButton cart;
    int i = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_layout, container, false);

        item = view.findViewById(R.id.textView);
        cost = view.findViewById(R.id.Cost);
        food = view.findViewById(R.id.imageView1);
        ingredient1 = view.findViewById(R.id.textView_M1);
        ingredient2 = view.findViewById(R.id.textView_M2);
        ingredient3 = view.findViewById(R.id.textView_M3);
        ingredient4 = view.findViewById(R.id.textView_M4);
        cart = view.findViewById(R.id.imageButton);

        Bundle args = getArguments();
        int fid = args.getInt("fid");

        // ip, port 연결, network 연결
        final ApplicationController application = ApplicationController.getInstance();
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

        cart.setImageResource(R.drawable.cart);
        cart.setScaleType(ImageButton.ScaleType.FIT_CENTER);
        cart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                i  = 1 - i;
                if(i == 1){
                    cart.setImageResource(R.drawable.cart_click);
                    Toast.makeText(getActivity(), "장바구니에 추가했습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    cart.setImageResource(R.drawable.cart);
                    Toast.makeText(getActivity(), "취소했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    /*
    public void onClick(View view){
        switch(view.getId()){
            case R.id.imageButton:
                cart.setSelected(true);
                break;
        }
    }
    */
}