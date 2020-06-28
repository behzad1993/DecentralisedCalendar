package de.htw.ai.decentralised_calendar.network;

import java.io.*;


/**
 * Using BufferedOutputStream and DataOutputStream to send the whole cache
 */
public class CacheSender implements Runnable {

    private final BufferedOutputStream bos;
    private final DataOutputStream dos;
    private final File file;


    public CacheSender(BufferedOutputStream bos, DataOutputStream dos, File file) {
        this.bos = bos;
        this.dos = dos;
        this.file = file;
    }


    @Override
    public void run() {
        long length = this.file.length();
        try {
            this.dos.writeLong(length);
            String name = this.file.getName();
            this.dos.writeUTF(name);
            FileInputStream fis = new FileInputStream(this.file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            int theByte = 0;
            while ((theByte = bis.read()) != - 1) {
                this.bos.write(theByte);
            }
            bis.close();
//                TODO: maybe fis.close()
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
