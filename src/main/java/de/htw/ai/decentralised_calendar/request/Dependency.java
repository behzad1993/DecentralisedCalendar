package de.htw.ai.decentralised_calendar.request;

/**
 * @author Behzad Karimi 10.09.19
 * @project decentralised_calendar
 */
public class Dependency {

    private final boolean dependent;
    private final int dependencyType;


    public Dependency(final boolean dependent, final int dependencyType) {
        this.dependent = dependent;
        this.dependencyType = dependencyType;
    }


    public boolean isDependent() {
        return this.dependent;
    }


    public int getDependencyType() {
        return this.dependencyType;
    }


    @Override
    public String toString() {
        return "Dependency{" +
                "dependent=" + this.dependent +
                ", dependencyType=" + this.dependencyType +
                '}';
    }
}
