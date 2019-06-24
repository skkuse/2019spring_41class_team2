package com.skkuseteam2.eatit;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends android.support.v4.app.Fragment {

    private NetworkService networkService;
    private ApplicationController applicationController;

    FragmentManager fragmentManager;
    boolean hasprinted = false;

    ArrayList<Integer> fid = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        fragmentManager = getFragmentManager();

        // django network 연결
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("52.78.88.3",8080);
        networkService = ApplicationController.getInstance().getNetworkService();

        final EditText editText = view.findViewById(R.id.search_input);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                switch (i) {
                    case EditorInfo
                            .IME_ACTION_SEARCH:
                        //  검색 기록이 있으면 아이템들 삭제
                        if (hasprinted) {
                            List<Fragment> fragments = fragmentManager.getFragments();
                            for (Fragment fragment : fragments)
                                if (fragment.toString().contains("LayoutFragment"))
                                    fragmentManager.beginTransaction().remove(fragment).commit();
                            hasprinted = false;
                        }

                        String input = editText.getText().toString();

                        if (input.isEmpty()) {
                            System.out.println("empty");
                            break;
                        }

                        findItems(input);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        return view;
    }

    private void findItems(final String input) {

        fid.clear();

        Call<List<Food>> foodCall = networkService.getAllFood();
        foodCall.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful()) {
                    List<Food> foods = response.body();
                    //  사용자가 입력한 단어를 포함하는 음식들 찾아서 fid 리스트에 추가
                    for (Food food : foods) {
                        String curString = food.getName();
                        if (curString.contains(input))
                            fid.add(food.getId());
                    }

                    //  리스트를 어레이로 변환
                    int n = fid.size();
                    int[] result = new int[n];
                    Integer[] temp = fid.toArray(new Integer[n]);
                    for (int i=0; i<n; ++i)
                        result[i] = temp[i];

                    if (n > 0)
                        hasprinted = true;

                    // 검색된 아이템들 프린트
                    printItems(result);

                }else {
                    int statusCode = response.code();
                    Log.i("MyTag", "응답코드 : " + statusCode);
                }
            }
            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) { }
        });
    }

    private void printItems(int[] fid) {

        int n = fid.length;

        // 프래그먼트 추가
        int count = 0;
        while (count < n) {
            Bundle args = new Bundle();
            args.putInt("fid", fid[count]);

            Fragment fragment = new LayoutFragment();
            fragment.setArguments(args);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.Search_Linear, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            count++;
        }
    }
}

