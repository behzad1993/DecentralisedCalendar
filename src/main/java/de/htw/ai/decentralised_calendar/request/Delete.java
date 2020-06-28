package de.htw.ai.decentralised_calendar.request;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author Behyad Karimi 2019-09-07
 * @project decentralised_calendar
 */
public class Delete implements IOperation, Serializable {

    private int line;
    private final OperationType type = OperationType.DELETE;


    public Delete(final int line) {
        this.line = line;
    }


    @Override
    public String doOperation(final String iCalendar) {
        final Stream<String> lineStream = iCalendar.lines();
        final List<String> lines = lineStream.collect(Collectors.toList());
        if (lines.size() >= this.line) {
            lines.remove(this.line - 1);
        }

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


    @Override
    public String getNewAttribute() {
        return null;
    }


    @Override
    public String toString() {
        return "Delete{" +
                "line=" + line +
                ", type=" + type +
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

        final Delete delete = (Delete) o;
        if (this.line == delete.getLine()) {
            return true;
        }

        return false;
    }
}
