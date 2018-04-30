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

public class SearchFragment extends Fragment {

    private RecyclerView recipeFeed;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchService mService;

    private ArrayList<Recipe> recipes = new ArrayList<>();

    private MainActivity activity;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        View rootView = inflater.inflate(R.layout.search_fragment, container, false);
        recipeFeed = (RecyclerView) rootView.findViewById(R.id.feed);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recipeFeed.setLayoutManager(layoutManager);

        mAdapter = new RecipeAdapter(recipes);
        recipeFeed.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SearchView searchItem = (SearchView) getView().findViewById(R.id.search_view);
        searchItem.setIconifiedByDefault(false);
        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("search query submit: "+query);
                getRecipeList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getRecipeList(String query) {
        SharedPreferences pref = activity.getSharedPreferences("SearchOptions", Context.MODE_PRIVATE);
        mService = APIUtils.getSearchService();

        if(pref.getBoolean("searchByIngredients", false)) {
            query = query.replaceAll(" ", ",").toLowerCase();
            mService.searchByIngredients(query,false, pref.getInt("maxSearchResults", 0),1).enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    System.out.println(response.toString());
                    int size = recipes.size();
                    recipes.clear();
                    mAdapter.notifyItemRangeRemoved(0, size);
                    for(Recipe item: response.body()) {
                        recipes.add(item);
                        mAdapter.notifyItemInserted(recipes.size() - 1);
                    }
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    System.out.println(t.toString());
                    Toast.makeText(getActivity(), "Unable to get recipes: "+t.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            mService.searchByQuery(pref.getInt("maxSearchResults", 0), query).enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    System.out.println(response.toString());
                    int size = recipes.size();
                    recipes.clear();
                    mAdapter.notifyItemRangeRemoved(0, size);
                    System.out.println(response.body());
                    for(Recipe item: response.body().getResults()) {
                        item.setImage("https://spoonacular.com/recipeImages/"  + item.getImage());
                        recipes.add(item);
                        mAdapter.notifyItemInserted(recipes.size() - 1);
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    System.out.println(t.toString());
                    Toast.makeText(getActivity(), "Unable to get recipes: "+t.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }

    }
}
