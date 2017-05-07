package serverPackage;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import clientPackage.*;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    public ArrayList<Client> getConnectedClientList() {
        return connectedClientList;
    }

    private void addClientToConnectedList(Client connectedClient) {
        connectedClientList.add(connectedClient);
    }

    public void stopServer() {
        serverSocket = null;
        address = null;
        connectedSocket = null;
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

    private void init() throws IOException {
        outputStream = new ObjectOutputStream(connectedSocket.getOutputStream());
        inputStream = new ObjectInputStream(connectedSocket.getInputStream());
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

    public void uploadFile(Client c, File f) {
        makeUserDirectory(c.getUserName());
        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            DataOutputStream dataOutputStream = new DataOutputStream(connectedSocket.getOutputStream());

            dataOutputStream.write(fileInputStream.read());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
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

    protected void sendMessage(String message) throws IOException {
        outputStream.writeObject(message);
    }

    private String readMessage() throws IOException, ClassNotFoundException {
        return (String) inputStream.readObject();
    }

    private void saveFile() throws IOException, ClassNotFoundException {

        fileName = readMessage();
        System.out.println("file name: " + fileName);        
        int current;
        FileOutputStream fos = new FileOutputStream(fileName);
        InputStream in = connectedSocket.getInputStream();
        try {
            byte[] bytes = new byte[16 * 1024];
            while ((current = in.read(bytes)) > 0) {
                fos.write(bytes, 0, current);
            }
        } finally {
            fos.close();
            in.close();
        }
    }

    class ServerThread extends Thread {

        @Override
        public void run() {
            while (!serverSocket.isClosed()) {
                try {
                    if (isConnected) {
                        //bağlı oldugu icin gelen mesaja göre işlem yapılır.
                        saveFile();
                    } else {
                        connectToServer();
                    }
                } catch (ClassNotFoundException | IOException ex) {
                    Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void connectToServer() throws IOException, ClassNotFoundException {
            connectedSocket = serverSocket.accept();//client bağlanana kadar burda bekler
            System.out.println("[Log]--> Connected from " + connectedSocket.getInetAddress().getHostName() + "/" + connectedSocket.getPort());
            init();
            sendMessage(serverIp);
            userName = readMessage();
            System.out.println("[Log]--> " + userName + " connected to server");
            makeUserDirectory(userName);
            isConnected = true;
        }

    }

}
