
package com.bagslabs.SR5K3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class  MainActivity extends BaseActivity {

    Button mBtnAddAccident;
    Button mBtnAddCrash;
    Button mBtnViewAccident;
    Button mBtnViewCrash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnAddAccident=findViewById(R.id.btnAddAccident);
        mBtnAddCrash=findViewById(R.id.btnAddCrash);
        mBtnViewAccident=findViewById(R.id.btnViewAccident);
        mBtnViewCrash=findViewById(R.id.btnViewCrash);

        // Button launches AddAccidentsActivity
        mBtnAddAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddAccidentsActivity.class));
            }
        });

        // Button launches AddCrashActivity
        mBtnAddCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddCrashActivity.class));
            }
        });

        // Button launches ViewAccidentsActivity
        mBtnViewAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewAccidentsList.class));
            }
        });

        // Button launches ViewCrashActivity
        mBtnViewCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewCrashList.class));
            }
        });

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
