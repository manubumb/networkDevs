import java.io.*;
import java.net.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

public class networkDevs {

    /*
        Helper function to write a message to the event log
    */
    static void log(String msg) {
        try {
            FileWriter fw = new FileWriter("networkDevs.log", true);
            fw.write(msg);
            fw.close();
        } catch (IOException e) {
            System.out.println("Error opening networkDevs.log");
        }
    }

	public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException, AddressException, MessagingException {
        int i = 0;
        String host = "192.168.0.";
        InetAddress temp;

        String mailer = "smtpsend";
        String mailhost = "smtp.gmail.com";
        // REPLACE username and password
        String username = "";
        String password = "";
        Properties props = System.getProperties();
  
        props.setProperty("mail.smtp.host", mailhost);
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");
        // set any other needed mail.smtp.* properties here
        Session session = Session.getInstance(props);

        // Check to make sure there's 1 arg (for now - in the future, I'll support a second arg for texting)
        if (args.length != 1) {
            System.out.println("Error: Must include final byte of starting address in command line");
        } else {
            // got 1 arg!
            try {
                // convert args[0] to an int for the for loop
                i = Integer.parseInt(args[0]);
                // make sure the supplied starting address makes sense
                if ((i < 1) || (i > 255)) {
                    System.out.println("Error: args[0] must be a number between 1 and 255");
                } else {
                    // got a valid starting address, now loop through all addresses forever!
                    while (true) {
                        for (int j = i; j < 256; j++){
                            // get an InetAddress so we can call isReachable() with it
                            temp = InetAddress.getByName(host+j);
                            try {
                                if (temp.isReachable(500)){
                                    // got a device!  Display output and log it!
                                    System.out.println((new Date()) +" "+ host+j + " detected");
                                    log((new Date()) + "\t" + host+j + " detected\r\n");

                                    // BEEP! BEEP! BEEP!
                                    for (int k = 0; k < 3; k++) {
                                        java.awt.Toolkit.getDefaultToolkit().beep();
                                        Thread.sleep(800);
                                    }

                                    try{
                                        MimeMessage msg = new MimeMessage(session);
                                        // set the message content here
                                        // msg.setFrom(new InternetAddress("@gmail.com"));
                                        // REPLACE recipient
                                        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("@gmail.com", false));
                                        msg.setSubject("Device detected: " + host+j);
                                        msg.setText((new Date()) +" "+host+j + " detected.");
                                        msg.setHeader("X-Mailer", mailer);
                                        msg.setSentDate(new Date());
                            
                                        Transport.send(msg, username, password);
                                    }
                                    catch (MessagingException m) {
                                        m.printStackTrace();
                                    }                            
                                }// else System.out.println(host+j + " is not reachable");
                            } catch (SocketException e) {
                                // One night, my internet stopped working and a SocketException was thrown and the program exited,
                                // so catch the exception here and log it, then continue, that way the program doesn't just exit if
                                // something happens to your network
                                System.out.println((new Date()) + " Error: could not reach " + host+j + ". Check network connection");
                                log ((new Date()) + " Error: could not reach " + host+j + ". Check network connection");
                            }
                        } 
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: args[0] must be a number between 1 and 255");
            }
        }
    }
}
