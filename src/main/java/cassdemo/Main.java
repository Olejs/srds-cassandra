package cassdemo;

import java.io.IOException;
import java.util.Properties;

import cassdemo.backend.BackendException;
import cassdemo.backend.BackendSession;

public class Main {

	private static final String PROPERTIES_FILENAME = "config.properties";

	public static void main(String[] args) throws IOException, BackendException {
		String contactPoint = null;
		String keyspace = null;

		final int meetUpId = -15;
		final int workshopCount = 100;
		final int workshopCapacity = 6;
		final int clientCount = 1000;
		final int simulationTime = 100;
		final int checkDelay = 100; // Depends on system overload

		Properties properties = new Properties();
		try {
			properties.load(Main.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME));

			contactPoint = properties.getProperty("contact_point");
			keyspace = properties.getProperty("keyspace");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
			
		BackendSession session = new BackendSession(contactPoint, keyspace);

		//crate meetup instance
		MeetUp meetup = new MeetUp(session, meetUpId, workshopCount, workshopCapacity);


		Client.spawnAndSendToTrain(clientCount, meetup, 1000, simulationTime, checkDelay, workshopCapacity);

		Stats.getInstance().showStats();
//		Client.spawnAndSendToTrain(clientCount, train, 1000, simulationTime, checkDelay, roomCapacity);

//		String output = session.selectAll();
//		System.out.println("Users: \n" + output);

		System.out.println("wszystko sie udalo");
		session.deleteAll();

		System.exit(0);
	}
}
