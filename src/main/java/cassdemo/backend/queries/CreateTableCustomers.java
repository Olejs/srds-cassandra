package cassdemo.backend.queries;

import cassdemo.TicketRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cassdemo.backend.BackendSession;
import java.util.Properties;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.UUID;

public class CreateTableCustomers {

    public static BackendSession instance = null;
    public static Session session;

    public static void main(String[] args) throws BackendException {

        //tworzymy tabele TicketRequests
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("Customers").append("(")
                .append("id uuid, ")
                .append("name text,")
                .append("timestamp bigint,")
                .append("PRIMARY KEY (name, id));");

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
