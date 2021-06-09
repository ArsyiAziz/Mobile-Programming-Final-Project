package com.arsyiaziz.finalproject.frontend.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.arsyiaziz.finalproject.R;
import com.arsyiaziz.finalproject.backend.local.FavouritesHelper;
import com.arsyiaziz.finalproject.backend.models.ListedMovieModel;
import com.arsyiaziz.finalproject.frontend.activities.MovieDetailsActivity;
import com.arsyiaziz.finalproject.frontend.adapters.MovieAdapter;
import com.arsyiaziz.finalproject.frontend.interfaces.OnItemClickListener;
import com.arsyiaziz.finalproject.misc.Constants;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.progressindicator.LinearProgressIndicator;


public class FavouritesFragment extends Fragment implements OnItemClickListener<ListedMovieModel>, SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener  {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private TextView tvError;
    private MovieAdapter adapter;
    private FavouritesHelper helper;
    private String query = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (searchView != null) {
            //Clear query
            searchView.setQuery("", false);
            searchView.setIconified(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_movie, menu);
        MenuItem item = menu.findItem(R.id.item_search);
        searchView = (SearchView) item.getActionView();
        setSearchView(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void setSearchView(SearchView searchView) {
        searchView.setQueryHint("Movie title");
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_favourite, container, false);

        recyclerView = view.findViewById(R.id.rv_movie);
        refreshLayout = view.findViewById(R.id.swl_movie);
        tvError = view.findViewById(R.id.tv_error);

        helper = new FavouritesHelper(getContext());

        final FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.FLEX_START);

        refreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(layoutManager);
        getHelperData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getHelperData();
    }

    private void getHelperData() {
        adapter = null;
        if (helper.selectAll().size() > 0) {
            if (query.equals("")) {
                adapter = new MovieAdapter(helper.selectAll());
            } else {
                adapter = new MovieAdapter(helper.select(query));
            }
            adapter.setClickListener(FavouritesFragment.this);
            adapter.notifyDataSetChanged();
        } else {
            tvError.setText(getText(R.string.empty_favourites));
        }

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(ListedMovieModel listedMovieModel) {
        Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
        intent.putExtra(Constants.MOVIE_ID, listedMovieModel.getId());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        adapter = null;
        getHelperData();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        query = s;
        adapter = null;
        getHelperData();
        return true;
    }
}