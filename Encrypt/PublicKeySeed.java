package Encrypt;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class PublicKeySeed{


    public static final int MAXINT = 2147483640;


    /**
    * Function that clculates the number of non isomorphic graphs 
    *
    * @param vertex number of vertex to enerate the graph
    * @return the number of posible graphs wihht number of vetuices = vertex
    */
    public static int generateSeed(int vertex) {
        return countNonIsomorphicGraphs(vertex);
    }



    /**
    * Function which based on the output of the program counts the graphs
    *
    * @param numVetices number of vertices of the graph
    * @return the number of posible graphs wihht number of vetuices = vertex
    */
    private static int countNonIsomorphicGraphs(int numVertices) {

        if(numVertices == 0)  return 1;
        // Use Nauty to find canonical labels
        String command = "geng " + numVertices;
        int graph6Strings = executeCommand(command);


        return graph6Strings;
    }





    /**
    * Function to call the api
    *
    * @param command string commad for terminal and call the api
    * @return The number of graphs
    */
    private static int executeCommand(String command) {
        
        int counter = 0;

        try {

            //Variables for reading the buffer
            Process process = new ProcessBuilder("bash", "-c", command).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            //Read the line
            while (reader.readLine() != null) {
                counter =  (counter+1)%MAXINT;
            }

            //In order to catch exeptions
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Command execution failed with exit code: " + exitCode);
            }
            return counter;



        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }
}