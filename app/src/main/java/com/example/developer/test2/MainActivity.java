package com.example.developer.test2;

import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.tool.Binding;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;

import com.example.developer.test2.databinding.ActivityBinding;

public class MainActivity extends AppCompatActivity {

    private ViewPagerFragment viewPagerFragment;
    private ActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            if (savedInstanceState == null) {

                initScreen();

            } else {

                viewPagerFragment = (ViewPagerFragment)
                        getSupportFragmentManager().getFragments().get(0);
            }
        }

        private void initScreen() {
            viewPagerFragment = new ViewPagerFragment();

            final FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, viewPagerFragment)
                    .commit();
        }

        @Override
        public void onBackPressed() {

            if (!viewPagerFragment.onBackPressed()) {
                super.onBackPressed();

            }
        }

    }

