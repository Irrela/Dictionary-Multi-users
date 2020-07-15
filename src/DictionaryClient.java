import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Ruocheng Ning
 * @studentId 1106219
 */

public class DictionaryClient {

    private JButton queryButton;
    private JButton addButton;
    private JButton removeButton;
    private JPanel Main;
    private JTextArea response;
    private JTextField word;
    private JTextArea instruction;

    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;
    private JFrame frame;

    public static void main(String[] args) {

        DictionaryClient client = new DictionaryClient();
        client.socketInit(args[0], Integer.parseInt(args[1]));
//        client.socketInit("localhost", 1234);
    }


    public DictionaryClient() {

        //Initialize the GUI frame
        frame = new JFrame("DictionaryClient");
        frame.setContentPane(this.Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


        //Get the valid query word in textfield, plus a 'query' operation indication and send to server
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!word.getText().equals("")) {
                    try {
                        String str = "query" + "@split@" + word.getText() + "\n";
                        output.write(str);
                        output.flush();

                        String line = null;
                        String re = "";
                        while((line = input.readLine()) != null && line.length() != 0){
                            re += line + "\n";
                        }

                        response.setText(re);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    response.setText("\nNo word in box, fill in a word you want to query.");
                }
            }
        });

        //Get the valid add word in textfield, plus a 'add' operation indication and send to server
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = word.getText();
                String send;

                try{
                    String word = text.split(":", 2)[0];
                    String meaning = text.split(":", 2)[1];

                    if(text.equals("") || word.equals("") || meaning.equals("")){
                        response.setText("Invalid add input, please follow the input form in instruction.");
                    }else{
                        send = "add" + "@split@" + word + "@split@" + meaning + "\n";
                        output.write(send);
                        output.flush();

                        String line = null;
                        String re = "";
                        while((line = input.readLine()) != null && line.length() != 0){
                            re += line + "\n";
                        }

                        response.setText(re);
                    }
                }catch (Exception ex){
                    response.setText("Invalid input, please follow the  \' word:meaning\' form when you add a word and it's meaning.");
                }
            }
        });

        //Get the valid remove word in textfield, plus a 'remove' operation indication and send to server
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!word.getText().equals("")) {
                    try {
                        String str = "remove" + "@split@" + word.getText() + "\n";
                        output.write(str);
                        output.flush();

                        String line = null;
                        String re = "";
                        while((line = input.readLine()) != null && line.length() != 0){
                            re += line + "\n";
                        }

                        response.setText(re);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    response.setText("\nNo word in box, fill in a word you want to remove.");
                }
            }
        });
    }

    //Initialize client socket, I/O buffer, shows instruction information on GUI when user launches client
    public void socketInit(String serverAddress, int serverPort){

        try{
            this.setSocket(new Socket(serverAddress, serverPort));
            this.setInput(new BufferedReader(new InputStreamReader(socket.getInputStream())));
            this.setOutput(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            instruction.setText("\n     *****  Welcome to Dict, you have connected to Server.  ***** \n\n? How to use Dict ?\n\n[Query a word]: Fill it in the following TEXT FIELD, and press \'Query\' button.\n\n[Add a word]: Fill the word and its meaning in a  \'word:meaning\' form , and press \'Add\' button.\n\n[Remove a word]: Fill it in the following TEXT FIELD, and press \'Remove\' button. ");

        } catch (UnknownHostException e) {
            e.printStackTrace();
            instruction.setText("\n     *****  Fail to connect to Server(UnknownHost).  ***** ");
        } catch (IOException e) {
            e.printStackTrace();
            instruction.setText("\n     *****  Fail to connect to Server(IO error).  ***** ");
        }
    }

    public BufferedReader getInput() {
        return input;
    }

    public void setInput(BufferedReader input) {
        this.input = input;
    }

    public BufferedWriter getOutput() {
        return output;
    }

    public void setOutput(BufferedWriter output) {
        this.output = output;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;

    }

}
