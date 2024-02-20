package com.varenyky.naziplane;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
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

public class RegistrationActivity extends AppCompatActivity {
    private EditText loginText;
    private EditText firstNameText;
    private EditText lastNameText;
    private EditText mailNameText;
    private EditText passwordText;
    private ImageButton loginBtn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        loginText = findViewById(R.id.login_log);
        firstNameText = findViewById(R.id.first_name_reg);
        lastNameText = findViewById(R.id.second_name_reg);
        mailNameText = findViewById(R.id.mail_reg);
        passwordText = findViewById(R.id.password_log);
        loginBtn = findViewById(R.id.log_btn);


        loginBtn.setOnClickListener(view -> {
            String login = loginText.getText().toString().replaceAll("\\s", "");
            String firstName = firstNameText.getText().toString().replaceAll("\\s", "");
            String lastName = lastNameText.getText().toString().replaceAll("\\s", "");
            String mail = mailNameText.getText().toString().replaceAll("\\s", "");
            String password = passwordText.getText().toString().replaceAll("\\s", "");
            if(login.isEmpty()) Toast.makeText(this, "Поле login повинно бути обовʼязково заповнене",
                    Toast.LENGTH_SHORT).show();
            else if(firstName.isEmpty()) Toast.makeText(this, "Поле firstName повинно бути обовʼязково заповнене",
                    Toast.LENGTH_SHORT).show();
            else if(lastName.isEmpty()) Toast.makeText(this, "Поле lastName повинно бути обовʼязково заповнене",
                    Toast.LENGTH_SHORT).show();
            else if(mail.isEmpty()) Toast.makeText(this, "Поле mail повинно бути обовʼязково заповнене",
                    Toast.LENGTH_SHORT).show();
            else if(password.isEmpty()) Toast.makeText(this, "Поле password повинно бути обовʼязково заповнене",
                    Toast.LENGTH_SHORT).show();
            else{
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            MyAsyncTask myTask = new MyAsyncTask(login, firstName, lastName, mail, password);
            myTask.execute();
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
        private final String firstName;
        private final String lastName;
        private final String userMail;
        private final String password;


        public MyAsyncTask(String login, String firstName, String lastName, String mail, String password) {
            this.login = login;
            this.firstName = firstName;
            this.lastName = lastName;
            this.userMail = mail;
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
                statement.executeUpdate("INSERT INTO rikiplane.rp_users (`Name`, `Second name`, password_hash, email, IsSuperUser, login) VALUES ('" + firstName + "', '" + lastName + "', '" + password + "', '" + userMail + "', '0', '" + login + "');");


                StringBuilder recordsBuilder = new StringBuilder();

                records = recordsBuilder.toString();


//                resultSet.close();
                statement.close();
                connection.close();

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
