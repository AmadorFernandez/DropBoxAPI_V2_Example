package com.amador.androidbox;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


/**
 * @author Amador Fernandez Gonzalez
 *         <p>
 *         Actividad principal de la aplicación que maneja los fragments
 */
public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterPager adapterPager;
    private CoordinatorLayout parent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity();


    }

    public void startActivity() {


        parent = (CoordinatorLayout) findViewById(R.id.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapterPager = new AdapterPager(getSupportFragmentManager());
        setSupportActionBar(toolbar);
        viewPager.setAdapter(adapterPager);
        tabLayout.setupWithViewPager(viewPager, true);
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_local).setText("Dispositivo"), 0);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_cloud).setText("Dropbox"), 1);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());


    }

    public void setResultMsg(String msg) {

        Snackbar.make(parent, msg, Snackbar.LENGTH_LONG).show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                startActivity();
            }
        });

    }

}
