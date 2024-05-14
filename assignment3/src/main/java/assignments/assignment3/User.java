package assignments.assignment3;

import assignments.assignment3.payment.DepeFoodPaymentSystem;
import java.util.ArrayList;

public class User {
    private String namaUser;
    private String noTelepon;
    private String email;
    private String lokasi;
    private String role;
    private ArrayList<Order> orderHistory = new ArrayList<Order>();
    private DepeFoodPaymentSystem paymentMethod;
    private long saldo;
    
    public User(String namaUser, String noTelepon, String email, String lokasi, String role, DepeFoodPaymentSystem paymentMethod, long saldo){
        // initialization
        this.namaUser = namaUser;
        this.noTelepon = noTelepon;
        this.email = email;
        this.lokasi = lokasi;
        this.role = role;
        this.paymentMethod = paymentMethod;
        this.saldo = saldo;
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

    public DepeFoodPaymentSystem getPaymentMethod(){
        // return payment object
        return paymentMethod;
    }

    public void setSaldo(long saldo){
        // set saldo
        this.saldo = saldo;
    }

    public long getSaldo(){
        // return saldo
        return saldo;
    }
}
