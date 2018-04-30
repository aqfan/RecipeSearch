package com.example.angel.recipesearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ShoppingListFragment extends Fragment{


    private RecyclerView ingredientList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private EditText editName;
    private EditText editAmount;

    private Button addButton;
    private Button clearButton;
    private MainActivity activity;

    private ArrayList<Ingredient> ingredients = new ArrayList<>();

    public static ShoppingListFragment newInstance() {
        ShoppingListFragment fragment = new ShoppingListFragment();
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

        View rootView = inflater.inflate(R.layout.shopping_list_fragment, container, false);
        ingredientList = (RecyclerView) rootView.findViewById(R.id.ingredients_listView);
        editName = (EditText) rootView.findViewById(R.id.add_shopping_name);
        editAmount = (EditText) rootView.findViewById(R.id.add_shopping_amount);
        addButton = (Button) rootView.findViewById(R.id.add_shopping_button);
        clearButton = (Button) rootView.findViewById(R.id.clear_shopping_button);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        ingredientList.setLayoutManager(layoutManager);

        getIngredientsList();

        mAdapter = new ShoppingListAdapter(ingredients);
        ingredientList.setAdapter(mAdapter);

        createOnClickListeners();

        return rootView;
    }

    private void getIngredientsList() {
        SharedPreferences pref = activity.getSharedPreferences("MyShoppingList", Context.MODE_PRIVATE);

        if (!pref.contains("names")) {
            return;
        }

        String[] names = pref.getString("names", null).split(";");
        for(int i = 0; i < names.length; i++) {
            String name = names[i];
            if(!name.equals("")) {
                int amount = pref.getInt(name, 0);
                String exp = pref.getString(name + "_exp", null);
                ingredients.add(new Ingredient(name, amount, exp));
            }
        }
    }

    private void createOnClickListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = editName.getText().toString();
                String amountString = editAmount.getText().toString();
                int amount = 0;
                String exp = "";

                if (name.trim().length() == 0) {
                    Toast.makeText(getActivity(), "Must input item!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (amountString.matches("\\d+")) {
                    amount = Integer.parseInt(amountString);
                } else {
                    Toast.makeText(getActivity(), "Amount must be numeric!", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences pref = activity.getSharedPreferences("MyShoppingList", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                if(!pref.contains("names")) {
                    editor.putString("names", name);
                    editor.putInt(name, amount);
                    editor.putString(name+"_exp", exp);
                } else {
                    String names = pref.getString("names", null);
                    names += ";"+name;
                    editor.putInt(name, amount);
                    editor.putString(name+"_exp", exp);
                    editor.putString("names", names);
                }

                editor.apply();

                ingredients.add(new Ingredient(name, amount, exp));
                mAdapter.notifyItemInserted(ingredients.size() - 1);

                editName.getText().clear();
                editAmount.getText().clear();

                Toast.makeText(getActivity(), "Item added!", Toast.LENGTH_SHORT).show();

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences pref = activity.getSharedPreferences("MyShoppingList", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                editor.clear();
                editor.apply();

                int size = ingredients.size();
                ingredients.clear();
                mAdapter.notifyItemRangeRemoved(0, size);
                Toast.makeText(getActivity(), "Shopping list cleared!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
