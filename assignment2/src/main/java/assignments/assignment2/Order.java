package assignments.assignment2;
import java.util.ArrayList;

public class Order {
    private String orderID;
    private String tanggalOrder;
    private int ongkir;
    private Restaurant restoran;
    private ArrayList<Menu> items = new ArrayList<Menu>();
    private boolean orderFinished = false;

    public Order(String orderID, String tanggalOrder, int ongkir, Restaurant restoran, Menu[] items){
        // initialization
        this.orderID = orderID;
        this.tanggalOrder = tanggalOrder;
        this.ongkir = ongkir;
        this.restoran = restoran;
        for (Menu item: items){
            this.items.add(item);
        }
    }
    
    public String getOrderID(){
        // return orderID
        return orderID;
    }

    public String getTanggalOrder(){
        // return order date
        return tanggalOrder;
    }

    public int getOngkir(){
        // return shipping cost
        return ongkir;
    }

    public Restaurant getRestoran(){
        // return restaurant object
        return restoran;
    }

    public ArrayList<Menu> getItems(){
        // return items array list in menu
        return items;
    }

    public void setOrderStatus(){
        // set orderFinished to true
        this.orderFinished = true;
    }

    public String getOrderStatus(){
        // return order status
        if (orderFinished == true){
            return "Finished";
        }
        return "Not finished";
    }
}
