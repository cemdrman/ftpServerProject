package clientPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author cem
 */
public class Client {

    private String name;
    private String surname;
    private Socket serverSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private boolean isConnected = false;

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
    
    protected boolean getIsConnected() {
        return isConnected;
    }
    
    protected void sendMessage(String message) throws IOException {
        outputStream.writeObject(message);
    }

    private Object readMessage() throws IOException, ClassNotFoundException {
        return inputStream.readObject();
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

    /**
     *
     * @param nameSurname for creating userDir on server side
     */
    protected void connetServer(String nameSurname) {

        try {
            serverSocket = new Socket("localhost", 1907);
            outputStream = new ObjectOutputStream(serverSocket.getOutputStream());
            inputStream = new ObjectInputStream(serverSocket.getInputStream());
            sendMessage(nameSurname);
            System.out.println("Connection accepted " + serverSocket.getInetAddress() + "/" + serverSocket.getPort());
            isConnected = true;
        } catch (IOException ex) {
            Logger.getLogger(FrameGiris.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @return gets the clients' file from server
     */
    protected ArrayList<String> getFileListFromServer() {

        ArrayList<String> fileList = null;
        try {
            sendMessage("getFileList"); //goes to server for which method works
            fileList = (ArrayList<String>) readMessage();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileList;
    }
        
    /**
     * 
     * @param fileName for downloading file from server
     */
    protected void downloadFileFromServer(String fileName){
        try {
            sendMessage("downloadFile");
            sendMessage(fileName);
            //readFile()
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
