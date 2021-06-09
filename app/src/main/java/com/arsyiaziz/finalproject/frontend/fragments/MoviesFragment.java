package com.arsyiaziz.finalproject.frontend.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.arsyiaziz.finalproject.R;
import com.arsyiaziz.finalproject.backend.callbacks.OnMovieListCallback;
import com.arsyiaziz.finalproject.backend.callbacks.OnSearchCallback;
import com.arsyiaziz.finalproject.backend.models.ListedMovieModel;
import com.arsyiaziz.finalproject.backend.api.MovieRepository;
import com.arsyiaziz.finalproject.frontend.activities.MovieDetailsActivity;
import com.arsyiaziz.finalproject.frontend.adapters.MovieAdapter;
import com.arsyiaziz.finalproject.frontend.interfaces.OnItemClickListener;
import com.arsyiaziz.finalproject.misc.Constants;
import com.arsyiaziz.finalproject.misc.HelperFunctions;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.List;


public class MoviesFragment extends Fragment implements OnItemClickListener<ListedMovieModel>, SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {
    private final String TAG = "MoviesFragment";
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private TextView tvError;
    private MovieAdapter adapter;
    private MovieRepository repository;
    private SearchView searchView;
    private String query = "";
    private boolean isFetching;
    private int currentPage = 1;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        refreshLayout = view.findViewById(R.id.swl_movie);
        recyclerView = view.findViewById(R.id.rv_movie);
        progressBar = view.findViewById(R.id.progress_bar);
        tvError = view.findViewById(R.id.tv_error);

        repository = MovieRepository.getRepository();
        onScrollListener();
        getRepositoryData(currentPage, progressBar);
        refreshLayout.setOnRefreshListener(this);

        return view;
    }

    private void onScrollListener() {
        final FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.FLEX_START);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItems = layoutManager.getItemCount();
                int visibleItems = layoutManager.getChildCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItem + visibleItems >= totalItems / 2) {
                    if (!isFetching) {
                        getRepositoryData(currentPage + 1, null);
                    }
                }
            }
        });
    }

    private void getRepositoryData(int page, ProgressBar progressBar) {
        HelperFunctions.hideError(tvError);
        HelperFunctions.showView(progressBar);
        isFetching = true;
        if (query.isEmpty()) {
            repository.getMovie(getBundle(), page, new OnMovieListCallback() {
                @Override
                public void onSuccess(List<ListedMovieModel> movieList, int page, String msg) {
                    if (adapter == null) {
                        adapter = new MovieAdapter(movieList);
                        adapter.setClickListener(MoviesFragment.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.appendList(movieList);
                    }
                    currentPage = page;
                    isFetching = false;
                    refreshLayout.setRefreshing(false);
                    HelperFunctions.hideView(progressBar);
                }

                @Override
                public void onFailure(String msg) {
                    Log.d(TAG, "onFailure: " + msg);
                    HelperFunctions.showError(tvError, getString(R.string.retrieval_error));
                    refreshLayout.setRefreshing(false);
                    HelperFunctions.hideView(progressBar);
                    recyclerView.setAdapter(null);
                }
            });
        } else {
            repository.search(query, page, new OnSearchCallback() {
                @Override
                public void onSuccess(List<ListedMovieModel> movieList, int page, String msg) {
                    if (adapter == null) {
                        adapter = new MovieAdapter(movieList);
                        adapter.setClickListener(MoviesFragment.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.appendList(movieList);
                    }
                    currentPage = page;
                    isFetching = false;
                    refreshLayout.setRefreshing(false);
                    HelperFunctions.hideView(progressBar);
                }

                @Override
                public void onFailure(String msg) {
                    Log.d(TAG, "onFailure: " + msg);
                    HelperFunctions.showError(tvError, msg);
                    refreshLayout.setRefreshing(false);
                    HelperFunctions.hideView(progressBar);
                    recyclerView.setAdapter(null);
                }
            });
        }
    }
    private String getBundle() {
        if (getArguments() != null) {
            return getArguments().getString("QUERY_TYPE");
        }
        return "now_playing";
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
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        query = s;
        adapter = null;
        currentPage = 1;
        getRepositoryData(currentPage, progressBar);
        return true;
    }


    @Override
    public void onRefresh() {
        adapter = null;
        currentPage = 1;
        getRepositoryData(currentPage, null);
    }

    @Override
    public void onClick(ListedMovieModel listedMovieModel) {
        Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
        intent.putExtra(Constants.MOVIE_ID, listedMovieModel.getId());
        startActivity(intent);
    }

}