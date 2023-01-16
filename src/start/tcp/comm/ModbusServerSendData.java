package start.tcp.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Date;
import java.util.Random;

public class ModbusServerSendData {

    public static void main(String[] args) throws IOException, InterruptedException {

        ServerSocket serverSocket = null;
        try {
            int port = Integer.parseInt(args[0]);
            System.out.println("Sarver start listening on " + port + "..............");


            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }
        Socket clientSocket = null;
        while (true) {

            try {
                System.out.println(new Date());
                System.out.println("Waiting for request.");
                clientSocket = serverSocket.accept();
                System.out.print("Request accepted at ");
                System.out.println(new Date());
                if (clientSocket != null) {
                    System.out.println("Accept request.");
                }
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            System.out.println("Aquiring OutputStream......");
            OutputStream out = clientSocket.getOutputStream();
           // System.out.println("Server sending data......");
            byte[] request = {0x00, 0x01, 0x00, 0x00, 0x00, 0x06, 0x02, 0x03, 0x4E, (byte) 0xA0, 0x00, 0x40};
            byte[] requestMB = {0x02, 0x03, 0x4E, (byte) 0xA0, 0x00, 0x40, 0x53, 0x03};
            //out.write(request);
            //out.flush();
           // System.out.println("End sending......");
            HttpClient client = HttpClient.newHttpClient();

            System.out.println("Aquiring InputStream......");
            InputStream is = clientSocket.getInputStream();

            byte[] buffer = new byte[1024];
            int read;
            System.out.println("trying reading data");


           while ((read = is.available()) != 0) {
                System.out.println("available data:"+read);
                read = is.read(buffer);
                System.out.println("Reading No of Byte: " + read);
                System.out.println(" Byte Data: " + read);
                if(read>100) {
                    try {
                        HttpRequest request2 = HttpRequest.newBuilder()
                                .uri(URI.create("http://192.168.88.124:8080/api/receive"))
                                .timeout(Duration.ofMinutes(1))
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofByteArray(buffer))
                                .build();
                        client.sendAsync(request2, HttpResponse.BodyHandlers.ofString())
                                .thenApply(HttpResponse::body)
                                .thenAccept(System.out::println)
                                .join();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                Random random=new Random();
                int min=10;
                int max=30;
                int sleepTime=random.nextInt(max-min)+min;
                System.out.printf("Wait for %ds%n",sleepTime);
                Thread.sleep(1000 * sleepTime);
                System.out.println("Server sending data......");
                out.write(requestMB);
                out.flush();
                System.out.println(new Date());
            }
            System.out.println("Closing connections.......");
            out.close();
            is.close();
            clientSocket.close();
            //serverSocket.close();
        }

    }
}
