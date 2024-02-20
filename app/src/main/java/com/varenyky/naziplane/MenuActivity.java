package com.varenyky.naziplane;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MenuActivity extends AppCompatActivity {
    private ImageView bgMenu;
    private ImageButton menuBtn;
    private ImageButton userBtn;
    private ImageButton userMenuBtn;
    private ImageButton closeBtn;
    private ImageButton accBtn;
    private ImageButton searchBtn;
    private ImageButton settingsBtn;
    private ImageButton chooseAirBtn;
    private TextView accText;
    private TextView searchText;
    private TextView settingsText;
    private TextView chooseAirText;
    private TextView test;
    private TextView chooseTicketText;
    private ImageButton chooseTicketBtn;
    private long pressedTime;
    private String id;
    private UserData userData = new UserData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (pressedTime + 2000 > System.currentTimeMillis()) {
                    finishAffinity();
                } else {
                    Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
                }
                pressedTime = System.currentTimeMillis();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        setContentView(R.layout.activity_menu);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

        id = sharedPreferences.getString("id", null);


        bgMenu = findViewById(R.id.bg_menu);
        menuBtn = findViewById(R.id.menu_btn);
        userBtn = findViewById(R.id.user_btn);
        userMenuBtn = findViewById(R.id.user_menu_btn);
        closeBtn = findViewById(R.id.close_menu_btn);
        chooseTicketBtn = findViewById(R.id.my_tickets_btn);
        chooseTicketText = findViewById(R.id.my_tickets_text);


        accBtn = findViewById(R.id.acc_btn);
        searchBtn = findViewById(R.id.search_fly_btn);
        settingsBtn = findViewById(R.id.settings_btn);
        chooseAirBtn = findViewById(R.id.choose_air_btn);

        accText = findViewById(R.id.acc_text);
        searchText = findViewById(R.id.search_fly_text);
        settingsText = findViewById(R.id.settings_text);
        chooseAirText = findViewById(R.id.choose_air_text);
        menuBtn.setOnClickListener(view -> {
            clickMenu();
        });
        closeBtn.setOnClickListener(view -> {
            clickClose();
        });
        searchBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, ChooseTicketActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        chooseTicketBtn.setOnClickListener(view -> {
            Intent intent;
            if(id == null){
                intent = new Intent(MenuActivity.this, LoginActivity.class);
            }else {
                intent = new Intent(MenuActivity.this, MyTicketsActivity.class);
            }
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        if(id != null){
            MyAsyncTask myTask = new MyAsyncTask(id);
            myTask.execute();
        }
        hideUi();
    }
    public void ButtonClick(View view) {
        Intent intent;
        if(id == null){
            intent = new Intent(MenuActivity.this, LoginActivity.class);
        }else {
            intent = new Intent(MenuActivity.this, AccountActivity.class);
        }
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    private void clickMenu(){
        bgMenu.setVisibility(View.VISIBLE);
        menuBtn.setVisibility(View.INVISIBLE);
        userBtn.setVisibility(View.INVISIBLE);
        closeBtn.setVisibility(View.VISIBLE);
        accBtn.setVisibility(View.VISIBLE);
        searchBtn.setVisibility(View.VISIBLE);
        settingsBtn.setVisibility(View.VISIBLE);
        chooseAirBtn.setVisibility(View.VISIBLE);
        userMenuBtn.setVisibility(View.VISIBLE);
        chooseTicketText.setVisibility(View.VISIBLE);
        chooseTicketBtn.setVisibility(View.VISIBLE);

        accText.setVisibility(View.VISIBLE);
        searchText.setVisibility(View.VISIBLE);
        settingsText.setVisibility(View.VISIBLE);
        chooseAirText.setVisibility(View.VISIBLE);
    }
    private void clickClose(){
        bgMenu.setVisibility(View.INVISIBLE);
        menuBtn.setVisibility(View.VISIBLE);
        userBtn.setVisibility(View.VISIBLE);
        closeBtn.setVisibility(View.INVISIBLE);
        accBtn.setVisibility(View.INVISIBLE);
        searchBtn.setVisibility(View.INVISIBLE);
        settingsBtn.setVisibility(View.INVISIBLE);
        chooseAirBtn.setVisibility(View.INVISIBLE);
        userMenuBtn.setVisibility(View.INVISIBLE);
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
    class MyAsyncTask extends AsyncTask<Void, Void, String> {
        String records = "";
        UserData ud = new UserData();
        private final String id;
        public MyAsyncTask(String id) {
            this.id = id;
        }
        @Override
        protected String doInBackground(Void... voids) {
            try {
                Log.d("Tag", "first log");

                Class.forName("com.mysql.jdbc.Driver");

                Log.d("Tag", "second log");

                Connection connection = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/rikiplane?characterEncoding=UTF-8", "root", "12345678");

                Log.d("Tag", "third log");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM rikiplane.rp_users WHERE user_id=" + id);

                StringBuilder recordsBuilder = new StringBuilder();
                while(resultSet.next()) {
                    recordsBuilder.append(resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(5) + " " + resultSet.getString(7));
                }

                records = recordsBuilder.toString();


                resultSet.close();
                statement.close();
                connection.close();

            } catch (Exception e) {
                String error = e.toString();
                Log.d("Tag", "This is error: " + error);
            }
            return "Результат фонової роботи";
        }

        @Override
        protected void onPostExecute(String result) {
            String[] userData = records.split(" ");
            ud.setName(userData[0]);
            ud.setLastName(userData[1]);
            ud.setUserMail(userData[2]);
            ud.setLogin(userData[3]);
        }
    }
}

