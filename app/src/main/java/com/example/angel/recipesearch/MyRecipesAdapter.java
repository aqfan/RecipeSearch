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

public class MyRecipesAdapter extends
        RecyclerView.Adapter<MyRecipesAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image) ImageView imageView;
        @BindView(R.id.title) TextView titleView;
        @BindView(R.id.add_recipe) CircleButton addButton;
        @BindView(R.id.delete_recipe) CircleButton deleteButton;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

    }

    private List<Recipe> recipes;
    private Context context;

    public MyRecipesAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public MyRecipesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View feedView = inflater.inflate(R.layout.recipe_search_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(feedView);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyRecipesAdapter.ViewHolder viewHolder, final int position){
        final Recipe recipe = recipes.get(position);

        TextView titleView = viewHolder.titleView;
        titleView.setText(recipe.getTitle());

        ImageView imageView = viewHolder.imageView;
        Picasso.with(context).load(recipe.getImage()).into(imageView);

        viewHolder.addButton.setVisibility(View.INVISIBLE);
        viewHolder.deleteButton.setVisibility(View.VISIBLE);

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "https://spoonacular.com/recipes/"+recipe.getTitle()+"-"+recipe.getId();

                MainActivity activity = (MainActivity) context;
                activity.setURL(url);

                Fragment recipeInfoFragment = activity.getSupportFragmentManager().findFragmentByTag("fragment_recipe_info");
                Fragment myRecipesFragment = activity.getSupportFragmentManager().findFragmentByTag("fragment_my_recipes");
                activity.getSupportFragmentManager().beginTransaction().detach(recipeInfoFragment).commit();
                activity.getSupportFragmentManager().beginTransaction().attach(recipeInfoFragment).commit();

                activity.getSupportFragmentManager().beginTransaction().hide(myRecipesFragment).show(recipeInfoFragment).commit();

                activity.setCurrentFragment(recipeInfoFragment);
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity activity = (MainActivity) context;

                if (position < recipes.size()) {
                    String originalId = recipes.get(position).getId().toString();
                    recipes.remove(position);
                    SharedPreferences pref = context.getSharedPreferences("MyRecipes", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    String newIds = "";
                    String[] ids = pref.getString("recipes", null).split(";");
                    for(int i = 0; i < ids.length; i++) {
                        if(ids[i].equals(originalId)) {
                            editor.remove(originalId);
                            editor.remove(originalId+"_title");
                            editor.remove(originalId+"_image");
                        } else {
                            if (newIds.equals("")) {
                                newIds = ids[i];
                            } else {
                                newIds += ";"+ids[i];
                            }
                        }
                    }

                    editor.putString("recipes", newIds);
                    editor.apply();

                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, recipes.size());

                    Toast.makeText(context, "Recipe deleted!", Toast.LENGTH_SHORT);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
