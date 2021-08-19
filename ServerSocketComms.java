package serverTest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerSocketComms implements Runnable {

    private final BlockingQueue<String> outqueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> inqueue = new LinkedBlockingQueue<>();
    private final int port;

    public ServerSocketComms(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        // Open server socket and wait for connection
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();

            // Continually loop over blocking data queue until stopped
            BufferedWriter dataOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while(socket.isConnected()) {
                String data = outqueue.take();
                System.out.println("Request: --------> "+data);
                inqueue.put("--------> "+data+" i add some thing");
                //dataOut.write(data);
                //dataOut.write("48 characters should fill the 8K buffer in approximately: ");
                //dataOut.newLine(); // delimit strings with a line separator
                //dataOut.flush();
            }

            // Loop never exits because client socket never completes because of BufferedReader issue
            // so sockets never close and application never terminates
            socket.close();
            serverSocket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public BlockingQueue<String> getQueue() {
        // Return a reference to the sending queue to be populated by other threads
        return this.outqueue;
    }

    public BlockingQueue<String> getInQueue() {
        // Return a reference to the sending queue to be populated by other threads
        return this.inqueue;
    }
}