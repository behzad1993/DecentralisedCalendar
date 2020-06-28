package de.htw.ai.decentralised_calendar.request;

import biweekly.property.Uid;


/**
 * @author Behyad Karimi 2019-09-07
 * @project decentralised_calendar
 */
public interface IOperation{

    String doOperation(String iCalendar);

    void incrementLine();

    void decrementLine();

    int getLine();

    OperationType getType();

    String getOldAttribute();

    String getNewAttribute();
}
