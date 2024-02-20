package com.varenyky.naziplane;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    private ImageButton exitBtn;
    private ImageButton loginBtn;
    private Button regBtn;
    private EditText loginText;
    private EditText passwordText;
    private boolean isLogin = true;
    public UserData userData = new UserData();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        exitBtn = findViewById(R.id.back_btn_log);
        loginBtn = findViewById(R.id.log_btn);
        regBtn = findViewById(R.id.reg_log_btn);
        loginText = findViewById(R.id.login_log);
        passwordText = findViewById(R.id.password_log);


        exitBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        loginBtn.setOnClickListener(view -> {
            String login = loginText.getText().toString().replaceAll("\\s", "");
            String password = passwordText.getText().toString().replaceAll("\\s", "");
            if(login.isEmpty()) Toast.makeText(this, "Поле login повинно бути обовʼязково заповнене",
                    Toast.LENGTH_SHORT).show();
            else if(password.isEmpty()) Toast.makeText(this, "Поле password повинно бути обовʼязково заповнене",
                    Toast.LENGTH_SHORT).show();
            else{
                userData.setIsLogin(false);
                MyAsyncTask myTask = new MyAsyncTask(login, password);
                myTask.execute();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(userData.isLogin()){
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    Intent intent1 = new Intent(LoginActivity.this, MyTicketsActivity.class);

                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();


                    myEdit.putString("id", userData.getUserId());
                    myEdit.apply();
                    if(userData.getIsChoose()){
                        userData.setIsChoose(false);
                        startActivity(intent1);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }else{
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }else{
                    Toast.makeText(this, "Неправильно введений логін або пароль",
                            Toast.LENGTH_SHORT).show();
                }

            }

        });
        regBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        hideUi();
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
        private final String login;
        private final String password;


        public MyAsyncTask(String login, String password) {
            this.login = login;
            this.password = password;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Log.d("Tag", "first log");

                Class.forName("com.mysql.jdbc.Driver");



                Connection connection = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/rikiplane?characterEncoding=UTF-8", "root", "12345678");

                Log.d("Tag", "third log");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM rikiplane.rp_users WHERE login ='" + login + "'AND password_hash = '" + password + "'");

                StringBuilder recordsBuilder = new StringBuilder();
                while (resultSet.next()) {
                    userData.setIsLogin(true);
                    recordsBuilder.append(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(5) + " " + resultSet.getString(7));
                }

                records = recordsBuilder.toString();


                resultSet.close();
                statement.close();
                connection.close();

                if(userData.isLogin()){
                    String[] userData = records.split(" ");
                    ud.setUserId(userData[0]);
                    ud.setName(userData[1]);
                    ud.setLastName(userData[2]);
                    ud.setUserMail(userData[3]);
                    ud.setLogin(userData[4]);
                }
            } catch (Exception e) {
                String error = e.toString();
                Log.d("Tag", "This is error: " + error);
                return "error login";
            }
            return "Результат фонової роботи";
        }
        @Override
        protected void onPostExecute(String result) {


        }
    }
}
