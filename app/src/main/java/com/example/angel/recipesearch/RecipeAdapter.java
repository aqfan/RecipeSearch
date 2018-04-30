package com.example.angel.recipesearch;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Angel on 4/16/2018.
 */

public class RecipeAdapter extends
        RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image) ImageView imageView;
        @BindView(R.id.title) TextView titleView;
        @BindView(R.id.add_recipe) CircleButton addButton;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

    }

    private List<Recipe> recipes;
    private Context context;

    public RecipeAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View feedView = inflater.inflate(R.layout.recipe_search_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(feedView);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder viewHolder, final int position){
        final Recipe recipe = recipes.get(position);

        TextView titleView = viewHolder.titleView;
        titleView.setText(recipe.getTitle());

        ImageView imageView = viewHolder.imageView;
        Picasso.with(context).load(recipe.getImage()).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "https://spoonacular.com/recipes/"+recipe.getTitle()+"-"+recipe.getId();

                MainActivity activity = (MainActivity) context;
                activity.setURL(url);

                Fragment recipeInfoFragment = activity.getSupportFragmentManager().findFragmentByTag("fragment_recipe_info");
                Fragment searchFragment = activity.getSupportFragmentManager().findFragmentByTag("fragment_search");
                activity.getSupportFragmentManager().beginTransaction().detach(recipeInfoFragment).commitAllowingStateLoss();
                activity.getSupportFragmentManager().beginTransaction().attach(recipeInfoFragment).commitAllowingStateLoss();

                activity.getSupportFragmentManager().beginTransaction().hide(searchFragment).show(recipeInfoFragment).commit();

                activity.setCurrentFragment(recipeInfoFragment);
            }
        });

        viewHolder.addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity activity = (MainActivity) context;

                SharedPreferences pref = context.getSharedPreferences("MyRecipes", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                if(!pref.contains("recipes")) {
                    editor.putString("recipes", recipe.getId().toString());
                    editor.putString(recipe.getId()+"_title", recipe.getTitle());
                    editor.putString(recipe.getId()+"_image", recipe.getImage());
                } else {
                    String ids = pref.getString("recipes", null);
                    ids += ";"+recipe.getId().toString();
                    editor.putString(recipe.getId()+"_title", recipe.getTitle());
                    editor.putString(recipe.getId()+"_image", recipe.getImage());
                    editor.putString("recipes", ids);
                }

                Fragment myRecipesFragment = activity.getSupportFragmentManager().findFragmentByTag("fragment_my_recipes");
                activity.getSupportFragmentManager().beginTransaction().detach(myRecipesFragment).commit();
                activity.getSupportFragmentManager().beginTransaction().attach(myRecipesFragment).commit();

                editor.apply();

                Toast.makeText(context, "Recipe saved!", Toast.LENGTH_SHORT);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
