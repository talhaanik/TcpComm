package start.tcp.comm;
import java.net.*;
import java.io.*;
import java.util.Date;
public class Main {

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

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            /*BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));*/
            System.out.println("Aquiring InputStream......");
            InputStream is=clientSocket.getInputStream();
         // byte [] nbyte=  is.readAllBytes();
         //   String inputLine, outputLine;
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
                System.out.println(output);
                System.out.flush();
                System.out.println(new Date());
                out.println("hello from server");
            }
            out.close();
            is.close();
            clientSocket.close();
            //serverSocket.close();
        }

    }
}
