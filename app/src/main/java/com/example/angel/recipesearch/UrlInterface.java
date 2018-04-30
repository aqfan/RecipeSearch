package com.example.angel.recipesearch;

import android.support.v4.app.Fragment;

/**
 * Created by Angel on 4/17/2018.
 */

public interface UrlInterface {
    public void setURL(String url);
    public String getURL();
    public void setCurrentFragment(Fragment fragment);
}
