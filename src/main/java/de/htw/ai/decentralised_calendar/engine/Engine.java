package de.htw.ai.decentralised_calendar.engine;

import de.htw.ai.decentralised_calendar.network.ConnectionHandler;
import de.htw.ai.decentralised_calendar.request.RequestManager;
import de.htw.ai.decentralised_calendar.storage.StorageManager;
import de.htw.ai.decentralised_calendar.tcp.TCPChannel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Engine {

    private static final Logger LOG = Logger.getLogger(Engine.class.getName());
    //    private final CacheManager cacheManager;
    private final StorageManager storageManager;
    private final RequestManager requestManager;
    private final TCPChannel tcp;


    public Engine(final StorageManager storageManager, final RequestManager requestManager, final TCPChannel tcp) {
        this.storageManager = storageManager;
        this.requestManager = requestManager;
        this.tcp = tcp;
    }


    public void startSynchronization() {
        try {
            this.tcp.start();
            this.tcp.waitForConnection();
            this.tcp.checkConnected();
            final Socket socket = this.tcp.getSocket();
            final InputStream is = socket.getInputStream();
            final OutputStream os = socket.getOutputStream();
            final ConnectionHandler connectionHandler = new ConnectionHandler(is, os, this.requestManager);

            connectionHandler.handleConnection();

            socket.close();
            this.tcp.close();

        } catch (final IOException e) {
            LOG.log(Level.WARNING, e.getMessage());
            e.printStackTrace();
        }

    }


    public StorageManager getStorageManager() {
        return this.storageManager;
    }
}
