package Seminar_2.Task6_Seabattle;

import java.io.Serializable;
import java.util.Random;

public class Map implements Serializable {
    public static class Ship implements Serializable{
        int size, hit = 0;
        int xlu, ylu, xrd, yrd;
        boolean isVertically;
        int coordX, coordY;
        int[][] coords;

        public Ship(int size) {
            this.size = size;
            coords = new int[this.size][2];
            Random rd = new Random();
            isVertically = rd.nextBoolean();
            coordX = (int) (Math.random() * 10);
            coordY = (int) (Math.random() * 10);
        }
    }


    int mapsize = 10;
    public char[][] map = new char[mapsize][mapsize];
    int[] shipssizes = {0, 4, 3, 2, 1};
    int numberofships = 10;
    public Ship[] listships = new Ship[numberofships];
    String name;

    public Map(String name) {
        this.name = name;
        for (int i = 0; i < mapsize; i++)
            for (int j = 0; j < mapsize; j++)
                map[i][j] = ' ';
    }

    public void setShip(Ship sh) {
        for (int j = 0; j < sh.size; j++) {
            if (sh.isVertically) {
                map[sh.coordX + j][sh.coordY] = 'S';
                sh.coords[j][0] = sh.coordX + j;
                sh.coords[j][1] = sh.coordY;
            }
            else {
                map[sh.coordX][sh.coordY + j] = 'S';
                sh.coords[j][0] = sh.coordX;
                sh.coords[j][1] = sh.coordY + j;
            }
        }
    }

    public void randomLoc() {
        int count = 0;
        for (int i = 1; i < shipssizes.length; i++) {
            for (int j = 1; j <= i; j++) {
                listships[count] = new Ship(shipssizes[i]);
                while (!checkLoc(listships[count])) {
                    listships[count] = new Ship(shipssizes[i]);
                }
                setShip(listships[count]);
                count++;
            }
        }
    }

    private boolean checkLoc(Ship ship){
        int xlu, xrd = mapsize, ylu, yrd = mapsize, kx, ky;
        if (ship.isVertically) {
            kx = 1;
            ky = 0;
        }
        else {
            kx = 0;
            ky = 1;
        }
        int x = ship.coordX;
        int y = ship.coordY;
        int decks = ship.size;
        if (!ship.isVertically){
            if (y + decks >= mapsize)
                return false;
        }
        else {
            if (x + decks >= mapsize)
                return false;
        }
        if (x == 0) xlu = x;
        else xlu = x - 1;
        if (x + kx * decks == mapsize & kx == 1)
            xrd = x + kx * decks;
        else
            if (x + kx * decks < mapsize & kx == 1)
                xrd = x + kx * decks + 1;
            else
                if (x == mapsize - 1 & kx == 0)
                    xrd = x + 1;
                else
                    if (x < mapsize - 1 & kx == 0)
                        xrd = x + 2;
        if (y == 0) ylu = y;
        else ylu = y - 1;
        if (y + ky * decks == mapsize & ky == 1)
            yrd = y + ky * decks;
        else
            if (y + ky * decks < mapsize & ky == 1)
                yrd = y + ky * decks + 1;
            else
                if (y == mapsize - 1 & ky == 0)
                    yrd = y + 1;
                else
                    if (y < mapsize - 1 & ky == 0)
                        yrd = y + 2;
        for (int i = xlu; i < xrd; i++) {
            for (int j = ylu; j < yrd; j++) {
                if (map[i][j] == 'S')
                    return false;
            }
        }
        ship.xlu = xlu;
        ship.ylu = ylu;
        ship.xrd = xrd;
        ship.yrd = yrd;
        return true;
    }

    public char[][] getSecure(boolean secret) {
        char[][] bufmap = new char[mapsize][mapsize];
        if (secret) {
            for (int i = 0; i < bufmap.length; i++) {
                for (int j = 0; j < bufmap[0].length; j++)
                    if (map[i][j] == 'S') bufmap[i][j] = ' ';
                    else bufmap[i][j] = map[i][j];
            }
        }
        else
            for (int i = 0; i < bufmap.length; i++)
                System.arraycopy(map[i], 0, bufmap[i], 0, bufmap[0].length);
        return bufmap;
    }

    public char[][] getSecure() { return getSecure(false); }

    public void mapOut(boolean secret) {
        System.out.print("  |");
        for(int i = 0; i < map.length; i++) {
            System.out.print(" " + i);
        }
        System.out.println();
        for (int i = 0; i < 23; i++) {
            System.out.print("͞");
        }
        System.out.println();
        for (int i = 0; i < map.length; i++) {
            System.out.print(i + " | " );
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == 'S' & secret) System.out.print("  ");
                else System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void mapOut() { mapOut(false); }
}
