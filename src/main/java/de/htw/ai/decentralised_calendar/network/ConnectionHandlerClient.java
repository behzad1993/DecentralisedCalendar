package de.htw.ai.decentralised_calendar.network;

import de.htw.ai.decentralised_calendar.storage.CacheManager;
import de.htw.ai.decentralised_calendar.storage.ICalendarStorage;
import de.htw.ai.decentralised_calendar.storage.StorageManager;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;


public class ConnectionHandlerClient implements IConnectionHandler {

    private final InputStream is;
    private final OutputStream os;
    private final CacheManager cacheManager;


    public ConnectionHandlerClient(final InputStream is, final OutputStream os, final CacheManager cacheManager) {
        this.is = is;
        this.os = os;
        this.cacheManager = cacheManager;
    }

//
//    public ConnectionHandlerClient(final InputStream is, final OutputStream os, final StorageManager storageManager) {
//        this.is = is;
//        this.os = os;
//        this.cacheManager = storageManager;
//    }


    @Override
    public void handleConnection() {
        try {
            List<File> fileList = this.cacheManager.getEntriesAsFiles();
            BufferedOutputStream bos = new BufferedOutputStream(this.os);
            DataOutputStream dos = new DataOutputStream(bos);

            dos.writeInt(fileList.size());

            for (File file : fileList) {
                CacheSender cacheSender = new CacheSender(bos, dos, file);
                cacheSender.run();
            }
            dos.flush();
            this.handleAnswer();
            dos.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void handleAnswer() {
        ICalendarStorage storage = this.cacheManager.getStorage();
        storage.clearStorage();
        BufferedInputStream bis = new BufferedInputStream(this.is);
        DataInputStream dis = new DataInputStream(bis);
        try {
            int filesCount = dis.readInt();
            for (int i = 0; i < filesCount; i++) {
                CacheReader cacheReader = new CacheReader(bis, dis, this.cacheManager.getStorage().getURI());
                cacheReader.run();

            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
