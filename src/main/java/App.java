import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import Encrypt.ParallelBlockEncryption;

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
                        String metod = exchange.getQueryParameters().get("metod") != null ? exchange.getQueryParameters().get("metod").getFirst() : null;                        
                        
                        
                        
                        //Validation 
                        if (key != null && mode != null && metod != null) {
                            
                            exchange.startBlocking();
                            String data = decoder(exchange.getRequestHeaders().getFirst("data"));
                            String responseMessage = "wajaka forever";        
                            String info = "";
                            String executionTime = "";
                            
                            if(metod.equals("p")){  

                                //System.err.println("Parallel algorithm");
                                ParallelBlockEncryption en = new ParallelBlockEncryption(key, 10, 4);  
                                //Call the algoritm 

                                
                                if(mode.equals("en")){
                                    info = en.Encrypt(data);
                                }
                                else if(mode.equals("de"))
                                {
                                    info = en.Decrypt(data);
                                }   

                                executionTime = en.executionTime_;


                            }else if(metod.equals("s")){

                                
                                //Only a single thread will execute the full algorithm 
                                ParallelBlockEncryption en2 = new ParallelBlockEncryption(key, 10, 1);  
                                
                                
                                if(mode.equals("en")){
                                    info = en2.Encrypt(data);
                                }
                                else if(mode.equals("de"))
                                {
                                    info = en2.Decrypt(data);
                                }
                                executionTime = en2.executionTime_;
                            }         

                            responseMessage = "{\"data\":" + "\"" + info + "\"" + ", \"time\":" + "\"" +  executionTime + "\"" + "}";


                            //Request send
                            exchange.getResponseSender().send(responseMessage);


                        } else {
                            String htmlContent = readHtmlFile();
                            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
                            exchange.getResponseSender().send(htmlContent);
                        }
                    }


                }).setServerOption(UndertowOptions.MAX_HEADER_SIZE, 900_000).build();


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

    private static String decoder(String toDecode) {
    

        try {
            String decodedValue = URLDecoder.decode(toDecode, StandardCharsets.UTF_8.toString());
            return decodedValue;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
