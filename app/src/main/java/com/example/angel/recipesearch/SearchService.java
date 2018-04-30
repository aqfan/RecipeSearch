package com.example.angel.recipesearch;

import java.util.List;

import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Angel on 3/23/2018.
 */

public interface SearchService {

    // Search recipes by ingredients
    @Headers({
            "X-Mashape-Key: u8TfVRCjLvmsh0omU12W8iRozGoJp16QY6hjsnrGP6q8FE3KFs",
            "Accept: application/json"
    })

    @GET("findByIngredients?fillIngredients=false")
    Call<List<Recipe>> searchByIngredients(
            @Query("ingredients") String ingredients,
            @Query("limitLicense") boolean limitLicense,
            @Query("number") int number,
            @Query("ranking") int ranking
    );

    @Headers({
            "X-Mashape-Key: u8TfVRCjLvmsh0omU12W8iRozGoJp16QY6hjsnrGP6q8FE3KFs",
            "Accept: application/json"
    })

    @GET("search")
    Call<Result> searchByQuery(
            @Query("number") int number,
            @Query("query") String query
    );
}
