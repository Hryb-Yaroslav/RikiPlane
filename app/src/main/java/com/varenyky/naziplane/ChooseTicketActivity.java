package com.varenyky.naziplane;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.util.List;

public class ChooseTicketActivity extends AppCompatActivity {
    private ImageView bgMenu;
    private ImageButton menuBtn;
    private ImageButton check;
    private ImageButton userBtn;
    private ImageButton userMenuBtn;
    private TextView accText;
    private TextView priceFilterText;
    private TextView searchText;
    private TextView settingsText;
    private TextView chooseAirText;

    private ImageButton closeBtn;
    private ImageButton accBtn;
    private ImageButton searchBtn;
    private ImageButton settingsBtn;
    private ImageButton chooseAirBtn;
    private ImageButton search;
    private ImageButton filterBtn;
    private EditText inputText;
    private EditText inputStartPrice;
    private EditText inputEndPrice;
    private TextView chooseTicketText;
    private ImageButton chooseTicketBtn;

    private ImageButton closeFilterBtn;
    private ImageView blackBg;
    private ImageView popup;
    private ImageButton acceptBtn;
    private TextView acceptText;
    private FrameLayout[] arrayFrame;
    private boolean isChecked = false;
    private String id;
    private ImageButton[] tickets;
    private TicketBD[] ticketBDS;
    private TextView[] arrayArrived;
    private TextView[] arrayDates;
    private TextView[] arrayPrices;
    private ArrayList<TicketBD> searchingResult;
    private ArrayList<TicketBD> filterResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_ticket);
        bgMenu = findViewById(R.id.bg_menu);
        menuBtn = findViewById(R.id.menu_choose_btn);
        userBtn = findViewById(R.id.user_choose_btn);
        userMenuBtn = findViewById(R.id.user_menu_btn);
        closeBtn = findViewById(R.id.close_menu_btn);
        search = findViewById(R.id.search_filter_btn);
        chooseTicketBtn = findViewById(R.id.my_tickets_btn);
        chooseTicketText = findViewById(R.id.my_tickets_text);

        searchBtn = findViewById(R.id.search_fly_btn);
        accBtn = findViewById(R.id.acc_btn);
        check = findViewById(R.id.check_all_btn);
        settingsBtn = findViewById(R.id.settings_btn);
        chooseAirBtn = findViewById(R.id.choose_air_btn);
        accText = findViewById(R.id.acc_text);
        searchText = findViewById(R.id.search_fly_text);
        settingsText = findViewById(R.id.settings_text);
        chooseAirText = findViewById(R.id.choose_air_text);
        inputText = findViewById(R.id.login_log);
        filterBtn = findViewById(R.id.filter_btn);
        closeFilterBtn = findViewById(R.id.close_filter_btn);
        blackBg = findViewById(R.id.black_bg);
        popup = findViewById(R.id.popup);
        popup = findViewById(R.id.popup);
        inputStartPrice = findViewById(R.id.input_start_price);
        inputEndPrice = findViewById(R.id.input_end_price);
        priceFilterText = findViewById(R.id.price_filter_text);
        acceptBtn = findViewById(R.id.accept_btn);
        acceptText = findViewById(R.id.accept_text);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        id = sharedPreferences.getString("id", null);
        MyAsyncTask myTask = new MyAsyncTask();
        myTask.execute();
        arrayArrived = new TextView[]{findViewById(R.id.first_arrived), findViewById(R.id.second_arrived), findViewById(R.id.third_arrived), findViewById(R.id.fourth_arrived), findViewById(R.id.five_arrived), findViewById(R.id.six_arrived), findViewById(R.id.seven_arrived)};
        arrayDates = new TextView[]{findViewById(R.id.first_date), findViewById(R.id.second_date), findViewById(R.id.third_date), findViewById(R.id.fourth_date), findViewById(R.id.five_date), findViewById(R.id.six_date), findViewById(R.id.seven_date)};
        arrayPrices = new TextView[]{findViewById(R.id.first_price), findViewById(R.id.second_price), findViewById(R.id.third_price), findViewById(R.id.fourth_price), findViewById(R.id.five_price), findViewById(R.id.six_price), findViewById(R.id.seven_price)};
        tickets = new ImageButton[]{findViewById(R.id.first_ticket), findViewById(R.id.second_ticket), findViewById(R.id.third_ticket), findViewById(R.id.fourth_ticket), findViewById(R.id.five_ticket), findViewById(R.id.six_ticket), findViewById(R.id.seven_ticket)};
        arrayFrame = new FrameLayout[]{findViewById(R.id.first_layout), findViewById(R.id.second_layout), findViewById(R.id.third_layout), findViewById(R.id.fourth_layout), findViewById(R.id.five_layout),findViewById(R.id.six_layout), findViewById(R.id.seven_layout)};

        searchingResult = new ArrayList<>();
        filterResult = new ArrayList<>();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ticketBDS = TicketBD.AllTickets.arr;
//        TicketBD.AllTickets.arr = new TicketBD[]
        Collections.addAll(searchingResult, ticketBDS);
        chooseTicketBtn.setOnClickListener(view -> {
            Intent intent;
            if(id == null){
                intent = new Intent(ChooseTicketActivity.this, LoginActivity.class);
            }else {
                intent = new Intent(ChooseTicketActivity.this, MyTicketsActivity.class);
            }
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        filterBtn.setOnClickListener(view -> {
            blackBg.setVisibility(View.VISIBLE);
            popup.setVisibility(View.VISIBLE);
            closeFilterBtn.setVisibility(View.VISIBLE);
            priceFilterText.setVisibility(View.VISIBLE);
            inputStartPrice.setVisibility(View.VISIBLE);
            inputEndPrice.setVisibility(View.VISIBLE);
            acceptBtn.setVisibility(View.VISIBLE);
            acceptText.setVisibility(View.VISIBLE);
            menuBtn.setEnabled(false);
            userBtn.setEnabled(false);
        });
        closeFilterBtn.setOnClickListener(view -> {
            closeFilter();
        });
        menuBtn.setOnClickListener(view -> {
            clickMenu();
        });
        closeBtn.setOnClickListener(view -> {
            clickClose();
        });
        acceptBtn.setOnClickListener(view -> {
            String inputPrice = inputStartPrice.getText().toString().replaceAll("\\s", "");
            String outputPrice = inputEndPrice.getText().toString().replaceAll("\\s", "");
            if(inputPrice != null && outputPrice != null){
                filterResult.clear();
                for(int i = 0; i < searchingResult.size(); i++){
                    int editedPrice = Integer.parseInt(searchingResult.get(i).getPrice().replaceAll("[^0-9]", ""));
                    Log.d("Tag", editedPrice + " " + " " + outputPrice + " " +  inputPrice);
                    if(editedPrice >= Integer.parseInt(inputPrice) && editedPrice <= Integer.parseInt(outputPrice)){
                        filterResult.add(searchingResult.get(i));
                    }
                }
                Log.d("Tag", filterResult.size() + "");
                for(int i = 0; i < filterResult.size(); i++){
                    tickets[i].setContentDescription(filterResult.get(i).getId());
                    arrayArrived[i].setText(filterResult.get(i).getDepart() + "\n" + filterResult.get(i).getArrived());
                    arrayDates[i].setText("Departure \n" + filterResult.get(i).getTime() + "\nArrival Time");
                    arrayPrices[i].setText("Price: " + filterResult.get(i).getPrice());
                }
                for(int i = 0; i < arrayFrame.length; i++){
                    if(i >= filterResult.size())  arrayFrame[i].setVisibility(View.INVISIBLE);
                    else arrayFrame[i].setVisibility(View.VISIBLE);
                }
            }
            if(!isChecked){
                check.setImageResource(R.drawable.checked_box);
                isChecked = true;
            }else {
                check.setImageResource(R.drawable.uncheck_box);
                isChecked = false;
            }
            closeFilter();
        });
        search.setOnClickListener(view -> {
            searchingResult.clear();
            String inputValue = inputText.getText().toString().replaceAll("\\s", "");
            String newInputValue = "";
            for(Character item : inputValue.toCharArray()){
                newInputValue += Character.toLowerCase(item);
            }
            inputValue = newInputValue;
            for(int i = 0; i < ticketBDS.length; i++){
                newInputValue = "";
                for(Character item : ticketBDS[i].getArrived().toCharArray()){
                    newInputValue += Character.toLowerCase(item);
                }
                if(newInputValue.equals(inputValue))    searchingResult.add(ticketBDS[i]);
            }

            if(inputValue.isEmpty()){
                searchingResult.clear();
                searchingResult.addAll(Arrays.asList(ticketBDS));
            }
            for(int i = 0; i < searchingResult.size(); i++){
                tickets[i].setContentDescription(searchingResult.get(i).getId());
                arrayArrived[i].setText(searchingResult.get(i).getDepart() + "\n" + searchingResult.get(i).getArrived());
                arrayDates[i].setText("Departure \n" + searchingResult.get(i).getTime() + "\nArrival Time");
                arrayPrices[i].setText("Price: " + searchingResult.get(i).getPrice());
            }
            for(int i = 0; i < arrayFrame.length; i++){
                if(i >= searchingResult.size())  arrayFrame[i].setVisibility(View.INVISIBLE);
                else arrayFrame[i].setVisibility(View.VISIBLE);
            }
        });
        for(int i = 0; i < 7; i++){
            int finalI = i;
            tickets[i].setOnClickListener(view -> {
                Intent intent = new Intent(ChooseTicketActivity.this, TicketInformationActivity.class);
                intent.putExtra("key", tickets[finalI].getContentDescription());
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }
        check.setOnClickListener(view -> {
            if(!isChecked){
                for(int i = 0; i < filterResult.size(); i++){
                    tickets[i].setContentDescription(filterResult.get(i).getId());
                    arrayArrived[i].setText(filterResult.get(i).getDepart() + "\n" + filterResult.get(i).getArrived());
                    arrayDates[i].setText(filterResult.get(i).getTime());
                    arrayPrices[i].setText(filterResult.get(i).getPrice());
                }
                for(int i = 0; i < arrayFrame.length; i++){
                    if(i >= filterResult.size())  arrayFrame[i].setVisibility(View.INVISIBLE);
                    else arrayFrame[i].setVisibility(View.VISIBLE);
                }
                check.setImageResource(R.drawable.checked_box);
                isChecked = true;
            }else {
                for(int i = 0; i < searchingResult.size(); i++){
                    tickets[i].setContentDescription(searchingResult.get(i).getId());
                    arrayArrived[i].setText(searchingResult.get(i).getDepart() + "\n" + searchingResult.get(i).getArrived());
                    arrayDates[i].setText(searchingResult.get(i).getTime());
                    arrayPrices[i].setText(searchingResult.get(i).getPrice());
                }
                for(int i = 0; i < arrayFrame.length; i++) {
                    if (i >= searchingResult.size()) arrayFrame[i].setVisibility(View.INVISIBLE);
                    else arrayFrame[i].setVisibility(View.VISIBLE);
                }
                check.setImageResource(R.drawable.uncheck_box);
                isChecked = false;
            }
        });

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
            intent = new Intent(ChooseTicketActivity.this, LoginActivity.class);
        }else {
            intent = new Intent(ChooseTicketActivity.this, AccountActivity.class);
        }
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    private void closeFilter(){
        blackBg.setVisibility(View.INVISIBLE);
        popup.setVisibility(View.INVISIBLE);
        closeFilterBtn.setVisibility(View.INVISIBLE);
        priceFilterText.setVisibility(View.INVISIBLE);
        inputStartPrice.setVisibility(View.INVISIBLE);
        inputEndPrice.setVisibility(View.INVISIBLE);
        acceptBtn.setVisibility(View.INVISIBLE);
        acceptText.setVisibility(View.INVISIBLE);
        menuBtn.setEnabled(true);
        userBtn.setEnabled(true);
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
        ArrayList<TicketBD> list = new ArrayList<>();


        @Override
        protected String doInBackground(Void... voids) {
            try {
                Log.d("Tag", "first log");

                Class.forName("com.mysql.jdbc.Driver");


                Connection connection = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/rikiplane?characterEncoding=UTF-8", "root", "12345678");

                Log.d("Tag", "third log");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT flight_id, CONCAT(departure_time, \"\n\",  arrival_time) AS time, price, d.name departure, a.name arrived FROM rikiplane.rp_flights \n" +
                        "JOIN rp_airports d ON d.id=rp_flights.RP_Airports_id\n" +
                        "JOIN rp_airports a ON a.id=rp_flights.RP_Airports_id1;");

//                StringBuilder recordsBuilder = new StringBuilder();
                while (resultSet.next()) {
                    list.add(new TicketBD(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));
//                    recordsBuilder.append(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(5) + " " + resultSet.getString(7));
                }
                TicketBD.AllTickets.setAllTickets(list);
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
