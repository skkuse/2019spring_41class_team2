package com.skkuseteam2.eatit;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class User {

    private int id;
    private String nickname;
    private String name;
    private String profile_image;
    private Boolean evaluate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public Boolean getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(Boolean evaluate) {
        this.evaluate = evaluate;
    }
/*
    public Drawable drawableFromUrl(String url){

        BitmapDrawable drawable = new BitmapDrawable(Resources.getSystem());

        try {
            Bitmap x;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();

            InputStream input = connection.getInputStream();
            x = BitmapFactory.decodeStream(input);

            drawable = new BitmapDrawable(Resources.getSystem(), x);
        } catch (Exception e) {
            Log.e("eatit", "drawable convert error: ", e);
        }

        return drawable;
    }*/
}