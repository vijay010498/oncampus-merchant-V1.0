package com.svijayr007.oncampuspartner.ui.orders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class TabsSetup extends FragmentPagerAdapter {
    String[] tabsTitle;
    List<Fragment> simpleTabList;

    public TabsSetup(List<Fragment> simpleTabList, FragmentManager fragmentManager, String[] tabsTitle){
        super(fragmentManager);
        this.simpleTabList = simpleTabList;
        this.tabsTitle = tabsTitle;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return simpleTabList.get(position);
    }

    @Override
    public int getCount() {
        return simpleTabList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(tabsTitle.length == 0){
            return null;
        }
        return tabsTitle[position];
    }
}
