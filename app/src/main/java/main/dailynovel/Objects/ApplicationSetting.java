package main.dailynovel.Objects;

import java.io.Serializable;

/**
 * Created by l3umb on 12/5/2017.
 */

public class ApplicationSetting implements Serializable {
    private String userMail;
    public String getUserMail() {
        return userMail;
    }
    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    private int fontSize;
    public int getFontSize() {
        return fontSize;
    }
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    private int themeBackground;
    public int getThemeBackground() {
        return themeBackground;
    }
    public void setThemeBackground(int themeBackground) {
        this.themeBackground = themeBackground;
    }

    private int themeTextColor;
    public int getThemeTextColor() {
        return themeTextColor;
    }
    public void setThemeTextColor(int themeTextColor) {
        this.themeTextColor = themeTextColor;
    }

    public ApplicationSetting() {

    }
}
