# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing Relational Data using JDBC with Spring](https://spring.io/guides/gs/relational-data-access/)
* [Managing Transactions](https://spring.io/guides/gs/managing-transactions/)

### Changing glogal Log Level
Using the Configuration file from Java. The java.util.logging properties file (by default, this is the logging.properties file in JRE_HOME/lib) can be modified to change the default level of the ConsoleHandler:

```java.util.logging.ConsoleHandler.level = FINER``` and
```.level= FINE```

### DONE
-[x] EngineThread starts the engine wich builds up the connection and sends the cache  
-[x] TCP Channel can be build now.  
-[x] Synchronizer can merge two lists into one.  
-[x] ConnectionHandler can receive one file and save it in one `.ics` file  
-[x] ConnectionHandlerSender can get take one `.ics` file and send it to the client  
-[x] IStorageManager interface for storage and cache  
    -[x] StorageManager is able to do all methods from [ICalendarStorage](src/main/java/de/htw/ai/decentralised_calendar/storage/ICalendarStorage.java)  
    -[x] CacheManager extends from [StorageManager](src/main/java/de/htw/ai/decentralised_calendar/storage/CacheManager.java) and do more methods:  
        - `String copyFileToCache(File file)`  
        - `boolean filenameExistsInCache(String filename)`  
        - `boolean fileExistsInCache(File file)`  
        - `void updateCache()`  

### TODO
-[x] ConnectionHandlerSender should send whole cache to the server  
-[x] ConnectionHandler should receive the whole cache  
    -[ ] and synchrinize it localy. After that it should answer with the synchrinized list of `ICalendar` entries  
-[ ] All tests should be updated to AssertJ  
-[ ] All tests should use `List<ICalendar> createSampleICalendars(int number)` instead of `ICalendar createSampleICalendar()`  
-[ ] Think about following problem: Syncing same `.ics` file, but one got updated. Which one should be keeped and why.  
-[ ] more and more are coming  
-[ ] cache should be produced for every device that needs to be synced  
-[ ] build tcp testcase with disconnections and rebuilding connections  
-[ ] cache should have a list for recipients  
-[ ] engine should have a list of connected peers to prevent multiple connections, ad hoc networks have that some times  
-[ ] have a look at ASAPReceivedChunkListener.java in ASAP  
-[ ] storageManager, replace with streams

https://tools.ietf.org/html/rfc5546#section-4.1.5