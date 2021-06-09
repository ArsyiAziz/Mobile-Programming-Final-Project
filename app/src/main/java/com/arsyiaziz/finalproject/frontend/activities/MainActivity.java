package com.arsyiaziz.finalproject.frontend.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.arsyiaziz.finalproject.R;
import com.arsyiaziz.finalproject.frontend.fragments.FavouritesFragment;
import com.arsyiaziz.finalproject.frontend.fragments.MoviesFragment;
import com.arsyiaziz.finalproject.misc.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout flMain = findViewById(R.id.fl_main);
        BottomNavigationView bnvMain = findViewById(R.id.bnv_main);
        bnvMain.setOnNavigationItemSelectedListener(this);
        bnvMain.setSelectedItemId(R.id.item_now_playing);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        String queryType = null;
        int itemId = item.getItemId();

        if (itemId == R.id.item_now_playing) {
            setActionBarTitle(getString(R.string.now_playing));
            fragment = new MoviesFragment();
            queryType = "now_playing";
        } else if (itemId == R.id.item_upcoming) {
            setActionBarTitle(getString(R.string.upcoming));
            fragment = new MoviesFragment();
            queryType = "upcoming";
        } else if (itemId == R.id.item_popular) {
            setActionBarTitle(getString(R.string.popular));
            fragment = new MoviesFragment();
            queryType = "popular";
        } else if (itemId == R.id.item_favourites) {
            setActionBarTitle(getString(R.string.favourites));
            fragment = new FavouritesFragment();
            queryType = "favourites";
        }

        if (fragment != null) {
            startFragment(fragment, queryType);
            return true;
        } else {
            return false;
        }
    }

    private void startFragment(Fragment fragment, String bundle) {
        if (bundle != null) {
            Bundle queryType = new Bundle();
            queryType.putString(Constants.QUERY_TYPE, bundle);
            fragment.setArguments(queryType);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_main, fragment)
                .commit();
    }


    private void setActionBarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }
}