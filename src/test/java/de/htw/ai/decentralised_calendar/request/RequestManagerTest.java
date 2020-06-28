package de.htw.ai.decentralised_calendar.request;

import biweekly.Biweekly;
import biweekly.ICalendar;
import de.htw.ai.decentralised_calendar.storage.ICalendarGenerator;
import de.htw.ai.decentralised_calendar.storage.StorageManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.LinkedList;

import static de.htw.ai.decentralised_calendar.request.RequestManager.isDependentOn;
import static de.htw.ai.decentralised_calendar.request.RequestManager.perm;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author Behzad Karimi 2019-09-09
 * @project decentralised_calendar
 */
class RequestManagerTest {

    private RequestLog log;
    private final String ical = "BEGIN:VCALENDAR\n" +
            "VERSION:2.0\n" +
            "PRODID:-//Michael Angstadt//biweekly 0.6.3//EN\n" +
            "UID:2e6cacb8-1093-4f5f-ba64-6ba3a768b9cd\n" +
            "NAME:alone\n" +
            "LAST-MODIFIED:20190810T152454Z\n" +
            "BEGIN:VEVENT\n" +
            "DTSTAMP:20190810T152454Z\n" +
            "SUMMARY;LANGUAGE=en-us:Meeting with Team A\n" +
            "DTSTART:20190809T220000Z\n" +
            "DURATION:PT1H\n" +
            "END:VEVENT\n" +
            "END:VCALENDAR";


    @BeforeEach
    void init() throws Exception {
        final Constructor<RequestLog> constructor = RequestLog.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        final RequestLog log = constructor.newInstance();
    }


    @Test
    void test_IT_caseInsIns_notIncreasing() {
        final IOperation localOperation = new Insert(1, "a");
        final IOperation replicateOperation = new Insert(1, "b");

        final Request localRequest = new Request(2, "", null, localOperation);
        final Request replicateRequest = new Request(1, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Insert(1, "b");
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseInsIns_increasingByWinningSiteId() {
        final IOperation localOperation = new Insert(1, "a");
        final IOperation replicateOperation = new Insert(1, "b");

        final Request localRequest = new Request(1, "", null, localOperation);
        final Request replicateRequest = new Request(2, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Insert(2, "b");
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseInsIns_increasingByWinningLine() {
        final IOperation localOperation = new Insert(1, "a");
        final IOperation replicateOperation = new Insert(2, "b");

        final Request localRequest = new Request(1, "", null, localOperation);
        final Request replicateRequest = new Request(2, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Insert(3, "b");
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseInsDel_notDecreasing() {
        final IOperation localOperation = new Delete(1);
        final IOperation replicateOperation = new Insert(1, "b");

        final Request localRequest = new Request(2, "", null, localOperation);
        final Request replicateRequest = new Request(1, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Insert(1, "b");
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseInsDel_decreasing() {
        final IOperation localOperation = new Delete(1);
        final IOperation replicateOperation = new Insert(2, "b");

        final Request localRequest = new Request(1, "", null, localOperation);
        final Request replicateRequest = new Request(2, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Insert(1, "b");
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseDelIns_notIncreasing() {
        final IOperation localOperation = new Insert(3, "a");
        final IOperation replicateOperation = new Delete(2);

        final Request localRequest = new Request(1, "", null, localOperation);
        final Request replicateRequest = new Request(2, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Delete(2);
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseDelIns_Increasing_winningBySameLine() {
        final IOperation localOperation = new Insert(2, "a");
        final IOperation replicateOperation = new Delete(2);

        final Request localRequest = new Request(1, "", null, localOperation);
        final Request replicateRequest = new Request(2, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Delete(3);
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseDelIns_Increasing_winningByGreaterLine() {
        final IOperation localOperation = new Insert(2, "a");
        final IOperation replicateOperation = new Delete(3);

        final Request localRequest = new Request(1, "", null, localOperation);
        final Request replicateRequest = new Request(2, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Delete(4);
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseDelDel_notDecreasing() {
        final IOperation localOperation = new Delete(2);
        final IOperation replicateOperation = new Delete(1);

        final Request localRequest = new Request(1, "", null, localOperation);
        final Request replicateRequest = new Request(2, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Delete(1);
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseDelDel_decreasing() {
        final IOperation localOperation = new Delete(1);
        final IOperation replicateOperation = new Delete(2);

        final Request localRequest = new Request(1, "", null, localOperation);
        final Request replicateRequest = new Request(2, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Delete(1);
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseDelDel_noOperation() {
        final IOperation localOperation = new Delete(1);
        final IOperation replicateOperation = new Delete(1);

        final Request localRequest = new Request(1, "", null, localOperation);
        final Request replicateRequest = new Request(2, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        assertThat(actualOperation instanceof NoOperation).isTrue();
    }


    @Test
    void test_IT_caseUpIns_notIncreasing() {
        final IOperation localOperation = new Insert(2, "a");
        final IOperation replicateOperation = new Update(1, "b", "a");

        final Request localRequest = new Request(2, "", null, localOperation);
        final Request replicateRequest = new Request(1, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Update(1, "b", "a");
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseUpIns_increasing_winningBySameLine() {
        final IOperation localOperation = new Insert(1, "a");
        final IOperation replicateOperation = new Update(1, "b", "a");

        final Request localRequest = new Request(2, "", null, localOperation);
        final Request replicateRequest = new Request(1, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Update(2, "b", "a");
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseUpIns_increasing_winningByGreaterLine() {
        final IOperation localOperation = new Insert(1, "a");
        final IOperation replicateOperation = new Update(2, "b", "a");

        final Request localRequest = new Request(2, "", null, localOperation);
        final Request replicateRequest = new Request(1, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Update(3, "b", "a");
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseUpUp_overwriteLocal() {
        final IOperation localOperation = new Update(1, "a", "x");
        final IOperation replicateOperation = new Update(1, "a", "y");

        final Request localRequest = new Request(1, "", null, localOperation);
        final Request replicateRequest = new Request(2, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Update(1, "x", "y");
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseUpUp_noOperation() {
        final IOperation localOperation = new Update(1, "a", "x");
        final IOperation replicateOperation = new Update(1, "a", "y");

        final Request localRequest = new Request(2, "", null, localOperation);
        final Request replicateRequest = new Request(1, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        assertThat(actualOperation instanceof NoOperation).isTrue();
    }


    @Test
    void test_IT_caseUpUp_dontOverwriteLocal() {
        final IOperation localOperation = new Update(1, "a", "x");
        final IOperation replicateOperation = new Update(2, "a", "y");

        final Request localRequest = new Request(2, "", null, localOperation);
        final Request replicateRequest = new Request(1, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Update(2, "a", "y");
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseUpDel_notDecreasing() {
        final IOperation localOperation = new Delete(2);
        final IOperation replicateOperation = new Update(1, "a", "y");

        final Request localRequest = new Request(1, "", null, localOperation);
        final Request replicateRequest = new Request(2, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Update(1, "a", "y");
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseUpDel_decreasing() {
        final IOperation localOperation = new Delete(1);
        final IOperation replicateOperation = new Update(2, "a", "y");

        final Request localRequest = new Request(1, "", null, localOperation);
        final Request replicateRequest = new Request(2, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new Update(1, "a", "y");
        assertThat(actualOperation).isEqualTo(expectedOperation);
    }


    @Test
    void test_IT_caseUpDel_noOperation() {
        final IOperation localOperation = new Delete(1);
        final IOperation replicateOperation = new Update(1, "a", "y");

        final Request localRequest = new Request(1, "", null, localOperation);
        final Request replicateRequest = new Request(2, "", null, replicateOperation);

        final IOperation actualOperation = RequestManager.it(replicateRequest, localRequest);
        final IOperation expectedOperation = new NoOperation();
        assertThat(actualOperation instanceof NoOperation).isTrue();
    }


    @Test
    void test_isDependentOn_InsIns_isTrue_type1() {
        final Insert insert1 = mock(Insert.class);
        final Insert insert2 = mock(Insert.class);

        final Request request1 = mock(Request.class);
        final Request request2 = mock(Request.class);

        when(request1.getOperation()).thenReturn(insert1);
        when(request2.getOperation()).thenReturn(insert2);
        when(request1.getSiteId()).thenReturn(1);
        when(request2.getSiteId()).thenReturn(2);

        when(insert1.getLine()).thenReturn(1);
        when(insert2.getLine()).thenReturn(1);
        when(insert1.getType()).thenReturn(OperationType.INSERT);
        when(insert2.getType()).thenReturn(OperationType.INSERT);
        when(insert1.getNewAttribute()).thenReturn("a");
        when(insert2.getNewAttribute()).thenReturn("b");

        final Dependency dependentOn = RequestManager.isDependentOn(request1, request2);
        assertThat(dependentOn.isDependent()).isTrue();
        assertThat(dependentOn.getDependencyType()).isEqualTo(1);
    }


    @Test
    void test_isDependentOn_InsIns_isFalse() {
        final Insert insert1 = mock(Insert.class);
        final Insert insert2 = mock(Insert.class);

        final Request request1 = mock(Request.class);
        final Request request2 = mock(Request.class);

        when(request1.getOperation()).thenReturn(insert1);
        when(request2.getOperation()).thenReturn(insert2);
        when(request1.getSiteId()).thenReturn(1);
        when(request2.getSiteId()).thenReturn(2);

        when(insert1.getLine()).thenReturn(1);
        when(insert2.getLine()).thenReturn(2);
        when(insert1.getType()).thenReturn(OperationType.INSERT);
        when(insert2.getType()).thenReturn(OperationType.INSERT);
        when(insert1.getNewAttribute()).thenReturn("a");
        when(insert2.getNewAttribute()).thenReturn("b");

        final Dependency dependentOn = isDependentOn(request1, request2);
        assertThat(dependentOn.isDependent()).isFalse();
    }


    @Test
    void test_perm_noDependency_transformOperation() {
        final Insert insert1a = new Insert(1, "a");
        final Delete delete2 = new Delete(2);

        final Request request1 = mock(Request.class);
        final Request request2 = mock(Request.class);

        when(request1.getOperation()).thenReturn(insert1a);
        when(request2.getOperation()).thenReturn(delete2);
        when(request1.getSiteId()).thenReturn(1);
        when(request2.getSiteId()).thenReturn(2);

        final Request[] perm = perm(request2, request1);
        final IOperation newInsert1a = perm[1].getOperation();
        final IOperation newDelete2 = perm[0].getOperation();

        assertThat(newInsert1a).isEqualTo(insert1a);
        assertThat(newDelete2.getLine()).isEqualTo(1);
    }


    @Test
    void test_generatingRequest_insertOnFile() throws Exception {
        final StorageManager storageManager = new StorageManager("extern/testFiles/requestManager/");
        final RequestManager requestManager = new RequestManager(storageManager);

        final Insert insert = new Insert(9, "SUMMARY;LANGUAGE=en-us:Meeting with Team A");
        final String filename = "2e6cacb8-1093-4f5f-ba64-6ba3a768b9cd.ics";

        final Request request = requestManager.generatingRequest(insert, filename);
        assertThat(request).isNotNull();
    }


    @Test
    void test_generatingRequest_InsUpDel() throws Exception {
        final StorageManager storageManager = new StorageManager("extern/testFiles/requestManager/");
        final RequestManager requestManager = new RequestManager(storageManager);

        final Insert insert = new Insert(9, "SUMMARY;LANGUAGE=en-us:Meeting with Team A");
        final Update update = new Update(9, "SUMMARY;LANGUAGE=en-us:Meeting with Team A", "SUMMARY;LANGUAGE=en-us:Meeting with Team B");
        final Delete delete = new Delete(5);
        final String filename = "2e6cacb8-1093-4f5f-ba64-6ba3a768b9cd.ics";

        requestManager.generatingRequest(insert, filename);
        requestManager.generatingRequest(update, filename);
        requestManager.generatingRequest(delete, filename);
    }


    @Test
    void test_generatingRequest_withoutDependentRequests() throws Exception {
        final StorageManager storageManager = new StorageManager("extern/testFiles/requestManager/");
        final ICalendar sampleICalendar = ICalendarGenerator.createSampleICalendar();
        final String filename = storageManager.saveEntry(sampleICalendar);
        final RequestManager requestManager = new RequestManager(storageManager);

        final Insert insert = new Insert(13, "ATTENDEE;ROLE=ATTENDEE;CN=user;EMAIL=NEWUSER@mail.com:uri");
        final Update update = new Update(10, "SUMMARY;LANGUAGE=en-us:Meeting with Team A", "SUMMARY;LANGUAGE=en-us:Meeting with Team B");
        final Delete delete = new Delete(18);
        System.out.println(insert);
        System.out.println(delete);
        System.out.println(update);
        System.out.println();

        final RequestLog instance = RequestLog.getInstance();
        final LinkedList<Request> requestListToClear = instance.getRequestList();
        requestListToClear.clear();
        instance.setRequestList(requestListToClear);

        requestManager.generatingRequest(insert, filename);
        requestManager.generatingRequest(update, filename);
        requestManager.generatingRequest(delete, filename);
        final String result = storageManager.getEntry(filename);

        final LinkedList<Request> requestList = requestManager.getLog().getRequestList();
        final String sampleICalendarString = Biweekly.write(sampleICalendar).go();
        String expected = sampleICalendarString;
        for (final Request request : requestList) {
            final IOperation operation = request.getOperation();
            System.out.println(operation);
            expected = operation.doOperation(expected);
        }

        assertThat(result).isEqualToIgnoringNewLines(expected);
    }


    @Test
    void test_generatingRequest_withDependentRequests() throws Exception {
        final StorageManager storageManager = new StorageManager("extern/testFiles/requestManager/");
        final ICalendar sampleICalendar = ICalendarGenerator.createSampleICalendar();
        final String filename = storageManager.saveEntry(sampleICalendar);
        final RequestManager requestManager = new RequestManager(storageManager);

        final Update update = new Update(18, "ATTENDEE;ROLE=ATTENDEE;CN=user;EMAIL=NEWUSER@mail.com:uri", "ATTENDEE;ROLE=ATTENDEE;CN=user;EMAIL=DOUBLENEWUSER@mail.com:uri");
        final Insert insert = new Insert(18, "ATTENDEE;ROLE=ATTENDEE;CN=user;EMAIL=NEWUSER@mail.com:uri");
        final Delete delete = new Delete(18);
        System.out.println(insert);
        System.out.println(update);
        System.out.println(delete);
        System.out.println();

        final RequestLog instance = RequestLog.getInstance();
        final LinkedList<Request> requestListToClear = instance.getRequestList();
        requestListToClear.clear();
        instance.setRequestList(requestListToClear);

        LinkedList<Request> cbfList = new LinkedList<>();

        cbfList.add(requestManager.generatingRequest(insert, filename));
        cbfList.add(requestManager.generatingRequest(update, filename));
        cbfList.add(requestManager.generatingRequest(delete, filename));
        final String result = storageManager.getEntry(filename);

        final LinkedList<Request> requestList = requestManager.getLog().getRequestList();
        final String sampleICalendarString = Biweekly.write(sampleICalendar).go();
        String expected = sampleICalendarString;
        for (final Request request : requestList) {
            final IOperation operation = request.getOperation();
            System.out.println(operation);
            expected = operation.doOperation(expected);
        }

        System.out.println();
        for (Request request : cbfList) {
            System.out.println(request);
        }

        assertThat(result).isEqualToIgnoringNewLines(expected);
    }
}