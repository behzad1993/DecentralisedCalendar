package de.htw.ai.decentralised_calendar.tcp;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Behzad Karimi on 2019-06-24.
 * @project decentralised_calendar
 */
public class TCPClient extends TCPChannel {

    private static final Logger LOG = Logger.getLogger(TCPClient.class.getName());


    public TCPClient(final int port, final String name) {
        super(port, name);
    }


    protected Socket buildSocket() throws IOException {
        for (int i = 0; i < 5; i++) {
            try {
                return new Socket("localhost", this.port);
            } catch (final IOException ioe) {
                LOG.log(Level.FINE, "TCPChannel ({0}) failed / wait and re-try.", new Object[]{this.name});
                try {
                    this.waitForConnection();
                    Thread.sleep(this.WAIT_LOOP_IN_MILLIS);
                } catch (final InterruptedException ex) {
                    // ignore
                }
            }
        }
        throw new IOException();
    }
}
