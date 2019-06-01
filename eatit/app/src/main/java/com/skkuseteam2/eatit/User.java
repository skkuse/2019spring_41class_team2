package com.skkuseteam2.eatit;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class User {

    private String id;
    private String nickname;
    private String name;
    private String imgUrl;
    private Drawable profile_image;
    private Boolean evaluate;

    private Drawable drawableFromUrl(String url) throws IOException {

        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();

        InputStream input = connection.getInputStream();
        x = BitmapFactory.decodeStream(input);

        return new BitmapDrawable(Resources.getSystem(), x);
    }
}