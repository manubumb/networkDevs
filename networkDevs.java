import java.io.*;
import java.net.*;
import java.util.Date;

public class networkDevs {
	public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException {
        int i = 0;
        String host = "192.168.0.";
        InetAddress temp;
        if (args.length != 1) {
            System.out.println("Error: Must include final byte of starting address in command line");
        } else {
            try {
                // convert args[0] to an int for the for loop
                i = Integer.parseInt(args[0]);
                if ((i < 1) || (i > 255)) {
                    System.out.println("Error: args[0] must be a number between 1 and 255");
                } else {
                    while (true) {
                        for (int j = i; j < 256; j++){
                            temp = InetAddress.getByName(host+j);
                            if (temp.isReachable(1000)){
                                System.out.println(host+j + /*" (" + temp.getCanonicalHostName() +*/ " is reachable");
                                for (int k = 0; k < 5; k++) {
                                    java.awt.Toolkit.getDefaultToolkit().beep();
                                    Thread.sleep(800);
                                }
                            } //else System.out.println(host+i + " is not reachable");
                        } 
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: args[0] must be a number between 1 and 255");
            }
        }
    }
}