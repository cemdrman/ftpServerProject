package serverPackage;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import clientPackage.*;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

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
    private File serverFile;
    private final String systemPath = System.getProperty("user.home");
    private final String serverDirectoryPath = systemPath + "\\desktop\\FtpServerDirectory"; //Directory masaüstüne açılması için her pcye uygun şekilde path alıyoruz
    private static Client connectedClient;
    private static ServerThread serverThread;
    private ArrayList<Client> connectedClientList = new ArrayList();

    public ArrayList<Client> getConnectedClientList() {
        return connectedClientList;
    }

    private void addClientToConnectedList(Client connectedClient) {
        connectedClientList.add(connectedClient);
    }

    public void stopServer() {
        serverSocket = null;
        address = null;
        socket = null;
        serverFile = null;
    }

    public void startServer() {
        try {
            serverFile = new File(serverDirectoryPath);
            address = InetAddress.getByName(serverIp);
            serverSocket = new ServerSocket(serverPort);
            System.out.println("---------FTP-SERVER---------");
            System.out.println("****************************");
            System.out.println("Server Info");
            System.out.println("Server directory path: " + serverDirectoryPath);
            System.out.println("Server IP            : " + address.getHostName());
            System.out.println("Server Port Number   : " + String.valueOf(serverPort));
            System.out.println("****************************");
            System.out.println("Server Started...");
            System.out.println("Waiting for connections...");
            makeMainDirectory();//öncelikle ana Directoryi oluşturuyoruz
            serverThread = new ServerThread();
            serverThread.start();

        } catch (IOException ex) {
            Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
            stopServer();
        }
    }

    private void receiveFileFromClient() {

    }

    private void sendFileToClient() {

    }

    private void makeMainDirectory() {
        if (serverFile.exists()) { //böyle bir klasör var mı?
            System.out.println("Directory is allready created!");
        } else if (serverFile.mkdir()) {
            System.out.println("Directory is created!");
        } else {
            System.out.println("Directory is not created!");
        }
    }

    private void makeUserDirectory() {
        String userName = connectedClient.getUserName();
        String userDirectoryPath = getMainDirectoryPath().concat("\\").concat(userName);
        File userFile = new File(userDirectoryPath);
        if (userFile.exists()) {
            System.out.println("User Directory is allready created!");
        } else if (userFile.mkdir()) {
            System.out.println("User Directory is created on the " + userDirectoryPath);
        } else {
            System.out.println("User Directory is not created!");
        }
    }

    public File[] allFileListOnTheServer() {
        return serverFile.listFiles();
    }

    public File[] allFileListsClient(Client client) {
        File[] fileList = null;
        for (int i = 0; i < 10; i++) {
            //bütün user listemden client ismine göre kontrol edip ona göre gelmesi gerekir
            //user listesi eşittir serverDirectory'deki klasörler
            //user listesi için bir veri yapısı olabilir!
        }

        return fileList;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getMainDirectoryPath() {
        return serverDirectoryPath;
    }

    class ServerThread extends Thread {

        private Socket socket;

        @Override
        public void run() {
            while (!serverSocket.isClosed()) {
                try {
                    socket = serverSocket.accept();//client bağlanana kadar burda bekler
                    System.out.println("Connected from " + socket.getInetAddress().getHostName() + "/" + socket.getPort());
                } catch (IOException ex) {
                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
