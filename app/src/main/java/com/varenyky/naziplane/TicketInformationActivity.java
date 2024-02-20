package com.varenyky.naziplane;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TicketInformationActivity extends AppCompatActivity {
    private ImageView bgMenu;
    private ImageButton menuBtn;
    private ImageButton userBtn;
    private ImageButton userMenuBtn;
    private TextView accText;
    private TextView searchText;
    private TextView settingsText;
    private TextView chooseAirText;

    private TextView arrivedText;
    private TextView dataText;
    private TextView price;
    private String id;
    public String idFlights;
    private ImageButton closeBtn;
    private ImageButton accBtn;
    private ImageButton searchBtn;
    private ImageButton settingsBtn;
    private ImageButton chooseAirBtn;
    private TextView chooseTicketText;
    private ImageButton chooseTicketBtn;
    private ImageButton bookBtn;
    private String[] classes;
    private String[][] classesss;
    private String selectedCategory;
    private String check;
    private String idTable3;
    String records = "";
    private UserData userData = new UserData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_information);

        bgMenu = findViewById(R.id.bg_menu);
        menuBtn = findViewById(R.id.menu_information_btn);
        userBtn = findViewById(R.id.user_information_btn);
        userMenuBtn = findViewById(R.id.user_menu_btn);
        closeBtn = findViewById(R.id.close_menu_btn);
        bookBtn = findViewById(R.id.booking_btn);
        chooseTicketBtn = findViewById(R.id.my_tickets_btn);

        searchBtn = findViewById(R.id.search_fly_btn);
        accBtn = findViewById(R.id.acc_btn);
        settingsBtn = findViewById(R.id.settings_btn);
        chooseAirBtn = findViewById(R.id.choose_air_btn);
        accText = findViewById(R.id.acc_text);
        searchText = findViewById(R.id.search_fly_text);
        settingsText = findViewById(R.id.settings_text);
        chooseAirText = findViewById(R.id.choose_air_text);
        chooseTicketText = findViewById(R.id.my_tickets_text);

        arrivedText = findViewById(R.id.arived_text);
        dataText = findViewById(R.id.date_text);
        price = findViewById(R.id.price_text);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        id = sharedPreferences.getString("id", null);

        chooseTicketBtn.setOnClickListener(view -> {
            Intent intent;
            if(id == null){
                intent = new Intent(TicketInformationActivity.this, LoginActivity.class);
            }else {
                intent = new Intent(TicketInformationActivity.this, MyTicketsActivity.class);
            }
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        searchBtn.setOnClickListener(view -> {
            Intent intent = new Intent(TicketInformationActivity.this, ChooseTicketActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        accBtn.setOnClickListener(view -> {
            Intent intent = new Intent(TicketInformationActivity.this, AccountActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        bookBtn.setOnClickListener(view -> {
            Intent intent;
            if(id == null){
                userData.setIsChoose(true);
                intent = new Intent(TicketInformationActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }else if(selectedCategory == "Виберіть клас"){
                Toast.makeText(getApplicationContext(), "Виберіть категорію!", Toast.LENGTH_SHORT).show();
            }
            else {
                for(int i = 0; i < classesss.length; i++){
                    if(selectedCategory == classesss[i][1]) check = classesss[i][0];
                }
                MyAsyncTask2 myTask2 = new MyAsyncTask2(idFlights, check);
                myTask2.execute();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                MyAsyncTask3 myTask3 = new MyAsyncTask3();
                myTask3.execute();

                intent = new Intent(TicketInformationActivity.this, MyTicketsActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

        });
        menuBtn.setOnClickListener(view -> {
            clickMenu();
        });
        closeBtn.setOnClickListener(view -> {
            clickClose();
        });
        Intent intent = getIntent();
        idFlights = intent.getStringExtra("key");
        MyAsyncTask myTask = new MyAsyncTask(id);
        myTask.execute();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(int i = 0; i < TicketBD.AllTickets.arr.length; i++){
            if(Objects.equals(TicketBD.AllTickets.arr[i].getId(), idFlights)){
                dataText.setText("Departure\n" + TicketBD.AllTickets.arr[i].getTime() + "\nArrival");
                arrivedText.setText(TicketBD.AllTickets.arr[i].getDepart() + "\n" + TicketBD.AllTickets.arr[i].getArrived());
                price.setText("Price: " + TicketBD.AllTickets.arr[i].getPrice());
                Log.d("Tag", "Working");
            }
        }
        Spinner spinner = findViewById(R.id.spinner);
        classes = records.split("\\|");
        classesss = new String[classes.length / 3][3];
        for (int i = 0, j = 0; i < classes.length / 3; i++, j+=3){
            classesss[i][0] = classes[j];
            classesss[i][1] = classes[j+1];
            classesss[i][2] = classes[j+2];
        }
        List<String> categories = new ArrayList<>();
        categories.add("Виберіть клас");
        for (int i = 0; i < classes.length / 3; i++){
            categories.add(classesss[i][1]);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory = categories.get(position);
                Toast.makeText(getApplicationContext(), "Вибрано: " + selectedCategory, Toast.LENGTH_SHORT).show();
                bookBtn.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Обробка події, коли нічого не вибрано
            }
        });

        hideUi();
    }
    public void ButtonClick(View view) {
        Intent intent;
        if(id == null){
            intent = new Intent(TicketInformationActivity.this, LoginActivity.class);
        }else {
            intent = new Intent(TicketInformationActivity.this, AccountActivity.class);
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
                ResultSet resultSet = statement.executeQuery("SELECT id, Name, `Class price` FROM rikiplane.`rp_classes of service` WHERE id IN (SELECT `RP_Classes of service_id` FROM rikiplane.rp_table3 WHERE RP_flights_flight_id = " + idFlights + ");");

                StringBuilder recordsBuilder = new StringBuilder();
                while(resultSet.next()) {
                    recordsBuilder.append(resultSet.getString(1) + "|" + resultSet.getString(2) + "|" + resultSet.getString(3) + "|");
                }

                records = recordsBuilder.toString();
                Log.d("Tag", records);

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
        }
    }
    class MyAsyncTask2 extends AsyncTask<Void, Void, String> {
        public final String RP_flights_flight_id;
        public final String RP_Classes_of_service_id;

        public MyAsyncTask2(String RP_flights_flight_id, String RP_Classes_of_service_id) {
            this.RP_flights_flight_id = RP_flights_flight_id;
            this.RP_Classes_of_service_id = RP_Classes_of_service_id;
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
                ResultSet resultSet = statement.executeQuery("SELECT id FROM rikiplane.rp_table3 WHERE RP_flights_flight_id = '" + RP_flights_flight_id + "'AND `RP_Classes of service_id` = '" + RP_Classes_of_service_id + "';");

                StringBuilder recordsBuilder = new StringBuilder();
                while(resultSet.next()) {
                    recordsBuilder.append(resultSet.getString(1));
                }

                records = recordsBuilder.toString();
                idTable3 = records;
                Log.d("Tag", records);

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
        }
    }
    class MyAsyncTask3 extends AsyncTask<Void, Void, String> {

        public MyAsyncTask3() {
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
                statement.executeUpdate("INSERT INTO `rikiplane`.`rp_reservations` (`RP_user_id`, `seat_number`, `total_price`, `booking_time`, `table3_╨Ъ╨╗╨░╤Б╤Б╨╕ ╨╛╨▒╤Б╨╗╨║╨│╨╛╨▓╤Г╨▓╨░╨╜╨╜╤П_id`, `RP_table3_id`, `RP_table3_flights_flight_id`, `RP_table3_Classes of service_id`) VALUES ('" + id + "', '5555', '5555.00', '2023-12-11 20:30:00', '1','" + idTable3 + "' ,'" + idFlights + "' ,'" + check + "');");
                StringBuilder recordsBuilder = new StringBuilder();


                records = recordsBuilder.toString();
                Log.d("Tag", records);

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
        }
    }

}
