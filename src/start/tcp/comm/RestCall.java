package start.tcp.comm;

import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class RestCall {
    private final String API_ENDPOINT = "http://localhost:8080/broadcast-g/";
    private final HttpResponse.BodyHandler<String> asString = HttpResponse.BodyHandlers.ofString();
    private final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .proxy(ProxySelector.getDefault()).build();

    public RestCall() {

    }

    public void call() throws Exception {
        System.out.println("------------------start HTTP czll-----");
        var HTTP_REQUEST = HttpRequest.newBuilder()
                .uri(URI.create( //Set the appropriate endpoint
                        new StringBuilder(API_ENDPOINT)
                                .append("HTTP-JAVA-11")

                                .toString()))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .build();
        var HTTP_RESPONSE = HTTP_CLIENT.send(HTTP_REQUEST, asString);
        var statusCode = HTTP_RESPONSE.statusCode();
        if (statusCode == 200 || statusCode == 201)
            System.out.println("Success! -- Java 11 REST API Call\n" );
        else
            System.out.println("Failure! -- Java 11 REST API Call");
    }

    public static void main(String[] args) {
        try {
            new RestCall().call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
