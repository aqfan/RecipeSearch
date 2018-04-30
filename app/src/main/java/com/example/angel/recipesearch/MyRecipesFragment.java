package com.example.angel.recipesearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRecipesFragment extends Fragment {

    private RecyclerView recipeFeed;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Recipe> recipes = new ArrayList<>();

    private MainActivity activity;

    public static MyRecipesFragment newInstance() {
        MyRecipesFragment fragment = new MyRecipesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_recipes_fragment, container, false);
        activity = (MainActivity) getActivity();

        recipeFeed = (RecyclerView) rootView.findViewById(R.id.my_recipe_feed);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recipeFeed.setLayoutManager(layoutManager);

        getRecipeList();

        mAdapter = new MyRecipesAdapter(recipes);
        recipeFeed.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    private void getRecipeList() {
        SharedPreferences pref = activity.getSharedPreferences("MyRecipes", Context.MODE_PRIVATE);

        if (!pref.contains("recipes")) {
            return;
        }

        String[] ids = pref.getString("recipes", null).split(";");
        for(int i = 0; i < ids.length; i++) {
            String id = ids[i];
            if(!id.equals("")) {
                String title = pref.getString(id + "_title", null);
                String image = pref.getString(id + "_image", null);
                recipes.add(new Recipe(Integer.parseInt(id), title, image));
            }
        }
    }
}
