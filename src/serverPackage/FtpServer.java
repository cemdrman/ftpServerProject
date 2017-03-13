package serverPackage;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import clientPackage.*;

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
    private File file;
    private final String systemPath = System.getProperty("user.home");
    private final String serverDirectoryPath = systemPath + "\\desktop\\FtpServerDirectory"; //Directory masaüstüne açılması için her pcye uygun şekilde path alıyoruz
    private Client connectedClient;
    
    public void stopServer(){
        serverSocket = null;
        address = null;
        socket = null;
        file = null;
    }
    public void startServer() {
        try {            
            file = new File(serverDirectoryPath);
            address = InetAddress.getByName(serverIp);
            serverSocket = new ServerSocket(serverPort);
            System.out.println("---------FTP-SERVER---------");
            System.out.println("****************************");
            System.out.println("Server Info");
            System.out.println("Server directory path:" + serverDirectoryPath);
            System.out.println("Server IP            : " + address.getHostName());
            System.out.println("Server Port Number   : " + String.valueOf(serverPort));
            System.out.println("****************************");
            System.out.println("Server Started...");
            System.out.println("Waiting for connections...");
            makeMainDirectory();//öncelikle ana Directoryi oluşturuyoruz
            socket = serverSocket.accept();//client bağlanana kadar burda bekler            
            System.out.println("New Client Connected from " + socket.getInetAddress().getHostName() + "/" +socket.getPort());
            ServerThread serverThread = new ServerThread(socket);
            serverThread.start();
        } catch (IOException ex) {
            Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
            stopServer();
        }
    }

    private void makeMainDirectory() {
        if (file.exists()) { //böyle bir klasör var mı?
            System.out.println("Directory is allready created!");
        } else {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            }else{
                System.out.println("Directory is not created!");
            }            
        }
    }

    private File[] allFileListOnTheServer() {
        return file.listFiles();
    }
    
    public int getServerPort() {
        return serverPort;
    }

}
