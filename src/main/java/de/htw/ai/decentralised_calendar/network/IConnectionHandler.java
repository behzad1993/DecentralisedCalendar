package de.htw.ai.decentralised_calendar.network;

public interface IConnectionHandler {

    /**
     * Building up the connection and using the CacheReader thread to save the received cache
     */
    public void handleConnection();

    /**
     * The server synchronizes the received cache with its own cache, after that it saved the new cache to the own
     * storage and sends it to the client.
     */
    public void handleAnswer();

}
