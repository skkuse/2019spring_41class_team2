package com.skkuseteam2.eatit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;


public class RecommendationFragment extends android.support.v4.app.Fragment {

    int count = 0;
    Button button;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recommendation, container, false);

        // 버튼 클릭으로 OrderPage 불러오기
        button = view.findViewById(R.id.oderPage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderPage.class);
                startActivity(intent);
            }
        });

        // 프래그먼트 추가
        while (count < 10) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.Sub_Linear, new LayoutFragment());
            fragmentTransaction.commit();

            count++;
        }


        return view;
    }

}