package com.modern.tec.films.presintation.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.modern.tec.films.R;
import com.modern.tec.films.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    private MutableLiveData<Fragment> currentFragment;

    private final NavigationBarView.OnItemSelectedListener listener
            = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_bottom_home:
                    if (!(currentFragment.getValue() instanceof HomeFragment))
                        currentFragment.postValue(new HomeFragment());
                    break;
                case R.id.nav_bottom_rank:
                    if (!(currentFragment.getValue() instanceof RankingFragment))
                        currentFragment.postValue(new RankingFragment());
                    break;
                case R.id.nav_bottom_compass:
                    if (!(currentFragment.getValue() instanceof SearchFragment))
                        currentFragment.postValue(new SearchFragment());
                    break;
                case R.id.nav_bottom_fav:
                    if (!(currentFragment.getValue() instanceof FavoriteFragment))
                        currentFragment.postValue(new FavoriteFragment());
                    break;
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initBottomNavView();
        initLiveData();
        initFragment();


        currentFragment.observe(this, new Observer<Fragment>() {
            @Override
            public void onChanged(Fragment fragment) {
                replaceFragment(fragment);
            }
        });


    }


    private void initLiveData() {
        currentFragment = new MutableLiveData<>();
    }

    private void initFragment() {
        currentFragment.setValue(new HomeFragment());
    }

    private void initBottomNavView() {
        binding.navBottom.setOnItemSelectedListener(listener);
    }

    private void initBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(binding.container.getId(), fragment)
                .commit();
    }

    protected void setNavWithCustomFragment(Fragment fragment) {
        if (fragment instanceof HomeFragment)
            binding.navBottom.setSelectedItemId(R.id.nav_bottom_home);
        else if (fragment instanceof RankingFragment)
            binding.navBottom.setSelectedItemId(R.id.nav_bottom_rank);
        else if (fragment instanceof SearchFragment)
            binding.navBottom.setSelectedItemId(R.id.nav_bottom_compass);
        else if (fragment instanceof FavoriteFragment)
            binding.navBottom.setSelectedItemId(R.id.nav_bottom_fav);
    }


    @Override
    public void onBackPressed() {
        if (currentFragment.getValue() instanceof HomeFragment)
            finish();
        else if (currentFragment.getValue() instanceof RankingFragment ||
                currentFragment.getValue() instanceof SearchFragment ||
                currentFragment.getValue() instanceof FavoriteFragment) {
            setNavWithCustomFragment(new HomeFragment());
        }

    }

//    private Fragment getVisibleFragment(){
//        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
//        List<Fragment> fragments = fragmentManager.getFragments();
//        if(fragments != null){
//            for(Fragment fragment : fragments){
//                if(fragment != null && fragment.isVisible())
//                    return fragment;
//            }
//        }
//        return null;
//    }
}