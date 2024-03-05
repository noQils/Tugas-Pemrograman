package assignments.assignment1;
import java.time.format.DateTimeFormatter; // import DateTimeFormatter
import java.util.Scanner; // import java scanner


public class OrderGenerator {
    private static final Scanner input = new Scanner(System.in); // create scanner variable called "input"

    public static void showMenu(int num){
        /*
         * function to print logo and menu list
         */
        if (num == 0){ // if num == 0, print logo
            System.out.println(">>=======================================<<");
            System.out.println("|| ___                 ___             _ ||");
            System.out.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
            System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
            System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
            System.out.println("||          |_|                          ||");
            System.out.println(">>=======================================<<");
            System.out.println();
        }
        else{ // else, print menu list
            System.out.println("Pilih menu:");
            System.out.println("1. Generate Order ID");
            System.out.println("2. Generate Bill");
            System.out.println("3. Keluar");
            System.out.println("-------------------------------");
        }
    }


    public static String orderInput(){
        /*
         * function to receive order input
         * and validate the inputs
         */
        String namaRestoran;
        String tanggalOrder;
        String noTelepon;

        while (true){ // while loop keep looping until all inputs are validated successfuly
            System.out.println();
            System.out.print("Nama Restoran: ");
            namaRestoran = input.nextLine(); // input restaurant name
            if (namaRestoran.length() < 4){ // if restaurant name length is less than 4 characters, validation failed
                System.out.println("Nama Restoran tidak valid!");
                continue;
            }

            System.out.print("Tanggal pemesanan: ");
            tanggalOrder = input.nextLine(); // input order date in DD/MM/YYYY format
            if (valDate(tanggalOrder) == false){ // if date input is not valid (check usint funtion valDate()), validation failed
                System.out.println("Tanggal pemesanan dalam format DD/MM/YYYY!");
                continue;
            }
            
            int validation = 1;
            System.out.print("No. Telepon: ");
            noTelepon = input.nextLine(); // input telephone number
            for (int i = 0; i < noTelepon.length(); i++){ // for loop every character in "noTelepon" check if there is any non digit characters
                if ((int)noTelepon.charAt(i) < 48 || (int)noTelepon.charAt(i) > 57){ // if there is a non digit character, validation failed
                    System.out.println("Harap masukkan nomor telepon dalam bentuk bilang bulat positif!");
                    validation = 0; // if validation failed, validation = 0
                    break; // break loop
                }
            }
            if (validation == 0) continue; // if validation == 0, continue input
            break; // break if all input are successfully validated
        }
        return generateOrderID(namaRestoran, tanggalOrder, noTelepon); // call "generateOrderID" funtion and return value
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


    public static String generateBill(String orderID, String lokasi){
        /*
         * funtion to generate bill
         */
        String bill = "";
        String tanggalOrder = orderID.substring(4, 6)+"/" // tanggalOrder contains date stated in orderID (in DD/MM/YYYY format)
                            +orderID.substring(6, 8)+"/"
                            +orderID.substring(8, 12);

        bill += "Bill:\n"; // append "Bill:"
        bill += "Order ID: " + orderID + "\n"; // append orderID
        bill += "Tanggal Pemesanan: " + tanggalOrder + "\n"; // append order date
        bill += "Lokasi Pemesenan: " + lokasi + "\n"; // append delivery location
        bill += "Biaya Ongkos Kirim: " + calculateShippingCost(lokasi); // apppend shipping cost

        return bill; // return generated bill
    }


    public static String billInput(){
        /*
         * funtion to receive bill input
         */
        String orderID;
        String lokasi;
        String tanggalOrder;

        while (true){ // while loop keep looping until all inputs are validated successfuly
            System.out.println();
            System.out.print("Order ID: ");
            orderID = input.nextLine(); // input orderID
            if (orderID.length() != 16){ // if orderID lengt is less than 16, validation failed
                System.out.println("Order ID minimal 16 karakter");
                continue;
            }
            if (!calculateChecksum(orderID.substring(0, 14)).equals(orderID.substring(14))){ // if the checksum of orderID is not valid, validation failed
                System.out.println("Silahkan masukkan order ID yang valid!");
                continue;
            }

            int validation = 1;
            String kodeRestoran = orderID.substring(0, 4); // first 4 characters in orderID
            for (int i = 0; i < kodeRestoran.length(); i++){ // for loop every character in kodeRestoran
                if ((int)kodeRestoran.charAt(i) >= 97 && (int)kodeRestoran.charAt(i) <= 122){ // if there is a lowercase letter, validation failed
                    System.out.println("Silahkan masukkan order ID yang valid!");
                    validation = 0; // validation = 0
                    break; // break loop
                }
            }
            if (validation == 0) continue; // if validation == 0, validation failed

            tanggalOrder = orderID.substring(4, 6)+"/" // tanggalOrder contains date stated in orderID (in DD/MM/YYYY format)
                            +orderID.substring(6, 8)+"/"
                            +orderID.substring(8, 12);
            if (valDate(tanggalOrder) == false){ // if the date format is not correct, validation failed
                System.out.println("Silahkan masukkan order ID yang valid!");
                continue;
            }

            System.out.print("Lokasi Pengiriman: ");
            lokasi = input.nextLine(); // input delivery location
            lokasi = lokasi.toUpperCase();
            if (lokasi.equals("P")) break; // if location is listed in lab module, break loop
            else if (lokasi.equals("U")) break; // otherwise validation failed and continue loop
            else if (lokasi.equals("T")) break;
            else if (lokasi.equals("S")) break;
            else if (lokasi.equals("B")) break;
            System.out.println("Harap masukkan lokasi pengiriman yang ada pada jangkauan!");
        }
        System.out.println();
        return generateBill(orderID, lokasi); // return generated bill
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


    public static void main(String[] args) {
        /*
         * main funtion
         * receive command input from menu list
         */
        showMenu(0); // print depe food logo
        
        while (true){ // while loop keep running until program exited
            showMenu(1); // print menu list
            System.out.print("Pilihan menu: ");

            String pilihan = input.nextLine(); // input command

            if (pilihan.equals("1")){ // if command input == 1, generate order
                System.out.println("Order id " + orderInput() + " diterima!");
                System.out.println("-------------------------------");
            }
            else if (pilihan.equals("2")){ // if command input == 2, generate bill
                System.out.println(billInput());
                System.out.println("-------------------------------");
            }
            else if (pilihan.equals("3")){ // if command input == 3, exit program
                System.out.println("Terima kasih telah menggunakan DepeFood!");
                break;
            }
            else{ // if command input is unknown
                System.out.println("Pilihan menu tidak diketahui, silahkan coba lagi!");
                System.out.println("-------------------------------");
            }
        }
        input.close(); // close scanner
    }

    
}
