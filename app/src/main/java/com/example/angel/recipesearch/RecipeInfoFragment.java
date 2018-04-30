package com.example.angel.recipesearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class RecipeInfoFragment extends Fragment {
    private String url;
    UrlInterface mCallback;

    public static RecipeInfoFragment newInstance() {
        RecipeInfoFragment fragment = new RecipeInfoFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (UrlInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DataCommunication");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_info_fragment, container, false);
        WebView webView = (WebView) rootView.findViewById(R.id.webview);

        if(mCallback.getURL() != null) {
            webView.loadUrl(mCallback.getURL());
        } else {
            webView.loadUrl("https://spoonacular.com/recipes/Pan%20Seared%20Oven%20Roasted%20Steaks-985185");
        }
        return rootView;
    }

}
