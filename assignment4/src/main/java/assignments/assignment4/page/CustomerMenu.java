package assignments.assignment4.page;

import assignments.assignment1.OrderGenerator;
import assignments.assignment3.Order;
import assignments.assignment3.Menu;
import assignments.assignment3.Restaurant;
import assignments.assignment3.User;
import assignments.assignment3.payment.*;
import assignments.assignment3.systemCLI.UserSystemCLI;
import assignments.assignment3.systemCLI.CustomerSystemCLI;
import assignments.assignment4.MainApp;
import assignments.assignment4.components.BillPrinter;

import javafx.collections.ArrayChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerMenu extends MemberMenu{
    private Stage stage;
    private Scene scene;
    private Scene makeOrderScene;
    private Scene viewOrderHistoryScene;
    private Scene printBillScene;
    private Scene payBillScene;
    private Scene checkBalanceScene;
    private BillPrinter billPrinter; // Instance of BillPrinter
    private MainApp mainApp;
    private User user;


    public CustomerMenu(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user; // Store the user
        this.scene = createBaseMenu();
        this.makeOrderScene = createMakeOrderForm();
        this.viewOrderHistoryScene = createViewOrderHistory();
        this.billPrinter = new BillPrinter(stage, mainApp, this.user);  // Pass user to BillPrinter constructor
        this.printBillScene = createBillPrinter();
        this.payBillScene = createBillPaymentForm();
        this.checkBalanceScene = createCheckBalanceScene();

        addMenuSceneToMap();
    }

    /**
     * Create base menu scene for customer
     * 
     * @return scene for customer menu
     */
    @Override
    public Scene createBaseMenu() {
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        // Create make order button
        Button makeOrderButton = new Button("Make New Order");
        makeOrderButton.setMinWidth(150);
        makeOrderButton.setStyle("-fx-font: 14 arial; -fx-text-fill: white; -fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff9966, #ff5e62)");
        makeOrderButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> makeOrderButton.setEffect(new DropShadow()));
        makeOrderButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> makeOrderButton.setEffect(null)); 
        makeOrderButton.setOnAction(e -> {
            this.makeOrderScene = createMakeOrderForm();
            stage.setScene(this.makeOrderScene);
        });

        // Create view order history button
        Button viewOrderHistoryButton = new Button("Order History");
        viewOrderHistoryButton.setMinWidth(150);
        viewOrderHistoryButton.setStyle("-fx-font: 14 arial; -fx-text-fill: white; -fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff9966, #ff5e62)");
        viewOrderHistoryButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> viewOrderHistoryButton.setEffect(new DropShadow()));
        viewOrderHistoryButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> viewOrderHistoryButton.setEffect(null)); 
        viewOrderHistoryButton.setOnAction(e -> {
            this.viewOrderHistoryScene = createViewOrderHistory();
            stage.setScene(this.viewOrderHistoryScene);
        });

        // Create print bill button
        Button printBillButton = new Button("Print Bill");
        printBillButton.setMinWidth(150);
        printBillButton.setStyle("-fx-font: 14 arial; -fx-text-fill: white; -fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff9966, #ff5e62)");
        printBillButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> printBillButton.setEffect(new DropShadow()));
        printBillButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> printBillButton.setEffect(null)); 
        printBillButton.setOnAction(e -> {
            stage.setScene(this.printBillScene);
        });

        // Create pay bill button
        Button payButton = new Button("Pay Bill");
        payButton.setMinWidth(150);
        payButton.setStyle("-fx-font: 14 arial; -fx-text-fill: white; -fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff9966, #ff5e62)");
        payButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> payButton.setEffect(new DropShadow()));
        payButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> payButton.setEffect(null)); 
        payButton.setOnAction(e -> {
            this.payBillScene = createBillPaymentForm();
            stage.setScene(this.payBillScene);
        });

        // Create check balance button
        Button checkBalanceButton = new Button("Check Balance");
        checkBalanceButton.setMinWidth(150);
        checkBalanceButton.setStyle("-fx-font: 14 arial; -fx-text-fill: white; -fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff9966, #ff5e62)");
        checkBalanceButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> checkBalanceButton.setEffect(new DropShadow()));
        checkBalanceButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> checkBalanceButton.setEffect(null)); 
        checkBalanceButton.setOnAction(e -> {
            this.checkBalanceScene = createCheckBalanceScene();
            stage.setScene(this.checkBalanceScene);
        });

        // Create log out button
        Button logOutButton = new Button("Log Out");
        logOutButton.setMinWidth(150);
        logOutButton.setStyle("-fx-font: 14 arial; -fx-text-fill: white; -fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff9966, #ff5e62)");
        logOutButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> logOutButton.setEffect(new DropShadow()));
        logOutButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> logOutButton.setEffect(null)); 
        logOutButton.setOnAction(e -> mainApp.logout());

        menuLayout.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #2b5876, #524376)");
        menuLayout.getChildren().addAll(makeOrderButton, viewOrderHistoryButton, printBillButton, payButton, checkBalanceButton, logOutButton);

        return new Scene(menuLayout, 400, 600);
    }

    /**
     * Create make order scene
     * 
     * @return scene for make order form
     */
    private Scene createMakeOrderForm() {
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        Label restaurantNameLabel = new Label("Restaurant Name");
        restaurantNameLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        // Create restaurant combobox
        ListView<String> restoItemListView = new ListView<>();
        ComboBox<String> restaurantComboBox = new ComboBox<>();
        restoList.stream().forEach(r -> restaurantComboBox.getItems().add(r.getNamaRestoran()));
        restaurantComboBox.setPromptText("Select restaurant");
        restaurantComboBox.setStyle("-fx-font: 14 arial; -fx-background-color: white");
        restaurantComboBox.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> restaurantComboBox.setEffect(new DropShadow()));
        restaurantComboBox.addEventHandler(MouseEvent.MOUSE_EXITED, e -> restaurantComboBox.setEffect(null)); 
        restaurantComboBox.setOnAction(e -> {
            Restaurant restaurant = UserSystemCLI.searchResto(restaurantComboBox.getValue(), restoList);
            restoItemListView.getItems().clear();
            restaurant.getMenu().stream().forEach(menu -> {
                restoItemListView.getItems().add(menu.getNamaMakanan());
            });
        });

        Label dateLabel = new Label("Date(DD/MM/YYYY)");
        dateLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        // Create date input field
        TextField dateInput = new TextField();
        dateInput.setPrefHeight(30);
        dateInput.setMaxWidth(250);
        dateInput.setPromptText("Enter date");
        dateInput.setStyle("-fx-font: 14 arial");

        Label menuItemsLabel = new Label("Menu Items");
        menuItemsLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        Label selectTipsLabel = new Label("(hold ctrl + mouse click) for multiple selection");
        selectTipsLabel.setStyle("-fx-font: 11 arial; -fx-text-fill: white;");

        // Create list view for menu items
        ArrayList<String> selectedItems = new ArrayList<>();
        restoItemListView.setMaxHeight(300);
        restoItemListView.setStyle("-fx-font: 14 arial; -fx-text-fill: black;");
        restoItemListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        restoItemListView.setOnMouseClicked(e -> {
            selectedItems.clear();
            selectedItems.addAll(restoItemListView.getSelectionModel().getSelectedItems());
        });

        // Create make order button
        Button makeOrderButton = new Button("Make Order");
        makeOrderButton.setStyle("-fx-font: 14 arial; -fx-text-fill: white; -fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff9966, #ff5e62)");
        makeOrderButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> makeOrderButton.setEffect(new DropShadow()));
        makeOrderButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> makeOrderButton.setEffect(null)); 
        makeOrderButton.setOnAction(e -> {
            handleMakeOrder(restaurantComboBox.getValue(), dateInput.getText(), selectedItems);
            selectedItems.clear();
        });

        // Create return button
        Button returnButton = new Button("Return");
        returnButton.setStyle("-fx-font: 14 arial; -fx-text-fill: white; -fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff9966, #ff5e62)");
        returnButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> returnButton.setEffect(new DropShadow()));
        returnButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> returnButton.setEffect(null)); 
        returnButton.setOnAction(e -> {
            dateInput.clear();
            mainApp.setScene(mainApp.getScene(this.user.getNamaUser()));
        });

        menuLayout.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #2b5876, #524376)");
        menuLayout.getChildren().addAll(restaurantNameLabel, restaurantComboBox, dateLabel, dateInput, menuItemsLabel,
                                                    selectTipsLabel, restoItemListView, makeOrderButton, returnButton);

        return new Scene(menuLayout, 400, 600);
    }

    /**
     * Create view order history scene
     * 
     * @return scene for view order history form
     */
    private Scene createViewOrderHistory(){
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        Label orderHistoryLabel = new Label("Order History");
        orderHistoryLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        Label copyTipsLabel = new Label("click on orderID to copy");
        copyTipsLabel.setStyle("-fx-font: 11 arial; -fx-text-fill: white;");

        // Create list view for order history
        ListView<String> orderHistoryListView = new ListView<>();
        ArrayList<Order> orderHistoryList = this.user.getOrderHistory();
        orderHistoryList.stream().forEach(order -> orderHistoryListView.getItems().add(order.getOrderID()));
        orderHistoryListView.setMaxHeight(300);
        orderHistoryListView.setStyle("-fx-font: 14 arial; -fx-text-fill: black;");
        orderHistoryListView.setOnMouseClicked(e -> {
            String selectedOrderID = orderHistoryListView.getSelectionModel().getSelectedItem();
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(selectedOrderID);
            clipboard.setContent(content);
        });

        // Create return button
        Button returnButton = new Button("Return");
        returnButton.setStyle("-fx-font: 14 arial; -fx-text-fill: white; -fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff9966, #ff5e62)");
        returnButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> returnButton.setEffect(new DropShadow()));
        returnButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> returnButton.setEffect(null)); 
        returnButton.setOnAction(e -> mainApp.setScene(mainApp.getScene(this.user.getNamaUser())));

        menuLayout.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #2b5876, #524376)");
        menuLayout.getChildren().addAll(orderHistoryLabel, copyTipsLabel, orderHistoryListView, returnButton);

        return new Scene(menuLayout, 400,600);
    }

    /**
     * Create bill printer scene
     * 
     * * @return The Scene object for the bill printer form.
     */
    private Scene createBillPrinter(){
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        Label orderIDLabel = new Label("Order ID");
        orderIDLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        // Create orderID input field
        TextField orderIDInput = new TextField();
        orderIDInput.setPrefHeight(30);
        orderIDInput.setMaxWidth(250);
        orderIDInput.setPromptText("Enter order id");
        orderIDInput.setStyle("-fx-font: 14 arial");

        // Create print button
        Button printButton = new Button("Print Bill");
        printButton.setStyle("-fx-font: 14 arial; -fx-text-fill: white; -fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff9966, #ff5e62)");
        printButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> printButton.setEffect(new DropShadow()));
        printButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> printButton.setEffect(null)); 
        printButton.setOnAction(e -> {
            Scene billPrinterScene = new BillPrinter(stage, mainApp, this.user).getScene(orderIDInput.getText(), printBillScene);
            if (billPrinterScene != null) mainApp.setScene(billPrinterScene);
        });

        // Create return button
        Button returnButton = new Button("Return");
        returnButton.setStyle("-fx-font: 14 arial; -fx-text-fill: white; -fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff9966, #ff5e62)");
        returnButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> returnButton.setEffect(new DropShadow()));
        returnButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> returnButton.setEffect(null)); 
        returnButton.setOnAction(e -> {
            orderIDInput.clear();
            mainApp.setScene(mainApp.getScene(this.user.getNamaUser()));
        });

        menuLayout.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #2b5876, #524376)");
        menuLayout.getChildren().addAll(orderIDLabel, orderIDInput, printButton, returnButton);

        return new Scene(menuLayout, 400,600);
    }

    /**
     * Creates a Scene object for the bill payment form.
     * 
     * @return The Scene object for the bill payment form.
     */
    private Scene createBillPaymentForm() {
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        Label orderIDLabel = new Label("Order ID:");
        orderIDLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        // Create orderID input field
        TextField orderIDInput = new TextField();
        orderIDInput.setPrefHeight(30);
        orderIDInput.setMaxWidth(250);
        orderIDInput.setPromptText("Enter order id");
        orderIDInput.setStyle("-fx-font: 14 arial");

        // Create payment method combobox
        ComboBox<String> paymentMethodComboBox = new ComboBox<>();
        paymentMethodComboBox.getItems().addAll("Credit Card", "Debit");
        paymentMethodComboBox.setPromptText("Select payment method");
        paymentMethodComboBox.setStyle("-fx-font: 14 arial; -fx-background-color: white");

        // Create pay button
        Button payButton = new Button("Pay");
        payButton.setStyle("-fx-font: 14 arial; -fx-text-fill: white; -fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff9966, #ff5e62)");
        payButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> payButton.setEffect(new DropShadow()));
        payButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> payButton.setEffect(null)); 
        payButton.setOnAction(e -> handleBillPayment(orderIDInput.getText(), paymentMethodComboBox.getValue()));

        // Create return button
        Button returnButton = new Button("Return");
        returnButton.setStyle("-fx-font: 14 arial; -fx-text-fill: white; -fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff9966, #ff5e62)");
        returnButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> returnButton.setEffect(new DropShadow()));
        returnButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> returnButton.setEffect(null)); 
        returnButton.setOnAction(e -> {
            orderIDInput.clear();
            mainApp.setScene(mainApp.getScene(this.user.getNamaUser()));
        });

        menuLayout.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #2b5876, #524376)");
        menuLayout.getChildren().addAll(orderIDLabel, orderIDInput, paymentMethodComboBox, payButton, returnButton);

        return new Scene(menuLayout, 400,600);
    }


    /**
     * Creates a Scene object for displaying the customer menu with balance information.
     * 
     * @return the created Scene object
     */
    private Scene createCheckBalanceScene() {
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        Label userLabel = new Label(this.user.getNamaUser());
        userLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        Label balanceLabel = new Label("Balance: Rp " + Long.toString(this.user.getSaldo()));
        balanceLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");

        // Create return button
        Button returnButton = new Button("Return");
        returnButton.setStyle("-fx-font: 14 arial; -fx-text-fill: white; -fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff9966, #ff5e62)");
        returnButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> returnButton.setEffect(new DropShadow()));
        returnButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> returnButton.setEffect(null)); 
        returnButton.setOnAction(e -> mainApp.setScene(mainApp.getScene(this.user.getNamaUser())));

        menuLayout.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #2b5876, #524376)");
        menuLayout.getChildren().addAll(userLabel, balanceLabel, returnButton);

        return new Scene(menuLayout, 400,600);
    }

    /**
     * Handles the process of making an order.
     *
     * @param restaurantName The name of the restaurant.
     * @param orderDate The date of the order in the format (DD/MM/YYYY).
     * @param selectedItems The list of selected menu items.
     */
    private void handleMakeOrder(String restaurantName, String orderDate, ArrayList<String> selectedItems) {
        Restaurant restaurant = UserSystemCLI.searchResto(restaurantName, restoList);
        if (selectedItems.isEmpty() || selectedItems == null){
            showAlert("Error", "No Menu Items Selected", "Please select at least one menu item", AlertType.ERROR);
            return;
        }
        if (!OrderGenerator.valDate(orderDate)){
            showAlert("Error", "Invalid Date", "Please enter a valid date using the format (DD/MM/YYYY)", AlertType.ERROR);
            return;
        }
        
        Menu[] items = new Menu[selectedItems.size()];
        int iterator = 0;
        for (String itemName : selectedItems){
            items[iterator++] = CustomerSystemCLI.searchMenu(restaurant, itemName);
        }

        String orderID = CustomerSystemCLI.generateOrderID(restaurantName, orderDate, this.user.getNoTelepon());
        int shippingCost = Integer.parseInt(CustomerSystemCLI.calculateShippingCost(this.user.getLokasi())
                            .substring(3,9).replace(".", ""));
        this.user.addOrder(new Order(orderID, orderDate, shippingCost, restaurant, items));
        showAlert("Success", "Order Successful", "New order has been successfuly made!\nOrderID: " + orderID, AlertType.INFORMATION);
    }

    /**
     * Handles the bill payment for a specific order.
     *
     * @param orderIDInput The input order ID.
     * @param selectedPaymentMethod The selected payment method.
     */
    private void handleBillPayment(String orderIDInput, String selectedPaymentMethod) {
        Order order = CustomerSystemCLI.searchOrder(user, orderIDInput);
        if (order == null){
            showAlert("Error", "Invalid Order ID", "Please input a valid order ID", AlertType.ERROR);
            return;
        }
        if (selectedPaymentMethod == null){
            showAlert("Error", "No Payment Method Selected", "Please select a payment method", AlertType.ERROR);
            return;
        }
        if (order.getOrderStatus().equals("Finished")){
            showAlert("Error", "Order Already Paid", "This order has already been paid", AlertType.ERROR);
            return;
        }

        long itemsPrice = 0;
        for (Menu menu : order.getItems()) itemsPrice += menu.getHarga();
        long totalPrice = order.getOngkir() + itemsPrice;
        long userBalance = this.user.getSaldo();
        long newBalance = 0;
        DepeFoodPaymentSystem paymentMethod = this.user.getPaymentMethod();
        if (selectedPaymentMethod.equals("Credit Card")){
            if (paymentMethod.paymentType().equals("debit")){
                showAlert("Error", "Payment Method Unavailabe", "You do not have this type of payment method, please select another payment method!", AlertType.ERROR);
                return;
            }
            newBalance = paymentMethod.processPayment((long) totalPrice, userBalance);
            this.user.setSaldo(newBalance);
            order.getRestoran().addSaldo((long) itemsPrice);
            order.setOrderStatus();
            showAlert("Success", "Payment Successful", "Your transaction of Rp" + totalPrice + 
                        " for order ID \"" + order.getOrderID() + "\" has been successfuly made!", AlertType.INFORMATION);
        }
        else if (selectedPaymentMethod.equals("Debit")) {
            if (paymentMethod.paymentType().equals("credit")){
                showAlert("Error", "Payment Method Unavailabe", "You do not have this type of payment method, please select another payment method!", AlertType.ERROR);
                return;
            }
            DebitPayment debitPayment = (DebitPayment) paymentMethod;
            if (totalPrice < debitPayment.getMinPrice()){
                showAlert("Error", "Price Too Low", "The minimum price to pay with debit is Rp50000!", AlertType.ERROR);
                return;
            }
            if (userBalance < (long)(totalPrice)){
                showAlert("Error", "Insufficient Balance", "You do not have enough balance to pay for this order!", AlertType.ERROR);
                return;
            }
            newBalance = paymentMethod.processPayment((long) totalPrice, userBalance);
            this.user.setSaldo(newBalance);
            order.getRestoran().addSaldo((long) itemsPrice);
            order.setOrderStatus();
            showAlert("Success", "Payment Successful", "Your transaction of Rp" + totalPrice + 
                        " for order ID \"" + order.getOrderID() + "\" has been successfuly made!", AlertType.INFORMATION);
        }
    }

    public void addMenuSceneToMap(){
        mainApp.addScene(this.user.getNamaUser(), scene);
    }

    public Scene getScene(){
        return this.scene;
    }
}