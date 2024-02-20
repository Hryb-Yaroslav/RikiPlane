package com.varenyky.naziplane;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MyTicketsActivity extends AppCompatActivity {
    private ImageView bgMenu;
    private ImageButton menuBtn;
    private ImageButton userBtn;
    private ImageButton userMenuBtn;
    private TextView accText;
    private TextView searchText;
    private TextView settingsText;
    private TextView chooseAirText;
    private TextView chooseTicketText;

    private TextView arrivedText;
    private TextView dataText;
    private TextView price;
    private String id;
    private ImageButton closeBtn;
    private ImageButton accBtn;
    private ImageButton searchBtn;
    private ImageButton settingsBtn;
    private ImageButton chooseAirBtn;
    private ImageButton chooseTicketBtn;

    private ImageButton[] tickets;
    private TicketBD[] ticketBDS;
    private TextView[] arrayArrived;
    private TextView[] arrayDates;
    private TextView[] arrayPrices;
    private ArrayList<TicketBD> searchingResult;
    private ArrayList<TicketBD> filterResult;
    private FrameLayout[] arrayFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);

        bgMenu = findViewById(R.id.bg_menu);
        menuBtn = findViewById(R.id.menu_information_btn);
        userBtn = findViewById(R.id.user_information_btn);
        userMenuBtn = findViewById(R.id.user_menu_btn);
        closeBtn = findViewById(R.id.close_menu_btn);

        searchBtn = findViewById(R.id.search_fly_btn);
        accBtn = findViewById(R.id.acc_btn);
        settingsBtn = findViewById(R.id.settings_btn);
        chooseAirBtn = findViewById(R.id.choose_air_btn);
        chooseTicketBtn = findViewById(R.id.my_tickets_btn);

        accText = findViewById(R.id.acc_text);
        searchText = findViewById(R.id.search_fly_text);
        settingsText = findViewById(R.id.settings_text);
        chooseAirText = findViewById(R.id.choose_air_text);
        chooseTicketText = findViewById(R.id.my_tickets_text);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        id = sharedPreferences.getString("id", null);
        MyAsyncTask myTask = new MyAsyncTask(id);
        myTask.execute();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        searchBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MyTicketsActivity.this, ChooseTicketActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        accBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MyTicketsActivity.this, AccountActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        menuBtn.setOnClickListener(view -> {
            clickMenu();
        });
        closeBtn.setOnClickListener(view -> {
            clickClose();
        });
        Intent intent = getIntent();
        String id = intent.getStringExtra("key");

        arrayArrived = new TextView[]{findViewById(R.id.first_arrived), findViewById(R.id.second_arrived), findViewById(R.id.third_arrived), findViewById(R.id.fourth_arrived), findViewById(R.id.five_arrived), findViewById(R.id.six_arrived), findViewById(R.id.seven_arrived)};
        arrayDates = new TextView[]{findViewById(R.id.first_date), findViewById(R.id.second_date), findViewById(R.id.third_date), findViewById(R.id.fourth_date), findViewById(R.id.five_date), findViewById(R.id.six_date), findViewById(R.id.seven_date)};
        arrayPrices = new TextView[]{findViewById(R.id.first_price), findViewById(R.id.second_price), findViewById(R.id.third_price), findViewById(R.id.fourth_price), findViewById(R.id.five_price), findViewById(R.id.six_price), findViewById(R.id.seven_price)};
        tickets = new ImageButton[]{findViewById(R.id.first_ticket), findViewById(R.id.second_ticket), findViewById(R.id.third_ticket), findViewById(R.id.fourth_ticket), findViewById(R.id.five_ticket), findViewById(R.id.six_ticket), findViewById(R.id.seven_ticket)};
        arrayFrame = new FrameLayout[]{findViewById(R.id.first_layout), findViewById(R.id.second_layout), findViewById(R.id.third_layout), findViewById(R.id.fourth_layout), findViewById(R.id.five_layout),findViewById(R.id.six_layout), findViewById(R.id.seven_layout)};


        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ticketBDS = TicketBD.AllTickets.userArr;

        menuBtn.setOnClickListener(view -> {
            clickMenu();
        });
        closeBtn.setOnClickListener(view -> {
            clickClose();
        });
        for(int i = 0; i < arrayFrame.length; i++) {
            if (i >= ticketBDS.length) arrayFrame[i].setVisibility(View.INVISIBLE);
            else arrayFrame[i].setVisibility(View.VISIBLE);
        }

        for(int i = 0; i < ticketBDS.length; i++){
            tickets[i].setContentDescription(ticketBDS[i].getId());
            arrayArrived[i].setText(ticketBDS[i].getDepart() + "\n" + ticketBDS[i].getArrived());
            arrayDates[i].setText("Departure\n" + ticketBDS[i].getTime() + "\nArrival Time");
            arrayPrices[i].setText("Price: " + ticketBDS[i].getPrice());
        }
        hideUi();
    }
    public void ButtonClick(View view) {
        Intent intent;
        if(id == null){
            intent = new Intent(MyTicketsActivity.this, LoginActivity.class);
        }else {
            intent = new Intent(MyTicketsActivity.this, AccountActivity.class);
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
        chooseTicketBtn.setVisibility(View.VISIBLE);
        chooseTicketText.setVisibility(View.VISIBLE);

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
        chooseTicketBtn.setVisibility(View.INVISIBLE);
        chooseTicketText.setVisibility(View.INVISIBLE);

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
        private final String userId;
        ArrayList<TicketBD> list = new ArrayList<>();

        public MyAsyncTask(String userId) {
            this.userId = userId;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Log.d("Tag", "first log");

                Class.forName("com.mysql.jdbc.Driver");


                Connection connection = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/rikiplane?characterEncoding=UTF-8", "root", "12345678");

                Log.d("Tag", "" + userId);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT flight_id, CONCAT(departure_time, \"\\n\",  arrival_time) AS time, price, d.name departure, a.name arrived FROM rikiplane.rp_flights \n" +
                        "JOIN rp_airports d ON d.id=rp_flights.RP_Airports_id\n" +
                        "JOIN rp_airports a ON a.id=rp_flights.RP_Airports_id1 WHERE flight_id IN (SELECT RP_table3_flights_flight_id FROM rp_reservations WHERE RP_user_id = " + userId + ");");

//                StringBuilder recordsBuilder = new StringBuilder();
                while (resultSet.next()) {
                    list.add(new TicketBD(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));
//                    recordsBuilder.append(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(5) + " " + resultSet.getString(7));
                }
                TicketBD.AllTickets.setAllUsers(list);
//                records = recordsBuilder.toString();
                Log.d("Tag", "Feeld List");

                resultSet.close();
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
