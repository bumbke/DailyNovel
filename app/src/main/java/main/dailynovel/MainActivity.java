package main.dailynovel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import main.dailynovel.Fragments.*;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    MenuItem prevMenuItem;
    ViewPager vpContentMain;
    Intent intent;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        vpContentMain.setCurrentItem(0, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        intent = getIntent();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnavigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        //Nav listener
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomnavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_library:
                        vpContentMain.setCurrentItem(0, false);
                        break;
                    case R.id.nav_bookcase:
                        vpContentMain.setCurrentItem(1, false);
                        break;
                    case R.id.nav_wall:
                        vpContentMain.setCurrentItem(2, false);
                        break;
                    case R.id.nav_user:
                        vpContentMain.setCurrentItem(3, false);
                        break;
                }
                return true;
            }
        });
        //Hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        vpContentMain = (ViewPager) findViewById(R.id.content);
        vpContentMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setUpFragment(vpContentMain);
    }

    public void setUpFragment(ViewPager vpContentMain) {
        MainContentViewPager mcViewPager = new MainContentViewPager(getSupportFragmentManager());
        LibraryFragment libraryFragment = new LibraryFragment();
        BookcaseFragment bookcaseFragment = new BookcaseFragment();
        WallFragment wallFragment = new WallFragment();
        UserFragment userFragment = new UserFragment();

        Bundle bundle = new Bundle();
        bundle.putString("accountname", intent.getExtras().getString("accountname"));
        bundle.putString("accountemail", intent.getExtras().getString("accountemail"));
        bundle.putString("accountimg", intent.getExtras().getString("accountimg"));
        bundle.putString("isfacebook", intent.getExtras().getString("isfacebook"));
        userFragment.setArguments(bundle);
        libraryFragment.setArguments(bundle);
        bookcaseFragment.setArguments(bundle);

        mcViewPager.addFragment(libraryFragment);
        mcViewPager.addFragment(bookcaseFragment);
        mcViewPager.addFragment(wallFragment);
        mcViewPager.addFragment(userFragment);

        vpContentMain.setAdapter(mcViewPager);
        vpContentMain.setOffscreenPageLimit(3);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(R.mipmap.ic_warning).setTitle("Exit")
                .setMessage("Bạn có chắc muốn thoát?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("Không", null).show();
    }
}

