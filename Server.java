package Seminar_2.Task6_Seabattle;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;


public class Server extends Thread{
//    private static final String HOST = "25.100.170.100";
    private static final String HOST = "localhost";
    private static final int PORT = 5005;
    public static ArrayList<Connect> connects = new ArrayList<>();
    private ServerSocket serverSocket;
    private Map MapUser1, MapUser2;

    static public int getPort() { return PORT; }
    static public String getHost() { return HOST; }

    public Server() {
        try {
            InetAddress address = InetAddress.getByName(HOST);
            serverSocket = new ServerSocket(PORT, 0, address);
            System.out.println("Server is running: " + serverSocket.getLocalSocketAddress());
        } catch (IOException e) { System.out.println("Incorrect closing!"); }
    }

    public void run() {
        System.out.println("Waiting for players to connect!");
        while (!isStart()) {
            try {
                Connect clientConnection = new Connect(serverSocket.accept());
                Server.connects.add(clientConnection);
                clientConnection.run();
                System.out.println("Client connected: " + clientConnection.name);
                if (clientConnection.getName().equals("Thread-1")) MapUser1 = new Map(clientConnection.name);
                else if (clientConnection.getName().equals("Thread-2")) MapUser2 = new Map(clientConnection.name);
            } catch (IOException ex) {
                disconnect();
            }
        }
        System.out.println("Clients are connected");
        MapUser1.randomLoc();
        MapUser2.randomLoc();
        Server.connects.get(0).send(MapUser2.name);
        Server.connects.get(1).send(MapUser1.name);
        Server.connects.get(0).send(MapUser1.getSecure());
        Server.connects.get(0).send(MapUser2.getSecure(true));
        Server.connects.get(1).send(MapUser2.getSecure());
        Server.connects.get(1).send(MapUser1.getSecure(true));
        sendMessageAll("Start the game!");
        Random rd = new Random();
        boolean first_way = rd.nextBoolean();
        ways(first_way, MapUser1, MapUser2);
        killThreads();
        run();
    }

    private void sendMessageAll(String msg) {
        for(Connect client : connects) {
            client.send(msg);
        }
    }

    private void disconnect() {
        try { serverSocket.close(); }
        catch (SocketException e) { System.out.println("Incorrect closing"); }
        catch (IOException e) { System.out.println("Incorrect closing!"); }
    }

    private void ways(boolean first_way, Map mapus, Map mapen) {
        if (first_way) sendMessageAll(mapus.name + " goes first!");
        else sendMessageAll(mapen.name + " goes first!");
        while (true) {
            if (first_way) {
                if (!userWay(mapus, mapen)) break;
                if (isGameOver(mapus, mapen)) break;
                if (!userWay(mapen, mapus)) break;
                if (isGameOver(mapen, mapus)) break;
            }
            else {
                if (!userWay(mapen, mapus)) break;
                if (isGameOver(mapen, mapus)) break;
                if (!userWay(mapus, mapen)) break;
                if (isGameOver(mapus, mapen)) break;
            }
        }
    }

    private boolean isGameOver(Map mapus, Map mapen) {
        if (User_Interface.isMapEmpty(mapen)) {
            sendMessageAll(mapus.name + " won!!! Game over!");
            return true;
        }
        return false;
    }

    private boolean userWay(Map mapus, Map mapen) {
        int x, y;
        int[] coords;
        int index;
        if (mapus.name.equals(MapUser1.name)) index = 0;
        else index = 1;
        sendMessageAll(mapus.name + "`s way:");
        Server.connects.get(index).send("To exit, enter: -1 -1");
        Server.connects.get(index).send("Enter coordinates of cell (x, y): ");
        while (true) {
            try {
                coords = (int[]) Server.connects.get(index).receive();
                x = coords[0];
                y = coords[1];
                if (x == -1 & y == -1) { // Выход из игры
                    sendMessageAll(mapen.name + " won!!! Game over!");
                    return false;
                }
                else if (x >= 10 | x < 0 | y >= 10 | y < 0) { throw new InputMismatchException(); }
                else break;
            }
            catch (InputMismatchException e) {
                Server.connects.get(index).send(mapus.name + " are entered incorrect data!");
                Server.connects.get(index).send("Enter right coordinates: ");
            }
        }
        Server.connects.get(Math.abs(index - 1)).send(mapus.name + " selected the coordinates: " + x + " " + y);
        if (mapen.map[x][y] == 'S') {
            mapen.map[x][y] = '˟';
            if (!User_Interface.isKilled(mapen, x, y))
                sendMessageAll(mapus.name + " hit the ship!");
            else
                sendMessageAll(mapus.name + " killed the ship!");
            Server.connects.get(index).send(mapen.getSecure(true));
            Server.connects.get(Math.abs(index - 1)).send(mapen.getSecure());
            return userWay(mapus, mapen);
        }
        else if (mapen.map[x][y] == ' ' | mapen.map[x][y] == '·') {
            sendMessageAll(mapus.name + " missed!");
            mapen.map[x][y] = '·';
        }
        else if (mapen.map[x][y] == '˟') {
            sendMessageAll(mapus.name + " missed!");
        }
        Server.connects.get(index).send(mapen.getSecure(true));
        Server.connects.get(Math.abs(index - 1)).send(mapen.getSecure());
        return true;
    }

    private static boolean isStart() { return Server.connects.size() == 2; }

    private static void killThreads() {
        for (Connect conn: Server.connects)
            conn.interrupt();
        Server.connects.clear();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

}