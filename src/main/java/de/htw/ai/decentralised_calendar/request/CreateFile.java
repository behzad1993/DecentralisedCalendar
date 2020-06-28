package de.htw.ai.decentralised_calendar.request;

import java.io.Serializable;


/**
 * @author Behzad Karimi 2019-09-09
 * @project decentralised_calendar
 */
public class CreateFile implements IOperation, Serializable {

    String iCalendar;
    private final OperationType type = OperationType.CREATE_FILE;


    public CreateFile(final String iCalendar) {
        this.iCalendar = iCalendar;
    }


    @Override
    public String doOperation(final String iCalendar) {
        return null;
    }


    @Override
    public void incrementLine() {

    }


    @Override
    public void decrementLine() {

    }


    @Override
    public int getLine() {
        return 0;
    }


    @Override
    public OperationType getType() {
        return type;
    }


    @Override
    public String getOldAttribute() {
        return null;
    }


    @Override
    public String getNewAttribute() {
        return this.iCalendar;
    }


    @Override
    public String toString() {
        return "CreateFile{" +
                ", type=" + type +
                '}';
    }
}
