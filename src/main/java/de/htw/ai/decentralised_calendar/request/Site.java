package de.htw.ai.decentralised_calendar.request;

/**
 * @author Behzad Karimi on 2019-07-30.
 * @project decentralised_calendar
 */
public class Site {

    private int id;
    private int serialNumber;
    private String name;


    public Site(int id, int serialNumber, String name) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.name = name;
    }


    public int getId() {
        return this.id;
    }


    public void setId(final int id) {
        this.id = id;
    }


    public int getSerialNumber() {
        return this.serialNumber;
    }


    public void setSerialNumber(final int serialNumber) {
        this.serialNumber = serialNumber;
    }


    public String getName() {
        return this.name;
    }


    public void setName(final String name) {
        this.name = name;
    }
}
