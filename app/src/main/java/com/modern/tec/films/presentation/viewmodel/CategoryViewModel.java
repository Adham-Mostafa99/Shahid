package com.modern.tec.films.presentation.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.modern.tec.films.R;
import com.modern.tec.films.core.models.Category;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    public CategoryViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public LiveData<List<Category>> getCategories() {
        MutableLiveData<List<Category>> categoryMutableLiveData = new MutableLiveData<>();
        List<Category> categoryList = new ArrayList<>();

        categoryList.add(new Category(
                getApplication().getString(R.string.horror_category),
                R.drawable.horror));

        categoryList.add(new Category(
                getApplication().getString(R.string.comedy_category),
                R.drawable.comedy));

        categoryList.add(new Category(
                getApplication().getString(R.string.war_category),
                R.drawable.war));


        categoryList.add(new Category(
                getApplication().getString(R.string.action_category),
                R.drawable.action));


        categoryList.add(new Category(
                getApplication().getString(R.string.drama_category),
                R.drawable.drama));

        categoryList.add(new Category(
                getApplication().getString(R.string.romance_category),
                R.drawable.romance));

        categoryList.add(new Category(
                getApplication().getString(R.string.sci_fi_category),
                R.drawable.sci_fi));

        categoryList.add(new Category(
                getApplication().getString(R.string.animation_category),
                R.drawable.animation_catigory));

        categoryMutableLiveData.setValue(categoryList);
        return categoryMutableLiveData;
    }
}
