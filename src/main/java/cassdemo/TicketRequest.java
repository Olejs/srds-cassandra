package cassdemo;

import cassdemo.backend.BackendSession;

import java.security.MessageDigest;
import com.datastax.driver.core.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class TicketRequest {
    private int meetUpId;
    private int clientId;
    private int seats;
    private int workshopCount;
    private int workshopCapacity;
    private long timestamp;

    public TicketRequest(int meetUpId, int clientId, int seats, int workshopCount, int workshopCapacity) {
        this.meetUpId = meetUpId;
        this.clientId = clientId;
        this.seats = seats;
        this.workshopCount = workshopCount;
        this.workshopCapacity = workshopCapacity;
    }

    public void save() {
        try {
            BackendSession.insert(this);
        } catch(cassdemo.backend.BackendException e) {
            System.out.println("Could not connect to the cluster. " + e.getMessage() + ".");
        }
    }

    public boolean isApproved(int clientId, int trainId) {
        List<TicketRequest> ticketRequests = getAllHappy(trainId);

        ArrayList<Integer> rooms = new ArrayList<>();

        for (int i = 0; i < workshopCount; i++) {
            rooms.add(workshopCapacity);
        }

//        rooms.forEach(System.out::println);

        for (TicketRequest ticketRequest : ticketRequests) {
            boolean decision = false;
            for (int i = 0; i < workshopCount; i++) {
                int availableSeats = rooms.get(i);
                if (availableSeats >= ticketRequest.seats) {
                    rooms.set(i, availableSeats - ticketRequest.seats);
                    decision = true;
                    break;
                }
            }

//            System.out.println("-");
//            rooms.forEach(System.out::println);
//            System.out.println("-" + ticketRequest.clientId + " - " + clientId + " = "+ decision );

            if (ticketRequest.clientId == clientId) {
                return decision;
            }
        }
        return false;
    }

    public int getClientId() {
        return clientId;
    }

    public int getMeetUpId() {
        return meetUpId;
    }

    public int getSeats() {
        return seats;
    }

    public List<List<Integer>> giveTickets(int trainId) {
        List<TicketRequest> ticketRequests = getAllHappy(trainId);
//        List<Integer> tickets = new ArrayList<>();
        List<List<Integer>> tickets = new ArrayList<>();

        ArrayList<Integer> rooms = new ArrayList<>();

        for (int i = 0; i < workshopCount; i++) {
            rooms.add(workshopCapacity);
            List<Integer> room = new ArrayList<>();
            tickets.add(room);
        }

        for (TicketRequest ticketRequest : ticketRequests) {
            for (int i = 0; i < workshopCount; i++) {
                int availableSeats = rooms.get(i);
                if (availableSeats >= ticketRequest.seats) {
                    rooms.set(i, availableSeats - ticketRequest.seats);
//                    System.out.println("+++ " + ticketRequest.seats);
                    tickets.get(i).add(ticketRequest.seats);
//                    tickets.add();
                    break;
                }
            }
        }
        return tickets;
    }

    public List<TicketRequest> getAllHappy(int trainId) {
        ResultSet rs = BackendSession.getAllHeppy(trainId);
        List<TicketRequest> ticketRequests = new ArrayList<>();

        rs.forEach(r -> {
            if (r.getInt("seats") > 0) {
                ticketRequests.add(new TicketRequest(
                        r.getInt("meetUpId"),
                        r.getInt("customerId"),
                        r.getInt("seats"),
                        workshopCount,
                        workshopCapacity));
            } else {
                for (int i = 0; i < ticketRequests.size(); i++) {
                    // Delete users if canceled
                    if (ticketRequests.get(i).clientId == r.getInt("customerId")) {
                        ticketRequests.remove(i);
                        break;
                    }
                }
            }
        });

        return ticketRequests;
    }
}