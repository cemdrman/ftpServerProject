package serverPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cem
 */
public class ServerThread extends Thread {
    
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private OutputStream output;    

    public ServerThread(Socket socket) {
        try {
          
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.output = socket.getOutputStream();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void receiveFileFromClient(){
        
    }
    
    private void sendFileToClient(){
        
    }      
    
    @Override
    public void run() {
       while(true){
           if (true) { //servera dosya alma
               
           }else if(true){//serverdan dosya alma
               
           }
       }
    }
}
