package de.htw.ai.decentralised_calendar.request;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author Behyad Karimi 2019-09-07
 * @project decentralised_calendar
 */
public class Insert implements IOperation, Serializable {

    private int line;
    private final String newAttribute;
    private final OperationType type = OperationType.INSERT;


    public Insert(final int line, final String newAttribute) {
        this.line = line;
        this.newAttribute = newAttribute;
    }


    @Override
    public int getLine() {
        return this.line;
    }


    @Override
    public OperationType getType() {
        return this.type;
    }


    @Override
    public String getOldAttribute() {
        return null;
    }


    public String getNewAttribute() {
        return this.newAttribute;
    }


    @Override
    public String doOperation(final String iCalendar) {
        final Stream<String> lineStream = iCalendar.lines();
        final List<String> lines = lineStream.collect(Collectors.toList());
        lines.add(this.line - 1, this.newAttribute);

        final StringBuilder sb = new StringBuilder();
        for (final String s : lines) {
            sb.append(s + "\n");
        }
        final String transformedICal = sb.toString();
        return transformedICal;
    }


    @Override
    public void incrementLine() {
        this.line++;
    }


    @Override
    public void decrementLine() {
        if (this.line > 1) {
            this.line--;
        }
    }


    @Override
    public String toString() {
        return "Insert{" +
                "line=" + this.line +
                ", newAttribute='" + this.newAttribute + '\'' +
                ", type=" + this.type +
                '}';
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }

        final Insert insert = (Insert) o;
        if (this.line == insert.getLine()
                && this.newAttribute == insert.newAttribute) {
            return true;
        }

        return false;
    }
}
