package de.htw.ai.decentralised_calendar.network;

import de.htw.ai.decentralised_calendar.request.*;
import de.htw.ai.decentralised_calendar.storage.CacheManager;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author Behzad Karimi 2019-09-09
 * @project decentralised_calendar
 */
class ConnectionHandlerServerTest {

    @Test
    void test_connectionHandlerTest_transformOperations() {
        final InputStream is = mock(InputStream.class);
        final OutputStream os = mock(OutputStream.class);
        final CacheManager cacheManager = mock(CacheManager.class);

        final ConnectionHandlerServer chs = new ConnectionHandlerServer(is, os, cacheManager);

//        ReceivedLog
        final RequestLog receivedLog = RequestLog.getInstance();

        final Insert insert2x = new Insert(2, "x");
        final Insert insert6y = new Insert(6, "y");
        final Delete delete1 = new Delete(1);
        final Update up4z = new Update(4, "d", "z");

        final Request receivedRequest1 = new Request(1, "abcd", null, insert2x);
        final Request receivedRequest2 = new Request(1, "abcd", null, insert6y);
        final Request receivedRequest3 = new Request(1, "abcd", null, delete1);
        final Request receivedRequest4 = new Request(1, "abcd", null, up4z);

        receivedLog.addRequest(receivedRequest1, receivedRequest2, receivedRequest3, receivedRequest4);

//        LocalLog
        final RequestLog localLog = mock(RequestLog.class);

        final Delete delete4 = new Delete(4);
        final Update up1x = new Update(1, "a", "x");
        final Insert insert4y = new Insert(4, "y");
        final Insert insert2z = new Insert(2, "z");

        final Request localRequest1 = new Request(2, "abcd", null, delete4);
        final Request localRequest2 = new Request(2, "abcd", null, up1x);
        final Request localRequest3 = new Request(2, "abcd", null, insert4y);
        final Request localRequest4 = new Request(2, "abcd", null, insert2z);

        final LinkedList<Request> localRequestList = new LinkedList<>();
        localRequestList.add(localRequest1);
        localRequestList.add(localRequest2);
        localRequestList.add(localRequest3);
        localRequestList.add(localRequest4);

        when(localLog.getRequestList()).thenReturn(localRequestList);
        when(cacheManager.getRequestLog()).thenReturn(localLog);

        chs.transformOperations(receivedLog);
        final List<Request> requestList = receivedLog.getRequestList();
        final List<IOperation> operationList = requestList.stream().map(Request::getOperation).collect(Collectors.toList());
        assertThat(operationList).extracting("line").containsExactly(2, 7, 1, - 1);
    }


    @Test
    void test_connectionHandlerTest_transformOperations2() {
        final InputStream is = mock(InputStream.class);
        final OutputStream os = mock(OutputStream.class);
        final CacheManager cacheManager = mock(CacheManager.class);

        final ConnectionHandlerServer chs = new ConnectionHandlerServer(is, os, cacheManager);

//        ReceivedLog
        final RequestLog receivedLog = RequestLog.getInstance();

        final Delete delete4 = new Delete(4);
        final Update up1x = new Update(1, "a", "x");
        final Insert insert4y = new Insert(4, "y");
        final Insert insert2z = new Insert(2, "z");

        final Request receivedRequest1 = new Request(2, "abcd", null, delete4);
        final Request receivedRequest2 = new Request(2, "abcd", null, up1x);
        final Request receivedRequest3 = new Request(2, "abcd", null, insert4y);
        final Request receivedRequest4 = new Request(2, "abcd", null, insert2z);

        receivedLog.addRequest(receivedRequest1, receivedRequest2, receivedRequest3, receivedRequest4);

//        LocalLog
        final RequestLog localLog = mock(RequestLog.class);

        final Insert insert2x = new Insert(2, "x");
        final Insert insert6y = new Insert(6, "y");
        final Delete delete1 = new Delete(1);
        final Update up4z = new Update(4, "d", "z");

        final Request localRequest1 = new Request(1, "abcd", null, insert2x);
        final Request localRequest2 = new Request(1, "abcd", null, insert6y);
        final Request localRequest3 = new Request(1, "abcd", null, delete1);
        final Request localRequest4 = new Request(1, "abcd", null, up4z);

        final LinkedList<Request> localRequestList = new LinkedList<>();
        localRequestList.add(localRequest1);
        localRequestList.add(localRequest2);
        localRequestList.add(localRequest3);
        localRequestList.add(localRequest4);

        when(localLog.getRequestList()).thenReturn(localRequestList);
        when(cacheManager.getRequestLog()).thenReturn(localLog);

        chs.transformOperations(receivedLog);
        final List<Request> requestList = receivedLog.getRequestList();
        final List<IOperation> operationList = requestList.stream().map(Request::getOperation).collect(Collectors.toList());
        assertThat(operationList).extracting("line").containsExactly(4, - 1, 4, 2);
    }


    //FALL1
    @Test
    void test_connectionHandlerTest_transformOperations3() throws Exception {
        final InputStream is = mock(InputStream.class);
        final OutputStream os = mock(OutputStream.class);
        final CacheManager cacheManager = mock(CacheManager.class);

        final ConnectionHandlerServer chs = new ConnectionHandlerServer(is, os, cacheManager);

//        ReceivedLog
        final RequestLog receivedLog = RequestLog.getInstance();

        final Delete delete1 = new Delete(1);
        final Update up1x = new Update(1, "b", "x");
        final Delete delete2 = new Delete(2);

        final Request receivedRequest1 = new Request(2, "abc", null, delete1);
        final Request receivedRequest2 = new Request(2, "abc", null, up1x);
        final Request receivedRequest3 = new Request(2, "abc", null, delete2);

        receivedLog.addRequest(receivedRequest1, receivedRequest2, receivedRequest3);

//        LocalLog
        Constructor<RequestLog> constructor = RequestLog.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        RequestLog localLog = constructor.newInstance();
//        final RequestLog localLog = mock(RequestLog.class);

        final Insert insert2x = new Insert(2, "x");
        final Update up1z = new Update(1, "a", "z");
        final Delete delete3 = new Delete(4);

        final Request localRequest1 = new Request(1, "abc", null, insert2x);
        final Request localRequest2 = new Request(1, "abc", null, up1z);
        final Request localRequest3 = new Request(1, "abc", null, delete3);

        localLog.addRequest(localRequest1);
        localLog.addRequest(localRequest2);
        localLog.addRequest(localRequest3);
        when(cacheManager.getRequestLog()).thenReturn(localLog);

        chs.transformOperations(receivedLog);
        final List<Request> requestList = receivedLog.getRequestList();
        final List<IOperation> operationList = requestList.stream().map(Request::getOperation).collect(Collectors.toList());
        assertThat(operationList).extracting("line").containsExactly(1, -1, 2);

        final String start = "a\nb\nc\n";
        String tmp = start;
        System.out.println(start.replaceAll("\n", ""));
        for (final Request request : localLog.getRequestList()) {
            final IOperation operation = request.getOperation();
            tmp = operation.doOperation(tmp);
            System.out.println(tmp.replaceAll("\n", "") + "   " + operation.toString());
        }
    }


    //FALL2
    @Test
    void test_connectionHandlerTest_transformOperations4() throws Exception{
        final InputStream is = mock(InputStream.class);
        final OutputStream os = mock(OutputStream.class);
        final CacheManager cacheManager = mock(CacheManager.class);

        final ConnectionHandlerServer chs = new ConnectionHandlerServer(is, os, cacheManager);

//        ReceivedLog
        final RequestLog receivedLog = RequestLog.getInstance();

        final Insert insert2x = new Insert(2, "x");
        final Update up1z = new Update(1, "a", "z");
        final Delete delete3 = new Delete(4);

        final Request receivedRequest1 = new Request(1, "abc", null, insert2x);
        final Request receivedRequest2 = new Request(1, "abc", null, up1z);
        final Request receivedRequest3 = new Request(1, "abc", null, delete3);

        receivedLog.addRequest(receivedRequest1, receivedRequest2, receivedRequest3);

//        LocalLog
        Constructor<RequestLog> constructor = RequestLog.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        RequestLog localLog = constructor.newInstance();

        final Delete delete1 = new Delete(1);
        final Update up1x = new Update(1, "b", "x");
        final Delete delete2 = new Delete(2);


        final Request localRequest1 = new Request(2, "abc", null, delete1);
        final Request localRequest2 = new Request(2, "abc", null, up1x);
        final Request localRequest3 = new Request(2, "abc", null, delete2);

        localLog.addRequest(localRequest1);
        localLog.addRequest(localRequest2);
        localLog.addRequest(localRequest3);
        when(cacheManager.getRequestLog()).thenReturn(localLog);

        chs.transformOperations(receivedLog);
        final List<Request> requestList = receivedLog.getRequestList();
        final List<IOperation> operationList = requestList.stream().map(Request::getOperation).collect(Collectors.toList());
//        assertThat(operationList).extracting("line").containsExactly(1, - 1, 2);

        final String start = "a\nb\nc\n";
        String tmp = start;
        System.out.println(start.replaceAll("\n", ""));
        for (final Request request : localLog.getRequestList()) {
            final IOperation operation = request.getOperation();
            tmp = operation.doOperation(tmp);
            System.out.println(tmp.replaceAll("\n", "") + "   " + operation.toString());
        }
    }

}