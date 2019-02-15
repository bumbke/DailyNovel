package main.dailynovel;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import main.dailynovel.Fragments.LibraryFragment;
import main.dailynovel.Objects.Crawler;

public class LauncherActivity extends AppCompatActivity {
    ImageView iviewLogo;
    TextView tvLogo;
    TextView tvVersion;
    static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        //      SET HIDE STATUS BAR
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //      SET ANIMATION
        iviewLogo = findViewById(R.id.iviewLogo);
        tvLogo = findViewById(R.id.tvLogo);
        tvVersion = findViewById(R.id.tvVersion);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        iviewLogo.startAnimation(myanim);
        tvLogo.startAnimation(myanim);
        tvVersion.startAnimation(myanim);

        //    SET SPLASH_TIME_OUT
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(LauncherActivity.this, LoginActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
