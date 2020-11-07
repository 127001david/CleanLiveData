package com.rightpoint.oknet;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rightpoint.oknet.fragment.FirstFragment;
import com.rightpoint.oknet.fragment.SecondFragment;
import com.rightpoint.oknet.vm.CountViewModel;
import com.rightpoint.oknet.vm.EpidemicViewModel;

public class MainActivity extends AppCompatActivity {

    private FirstFragment firstFragment;
    private SecondFragment secondFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();

        ViewPager vp = findViewById(R.id.vp);
        vp.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()
                , FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

        BottomNavigationView bn = findViewById(R.id.view_bottom_navigation);

        vp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bn.getMenu().getItem(position).setChecked(true);
            }
        });

        bn.setOnNavigationItemSelectedListener(item -> {
            vp.setCurrentItem(item.getOrder());
            return true;
        });

        CountViewModel countViewModel = new ViewModelProvider(this
                , new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(CountViewModel.class);

        EpidemicViewModel epidemicViewModel = new ViewModelProvider(this
                , new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(EpidemicViewModel.class);

        FloatingActionButton fb = findViewById(R.id.fb);
        fb.setScaleType(ImageView.ScaleType.CENTER);
        fb.setOnClickListener(view -> {
            countViewModel.increase();
            epidemicViewModel.loadEpidemic();
        });
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (0 == position) {
                return firstFragment;
            } else {
                return secondFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}