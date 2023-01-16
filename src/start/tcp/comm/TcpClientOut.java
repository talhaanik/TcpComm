package start.tcp.comm;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class TcpClientOut {

    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;
        // PrintWriter out = null;
        OutputStream out = null;
        InputStream in = null;

        try {
            // echoSocket = new Socket("43.243.204.142", 2233);
            echoSocket = new Socket("127.0.0.1", 1234);
          //  echoSocket = new Socket("192.168.77.2", 502);
            // out = new PrintWriter(echoSocket.getOutputStream(), true);
            out = echoSocket.getOutputStream();
           in = echoSocket.getInputStream();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: taranis.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: taranis.");
            System.exit(1);
        }
        //http://localhost:8080/products

        BufferedReader stdIn = new BufferedReader(
                new InputStreamReader(System.in));
        String userInput;
        byte[] buffer = new byte[102];
        int read;
        while (!"e".equals(userInput = stdIn.readLine())) {
            //while (true) {
            // out.println(userInput);
            // out.print(123456);
            // out.flush();
            // byte b=16;
            //out.print(b);
            // out.flush();


            byte [] data={-91, 32, 0 ,115 ,1 ,0, 1, 0, 56, 54, 57, 52, 57, 50 ,48 ,53 ,50, 49 ,54, 50, 56, 57, 55,
                    0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0, 76, -4, -91};
            byte [] relayD0={1,1,0,0,0,2, (byte) 189, (byte) 203};
            byte [] readHoldingRgs={1,3,78, (byte) 160,0,64,83,48};
            byte [] readHoldingCoil={0x01,0x03,0x4E, 0x20,0x0,0x0A, (byte) 0xD3,0x2F};
           // out.write(readHoldingCoil);
           // out.write(new String("\r\n").getBytes());
          //  out.flush();
            // System.out.println("echo: " + in.readLine());
            System.out.println("Client Received data........");
            while ((read = in.available()) != 0) {
            //while((read = in.read(buffer))!=-1) {
                in.read(buffer);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < read; i++) {
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
            System.out.println("Client send data........");
            out.write(readHoldingCoil);
            // out.write(new String("\r\n").getBytes());
            out.flush();
            System.out.println("Start reading from Keyboard");
        }

        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }
}
