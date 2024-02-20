package com.varenyky.naziplane;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserData {
    private static String name;
    private static String login;
    private static String lastName;
    private static String userMail;
    private static boolean isChoose;
    public final String getLogin() {
        return login;
    }

    public final void setLogin(String login) {
        UserData.login = login;
    }
    public final boolean isLogin() {
        return isLogin;
    }

    public final void setIsLogin(boolean isLogin) {
        UserData.isLogin = isLogin;
    }

    private static boolean isLogin = false;
    public final String getLastName() {
        return lastName;
    }

    public final void setLastName(String lastName) {
        UserData.lastName = lastName;
    }

    public final String getUserMail() {
        return userMail;
    }

    public final void setUserMail(String userMail) {
        UserData.userMail = userMail;
    }


    private static String userId;
    public final String getName() {
        return name;
    }
    public final String getUserId() {
        return userId;
    }

    public final void setName(String name) {
        UserData.name = name;
    }
    public final void setUserId(String userId) {
        UserData.userId = userId;
    }

    public final boolean getIsChoose() {
        return UserData.isChoose;
    }

    public final void setIsChoose(boolean isChoose) {
        UserData.isChoose = isChoose;
    }
}