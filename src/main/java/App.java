import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class App {
    private static final String HTML_FILE_PATH = "html/prueba.html"; 

    public static void main(final String[] args) {
        Undertow server = Undertow.builder().addHttpListener(8080, "localhost")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        String message = exchange.getQueryParameters().get("mensaje") != null
                                ? exchange.getQueryParameters().get("mensaje").getFirst()
                                : null;

                        if (message != null) {
                            // Process the message and send a response
                            String responseMessage = "Mensaje recibido desde el cliente: " + message;
                            exchange.getResponseSender().send(responseMessage);
                        } else {
                            // Serve the HTML page
                            String htmlContent = readHtmlFile();
                            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
                            exchange.getResponseSender().send(htmlContent);
                        }
                    }
                }).build();
        server.start();
    }

    private static String readHtmlFile() {
        try (InputStream inputStream = App.class.getClassLoader().getResourceAsStream(HTML_FILE_PATH)) {
            if (inputStream != null) {
                try (Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A")) {
                    return scanner.hasNext() ? scanner.next() : "";
                }
            } else {
                return "HTML file not found";
            }
        } catch (IOException e) {
            // Handle the IOException or log the exception
            e.printStackTrace();
            return "Error leyendo archivo HTML principal: debe estar en src\\resources\\html";
        }
    }
}
