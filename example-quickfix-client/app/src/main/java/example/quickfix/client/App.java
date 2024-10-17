/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package example.quickfix.client;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.Scanner;

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

public class App {

    private static Scanner keyboard = new Scanner(System.in);

    private static String prompt(String msg) {
        return prompt(msg, "string");
    }

    private static String prompt(String msg, String type) {
        System.out.printf("%s [%s] >>> ", msg, type);
        return keyboard.nextLine();
    }

    private static char promptChar(String msg) {
        String s = prompt(msg, "char");
        return s.charAt(0);
    }

    public static void main(String[] args) {
        // FooApplication is your class that implements the Application interface
        // https://www.quickfixj.org/usermanual/2.3.0/usage/application.html
        MyApplication application = new MyApplication();

        try {
            String configFilename = System.getProperty("user.dir") + "/config";
            System.out.println(configFilename);
            SessionSettings settings = new SessionSettings(new FileInputStream(configFilename));

            LogFactory logFactory = new FileLogFactory(settings);

            MessageStoreFactory messageFactory = new FileStoreFactory(settings);

            SessionFactory sessionFactory= new DefaultSessionFactory(application, messageFactory, logFactory);

            Initiator initiator = new SocketInitiator(sessionFactory, settings, 1000000);

            initiator.start();

            while(true) {
                String command = prompt("Command", "print, place, cancel, replace");

                Message message = new Message();

                try {
                    switch (command) {
                        case "print" -> {
                                application.printMessages();
                            }
                        case "place" -> {
                            message = new quickfix.fix42.NewOrderSingle(
                                new ClOrdID(prompt("ClOrdId")),
                                new HandlInst(promptChar("HandleInst")),
                                new Symbol(prompt("Symbol")),
                                new Side(promptChar("Side")),
                                new TransactTime(LocalDateTime.now()),
                                new OrdType(promptChar("Order type"))
                            );
                        }
                        case "cancel" -> {
                            message = new quickfix.fix42.OrderCancelRequest(
                                new OrigClOrdID(prompt("OrigClOrdID")),
                                new ClOrdID(prompt("ClOrdId")),
                                new Symbol(prompt("Symbol")),
                                new Side(promptChar("Side")),
                                new TransactTime(LocalDateTime.now())
                            );
                        }
                        case "replace" -> {
                            message = new quickfix.fix42.OrderCancelReplaceRequest(
                                new OrigClOrdID(prompt("OrigClOrdID")),
                                new ClOrdID(prompt("ClOrdId")),
                                new HandlInst(promptChar("HandleInst")),
                                new Symbol(prompt("Symbol")),
                                new Side(promptChar("Side")),
                                new TransactTime(LocalDateTime.now()),
                                new OrdType(promptChar("Order type"))
                            );
                        }
                        default -> {
                            System.out.println("Command not found");
                            continue;
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }

                application.sendMessage(message);
            }
        } catch (Exception e) {
            // System.out.println("Config error");
            e.printStackTrace();
        }
        System.out.println("Exit");
        System.exit(2);
    }
}
