package com.example.android.miwok;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by Dell on 9/3/2017.
 */

public class CategoryAdapter extends FragmentPagerAdapter {

    /** Context of the app */
    private Context mContext;

    /**
     * Create a new {@link CategoryAdapter} object.
     *
     //* @param context is the context of the app
     * @param fm is the fragment manager that will keep each fragment's state in the adapter
     *           across swipes.
     */

    //public CategoryAdapter(FragmentManager fm)
    //{
     //   super(fm);
    //}
    public CategoryAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        mContext = context;
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
//        switch (position){
//            case 0:
//                return new NumbersFragment();
//            case 1:
//                return new FamilyFragment();
//            case 2:
//                return new ColorsFragment();
//            case 3:
//                return new PhraseFragment();
//            default: return null;
//        }
        if (position == 0) {
            return new NumbersFragment();
        } else if (position == 1) {
            return new FamilyFragment();
        } else if (position == 2) {
            return new ColorsFragment();
        } else {
            return new PhraseFragment();
        }
    }

    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {
        return 4;
    }

   // @Override
    public CharSequence getPageTitle(int position) {
        //We could return a hardcoded string such as “Numbers,” “Family,” and so on.
        // However, as emphasized at the end of Lesson 4,
        // we don’t want to restrict our app to only support the English language.
        // Instead, we should use the string resource for those category names.

        /**
         * that also means we need a Context object in order to turn the string resource ID into an actual String.
         * So we modify the CategoryAdapter constructor to also require a Context input so that we can get the proper text string.
         */
//        switch (position) {
//            case 0: return mContext.getString(R.string.category_numbers);
//            case 1: return mContext.getString(R.string.category_family);
//            case 2: return mContext.getString(R.string.category_colors);
//            case 3: return mContext.getString(R.string.category_phrases);
//            default: return null;
//        }
        if (position == 0) {
            return mContext.getString(R.string.category_numbers);
        } else if (position == 1) {
            return mContext.getString(R.string.category_family);
        } else if (position == 2) {
            return mContext.getString(R.string.category_colors);
        } else {
            return mContext.getString(R.string.category_phrases);
        }
    }
}
