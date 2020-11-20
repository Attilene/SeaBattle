package Seminar_2.Task6_Seabattle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;

public class Connect extends Thread {
    public String name;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public Connect(Socket socket) throws IOException {
        this.socket = socket;
        objectInputStream = new ObjectInputStream(this.socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
//        start();
    }

    public void run() {
        name = (String) receive();
        send("ОК, " + name + ". You are connected to the server!");
    }

    public void send(Object data) {
        try {
            objectOutputStream.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
//            System.out.println("Incorrect data type!");
        }
    }

    public Object receive() {
        try {
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
//            System.out.println("Incorrect data type!");
        }
        return null;
    }
}
