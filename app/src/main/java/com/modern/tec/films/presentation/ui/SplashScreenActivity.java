package com.modern.tec.films.presentation.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.modern.tec.films.core.models.Genre;
import com.modern.tec.films.databinding.ActivitySplashScreenBinding;
import com.modern.tec.films.presentation.viewmodel.GenreViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {
    private ActivitySplashScreenBinding binding;
    GenreViewModel genreViewModel;
    private LifecycleOwner lifecycleOwner = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getBinding());
        initViewModels();


        genreViewModel.getGenres().observe(this, new Observer<List<Genre>>() {
            @Override
            public void onChanged(List<Genre> genreList) {

                Map<Integer, String> map = new HashMap<>();
                for (Genre genre : genreList) {
                    map.put(genre.getGenreId(), genre.getGenreName());
                }
                genreViewModel.storeGenreInInternalMemory(map);

                intentToMainActivity();
            }
        });


    }

    private void intentToMainActivity() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }, 500);
    }

    private void initViewModels() {
        genreViewModel = new ViewModelProvider(this).get(GenreViewModel.class);
    }

    private View getBinding() {
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}