package assignments.assignment3.systemCLI;

import java.util.ArrayList;
import assignments.assignment3.Menu;
import assignments.assignment3.Restaurant;
import assignments.assignment3.User;
import assignments.assignment3.MainMenu;


//TODO: Extends Abstract yang diberikan
public class AdminSystemCLI extends UserSystemCLI{
    private ArrayList<Restaurant> restoList;

    public boolean handleMenu(int command){
        /*
         * method to handle admin menu
         */
        restoList = MainMenu.getRestoList();
        switch(command){
            case 1 -> handleTambahRestoran();
            case 2 -> handleHapusRestoran();
            case 3 -> {return false;}
            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
        }
        return true;
    }

    //TODO: Tambahkan modifier dan buatlah metode ini mengoverride dari Abstract class
    public void displayMenu() {
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Tambah Restoran");
        System.out.println("2. Hapus Restoran");
        System.out.println("3. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    protected void handleTambahRestoran(){
        /*
         * method for admin to add new restaurant
         * add the newly created restaurant to restaurant array list
         */
        // get restaurant array list from MainMenu class
        System.out.println("--------------Tambah Restoran--------------");
        while (true){ // keep looping until all inputs are successfully validated
            System.out.print("Nama: ");
            String namaRestoran = input.nextLine(); // receive restaurant name input
            if (namaRestoran.length() < 4){ // if restaurant name input length is less than 4 charactes, continue
                System.out.println("Nama restoran tidak valid!\n");
                continue;
            }
            if (searchResto(namaRestoran, restoList) != null){ // try to find restaurant by its name (if not found, continue)
                System.out.println("Restoran dengan nama " + namaRestoran + " sudah pernah terdaftar. Mohon masukkan nama yang berbeda!\n");
                continue;
            }

            boolean validation = true; // for validation (default value is true)
            Restaurant restoBaru = new Restaurant(namaRestoran); // create a new restaurant object by it's name
            System.out.print("Jumlah Makanan: ");
            int jumlahMakanan = input.nextInt(); // receive food amount input
            input.nextLine(); // clear scanner
            if (jumlahMakanan < 1){ // if food amount input is less than 1, continue
                System.out.println("Jumlah makanan minimal 1!\n");
                continue;
            }

            String[] listMenu = new String[jumlahMakanan]; // to store menu inputs
            System.out.println("Masukkan Menu:");
            for (int i = 0; i < jumlahMakanan; i++){ // for loop to receive menu inputs (food name and cost)
                String inputMenu = input.nextLine().trim(); // receive menu input
                listMenu[i] = inputMenu; // add menu input to listMenu
            }
            for (int i = 0; i < jumlahMakanan; i++){ // for loop to validate menu inputs
                String[] splitInputMenu = listMenu[i].split(" "); // split menu input by the spaces
                String namaMakanan = "";
                double harga;

                if (splitInputMenu.length == 1){ // if splitted menu string length is equal to 1, set validation to false and break for loop
                    validation = false;
                    System.out.println("Masukkan nama makanan dan harga dari setiap makanan tersebut!\n");
                    break;
                }

                try{ // try to convert last element of splitted menu list into double data type
                    harga = Double.parseDouble(splitInputMenu[(splitInputMenu.length) - 1]);  
                }
                catch (Exception e){ // if failed, set validation to false and break for loop
                    validation = false;
                    System.out.println("Harga menu harus bilangan bulat!\n");
                    break;
                }

                for (int j = 0; j < (splitInputMenu.length) - 1; j++){ // for loop to get food name from splitted menu list
                    namaMakanan += splitInputMenu[j];
                    if (j != (splitInputMenu.length) - 2){ // add spaces between words
                        namaMakanan += " ";
                    }
                }
                restoBaru.addMenu(new Menu(namaMakanan, harga)); // add menu to restaurant's menu array list
            }
            if (!validation) continue; // if validation is false, continue
            restoList.add(restoBaru); // add newly created restaurant to restaurant array list
            MainMenu.updateRestoList(restoList);
            System.out.print("Restoran " + namaRestoran + " berhasil terdaftar.");
            break; // break while loop
        }
    }

    protected void handleHapusRestoran(){
        /*
         * method for admin to remove existing restaurant from restaurant array list
         */
        System.out.println("--------------Hapus Restoran--------------");
        if (restoList.size() == 0){ // if restaurant array list is empty, print message and return
            System.out.println("Belum ada restoran yang terdaftar.\n");
            return;
        }
        while (true){ // keep looping until restaurant name input is valid
            System.out.print("Nama Restoran: ");
            String namaRestoran = input.nextLine().trim(); // receive restaurant name input
            Restaurant resto = searchResto(namaRestoran, restoList); // try to find restaurant by it's name

            if (resto != null){ // if restaurant is found, remove restaurant from restaurant array list and break while loop
                restoList.remove(resto);
                MainMenu.updateRestoList(restoList);
                System.out.print("Restoran berhasil dihapus.");
                break;
            }
            System.out.println("Restoran tidak terdaftar pada sistem.\n");    
        }
    }
}
