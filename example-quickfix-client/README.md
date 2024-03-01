# Example QuickFix Client

All exchange markets use the [(F)inancial (I)nformation E(x)change protocol](https://en.wikipedia.org/wiki/Financial_Information_eXchange) for transmitting transactions and status updates. The nature of FIX is a key value dictionary where keys are numbers representing specific properties of the message, you can find a list of key descriptions [here](https://www.onixs.biz/fix-dictionary/4.2/fields_by_tag.html). To improve developer agility and code maturity it is recommended to use a **Fix Engine** to facilitate parsing and transmitting FIX messages. [QuickFix](https://new.quickfixn.org/) is the industry preferred abstraction library for most programming environments.

## Installation

Add these dependencies to your build.gradle

```gradle
dependencies {
    // This dependency is used by the application.
    implementation group: 'org.quickfixj', name: 'quickfixj-all', version: '2.3.0'
    implementation group: 'org.quickfixj', name: 'quickfixj-core', version: '2.3.0'
    implementation group: 'org.quickfixj', name: 'quickfixj-messages-fix42', version: '2.3.0'
}
```

## Setup

Example config file. Read more [here](https://www.quickfixj.org/usermanual/2.3.0/usage/configuration.html)

```toml
# This is a client (initiator)
[DEFAULT]
DefaultApplVerID=FIX.4.2 # Always make sure the fix version is 4.2
ConnectionType=initiator # Declares that this connection is a user that sends transactions
FileLogPath=./Logs/ # Quickfix keeps logs that it needs to use in its basic operations, set the directory they're stored at here
FileStorePath=./Sessions/ # Quickfix keeps logs that it needs to use in its basic operations, set the directory they're stored at here

### These are the recommended settings for trading with Lime
StartTime=00:00:00
EndTime=00:00:00
UseDataDictionary=Y
ReconnectInterval=60
LogoutTimeout=5
LogonTimeout=30
ResetOnLogon=Y
ResetOnLogout=Y
ResetOnDisconnect=Y
SendRedundantResendRequests=Y
SocketNodelay=N
ValidateUserDefinedFields=N
ValidateFieldsOutOfOrder=N

# session definition
[SESSION]
BeginString=FIX.4.2 # Just like DEFAULT.DefaultApplVerID
SenderCompID= # Insert your sender comp ID here
TargetCompID=LIME # Always Lime
HeartBtInt=30 # Do not change
SocketConnectPort=3000 # PORT FOR FIX SERVER
SocketConnectHost=127.0.0.1 # DOMAIN NAME FOR FIX SERVER
LogonTag 553= # Insert your username here
LogonTag1 555= # Insert your password here
```

### Imports

```java
import quickfix.DefaultSessionFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.Message;
import quickfix.MessageStoreFactory;
import quickfix.SessionFactory;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.field.*;
import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.UnsupportedMessageType;
```

### Quickfix setup:

This is a code snippet that shows how to create a QuickFIX/J initiator, which is a software component used to establish a connection with a remote Financial Information Exchange (FIX) server. Here's a breakdown of the code:

1. `:String configFilename`: This declares a string variable to hold the filename of the configuration file used to configure the QuickFIX/J session.

2. `SessionSettings settings = new SessionSettings(new FileInputStream(configFilename));:` This creates a new SessionSettings object by reading the configuration file specified in configFilename. The FileInputStream class is used to read the file.

3. `LogFactory logFactory = new FileLogFactory(settings);:` This creates a new LogFactory object that will be used to log all messages exchanged between the initiator and the FIX server. In this case, a FileLogFactory is used to write the log messages to a file.

4. `MessageStoreFactory messageFactory = new FileStoreFactory(settings);`: This creates a new MessageStoreFactory object that will be used to store all messages exchanged between the initiator and the FIX server. In this case, a FileStoreFactory is used to write the messages to a file.

4. `SessionFactory sessionFactory= new DefaultSessionFactory(application, messageFactory, logFactory);`: This creates a new SessionFactory object using the DefaultSessionFactory class. The application parameter is a reference to the application that will handle all messages sent and received by the FIX initiator.

6. `Initiator initiator = new SocketInitiator(sessionFactory, settings, 1000000);`: This creates a new SocketInitiator object, which is a type of FIX initiator that uses sockets to communicate with the FIX server. The sessionFactory parameter specifies the session factory to use, the settings parameter specifies the session settings, and the 1000000 parameter specifies the number of milliseconds to wait before timing out.

7. `initiator.start();`: This starts the QuickFIX/J initiator, which will establish a connection with the FIX server using the specified settings and begin sending and receiving messages.

```java
String configFilename; // Click https://www.quickfixj.org/usermanual/2.3.0/usage/configuration.html to read about the configuration file
SessionSettings settings = new SessionSettings(new FileInputStream(configFilename));

LogFactory logFactory = new FileLogFactory(settings);

MessageStoreFactory messageFactory = new FileStoreFactory(settings);

SessionFactory sessionFactory= new DefaultSessionFactory(application, messageFactory, logFactory);

Initiator initiator = new SocketInitiator(sessionFactory, settings, 1000000); // Create a quickfix socket initiator to connect to the FIX server

initiator.start();

// Business logic
```

### Received messages

To processed *received* messages you'll need to create a `class` that implements the quickfix `Application` class.

```java
public class MyApplication implements Application {
    @Override
    public void fromAdmin(Message message, SessionID sessionId)
    throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        // This method is called when the initiator receives a message from the FIX server that is part of the FIX protocol's administrative messages (e.g. Logon, Logout, Heartbeat, etc.).
        // The message parameter is the received message, and the sessionId parameter is the ID of the session on which the message was received.
    }

    @Override
    public void onCreate(SessionID sessionId) {
        // This method is called when a new session is created by the initiator.
        // The sessionId parameter is the ID of the newly created session.
    }

    @Override
    public void onLogon(SessionID sessionId) {
        // This method is called when a session has been successfully logged on to the FIX server.
        // The sessionId parameter is the ID of the session that has been logged on.
    }

    @Override
    public void onLogout(SessionID sessionId) {
        // This method is called when a session has been logged out of the FIX server.
        // The sessionId parameter is the ID of the session that has been logged out.
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        // This method is called when the initiator sends an administrative message to the FIX server (e.g. Logon, Logout, Heartbeat, etc.).
        // The message parameter is the message being sent, and the sessionId parameter is the ID of the session on which the message is being sent.
    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        // This method is called when the initiator sends a non-administrative message to the FIX server (e.g. NewOrderSingle, OrderCancelRequest, etc.).
        // The message parameter is the message being sent, and the sessionId parameter is the ID of the session on which the message is being sent.
    }

    @Override
    public void fromApp(Message message, SessionID sessionId)
    throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        // This method is called when the initiator receives a non-administrative message from the FIX server (e.g. ExecutionReport, OrderCancelReject, etc.).
        // The message parameter is the received message, and the sessionId parameter is the ID of the session on which the message was received.
    }
}
```

## Initiator logic

Here are afew examples on how to do some FIX transactions with Lime.

### Placing a single order

```java
Message message = new quickfix.fix42.NewOrderSingle(
    new ClOrdID(""), // Client order IDs for Lime should be a random string of 8 characters from this set "1234567890ABCDEFGHIJKLMNOPQRSTUV"
    new HandlInst(""),
    new Symbol(""),
    new Side(""),
    new TransactTime(LocalDateTime.now()),
    new OrdType("")
);
```

### Canceling an order

```java
Message message = new quickfix.fix42.OrderCancelRequest(
    new OrigClOrdID(""),
    new ClOrdID(""),
    new Symbol(""),
    new Side(''),
    new TransactTime(LocalDateTime.now()),
);
```

### Replacing an order

```java
message = new quickfix.fix42.OrderCancelReplaceRequest(
    new OrigClOrdID(""),
    new ClOrdID(""),
    new HandlInst(''),
    new Symbol(""),
    new Side(''),
    new TransactTime(LocalDateTime.now()),
    new OrdType('')
);
```
