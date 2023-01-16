package start.tcp.comm;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class ModbusTcpClient {

    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;

        OutputStream out = null;
        InputStream in = null;

        try {
           // echoSocket = new Socket("192.168.77.2", 502);
            echoSocket = new Socket("127.0.0.1", 1234);
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
        System.out.println("sending request.........");
        byte[] request = {0x00, 0x01, 0x00, 0x00, 0x00, 0x06, 0x02, 0x03, 0x4E, (byte) 0xA0, 0x00, 0x40};
        out.write(request);
        out.flush();

        System.out.println("Waiting for response.........");
        byte[] buffer = new byte[1024];
        int read;

        while ((read = in.read(buffer)) != -1) {
            System.out.println("Reading No of Byte: " + read);
            System.out.println(" Byte Data: " + read);
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


            System.out.println(new Date());
        }

        out.close();
        in.close();

        echoSocket.close();
    }
}
