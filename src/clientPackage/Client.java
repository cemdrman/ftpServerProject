package clientPackage;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cem
 */
public class Client {

    private String userName;
    private int password;
    private Socket serverSocket;
    private ClientThread clientThread;

    protected void connetServer() {

        try {
            serverSocket = new Socket("localhost", 1907);
            System.out.println("Connection accepted " + serverSocket.getInetAddress() + "/" + serverSocket.getPort());
        } catch (IOException ex) {
            Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
            //baglanamadı!
        }

    }

    public String getUserName() {
        return userName;
    }

    protected void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPassword() {
        return password;
    }

    protected void setPassword(int password) {
        this.password = password;
    }

    protected void sendMessage(String message) {

    }

    protected void uploadFileToServer(File f) {

    }

    class ClientThread extends Thread {

        @Override
        public void run() {

        }
    }
}
