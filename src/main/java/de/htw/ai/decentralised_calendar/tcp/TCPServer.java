package de.htw.ai.decentralised_calendar.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Behzad Karimi on 2019-06-24.
 * @project decentralised_calendar
 */
public class TCPServer extends TCPChannel {

    private static final Logger LOG = Logger.getLogger(TCPServer.class.getName());


    public TCPServer(final int port, final String name) {
        super(port, name);
    }


    @Override
    protected Socket buildSocket() throws IOException {
        final ServerSocket srvSocket = new ServerSocket(this.port);
        LOG.log(Level.FINE, "TCPChannel ({0}) opened port {1} on localhost and wait", new Object[]{this.name, this.port});
        final Socket socket = srvSocket.accept();
        LOG.log(Level.FINE, "TCPChannel ({0}) connected", new Object[]{this.name});
        return socket;
    }
}
