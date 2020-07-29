import org.json.simple.JSONArray;
import java.io.*;
import java.net.Socket;
import java.util.Map;

/**
 * @author Ruocheng Ning
 * @studentId 1106219
 */
public class ServiceThread implements Runnable{

    private Map threadDict;
    private Socket clientSocket;
    private String clientMsg = "";

    @Override
    public void run() {

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(this.getClientSocket().getInputStream(), "UTF-8"));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(this.getClientSocket().getOutputStream(), "UTF-8"));

            System.out.println("A client has connected to Server, assign to thread \'" + Thread.currentThread().getName() + "\', start providing service.");
            System.out.println("Currently connected user number: " + (Thread.activeCount() - 1));

            //Handle client's requests until client side closes
            //ClientMsg will be parted into operation command and word inputs to be easily processed
            while((clientMsg = input.readLine()) != null){

                String[] operation = clientMsg.split("@split@");

                if(operation[0].equals("query")){
                    output.write(query(operation[1]) + "\n");
                    output.flush();
                }

                if(operation[0].equals("add")){
                    output.write(add(operation[1], operation[2]) + "\n");
                    output.flush();
                }

                if(operation[0].equals("remove")){
                    output.write(remove(operation[1])  + "\n");
                    output.flush();
                }
            }

            getClientSocket().close();
            System.out.println("Client on thread \'" + Thread.currentThread().getName() + "\' has disconnected.");

            //Update the .json file after a client disconnected.
            try {
                updateJsonFile();
                System.out.println("Server Dictionary has been updated.");
            } catch (IOException e){
                e.printStackTrace();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("Currently connected user number: " + (Thread.activeCount() - 2));
    }

    public ServiceThread(Map threadDict, Socket clientSocket) {
        this.threadDict = threadDict;
        this.clientSocket = clientSocket;
    }

    //Query requested word in current dictionary of server
    public String query(String word){

        String result = "";

        System.out.println("Client on thread \'" + Thread.currentThread().getName() + "\' QUERY: " + word);

        if(!getThreadDict().containsKey(word)){
            result = "###Not find word \'" + word + "\' in the dictionary.###";
        }else{
            String value = (String)getThreadDict().get(word);
            result = word + ":\n" + value.replace("\n\n", "\n");
        }
        return result + "\n";
    }

    //Add a new word into current dictionary of server.
    public static synchronized String add(String word, String meaning){
        String result = "";

        System.out.println("Client on thread \'" + Thread.currentThread().getName() + "\' ADD: " + word);

        if(getThreadDict().containsKey(word)){
            String value = (String)getThreadDict().get(word);
            result = "###Sorry, Word \'"+ word + "\' is already in the dictionary.###\n"+ word + ":\n" + value.replace("\n\n", "\n");
        }else{
            getThreadDict().put(word, meaning);
            String value = (String)getThreadDict().get(word);
            result = "###Word \'" + word + "\' has been added.###\n"+ word + ":\n" + value.replace("\n\n", "\n");
        }
        return result+"\n";
    }

    //Remove a word from current dictionary of server.
    public static synchronized String remove(String word){

        String result = "";

        System.out.println("Client on thread \'" + Thread.currentThread().getName() + "\' REMOVE: " + word);
        if(!getThreadDict().containsKey(word)){
            result = "###Word \'" + word + "\' is not in the dictionary, you can't remove it.###";
        }else{
            getThreadDict().remove(word);
            result = "###Word \'" + word + "\' has been deleted.###";
        }
        return result+"\n";
    }

    //Write current server dictionary data into 'dict.json' after client closes.
    public static synchronized void updateJsonFile() throws IOException {
        FileWriter file = new FileWriter("dict.json");
        JSONArray currentDict = new JSONArray();
        currentDict.add(getThreadDict());
        String str = currentDict.toJSONString();
        file.write(str.substring(1,str.length() - 1));
        file.flush();
    }


    public Map getThreadDict() {
        return threadDict;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
