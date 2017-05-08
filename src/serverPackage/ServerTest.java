package serverPackage;

/**
 *
 * @author cem
 */
public class ServerTest {
    public static void main(String[] args) {
        FtpServer ftpServer = FtpServer.getInstance();
        ftpServer.startServer();
    }
}
