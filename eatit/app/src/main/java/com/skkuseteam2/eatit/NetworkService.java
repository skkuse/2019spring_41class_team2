package com.skkuseteam2.eatit;

import java.util.List;
import okhttp3.RequestBody;
import okhttp3.internal.Version;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface NetworkService {

    @POST("/eatitapp/versions/")
    Call<Version> post_version(@Body Version version);

    @PATCH("/eatitapp/versions/{pk}/")
    Call<Version> patch_version(@Path("pk") int pk, @Body Version version);

    @DELETE("/eatitapp/versions/{pk}/")
    Call<Version> delete_version(@Path("pk") int pk);

    @GET("/eatitapp/versions/")
    Call<List<Version>> get_version();

    @GET("/eatitapp/versions/{pk}/")
    Call<Version> get_pk_version(@Path("pk") int pk);
}
