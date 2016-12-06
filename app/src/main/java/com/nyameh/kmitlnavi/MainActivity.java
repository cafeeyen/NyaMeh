package com.nyameh.kmitlnavi;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import model.EventData;

import static com.nyameh.kmitlnavi.FavoriteFragment.getFavoriteListData;

public class MainActivity extends AppCompatActivity{

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view) ;
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null){
            currentFragment = new TabFragment();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.containerView, currentFragment).commit();
        }

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(MenuItem menuItem) {
                 mDrawerLayout.closeDrawers();
                 mFragmentTransaction = mFragmentManager.beginTransaction();
                 if (menuItem.getItemId() == R.id.nav_item_event_news){currentFragment = new TabFragment();}
                 if (menuItem.getItemId() == R.id.nav_item_map)
                 {
                     ArrayList<Double> lat = new ArrayList<>();
                     ArrayList<Double> lng = new ArrayList<>();
                     ArrayList<String> name = new ArrayList<>();
                     ArrayList<EventData> data = getFavoriteListData();

                     NyaMehDatabase mHelper = new NyaMehDatabase(getApplicationContext());
                     SQLiteDatabase mDb = mHelper.getReadableDatabase();

                     Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                     for(EventData i : data)
                     {
                         Cursor mCursor = mDb.rawQuery(String.format("SELECT * FROM " + NyaMehDatabase.TABLE_NAME)
                                 + " WHERE " + NyaMehDatabase.COL_CODE + "='" + i.getPosition() + "'", null);
                         mCursor.moveToFirst();
                         lat.add(Double.parseDouble(mCursor.getString(mCursor.getColumnIndex(NyaMehDatabase.COL_LATITUDE))));
                         lng.add(Double.parseDouble(mCursor.getString(mCursor.getColumnIndex(NyaMehDatabase.COL_LONGITUDE))));
                         name.add(mCursor.getString(mCursor.getColumnIndex(NyaMehDatabase.COL_NAME)));
                     }
                     intent.putExtra("ArLat", lat);
                     intent.putExtra("ArLng", lng);
                     intent.putExtra("ArName", name);
                     startActivity(intent);
                 }
                 if (menuItem.getItemId() == R.id.nav_item_scan)
                 {
                     Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
                     startActivity(intent);
                 }
                 return false;
            }
        });

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.header_toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
            R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i("Test====", "=============== OnResume ==============");
    }
}