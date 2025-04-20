package segmentedfilesystem;

import java.net.UnknownHostException;

public class Main {
    
    // If there's one command line argument, it is assumed to
    // be the server. If there are two, the second is assumed
    // to be the port to use.
    public static void main(String[] args) {
        String server = "localhost";
        // CHANGE THIS DEFAULT PORT TO THE PORT NUMBER PROVIDED
        // BY THE INSTRUCTOR.
        int port = 6014;
        
        if (args.length >= 1) {
            server = args[0];
        }
        if (args.length >= 2) {
            port = Integer.parseInt(args[1]);
        }

        FileRetriever fileRetriever;
        try {
            fileRetriever = new FileRetriever(server, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();;
            return;
        }

        try {
            fileRetriever.downloadFiles(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
