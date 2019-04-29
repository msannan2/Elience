package com.HCI.elience;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

public class MenuActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabsAccessorAdapter mTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setTransparency();
        mToolbar = findViewById(R.id.main_page_toolbar);
        mToolbar.setLogo(R.drawable.elience_logo_small);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Welcome");
        mViewPager=findViewById(R.id.main_tabs_pager);
        mTabsAdapter=new TabsAccessorAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout=findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }
    void setTransparency() {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }
}
