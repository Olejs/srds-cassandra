package cassdemo;

import cassdemo.backend.BackendException;
import cassdemo.backend.BackendSession;

import java.io.IOException;
import java.util.Properties;

public class PositiveTest {

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

        //arranging situation
        Client.arrange(clientCount, meetup, 1000, simulationTime, checkDelay, workshopCapacity);

        Stats.getInstance().showStats();

        session.deleteAll();

        session.finalize();

        System.exit(0);
    }
}
