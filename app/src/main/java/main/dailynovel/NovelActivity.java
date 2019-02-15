package main.dailynovel;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import main.dailynovel.Objects.*;

public class NovelActivity extends AppCompatActivity {
    Button btnChapterList, btnReading, btnToggle, btnToggleLike, btnComment;
    TextView txtName, txtAuthor, txtStatus, txtChapter, txtType, txtPreface;
    ImageView imgCover;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Bitmap bm = null;
    Novel novel;
    boolean isLikeCheck = false, isSubsCheck = false, isSubExist = false, isLikeExist = false;
    String useremail, username;
    int position, positionSub, positionLike;
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Novel> novels = new ArrayList<>();
    ArrayList<String> userSubs = new ArrayList<>(), userLiked = new ArrayList<>(), userViewed = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        Intent intent = getIntent();
        useremail = intent.getExtras().getString("useremail");
        username = intent.getExtras().getString("username");
        novel = (Novel) intent.getSerializableExtra("Object");

        btnToggle = findViewById(R.id.toggleButton);
        btnToggleLike = findViewById(R.id.btnToggleLike);
        final Drawable topnon = getResources().getDrawable(R.drawable.ic_unlike);
        final Drawable topcheck = getResources().getDrawable(R.drawable.ic_like);
        final Drawable topnon1 = getResources().getDrawable(R.drawable.ic_notify_noncheck);
        final Drawable topcheck1 = getResources().getDrawable(R.drawable.ic_notify_checked);

        if(useremail.equalsIgnoreCase("null")) {
            btnToggleLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(NovelActivity.this).setIcon(R.mipmap.ic_warning).setTitle("Yêu cầu đăng nhập")
                            .setMessage("Bạn phải đăng nhập để sử dụng tính năng này!")
                            .setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(NovelActivity.this, LoginActivity.class);
                                    startActivity(i);
                                }
                            }).setNegativeButton("Bỏ qua", null).show();
                }
            });

            btnToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(NovelActivity.this).setIcon(R.mipmap.ic_warning).setTitle("Yêu cầu đăng nhập")
                            .setMessage("Bạn phải đăng nhập để sử dụng tính năng này!")
                            .setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(NovelActivity.this, LoginActivity.class);
                                    startActivity(i);
                                }
                            }).setNegativeButton("Bỏ qua", null).show();
                }
            });
        } else {
            if (!isEmpty()) {
                getUser();
                userSubs.clear();
                userLiked.clear();
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUserMail().equalsIgnoreCase(useremail)) {
                        position = i;
                    }
                }

                userLiked = users.get(position).getNovelLiked();
                for (int i = 0; i < userLiked.size(); i++) {
                    if (userLiked.get(i).equalsIgnoreCase(novel.getNovelID())) {
                        btnToggleLike.setCompoundDrawablesWithIntrinsicBounds(null, topcheck, null, null);
                        isLikeCheck = true;
                        isLikeExist = true;
                        positionLike = i;
                        break;
                    }
                }

                userSubs = users.get(position).getNovelSubscribe();
                for (int i = 0; i < userSubs.size(); i++) {
                    if (userSubs.get(i).equalsIgnoreCase(novel.getNovelID())) {
                        btnToggle.setCompoundDrawablesWithIntrinsicBounds(null, topcheck1, null, null);
                        isSubsCheck = true;
                        isSubExist = true;
                        positionSub = i;
                        break;
                    }
                }
            }

            btnToggleLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isLikeCheck) {
                        btnToggleLike.setCompoundDrawablesWithIntrinsicBounds(null, topnon, null, null);
                    } else {
                        btnToggleLike.setCompoundDrawablesWithIntrinsicBounds(null, topcheck, null, null);
                    }
                    isLikeCheck = !isLikeCheck; // reverse
                }
            });

            btnToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isSubsCheck) {
                        btnToggle.setCompoundDrawablesWithIntrinsicBounds(null, topnon1, null, null);
                    } else {
                        btnToggle.setCompoundDrawablesWithIntrinsicBounds(null, topcheck1, null, null);
                    }
                    isSubsCheck = !isSubsCheck; // reverse
                }
            });
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        imgCover = (ImageView) findViewById(R.id.imgCover);
        txtName = (TextView) findViewById(R.id.txtName);
        txtAuthor = (TextView) findViewById(R.id.txtAuthor);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtChapter = (TextView) findViewById(R.id.txtChapter);
        txtType = (TextView) findViewById(R.id.txtGenre);

        collapsingToolbarLayout.setTitle(novel.getNovelName());
        txtName.setText(novel.getNovelName());
        txtAuthor.setText(novel.getNovelAuthors());
        txtStatus.setText(novel.getNovelStatus());
        txtChapter.setText(String.valueOf(novel.getNovelChapter()));
        txtType.setText(novel.getNovelType());

        String coverURL = novel.getNovelCover();
        if(coverURL.substring(coverURL.indexOf("images/story/")).equalsIgnoreCase("images/story/.jpg")){
            imgCover.setImageResource(R.drawable.cover);
        } else {
            try {
                InputStream in = new java.net.URL(novel.getNovelCover()).openStream();
                bm = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgCover.setImageBitmap(bm);
        }
        //       adding transparency
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        //      SET HIDE STATUS BAR
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnChapterList = (Button) findViewById(R.id.btnChapterList);
        btnChapterList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NovelActivity.this, NovelChapterListActivity.class);
                intent.putExtra("itemID", novel.getNovelID());
                intent.putExtra("itemChapter", novel.getNovelChapter());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        txtPreface = (TextView) findViewById(R.id.txtPreface);
        try {
            txtPreface.setText(Crawler.getNovelDesc(novel.getNovelID()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnReading = (Button) findViewById(R.id.btnReading);
        btnReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(NovelActivity.this, NovelReaderActivity.class);
                    intent.putExtra("itemID", novel.getNovelID());
                    intent.putExtra("itemChapter", 1);
                    intent.putExtra("itemName", Crawler.getChapterTitle(novel.getNovelID(), 1));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnComment = (Button) findViewById(R.id.imbComment);
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launching facebook comments activity
                Intent intent = new Intent(NovelActivity.this, FbCommentsActivity.class);

                // passing the article url
                intent.putExtra("url", "http://truyenfull.vn/linh-vu-thien-ha/");
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!useremail.equalsIgnoreCase("null")) {
            if (isSubExist) {
                if (isSubsCheck) {
                    userSubs.set(positionSub, novel.getNovelID());
                } else {
                    userSubs.set(positionSub,"null");
                }
            } else {
                if (isSubsCheck) {
                    userSubs.add(novel.getNovelID());
                } else {
                    userSubs.add("null");
                }
            }
            if (isLikeExist) {
                if (isLikeCheck) {
                    userLiked.set(positionLike, novel.getNovelID());
                } else {
                    userLiked.set(positionLike, "null");
                }
            } else {
                if (isLikeCheck) {
                    userLiked.add(novel.getNovelID());
                } else {
                    userLiked.add("null");
                }
            }

            putUser();
            Log.d("testObj", "Sub " + isSubsCheck);
            Log.d("testObj", "Like " + isLikeCheck);
            putNovel();
        }
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }

    private void putUser() {
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();
        if (isEmpty()) {
            dbAdapter.addUser(useremail, username, dbAdapter.convertArrayToString(userSubs.toArray(new String[userSubs.size()])), dbAdapter.convertArrayToString(userLiked.toArray(new String[userLiked.size()])));
        } else {
            if (users.get(position).getUserMail().equalsIgnoreCase(useremail)) {
                Log.d("testObj", "Edit");
                dbAdapter.editNovelSubs(useremail, dbAdapter.convertArrayToString(userSubs.toArray(new String[userSubs.size()])));
                dbAdapter.editNovelLiked(useremail, dbAdapter.convertArrayToString(userLiked.toArray(new String[userLiked.size()])));
            } else {
                Log.d("testObj", "Add 2" + useremail + users.get(position).getUserMail());
                dbAdapter.addUser(useremail, username, dbAdapter.convertArrayToString(userSubs.toArray(new String[userSubs.size()])), dbAdapter.convertArrayToString(userLiked.toArray(new String[userLiked.size()])));
            }
        }
        dbAdapter.closeDB();
    }

    private void getUser() {
        ArrayList<String> subs = null, like = null;
        users.clear();
        DBAdapter dbAdapter = new DBAdapter(NovelActivity.this);
        dbAdapter.openDB();

        Cursor cursor = dbAdapter.getUser();
        while (cursor.moveToNext()) {
            if(cursor.getString(2)!=null) {
                subs = new ArrayList<>(Arrays.asList(dbAdapter.convertStringToArray(cursor.getString(2))));
            }

            if(cursor.getString(3)!=null) {
                like = new ArrayList<>(Arrays.asList(dbAdapter.convertStringToArray(cursor.getString(3))));
            }
            User user = new User(cursor.getString(0), cursor.getString(1), subs, like);
            users.add(user);
            Log.d("testObj", "user " + user.getUserMail() + ", " + user.getNovelSubscribe().get(0));
        }
        dbAdapter.closeDB();
    }

    private void putNovel() {
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();
        boolean isExist = false;

        if (dbAdapter.isNovelEmpty() > 0) {
            for (int i = 0; i < novels.size(); i++) {
                if (novels.get(i).getNovelID().equalsIgnoreCase(novel.getNovelID())) {
                    isExist = true;
                }
            }

            if (!isExist) {
                dbAdapter.addNovel(novel.getNovelID(), novel.getNovelName(), novel.getNovelAuthors(), novel.getNovelType(), novel.getNovelStatus(), novel.getNovelCover(), novel.getNovelChapter());
            }
        } else {
            dbAdapter.addNovel(novel.getNovelID(), novel.getNovelName(), novel.getNovelAuthors(), novel.getNovelType(), novel.getNovelStatus(), novel.getNovelCover(), novel.getNovelChapter());
        }
        dbAdapter.closeDB();
    }

    private boolean isEmpty () {
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();

        if(dbAdapter.isUserEmpty()>0) {
            return false;
        }
        return true;
    }
}