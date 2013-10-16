package com.designproject;

import com.designproject.R;
import com.designproject.R.id;
import com.designproject.R.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class MenuDetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(MenuDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(MenuDetailFragment.ARG_ITEM_ID));
            MenuDetailFragment fragment = new MenuDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MenuListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
