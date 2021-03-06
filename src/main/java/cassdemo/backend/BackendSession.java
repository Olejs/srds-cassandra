package cassdemo.backend;

import cassdemo.TicketRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class BackendSession {

	private static final Logger logger = LoggerFactory.getLogger(BackendSession.class);

	public static BackendSession instance = null;

	public static Session session;

	public BackendSession(String contactPoint, String keyspace) throws BackendException {

		Cluster cluster = Cluster.builder().addContactPoint(contactPoint).build();
		try {
			session = cluster.connect(keyspace);
		} catch (Exception e) {
			throw new BackendException("Could not connect to the cluster. " + e.getMessage() + ".", e);
		}
		prepareStatements();
	}

	public void main(int meetUpId, int clientId, int seat)  throws BackendException {

		StringBuilder sb = new StringBuilder("INSERT INTO ")
				.append("TicketRequests").append("(id, meetUpId, customerId, seats, timestamp) ")
				.append("VALUES (").append(UUID.randomUUID())
				.append(", ").append(meetUpId)
				.append(", ").append(clientId)
				.append(", ").append(seat)
				.append(", ").append(System.currentTimeMillis())
				.append(");");

		String query = sb.toString();
		session.execute(query);
	}

	public void prepareStatements() throws BackendException {
		//tworzymy tabele TicketRequests
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
				.append("TicketRequests").append("(")
				.append("id uuid, ")
				.append("meetUpId int,")
				.append("customerId int,")
				.append("seats int,")
				.append("timestamp bigint,")
				.append("PRIMARY KEY (meetUpId, id));");

		String query = sb.toString();
		session.execute(query);
	}

	public static void insert(TicketRequest ticketRequest) throws BackendException {

		StringBuilder sb = new StringBuilder("INSERT INTO ")
				.append("TicketRequests").append("(id, meetUpId, customerId, seats, timestamp) ")
				.append("VALUES (").append(UUID.randomUUID())
				.append(", ").append(ticketRequest.getMeetUpId())
				.append(", ").append(ticketRequest.getClientId())
				.append(", ").append(ticketRequest.getSeats())
				.append(", ").append(System.currentTimeMillis())
				.append(");");

		String query = sb.toString();
		session.execute(query);
	}

	public static void insertCutomRow(int meetUpId, int clientId, int seat)  throws BackendException {

		StringBuilder sb = new StringBuilder("INSERT INTO ")
				.append("TicketRequests").append("(id, meetUpId, customerId, seats, timestamp) ")
				.append("VALUES (").append(UUID.randomUUID())
				.append(", ").append(meetUpId)
				.append(", ").append(clientId)
				.append(", ").append(seat)
				.append(", ").append(System.currentTimeMillis())
				.append(");");

		String query = sb.toString();
		session.execute(query);
	}

	public static void deleteAll() {
		StringBuilder sb = new StringBuilder("Truncate ticketrequests;");
		String query = sb.toString();
		session.execute(query);
	}

	public static ResultSet getAllHeppy(int meetUpId) {
		StringBuilder sb = new StringBuilder("SELECT * FROM ").append("TicketRequests").append(" WHERE meetUpId=").append(meetUpId);
		String query = sb.toString();
		ResultSet rs = session.execute(query);
		return rs;
	}

	public void finalize() {
		try {
			if (session != null) {
				session.getCluster().close();
			}
		} catch (Exception e) {
			logger.error("Could not close existing cluster", e);
		}
	}
}
