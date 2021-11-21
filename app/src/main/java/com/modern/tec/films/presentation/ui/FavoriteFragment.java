package com.modern.tec.films.presentation.ui;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

public class FavoriteFragment  extends Fragment {


    private MainActivity activity;



    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            activity = (MainActivity) context;
    }
}
