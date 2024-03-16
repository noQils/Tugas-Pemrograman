package assignments.assignment2;
import java.util.ArrayList;

public class Restaurant {
    private String namaRestoran;
    private ArrayList<Menu> menu = new ArrayList<Menu>();

    public Restaurant(String namaRestoran){
        // initialization
        this.namaRestoran = namaRestoran;
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
}