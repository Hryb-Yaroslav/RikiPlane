package com.varenyky.naziplane;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class AccountActivity extends AppCompatActivity {
    private ImageView bgMenu;
    private ImageButton menuBtn;
    private ImageButton closeBtn;
    private ImageButton accBtn;
    private ImageButton searchBtn;
    private ImageButton settingsBtn;
    private ImageButton chooseAirBtn;
    private ImageButton exit;
    private String id;
    private TextView accText;
    private TextView searchText;
    private TextView settingsText;
    private TextView chooseAirText;
    private TextView chooseTicketText;
    private TextView accountName;
    private TextView lastName;
    private TextView userMail;
    private ImageButton chooseTicketBtn;
    private UserData userData = new UserData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        bgMenu = findViewById(R.id.bg_menu);
        menuBtn = findViewById(R.id.account_menu_btn);

        closeBtn = findViewById(R.id.close_menu_btn);
        accBtn = findViewById(R.id.acc_btn);
        searchBtn = findViewById(R.id.search_fly_btn);
        settingsBtn = findViewById(R.id.settings_btn);
        chooseAirBtn = findViewById(R.id.choose_air_btn);
        accText = findViewById(R.id.acc_text);
        searchText = findViewById(R.id.search_fly_text);
        settingsText = findViewById(R.id.settings_text);
        chooseAirText = findViewById(R.id.choose_air_text);
        chooseTicketBtn = findViewById(R.id.my_tickets_btn);
        chooseTicketText = findViewById(R.id.my_tickets_text);

        accountName = findViewById(R.id.name_account);
        lastName = findViewById(R.id.last_name_account);
        userMail = findViewById(R.id.mail_account);

        exit = findViewById(R.id.exit_account_btn);

        menuBtn.setOnClickListener(view -> {
            clickMenu();
        });
        closeBtn.setOnClickListener(view -> {
            clickClose();
        });
        searchBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AccountActivity.this, ChooseTicketActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        exit.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("id", null);
            myEdit.apply();
            Intent intent = new Intent(AccountActivity.this, MenuActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        accountName.setText(userData.getName());
        lastName.setText(userData.getLastName());
        userMail.setText(userData.getUserMail());
        hideUi();
    }
    public void ButtonClick(View view) {

    }
    private void clickMenu() {
        bgMenu.setVisibility(View.VISIBLE);
        menuBtn.setVisibility(View.INVISIBLE);
        closeBtn.setVisibility(View.VISIBLE);
        accBtn.setVisibility(View.VISIBLE);
        searchBtn.setVisibility(View.VISIBLE);
        settingsBtn.setVisibility(View.VISIBLE);
        chooseAirBtn.setVisibility(View.VISIBLE);
        chooseTicketText.setVisibility(View.VISIBLE);
        chooseTicketBtn.setVisibility(View.VISIBLE);

        accText.setVisibility(View.VISIBLE);
        searchText.setVisibility(View.VISIBLE);
        settingsText.setVisibility(View.VISIBLE);
        chooseAirText.setVisibility(View.VISIBLE);
    }

    private void clickClose() {
        bgMenu.setVisibility(View.INVISIBLE);
        menuBtn.setVisibility(View.VISIBLE);
        closeBtn.setVisibility(View.INVISIBLE);
        accBtn.setVisibility(View.INVISIBLE);
        searchBtn.setVisibility(View.INVISIBLE);
        settingsBtn.setVisibility(View.INVISIBLE);
        chooseAirBtn.setVisibility(View.INVISIBLE);
        chooseTicketText.setVisibility(View.INVISIBLE);
        chooseTicketBtn.setVisibility(View.INVISIBLE);

        accText.setVisibility(View.INVISIBLE);
        searchText.setVisibility(View.INVISIBLE);
        settingsText.setVisibility(View.INVISIBLE);
        chooseAirText.setVisibility(View.INVISIBLE);
    }

    private void hideUi() {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LOW_PROFILE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat insetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
            if (insetsController != null) {
                insetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                insetsController.hide(WindowInsetsCompat.Type.systemBars());
            }
        } else {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
