package assignments.assignment4.components;

import assignments.assignment3.Menu;
import assignments.assignment3.Order;
import assignments.assignment3.User;
import assignments.assignment3.systemCLI.CustomerSystemCLI;
import assignments.assignment4.MainApp;
import assignments.assignment4.page.MemberMenu;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;

public class BillPrinter {
    private Stage stage;
    private MainApp mainApp;
    private User user;

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
        billLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        Label orderIDLabel = new Label("Order ID: " + order.getOrderID());
        orderIDLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        Label dateLabel = new Label("Order Date: " + order.getTanggalOrder());
        dateLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        Label restaurantLabel = new Label("Restaurant: " + order.getRestoran().getNamaRestoran());
        restaurantLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        Label shippingLabel = new Label("Shipping Location: " + user.getLokasi());
        shippingLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        Label orderStatusLabel = new Label("Order Status: " + order.getOrderStatus());
        orderStatusLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        Label itemsLabel = new Label("Items:");
        itemsLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        long totalPrice = order.getOngkir();
        ArrayList<Label> itemsLabelList = new ArrayList<>();
        for (Menu item : order.getItems()){
            totalPrice += item.getHarga();
            itemsLabelList.add(new Label("- " + item.getNamaMakanan() + " Rp " + String.format("%.0f", item.getHarga())));
            itemsLabelList.forEach(label -> {
                label.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");
            });
        }

        Label shippingCostLabel = new Label("Shipping Cost: Rp " + order.getOngkir());
        shippingCostLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        Label totalPriceLabel = new Label("Total Price: Rp " + totalPrice);
        totalPriceLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        // Create return button
        Button returnButton = new Button("Return");
        returnButton.setStyle("-fx-font: 14 arial; -fx-text-fill: white; -fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff9966, #ff5e62)");
        returnButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> returnButton.setEffect(new DropShadow()));
        returnButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> returnButton.setEffect(null)); 
        returnButton.setOnAction(e -> mainApp.setScene(printBillForm));

        menuLayout.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #2b5876, #524376)");
        menuLayout.getChildren().addAll(billLabel, orderIDLabel, dateLabel, restaurantLabel, shippingLabel, orderStatusLabel, itemsLabel);
        for (Label itemLabel : itemsLabelList) menuLayout.getChildren().add(itemLabel);
        menuLayout.getChildren().addAll(shippingCostLabel, totalPriceLabel, returnButton);
        
        return new Scene(menuLayout, 500, 500);
    }


    /**
     * Method to validate orderID input by user
     * 
     * @param orderID
     * @return order object if order is found
     * @return null if else
     */
    private Order validateOrderID(String orderID){
        return CustomerSystemCLI.searchOrder(user, orderID);
    }

    /**
     * Method to get bill printer scene
     * Validate orderID first
     * 
     * @param orderID
     * @param printBillForm
     * @return bill printer scene if orderID is found
     * @return null if else
     */
    public Scene getScene(String orderID, Scene printBillForm) {
        Order order = validateOrderID(orderID);
        if (order == null){
            MemberMenu.showAlert("Error", "Invalid Order ID", "Please input a valid order ID", AlertType.ERROR);
            return null;
        }
        else return this.createBillPrinterForm(order, printBillForm);
    }
}
