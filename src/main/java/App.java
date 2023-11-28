import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import Encrypt.BlockEncryption;

public class App {
    private static final String HTML_FILE_PATH = "html/index.html"; 

    public static void main(final String[] args) {
        Undertow server = Undertow.builder().addHttpListener(8080, "localhost")
                .setHandler(new HttpHandler() {


                    @Override
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        

                        //PARAMETERS FOR THE ALGORITHM
                        String key = exchange.getQueryParameters().get("key") != null ? exchange.getQueryParameters().get("key").getFirst() : null;
                        String mode = exchange.getQueryParameters().get("mode") != null ? exchange.getQueryParameters().get("mode").getFirst() : null;
                        String text = exchange.getQueryParameters().get("content") != null ? exchange.getQueryParameters().get("content").getFirst() : null;

                        //Validation 
                        if (key != null && mode != null && text != null) {
                            

                            
                            String responseMessage = "wajaka forever";
                            BlockEncryption en = new BlockEncryption(key, 10);
                            

                            //Call the algoritm 
                            if(mode.equals("en")){
                                responseMessage = en.Encrypt(text);
                            }
                            else if(mode.equals("de"))
                            {
                                responseMessage = en.Decrypt(text);
                            }   

                        
                            //Request send
                            exchange.getResponseSender().send(responseMessage);


                        } else {
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
