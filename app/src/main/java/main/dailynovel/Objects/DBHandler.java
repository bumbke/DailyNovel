package main.dailynovel.Objects;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by l3umb on 12/5/2017.
 */

public class DBHandler extends SQLiteOpenHelper {
    public static String DBName = "Setting";
    public static int DBVersion = 1;
    Context context;

    public static String strSeparator = "__,__";

    public static String settingTab = "setting";
    public static String userTab = "user";
    public static String novelTab = "novel";

    public static String fontSize = "fontsize";
    public static String themeBackground = "background";
    public static String themeTextColor = "textcolor";

    public static String ID = "id";
    public static String userName = "name";
    public static String novelSubs = "subscribe";
    public static String novelLiked = "like";
    public static String novelViewed = "view";

    public static String novelName = "name";
    public static String novelAuthor = "author";
    public static String novelType = "type";
    public static String novelStatus = "status";
    public static String novelCover = "cover";
    public static String novelChapter = "chapter";


    public static String createSettingTB = "CREATE TABLE " + settingTab + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                            + fontSize + " TEXT NOT NULL, "
                                            + themeBackground + " TEXT NOT NULL, "
                                            + themeTextColor + " TEXT NOT NULL);";
    public static String createUserTB = "CREATE TABLE " + userTab + " (" + ID + " TEXT PRIMARY KEY, "
                                        + userName + " TEXT NOT NULL, " + novelSubs + " TEXT, "
                                        + novelLiked + " TEXT);";
    public static String createNovelTB = "CREATE TABLE " + novelTab + " (" + ID + " TEXT PRIMARY KEY, "
                                        + novelName + " TEXT, " + novelAuthor + " TEXT, "
                                        + novelType + " TEXT, " + novelStatus + " TEXT, "
                                        + novelCover + " TEXT, " + novelChapter + " INTEGER);";

    static String dropSettingTB = "DROP TABLE IF EXISTS " + settingTab ;
    static String dropUserTB = "DROP TABLE IF EXISTS " + userName;
    static String dropNovelTB = "DROP TABLE IF EXISTS " + novelTab;

    public DBHandler(Context context) {
        super(context, DBName, null, DBVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createSettingTB);
        sqLiteDatabase.execSQL(createUserTB);
        sqLiteDatabase.execSQL(createNovelTB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(dropSettingTB);
        sqLiteDatabase.execSQL(dropUserTB);
        sqLiteDatabase.execSQL(dropNovelTB);
        onCreate(sqLiteDatabase);
    }
}

