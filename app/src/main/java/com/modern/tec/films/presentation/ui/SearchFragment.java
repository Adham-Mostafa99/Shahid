package com.modern.tec.films.presentation.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.modern.tec.films.core.models.Film;
import com.modern.tec.films.databinding.FragmentSearchBinding;
import com.modern.tec.films.presentation.adapters.FilmAdapter;
import com.modern.tec.films.presentation.viewmodel.FilmsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    FragmentSearchBinding binding;
    FilmAdapter filmAdapter;
    FilmsViewModel filmsViewModel;
    List<Film> filmList;

    MainActivity activity;

    int page;
    String searchedFilmName;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        initAdapters();
        initRecyclers();
        initViewModels();
        initPage();
        initEditText();

        onBackClick();

        searchListener();

        getSearchedFilms();


        return binding.getRoot();
    }

    private void initEditText() {
        binding.searchContent.filmDateEdittext.requestFocus();
        InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(binding.searchContent.filmDateEdittext, InputMethodManager.SHOW_IMPLICIT);
    }

    private void onBackClick() {
        binding.searchBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setNavWithCustomFragment(new HomeFragment());
            }
        });
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            activity = (MainActivity) context;
    }

    private void getSearchedFilms() {
        filmsViewModel.getSearchedFilmsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Film>>() {
            @Override
            public void onChanged(List<Film> films) {
                if (films != null) {
                    filmAdapter.notifyDataSetChanged();
                    filmList.addAll(films);
                    filmAdapter.submitList(filmList);

                    if (filmList.isEmpty())
                        binding.txtNoMovie.setVisibility(View.VISIBLE);
                    else
                        binding.txtNoMovie.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getActivity(), "Check your connection, and try again.", Toast.LENGTH_SHORT).show();
                    if (filmList.isEmpty()) {
                        binding.txtNoMovie.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void searchListener() {
        binding.searchContent.filmDateEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                resetPage();
                if (!s.toString().isEmpty()) {
                    resetAdapter();
                    setSearchedFilmName(s.toString());
                    filmsViewModel.searchFilms(getSearchedFilmName(), null, getPage());
                }

            }
        });
    }

    private void initPage() {
        setPage(1);
    }

    private void setPage(int page) {
        this.page = page;
    }

    private void resetPage() {
        setPage(1);
    }

    private void resetAdapter() {
        filmList.clear();
        filmAdapter.submitList(filmList);

    }


    private int getPage() {
        return page;
    }

    public String getSearchedFilmName() {
        return searchedFilmName;
    }

    public void setSearchedFilmName(String searchedFilmName) {
        this.searchedFilmName = searchedFilmName;
    }

    private void initViewModels() {
        filmsViewModel = new ViewModelProvider(this).get(FilmsViewModel.class);
    }

    private void initRecyclers() {
        binding.searchContent.searchRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.searchContent.searchRecycler.setAdapter(filmAdapter);

        filmList = new ArrayList<>();
    }

    private void initAdapters() {
        filmAdapter = new FilmAdapter();
        filmAdapter.setOnItemClick(onItemClick());
        filmAdapter.setLoadMoreData(onLoadData());
    }

    private FilmAdapter.LoadMoreData onLoadData() {
        return () -> {
            page++;
            filmsViewModel.searchFilms(getSearchedFilmName(), null, getPage());
        };
    }

    private FilmAdapter.OnItemClick onItemClick() {
        return film -> {
            startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra(DetailsActivity.FILM_DETAILS_INTENT, film));
        };
    }


}
