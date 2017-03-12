package ftpserverproject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cem
 */
public class FtpServer {

    private ServerSocket serverSocket;
    private String serverIp;
    private final int serverPort = 1907;
    private InetAddress address;
    private Socket socket;

    public void startServer() {
        init();
        System.out.println("---------FTP-SERVER---------");
        System.out.println("****************************");
        System.out.println("Server Info");
        System.out.println("Server IP         : " + address.getHostName());
        System.out.println("Server Port Number: " + String.valueOf(serverPort));
        System.out.println("****************************");
        System.out.println("Server Started...");
        System.out.println("Waiting for connections...");
        

    }
    public int getServerPort(){
        return serverPort;
    }
    
    private void init() {
        try {
            address = InetAddress.getByName(serverIp);
            serverSocket = new ServerSocket(serverPort);
            socket = serverSocket.accept();//client bağlanana kadar burda bekler
            System.out.println("New Client Connected from " + socket.getInetAddress().getHostName() + "...");
            ServerThread serverThread = new ServerThread(socket);
            serverThread.start();
        } catch (IOException ex) {
            Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}


