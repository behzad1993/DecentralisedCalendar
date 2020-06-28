package de.htw.ai.decentralised_calendar.tcp;

import biweekly.ICalendar;
import de.htw.ai.decentralised_calendar.network.ConnectionHandlerClient;
import de.htw.ai.decentralised_calendar.network.ConnectionHandlerServer;
import de.htw.ai.decentralised_calendar.storage.ICalendarGenerator;
import de.htw.ai.decentralised_calendar.storage.StorageManager;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Behzad Karimi on 2019-07-21.
 * @project decentralised_calendar
 */
class TCPChannelTest {

    private final static int PORT = 3456;
    private static final Logger LOG = Logger.getLogger(TCPChannelTest.class.getName());
    private static File BASE_FILE = null;
    private static File SERVER_FILE = null;
    private static File CLIENT_FILE = null;
    private static URI BASE_URI = null;
    private static URI SERVER_URI = null;
    private static URI CLIENT_URI = null;
    private static StorageManager storageManager;


    @BeforeAll
    static void setUp() throws Exception {
        BASE_URI = new URI("extern/testFiles/TCPChannelTest/");
        SERVER_URI = new URI(BASE_URI + "server/");
        CLIENT_URI = new URI(BASE_URI + "client/");
        BASE_FILE = new File(BASE_URI.toString());
        SERVER_FILE = new File(SERVER_URI.toString());
        CLIENT_FILE = new File(CLIENT_URI.toString());
        storageManager = new StorageManager(BASE_URI);

        if (! SERVER_FILE.mkdirs() && ! SERVER_FILE.isDirectory() || ! CLIENT_FILE.mkdir() && ! CLIENT_FILE.isDirectory()) {
            throw new IOException("Could not create dir for TCPChannelTest.java.");
        }
    }


    @AfterAll
    static void deleteDirectory() throws IOException {
        deleteAllFiles(true);
    }


    private static void deleteAllFiles(boolean withDirectory) {
        File[] dir = BASE_FILE.listFiles();
        File[] filesServer = SERVER_FILE.listFiles();
        File[] filesClient = CLIENT_FILE.listFiles();

        File[] wholeDir = ArrayUtils.addAll(filesServer, filesClient);
        wholeDir = ArrayUtils.addAll(wholeDir, dir);
        wholeDir = ArrayUtils.addAll(wholeDir, BASE_FILE);

        for (File file : wholeDir) {
            if (! withDirectory && file.isFile()) {
                file.delete();
            } else if (withDirectory) {
                file.delete();
            }
        }
    }


    @BeforeEach
    void deleteFiles() {
        deleteAllFiles(false);
    }


    @Test
    void test_connection() throws IOException, InterruptedException, URISyntaxException {
        LOG.log(Level.FINE, "Starting test_connection()");
        StorageManager senderStorage = new StorageManager(SERVER_URI);
        StorageManager receiverStorage = new StorageManager(CLIENT_URI);

        List<ICalendar> sampleICalendars = ICalendarGenerator.createSampleICalendars(5);
        senderStorage.saveEntries(sampleICalendars);

        final TCPChannel server = new TCPServer(PORT, "server");
        final TCPChannel client = new TCPClient(PORT, "client");

        LOG.log(Level.FINE, "Build connection");
        server.start();
        client.start();

        server.waitForConnection();
        client.waitForConnection();

        server.checkConnected();
        client.checkConnected();

        final Socket serverSocket = server.getSocket();
        final Socket clientSocket = client.getSocket();

        final ConnectionHandlerClient handleSender = null;
//                new ConnectionHandlerClient(serverSocket.getInputStream(), serverSocket.getOutputStream(), senderStorage);
        final ConnectionHandlerServer connectionHandler = null;
//                new ConnectionHandlerServer(clientSocket.getInputStream(), clientSocket.getOutputStream(), receiverStorage);

        handleSender.handleConnection();
        connectionHandler.handleConnection();

        Thread.sleep(1000);

        serverSocket.close();
        clientSocket.close();

        server.close();
        client.close();

        String entriesFromSender = senderStorage.getEntriesAsOneString();
        String entriesFromReceiver = receiverStorage.getEntriesAsOneString();
        assertThat(entriesFromSender).isEqualTo(entriesFromReceiver);

        List<String> filenameSender = senderStorage.getFileNames();
        List<String> filenameReceiver = receiverStorage.getFileNames();
        LOG.log(Level.FINE, "Comparing bot list. filenameSender.size() = {0}, filenameReceiver.size() = {1}",
                new Object[]{filenameSender.size(), filenameReceiver.size()});
        String[] strings = filenameReceiver.toArray(new String[filenameReceiver.size()]);
        assertThat(filenameSender).containsExactly(strings);
    }
}