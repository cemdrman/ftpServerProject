/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientPackage;

import serverPackage.FtpServer;
import java.net.Socket;

/**
 *
 * @author cem
 */
public class Client {
    private String userName;
    private int password;
    private Socket clientSocket;

    public Client(String userName, int password, Socket clientSocket) {
        this.userName = userName;
        this.password = password;
        this.clientSocket = clientSocket;
    }

    protected void connetServer(){
        FtpServer ftpServer = new FtpServer();
        ftpServer.startServer();
    }
    
}
