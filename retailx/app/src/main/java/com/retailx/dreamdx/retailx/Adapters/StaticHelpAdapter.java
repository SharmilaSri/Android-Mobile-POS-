package com.retailx.dreamdx.retailx.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.retailx.dreamdx.retailx.Fragments.HelpFragment;

public class StaticHelpAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;
    int flag=0;//default,1-for subscription firs page,2-subscription 2nd ,3 subscription 3

    public StaticHelpAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public StaticHelpAdapter(FragmentManager fragmentManager,int flag,int number) {
        super(fragmentManager);
        this.flag=flag;
        NUM_ITEMS=number;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        if (flag == 0) {//used in help menu
            switch (position) {
                case 0:
                    return HelpFragment.newInstance(0, "Page_1");
                case 1:
                    return HelpFragment.newInstance(1, "page_2");
                case 2:
                    return HelpFragment.newInstance(2, "page_3");
                default:
                    return null;
            }
        } else if (flag == 3) {//used in subscription panel
            switch (position) {
                case 0:
                    return HelpFragment.newInstance(0, "",3);
                case 1:
                    return HelpFragment.newInstance(1, "",3);
                case 2:
                    return HelpFragment.newInstance(2, "",3);
                case 3:
                    return HelpFragment.newInstance(3, "",3);
                default:
                    return null;
            }
        }else
            return null;


    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Page 1";
            case 1:
                return "Page 2";
            case 2:
                return "Page 3";
            case 3:
                return "Page 4";
            default:
                return "Page 5";
        }

    }
}
