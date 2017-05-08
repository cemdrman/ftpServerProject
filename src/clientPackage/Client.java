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

    private String name;
    private String surname;
    private Socket serverSocket;
    private ClientThread clientThread;
    private ObjectOutputStream outputStream; 
      
    protected void connetServer(String nameSurname) {
       
        try {
            serverSocket = new Socket("localhost", 1907);  
            outputStream = new ObjectOutputStream(serverSocket.getOutputStream());
            sendMessage(nameSurname);
            System.out.println("Connection accepted " + serverSocket.getInetAddress() + "/" + serverSocket.getPort());

        } catch (IOException ex) {
            Logger.getLogger(FrameGiris.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected String getName() {
        return name;
    }

    protected void setName(String userName) {
        this.name = userName;
    }

    protected String getSurname() {
        return surname;
    }

    protected void setSurname(String surname) {
        this.surname = surname;
    }

    protected void sendMessage(String message) throws IOException {       
        outputStream.writeObject(message);

    }
    
    

    /**
     *
     * @param file firstly, the name of file has been sending for server side
     * file name
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

    }

    class ClientThread extends Thread {

        @Override
        public void run() {

        }
    }
}
