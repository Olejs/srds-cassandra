package cassdemo;

import cassdemo.backend.BackendSession;

public class MeetUp {
    private int id;


    private int workshopCount;
    private int workshopCapacity;

    public MeetUp(BackendSession backend, int meetUpId, int workshopCount, int workshopCapacity) {
        this.workshopCount = workshopCount;
        this.workshopCapacity = workshopCapacity;
        id = meetUpId;
    }

    public int getId() {
        return id;
    }

    public int getWorkshopCount() {
        return workshopCount;
    }

    public int getWorkshopCapacity() {
        return workshopCapacity;
    }

    public boolean buyTicket(int clientId, int seats, int checkDelay) {

        TicketRequest ticketRequest = new TicketRequest(id, clientId, seats, workshopCount, workshopCapacity);
        ticketRequest.save();
        try {
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ticketRequest.isApproved(clientId, id);

    }

    public void cancelTicket(int clientId) {

        TicketRequest ticketRequest = new TicketRequest(id, clientId, -1, workshopCount, workshopCapacity);
        ticketRequest.save();

    }

    public boolean start(int clientId) {

        TicketRequest ticketRequest = new TicketRequest(id, clientId, 0, workshopCount, workshopCapacity);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ticketRequest.isApproved(clientId, id);

    }
}
