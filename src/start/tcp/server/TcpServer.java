package start.tcp.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TcpServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            int port = Integer.parseInt(args[0]);
            System.out.println("Sarver start listening on " + port + "..............");


            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port:.");
            System.exit(1);
        }
        Socket clientSocket = null;

        while (true){
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
            System.out.println("Aquiring InputStream......");
            InputStream is = clientSocket.getInputStream();
            byte[] requestMB = {0x02, 0x03, 0x4E, (byte) 0xA0, 0x00, 0x40, 0x53, 0x03};
            byte[] buffer = new byte[1024];
            int read;
            System.out.println("trying reading data");
            while ((read = is.read(buffer)) != -1) {
                System.out.println("available data:"+read);
                read = is.read(buffer);
                System.out.println("Reading No of Byte: " + read);
                System.out.println(" Byte Data: " + read);
                System.out.println("Server sending data......");
                out.write(requestMB);
                out.flush();
                System.out.println(new Date());
            }
            System.out.println("Closing connections.......");
            out.close();
            is.close();
            clientSocket.close();
        }
    }
}
