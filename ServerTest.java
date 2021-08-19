package serverTest;

import java.util.concurrent.BlockingQueue;

public class ServerTest {

    public static void main(String[] args) {

        int port = 54321;
        ServerSocketComms server = new ServerSocketComms(port);
        BlockingQueue<String> queue1 = server.getQueue();
        BlockingQueue<String> queue2 = server.getInQueue();
        new Thread(server).start();

        ClientSocketComms client = new ClientSocketComms("localhost", port);
        new Thread(client).start();

        for(int i = 0; i < 1024; i++) { // should give about 10 seconds of output
            try {
                queue1.put("48 characters should fill the 8K buffer in approximately:  "+i);
                Thread.sleep(10);
                System.out.println("Response: "+queue2.take());
                // 48 characters should fill the 8K buffer in approximately 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}