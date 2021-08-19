package serverTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientSocketComms implements Runnable {

    private final String server;
    private final int port;

    public ClientSocketComms(String server, int port) {
        this.server = server;
        this.port = port;
    }

    @Override
    public void run() {
        // Open socket to server and wait for incoming data
        try {
            Socket socket = new Socket(server, port);
            BufferedReader dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Continually loop over incoming data until stopped
            String data;
            while((data = dataIn.readLine()) != null) {
                // Should print out every line as it's received,
                // but instead waits until buffer is full
                // (outputs about 170 lines at a time)
                System.out.println(data);
                //System.out.println("-------- client");
            }

            // Close socket and thread will die
            // (but loop never ends because buffer doesn't get completely refilled)
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}