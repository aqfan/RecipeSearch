package com.example.angel.recipesearch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {
    private EditText maxSearchResults;
    private Button saveSearchOptions;
    private MainActivity activity;
    private Spinner spinner;

    private String searchOption;
    ArrayAdapter<String> dataAdapter;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
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

        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);
        maxSearchResults = (EditText) rootView.findViewById(R.id.maxNumResults);
        saveSearchOptions = (Button) rootView.findViewById(R.id.save_search_options);
        spinner = (Spinner) rootView.findViewById(R.id.spinner);

        List<String> categories = new ArrayList<String>();
        categories.add("By Recipe Name");
        categories.add("By Ingredients");
        dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, categories);
        spinner.setAdapter(dataAdapter);

        SharedPreferences pref = activity.getSharedPreferences("SearchOptions", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if(pref.getBoolean("searchByIngredients", false)) {
            spinner.setSelection(1);
        } else {
            spinner.setSelection(0);
        }

        maxSearchResults.setText("" + pref.getInt("maxSearchResults", 0));

        createOnClickListeners();
        return rootView;
    }

    private void createOnClickListeners() {
        saveSearchOptions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences pref = activity.getSharedPreferences("SearchOptions", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                String maxResults = maxSearchResults.getText().toString();

                if(!maxResults.equals("")) {
                    if (Integer.parseInt(maxResults) < 1 || Integer.parseInt(maxResults) > 1000) {
                        Toast.makeText(activity, "Max search results must be between 1 and 1000!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        editor.putInt("maxSearchResults", Integer.parseInt(maxResults));
                    }
                } else {
                    String temp = "" + pref.getInt("maxSearchResults", 0);
                    maxSearchResults.setText(temp);
                }

                if(searchOption.equals("By Recipe Name")) {
                    editor.putBoolean("searchByIngredients", false);
                    editor.putBoolean("searchByRecipe", true);
                    editor.apply();
                } else {
                    editor.putBoolean("searchByIngredients", true);
                    editor.putBoolean("searchByRecipe", false);
                    editor.apply();
                }
                Toast.makeText(getActivity(), "Options saved!", Toast.LENGTH_SHORT).show();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchOption = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}