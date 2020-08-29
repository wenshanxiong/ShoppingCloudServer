package com.shoppingcloud.app;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *  Demo Socket server
 */
public class App {

    // Functions
    /**
     * Starts server and listens on port
     * Utilizing server socket instead of websocket. Unsure regarding websocket scalability.
     * But websocket might be the go to when developing real websocket. Benefits being
     * ability to broad cast to all clients (e.g. one person making an update) and scalability (horizontal).
     */
    public static void startServer() throws UnknownHostException {
        // Declarations

        // Constants
        final int port = 6969;
        final String charSet = "UTF-8";
        // Variables
        boolean clientTerminated = false;

        // Try listening to specified port on local host
        // Multithread capability can be added by extending "Thread"
        try (ServerSocket server = new ServerSocket(port, 50)) {
            // If valid client, connect
            Socket socketCnx = server.accept();

            // Handle I/O streams
            InputStream input = socketCnx.getInputStream();
            OutputStream output = socketCnx.getOutputStream();

            // Translate streams (should use buffered stream instead of scanner)
            Scanner textInput = new Scanner(input, charSet);
            OutputStreamWriter serverBroadcast = new OutputStreamWriter(output, charSet);
            PrintWriter broadcast = new PrintWriter(serverBroadcast, true);

            broadcast.println("Will print sent text, send 'exit' to terminate");

            // Scanner loop that ends
            while (!clientTerminated &&  textInput.hasNextLine()) {
                String line = textInput.nextLine().toLowerCase().trim();
                if (line.equals("exit")) {
                    clientTerminated = true;
                    broadcast.println("Terminated");
                }
            }
            broadcast.close();
            textInput.close();
            socketCnx.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main
     * @param args big useless
     */
    public static void main(String[] args) {
        System.out.println( "Test Socket" );
        while (true) {
            try {
                startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
