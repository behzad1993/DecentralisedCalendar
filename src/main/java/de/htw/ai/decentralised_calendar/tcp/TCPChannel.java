package de.htw.ai.decentralised_calendar.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author thsc
 */
public abstract class TCPChannel extends Thread {

    private static final Logger LOG = Logger.getLogger(TCPChannel.class.getName());

    public final int WAIT_LOOP_IN_MILLIS = 1000;
    protected final String name;
    protected final int port;
    private Socket socket = null;
    private boolean fatalError = false;
    private boolean threadRunning = false;


    public TCPChannel(final int port, final String name) {
        this.port = port;
        this.name = name;
    }


    @Override
    public void run() {
        this.threadRunning = true;
        try {
            this.socket = this.buildSocket();
        } catch (final IOException e) {
            LOG.log(Level.FINE, "Could not establish connection. Excepion: \n{0}", new Object[]{e.getMessage()});
            this.fatalError = true;
        }
    }


    protected abstract Socket buildSocket() throws IOException;


    public void close() throws IOException {
        if (this.socket != null) {
            this.socket.close();
            LOG.log(Level.FINE, "socket closed");
        }
    }


    /**
     * holds thread until a connection is established
     */
    public void waitForConnection() {
        if (! this.threadRunning) {
            try {
                Thread.sleep(this.WAIT_LOOP_IN_MILLIS);
            } catch (final InterruptedException ex) {
                LOG.log(Level.WARNING, "Trying Thread.sleep()");
                ex.printStackTrace();
            }
        }
        while (! this.fatalError && this.socket == null) {
            try {
                Thread.sleep(this.WAIT_LOOP_IN_MILLIS);
            } catch (final InterruptedException ex) {
                LOG.log(Level.WARNING, "Trying Thread.sleep()");
                ex.printStackTrace();
            }
        }
    }


    public void checkConnected() throws IOException {
        if (this.socket == null) {
            final String error = "no socket yet - should call connect first";
            LOG.log(Level.FINE, error);
            throw new IOException(error);
        }
    }


    public InputStream getInputStream() throws IOException {
        this.checkConnected();
        return this.socket.getInputStream();
    }


    public OutputStream getOutputStream() throws IOException {
        this.checkConnected();
        return this.socket.getOutputStream();
    }


    public Socket getSocket() {
        return this.socket;
    }
}
