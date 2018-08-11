package com.robertsimoes.conscious.ui;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.robertsimoes.conscious.R;
import com.robertsimoes.conscious.Const;
import com.robertsimoes.conscious.UserSettings;
import com.pressurelabs.swissarmyknife.GTools;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.viewpager_main)
    ViewPager mViewPager;
    private MainPagerAdapter pagerAdapter;
    private MaterialDialog currentDialog;
    private UserSettings settings = UserSettings.getInstance();

    private final String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        if(getIntent().getAction()!=null) {
            if (getIntent().getAction().equals(Const.ACTION_ONBOARD)) {
                startActivity(new Intent(MainActivity.this,OnBoardingActivity.class));
            }
        }


        /* ====== Init Pager View ====== */
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),MainActivity.this);
        mViewPager.setAdapter(pagerAdapter);

        /* ====== Init Drawer Layout ======*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        setNavUserName("");

        /* ====== Init Fab ====== */

        final FloatingActionButton fabSpeak = (FloatingActionButton) findViewById(R.id.fab_main_speak);
        final FloatingActionButton fabExpress = (FloatingActionButton) findViewById(R.id.fab_main_express);
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.fab_menu_main_activity);

        fabSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent speakIntent = new Intent(MainActivity.this,ExpressActivity.class);
                speakIntent.setAction(Const.ACTION_SPEAK);
                startActivity(speakIntent);
            }
        });
        fabExpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent expressIntent = new Intent(MainActivity.this,ExpressActivity.class);
                expressIntent.setAction(Const.ACTION_EXPRESS);
                startActivity(expressIntent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);

        MenuItem debug = menu.findItem(R.id.action_debug);
        debug.setVisible(false);


        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search_thoughts).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultsActivity.class)));
        searchView.setQueryHint(getResources().getString(R.string.action_search_thoughts));
        /* Docs:
            The call to getSearchableInfo() obtains a SearchableInfo object that
            is created from the searchable configuration XML file.
            When the searchable configuration is correctly associated with your SearchView,
            the SearchView starts an activity with the ACTION_SEARCH
         */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search_thoughts:

                return true;

            case R.id.action_debug:
                startActivity(new Intent(MainActivity.this,DebuggerActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this,SettingsActivity.class));

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_nav_terms_of_use:
                startActivity(new Intent(MainActivity.this,TermsOfUseActivity.class));
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        reloadContent();
        setNavUserName("");
    }

    /**
     * Sets user name to the supplied parameter, or stranger if blank
     *
     * @param name
     */
    private void setNavUserName(String name) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView userName = (TextView) header.findViewById(R.id.tv_nav_header_user_hello);
        if (name.equals("")) {
            name=settings.getPrefs(MainActivity.this).getString(UserSettings.KEY_USER_NAME,"Stranger");
        }
        userName.setText(name);
    }

    /**
     * Reloads all content from the pager adapter
     */
    private void reloadContent() {
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),MainActivity.this);
        mViewPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(currentDialog!=null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        reloadContent();
    }

}
