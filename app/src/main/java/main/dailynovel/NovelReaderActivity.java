package main.dailynovel;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.view.MotionEvent;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionBarContainer;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import main.dailynovel.Objects.Crawler;
import main.dailynovel.Objects.DBAdapter;

public class NovelReaderActivity extends AppCompatActivity {
    ImageButton imbSetting, imbBack, imbNext;
    TextView txtTitle, fullscreen_content;
    ImageButton imgComment;
    Dialog dialog;
    String itemID, itemName, contentSize, contentBackground, contentColor;
    int itemChapter;
    ColorDrawable drawable;
    ScrollView scrollView;
    Toolbar toolbar;

    private static final boolean AUTO_HIDE = true;

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {

            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
//            dialog.hide();
        }
    };

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        Intent intent = getIntent();

        //Hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        findViewById(R.id.toolbar).setOnTouchListener(mDelayHideTouchListener);

        itemID = intent.getExtras().getString("itemID");
        itemChapter = intent.getExtras().getInt("itemChapter");
        itemName = intent.getExtras().getString("itemName");

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        scrollView = (ScrollView)findViewById(R.id.scrollView) ;


        imbSetting = (ImageButton) findViewById(R.id.imbSetting);
        imbSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSettingDialog();
            }
        });

        imbBack = (ImageButton) findViewById(R.id.imbBackround);
        imbNext = (ImageButton) findViewById(R.id.imbNextround);

        imbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fullscreen_content.setText(Crawler.getChapterContent(itemID, itemChapter--));
                    txtTitle.setText(Crawler.getChapterTitle(itemID, itemChapter));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        imbNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fullscreen_content.setText(Crawler.getChapterContent(itemID, itemChapter++));
                    txtTitle.setText(Crawler.getChapterTitle(itemID, itemChapter));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        fullscreen_content = (TextView) findViewById(R.id.fullscreen_content);
        prepareContent();

//        fullscreen_content.setText(itemName);
        try {
            fullscreen_content.setText(Crawler.getChapterContent(itemID, itemChapter));
        } catch (Exception e) {
            e.printStackTrace();
        }

        imgComment = (ImageButton) findViewById(R.id.imbComment);
        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launching facebook comments activity
                Intent intent = new Intent(NovelReaderActivity.this, FbCommentsActivity.class);

                // passing the article url
                intent.putExtra("url", "http://truyenfull.vn/linh-vu-thien-ha/");
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }



    private void getSettingDialog() {
        dialog = new Dialog(NovelReaderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_reader_setting);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        ImageButton imbCancel = (ImageButton)dialog.findViewById(R.id.imbCancel);
        imbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        ImageButton imbDecrease = (ImageButton) dialog.findViewById(R.id.imbDecrease);
        imbDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float size = fullscreen_content.getTextSize();
//                Float title = txtTitle.getTextSize();
//                txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, (title-2f));
                fullscreen_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, (size-2f));


            }
        });

        ImageButton imbIncrease = (ImageButton)dialog.findViewById(R.id.imbIncrease);
        imbIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float size = fullscreen_content.getTextSize();
//                Float title = txtTitle.getTextSize();
////                txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, (title+2f));
                fullscreen_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, (size+2f));
            }
        });

        ImageButton imbTheme1 = (ImageButton) dialog.findViewById(R.id.imbTheme1);
        imbTheme1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullscreen_content.setBackgroundColor(getResources().getColor(R.color.white));
                fullscreen_content.setTextColor(getResources().getColor(R.color.black));
//                txtTitle.setBackgroundColor(getResources().getColor(R.color.white));
//                txtTitle.setTextColor(getResources().getColor(R.color.black));
            }
        });

        ImageButton imbTheme2 = (ImageButton) dialog.findViewById(R.id.imbTheme2);
        imbTheme2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullscreen_content.setBackgroundColor(getResources().getColor(R.color.backbrown));
                fullscreen_content.setTextColor(getResources().getColor(R.color.brown));
//                txtTitle.setBackgroundColor(getResources().getColor(R.color.backbrown));
//                txtTitle.setTextColor(getResources().getColor(R.color.brown));
            }
        });

        ImageButton imbTheme3 = (ImageButton) dialog.findViewById(R.id.imbTheme3);
        imbTheme3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullscreen_content.setBackgroundColor(getResources().getColor(R.color.black));
                fullscreen_content.setTextColor(getResources().getColor(R.color.white));
//                txtTitle.setBackgroundColor(getResources().getColor(R.color.black));
//                txtTitle.setTextColor(getResources().getColor(R.color.white));
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        drawable = (ColorDrawable) fullscreen_content.getBackground();
//        drawable = (ColorDrawable) txtTitle.getBackground();
        putSetting(String.valueOf(fullscreen_content.getTextSize()), String.valueOf(drawable.getColor()), String.valueOf(fullscreen_content.getCurrentTextColor()));
//        putSetting(String.valueOf(txtTitle.getTextSize()), String.valueOf(drawable.getColor()), String.valueOf(txtTitle.getCurrentTextColor()));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }

    private void prepareContent() {
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();

        if(dbAdapter.isSettingEmpty() > 0) {
            fullscreen_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, Float.parseFloat(getSetting().get(0)));
            fullscreen_content.setBackgroundColor(Integer.parseInt(getSetting().get(1)));
            fullscreen_content.setTextColor(Integer.parseInt(getSetting().get(2)));

//            txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, Float.parseFloat(getSetting().get(0)));
//            txtTitle.setBackgroundColor(Integer.parseInt(getSetting().get(1)));
//            txtTitle.setTextColor(Integer.parseInt(getSetting().get(2)));
        }
        dbAdapter.closeDB();
    }

    private void putSetting(String fontSize, String themeBackground, String themeTextColor) {
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();

        if(dbAdapter.isSettingEmpty()>0) {
            dbAdapter.editTextSize(1, fontSize);
            dbAdapter.editBackground(1, themeBackground);
            dbAdapter.editTextColor(1, themeTextColor);
        } else {
            dbAdapter.addSetting(fontSize, themeBackground, themeTextColor);
        }
        dbAdapter.closeDB();
    }

    private ArrayList<String> getSetting() {
        ArrayList<String> array = new ArrayList<>();
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();

        Cursor cursor = dbAdapter.getSetting();
        while (cursor.moveToNext()) {
            contentSize = cursor.getString(1);
            contentBackground = cursor.getString(2);
            contentColor = cursor.getString(3);
            array.add(contentSize);
            array.add(contentBackground);
            array.add(contentColor);
        }
        dbAdapter.closeDB();
        return array;
    }


}
