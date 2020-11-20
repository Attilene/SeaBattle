package src;

import java.util.Scanner;

public class User_Client {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String name;
        System.out.print("Input your name: ");
        name = scan.next();
        User_Interface ui = new User_Interface(name);
        ui.newGame();
    }
}