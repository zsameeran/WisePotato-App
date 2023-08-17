package com.wisepotato.wp_app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class tabLayoutPagerAdaptor extends FragmentStateAdapter {
    public tabLayoutPagerAdaptor(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
       switch (position){
           case 0:
               return new savedPostsFragment();
           case 1:
               return new myPostsFragment();
           default:
               return new savedPostsFragment();

       }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
