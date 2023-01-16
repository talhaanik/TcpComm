package start.tcp.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class MainCmd {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        try {
            int port =Integer.parseInt(args[0]);
            System.out.println("Sarver start listening on "+port+"..............");


            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }
        Socket clientSocket = null;
        while(true)
        {

            try {
                System.out.println(new Date());
                System.out.println("Waiting for request.");
                clientSocket = serverSocket.accept();
                System.out.print("Request accepted at ");
                System.out.println(new Date());
                if(clientSocket!=null){
                    System.out.println("Accept request.");
                }
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            System.out.println("Aquiring OutputStream......");
            OutputStream out = clientSocket.getOutputStream();
            System.out.println("Server sending data......");
            byte [] readHoldingRgs={1,3,78, (byte) 160,0,64,83,48};
            byte [] readHoldingCoil={0x01,0x03,0x4E, 0x20,0x0,0x0A, (byte) 0xD3,0x2F};
            out.write(readHoldingRgs);
            out.flush();
            System.out.println("End sending......");


            System.out.println("Aquiring InputStream......");
            InputStream is=clientSocket.getInputStream();

            byte[] buffer = new byte[1024];
            int read;
            System.out.println("trying reading data");
            while ( (read = is.read(buffer)) != -1) {
                StringBuilder sb = new StringBuilder();
                System.out.println("Reading No of Byte: "+read);
                System.out.println(" Byte Data: "+read);
                for (int i=0;i<read;i++) {
                    System.out.print(buffer[i]);
                    System.out.print(" ");
                    sb.append(String.format("%02X ", buffer[i]));
                }
                System.out.println("");
                System.out.println(sb.toString());
                System.out.println("-----------------");
                String output = new String(buffer, 0, read);
                System.out.print(output);
                System.out.flush();
                System.out.println(new Date());
            }
            out.close();
            is.close();
            clientSocket.close();
            //serverSocket.close();
        }

    }
}
