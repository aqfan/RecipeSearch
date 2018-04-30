package com.example.angel.recipesearch;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Angel on 4/16/2018.
 */

public class ShoppingListAdapter extends
        RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredient_name) EditText ingredientName;
        @BindView(R.id.ingredient_amount) EditText ingredientAmount;
        @BindView(R.id.ingredient_expiration) EditText ingredientExpiration;
        @BindView(R.id.edit_ingredient) ImageButton editIngredient;
        @BindView(R.id.save_ingredient) ImageButton saveIngredient;
        @BindView(R.id.delete_ingredient) ImageButton deleteIngredient;
        @BindView(R.id.shopping_checkbox) CheckBox checkBox;


        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

    }

    private List<Ingredient> ingredients;
    private Context context;

    public ShoppingListAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View feedView = inflater.inflate(R.layout.ingredients_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(feedView);
        viewHolder.checkBox.setVisibility(View.VISIBLE);
        viewHolder.deleteIngredient.setVisibility(View.INVISIBLE);
        viewHolder.ingredientExpiration.setVisibility(View.INVISIBLE);
        context = parent.getContext();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ShoppingListAdapter.ViewHolder viewHolder, final int position){
        final Ingredient ingredient = ingredients.get(position);

        final EditText nameView = viewHolder.ingredientName;
        nameView.setText(ingredient.getName());
        nameView.setFocusable(false);
        nameView.setFocusableInTouchMode(false);

        final EditText amountView = viewHolder.ingredientAmount;
        amountView.setText(String.valueOf(ingredient.getAmount()));
        amountView.setFocusable(false);
        amountView.setFocusableInTouchMode(false);

        final EditText expirationView = viewHolder.ingredientExpiration;

        SharedPreferences pref = context.getSharedPreferences("MyShoppingList", Context.MODE_PRIVATE);
        viewHolder.checkBox.setChecked(pref.getBoolean(nameView.getText().toString()+"_checked", false));

        viewHolder.editIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameView.setFocusable(true);
                nameView.setFocusableInTouchMode(true);

                amountView.setFocusable(true);
                amountView.setFocusableInTouchMode(true);

                view.setVisibility(View.INVISIBLE);
                viewHolder.saveIngredient.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.saveIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameView.setFocusable(false);
                nameView.setFocusableInTouchMode(false);

                amountView.setFocusable(false);
                amountView.setFocusableInTouchMode(false);

                String originalName = ingredient.getName();
                String newName = nameView.getText().toString();

                SharedPreferences pref = context.getSharedPreferences("MyShoppingList", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                String newNames = "";
                String[] names = pref.getString("names", null).split(";");
                for(int i = 0; i < names.length; i++) {
                    if(names[i].equals(originalName)) {
                        names[i] = nameView.getText().toString();
                        editor.remove(originalName);
                        editor.remove(originalName+"_exp");
                        editor.putInt(newName, Integer.parseInt(amountView.getText().toString()));
                        editor.putString(newName+"_exp", ingredient.getExpirationDate());
                    }

                    if (i == 0) {
                        newNames = names[i];
                    } else {
                        newNames += ";"+names[i];
                    }
                }

                editor.putString("names", newNames);
                editor.apply();

                ingredient.setName(nameView.getText().toString());

                ingredient.setAmount(Integer.parseInt(amountView.getText().toString()));
                ingredient.setExpirationDate(expirationView.getText().toString());

                notifyDataSetChanged();

                view.setVisibility(View.INVISIBLE);
                viewHolder.editIngredient.setVisibility(View.VISIBLE);

                Toast.makeText(context, "Edits saved!", Toast.LENGTH_SHORT).show();

            }
        });

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.checkBox.isChecked()) {
                    nameView.setTextColor((0xff & 0xff) << 24 | (0xc0 & 0xff) << 16 | (0xc0 & 0xff) << 8 | (0xc0 & 0xff));
                    amountView.setTextColor((0xff & 0xff) << 24 | (0xc0 & 0xff) << 16 | (0xc0 & 0xff) << 8 | (0xc0 & 0xff));
                    nameView.setPaintFlags(nameView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    amountView.setPaintFlags(amountView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    SharedPreferences pref = context.getSharedPreferences("MyShoppingList", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(nameView.getText().toString()+"_checked", true).apply();

                } else {
                    nameView.setTextColor((0xff & 0xff) << 24 | (0x00 & 0xff) << 16 | (0x00 & 0xff) << 8 | (0x00 & 0xff));
                    amountView.setTextColor((0xff & 0xff) << 24 | (0x00 & 0xff) << 16 | (0x00 & 0xff) << 8 | (0x00 & 0xff));
                    nameView.setPaintFlags(nameView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    amountView.setPaintFlags(amountView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    SharedPreferences pref = context.getSharedPreferences("MyShoppingList", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(nameView.getText().toString()+"_checked", false).apply();
                }
            }
        });

        nameView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && viewHolder.saveIngredient.getVisibility() == View.VISIBLE &&
                        !(viewHolder.editIngredient.hasFocus() || viewHolder.deleteIngredient.hasFocus() || viewHolder.saveIngredient.hasFocus()
                                || amountView.hasFocus() || expirationView.hasFocus())) {
                    Toast.makeText(context, "Please save your edits first!", Toast.LENGTH_SHORT).show();
                    view.requestFocus();
                }
            }
        });

        amountView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && viewHolder.saveIngredient.getVisibility() == View.VISIBLE &&
                        !(viewHolder.editIngredient.hasFocus() || viewHolder.deleteIngredient.hasFocus() || viewHolder.saveIngredient.hasFocus()
                                || nameView.hasFocus() || expirationView.hasFocus())) {
                    Toast.makeText(context, "Please save your edits first!", Toast.LENGTH_SHORT).show();
                    view.requestFocus();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
