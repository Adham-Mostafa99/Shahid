package com.modern.tec.films;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.modern.tec.films.adapters.FilmAdapter;
import com.modern.tec.films.data.FilmApi;
import com.modern.tec.films.data.RetrofitInstance;
import com.modern.tec.films.models.Film;
import com.modern.tec.films.models.FilmsRespond;
import com.modern.tec.films.viewmodel.MainActivityViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    FilmAdapter filmAdapter;


    MainActivityViewModel mainActivityViewModel;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRecycler();

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mainActivityViewModel.getAllFilms().observe(this, new Observer<List<Film>>() {
            @Override
            public void onChanged(List<Film> films) {
                filmAdapter.submitList(films);
            }
        });


    }

    private void initRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        filmAdapter = new FilmAdapter(this);
        recyclerView.setAdapter(filmAdapter);
    }
}