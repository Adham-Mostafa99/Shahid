package com.modern.tec.films.presentation.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationBarView;
import com.modern.tec.films.R;
import com.modern.tec.films.data.network.Network;
import com.modern.tec.films.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    private MutableLiveData<Fragment> currentFragment;
    private boolean isWantToExit;

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

        checkNetworkListener();


        currentFragment.observe(this, new Observer<Fragment>() {
            @Override
            public void onChanged(Fragment fragment) {
                replaceFragment(fragment);
            }
        });


    }

    private void checkNetworkListener() {
        Network network = new Network(getApplication());
        network.getIsNetworkAvailable().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    binding.mainActivityNetworkText.setVisibility(View.GONE);
                else
                    binding.mainActivityNetworkText.setVisibility(View.VISIBLE);
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
        if (currentFragment.getValue() instanceof HomeFragment) {
            if (isWantToExit)
                onDestroy();
            else {
                showCustomToast();
                isWantToExit = true;
            }
        } else if (currentFragment.getValue() instanceof RankingFragment ||
                currentFragment.getValue() instanceof SearchFragment ||
                currentFragment.getValue() instanceof FavoriteFragment) {
            setNavWithCustomFragment(new HomeFragment());
        }

    }

    private void showCustomToast() {
        Toast toast = Toast.makeText(this, "click again to exit from App ! ", Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackground(ContextCompat.getDrawable(this, R.drawable.toast_background));
//        view.setBackgroundColor(ContextCompat.getColor(this, R.color.assent_color));
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        text.setPadding(16, 0, 16, 0);

        toast.show();
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