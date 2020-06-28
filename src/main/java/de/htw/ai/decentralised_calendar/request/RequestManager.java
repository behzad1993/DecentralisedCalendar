package de.htw.ai.decentralised_calendar.request;

import biweekly.Biweekly;
import biweekly.ICalendar;
import de.htw.ai.decentralised_calendar.storage.StorageManager;
import org.apache.commons.lang3.SerializationUtils;

import java.util.*;

import static de.htw.ai.decentralised_calendar.request.OperationType.*;


/**
 * @author Behzad Karimi 2019-09-09
 * @project decentralised_calendar
 */
public class RequestManager {


    private RequestLog log;
    private int requestCounter;
    private final StorageManager storageManager;
    private final Queue<Request> requestQueue;
    private final HashSet<String> usedRequests;


    public RequestManager(final StorageManager storageManager) {
        this.log = RequestLog.getInstance();
        this.requestCounter = 0;
        this.storageManager = storageManager;
        this.requestQueue = new LinkedList<>();
        this.usedRequests = new HashSet<>();
    }


    public Request generatingRequest(final IOperation operation, final String filename) throws Exception {
        if (operation.getType().equals(CREATE_FILE)) {
            final String iCal = operation.getNewAttribute();
            final ICalendar iCalendarObject = Biweekly.parse(iCal).first();
            this.storageManager.saveEntry(iCalendarObject);
        } else if (operation.getType().equals(DELETE_FILE)) {
            final DeleteFile deleteFile = (DeleteFile) operation;
            final String fileName = deleteFile.getFilename();
            this.storageManager.deleteEntry(fileName);
        } else {
            String iCal = this.storageManager.getEntry(filename);
            iCal = operation.doOperation(iCal);
            final ICalendar iCalendarObj = Biweekly.parse(iCal).first();
            if (this.storageManager.saveEntry(iCalendarObj) == null) {
                throw new Exception();
            }
        }

        Random rand = new Random();
        int id = rand.nextInt(100000000);

//        final Request request = new Request(this.storageManager.getId(), this.requestCounter, filename, null, operation);
        final Request request = new Request(this.storageManager.getId(), id, filename, null, operation);
        this.usedRequests.add(request.getID());
        this.requestCounter++;
        final Request requestComputed = cbf(request, this.log);
        final LinkedList<Request> canonized = canonize(this.log.getRequestList(), request);
        this.log.setRequestList(canonized);

        return requestComputed;
    }


    public void addToQueue(final LinkedList<Request> receivedRequests) {
        while (! receivedRequests.isEmpty()) {
            final Request request = receivedRequests.pollFirst();
            if (! this.usedRequests.contains(request.getID())) {
                this.requestQueue.add(request);
                this.usedRequests.add(request.getID());
            }
        }

        this.integrateRemoteRequest();
    }


    //TODO: canonized = canonize(this.log.getRequestList(), request); nur einmal ausfuehren
    public void integrateRemoteRequest() {
        while (! this.requestQueue.isEmpty()) {
            final LinkedList<Request> canonized;
            final Request request = this.requestQueue.poll();
            final IOperation operation = request.getOperation();
            if (operation.getType().equals(CREATE_FILE)) {
                final String iCal = operation.getNewAttribute();
                final ICalendar iCalendarObject = Biweekly.parse(iCal).first();
                this.storageManager.saveEntry(iCalendarObject);
                canonized = canonize(this.log.getRequestList(), request);
            } else if (operation.getType().equals(DELETE_FILE)) {
                final String fileName = request.getFileName();
                this.storageManager.deleteEntry(fileName);
                canonized = canonize(this.log.getRequestList(), request);
            } else {
                final Request computed = this.computeff(request, this.log);
                String iCal = this.storageManager.getEntry(computed.getFileName());
                iCal = computed.getOperation().doOperation(iCal);
                final ICalendar iCalendarObject = Biweekly.parse(iCal).first();
                this.storageManager.saveEntry(iCalendarObject);
                canonized = canonize(this.log.getRequestList(), computed);
            }
            this.log.setRequestList(canonized);
        }
    }


    //TODO remove
    public void setLog(final RequestLog log) {
        this.log = log;
    }


    private Request computeff(final Request request, final RequestLog log) {
        final Request toCompute = SerializationUtils.clone(request);
        final int j = - 1;

        final int[] dependingOnRequest = toCompute.getDependingOnRequest();
        if (dependingOnRequest != null) {

        }
        for (int i = j + 1; i <= log.getRequestList().size() - 1; i++) {
            final Request localRequest = log.getRequestList().get(i);
            if (toCompute.getFileName() == localRequest.getFileName()) {
                final IOperation operation = it(toCompute, localRequest);
                toCompute.setOperation(operation);
            }
        }
        return toCompute;
    }


    public RequestLog getLog() {
        return this.log;
    }


    public static Request[] perm(final Request r2, final Request r1) {
        final Dependency dependentOn = isDependentOn(r1, r2);
        if (dependentOn.isDependent() || r2.getFileName() != r1.getFileName()) {
            return null;
        }

        final IOperation o2 = et(r2, r1);
        r2.setOperation(o2);

        final IOperation o1 = it(r1, r2);
        r1.setOperation(o1);

        return new Request[]{r2, r1};
    }


    public static LinkedList<Request> canonize(final LinkedList<Request> requestList, final Request request) {
        final LinkedList<Request> canonized = requestList;
        requestList.add(request);

        int i = canonized.size() - 1;
//        while (!isCanonized(requestList)) {
        while (i > 0) {
            final Request[] perm = perm(requestList.get(i), requestList.get(i - 1));
            if (perm == null) {
                return canonized;
            }
            requestList.set(i - 1, perm[0]);
            requestList.set(i, perm[1]);
            i--;
        }
        return canonized;
    }


    private static boolean isCanonized(final List<Request> requestList) {
        boolean firstDelete = false;
        boolean insert = false;
        for (final Request request : requestList) {
            final IOperation operation = request.getOperation();
            final OperationType type = operation.getType();
            if (type == INSERT) {
                insert = true;
            }
            if (type == DELETE) {
                firstDelete = true;
            }
        }
        return false;
    }


    public static Request cbf(final Request request, final RequestLog requestLog) {
        final Request toCompute = SerializationUtils.clone(request);
        ;
        final List<Request> requestList = requestLog.getRequestList();
        for (int i = requestList.size() - 1; i >= 0; i--) {
            final Request checkAgainst = requestList.get(i);
            final Dependency dependent = isDependentOn(toCompute, checkAgainst);
            if (! dependent.isDependent()) {
                final IOperation operation = et(toCompute, checkAgainst);
                toCompute.setOperation(operation);
            } else {
                final int[] identity = {checkAgainst.getSiteId(), checkAgainst.getRequestNr(), dependent.getDependencyType()};
                toCompute.setDependingOnRequest(identity);
                return toCompute;
            }
        }
        return toCompute;
    }


    public static Dependency isDependentOn(final Request toCompute, final Request checkAgainst) {
        final IOperation oToCompute = toCompute.getOperation();
        final IOperation oCheckingAgainst = checkAgainst.getOperation();
        final int lineToCompute = oToCompute.getLine();
        final int lineToCheckAgainst = oCheckingAgainst.getLine();
        final OperationType toComputeType = oToCompute.getType();
        final OperationType checkAgainstType = oCheckingAgainst.getType();

        boolean isDependent = false;
        int dependencyType = 0;
        if (toComputeType == INSERT && checkAgainstType == INSERT) {
            if (lineToCompute == lineToCheckAgainst
                    && oToCompute.getNewAttribute() != oCheckingAgainst.getNewAttribute()
                    && toCompute.getSiteId() <= checkAgainst.getSiteId()) {
                isDependent = true;
                dependencyType = 1;
            } else if (lineToCompute == lineToCheckAgainst + 1
                    && oToCompute.getNewAttribute() != oCheckingAgainst.getNewAttribute()
                    && toCompute.getSiteId() >= checkAgainst.getSiteId()) {
                isDependent = true;
                dependencyType = 2;
            }
        } else if (toComputeType == INSERT && checkAgainstType == DELETE) {
            if (lineToCompute == lineToCheckAgainst) {
                isDependent = true;
                dependencyType = 3;
            }
        } else if (toComputeType == INSERT && checkAgainstType == UPDATE) {
            if (lineToCompute == lineToCheckAgainst
                    && oToCompute.getNewAttribute() == oCheckingAgainst.getOldAttribute()) {
                isDependent = true;
                dependencyType = 5;
            }
        } else if (toComputeType == UPDATE && checkAgainstType == UPDATE) {
            if (lineToCompute == lineToCheckAgainst
                    && oToCompute.getNewAttribute() == oCheckingAgainst.getOldAttribute()
                    && toCompute.getSiteId() >= checkAgainst.getSiteId()) {
                isDependent = true;
                dependencyType = 4;
            }
        } else if (toComputeType == CREATE_FILE && checkAgainstType == INSERT) {
            final String filenameToCompute = toCompute.getFileName();
            final String filenameCheckAgainst = checkAgainst.getFileName();
            if (filenameToCompute == filenameCheckAgainst) {
                isDependent = true;
                dependencyType = 6;
            }
        } else if (toComputeType == CREATE_FILE && checkAgainstType == UPDATE) {
            final String filenameToCompute = toCompute.getFileName();
            final String filenameCheckAgainst = checkAgainst.getFileName();
            if (filenameToCompute == filenameCheckAgainst) {
                isDependent = true;
                dependencyType = 7;
            }
        } else if (toComputeType == CREATE_FILE && checkAgainstType == DELETE) {
            final String filenameToCompute = toCompute.getFileName();
            final String filenameCheckAgainst = checkAgainst.getFileName();
            if (filenameToCompute == filenameCheckAgainst) {
                isDependent = true;
                dependencyType = 8;
            }
        } else if (toComputeType == CREATE_FILE && checkAgainstType == DELETE_FILE) {
            final String filenameToCompute = toCompute.getFileName();
            final String filenameCheckAgainst = checkAgainst.getFileName();
            if (filenameToCompute == filenameCheckAgainst) {
                isDependent = true;
                dependencyType = 9;
            }
        }
        return new Dependency(isDependent, dependencyType);
    }


    /**
     * IT stands for Inclusive Transformation
     *
     * @param replicateRequest is the request which is coming in (replicate)
     * @param localRequest     is the request it depents on (local)
     * @return
     */
    public static IOperation it(final Request replicateRequest, final Request localRequest) {
        IOperation replicateOperation = replicateRequest.getOperation();
        final IOperation localOperation = localRequest.getOperation();
        final OperationType localOpType = localOperation.getType();
        final OperationType replicationOpType = replicateOperation.getType();
        final int replicateLine = replicateOperation.getLine();
        final int localLine = localOperation.getLine();
        final int replicateId = replicateRequest.getSiteId();
        final int localId = localRequest.getSiteId();

//        IT Algorithm
        if (replicationOpType == INSERT && localOpType == INSERT) {
            if (localLine < replicateLine || (localLine == replicateLine && localId < replicateId)) {
                replicateOperation.incrementLine();
            }
        } else if (replicationOpType == INSERT && localOpType == DELETE) {
            if (localLine < replicateLine) {
                replicateOperation.decrementLine();
            }
        } else if (replicationOpType == DELETE && localOpType == INSERT) {
            if (localLine <= replicateLine) {
                replicateOperation.incrementLine();
            }
        } else if (replicationOpType == DELETE && localOpType == DELETE) {
            if (localLine < replicateLine) {
                replicateOperation.decrementLine();
            } else if (localLine == replicateLine) {
                replicateOperation = new NoOperation();
            }
        } else if (replicationOpType == UPDATE && localOpType == INSERT) {
            if (localLine <= replicateLine) {
                replicateOperation.incrementLine();
            }
        } else if (replicationOpType == UPDATE && localOpType == UPDATE) {
            if (localLine == replicateLine && localId < replicateId) {
                final String upLocalNew = localOperation.getNewAttribute();
                final String upReplicateNew = replicateOperation.getNewAttribute();
                replicateOperation = new Update(replicateLine, upLocalNew, upReplicateNew);
            } else if (localLine == replicateLine && localId > replicateId) {
                replicateOperation = new NoOperation();
            }
        } else if (replicationOpType == UPDATE && localOpType == DELETE) {
            if (localLine < replicateLine) {
                replicateOperation.decrementLine();
            } else if (localLine == replicateLine) {
                replicateOperation = new NoOperation();
            }
        }
        return replicateOperation;
    }


    /**
     * IT stands for Inclusive Transformation
     *
     * @param replicateRequest is the request which is coming in (replicate)
     * @param localRequest     is the request it depents on (local)
     * @return
     */
    public static IOperation et(final Request replicateRequest, final Request localRequest) {
        IOperation replicateOperation = replicateRequest.getOperation();
        final IOperation localOperation = localRequest.getOperation();
        final OperationType localOpType = localOperation.getType();
        final OperationType replicationOpType = replicateOperation.getType();
        final int replicateLine = replicateOperation.getLine();
        final int localLine = localOperation.getLine();
        final int replicateId = replicateRequest.getSiteId();
        final int localId = localRequest.getSiteId();

//        ET Algorithm
        if (replicationOpType == INSERT && localOpType == INSERT) {
            if ((replicateLine == localLine && replicateId > localId) || (replicateLine == localLine + 1 && replicateId > localId)) {
                replicateOperation = new NoOperation();
            } else {
                localOperation.decrementLine();
            }
        } else if (replicationOpType == INSERT && localOpType == DELETE) {
//            if (replicateLine == localLine) {
//                return localOperation;
            if (replicateLine > localLine) {
                replicateOperation.incrementLine();
            }
        } else if (replicationOpType == DELETE && localOpType == INSERT) {
            if (replicateLine >= localLine + 1) {
                replicateOperation.decrementLine();
            } else if (replicateLine == localLine) {
                replicateOperation = new NoOperation();
            }
        } else if (replicationOpType == DELETE && localOpType == DELETE) {
            if (replicateLine >= localLine) {
                replicateOperation.incrementLine();
            }
        } else if (replicationOpType == UPDATE && localOpType == INSERT) {
            if (replicateLine > localLine) {
                replicateOperation.decrementLine();
            }
        } else if (replicationOpType == UPDATE && localOpType == UPDATE) {
            if (replicateLine == localLine && replicateId > localId) {
                replicateOperation = new Update(replicateLine, localOperation.getOldAttribute(), localOperation.getNewAttribute());
            } else if (replicateLine == localLine) {
                replicateOperation = new NoOperation();
            }
        } else if (replicationOpType == UPDATE && localOpType == DELETE) {
            if (replicateLine > localLine) {
                replicateOperation.incrementLine();
            }
        }
        return replicateOperation;
    }


    public HashSet<String> getUsedRequests() {
        return usedRequests;
    }
}
