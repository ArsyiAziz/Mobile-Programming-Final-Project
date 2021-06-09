package com.arsyiaziz.finalproject.frontend.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arsyiaziz.finalproject.R;
import com.arsyiaziz.finalproject.backend.callbacks.OnMovieCallback;
import com.arsyiaziz.finalproject.backend.local.FavouritesHelper;
import com.arsyiaziz.finalproject.backend.models.MovieModel;
import com.arsyiaziz.finalproject.backend.models.VideoModel;
import com.arsyiaziz.finalproject.backend.api.MovieRepository;
import com.arsyiaziz.finalproject.frontend.adapters.CreditAdapter;
import com.arsyiaziz.finalproject.misc.Constants;
import com.arsyiaziz.finalproject.misc.HelperFunctions;
import com.arsyiaziz.finalproject.misc.ImageSize;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;
import java.util.Objects;

public class MovieDetailsActivity extends AppCompatActivity {
    private final String TAG = "MovieDetailsActivity";
    private int movieId;
    private MovieModel movie;
    private ImageView ivBackdrop;
    private ImageView ivPoster;
    private TextView tvTitle;
    private RatingBar rbRating;
    private TextView tvRuntime;
    private TextView tvReleaseYear;
    private TextView tvOverview;
    private TextView tvRatingLabel;
    private RecyclerView rvCredits;
    private TextView tvGenres;
    private RelativeLayout rlOverlay;
    private LinearLayout llCredits;
    private TextView tvError;
    private FloatingActionButton btnTrailer;
    private ProgressBar progressBar;
    private MovieRepository repository;
    private FavouritesHelper helper;
    private boolean isLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.loading));

        ivBackdrop = findViewById(R.id.iv_backdrop);
        ivPoster = findViewById(R.id.iv_poster);
        tvTitle = findViewById(R.id.tv_title);
        rbRating = findViewById(R.id.rb_rating);
        tvRatingLabel = findViewById(R.id.tv_rating_label);
        tvRuntime = findViewById(R.id.tv_runtime);
        tvReleaseYear = findViewById(R.id.tv_release_year);
        tvGenres = findViewById(R.id.tv_genres);
        tvOverview = findViewById(R.id.tv_overview);
        llCredits = findViewById(R.id.ll_credits);
        rvCredits = findViewById(R.id.rv_credits);
        rlOverlay = findViewById(R.id.rl_overlay);
        tvError = findViewById(R.id.tv_error);
        progressBar = findViewById(R.id.progress_bar);
        btnTrailer = findViewById(R.id.btn_trailer);

        HelperFunctions.showView(rlOverlay);
        HelperFunctions.showView(progressBar);
        movieId = getIntent().getIntExtra(Constants.MOVIE_ID, 0);
        repository = MovieRepository.getRepository();
        helper = new FavouritesHelper(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getRepositoryData(movieId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_movie_details, menu);
        if (helper.isFavourite(movieId)) {
            MenuItem itemFavourite = menu.getItem(0);
            itemFavourite.setIcon(R.drawable.ic_baseline_favorite_checked_24);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.item_favourite) {
            if (isLoaded) {
                if (helper.isFavourite(movieId)) {
                    helper.delete(movieId);
                    item.setIcon(R.drawable.ic_baseline_favorite_unchecked_24);
                } else if (movieId > 0) {
                    helper.insert(movie);
                    item.setIcon(R.drawable.ic_baseline_favorite_checked_24);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void getRepositoryData(int movieId) {
        repository.getMovieDetails(movieId, new OnMovieCallback() {
            @Override
            public void onSuccess(MovieModel movieModel, String msg) {
                movie = movieModel;
                onBindView(movie);
                isLoaded = true;
            }

            @Override
            public void onFailure(String msg) {
                HelperFunctions.hideView(progressBar);
                Log.d(TAG, "onFailure: "+msg);
                HelperFunctions.showError(tvError, getString(R.string.retrieval_error));
            }
        });
    }

    private void onBindView(MovieModel movie) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(movie.getTitle());
        Glide.with(this)
                .load(movie.getBackdropPath(ImageSize.W1280))
                .into(ivBackdrop);
        Glide.with(this)
                .load(movie.getPosterPath(ImageSize.W342))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        HelperFunctions.hideView(progressBar);
                        HelperFunctions.hideView(rlOverlay);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        HelperFunctions.hideView(progressBar);
                        HelperFunctions.hideView(rlOverlay);
                        return false;
                    }
                })
                .error(R.drawable.ic_image_not_available)
                .into(ivPoster);
        tvTitle.setText(movie.getTitle());
        tvRatingLabel.setText(String.format(Locale.getDefault(), "%.1f", movie.getVoteAverage()));
        rbRating.setRating(movie.getVoteAverage());
        tvRuntime.setText(movie.getRuntime());
        tvOverview.setText(movie.getOverview());
        tvReleaseYear.setText(String.format(Locale.getDefault(), "(%s)", movie.getReleaseYear()));
        tvGenres.setText(movie.getGenres());

        if (movie.getCredits().size() > 0) {
            CreditAdapter creditAdapter = new CreditAdapter(movie.getCredits());
            rvCredits.setAdapter(creditAdapter);
        } else {
            HelperFunctions.hideView(llCredits);
        }

        if (movie.getVideos().size() > 0) {
            btnTrailer.setOnClickListener(view -> {
                VideoModel firstTrailer = movie.getVideos().get(0);

                //Open youtube app
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + firstTrailer.getVideoKey()));
                startActivity(intent);
            });
        } else {
            HelperFunctions.hideView(btnTrailer);
        }
    }


}