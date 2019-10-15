package cassdemo;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class Client2 implements Runnable {
    private final AtomicInteger flag;
    private static int counter = 40000;
    private int id;
    private int count;
    private MeetUp meetUp;
    private int beforeStartDelay;
    private int delayRange;
    private int checkDelay;
    private int maxGroup;

    public Client2(MeetUp meetUp, int beforeStartDelay, int delayRange, int checkDelay, int maxGroup) {
        this.flag = new AtomicInteger(1);
        Random generator = new Random();
        this.count = generator.nextInt(maxGroup-1)+1;
        this.meetUp = meetUp;
        this.id = counter++;

        this.beforeStartDelay = beforeStartDelay;
        this.delayRange = 100;
        this.checkDelay = checkDelay;
        this.maxGroup = maxGroup;
    }

    public void run(){
        Random generator = new Random();
        try {
            Thread.sleep(beforeStartDelay + generator.nextInt(delayRange));
            if(flag.get() == 1) {
                flag.set(0);
                if (meetUp.buyTicket(id, count, checkDelay)) {
                    if (generator.nextInt(20) == 10) {
                        //co jakis czas klient sam rezygnuje z biletu
                        meetUp.cancelTicket(id);
                        Stats.getInstance().cancel(this.id);
                    } else {
                        //dostalismy rezerwacje
                        Stats.getInstance().gotReservation(this.id, count);
                        if (meetUp.start(id)) {
                            Stats.getInstance().go(this.id, count);
                        } else {
                            Stats.getInstance().mistake(this.id);
                        }
                    }
                    flag.set(1);
                } else {
                    //bilet jest usuwany przez system bo nie mam miejsc
                    meetUp.cancelTicket(id);
                    Stats.getInstance().stay(this.id, count);
                    flag.set(1);
                }
            } else {
                this.run();
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