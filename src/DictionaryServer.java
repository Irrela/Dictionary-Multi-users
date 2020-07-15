import org.json.simple.JSONObject;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.io.*;
import org.json.simple.parser.*;

/**
 * @author Ruocheng Ning
 * @studentId 1106219
 */

public class DictionaryServer {
    private ServerSocket serverSocket;
    private Map dictionary;

    public static void main(String[] args) {
        try {
            //Load dictionary data from a json file, transport it into a Map structure.
            Object obj = new JSONParser().parse(new FileReader(args[1]));

            //Initialize server(server socket port and dictionary map)
            DictionaryServer server = new DictionaryServer(Integer.parseInt(args[0]), (JSONObject) obj);

//            Object obj = new JSONParser().parse(new FileReader("dict.json"));
//            DictionaryServer server = new DictionaryServer(1234, (JSONObject) obj);

            Socket clientSocket = null;

            //Keep listening and assign thread to new client socket
            while (true) {
                System.out.println("\nListening to port: " + Integer.parseInt(args[0]));

                clientSocket = server.getServerSocket().accept();
                Thread thread = new Thread(new ServiceThread(server.getDictionary(), clientSocket));
                thread.start();
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("Server fails to launch, can't find initial data file.");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println("Server fails to launch, problems of parsing json file to JSONObject.");
        }
    }

    public DictionaryServer(int serverPort, JSONObject obj) throws IOException{
        this.serverSocket = new ServerSocket(serverPort);
        this.dictionary = (Map)obj;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Map getDictionary() {
        return dictionary;
    }

}
