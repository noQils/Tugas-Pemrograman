package assignments.assignment4.components;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import assignments.assignment3.Menu;
import assignments.assignment3.Order;
import assignments.assignment3.User;
import assignments.assignment4.MainApp;

public class BillPrinter {
    private Stage stage;
    private MainApp mainApp;
    private User user;
    private static Map<String, Scene> billPrinterScenes = new HashMap<>();

    public BillPrinter(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user;
    }

    /**
        * Creates a bill printer form scene.
        *
        * @param order The order object containing the order details.
        * @param printBillForm The scene to return to when the return button is clicked.
        * @return The created bill printer form scene.
        */
    private Scene createBillPrinterForm(Order order, Scene printBillForm){
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        Label billLabel = new Label("Bill");
        billLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white;");

        Label orderIDLabel = new Label("Order ID: " + order.getOrderID());
        orderIDLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white;");

        Label dateLabel = new Label("Order Date: " + order.getTanggalOrder());
        dateLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white;");

        Label restaurantLabel = new Label("Restaurant: " + order.getRestoran().getNamaRestoran());
        restaurantLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white;");

        Label shippingLabel = new Label("Shipping Location: " + user.getLokasi());
        shippingLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white;");

        Label orderStatusLabel = new Label("Order Status: " + order.getOrderStatus());
        orderStatusLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white;");

        Label itemsLabel = new Label("Items:");
        itemsLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white;");

        long totalPrice = order.getOngkir();
        ArrayList<Label> itemsLabelList = new ArrayList<>();
        for (Menu item : order.getItems()){
            totalPrice += item.getHarga();
            itemsLabelList.add(new Label("- " + item.getNamaMakanan() + " Rp " + String.format("%.0f", item.getHarga())));
            itemsLabelList.forEach(label -> {
                label.setStyle("-fx-font: 15 arial; -fx-text-fill: white;");
            });
        }

        Label shippingCostLabel = new Label("Shipping Cost: Rp " + order.getOngkir());
        shippingCostLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white;");

        Label totalPriceLabel = new Label("Total Price: Rp " + totalPrice);
        totalPriceLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white;");

        // Create return button
        Button returnButton = new Button("Return");
        returnButton.setStyle("-fx-font: 14 arial; -fx-text-fill: black;");
        returnButton.setOnAction(e -> mainApp.setScene(printBillForm));

        menuLayout.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #076585, white)");
        menuLayout.getChildren().addAll(billLabel, orderIDLabel, dateLabel, restaurantLabel, shippingLabel, orderStatusLabel, itemsLabel);
        for (Label itemLabel : itemsLabelList) menuLayout.getChildren().add(itemLabel);
        menuLayout.getChildren().addAll(shippingCostLabel, totalPriceLabel, returnButton);
        return new Scene(menuLayout, 500, 500);
    }

    /**
     * Adds a bill printer scene to the collection of scenes.
     *
     * @param orderID the ID of the order
     * @param scene the scene to be added
     */
    public static void addBillPrinterScene(String orderID, Scene scene){
        billPrinterScenes.put(orderID, scene);
    }

    /**
     * Represents a scene in a graphical user interface.
     * A scene is a container for all the visual elements that make up a user interface.
     */
    public static Scene checkBillPrinterScene(Order order){
        return billPrinterScenes.get(order.getOrderID());
    }

    public Scene getScene(Order order, Scene printBillForm) {
        return this.createBillPrinterForm(order, printBillForm);
    }
}
