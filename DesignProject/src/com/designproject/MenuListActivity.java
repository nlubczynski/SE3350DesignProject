package com.designproject;

import com.designproject.R;
import com.designproject.R.id;
import com.designproject.R.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class MenuListActivity extends FragmentActivity
        implements MenuListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        if (findViewById(R.id.menu_detail_container) != null) {
            mTwoPane = true;
            ((MenuListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.menu_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    //For the main part when you select an item what happens
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(MenuDetailFragment.ARG_ITEM_ID, id);
            MenuDetailFragment fragment = new MenuDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.menu_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, MenuDetailActivity.class);
            detailIntent.putExtra(MenuDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
