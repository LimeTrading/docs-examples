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
DefaultApplVerID=FIX.4.2
 #settings which apply to all the Sessions.
ConnectionType=initiator
 # FIX messages have a sequence ID, which shouldn't be used for uniqueness as specification doesn't guarantee anything about them. If Y is provided every time logon message is sent, server will reset the sequence.
FileLogPath=./Logs/
 #Path where logs will be written
StartTime=00:00:00
 # Time when session starts and ends
EndTime=00:00:00
UseDataDictionary=Y
 #Time in seconds before your session will expire, keep sending heartbeat requests if you don't want it to expire
ReconnectInterval=60
LogoutTimeout=5
LogonTimeout=30
 # Time in seconds before reconnecting
ResetOnLogon=Y
ResetOnLogout=Y
ResetOnDisconnect=Y
SendRedundantResendRequests=Y
# RefreshOnLogon=Y
SocketNodelay=N
# PersistMessages=Y
ValidateUserDefinedFields=N
ValidateFieldsOutOfOrder=N
# CheckLatency=Y

# session definition
[SESSION]
BeginString=FIX.4.2
SenderCompID=CLIENT
TargetCompID=SERVER
HeartBtInt=30
SocketConnectPort=3000 # PORT FOR FIX SERVER
SocketConnectHost=127.0.0.1 # DOMAIN NAME FOR FIX SERVER
# DataDictionary=./spec/FIX43.xml
FileStorePath=./Sessions/
LogonTag 553=YOUR USERNAME
LogonTag1 555=YOUR PASSWORD
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
    }

    @Override
    public void onCreate(SessionID sessionId) {
    }

    @Override
    public void onLogon(SessionID sessionId) {
    }

    @Override
    public void onLogout(SessionID sessionId) {
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
    }

    @Override
    public void fromApp(Message message, SessionID sessionId)
    throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
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
    new OrigClOrdID(prompt("OrigClOrdID")),
    new ClOrdID(prompt("ClOrdId")),
    new Symbol(prompt("Symbol")),
    new Side(promptChar("Side")),
    new TransactTime(LocalDateTime.now())
);
```

### Replacing an order

```java
message = new quickfix.fix42.OrderCancelReplaceRequest(
    new OrigClOrdID(prompt("OrigClOrdID")),
    new ClOrdID(prompt("ClOrdId")),
    new HandlInst(promptChar("HandleInst")),
    new Symbol(prompt("Symbol")),
    new Side(promptChar("Side")),
    new TransactTime(LocalDateTime.now()),
    new OrdType(promptChar("Order type"))
);
```
