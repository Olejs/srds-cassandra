package cassdemo;

import java.util.ArrayList;
import java.util.Random;

public class Client implements Runnable {
    private static int counter = 40000;
    private int id;
    private int count;
    private MeetUp meetUp;
    private int beforeStartDelay;
    private int delayRange;
    private int checkDelay;
    private int maxGroup;

    public Client(MeetUp meetUp, int beforeStartDelay, int delayRange, int checkDelay, int maxGroup) {
        Random generator = new Random();
        this.count = generator.nextInt(maxGroup-1)+1;
        this.meetUp = meetUp;
        this.id = counter++;

        this.beforeStartDelay = beforeStartDelay;
        this.delayRange = delayRange;
        this.checkDelay = checkDelay;
        this.maxGroup = maxGroup;
    }
    public void run(){
        Random generator = new Random();
        try {
            Thread.sleep(beforeStartDelay + generator.nextInt(delayRange));
            if(meetUp.buyTicket(id, count, checkDelay)) {
                if(generator.nextInt(20) == 10) {
                    meetUp.cancelTicket(id);
                    Stats.getInstance().cancel(this.id);
                } else {
                    Stats.getInstance().gotReservation(this.id, count);
                    if(meetUp.start(id)) {
                        Stats.getInstance().go(this.id, count);
                    } else {
                        Stats.getInstance().mistake(this.id);
                    }
                }
            } else {
                meetUp.cancelTicket(id);
                Stats.getInstance().stay(this.id, count);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void arrange(int count, MeetUp meetUp, int beforeStartDelay, int delayRange, int checkDelay, int maxGroup) {
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Thread thread = new Thread(new Client(meetUp, beforeStartDelay, delayRange, checkDelay, maxGroup));
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}