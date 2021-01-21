package com.retailx.dreamdx.retailx.Fragments;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class FragmentMainPagerAdaptor extends FragmentPagerAdapter{

int count;
String title="";
Fragment fragment;
    public FragmentMainPagerAdaptor(FragmentManager fragmentManager,int count,Fragment fragment) {
        super(fragmentManager);
        this.count=count;
        this.fragment=fragment;
    }

    @Override
    public Fragment getItem(int i) {

        return fragment;
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}
