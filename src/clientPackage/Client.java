package clientPackage;

import java.net.Socket;

/**
 *
 * @author cem
 */
public class Client {

    private String userName;
    private int password;
    private Socket clientSocket;
    private ClientThread clientThread;

    public Client(String userName, int password, Socket clientSocket) {
        this.userName = userName;
        this.password = password;
        this.clientSocket = clientSocket;
    }

    protected void connetServer() {
        clientThread = new ClientThread(this);
        clientThread.start();
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

    class ClientThread extends Thread {
        Client c;

        public ClientThread(Client c) {
            this.c = c;
        }
        

        @Override
        public void run() {
            if(c.clientSocket.isConnected()){
                System.out.println("bağnıldı!");
            }
        }
    }
}
