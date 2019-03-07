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
            System.out.println("Problem z podłączeniem do clustra. " + e.getMessage() + ".");
        }
    }

    public boolean isApproved(int clientId, int trainId) {
        List<TicketRequest> ticketRequests = getAllHappy(trainId);

        ArrayList<Integer> workshops = new ArrayList<>();

        for (int i = 0; i < workshopCount; i++) {
            workshops.add(workshopCapacity);
        }

        for (TicketRequest ticketRequest : ticketRequests) {
            boolean decision = false;
            for (int i = 0; i < workshopCount; i++) {
                int availableSeats = workshops.get(i);
                if (availableSeats >= ticketRequest.seats) {
                    workshops.set(i, availableSeats - ticketRequest.seats);
                    decision = true;
                    break;
                }
            }

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