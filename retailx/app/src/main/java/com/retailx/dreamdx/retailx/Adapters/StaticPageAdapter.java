package com.retailx.dreamdx.retailx.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.retailx.dreamdx.retailx.Fragments.CategoryListFragment;
import com.retailx.dreamdx.retailx.Fragments.ProductListFragment;

public class StaticPageAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;

    public StaticPageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }




    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ProductListFragment.newInstance(0, "PRODUCTS");
            case 1:
                return ProductListFragment.newInstance(1, "STOCK");
            case 2:
                return CategoryListFragment.newInstance(2, "CATEGORIES");
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "PRODUCTS";
            case 1:
                return "STOCK";
            case 2:
                return "CATEGORIES";
            default:
                return "";
        }

    }
}
