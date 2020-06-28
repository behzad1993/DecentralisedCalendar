package de.htw.ai.decentralised_calendar.network;

import de.htw.ai.decentralised_calendar.storage.CacheManager;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Using BufferedInputStream and DataInputStream to receive and save the new cache
 */
public class CacheReader implements Runnable {

    private static final Logger LOG = Logger.getLogger(CacheReader.class.getName());
    private final BufferedInputStream bis;
    private final DataInputStream dis;
    private final URI storage;


    public CacheReader(BufferedInputStream bis, DataInputStream dis, URI storage) {
        this.bis = bis;
        this.dis = dis;
        this.storage = storage;
    }


    @Override
    public void run() {
        long fileLength;

        try {
            fileLength = this.dis.readLong();
            String filename = this.dis.readUTF();

//            TODO: replace receivedCache with devicename
            File file = new File(storage + filename);

            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            for (int j = 0; j < fileLength; j++) {
                bos.write(this.bis.read());
            }
            bos.close();
//            TODO: maybe fos.close()

        } catch (Exception e) {
            LOG.log(Level.FINE, "Could not save cache properly");
            e.printStackTrace();
        }
    }
}
