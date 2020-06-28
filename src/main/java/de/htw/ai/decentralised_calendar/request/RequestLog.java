package de.htw.ai.decentralised_calendar.request;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 * @author Behyad Karimi 2019-09-08
 * @project decentralised_calendar
 */
public class RequestLog implements Serializable {

    private static RequestLog requestLogInstance;
    private LinkedList<Request> requestList;


    private RequestLog() {
        this.requestList = new LinkedList<>();
    }


    public static synchronized RequestLog getInstance() {
        if (requestLogInstance == null) {
            requestLogInstance = new RequestLog();
        }
        return requestLogInstance;
    }


    public LinkedList<Request> getRequestList() {
        return this.requestList;
    }


    public void addRequest(final Request... requests) {
        for (final Request request : requests) {
            this.requestList.add(request);
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Request request : requestList) {
            sb.append(request + "\n");
        }
        String requestListAsString = sb.toString();
        return "RequestLog{" +
                "requestList=" + requestListAsString +
                '}';
    }


    public void setRequestList(final LinkedList<Request> requestList) {
        this.requestList = requestList;
    }
}