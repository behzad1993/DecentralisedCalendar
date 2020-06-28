//package de.htw.ai.decentralised_calendar.request;
//
//import biweekly.component.VEvent;
//import biweekly.property.Uid;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
///**
// * @author Behyad Karimi 2019-09-08
// * @project decentralised_calendar
// */
//class RequestLogTest {
//
//    private static RequestLog requestLog;
//
//
//    @BeforeAll
//    static void setUp() {
//        requestLog = RequestLog.getInstance();
//    }
//
//
//    @BeforeEach
//    void clearLogList() {
//        final List<IOperation> insertList = requestLog.getOperationList(OperationType.INSERT);
//        final List<IOperation> updateList = requestLog.getOperationList(OperationType.UPDATE);
//        final List<IOperation> deleteList = requestLog.getOperationList(OperationType.DELETE);
//
//        insertList.clear();
//        updateList.clear();
//        deleteList.clear();
//
//        requestLog.setOperationList(OperationType.INSERT, insertList);
//        requestLog.setOperationList(OperationType.UPDATE, updateList);
//        requestLog.setOperationList(OperationType.DELETE, deleteList);
//    }
//
//
//    @Test
//    void sandbox() {
//        final Uid uid = Uid.random();
//        final VEvent vEvent = new VEvent();
//        vEvent.setUid(uid);
//        final Update update = new Update(1, uid, vEvent.toString(), vEvent.toString());
//        IOperation o = update;
//
//        System.out.println(o.getClass());
//        System.out.println(o.getClass().getName());
//        System.out.println(o.getClass().getSimpleName());
//    }
//
//
//    @Test
//    void test_requestLogTest_addInsert() {
//        final Uid uid = Uid.random();
//        final VEvent vEvent = new VEvent();
//        vEvent.setUid(uid);
//        final Insert insert = new Insert(1, uid, vEvent.toString());
//
//        requestLog = RequestLog.getInstance();
////        requestLog.addOperation(insert);
//
////        assertThat(requestLog.getLastInsertOperation()).isEqualTo(0);
//        final List<IOperation> operationList = requestLog.getOperationList(OperationType.INSERT);
//        assertThat(operationList).containsExactly(insert);
//    }
//
//
//    @Test
//    void test_requestLogTest_addUpdate() {
//        final Uid uid = Uid.random();
//        final VEvent vEvent = new VEvent();
//        vEvent.setUid(uid);
//        final Update update = new Update(1, uid, vEvent.toString(), vEvent.toString());
//
//        requestLog = RequestLog.getInstance();
////        requestLog.addOperation(update);
//
//        final List<IOperation> operationList = requestLog.getOperationList(OperationType.UPDATE);
//        assertThat(operationList).containsExactly(update);
//    }
//
//
//    @Test
//    void test_requestLogTest_addDelete() {
//        final Uid uid = Uid.random();
//        final VEvent vEvent = new VEvent();
//        vEvent.setUid(uid);
//        final Delete delete = new Delete(1, uid, vEvent.toString());
//
//        requestLog = RequestLog.getInstance();
////        requestLog.addOperation(delete);
//
//        final List<IOperation> operationList = requestLog.getOperationList(OperationType.DELETE);
//        assertThat(operationList).containsExactly(delete);
//    }
//
//
//    @Test
//    void test_requestLogTest_addMultipleInsertOperations_increasingOrder() {
//        final Uid uid = Uid.random();
//        final VEvent vEvent = new VEvent();
//        vEvent.setUid(uid);
//
//        final Insert insertLine1 = new Insert(1, uid, vEvent.toString());
//        final Insert insertLine5 = new Insert(5, uid, vEvent.toString());
//        final Insert insertLine7 = new Insert(7, uid, vEvent.toString());
//        final Insert insertLine10 = new Insert(10, uid, vEvent.toString());
//
////        requestLog.addOperation(insertLine1);
////        requestLog.addOperation(insertLine5);
////        requestLog.addOperation(insertLine7);
////        requestLog.addOperation(insertLine10);
//
//        List<IOperation> operationList = requestLog.getOperationList(OperationType.INSERT);
//        assertThat(operationList).extracting("line").containsExactly(1, 5, 7, 10);
//    }
//
//    @Test
//    void test_requestLogTest_addMultipleInsertOperations_randomOrder() {
//        final Uid uid = Uid.random();
//        final VEvent vEvent = new VEvent();
//        vEvent.setUid(uid);
//
//        final Insert insertLine1 = new Insert(1, uid, vEvent.toString());
//        final Insert insertLine5 = new Insert(5, uid, vEvent.toString());
//        final Insert insertLine7 = new Insert(7, uid, vEvent.toString());
//        final Insert insertLine10 = new Insert(10, uid, vEvent.toString());
//
////        requestLog.addOperation(insertLine1);
////        requestLog.addOperation(insertLine7);
////        requestLog.addOperation(insertLine10);
////        requestLog.addOperation(insertLine5);
//
//        List<IOperation> operationList = requestLog.getOperationList(OperationType.INSERT);
//        assertThat(operationList).extracting("line").containsExactlyInAnyOrder(1, 5, 8, 11);
//    }
//}