package com.modern.tec.films.presentation.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.modern.tec.films.R;
import com.modern.tec.films.core.models.Film;
import com.modern.tec.films.databinding.FragmentRankingBinding;
import com.modern.tec.films.presentation.adapters.RankingFilmAdapter;
import com.modern.tec.films.presentation.viewmodel.FilmsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RankingFragment extends Fragment {
    private FragmentRankingBinding binding;

    private RankingFilmAdapter rankingFilmAdapter;
    private FilmsViewModel filmsViewModel;

    private List<Film> filmList;

    private int pageNumber;
    private String yearSort;
    private String sortType;

    private int oldCheckBoxId;

    private boolean isFilterShown;
    private boolean isApplyBtnEnabled;
    private MainActivity activity;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentRankingBinding.inflate(inflater, container, false);
        initToolbar();
        initViewModels();
        initAdapters();
        initRecyclers();
        initFilter();

        getDiscoveredFilmsLiveData();

        onClickFilterCloseBtn();
        onClickApplyFilterBtn();
        onBackClick();
        onCLickSearch();

        filmsViewModel.getDiscoveredFilms(sortType, yearSort, pageNumber);

        return binding.getRoot();
    }

    private void onCLickSearch() {
        binding.rankingContent.linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setNavWithCustomFragment(new SearchFragment());
            }
        });
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            activity = (MainActivity) context;
    }

    private void onBackClick() {
        binding.rankingBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setNavWithCustomFragment(new HomeFragment());
            }
        });
    }


    private void getDiscoveredFilmsLiveData() {
        filmsViewModel.getDiscoveredFilmsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Film>>() {
            @Override
            public void onChanged(List<Film> films) {
                if (films != null) {
                    rankingFilmAdapter.notifyDataSetChanged();
                    filmList.addAll(films);
                    rankingFilmAdapter.submitList(filmList);

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

    private void initFilter() {
        setPageNumber(1);
        setSortType(getString(R.string.top_rated_sort));
        resetYearSort();
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);

    }


    private void initRecyclers() {
        binding.rankingContent.rankingRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rankingContent.rankingRecycler.setHasFixedSize(true);
        binding.rankingContent.rankingRecycler.setAdapter(rankingFilmAdapter);
        filmList = new ArrayList<>();

    }

    private void initAdapters() {
        rankingFilmAdapter = new RankingFilmAdapter();
        rankingFilmAdapter.setOnItemClick(onRankingClick());
        rankingFilmAdapter.setLoadMoreData(onLoadMoreFilms());

    }

    private void initViewModels() {
        filmsViewModel = new ViewModelProvider(this).get(FilmsViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.custom_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.nav_menu) {
            if (isFilterShown)
                hideFilterLayout();
            else
                showFilterLayout();

        }
        return super.onOptionsItemSelected(item);
    }

    private void onClickFilterCloseBtn() {
        binding.rankingFilterContent.filterLayoutCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFilterLayout();
            }
        });
    }


    private void onClickApplyFilterBtn() {
        binding.rankingFilterContent.filterLayoutApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.rankingFilterContent.filterLayoutGroup.getCheckedRadioButtonId() == -1) { //No Filter
                    Toast.makeText(getContext(), "Choose one Filter to Apply", Toast.LENGTH_SHORT).show();
                } else {
                    applyFilter();
                }
            }
        });

    }

    private void applyFilter() {
        if (isApplyBtnEnabled) {
            clearList();
            filmsViewModel.getDiscoveredFilms(sortType, yearSort, pageNumber);
            hideFilterLayout();
        }
    }

    private void clearList() {
        filmList.clear();
        rankingFilmAdapter.notifyDataSetChanged();
    }

    private void showFilterLayout() {
        binding.filterBackLayout.setVisibility(View.VISIBLE);
        binding.rankingFilterContent.content.setVisibility(View.VISIBLE);
        slideUp(binding.rankingFilterContent.content);
        disableLayoutWhenShowFilter();
        isFilterShown = true;

        initFilterBtns();
    }

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    private void initFilterBtns() {
        disableApplyBtn();
        initRadioButton();
        binding.rankingFilterContent.filterLayoutGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.filter_layout_top_rated_btn:
                        setSortType(getString(R.string.top_rated_sort));
                        resetPageNumber();
                        resetYearSort();
                        break;
                    case R.id.filter_layout_most_watched_btn:
                        setSortType(getString(R.string.most_watched_sort));
                        resetPageNumber();
                        resetYearSort();
                        break;
                    case R.id.filter_layout_this_year_btn:
                        setSortType(getString(R.string.top_rated_sort));
                        setYearSort(getCurrentYear());
                        resetPageNumber();
                        break;
                    case R.id.filter_layout_all_btn:
                        resetPageNumber();
                        resetSortType();
                        resetYearSort();
                        break;
                }

                if (checkedId != oldCheckBoxId)
                    enableApplyBtn();
                else
                    disableApplyBtn();
            }
        });
    }

    private void initRadioButton() {
        if (getYearSort().equals(getCurrentYear())) {
            oldCheckBoxId = binding.rankingFilterContent.filterLayoutThisYearBtn.getId();
            binding.rankingFilterContent.filterLayoutThisYearBtn.setChecked(true);
        } else if (sortType.equals(getString(R.string.top_rated_sort))) {
            oldCheckBoxId = binding.rankingFilterContent.filterLayoutTopRatedBtn.getId();
            binding.rankingFilterContent.filterLayoutTopRatedBtn.setChecked(true);
        } else if (sortType.equals(getString(R.string.most_watched_sort))) {
            oldCheckBoxId = binding.rankingFilterContent.filterLayoutMostWatchedBtn.getId();
            binding.rankingFilterContent.filterLayoutMostWatchedBtn.setChecked(true);
        } else {
            oldCheckBoxId = binding.rankingFilterContent.filterLayoutAllBtn.getId();
            binding.rankingFilterContent.filterLayoutAllBtn.setChecked(true);
        }

    }

    private void disableApplyBtn() {
        isApplyBtnEnabled = false;
        binding.rankingFilterContent.filterLayoutApplyBtn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.filter_apply_btn_background));
    }

    private void enableApplyBtn() {
        isApplyBtnEnabled = true;
        binding.rankingFilterContent.filterLayoutApplyBtn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.enable_filter_apply_btn_background));
    }

    private void resetPageNumber() {
        setPageNumber(1);
    }

    private void resetSortType() {
        setSortType("");
    }

    private void resetYearSort() {
        setYearSort("");
    }

    private void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    private void setSortType(String sortType) {
        this.sortType = sortType;
    }

    private void setYearSort(String yearSort) {
        this.yearSort = yearSort;
    }


    public String getYearSort() {
        return yearSort;
    }


    private String getCurrentYear() {
        return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    }

    private void hideFilterLayout() {
        binding.filterBackLayout.setVisibility(View.INVISIBLE);
        binding.rankingFilterContent.content.setVisibility(View.INVISIBLE);
        slideDown(binding.rankingFilterContent.content);
        enableLayoutWhenDisplayFilter();
        isFilterShown = false;
    }

    private void disableLayoutWhenShowFilter() {
        binding.rankingContent.filmDateEdittext.setEnabled(false);
        binding.rankingContent.filmDateEdittext.setClickable(false);
        rankingFilmAdapter.setOnItemClick(null);
    }

    private void enableLayoutWhenDisplayFilter() {
        binding.rankingContent.filmDateEdittext.setEnabled(true);
        binding.rankingContent.filmDateEdittext.setClickable(true);
        rankingFilmAdapter.setOnItemClick(onRankingClick());
    }

    private RankingFilmAdapter.OnItemClick onRankingClick() {
        return film -> {
            startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra(DetailsActivity.FILM_DETAILS_INTENT, film));
        };
    }

    private RankingFilmAdapter.LoadMoreData onLoadMoreFilms() {
        return () -> {
            pageNumber++;
            filmsViewModel.getDiscoveredFilms(sortType, yearSort, pageNumber);
        };
    }


}
