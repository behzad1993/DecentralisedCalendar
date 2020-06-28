package de.htw.ai.decentralised_calendar.request;

import java.io.Serializable;


/**
 * @author Behzad Karimi 2019-09-09
 * @project decentralised_calendar
 */
public class DeleteFile implements IOperation, Serializable {

    private final OperationType type = OperationType.DELETE_FILE;
    private final String filename;


    public DeleteFile(final String filename) {
        this.filename = filename;
    }


    public String getFilename() {
        return this.filename;
    }


    @Override
    public String doOperation(final String filename) {
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
        return "DeleteFile{" +
                "type=" + type +
                ", filename='" + filename + '\'' +
                '}';
    }
}
