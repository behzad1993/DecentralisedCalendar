package de.htw.ai.decentralised_calendar.network;

import de.htw.ai.decentralised_calendar.request.Request;
import de.htw.ai.decentralised_calendar.request.RequestLog;
import de.htw.ai.decentralised_calendar.request.RequestManager;
import de.htw.ai.decentralised_calendar.storage.StorageManager;

import java.io.*;
import java.util.LinkedList;


/**
 * @author Behzad Karimi 13.09.19
 * @project decentralised_calendar
 */
public class ConnectionHandler {

    private final InputStream is;
    private final OutputStream os;
    private final RequestManager requestManager;


    public ConnectionHandler(final InputStream is, final OutputStream os, final RequestManager requestManager) {
        this.is = is;
        this.os = os;
        this.requestManager = requestManager;
    }


    public void handleConnection() {
        // sending log
        final RequestLog requestLog = this.requestManager.getLog();
        final LinkedList<Request> log = requestLog.getRequestList();
        try {
            final ObjectOutputStream oos = new ObjectOutputStream(this.os);
            oos.writeObject(log);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        // receiving log
        final LinkedList<Request> receivedLog;
        try {
            final ObjectInputStream ois = new ObjectInputStream(this.is);
            receivedLog = (LinkedList<Request>) ois.readObject();
            this.requestManager.addToQueue(receivedLog);
        } catch (final IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }
}
