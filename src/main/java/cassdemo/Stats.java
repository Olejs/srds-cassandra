package cassdemo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Stats {
    private static Stats INSTANCE;

    private int happyParticipants = 0;
    private int sadParticipants = 0;
    private int cancelingParticipants = 0;
    private int angryParticipants = 0;

    private ArrayList<Integer> reservations = new ArrayList<>();
    private ArrayList<Integer> sad = new ArrayList<>();

    private Stats(){}

    public static Stats getInstance(){
        if(INSTANCE==null)
            INSTANCE = new Stats();
        return INSTANCE;
    }

    public void gotReservation(int id, int seats) {
        log(id, "Dostałem się na MEETUP {"+seats+"}");
    }

    public void go(int id, int seats) {
        reservations.add(seats);
        log(id, "Zaczynamy MEETUP!!!");
        happyParticipants++;
    }

    public void cancel(int id) {
        log(id, "Anuluje bilet");
        cancelingParticipants++;
    }

    public void stay(int id, int seats) {
        sad.add(seats);
        log(id,"||||NIE MAM WEJSCIA NA MEETUP :( ||||");
        sadParticipants++;
    }

    public void mistake(int id) {
        log(id,"*****************CHYBA MAMY DO CZYNIENIA Z BLEDEM SYSTEMU*********************");
        angryParticipants++;
    }

    public void showStats() {
        System.out.println("happyParticipants = "+happyParticipants);
        System.out.println("sadParticipants = "+sadParticipants);
        System.out.println("cancelingParticipants = "+cancelingParticipants);
        System.out.println("angryParticipants = "+angryParticipants);
//        System.out.println("------\nHappy Participant:");
//        reservations.forEach(r -> System.out.print("" + r + ", "));
//        System.out.println("\n------");

//        System.out.println("------\nSad Participant:");
//        sad.forEach(r -> System.out.print("" + r + ", "));
//        System.out.println("\n------");

    }

    private void log(int id, String message) {
        System.out.println("[" + id + "] " + message);
    }
}