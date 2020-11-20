package src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;


public class Client{
    private static final String HOST = Server.getHost();
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private InetAddress address;
    private int port;
    private String name;

    public Client(String name) {
        try {
            this.name = name;
            address = InetAddress.getByName(HOST);
            port = Server.getPort();
        } catch (UnknownHostException e) { System.out.println("Server not found"); }
    }

    public void connect() {
        try {
            socket = new Socket(address, port);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        }
        catch (ConnectException e) { System.out.println(); }
        catch (IOException e) { System.out.println("Incorrect data type"); }
    }

    public void send(Object data) {
        try { objectOutputStream.writeObject(data); }
        catch (IOException e) { System.out.println("Incorrect data type"); }
    }

    public Object receive() {
        try { return objectInputStream.readObject(); }
        catch (SocketException e) { System.out.println(); }
        catch (IOException | ClassNotFoundException e) { System.out.println("Incorrect data type"); }
        return null;
    }
}