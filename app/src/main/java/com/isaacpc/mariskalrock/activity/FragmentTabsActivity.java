package com.isaacpc.mariskalrock.activity;


import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.isaacpc.mariskalrock.R;
import com.isaacpc.mariskalrock.adapter.TabsPagerAdapter;
import com.isaacpc.mariskalrock.common.BroadcastConstants;
import com.isaacpc.mariskalrock.common.Constants;
import com.isaacpc.mariskalrock.log.Log;
import com.isaacpc.mariskalrock.thread.FeedUpdaterService;
import com.isaacpc.mariskalrock.utils.DateUtils;
import com.isaacpc.mariskalrock.utils.PreferencesUtils;


public class FragmentTabsActivity extends ActionBarActivity implements ActionBar.TabListener {

    private static final String LOG_TAG = "FragmentTabsActivity";

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_tabs_pager);

        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.addTab(actionBar.newTab().setText(getString(R.string.tabNoticias)).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(getString(R.string.tabPodcast)).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(getString(R.string.tabVideo)).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(getString(R.string.tabRadio)).setTabListener(this));


        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        checkUpdateNoticias();
    }


    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        Log.d(LOG_TAG, "Item seleccionado: " + item);

        switch (item.getItemId()) {

            case R.id.actionConfig:
                final Intent intentConfig = new Intent(this, PreferencesActivity.class);
                startActivity(intentConfig);
                return true;
            case R.id.actionInfo:
                final Intent intentInfo = new Intent(this, AboutActivity.class);
                startActivity(intentInfo);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }


    /**
     * Chequea si ha pasado mucho tiempo desde la ultima vez que se actualizaron las noticias y tiene que hacerlo.
     */
    private void checkUpdateNoticias() {

        if (isNeededRefreshTime(this)) {
            final Intent msgIntent = new Intent(this, FeedUpdaterService.class);
            msgIntent.putExtra(FeedUpdaterService.FEED_CATEGORIA, Constants.Categoria.NOTICIAS);
            msgIntent.putExtra(FeedUpdaterService.SOURCE_NOTICIAS_BROADCAST, BroadcastConstants.BROADCAST_NOTICIAS);
            startService(msgIntent);
        }
    }

    /**
     * True si es necesario refrescar. Esto se produce si hace más de 60 minutos que se actualizó el feed de noticias.
     *
     * @return
     */
    protected boolean isNeededRefreshTime(Context context) {
        final String fecha = PreferencesUtils.getPreferenceValue(Constants.PREF_LAST_UPDATE_NOTICIAS, context);

        final Calendar now = Calendar.getInstance();
        if (fecha != null) {
            final Calendar lastUpdate = DateUtils.stringToCalendar(fecha);
            lastUpdate.add(Calendar.MINUTE, Constants.REFRESH_NOTICIAS_MIN_TIME);
            return lastUpdate.before(now);
        } else {
            return false;
        }
    }
}