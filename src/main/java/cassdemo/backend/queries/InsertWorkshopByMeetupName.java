package cassdemo.backend.queries;

import cassdemo.TicketRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cassdemo.backend.BackendSession;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.UUID;

public class InsertWorkshopByMeetupName {

    public static BackendSession instance = null;
    public static Session session;

    public static void main(String[] args) throws BackendException {
        //tworzymy tabele TicketRequests
//        StringBuilder sb = new StringBuilder("INSERT INTO ")
//                .append("Meetups").append("(id, name, description, timestamp) ")
//                .append("VALUES (").append(UUID.randomUUID())
//                .append(", ").append(args[0])
//                .append(", ").append(args[1])
//                .append(", ").append(System.currentTimeMillis())
//                .append(");");

        StringBuilder sb = new StringBuilder("SELECT * FROM ").append("Meetups").append(" WHERE name='").append(args[0]).append("'");
        String query = sb.toString();

//        ResultSet rs = session.execute(query);


        try {
            Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
            session = cluster.connect("srds");
            ResultSet rs = session.execute(query);

            rs.forEach(r -> {
                String s = r.getString("name");
                StringBuilder sb2 = new StringBuilder("INSERT INTO ")
                        .append("Workshops").append("(id, meetUpId, customerId, seats, timestamp) ")
                        .append("VALUES (").append(UUID.randomUUID())
                        .append(", ").append(r.getInt("meetUpId"))
                        .append(", ").append(args[1])
                        .append(", ").append(args[2])
                        .append(", ").append(System.currentTimeMillis())
                        .append(");");

                String query2 = sb2.toString();
                session.execute(query2);
                System.exit(0);
            });
        } catch (Exception e) {
            throw new BackendException("Could not connect to the cluster. " + e.getMessage() + ".", e);
        }
    }
}
