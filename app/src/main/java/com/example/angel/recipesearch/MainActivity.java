package com.example.angel.recipesearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements UrlInterface {

    private static final String TAG_HOME_FRAGMENT = "fragment_home";
    private static final String TAG_SEARCH_FRAGMENT = "fragment_search";
    private static final String TAG_SETTINGS_FRAGMENT = "fragment_settings";
    private static final String TAG_RECIPE_INFO_FRAGMENT = "fragment_recipe_info";
    private static final String TAG_MY_FRIDGE_FRAGMENT = "fragment_my_fridge";
    private static final String TAG_SHOPPING_LIST_FRAGMENT = "fragment_shopping_list";
    private static final String TAG_MY_RECIPES_FRAGMENT = "fragment_my_recipes";

    private final HomeFragment homeFragment = new HomeFragment();
    private final SearchFragment searchFragment = new SearchFragment();
    private final SettingsFragment settingsFragment = new SettingsFragment();
    private final RecipeInfoFragment recipeInfoFragment = new RecipeInfoFragment();
    private final MyFridgeFragment myFridgeFragment = new MyFridgeFragment();
    private final ShoppingListFragment shoppingListFragment = new ShoppingListFragment();
    private final MyRecipesFragment myRecipesFragment = new MyRecipesFragment();

    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    private String URL;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(currentFragment != homeFragment) {
                        fragmentManager.beginTransaction().hide(currentFragment).show(homeFragment).commit();
                    }
                    currentFragment = homeFragment;
                    break;
                case R.id.navigation_search:
                    if(currentFragment != searchFragment) {
                        fragmentManager.beginTransaction().hide(currentFragment).show(searchFragment).commit();
                    }
                    currentFragment = searchFragment;
                    break;
                case R.id.navigation_settings:
                    if(currentFragment != settingsFragment) {
                        fragmentManager.beginTransaction().hide(currentFragment).show(settingsFragment).commit();
                    }
                    currentFragment = settingsFragment;
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = this.getSharedPreferences("SearchOptions", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if(!pref.contains("searchByRecipe") && !pref.contains("searchByIngredients")) {
            editor.putBoolean("searchByRecipe", true);
            editor.putBoolean("searchByIngredients", false);
            editor.apply();
        }

        if(!pref.contains("maxSearchResults")) {
            editor.putInt("maxSearchResults", 10);
            editor.apply();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();
        currentFragment = homeFragment;

        fragmentManager.beginTransaction().add(R.id.frame_layout, homeFragment,TAG_HOME_FRAGMENT).commit();
        fragmentManager.beginTransaction().add(R.id.frame_layout, searchFragment,TAG_SEARCH_FRAGMENT).commit();
        fragmentManager.beginTransaction().add(R.id.frame_layout, settingsFragment,TAG_SETTINGS_FRAGMENT).commit();
        fragmentManager.beginTransaction().add(R.id.frame_layout, recipeInfoFragment, TAG_RECIPE_INFO_FRAGMENT).commit();
        fragmentManager.beginTransaction().add(R.id.frame_layout, myFridgeFragment, TAG_MY_FRIDGE_FRAGMENT).commit();
        fragmentManager.beginTransaction().add(R.id.frame_layout, shoppingListFragment, TAG_SHOPPING_LIST_FRAGMENT).commit();
        fragmentManager.beginTransaction().add(R.id.frame_layout, myRecipesFragment, TAG_MY_RECIPES_FRAGMENT).commit();

        fragmentManager.beginTransaction()
                .hide(settingsFragment)
                .hide(searchFragment)
                .hide(recipeInfoFragment)
                .hide(myFridgeFragment)
                .hide(shoppingListFragment)
                .hide(myRecipesFragment)
                .show(homeFragment)
                .commit();
    }

    public void setURL(String url) {
        URL = url;
    }

    public String getURL() {
        return URL;
    }

    public void setCurrentFragment(Fragment fragment) {
        currentFragment = fragment;
    }

}