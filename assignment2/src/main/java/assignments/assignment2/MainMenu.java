package assignments.assignment2;
import java.time.format.DateTimeFormatter; // import java DateTimeFormatter
import java.util.ArrayList; // import java arraylist
import java.util.Scanner; // import java scanner
// import assignments.assignment1.*; // import everything from assignment 1 (TP 1)

public class MainMenu {
    private static final Scanner input = new Scanner(System.in); // create scanner variable called "input"
    private static ArrayList<Restaurant> restoList = new ArrayList<Restaurant>(); // create a new array list for restaurants
    private static ArrayList<User> userList; // create an empty array list for users

    public static void main(String[] args) { // main program
        boolean programRunning = true;
        printHeader(); // print header
        initUser(); // setup users and add to the user array list
        while(programRunning){ // keep looping until program exited (command == "2" and programRunning == false)
            startMenu(); // print start menu
            String command = input.nextLine(); // receive command input 

            if(command.equals("1")){ // if command == "1" (login)
                System.out.println("\nSilakan Login:");
                System.out.print("Nama: ");
                String nama = input.nextLine(); // receive input nama
                System.out.print("Nomor Telepon: ");
                String noTelepon = input.nextLine(); // receive input nomor telepon

                User userLoggedIn = getUser(nama, noTelepon); // try to find user info in user array list
                if (userLoggedIn == null){ // if user not found, continue
                    System.out.println("Pengguna dengan data tersebut tidak ditemukan!\n");
                    continue;
                }

                boolean isLoggedIn = true;
                if(userLoggedIn.getRole() == "Customer"){ // if user logged in is a customer
                    while (isLoggedIn){ // loop ends until exited (commandCust == "5" and isLoggedIn == false)
                        menuCustomer(); // print customer menu
                        String commandCust = input.nextLine(); // receive customer command input

                        switch(commandCust){
                            case "1" -> handleBuatPesanan(userLoggedIn); // make an order
                            case "2" -> handleCetakBill(userLoggedIn); // print bill
                            case "3" -> handleLihatMenu(); // print menu list
                            case "4" -> handleUpdateStatusPesanan(userLoggedIn); // update status of orders
                            case "5" -> { // logout
                                isLoggedIn = false; // set to false to stop while loop
                                System.out.println();
                            }
                            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali"); // if customer command is unknown
                        }
                    }
                }
                else{ // if user logged in is an admin
                    while (isLoggedIn){ // loop ends until exited (commandAdmin == "5" and isLoggedIn == false)
                        menuAdmin(); // print admin menu
                        String commandAdmin = input.nextLine(); // receive admin command input

                        switch(commandAdmin){
                            case "1" -> handleTambahRestoran(); // add new restaurant
                            case "2" -> handleHapusRestoran(); // remove existing restaurant
                            case "3" -> { // logout
                                isLoggedIn = false; // set to false to stop while loop
                                System.out.println();
                            }
                            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali"); // if admin command is unknown
                        }
                    }
                }
            }
            else if(command.equals("2")){ // exit program
                programRunning = false; // set to false to stop while loop
            }
            else{ // if input command is unknown
                System.out.println("Perintah tidak diketahui, silakan periksa kembali.\n");
            }

        }
        System.out.println("\nTerima kasih telah menggunakan DepeFood ^___^");
    }
    

    public static User getUser(String nama, String noTelepon){
        /*
         * check if user exists in user array list
         * if yes, return user
         * else, return null
         */
        for (User user: userList){
            if (user.getNamaUser().equals(nama) && user.getNoTelepon().equals(noTelepon)){
                System.out.print("Selamat Datang " + nama + "!");
                return user;
            }
        }
        return null;
    }


    public static void handleBuatPesanan(User userLoggedIn){
        /*
         * make a new order from the logged-in user (customer)
         */
        System.out.println("\n--------------Buat Pesanan--------------");
        while (true){ // keep looping until all inputs are successfully validated
            if (isRestoListEmpty()){ // if restaurant array list is empty, break
                System.out.println("Belum ada restoran yang terdaftar.");
                break;
            }

            System.out.print("Nama Restoran: ");
            String namaRestoran = input.nextLine(); // receive restaurant name

            Restaurant resto = searchResto(namaRestoran); // try to find the restaurant by its name
            if (resto == null){ // if restaurant not found, continue
                System.out.println("Restoran tidak terdaftar pada sistem.\n");
                continue;
            }

            System.out.print("Tanggal Pemesanan (DD/MM/YYYY): ");
            String tanggalOrder = input.nextLine(); // receive order date
            if (!valDate(tanggalOrder)){ // call method valDate() to validate date input (if date is invalid, continue)
                System.out.println("Masukkan tanggal sesuai format (DD/MM/YYYY)!\n");
                continue;
            }

            System.out.print("Jumlah Pesanan: ");
            int jumlahPesanan = input.nextInt(); // receive amount of items
            input.nextLine(); // clear scanner
            if (jumlahPesanan < 1){ // if amount of items input is less than 1, continue
                System.out.println("Jumlah pesanan minimal 1!\n");
                continue;
            }

            String[] pesananList = new String[jumlahPesanan]; // stores items list
            Menu[] items = new Menu[jumlahPesanan]; // define items array with given size
            Menu menu = null; // menu object to store menu object (for validation default value is null)
            System.out.println("Order:");
            for (int i = 0; i < jumlahPesanan; i++){ // for loop to receive items input
                String pesanan = input.nextLine(); // receive item input
                pesananList[i] = pesanan; // add each item to pesananList
            }
            for (int i = 0; i < jumlahPesanan; i++){ // for loop to validate input
                menu = searchMenu(resto, pesananList[i]); // try to find the items input in menu
                if (menu != null) items[i] = menu; // if item is found add menu to items array list
                else break; // else if item is not found, break
            }
            if (menu == null) { // if an item is not found in menu, continue
                System.out.println("Mohon memesan menu yang tersedia di Restoran!\n");
                continue;
            }

            String orderID = generateOrderID(namaRestoran, tanggalOrder, userLoggedIn.getNoTelepon()); // get order id
            int ongkir = Integer.parseInt(calculateShippingCost(userLoggedIn.getLokasi()).substring(3,9)
                    .replace(".", "")); // get shipping cost
            userLoggedIn.addOrder(new Order(orderID, tanggalOrder, ongkir, resto, items)); // add new order to user's order array list
            System.out.print("Pesanan dengan ID " + orderID + " diterima!");
            break; // break while loop
        }
    }


    public static void handleCetakBill(User userLoggedIn){
        /*
         * receive and validate orderID input
         * add all information about order to string 'bill'
         * print bill
         */
        String bill = "\nBill:"; // string bill to make the bill
        System.out.println("\n--------------Cetak Bill--------------");
        while (true){ // keep looping until orderID input is valid
            if (isOrderHistoryEmpty(userLoggedIn)){ // if user's order history array list is empty, break
                System.out.println("Belum ada pesanan yang terdaftar. Mohon pesan terlebih dahulu!");
                break;
            }

            System.out.print("Masukkan Order ID: ");
            String orderID = input.nextLine(); // receive orderID input

            Order selectedOrder = searchOrder(userLoggedIn, orderID); // try to find orderID in user's order array list
            if (selectedOrder == null){ // if order is not found, continue
                System.out.println("Order ID tidak ditemukan.\n");
                continue;
            }

            bill += "\nOrder ID: " + orderID; // add orderID to bill
            bill += "\nTanggal Pemesanan: " + selectedOrder.getTanggalOrder(); // add order date to bill
            bill += "\nRestaurant: " + selectedOrder.getRestoran().getNamaRestoran(); // add restaurant name to bill 
            bill += "\nLokasi Pengiriman: " + userLoggedIn.getLokasi(); // add user location to bill 
            bill += "\nStatus Pengiriman: " + selectedOrder.getOrderStatus(); // add order status to bill 
            bill += "\nPesanan:\n";
            ArrayList<Menu> items = selectedOrder.getItems(); // store items of the order into items array list
            double totalHarga = Double.parseDouble(calculateShippingCost(userLoggedIn.getLokasi())
                                .substring(3,9).replace(".", "")); // define totalHarga as shipping cost
            for (Menu item: items){ // for loop every items in items array list
                bill += "- " + item.getNamaMakanan(); // add food name to bill 
                bill += String.format(" %.0f\n", item.getHarga()); // add food cost to bill 
                totalHarga += item.getHarga(); // calculate total cost
            }
            bill += "Biaya Ongkos Kirim: " + calculateShippingCost(userLoggedIn.getLokasi()).replace(".", ""); // add shipping cost to bill 
            bill += String.format("\nTotal Biaya: Rp %.0f", totalHarga); // add total cost to bill 
            break; // break while loop
        }   
        System.out.print(bill); // print bill
    }


    public static void handleLihatMenu(){
        /*
         * receive and validate restaurant name
         * add all food and cost in menu to string 'menu'
         * print menu list
         */
        String menu = "Menu:\n"; // string to store menu list
        System.out.println("\n--------------Lihat Menu--------------");
        while (true){ // keep looping until restaurant name input is valid
            if (isRestoListEmpty()){ // if restaurant array list is empty, break
                System.out.println("Belum ada restoran yang terdaftar.");
                break;
            }

            System.out.print("Nama Restoran: ");
            String namaRestoran = input.nextLine(); // receive restaurant name input

            Restaurant restoran = searchResto(namaRestoran); // try to find restaurant by its name
            if (restoran == null){ // if restauran not found, continue
                System.out.println("Restoran tidak terdaftar pada sistem.\n");
                continue;
            }
            int index = 0; // index to iterate
            for (Menu currentMenu: restoran.getMenu()){ // for loop every menu in restaurant's menu array list
                ++index; // index + 1 every loop
                menu += index + ". " + currentMenu.getNamaMakanan(); // add index and food name to menu
                menu += String.format(" %.0f\n", currentMenu.getHarga()); // add food cost to menu
            }
            break; // break while loop
        }
        System.out.print(menu); // print menu
    }


    public static void handleUpdateStatusPesanan(User userLoggedIn){
        /*
         * receive and validate orderID input
         * receive and validate status input
         * update order status
         */
        System.out.println("\n--------------Update Status Pesanan--------------");
        while (true){ // keep looping until all input are successfully validated
            if (isOrderHistoryEmpty(userLoggedIn)){ // if user's order history array list is empty, break
                System.out.println("Belum ada pesanan yang terdaftar. Mohon pesan terlebih dahulu!");
                break;
            }

            System.out.print("OrderID: ");
            String orderID = input.nextLine(); // receive orderID input

            Order selectedOrder = searchOrder(userLoggedIn, orderID); // try to find orderID in user's orderHistory arra list
            if (selectedOrder == null){ // if orderID is not found, continue
                System.out.println("Order ID tidak dapat ditemukan.\n");
                continue;
            }

            System.out.print("Status: ");
            String status = input.nextLine(); // receive status input
            if (status.toLowerCase().equals("selesai")){ // if status input equals to "selesai"
                if (selectedOrder.getOrderStatus().equals("Finished")){ // if order status is already "Finished", continue
                    System.out.println("Status pesanan dengan ID " + orderID + " tidak berhasil diupdate!\n");
                    continue;
                }
                selectedOrder.setOrderStatus(); // set order status as "Finished"
                System.out.print("Status pesanan dengan ID " + orderID + " berhasil diupdate!");
            }
            else{ // else if status input is not equal to "selesai", continue
                System.out.println("Status tidak valid!\n");
                continue;
            }
            break; // break while loop
        }
    }


    public static void handleTambahRestoran(){
        /*
         * method for admin to add new restaurant
         * add the newly created restaurant to restaurant array list
         */
        System.out.println("\n--------------Tambah Restoran--------------");
        while (true){ // keep looping until all inputs are successfully validated
            System.out.print("Nama: ");
            String namaRestoran = input.nextLine(); // receive restaurant name input
            if (namaRestoran.length() < 4){ // if restaurant name input length is less than 4 charactes, continue
                System.out.println("Nama restoran tidak valid!\n");
                continue;
            }
            if (searchResto(namaRestoran) != null){ // try to find restaurant by its name (if not found, continue)
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
                String inputMenu = input.nextLine(); // receive menu input
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
                catch (NumberFormatException nfe){ // if failed, set validation to false and break for loop
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
            System.out.print("Restoran " + namaRestoran + " berhasil terdaftar.");
            break; // break while loop
        }
    }


    public static void handleHapusRestoran(){
        /*
         * method for admin to remove existing restaurant from restaurant array list
         */
        System.out.println("\n--------------Hapus Restoran--------------");
        while (true){ // keep looping until restaurant name input is valid
            if (isRestoListEmpty()){ // if restaurant array list is empty, break
                System.out.println("Belum ada restoran yang terdaftar. Mohon tambahkan restoran baru ke sistem!");
                break;
            }
            
            System.out.print("Nama Restoran: ");
            String namaRestoran = input.nextLine(); // receive restaurant name input
            Restaurant resto = searchResto(namaRestoran); // try to find restaurant by it's name

            if (resto != null){ // if restaurant is found, remove restaurant from restaurant array list and break while loop
                restoList.remove(resto);
                System.out.print("Restoran berhasil dihapus.");
                break;
            }
            System.out.println("Restoran tidak terdaftar pada sistem.\n");
        }
    }


    public static Restaurant searchResto(String namaRestoran){
        /*
         * method to search for a restaurant in the restaurant array list
         * if restaurant is found, return Restaurant object
         * else, return null
         */
        if (isRestoListEmpty()) return null; // if restaurant array list is empty, return null
        for (Restaurant resto: restoList){ // for loop to search for the restaurant
            if (resto.getNamaRestoran().equalsIgnoreCase(namaRestoran)) return resto; // if found return Restaurant object
        }
        return null;
    }


    public static Menu searchMenu(Restaurant resto, String namaMakanan){
        /*
         * method to search for a food in the restaurant's menu list
         * if restaurant is found, return Menu object
         * else, return null
         */
        for (Menu menu: resto.getMenu()){ // for loop to search for the food
            if (menu.getNamaMakanan().equalsIgnoreCase(namaMakanan)) return menu; // if found return Menu object
        }
        return null;
    }


    public static Order searchOrder(User userLoggedIn, String orderID){
        /*
         * method to search for orderID in user's order history array list
         * if restaurant is found, return Order object
         * else, return null
         */
        if (isOrderHistoryEmpty(userLoggedIn)) return null; // if user's order history array list is empty, return null
        for (Order order: userLoggedIn.getOrderHistory()){ // for loop to search for the orderID
            if (order.getOrderID().equalsIgnoreCase(orderID)) return order; // if found return Order object
        }
        return null;
    }


        public static boolean isRestoListEmpty(){
        /*
         * check if resto array list is empty or not
         * if resto array list is empty, return true
         * else, return false
         */
        if (restoList == null || restoList.isEmpty()){ // if restaurant array list is empty, return true
            return true;
        }
        return false;
    }


    public static boolean isOrderHistoryEmpty(User userLoggedIn){
        /*
         * check if order history array list is empty or not
         * if user's order history array list is empty, return true
         * else, return false
         */
        if (userLoggedIn.getOrderHistory() == null || userLoggedIn.getOrderHistory().isEmpty()){ // if user's order history array list is empty, return null
            return true;
        }
        return false;
    }


    public static String generateOrderID(String namaRestoran, String tanggalOrder, String noTelepon) {
        /*
         * fucntion to generate orderID
         * return orderID
         */
        String orderID = "";
        
        orderID += String.format("%S", namaRestoran.substring(0, 4)); // append first 4 letter of restaurant name in uppercase
        orderID += tanggalOrder.replaceAll("/", ""); // append date without slashes
        orderID += calculateNoTelepon(noTelepon); // append calculated no telp
        orderID += calculateChecksum(orderID);  // append calculated checksum

        return orderID; // return generated orderID
    }


    public static String calculateNoTelepon(String noTelepon){
        /*
         * funtion to calculate no telp from given input
         */
        String result = "";
        int sum = 0;
        for (int i = 0; i < noTelepon.length(); i++){ // add all digits in noTelepon
            sum += (int)noTelepon.charAt(i) - 48;
        }
        if (sum % 100 < 10) result = "0"; // if sum % 100 less than 10, append "0" to result
        result += Integer.toString(sum % 100); // append the result of sum % 100 to result
        return result; // return result
    }


    public static String calculateChecksum(String orderID){
        /*
         * funtion to calculate checksum
         */
        String checksum = "";
        int oddSum = 0;
        int evenSum = 0;
        for (int i = 0; i < orderID.length(); i++){ // for loop all character in orderID
            int charInt = (int)orderID.charAt(i); // charInt contains ascii value of character[i] in orderID
            if (i % 2 == 0){ // calculate even sum
                if (charInt >= 48 && charInt <= 57){ // if character[i] is a digit
                    evenSum += charInt - 48; // substract by 48
                }
                else{ // if character[i] is an uppercase letter
                    evenSum += charInt - 55; // substract by 55
                }
            }
            else{ // calculate odd sum
                if (charInt >= 48 && charInt <= 57){ // if character[i] is a digit
                    oddSum += charInt - 48; // substract by 48
                }
                else{ // if character[i] is an uppercase letter
                    oddSum += charInt - 55; // substract by 55
                }
            }
        }
        if (evenSum % 36 >= 10){ // if evenSum%36 is greater or equal than 10
            checksum += Character.toString((char)(evenSum%36) + 55); // convert ascii value of (evenSum%36)+55 to its corresponding uppercase and append to checksum
        }
        else{// if evenSum%36 is less than 10
            checksum += Integer.toString(evenSum%36); // convert result of evenSum%36 to string and append to checksum
        }
        if (oddSum % 36 >= 10){ // if oddSum%36 is greater or equal than 10
            checksum += Character.toString((char)(oddSum%36) + 55); // convert ascii value of (oddSum%36)+55 to its corresponding uppercase and append to checksum
        }
        else{ // if oddSum%36 is less than 10
            checksum += Integer.toString(oddSum%36); // convert result of oddSum%36 to string and append to checksum
        }
        return checksum; // return calculated checksum
    }


    public static String calculateShippingCost(String lokasi){
        /*
         * funtion to calculateShippingCost
         * shipping cost is based on delivery location
         */
        String shippingCost = "";
        if (lokasi.equals("P")) shippingCost = "Rp 10.000";
        else if (lokasi.equals("U")) shippingCost = "Rp 20.000";
        else if (lokasi.equals("T")) shippingCost = "Rp 35.000";
        else if (lokasi.equals("S")) shippingCost = "Rp 40.000";
        else if (lokasi.equals("B")) shippingCost = "Rp 60.000";
        return shippingCost; // return shipping cost
    }


    public static Boolean valDate(String tanggalOrder){
        /*
         * function to validate date (in DD/MM/YYYY format)
         */
        boolean check = true;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try{
            formatter.parse(tanggalOrder);
        }
        catch(Exception e){ // if error occurred, check = false (date is not valid)
            check = false;
        }
        return check; // return validation result
    }


    public static void initUser(){
        /*
         * add users to user array list
         */
       userList = new ArrayList<User>();
       userList.add(new User("Thomas N", "9928765403", "thomas.n@gmail.com", "P", "Customer"));
       userList.add(new User("Sekar Andita", "089877658190", "dita.sekar@gmail.com", "B", "Customer"));
       userList.add(new User("Sofita Yasusa", "084789607222", "sofita.susa@gmail.com", "T", "Customer"));
       userList.add(new User("Dekdepe G", "080811236789", "ddp2.gampang@gmail.com", "S", "Customer"));
       userList.add(new User("Aurora Anum", "087788129043", "a.anum@gmail.com", "U", "Customer"));

       userList.add(new User("Admin", "123456789", "admin@gmail.com", "-", "Admin"));
       userList.add(new User("Admin Baik", "9123912308", "admin.b@gmail.com", "-", "Admin"));
    }


    public static void printHeader(){
        /*
         * depe food's header
         */
        System.out.println("\n>>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.out.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<\n");
    }


    public static void startMenu(){
        /*
         * depe food's start menu
         */
        System.out.println("Selamat datang di DepeFood!");
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Login");
        System.out.println("2. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }


    public static void menuAdmin(){
        /*
         * depe food's admin menu
         */
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Tambah Restoran");
        System.out.println("2. Hapus Restoran");
        System.out.println("3. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }


    public static void menuCustomer(){
        /*
         * depe food's customer menu
         */
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Cetak Bill");
        System.out.println("3. Lihat Menu");
        System.out.println("4. Update Status Pesanan");
        System.out.println("5. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    
}