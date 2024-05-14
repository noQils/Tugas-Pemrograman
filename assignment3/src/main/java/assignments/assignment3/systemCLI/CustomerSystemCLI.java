package assignments.assignment3.systemCLI;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import assignments.assignment3.Order;
import assignments.assignment3.Menu;
import assignments.assignment3.Restaurant;
import assignments.assignment3.User;
import assignments.assignment3.MainMenu;
import assignments.assignment3.payment.*;

public class CustomerSystemCLI extends UserSystemCLI{
    private ArrayList<Restaurant> restoList;
    private User userLoggedIn;

    public boolean handleMenu(int choice){
        /*
         * handle menu for customer
         */
        userLoggedIn = MainMenu.getUserLoggedIn();
        restoList = MainMenu.getRestoList();
        switch(choice){
            case 1 -> handleBuatPesanan();
            case 2 -> handleCetakBill();
            case 3 -> handleLihatMenu();
            case 4 -> handleBayarBill();
            case 5 -> handleCekSaldo();
            case 6 -> {return false;}
            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
        }
        return true;
    }

    public void displayMenu() {
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Cetak Bill");
        System.out.println("3. Lihat Menu");
        System.out.println("4. Bayar Bill");
        System.out.println("5. Cek Saldo");
        System.out.println("6. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    protected void handleBuatPesanan(){
        /*
         * make a new order from the logged-in user (customer)
         */
        String orderID; // stores orderID input
        String namaRestoran; // stores restaurant name input
        String tanggalOrder; // stores date input
        Restaurant resto; // stores selected restaurant object
        Menu[] items; // stores list of items input
        System.out.println("--------------Buat Pesanan--------------");
        while (true){ // keep looping until all inputs are successfully validated
            System.out.print("Nama Restoran: ");
            namaRestoran = input.nextLine(); // receive restaurant name
            
            resto = searchResto(namaRestoran, restoList); // try to find the restaurant by its name
            if (resto == null){ // if restaurant not found, continue
                System.out.println("Restoran tidak terdaftar pada sistem.\n");
                continue;
            }

            System.out.print("Tanggal Pemesanan (DD/MM/YYYY): ");
            tanggalOrder = input.nextLine(); // receive order date
            if (!valDate(tanggalOrder)){ // call method valDate() to validate date input (if date is invalid, continue)
                System.out.println("Masukkan tanggal sesuai format (DD/MM/YYYY)!\n");
                continue;
            }

            System.out.print("Jumlah Pesanan: ");
            int jumlahPesanan = input.nextInt(); // receive amount of items
            input.nextLine(); // clear scanner
            if (jumlahPesanan < 1){ // if amount of items input is less than 1, continue
                System.out.println("Jumlah pesanan minimal 1!\n");
            }

            String[] pesananList = new String[jumlahPesanan]; // stores items list
            items = new Menu[jumlahPesanan]; // define items array with given size
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

            orderID = generateOrderID(namaRestoran, tanggalOrder, userLoggedIn.getNoTelepon()); // get order id
            break; // break while loop
        }
        int ongkir = Integer.parseInt(calculateShippingCost(userLoggedIn.getLokasi()).substring(3,9)
                    .replace(".", "")); // get shipping cost
        userLoggedIn.addOrder(new Order(orderID, tanggalOrder, ongkir, resto, items)); // add new order to user's order array list
        System.out.print("Pesanan dengan ID " + orderID + " diterima!");    
    }

    protected void handleCetakBill(){
        /*
         * receive and validate orderID
         * generate bill
         * print bill
         */
        if (userLoggedIn. getOrderHistory().isEmpty()){ // if user's order history array list is empty, print message and return
            System.out.println("Belum ada pesanan yang dibuat!");
            return;
        }
        System.out.println("--------------Cetak Bill--------------");
        System.out.println(generateBill(inputOrderID()));
    }

    protected void handleLihatMenu(){
        /*
         * receive and validate restaurant name
         * add all food and cost in menu to string 'menu'
         * print menu list
         */
        String menu = "Menu:\n"; // string to store menu list
        System.out.println("--------------Lihat Menu--------------");
        while (true){ // keep looping until restaurant name input is valid
            System.out.print("Nama Restoran: ");
            String namaRestoran = input.nextLine(); // receive restaurant name input
            Restaurant restoran = searchResto(namaRestoran, restoList); // try to find restaurant by its name
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


    protected void handleBayarBill(){
        /*
         * receive and validate orderID
         * generate bill
         * receive payment method
         * process payment
         * update user's saldo
         * add totalHarga to restaurant's saldo
         */
        System.out.println("--------------Bayar Bill--------------");
        if (userLoggedIn.getOrderHistory().isEmpty()){ // if user's order history array list is empty, print message and return
            System.out.println("Belum ada pesanan yang dibuat!");
            return;
        }
        Order selectedOrder = inputOrderID(); // get orderID
        if (selectedOrder.getOrderStatus().equals("Finished")){ // if order status is finished, print message and return
            System.out.println("Pesanan dengan ID ini sudah lunas!");
            return;
        }
        System.out.println(generateBill(selectedOrder)); // generate bill and print it
        System.out.println("\nOpsi Pembayaran: ");
        System.out.println("1. Credit Card");
        System.out.println("2. Debit");
        System.out.print("Pilihan Metode Pembayaran: ");
        int paymentMethod = input.nextInt(); // receive payment method input
        input.nextLine(); 
        DepeFoodPaymentSystem payment = userLoggedIn.getPaymentMethod(); // get payment method
        ArrayList<Menu> items = selectedOrder.getItems(); // store items of the order into items array list
        double hargaItem = 0; // define totalHarga as shipping cost
        double hargaOngkir = selectedOrder.getOngkir(); // get shipping cost
        for (Menu item : items) hargaItem += item.getHarga(); // calculate total cost
        long saldoUser = userLoggedIn.getSaldo(); // get user's saldo
        long newSaldo = 0;
        long totalHarga = (long) (hargaItem + hargaOngkir); // define totalHarga as long of hargaItem
        if (paymentMethod == 1){
            if (payment.paymentType().equals("debit")){ // if payment method is debit card print fail message and return
                System.out.println("User belum memiliki metode pembayaran ini!");
                return;
            }
            CreditCardPayment creditPayment = (CreditCardPayment) payment; // cast payment to CreditCardPayment
            System.out.println("this is credit");
            newSaldo = payment.processPayment((long) totalHarga, saldoUser); // process payment
            System.out.println("Berhasil membayar bill sebesar Rp " + (long) totalHarga + 
                                " dengan biaya transaksi sebesar Rp " + creditPayment.countTransactionFee((long) totalHarga));
        }
        else if (paymentMethod == 2){ // if payment method is debit card
            if (payment.paymentType().equals("credit")){ // if payment method is credit card print fail message and return
                System.out.println("User belum memiliki metode pembayaran ini!");
                return;
            }
            DebitPayment debitPayment = (DebitPayment) payment; // cast payment to DebitPayment
            if (totalHarga < debitPayment.getMinPrice()){ // if totalHarga is less than minPrice, print message and return
                System.out.println("Jumlah pesanan < 50000 mohon menggunakan metode pembayaran yang lain");
                return;
            }
            if (saldoUser < (long) (totalHarga)){ // if saldoUser is less than totalHarga, print message and return
                System.out.println("Saldo tidak mencukupi mohon menggunakan metode pembayaran yang lain");
                return;
            }
            newSaldo = payment.processPayment((long) totalHarga, saldoUser); // process payment
            System.out.println("Berhasil membayar bill sebesar Rp " + (long) totalHarga);
        }
        else {
            System.out.println("Perintah tidak diketahui!");
            return;
        }
        userLoggedIn.setSaldo(newSaldo); // set new saldo
        selectedOrder.getRestoran().addSaldo((long) hargaItem); // add totalHarga to restaurant's saldo
        selectedOrder.setOrderStatus(); // set order status to finished
    }

    protected void handleCekSaldo(){
        /*
         * print user's saldo
         */
        System.out.println("\nSisa saldo sebesar: Rp " + userLoggedIn.getSaldo());
    }
    protected Order inputOrderID(){
        /*
         * receive and validate orderID input
         * return Order object
         */
        while (true){
            System.out.print("Masukkan Order ID: ");
            String orderID = input.nextLine(); // receive orderID input

            Order selectedOrder = searchOrder(userLoggedIn, orderID); // try to find orderID in user's order array list
            if (selectedOrder == null){ // if order is not found, continue
                System.out.println("Order ID tidak ditemukan.\n");
                continue;
            }

            return selectedOrder; // return orderID
        }
    }

    protected String generateOrderID(String namaRestoran, String tanggalOrder, String noTelepon) {
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

    protected String generateBill(Order selectedOrder){
        /*
         * receive and validate orderID input
         * add all information about order to string 'bill'
         */
        String bill = "\nBill:"; // string bill to make the bill
        while (true){ // keep looping until orderID input is valid
            String orderID = selectedOrder.getOrderID(); // get orderID
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
        return bill;
    }

    protected Menu searchMenu(Restaurant resto, String namaMakanan){
        /*
         * method to search for a food in the restaurant's menu list
         * if restaurant is found, return Menu object
         * else, return null
         */
        namaMakanan = namaMakanan.toLowerCase();
        for (Menu menu: resto.getMenu()){ // for loop to search for the food
            if (menu.getNamaMakanan().toLowerCase().equals(namaMakanan)) return menu; // if found return Menu object
        }
        return null;
    }
    
    protected Order searchOrder(User userLoggedIn, String orderID){
        /*
         * method to search for orderID in user's order history array list
         * if restaurant is found, return Order object
         * else, return null
         */
        if (userLoggedIn.getOrderHistory() == null || userLoggedIn.getOrderHistory().isEmpty()){ // if user's order history array list is empty, return null
            return null;
        }
        orderID = orderID.toLowerCase();
        for (Order order: userLoggedIn.getOrderHistory()){ // for loop to search for the orderID
            if (order.getOrderID().toLowerCase().equals(orderID)) return order; // if found return Order object
        }
        return null;
    }

    protected Boolean valDate(String tanggalOrder){
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

    protected String calculateNoTelepon(String noTelepon){
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

    protected String calculateChecksum(String orderID){
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

    protected String calculateShippingCost(String lokasi){
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
}
