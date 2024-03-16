package assignments.assignment2;
import java.util.ArrayList;

public class User {
    private String namaUser;
    private String noTelepon;
    private String email;
    private String lokasi;
    private String role;
    private ArrayList<Order> orderHistory = new ArrayList<Order>();
    
    public User(String namaUser, String noTelepon, String email, String lokasi, String role){
        // initialization
        this.namaUser = namaUser;
        this.noTelepon = noTelepon;
        this.email = email;
        this.lokasi = lokasi;
        this.role = role;
    }
    
    public String getNamaUser(){
        // return user name
        return namaUser;
    }

    public String getNoTelepon(){
        // return telephone number
        return noTelepon;
    }

    public String getEmail(){
        // return email
        return email;
    }

    public String getLokasi(){
        // return location
        return lokasi;
    }

    public String getRole(){
        // return user role
        return role;
    }

    public void addOrder(Order orderBaru){
        // add new order to order history array list
        this.orderHistory.add(orderBaru);
    }

    public ArrayList<Order> getOrderHistory(){
        // return order history array list
        return orderHistory;
    }
}
