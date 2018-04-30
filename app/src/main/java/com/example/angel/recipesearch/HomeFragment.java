package com.example.angel.recipesearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class HomeFragment extends Fragment {

    private RelativeLayout myFridge;
    private RelativeLayout myRecipes;
    private RelativeLayout shoppingList;
    private MainActivity activity;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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

        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        myFridge = (RelativeLayout) rootView.findViewById(R.id.my_fridge);
        myRecipes = (RelativeLayout) rootView.findViewById(R.id.my_recipes);
        shoppingList = (RelativeLayout) rootView.findViewById(R.id.shopping_list);

        createOnClickListeners();

        return rootView;
    }

    private void createOnClickListeners() {
        myFridge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment myFridgeFragment = activity.getSupportFragmentManager().findFragmentByTag("fragment_my_fridge");
                Fragment homeFragment = activity.getSupportFragmentManager().findFragmentByTag("fragment_home");
                activity.getSupportFragmentManager().beginTransaction().hide(homeFragment).show(myFridgeFragment).commit();
                activity.setCurrentFragment(myFridgeFragment);
            }
        });

        myRecipes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment myRecipesFragment = activity.getSupportFragmentManager().findFragmentByTag("fragment_my_recipes");
                Fragment homeFragment = activity.getSupportFragmentManager().findFragmentByTag("fragment_home");
                Fragment infoFragment = activity.getSupportFragmentManager().findFragmentByTag("fragment_recipe_info");
                activity.getSupportFragmentManager().beginTransaction().hide(homeFragment).hide(infoFragment).show(myRecipesFragment).commit();
                activity.setCurrentFragment(myRecipesFragment);
            }
        });

        shoppingList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment shoppingListFragment = activity.getSupportFragmentManager().findFragmentByTag("fragment_shopping_list");
                Fragment homeFragment = activity.getSupportFragmentManager().findFragmentByTag("fragment_home");
                activity.getSupportFragmentManager().beginTransaction().hide(homeFragment).show(shoppingListFragment).commit();
                activity.setCurrentFragment(shoppingListFragment);
            }
        });
    }
}