package de.htw.ai.decentralised_calendar.network;

import biweekly.Biweekly;
import biweekly.ICalendar;
import de.htw.ai.decentralised_calendar.request.IOperation;
import de.htw.ai.decentralised_calendar.request.Request;
import de.htw.ai.decentralised_calendar.request.RequestLog;
import de.htw.ai.decentralised_calendar.request.RequestManager;
import de.htw.ai.decentralised_calendar.storage.CacheManager;
import de.htw.ai.decentralised_calendar.storage.ICalendarStorage;
import de.htw.ai.decentralised_calendar.storage.Synchronizer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ConnectionHandlerServer implements IConnectionHandler {

    private final InputStream is;
    private final OutputStream os;
    private final CacheManager cacheManager;


    public ConnectionHandlerServer(final InputStream is, final OutputStream os, final CacheManager cacheManager) {
        this.is = is;
        this.os = os;
        this.cacheManager = cacheManager;
    }

//    public ConnectionHandlerServer(final InputStream is, final OutputStream os, StorageManager storageManager) {
//        this.is = is;
//        this.os = os;
//        this.cacheManager = storageManager;
//    }


    @Override
    public void handleConnection() {
        final BufferedInputStream bis = new BufferedInputStream(this.is);
        final DataInputStream dis = new DataInputStream(bis);
        try {
            final int filesCount = dis.readInt();
            for (int i = 0; i < filesCount; i++) {
                final CacheReader cacheReader = new CacheReader(bis, dis, this.cacheManager.getUriOfReceivedCache());
                cacheReader.run();
            }
            this.handleAnswer();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void handleAnswer() {
        final ICalendarStorage storage = this.cacheManager.getStorage();
        this.syncReceivedCache(storage);
        final List<File> storageFiles = storage.getEntriesAsFiles();

        try {
            final BufferedOutputStream bos = new BufferedOutputStream(this.os);
            final DataOutputStream dos = new DataOutputStream(bos);

            dos.writeInt(storageFiles.size());
            for (final File storageFile : storageFiles) {
                if (storageFile.isFile()) {
                    final CacheSender sender = new CacheSender(bos, dos, storageFile);
                    sender.run();
                }
            }
            dos.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }


    private void syncReceivedCache(final ICalendarStorage storage) {
        final List<String> storageEntries = storage.getEntriesAsStrings();
        final List<String> receivedCacheFilenames = this.cacheManager.getReceivedEntriesAsString();
        final List<String> receivedCache = this.cacheManager.iCalStringsToList(receivedCacheFilenames);
        final List<String> mergedEntries = Synchronizer.syncDevices(storageEntries, receivedCache);

        storage.clearStorage();
        final ArrayList<ICalendar> iCalendarList = new ArrayList<>();

        for (final String icalString : mergedEntries) {

            final List<ICalendar> parsedString = Biweekly.parse(icalString).all();
            iCalendarList.addAll(parsedString);
        }
        storage.saveEntries(iCalendarList);
    }


    public void transformOperations(final RequestLog receivedLog) {
        final RequestLog localLog = this.cacheManager.getRequestLog();
        final List<Request> localRequestList = localLog.getRequestList();
        final int counter = localRequestList.size() - 1;

        for (final Request receivedRequest : receivedLog.getRequestList()) {
            for (int i = 0; i < localRequestList.size(); i++) {
                final Request localRequest = localRequestList.get(i);
                final IOperation transformedOperation = RequestManager.it(receivedRequest, localRequest);
                receivedRequest.setOperation(transformedOperation);
            }
            localLog.addRequest(receivedRequest);
        }
    }
}
