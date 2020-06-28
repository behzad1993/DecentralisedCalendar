package de.htw.ai.decentralised_calendar.request;

import java.io.Serializable;


/**
 * @author Behzad Karimi 2019-09-09
 * @project decentralised_calendar
 */
public class NoOperation implements IOperation, Serializable {

    private final OperationType type;


    public NoOperation() {
        this.type = OperationType.NO_OPERATION;
    }


    @Override
    public String doOperation(final String iCalendar) {
        return iCalendar;
    }


    @Override
    public void incrementLine() {

    }


    @Override
    public void decrementLine() {

    }


    @Override
    public int getLine() {
        return - 1;
    }


    @Override
    public OperationType getType() {
        return this.type;
    }


    @Override
    public String getOldAttribute() {
        return null;
    }


    @Override
    public String getNewAttribute() {
        return null;
    }


    @Override
    public String toString() {
        return "NoOperation{" +
                "type=" + this.type +
                '}';
    }
}
