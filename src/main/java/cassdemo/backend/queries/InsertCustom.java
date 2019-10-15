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

/*
 * For error handling done right see:
 * https://www.datastax.com/dev/blog/cassandra-error-handling-done-right
 *
 * Performing stress tests often results in numerous WriteTimeoutExceptions,
 * ReadTimeoutExceptions (thrown by Cassandra replicas) and
 * OpetationTimedOutExceptions (thrown by the client). Remember to retry
 * failed operations until success (it can be done through the RetryPolicy mechanism:
 * https://stackoverflow.com/questions/30329956/cassandra-datastax-driver-retry-policy )
 */

public class InsertCustom {

    public static BackendSession instance = null;
    public static Session session;

    public static void main(String[] args) throws BackendException {

        //tworzymy tabele TicketRequests
        StringBuilder sb = new StringBuilder("INSERT INTO ")
                .append("TicketRequests").append("(id, meetUpId, customerId, seats, timestamp) ")
                .append("VALUES (").append(UUID.randomUUID())
                .append(", ").append(args[0])
                .append(", ").append(args[1])
                .append(", ").append(args[2])
                .append(", ").append(System.currentTimeMillis())
                .append(");");

        String query = sb.toString();

        try {
            Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
            session = cluster.connect("srds");
            session.execute(query);
            System.exit(0);
        } catch (Exception e) {
            throw new BackendException("Could not connect to the cluster. " + e.getMessage() + ".", e);
        }
    }
}