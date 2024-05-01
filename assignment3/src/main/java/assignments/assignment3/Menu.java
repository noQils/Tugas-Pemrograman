package assignments.assignment3;

public class Menu {
    private String namaMakanan;
    private double harga;

    public Menu(String namaMakanan, double harga){
        // initialization
        this.namaMakanan = namaMakanan;
        this.harga = harga;
    }

    public String getNamaMakanan(){
        // return food name
        return namaMakanan;
    }

    public double getHarga(){
        // return food cost
        return harga;
    }
}
