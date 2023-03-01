package example.quickfix.client;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
import quickfix.field.ApplVerID;
import quickfix.field.Password;
import quickfix.field.Username;

public class MyApplication implements Application {

    private static void printMsg(Message message) {
        System.out.println("Message: " + message.toString());
        // for (int field : message.getFieldOrder()) {
        for (int field = 0; field < 1e5; field++) {
            try {
                String value = message.getString(field);
                System.out.printf("  Field: %d = %s\n", field, value);
            } catch (FieldNotFound e) {
                // System.err.println("FIELD NOT FOUND");
                // e.printStackTrace();
            }
        }
        System.out.println("-----------------");
    }

    private static void printSession(SessionID sessionID) {
        System.out.println("Session");
        System.out.printf("  Beginning string: %s\n", sessionID.getBeginString());
        System.out.printf("  Sender comp id: %s\n", sessionID.getSenderCompID());
        System.out.printf("  Sender location id: %s\n", sessionID.getSenderLocationID());
        System.out.printf("  Sender sub id: %s\n", sessionID.getSenderSubID());
        System.out.printf("  Session qualifier: %s\n", sessionID.getSessionQualifier());
        System.out.printf("  Target comp id: %s\n", sessionID.getTargetCompID());
        System.out.printf("  Target location id: %s\n", sessionID.getTargetLocationID());
        System.out.printf("  Target sub id: %s\n", sessionID.getTargetSubID());
        System.out.printf("  Is fix: %b\n", sessionID.isFIXT());
        System.out.println("-----------------");
    }

    private static ArrayList<Message> messageBuffer = new ArrayList<>();
    private static ArrayList<SessionID> sessionBuffer = new ArrayList<>();

    @Override
    public void fromAdmin(Message message, SessionID sessionId)
    throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        // TODO Auto-generated method stub
        // System.out.println("New message from admin");
        // printSession(sessionId);
        messageBuffer.add(message);
    }

    private SessionID sessionId;

    @Override
    public void onCreate(SessionID sessionId) {
        // TODO Auto-generated method stub
        // System.out.println("A new sessionId was created");
        // printSession(sessionId);
        sessionBuffer.add(sessionId);
        this.sessionId = sessionId;
    }

    @Override
    public void onLogon(SessionID sessionId) {
        // TODO Auto-generated method stub
        System.out.println("Client is logged on");

        sessionBuffer.add(sessionId);
    }

    @Override
    public void onLogout(SessionID sessionId) {
        // TODO Auto-generated method stub
        System.out.println("Client logged out");
        sessionBuffer.add(sessionId);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        // TODO Auto-generated method stub
        // System.out.println("Message to admin");

        // Session.lookupSession(sessionId).setTargetDefaultApplicationVersionID(new ApplVerID("9"));

        // printSession(sessionId);
        messageBuffer.add(message);
    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        // TODO Auto-generated method stub
        // System.out.println("Message to app");
        // printSession(sessionId);
        messageBuffer.add(message);
    }

    @Override
    public void fromApp(Message message, SessionID sessionId)
    throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        // TODO Auto-generated method stub
        // System.out.println("Message from app");
        // printSession(sessionId);
        messageBuffer.add(message);
    }

    public void sendMessage(Message message) {
        try {
            System.out.println("Sending message: " + message.toString());
            Session.sendToTarget(message, sessionId);
        } catch (SessionNotFound e) {
            e.printStackTrace();
        }
    }

    public void printMessages() {
        for (Message message : messageBuffer) {
            printMsg(message);
        }
        messageBuffer.clear();
        for (SessionID session : sessionBuffer) {
            printSession(session);
        }
        sessionBuffer.clear();
    }
}
