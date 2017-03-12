package ftpserverproject;

import java.io.BufferedReader;
import java.io.File;
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
class ServerThread extends Thread {

    private final String serverDirectoryPath = System.getProperty("user.home" + "/FtpServerDirectory"); //Directory masaüstüne açılması için her pcye uygun şekilde path alıyoruz
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private OutputStream output;
    private File file;    

    public ServerThread(Socket socket) {
        try {
            file = new File(serverDirectoryPath);
            makeMainDirectory();//öncelikle ana Directoryi oluşturuyoruz
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
    
    private void allFileListOnTheServer(){
        file.listFiles();//ana dir altındaki bütün dosyalar
    }
    
    private void makeMainDirectory(){        
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Directory is allread created!");
            }
        }        
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
