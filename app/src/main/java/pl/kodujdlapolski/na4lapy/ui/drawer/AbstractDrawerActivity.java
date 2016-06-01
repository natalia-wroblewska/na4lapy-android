package pl.kodujdlapolski.na4lapy.ui.drawer;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import pl.kodujdlapolski.na4lapy.R;

/**
 * Created by Malgorzata Syska on 2016-03-12.
 */
public abstract class AbstractDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;
    protected DrawerActivityHandler handler;
    private final Handler drawerCloseDelay = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        if (findViewById(R.id.toolbar) != null) {
            setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        handler = new DrawerActivityHandler(this);
        handler.setDrawer();
    }

    protected abstract int getContentView();

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        drawerCloseDelay.removeCallbacksAndMessages(null);
        // so the nav drawer closes smoothly
        drawerCloseDelay.postDelayed(() -> handler.onNavigationItemSelected(item), 250);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void callSuperOnCreate(Bundle saved) {
        super.onCreate(saved);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.onResume();
    }
}
