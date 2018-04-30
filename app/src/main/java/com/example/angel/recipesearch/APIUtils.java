package com.example.angel.recipesearch;

public class APIUtils {

    public static final String BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";

    public static SearchService getSearchService() {
        return RetrofitClient.getClient(BASE_URL).create(SearchService.class);
    }
}
