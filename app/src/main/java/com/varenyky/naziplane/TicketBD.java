package com.varenyky.naziplane;

import java.util.ArrayList;

public class TicketBD {

    public String id;
    public String time;
    public String price;
    public String depart;
    public String arrived;

    public TicketBD(String id, String time, String price, String depart, String arrived){
        this.id = id;
        this.time = time;
        this.price = price;
        this.depart = depart;
        this.arrived = arrived;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return price;
    }

    public String getDepart() {
        return depart;
    }

    public String getArrived() {
        return arrived;
    }

    public static class AllTickets{
        public static TicketBD[] userArr = new TicketBD[]{new TicketBD("666", "20.11.2020", "1000$", "Ukraine", "Usa"),
                new TicketBD("2", "31.12.2020", "100$", "Poland", "Germany"),
                new TicketBD("3", "9.05.1945", "300$", "Ukraine", "Germany"),
        };
        public static TicketBD[] arr = new TicketBD[]{
                new TicketBD("666", "20.11.2020", "1000$", "Ukraine", "Usa"),
                new TicketBD("2", "31.12.2020", "100$", "Poland", "Germany"),
                new TicketBD("3", "9.05.1945", "300$", "Ukraine", "Germany"),
        };
        public static void setAllTickets(ArrayList<TicketBD> list) {
            AllTickets.arr = list.toArray(new TicketBD[0]);
        }
        public static void setAllUsers(ArrayList<TicketBD> list){
            AllTickets.userArr = list.toArray(new TicketBD[0]);
        }
    }
}
