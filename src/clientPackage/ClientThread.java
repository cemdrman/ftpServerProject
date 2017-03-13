package clientPackage;

/**
 *
 * @author cem
 */
public class ClientThread extends Thread{
    
    private Client client;

    public ClientThread(Client client) {
        this.client = client;
    }
    
    @Override
    public void run() {
        client.connetServer();
    }    
}
