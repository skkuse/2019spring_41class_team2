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

public class OrderPage extends AppCompatActivity {

    EditText editText = findViewById(R.id.addressEdit);
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        button = findViewById(R.id.addressButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DaumWebViewActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data){

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String a = data.getStringExtra("address");
                editText.setText(a);
            }
        }
    }
}
