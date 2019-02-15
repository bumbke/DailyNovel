package main.dailynovel.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {
    Context context;
    SQLiteDatabase db;
    DBHandler dbHandler;

    public DBAdapter(Context context) {
        this.context = context;
        dbHandler = new DBHandler(context);
    }

    public DBAdapter openDB() {
        db = dbHandler.getWritableDatabase();
        return this;
    }

    public void closeDB() {
        dbHandler.close();
    }

    public long addSetting(String fontsize, String background, String textcolor) {
        try {
            ContentValues values = new ContentValues();
            values.put(DBHandler.fontSize, fontsize);
            values.put(DBHandler.themeBackground, background);
            values.put(DBHandler.themeTextColor, textcolor);

            return db.insert(DBHandler.settingTab, DBHandler.ID, values);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public long addUser(String id, String name, String subs, String liked) {
        try {
            ContentValues values = new ContentValues();
            values.put(DBHandler.ID, id);
            values.put(DBHandler.userName, name);
            values.put(DBHandler.novelSubs, subs);
            values.put(DBHandler.novelLiked, liked);

            return db.insert(DBHandler.userTab, DBHandler.ID, values);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public long addNovel (String id, String name, String author, String type, String status, String cover, int chapter) {
        try {
            ContentValues values = new ContentValues();
            values.put(DBHandler.ID, id);
            values.put(DBHandler.novelName, name);
            values.put(DBHandler.novelAuthor, author);
            values.put(DBHandler.novelType, type);
            values.put(DBHandler.novelStatus, status);
            values.put(DBHandler.novelCover, cover);
            values.put(DBHandler.novelChapter, chapter);

            return db.insert(DBHandler.novelTab, DBHandler.ID, values);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public long editTextSize(int id, String textsize) {
        try {
            ContentValues values=new ContentValues();
            values.put(DBHandler.fontSize, textsize);
            return db.update(DBHandler.settingTab, values, DBHandler.ID + "=?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long editBackground(int id, String background) {
        try {
            ContentValues values=new ContentValues();
            values.put(DBHandler.themeBackground, background);
            return db.update(DBHandler.settingTab, values, DBHandler.ID + "=?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long editTextColor(int id, String textcolor) {
        try {
            ContentValues values=new ContentValues();
            values.put(DBHandler.themeTextColor, textcolor);
            return db.update(DBHandler.settingTab, values, DBHandler.ID + "=?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long editNovelSubs(String id, String subs) {
        try {
            ContentValues values=new ContentValues();
            values.put(DBHandler.novelSubs, subs);
            return db.update(DBHandler.userTab, values, DBHandler.ID + "=?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long editNovelLiked (String id, String liked) {
        try {
            ContentValues values=new ContentValues();
            values.put(DBHandler.novelLiked, liked);
            return db.update(DBHandler.userTab, values, DBHandler.ID + "=?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    //Novel
    public long editNovelName (String id, String name) {
        try {
            ContentValues values=new ContentValues();
            values.put(DBHandler.novelName, name);
            return db.update(DBHandler.novelTab, values, DBHandler.ID + "=?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long editNovelAuthor (String id, String author) {
        try {
            ContentValues values=new ContentValues();
            values.put(DBHandler.novelAuthor, author);
            return db.update(DBHandler.novelTab, values, DBHandler.ID + "=?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long editNovelType (String id, String type) {
        try {
            ContentValues values=new ContentValues();
            values.put(DBHandler.novelType, type);
            return db.update(DBHandler.novelTab, values, DBHandler.ID + "=?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long editNovelStatus (String id, String status) {
        try {
            ContentValues values=new ContentValues();
            values.put(DBHandler.novelStatus, status);
            return db.update(DBHandler.novelTab, values, DBHandler.ID + "=?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long editNovelCover (String id, String cover) {
        try {
            ContentValues values=new ContentValues();
            values.put(DBHandler.novelName, cover);
            return db.update(DBHandler.novelTab, values, DBHandler.ID + "=?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long editNovelChapter (String id, int chapter) {
        try {
            ContentValues values=new ContentValues();
            values.put(DBHandler.novelName, chapter);
            return db.update(DBHandler.novelTab, values, DBHandler.ID + "=?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+ DBHandler.strSeparator;
            }
        }
        return str;
    }

    public String[] convertStringToArray(String str){
        return str.split(DBHandler.strSeparator);
    }

    public Cursor getSetting () {
        String[] columns = {DBHandler.ID, DBHandler.fontSize, DBHandler.themeBackground, DBHandler.themeTextColor};
        return db.query(DBHandler.settingTab, columns, null, null, null, null, null);
    }

    public Cursor getUser () {
        String[] columns = {DBHandler.ID, DBHandler.userName, DBHandler.novelSubs, DBHandler.novelLiked};
        return db.query(DBHandler.userTab, columns, null, null, null, null, null);
    }

    public Cursor getNovel() {
        String[] columns = {DBHandler.ID, DBHandler.novelName, DBHandler.novelAuthor, DBHandler.novelType, DBHandler.novelStatus, DBHandler.novelCover, DBHandler.novelChapter};
        return db.query(DBHandler.novelTab, columns, null, null, null, null, null);
    }

    public int isSettingEmpty() {
        String s = "SELECT count(*) FROM setting";
        Cursor mcursor = db.rawQuery(s, null);
        mcursor.moveToFirst();
        return mcursor.getInt(0);
    }

    public int isUserEmpty() {
        String s = "SELECT count(*) FROM user";
        Cursor mcursor = db.rawQuery(s, null);
        mcursor.moveToFirst();
        return mcursor.getInt(0);
    }

    public int isNovelEmpty() {
        String s = "SELECT count(*) FROM novel";
        Cursor mcursor = db.rawQuery(s, null);
        mcursor.moveToFirst();
        return mcursor.getInt(0);
    }
}
