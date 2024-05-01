package assignments.assignment3.systemCLI;

import java.util.ArrayList;
import java.util.Scanner;
import assignments.assignment3.Restaurant;

public abstract class UserSystemCLI {
    protected Scanner input;
    public void run() {
        input = new Scanner(System.in);
        boolean isLoggedIn = true;
        while (isLoggedIn) {
            displayMenu();
            int command = input.nextInt();
            input.nextLine();
            isLoggedIn = handleMenu(command);
        }
    }

    protected Restaurant searchResto(String namaRestoran, ArrayList<Restaurant> restoList){
        /*
         * method to search for a restaurant in the restaurant array list
         * if restaurant is found, return Restaurant object
         * else, return null
         */
        if (restoList == null || restoList.isEmpty()){ // if restaurant array list is empty, return null
            return null;
        }
        namaRestoran = namaRestoran.toLowerCase();
        for (Restaurant resto : restoList){ // for loop to search for the restaurant
            if (resto.getNamaRestoran().toLowerCase().equals(namaRestoran)) return resto; // if found return Restaurant object
        }
        return null;
    }

    abstract void displayMenu();
    abstract boolean handleMenu(int command);
}
