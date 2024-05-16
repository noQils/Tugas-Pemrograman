package assignments.assignment3;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import assignments.assignment1.OrderGenerator;
import assignments.assignment3.payment.CreditCardPayment;
import assignments.assignment3.payment.DebitPayment;
import assignments.assignment3.payment.DepeFoodPaymentSystem;
import assignments.assignment3.systemCLI.AdminSystemCLI;
import assignments.assignment3.systemCLI.CustomerSystemCLI;
import assignments.assignment3.systemCLI.UserSystemCLI;

public class DepeFood {
    private static ArrayList<User> userList;
    private static List<Restaurant> restoList = new ArrayList<>();
    private static User userLoggedIn;

    public static User getUserLoggedIn() {
        return userLoggedIn;
    }

    public static String getUserLoggedInRole(){
        return userLoggedIn.getRole();
    }

    public static void initUser() {
        userList = new ArrayList<>();

        userList.add(
                new User("Thomas N", "9928765403", "thomas.n@gmail.com", "P", "Customer", new DebitPayment(), 500000));
        userList.add(new User("Sekar Andita", "089877658190", "dita.sekar@gmail.com", "B", "Customer",
                new CreditCardPayment(), 2000000));
        userList.add(new User("Sofita Yasusa", "084789607222", "sofita.susa@gmail.com", "T", "Customer",
                new DebitPayment(), 750000));
        userList.add(new User("Dekdepe G", "080811236789", "ddp2.gampang@gmail.com", "S", "Customer",
                new CreditCardPayment(), 1800000));
        userList.add(new User("Aurora Anum", "087788129043", "a.anum@gmail.com", "U", "Customer", new DebitPayment(),
                650000));

        userList.add(new User("Admin", "123456789", "admin@gmail.com", "-", "Admin", new CreditCardPayment(), 0));
        userList.add(
                new User("Admin Baik", "9123912308", "admin.b@gmail.com", "-", "Admin", new CreditCardPayment(), 0));
    }

    public static User getUser(String nama, String nomorTelepon) {

        for (User user : userList) {
            if (user.getNamaUser().equals(nama.trim()) && user.getNoTelepon().equals(nomorTelepon.trim())) {
                return user;
            }
        }
        return null;
    }

    public static User handleLogin(String nama, String nomorTelepon) {
        User user = getUser(nama, nomorTelepon);

        if (user == null) {
            System.out.println("Pengguna dengan data tersebut tidak ditemukan!");
            return null;
        }

        userLoggedIn = user;
        return user;
    }

    public static void handleTambahRestoran(String nama) {
        String nameValidation = getValidRestaurantName(nama);
        Restaurant restaurant = null;
        if (nameValidation.equals(nama)){
            restaurant = findRestaurant(nama);
            if (restaurant == null){
                restaurant = new Restaurant(nama);
                restoList.add(restaurant);
                System.out.print("Restaurant " + restaurant.getNamaRestoran() + " Berhasil terdaftar.");
            }
            else {
                System.out.print("Restaurant " + restaurant.getNamaRestoran() + " sudah terdaftar.");
            }
        }
        else {
            System.out.print(nameValidation);
        }
    }

    public static String getValidRestaurantName(String inputName) {
        String name = "";
        boolean isRestaurantNameValid = false;
    
        while (!isRestaurantNameValid) {
            boolean isRestaurantExist = restoList.stream()
                    .anyMatch(restoran -> restoran.getNamaRestoran().toLowerCase().equals(inputName.toLowerCase()));
            boolean isRestaurantNameLengthValid = inputName.length() >= 4;
    
            if (isRestaurantExist) {
                return String.format("Restaurant with the name %s already exists. Please enter a different name!", inputName);
            } else if (!isRestaurantNameLengthValid) {
               return "Restaurant name needs to be at least 4 characters!";
            } else {
                name = inputName;
                isRestaurantNameValid = true;
            }
        }
        return name;
    }

    public static Restaurant findRestaurant(String nama) {
        for (Restaurant resto : restoList) {
            if (resto.getNamaRestoran().equalsIgnoreCase(nama)) {
                return resto;
            }
        }
        return null; 
    }

    public static void addRestoList(Restaurant restaurant){
        restoList.add(restaurant);
    }

    public static List<Restaurant> getRestoList() {
        return restoList;
    }
    
    public static void handleTambahMenuRestoran(Restaurant restoran, String namaMakanan, double harga){
        if (getMenuByName(restoran, namaMakanan) != null){
            System.out.println("Menu sudah ada di restoran!");
        } 
        else {
            Menu menu = new Menu(namaMakanan, harga);
            addMenu(restoran, menu);
            System.out.println("Menu berhasil ditambahkan!");
        }
    }

    public static void addMenu(Restaurant restaurant,Menu menu){
        restaurant.addMenu(menu);
    }

    public static Menu getMenuByName(Restaurant restaurant, String nama){
        for (Menu menu : restaurant.getMenu()){
            if (menu.getNamaMakanan().equalsIgnoreCase(nama)){
                return menu;
            }
        }
        return null;
    }

    public static Restaurant getRestaurantByName(String name) {
        Optional<Restaurant> restaurantMatched = restoList.stream()
                .filter(restoran -> restoran.getNamaRestoran().equalsIgnoreCase(name)).findFirst();
        if (restaurantMatched.isPresent()) {
            return restaurantMatched.get();
        }
        return null;
    }

    public static String handleBuatPesanan(String namaRestoran, String tanggalPemesanan, int jumlahPesanan, List<String> listMenuPesananRequest) {
        System.out.println("--------------Buat Pesanan----------------");
    
        Restaurant restaurant = getRestaurantByName(namaRestoran);
        if (restaurant == null) {
            System.out.println("Restoran tidak terdaftar pada sistem.\n");
            return null;
        }
    
        if (!OrderGenerator.valDate(tanggalPemesanan)) {
            System.out.println("Masukkan tanggal sesuai format (DD/MM/YYYY)");
            return null;
        }
    
        if (!validateRequestPesanan(restaurant, listMenuPesananRequest)) {
            System.out.println("Mohon memesan menu yang tersedia di Restoran!");
            return null;
        }
    
        Order order = new Order(
                OrderGenerator.generateOrderID(namaRestoran, tanggalPemesanan, userLoggedIn.getNoTelepon()),
                tanggalPemesanan,
                calculateShippingCost(userLoggedIn.getLokasi()),
                restaurant,
                getMenuRequest(restaurant, listMenuPesananRequest));
    
        System.out.printf("Pesanan dengan ID %s diterima!", order.getOrderID());
        userLoggedIn.addOrder(order);
        return order.getOrderID();
    }

    public static void handleBayarBill(String orderId, String paymentOption) {
        while (true) {
            Order order = getOrderOrNull(userLoggedIn, orderId);

            if (order == null) {
                System.out.println("Order ID tidak dapat ditemukan.\n");
                continue;
            }

            if (order.getOrderStatus().equals("Finished")) {
                System.out.println("Pesanan dengan ID ini sudah lunas!\n");
                return;
            }

            System.out.print("Pilihan Metode Pembayaran: ");

            if (!paymentOption.equals("Credit Card") && !paymentOption.equals("Debit")) {
                System.out.println("Pilihan tidak valid, silakan coba kembali\n");
                continue;
            }

            DepeFoodPaymentSystem paymentSystem = userLoggedIn.getPaymentMethod();

            boolean isCreditCard = paymentSystem instanceof CreditCardPayment;

            if ((isCreditCard && paymentOption.equals("Debit")) || (!isCreditCard && paymentOption.equals("Credit Card"))) {
                System.out.println("User belum memiliki metode pembayaran ini!\n");
                continue;
            }

            long amountToPay = 0;
            long totalHarga = 0;

            for (Menu item : order.getItems()){
                totalHarga += item.getHarga();
            }

            try {
                amountToPay = paymentSystem.processPayment(totalHarga, userLoggedIn.getSaldo());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println();
                continue;
            }

            long saldoLeft = userLoggedIn.getSaldo() - amountToPay;

            userLoggedIn.setSaldo(saldoLeft);
            handleUpdateStatusPesanan(order);

            DecimalFormat decimalFormat = new DecimalFormat();
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator('.');
            decimalFormat.setDecimalFormatSymbols(symbols);

            System.out.printf("Berhasil Membayar Bill sebesar Rp %s", decimalFormat.format(amountToPay));

            return;
        }
    }

    public static Order getOrderOrNull(User user, String orderId) {
        for (Order order : user.getOrderHistory()) {
            if (order.getOrderID().equals(orderId)) {
                return order;
            }
        }
        return null;
    }

    public static boolean validateRequestPesanan(Restaurant restaurant, List<String> listMenuPesananRequest) {
        return listMenuPesananRequest.stream().allMatch(
                pesanan -> restaurant.getMenu().stream().anyMatch(menu -> menu.getNamaMakanan().equals(pesanan)));
    }

    public static Menu[] getMenuRequest(Restaurant restaurant, List<String> listMenuPesananRequest) {
        Menu[] menu = new Menu[listMenuPesananRequest.size()];
        for (int i = 0; i < menu.length; i++) {
            for (Menu existMenu : restaurant.getMenu()) {
                if (existMenu.getNamaMakanan().equals(listMenuPesananRequest.get(i))) {
                    menu[i] = existMenu;
                }
            }
        }
        return menu;
    }

    public static Order findUserOrderById(String orderId) {
        List<Order> orderHistory = userLoggedIn.getOrderHistory();
        
        for (Order order : orderHistory) {
            if (order.getOrderID() == orderId) {
                return order; 
            }   
        }
        return null; 
    }

    public static void handleUpdateStatusPesanan(Order order) {
        order.setOrderStatus();
    }
    
    public static void setPenggunaLoggedIn(User user){
        userLoggedIn = user;
    }

    public static int calculateShippingCost(String lokasi){
        /*
         * funtion to calculateShippingCost
         * shipping cost is based on delivery location
         */
        int shippingCost = 0;
        if (lokasi.equals("P")) shippingCost = 10000;
        else if (lokasi.equals("U")) shippingCost = 20000;
        else if (lokasi.equals("T")) shippingCost = 35000;
        else if (lokasi.equals("S")) shippingCost = 40000;
        else if (lokasi.equals("B")) shippingCost = 60000;
        return shippingCost; // return shipping cost
    }




    

    







   
}
