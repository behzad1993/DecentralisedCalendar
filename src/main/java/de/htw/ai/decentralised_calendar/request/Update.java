package de.htw.ai.decentralised_calendar.request;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author Behyad Karimi 2019-09-07
 * @project decentralised_calendar
 */
public class Update implements IOperation, Serializable {

    private int line;
    private String newAttribute;
    private String oldAttribute;
    private final OperationType type = OperationType.UPDATE;


    public Update(final int line, final String oldAttribute, final String newAttribute) {
        this.line = line;
        this.newAttribute = newAttribute;
        this.oldAttribute = oldAttribute;
    }


    @Override
    public String doOperation(final String iCalendar) {
        final Stream<String> lineStream = iCalendar.lines();
        final List<String> lines = lineStream.collect(Collectors.toList());
        lines.set(this.line - 1, this.newAttribute);


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


    public void setLine(final int line) {
        this.line = line;
    }


    @Override
    public String getNewAttribute() {
        return this.newAttribute;
    }


    public void setNewAttribute(final String newAttribute) {
        this.newAttribute = newAttribute;
    }


    @Override
    public String getOldAttribute() {
        return this.oldAttribute;
    }


    public void setOldAttribute(final String oldAttribute) {
        this.oldAttribute = oldAttribute;
    }


    @Override
    public String toString() {
        return "Update{" +
                "line=" + line +
                ", newAttribute='" + newAttribute + '\'' +
                ", oldAttribute='" + oldAttribute + '\'' +
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

        final Update update = (Update) o;
        if (this.line == update.getLine()
                && this.newAttribute == update.getNewAttribute()
                && this.oldAttribute == update.getOldAttribute()) {
            return true;
        }

        return false;
    }
}
