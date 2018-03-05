package com.bagslabs.SR5K3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.bagslabs.SR5K3.fragment.CrashPostsFragment;
import com.google.firebase.auth.FirebaseAuth;

public class  ViewCrashList extends BaseActivity {

    FragmentPagerAdapter mPagerCrashAdapter;
    ViewPager mViewCrashPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crash_list_view);

        // Create the adapter that will return a fragment for each section
        mPagerCrashAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mCrashFragments = new Fragment[] {
                    new CrashPostsFragment(),
            };
            @Override
            public Fragment getItem(int position) {
                return mCrashFragments[position];
            }
            @Override
            public int getCount() {
                return mCrashFragments.length;
            }
        };
        // Set up the ViewPager with the sections adapter.
        mViewCrashPager = findViewById(R.id.crash_container);
        mViewCrashPager.setAdapter(mPagerCrashAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}