package de.htw.ai.decentralised_calendar.network;

import biweekly.ICalendar;
import de.htw.ai.decentralised_calendar.request.*;
import de.htw.ai.decentralised_calendar.storage.CacheManager;
import de.htw.ai.decentralised_calendar.storage.ICalendarGenerator;
import de.htw.ai.decentralised_calendar.storage.StorageManager;
import de.htw.ai.decentralised_calendar.tcp.TCPChannel;
import de.htw.ai.decentralised_calendar.tcp.TCPClient;
import de.htw.ai.decentralised_calendar.tcp.TCPServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import static de.htw.ai.decentralised_calendar.storage.ICalendarGenerator.storageInit;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author Behzad Karimi 13.09.19
 * @project decentralised_calendar
 */
class ConnectionHandlerTest {

//    RequestLog logA;
//    RequestLog logB;
//
//
//    @BeforeEach
//    void init() throws Exception {
//        final Constructor<RequestLog> constructor = RequestLog.class.getDeclaredConstructor();
//        constructor.setAccessible(true);
//
//        final RequestLog logA = constructor.newInstance();
//        final RequestLog logB = constructor.newInstance();
//
//        final LinkedList<Request> requestsA = new LinkedList<>();
//        final LinkedList<Request> requestsB = new LinkedList<>();
//
//        logA.setRequestList(requestsA);
//        logB.setRequestList(requestsB);
//    }


    // CASE 1 BEGIN:  EMPTY LIST
    @Test
    void test_handleConnection_testReceivingEmptyLogA() throws IOException, InterruptedException {

        final TCPChannel clientA = new TCPServer(99, "clientA");
        clientA.start();
        clientA.waitForConnection();
        clientA.checkConnected();

        final InputStream isA = clientA.getInputStream();
        final OutputStream osA = clientA.getOutputStream();

        final RequestManager requestManagerA = mock(RequestManager.class);
        final RequestLog requestLogA = mock(RequestLog.class);
        final LinkedList<Request> logA = new LinkedList<>();
        final ConnectionHandler connectionHandlerA = new ConnectionHandler(isA, osA, requestManagerA);
        when(requestManagerA.getLog()).thenReturn(requestLogA);
        when(requestLogA.getRequestList()).thenReturn(logA);

        connectionHandlerA.handleConnection();

        Thread.sleep(1000);
        clientA.getSocket().close();
        clientA.close();
    }


    @Test
    void test_handleConnection_testReceivingEmptyLogB() throws IOException {

        final TCPChannel clientB = new TCPClient(99, "clientB");
        clientB.start();
        clientB.waitForConnection();
        clientB.checkConnected();
        final InputStream isB = clientB.getInputStream();
        final OutputStream osB = clientB.getOutputStream();


        final RequestLog requestLogB = mock(RequestLog.class);
        final LinkedList<Request> logB = new LinkedList<>();
        final Request request = new Request(1, 1, "", null, null);
        logB.add(request);


        final RequestManager requestManagerB = mock(RequestManager.class);
        final ConnectionHandler connectionHandlerB = new ConnectionHandler(isB, osB, requestManagerB);
        when(requestManagerB.getLog()).thenReturn(requestLogB);
        when(requestLogB.getRequestList()).thenReturn(logB);

        connectionHandlerB.handleConnection();

        clientB.getSocket().close();
        clientB.close();
    }
// CASE 1 END:  EMPTY LIST


    // CASE 2 BEGIN:  EMPTY LIST
    @Test
    void test_handleConnection_testReceivingCorrectLogA() throws IOException, InterruptedException {

        final TCPChannel clientA = new TCPServer(99, "clientA");
        clientA.start();
        clientA.waitForConnection();
        clientA.checkConnected();

        final InputStream isA = clientA.getInputStream();
        final OutputStream osA = clientA.getOutputStream();


        final RequestLog requestLogA = mock(RequestLog.class);
        final LinkedList<Request> logA = new LinkedList<>();
        final Request request = new Request(1, 1, "", null, new NoOperation());
        logA.add(request);

        final RequestManager requestManagerA = mock(RequestManager.class);
        final ConnectionHandler connectionHandlerA = new ConnectionHandler(isA, osA, requestManagerA);
        when(requestManagerA.getLog()).thenReturn(requestLogA);
        when(requestLogA.getRequestList()).thenReturn(logA);

        connectionHandlerA.handleConnection();

        Thread.sleep(1000);
        clientA.getSocket().close();
        clientA.close();
    }


    @Test
    void test_handleConnection_testReceivingCorrectLogB() throws IOException {

        final TCPChannel clientB = new TCPClient(99, "clientB");
        clientB.start();
        clientB.waitForConnection();
        clientB.checkConnected();
        final InputStream isB = clientB.getInputStream();
        final OutputStream osB = clientB.getOutputStream();


        final RequestLog requestLogB = mock(RequestLog.class);
        final LinkedList<Request> logB = new LinkedList<>();
        final Request request = new Request(1, 1, "", null, new NoOperation());
        logB.add(request);

        final RequestManager requestManagerB = mock(RequestManager.class);
        final ConnectionHandler connectionHandlerB = new ConnectionHandler(isB, osB, requestManagerB);
        when(requestManagerB.getLog()).thenReturn(requestLogB);
        when(requestLogB.getRequestList()).thenReturn(logB);

        connectionHandlerB.handleConnection();

        clientB.getSocket().close();
        clientB.close();
    }
// CASE 2 END:  EMPTY LIST


    // CASE 3 BEGIN:  EMPTY LIST
//    extern/connectionHandlerTest
    @Test
    void integrationTestWithRequestManagerA() throws Exception {
        final TCPChannel clientA = new TCPServer(99, "clientA");
        clientA.start();
        clientA.waitForConnection();
        clientA.checkConnected();

        final InputStream isA = clientA.getInputStream();
        final OutputStream osA = clientA.getOutputStream();

        final StorageManager storageManagerA = new StorageManager("extern/testFiles/connectionHandlerTest/B/");
        storageInit(storageManagerA);
        RequestManager requestManager = new RequestManager(storageManagerA);
        final ConnectionHandler connectionHandlerB = new ConnectionHandler(isA, osA, requestManager);

        connectionHandlerB.handleConnection();


        System.out.println("Storage Manager A SENDING Log operations:");
        for (Request request : requestManager.getLog().getRequestList()) {
            System.out.println(request.getOperation());
        }
        System.out.println("\n");

        clientA.getSocket().close();
        clientA.close();
    }


    @Test
    void integrationTestWithRequestManagerB() throws Exception {

        final TCPChannel clientB = new TCPClient(99, "clientB");
        clientB.start();
        clientB.waitForConnection();
        clientB.checkConnected();

        InputStream isB = clientB.getInputStream();
        OutputStream osB = clientB.getOutputStream();

        final StorageManager storageManagerA = new StorageManager("extern/testFiles/connectionHandlerTest/A/");
        RequestManager requestManager = new RequestManager(storageManagerA);
        ConnectionHandler connectionHandlerB = new ConnectionHandler(isB, osB, requestManager);

        connectionHandlerB.handleConnection();

        System.out.println("Storage Manager B RECEIVED Log operations:");
        for (Request request : requestManager.getLog().getRequestList()) {
            System.out.println(request.getOperation());
        }
        System.out.println("\n");

        clientB.getSocket().close();
        clientB.close();
    }

// CASE 3 END:  EMPTY LIST

}