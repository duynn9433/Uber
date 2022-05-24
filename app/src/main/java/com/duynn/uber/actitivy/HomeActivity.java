package com.duynn.uber.actitivy;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.duynn.uber.R;
import com.duynn.uber.adapter.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseAnalytics;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottom_nav);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_uber);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_history);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_chat);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_info);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_uber:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.navigation_history:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.navigation_chat:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.navigation_info:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

}