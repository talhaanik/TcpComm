package start.tcp.comm;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.stream.IntStream;

public class SendByteOnHttp {
    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/broadcast-g/Another"))
                .build();
        System.out.println("Start1--"+System.currentTimeMillis());
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
        System.out.println("Start2--"+System.currentTimeMillis());
        byte[] requestMB = {0x02, 0x03, 0x4E, (byte) 0xA0, 0x00, 0x40, 0x53, 0x03};

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/broadcast-byte"))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestMB))
                .build();
        IntStream.range(1,6).forEach(i->{
            System.out.println(i+"-Start--"+System.currentTimeMillis());
        client.sendAsync(request2, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
            System.out.println(i+"-End---"+System.currentTimeMillis());
        }  );
    }
}
