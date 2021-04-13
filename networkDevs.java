import java.io.*;
import java.net.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

public class networkDevs {

    /*
     * Helper function to write a message to the event log
     */
    static void log(String msg) {
        try {
            FileWriter fw = new FileWriter("networkDevs.log", true);
            fw.write((new Date()).toString());
            fw.write("\t"+msg);
            fw.close();
        } catch (IOException e) {
            System.out.println("Error opening networkDevs.log");
        }
    }

    // Helper function to ping an IPv4 address
    // turns out I didn't need this, but keeping the code in here for future reference
    static void ping(String ip) {
        try {
            Process p = Runtime.getRuntime().exec("ping "+ip);
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String s = "";
            while ((s = inputStream.readLine()) != null) {
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException {
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

        Session session = Session.getInstance(props);

        // Check to make sure there's 2 args - in the future, I'll add support for prompting the user
        if (args.length != 2) {
            System.out.println("Error: Must include final byte of starting address and recipient\r\nemail address in command line");
            return;
        }
        // got 2 args! first should be last byte of starting address, second should be
        // an email address

        // convert args[0] to an int for the for loop
        try {
            i = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            i = 1;
            System.out.println("args[0] must be an integer between 1 and 255. Starting search at " + host + i);
            log("args[0] must be an integer between 1 and 255. Starting search at " + host+i + "\r\n");
        }

        // make sure the supplied starting address makes sense
        if ((i < 1) || (i > 255)) {
            i = 1;
            System.out.println("args[0] must be an integer between 1 and 255. Starting search at " + host + i);
            log("args[0] must be an integer between 1 and 255. Starting search at " + host+i + "\r\n");
        }

        // got a valid starting address, now loop through all addresses forever!
        while (true) {
            for (int j = i; j < 256; j++) {
                // get an InetAddress so we can call isReachable() with it
                temp = InetAddress.getByName(host+j);
                try {
                    //ping(host+j);
                    if (temp.isReachable(500)) {
                        // got a device! Display output and log it!
                        System.out.println((new Date()) + " " + host + j + " detected");
                        log(host+j + " detected\r\n");

                        // BEEP! BEEP! BEEP!
                        for (int k = 0; k < 3; k++) {
                            java.awt.Toolkit.getDefaultToolkit().beep();
                            Thread.sleep(800);
                        }

                        // send the email
                        try {
                            MimeMessage msg = new MimeMessage(session);
                            // set the message content here
                            // msg.setFrom(new InternetAddress("@gmail.com"));
                            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(args[1], true));
                            // msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("@txt.att.net", false));
                            msg.setSubject("Device detected: " + host + j);
                            msg.setText((new Date()) + " " + host + j + " detected.");
                            msg.setHeader("X-Mailer", mailer);
                            msg.setSentDate(new Date());

                            Transport.send(msg, username, password);
                        } catch (AddressException ae) {
                            System.out.println("Error: args[1] must be a series of comma-separated valid email addresses");
                            log("Error: args[1] must be a series of comma-separated valid email addresses\r\n");
                        } catch (SendFailedException se) {
                            System.out.println("Message not sent: " + se.toString());
                            log("Message not sent: " + se.toString());
                        } catch (MessagingException me) {
                            me.printStackTrace();
                        }
                    } // else System.out.println(host+j + " is not reachable");
                } catch (SocketException se) {
                    // One night, my internet stopped working and a SocketException was thrown and
                    // the program exited, so catch the exception here and log it, then continue, that way the program
                    // doesn't just exit if something happens to your network
                    System.out.println((new Date()) + " Error: could not reach " + host+j + ". Check network connection");
                    log("Error: could not reach " + host+j + ". Check network connection\r\n");
                }
            }
        }
    }
}
