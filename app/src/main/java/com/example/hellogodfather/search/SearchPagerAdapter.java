package com.example.hellogodfather.search;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;


/*
    @author: Peng Zhao
 */
public class SearchPagerAdapter extends FragmentStateAdapter {
    public SearchPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1 :
                return new TabSecondFragment();
            case 2 :
                return new TabThirdFragment();
        }
        return new TabFirstFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}