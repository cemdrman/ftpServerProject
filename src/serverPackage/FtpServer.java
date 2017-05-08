package serverPackage;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import clientPackage.*;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author cem
 */
public class FtpServer {

    private static ServerSocket serverSocket;
    private String serverIp;
    private final int serverPort = 1907;
    private InetAddress address;
    private static Socket connectedSocket;
    private File serverFile;
    private final String systemPath = System.getProperty("user.home");
    private final String serverDirectoryPath = systemPath + "\\desktop\\FtpServerDirectory"; //Directory masaüstüne açılması için her pcye uygun şekilde path alıyoruz    
    private static ServerThread serverThread;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ArrayList<Client> connectedClientList = new ArrayList();
    private boolean isConnected = false;
    private String userName;
    private String fileName;
    private static FtpServer singletonInstance;

    private FtpServer() {
    }

    public static FtpServer getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new FtpServer();
        }
        return singletonInstance;
    }

    private ArrayList<Client> getConnectedClientList() {
        return connectedClientList;
    }

    private void addClientToConnectedList(Client connectedClient) {
        connectedClientList.add(connectedClient);
    }

    protected void stopServer() {
        serverSocket = null;
        address = null;
        connectedSocket = null;
        serverFile = null;
    }

    protected void startServer() {
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

    private void init() throws IOException {
        outputStream = new ObjectOutputStream(connectedSocket.getOutputStream());
        inputStream = new ObjectInputStream(connectedSocket.getInputStream());
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

    private void makeUserDirectory(String userName) {
        String userDirectoryPath = getMainDirectoryPath().concat("\\").concat(userName);
        File userFile = new File(userDirectoryPath);
        if (userFile.exists()) {
            System.out.println("[Log]--> User Directory is allready created!");
        } else if (userFile.mkdir()) {
            System.out.println("[Log]--> User Directory is created on the " + userDirectoryPath);
        } else {
            System.out.println("[Log]--> User Directory is not created!");
        }
    }

    private File[] allFileListOnTheServer() {
        return serverFile.listFiles();
    }

    private File[] allFileListsClient(String userName) {
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

    private String getMainDirectoryPath() {
        return serverDirectoryPath;
    }

    private void sendMessage(String message) throws IOException {
        outputStream.writeObject(message);
    }

    private String readMessage() throws IOException, ClassNotFoundException {
        return (String) inputStream.readObject();
    }

    private void saveFile() throws IOException, ClassNotFoundException {

        fileName = readMessage();
        String filePath = getMainDirectoryPath().concat("\\").concat(userName).concat("\\").concat(fileName);
        System.out.println("file path: " + filePath);
        int current;
        FileOutputStream fos = new FileOutputStream(filePath);
        InputStream in = connectedSocket.getInputStream();

        byte[] bytes = new byte[1024 * 1024];
        while (true) {
            current = in.read(bytes);
            fos.write(bytes, 0, current);
            break;
        }
        fos.close();
    }

    private void connectToServer() throws IOException, ClassNotFoundException {
        connectedSocket = serverSocket.accept();//client bağlanana kadar burda bekler
        System.out.println("[Log]--> Connected from " + connectedSocket.getInetAddress().getHostName() + "/" + connectedSocket.getPort());
        init();
        //sendMessage(serverIp);
        userName = readMessage();
        System.out.println("[Log]--> " + userName + " connected to server");
        makeUserDirectory(userName);
        isConnected = true;
    }

    class ServerThread extends Thread {

        @Override
        public void run() {
            while (!serverSocket.isClosed()) {
                try {
                    if (isConnected) {  //bağlı oldugu icin gelen mesaja göre işlem yapılır.
                        if (readMessage().equals("saveFile")) {
                            saveFile();
                        }
                    } else {
                        connectToServer();
                    }
                } catch (ClassNotFoundException | IOException ex) {
                    Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
