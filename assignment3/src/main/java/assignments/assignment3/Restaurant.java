package assignments.assignment3;

import java.util.ArrayList;

public class Restaurant {
    private String namaRestoran;
    private ArrayList<Menu> menu = new ArrayList<Menu>();
    private long saldo;

    public Restaurant(String namaRestoran){
        // initialization
        this.namaRestoran = namaRestoran;
        this.saldo = 0;
    }
    
    public String getNamaRestoran(){
        // return restaurant name
        return namaRestoran;
    }

    public void addMenu(Menu menuBaru){
        // add new menu to menu array list
        this.menu.add(menuBaru);
    }

    public ArrayList<Menu> getMenu(){
        // return menu array list
        return menu;
    }

    public void addSaldo(long saldo){
        // set saldo to 0
        this.saldo += saldo;
    } 
}