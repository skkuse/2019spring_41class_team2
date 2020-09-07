package com.skkuseteam2.eatit;

import java.util.List;
import okhttp3.RequestBody;
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

    @POST("/versions/")
    Call<Version> post_version(@Body Version version);

    @GET("/versions/")
    Call<List<Version>> getAllVersion();

    @GET("/versions/{version}/")
    Call<Version> getVersionVersion(@Path("version") String version);

    @POST("/users/")
    Call<User> post_user(@Body User user);

    @GET("/users/{id}/")
    Call<User> getIdUser(@Path("id") int id);

    @PATCH("/users/{id}/")
    Call<User> patchEvalUser(@Path("id") int id, @Body User user);

    @GET("/foods/")
    Call<List<Food>> getAllFood();

    @GET("/foods/{id}/")
    Call<Food> getIdFood(@Path("id") int id);

    @GET("/ingredients")
    Call<List<Ingredient>> getAllIngredient();

    @GET("/carts/{uid}/")
    Call<Cart> getUidCart(@Path("uid") int uid);

    @POST("/carts/")
    Call<Cart> post_cart(@Body Cart cart);

    @PATCH("/carts/{uid}/")
    Call<Cart> patch_cart(@Path("uid") int uid, @Body Cart cart);

    @DELETE("/carts/{uid}/")
    Call<Cart> delete_cart(@Path("uid") int uid);

    @POST("/orders/")
    Call<Order> post_order(@Body Order order);

    @GET("/orders/")
    Call<List<Order>> getAllOrder();

    @GET("/orders/{id}/")
    Call<Order> getIdOrder(@Path("id") int id);

    @DELETE("/orders/{id}/")
    Call<Order> delete_order(@Path("id") int id);
}
