package de.htw.ai.decentralised_calendar.request;

import java.io.Serializable;
import java.util.Arrays;


/**
 * @author Behzad Karimi on 2019-07-30.
 * @project decentralised_calendar
 * <p>
 * A request is a Tupel q(c,r,a,o)
 * c is id of site
 * r serial number of request
 * a is a depending request
 * o is the operation
 */

public class Request implements Serializable {

    private final int siteId;
    private final int requestNr;
    private final String fileName;
    private int[] dependingOnRequest;
    private IOperation operation;


    public Request(final int siteId, final int requestNr, final String fileName, final int[] dependingOnRequest, final IOperation operation) {
        this.siteId = siteId;
        this.requestNr = requestNr;
        this.fileName = fileName;
        this.dependingOnRequest = dependingOnRequest;
        this.operation = operation;
    }


    public Request(final int siteId, final String fileName, final int[] dependingOnRequest, final IOperation operation) {
        this.siteId = siteId;
        this.requestNr = siteId;
        this.fileName = fileName;
        this.dependingOnRequest = dependingOnRequest;
        this.operation = operation;
    }


    public int getSiteId() {
        return this.siteId;
    }


    public String getFileName() {
        return this.fileName;
    }


    public int[] getDependingOnRequest() {
        return this.dependingOnRequest;
    }


    public IOperation getOperation() {
        return this.operation;
    }


    public void setOperation(final IOperation operation) {
        this.operation = operation;
    }


    public int getRequestNr() {
        return this.requestNr;
    }


    public void setDependingOnRequest(final int[] dependingOnRequest) {
        this.dependingOnRequest = dependingOnRequest;
    }


    public String getID() {
        return "" + this.getSiteId() + this.getRequestNr();
    }

    @Override
    public String toString() {
        return "Request{" +
                "operation=" + operation +
                ", siteId=" + siteId +
                ", requestNr=" + requestNr +
                ", fileName='" + fileName + '\'' +
                ", dependingOnRequest=" + Arrays.toString(dependingOnRequest) +
                '}';
    }
}
