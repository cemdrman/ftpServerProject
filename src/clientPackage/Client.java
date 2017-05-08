package clientPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.net.Socket;

/**
 *
 * @author cem
 */
public class Client {

    private String userName;
    private int password;
    private Socket serverSocket;
    private ClientThread clientThread;
    private ObjectOutputStream outputStream;
    protected int status = 0;
    private boolean isConnected = false;
    
    protected void connetServer() {
        clientThread = new ClientThread();
        clientThread.start();
        try {
            serverSocket = new Socket("localhost", 1907);
            outputStream = new ObjectOutputStream(serverSocket.getOutputStream());
            sendMessage("emir asaf");
            System.out.println("Connection accepted " + serverSocket.getInetAddress() + "/" + serverSocket.getPort());           
            
        } catch (IOException ex) {
            Logger.getLogger(FrameGiris.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected String getUserName() {
        return userName;
    }

    protected void setUserName(String userName) {
        this.userName = userName;
    }

    protected int getPassword() {
        return password;
    }

    protected void setPassword(int password) {
        this.password = password;
    }

    protected void sendMessage(String message) throws IOException {
        outputStream.writeObject(message);
    }

    /**
     * 
     * @param file firstly, the name of file has been sending for server side file name
     * @throws IOException 
     */
    protected void uploadFileToServer(File file) throws IOException {
        sendMessage("saveFile"); //yapacağımız işlemi servera bildiriyoruz
        sendMessage(file.getName());              
        byte[] bytes = new byte[(int) file.length()];
       
        FileInputStream is = new FileInputStream(file);
        OutputStream out = serverSocket.getOutputStream();
        int count;
        while ((count = is.read(bytes)) > 0) {            
            out.write(bytes, 0, count);
            break;
        }
        
        System.out.println("Sending " + file.getName() + "(" + bytes.length + " bytes)");      
        System.out.println("Done.");
        is.close();
    }

    class ClientThread extends Thread {

        @Override
        public void run() {
                      
        }
    }
}
