package de.htw.ai.decentralised_calendar.engine;

import de.htw.ai.decentralised_calendar.request.Delete;
import de.htw.ai.decentralised_calendar.request.Insert;
import de.htw.ai.decentralised_calendar.request.RequestManager;
import de.htw.ai.decentralised_calendar.storage.StorageManager;
import de.htw.ai.decentralised_calendar.tcp.TCPChannel;
import de.htw.ai.decentralised_calendar.tcp.TCPClient;
import de.htw.ai.decentralised_calendar.tcp.TCPServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import static de.htw.ai.decentralised_calendar.storage.ICalendarGenerator.storageInit;
import static org.assertj.core.api.Assertions.assertThat;


class EngineTest {

    private static final int PORT1 = 2345;
    private static final int PORT2 = 3456;
    private static final int PORT3 = 4567;
    private static final int PORT4 = 5678;
    private static final int PORT5 = 6789;
    private static final Logger LOG = Logger.getLogger(EngineTest.class.getName());
    private static URI SERVER_URI = null;
    private static URI CLIENT_A_URI = null;
    private static URI CLIENT_B_URI = null;


    @BeforeAll
    static void setUp() throws Exception {
        final URI baseUri = new URI("extern/testFiles/EngineTest/");
        SERVER_URI = new URI(baseUri + "server/");
        CLIENT_A_URI = new URI(baseUri + "clientA/");
        CLIENT_B_URI = new URI(baseUri + "clientB/");
        final File serverFile = new File(SERVER_URI.toString());
        final File clientAFile = new File(CLIENT_A_URI.toString());
        final File clientBFile = new File(CLIENT_B_URI.toString());
//        File serverReceived = new File(SERVER_URI.toString() + "/received/");
//        File serverCache = new File(SERVER_URI.toString() + "/cache/");
//        File clientReceived = new File(CLIENT_URI.toString() + "/received/");
//        File clientCache = new File(CLIENT_URI.toString() + "/cache/");

        if (! serverFile.mkdirs() && ! serverFile.isDirectory() || ! clientAFile.mkdir() && ! clientAFile.isDirectory() || ! clientBFile.mkdir() && ! clientBFile.isDirectory()) {
            throw new IOException("Could not create dir for EngineTest.java.");
        }

//        if (! serverReceived.mkdir() && ! serverReceived.isDirectory() || ! clientReceived.mkdir() && ! clientReceived.isDirectory()) {
//            throw new IOException("Could not create dir for EngineTest.java.");
//        }
//
//        if (! serverCache.mkdir() && ! serverCache.isDirectory() || ! clientCache.mkdir() && ! clientCache.isDirectory()) {
//            throw new IOException("Could not create dir for EngineTest.java.");
//        }

    }


//    @AfterAll
//    static void deleteDirectory() throws IOException {
//        deleteAllFiles(true);
//    }
//
//
//    private static void deleteAllFiles(boolean withDirectory) {
//        File[] dir = BASE_FILE.listFiles();
//        File[] filesServer = SERVER_FILE.listFiles();
//        File[] filesClient = CLIENT_FILE.listFiles();
//
//        File[] wholeDir = ArrayUtils.addAll(filesServer, filesClient);
//        wholeDir = ArrayUtils.addAll(wholeDir, dir);
//        wholeDir = ArrayUtils.addAll(wholeDir, BASE_FILE);
//
//        for (File file : wholeDir) {
//            if (! withDirectory && file.isFile()) {
//                file.delete();
//            } else if (withDirectory) {
//                file.delete();
//            }
//        }
//    }


    // To start, you need to comment out tcp.start() in Engine.java
//    @Test
//    void test_startSynchronization_shouldSynchronizeCacheAutomatically() {
//        StorageManager senderStorage = new StorageManager(SERVER_URI);
//        StorageManager receiverStorage = new StorageManager(CLIENT_URI);
//
//        List<ICalendar> sampleICalendars = ICalendarGenerator.createSampleICalendars(5);
//        senderStorage.saveEntries(sampleICalendars);
//
//        final TCPChannel server = new TCPServer(PORT, "server");
//        final TCPChannel client = new TCPClient(PORT, "client");
//
//        server.start();
//        client.start();
//
//        Engine serverEngine = new Engine(senderStorage, server);
//        Engine clientEngine = new Engine(receiverStorage, client);
//
//        EngineThread serverThread = new EngineThread(serverEngine);
//        EngineThread clientThread = new EngineThread(clientEngine);
//
//        serverThread.run();
//        clientThread.run();
//
//        String entriesFromSender = senderStorage.getEntriesAsOneString();
//        String entriesFromReceiver = receiverStorage.getEntriesAsOneString();
//        assertThat(entriesFromSender).isEqualTo(entriesFromReceiver);
//
//        List<String> filenameSender = senderStorage.getFileNames();
//        List<String> filenameReceiver = receiverStorage.getFileNames();
//        LOG.log(Level.FINE, "Comparing bot list. filenameSender.size() = {0}, filenameReceiver.size() = {1}",
//                new Object[]{filenameSender.size(), filenameReceiver.size()});
//        String[] strings = filenameReceiver.toArray(new String[filenameReceiver.size()]);
//        assertThat(filenameSender).containsExactly(strings);
//    }


    //     FIRST
    @Test
    void test_sync_Server() throws Exception {
        final StorageManager storageManager = new StorageManager(SERVER_URI.toString());
        final RequestManager requestManager = new RequestManager(storageManager);
        storageManager.setRequestManager(requestManager);
        storageInit(storageManager);

        final TCPChannel server1 = new TCPServer(PORT1, "server");
        final Engine serverEngine1 = new Engine(storageManager, requestManager, server1);
        final EngineThread serverThread1 = new EngineThread(serverEngine1);

        final TCPChannel server2 = new TCPServer(PORT2, "server");
        final Engine serverEngine2 = new Engine(storageManager, requestManager, server2);
        final EngineThread serverThread2 = new EngineThread(serverEngine2);

        final TCPChannel server3 = new TCPServer(PORT3, "server");
        final Engine serverEngine3 = new Engine(storageManager, requestManager, server3);
        final EngineThread serverThread3 = new EngineThread(serverEngine3);

        final TCPChannel server4 = new TCPServer(PORT4, "server");
        final Engine serverEngine4 = new Engine(storageManager, requestManager, server4);
        final EngineThread serverThread4 = new EngineThread(serverEngine4);

        final TCPChannel server5 = new TCPServer(PORT5, "server");
        final Engine serverEngine5 = new Engine(storageManager, requestManager, server5);
        final EngineThread serverThread5 = new EngineThread(serverEngine5);

        System.out.println("RUN1");
        serverThread1.run();
        System.out.println("RUN2");
        serverThread2.run();
        System.out.println("RUN3");
        serverThread3.run();
        System.out.println("RUN4");
        serverThread4.run();
        System.out.println("RUN5");
        serverThread5.run();
        System.out.println("END");
    }


    //    SECOND
    @Test
    void test_sync_Client_A() throws Exception {
        final StorageManager storageManager = new StorageManager(CLIENT_A_URI.toString());
        final RequestManager requestManager = new RequestManager(storageManager);
        storageManager.setRequestManager(requestManager);
        storageInit(storageManager);

        final TCPChannel client1 = new TCPClient(PORT1, "client");
        final Engine clientEngine1 = new Engine(storageManager, requestManager, client1);
        final EngineThread clientThread1 = new EngineThread(clientEngine1);

        final TCPChannel client2 = new TCPClient(PORT4, "client");
        final Engine clientEngine2 = new Engine(storageManager, requestManager, client2);
        final EngineThread clientThread2 = new EngineThread(clientEngine2);

        System.out.println("RUN1");
        this.generateNewRequestsInsert(storageManager);
        clientThread1.run();
        System.out.println("RUN4");
        clientThread2.run();
        System.out.println("END");
    }


    //     THIRD
    @Test
    void test_sync_Client_B() throws Exception {
        final StorageManager storageManager = new StorageManager("extern/testFiles/EngineTest/clientB/");
        final RequestManager requestManager = new RequestManager(storageManager);
        storageManager.setRequestManager(requestManager);

        final TCPChannel client1 = new TCPClient(PORT2, "client");
        final Engine clientEngine1 = new Engine(storageManager, requestManager, client1);
        final EngineThread clientThread1 = new EngineThread(clientEngine1);

        final TCPChannel client2 = new TCPClient(PORT3, "client");
        final Engine clientEngine2 = new Engine(storageManager, requestManager, client2);
        final EngineThread clientThread2 = new EngineThread(clientEngine2);

        final TCPChannel client3 = new TCPClient(PORT5, "client");
        final Engine clientEngine3 = new Engine(storageManager, requestManager, client3);
        final EngineThread clientThread3 = new EngineThread(clientEngine3);

        System.out.println("RUN2");
        clientThread1.run();
        this.generateNewRequestsDelete(storageManager);
        System.out.println("RUN3");
        clientThread2.run();
        System.out.println("RUN5");
        clientThread3.run();
        System.out.println("END");
    }


    private void generateNewRequestsDelete(final StorageManager storageManager) throws Exception {
        final RequestManager requestManager = storageManager.getRequestManager();

        final List<String> fileNames = storageManager.getFileNames();
        final String file = fileNames.get(0);
        final Delete delete1 = new Delete(13);
        final Delete delete2 = new Delete(13);
        final Delete delete3 = new Delete(13);
        final Delete delete4 = new Delete(13);
        final Delete delete5 = new Delete(13);
        requestManager.generatingRequest(delete1, file);
        requestManager.generatingRequest(delete2, file);
        requestManager.generatingRequest(delete3, file);
        requestManager.generatingRequest(delete4, file);
        requestManager.generatingRequest(delete5, file);
    }


    private void generateNewRequestsInsert(final StorageManager storageManager) throws Exception {
        final RequestManager requestManager = storageManager.getRequestManager();

        final List<String> fileNames = storageManager.getFileNames();
        final String file = fileNames.get(0);
        final Insert insert = new Insert(13, "ATTENDEE;ROLE=ATTENDEE;CN=user;EMAIL=CLIENT_A_INSERT@CLIENTA.com:uri");
        requestManager.generatingRequest(insert, file);
    }


    private void checkDir() {
        final StorageManager senderStorage = new StorageManager(SERVER_URI.toString());
        final StorageManager receiverStorage = new StorageManager(CLIENT_A_URI.toString());

        final String entriesFromSender = senderStorage.getEntriesAsOneString();
        final String entriesFromReceiver = receiverStorage.getEntriesAsOneString();
        assertThat(entriesFromSender).isEqualTo(entriesFromReceiver);
    }
}
