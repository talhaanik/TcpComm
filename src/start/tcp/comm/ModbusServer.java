package start.tcp.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ModbusServer {

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
            System.out.println("Server sending data......");
            byte[] request = {0x00, 0x01, 0x00, 0x00, 0x00, 0x06, 0x02, 0x03, 0x4E, (byte) 0xA0, 0x00, 0x40};
            byte[] requestMB = {0x02, 0x03, 0x4E, (byte) 0xA0, 0x00, 0x40, 0x53, 0x03};
            //out.write(request);
            //out.flush();
            System.out.println("End sending......");


            System.out.println("Aquiring InputStream......");
            InputStream is = clientSocket.getInputStream();

            byte[] buffer = new byte[1024];
            int read;
            System.out.println("trying reading data");

           // while ((read = is.read(buffer)) != -1) {
            while ((read = is.available()) != 0) {
                System.out.println("available data:"+read);
                read = is.read(buffer);
                System.out.println("Reading No of Byte: " + read);
                System.out.println(" Byte Data: " + read);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < read; i++) {
                    sb.append(String.format("%02X ", buffer[i]));
                }
                System.out.println(sb.toString());
                int j = 0;

                for (int i = 9; i < read; i = i + 4) {
                    int val = (
                            (0xff & buffer[i]) << 24 |
                                    (0xff & buffer[i + 1]) << 16 |
                                    (0xff & buffer[i + 2]) << 8 |
                                    (0xff & buffer[i + 3]) << 0
                    );
                    System.out.println((20128 + j) + "-" + (20129 + j) + "--------" + val);

                    j = j + 2;
                }

                Thread.sleep(1000 * 30);
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
