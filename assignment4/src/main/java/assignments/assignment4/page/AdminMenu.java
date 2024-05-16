package assignments.assignment4.page;

import assignments.assignment3.Restaurant;
import assignments.assignment3.User;
import assignments.assignment3.systemCLI.UserSystemCLI;
import assignments.assignment3.systemCLI.CustomerSystemCLI;
import assignments.assignment3.Menu;
import assignments.assignment4.MainApp;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminMenu extends MemberMenu{
    private Stage stage;
    private Scene scene;
    private User user;
    private Scene addRestaurantScene;
    private Scene addMenuScene;
    private Scene viewRestaurantsScene;
    private MainApp mainApp; // Reference to MainApp instance
    private ListView<String> menuItemsListView = new ListView<>();

    public AdminMenu(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user; // Store the user
        this.scene = createBaseMenu();
        this.addRestaurantScene = createAddRestaurantForm();
        this.addMenuScene = createAddMenuForm();
        this.viewRestaurantsScene = createViewRestaurantsForm();

        addMenuSceneToMap();
    }

    /**
     * Create base menu scene for admin
     * 
     * @return scene for admin menu
     */
    @Override
    public Scene createBaseMenu() {
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        Button addRestaurantButton = new Button("Add Restaurant");
        addRestaurantButton.setStyle("-fx-font: 14 arial; -fx-text-fill: black;");
        addRestaurantButton.setOnAction(e -> {
            mainApp.setScene(this.addRestaurantScene);
        });

        Button addMenuButton = new Button("Add Menu");
        addMenuButton.setStyle("-fx-font: 14 arial; -fx-text-fill: black;");
        addMenuButton.setOnAction(e -> {
            mainApp.setScene(this.addMenuScene);
        });

        Button viewRestaurantsButton = new Button("View Restaurants");
        viewRestaurantsButton.setStyle("-fx-font: 14 arial; -fx-text-fill: black;");
        viewRestaurantsButton.setOnAction(e -> {
            menuItemsListView.getItems().clear();
            mainApp.setScene(this.viewRestaurantsScene);
        });

        Button logOutButton = new Button("Log Out");
        logOutButton.setStyle("-fx-font: 14 arial; -fx-text-fill: black;");
        logOutButton.setOnAction(e -> mainApp.logout());

        menuLayout.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #076585, #c7fbfc)");
        menuLayout.getChildren().addAll(addRestaurantButton, addMenuButton, viewRestaurantsButton, logOutButton);

        return new Scene(menuLayout, 400, 600);
    }

    /**
     * Creates a Scene object for adding a restaurant form.
     * 
     * @return The Scene object for the add restaurant form.
     */
    public Scene createAddRestaurantForm() {
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        Label restaurantNameLabel = new Label("Restaurant Name:");
        restaurantNameLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white;");

        TextField restaurantNameInput = new TextField();
        restaurantNameInput.setPrefHeight(30);
        restaurantNameInput.setMaxWidth(250);
        restaurantNameInput.setPromptText("Enter Restaurant Name");
        restaurantNameInput.setStyle("-fx-font: 14 arial");

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-font: 14 arial; -fx-text-fill: black;");
        submitButton.setOnAction(e -> {
            handleAddRestaurant(restaurantNameInput.getText());
            restaurantNameInput.clear();
        });

        Button returnButton = new Button("Return");
        returnButton.setStyle("-fx-font: 14 arial; -fx-text-fill: black;");
        returnButton.setOnAction(e -> mainApp.setScene(scene));

        menuLayout.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #076585, #c7fbfc)");
        menuLayout.getChildren().addAll(restaurantNameLabel, restaurantNameInput, submitButton, returnButton);

        return new Scene(menuLayout, 400, 600);
    }

    /**
     * Create add menu scene
     * 
     * @return scene for add menu form
     */
    private Scene createAddMenuForm() {
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        Label restaurantNameLabel = new Label("Restaurant Name:");
        restaurantNameLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white;");

        TextField restaurantNameInput = new TextField();
        restaurantNameInput.setPrefHeight(30);
        restaurantNameInput.setMaxWidth(250);
        restaurantNameInput.setPromptText("Enter restaurant rame");
        restaurantNameInput.setStyle("-fx-font: 14 arial");

        Label menuItemLabel = new Label("Menu Item Name:");
        menuItemLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white;");

        TextField menuItemInput = new TextField();
        menuItemInput.setPrefHeight(30);
        menuItemInput.setMaxWidth(250);
        menuItemInput.setPromptText("Enter item name");
        menuItemInput.setStyle("-fx-font: 14 arial");

        Label priceLabel = new Label("Price:");
        priceLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white;");
    
        TextField priceInput = new TextField();
        priceInput.setPrefHeight(30);
        priceInput.setMaxWidth(250);
        priceInput.setPromptText("Enter item price");
        priceInput.setStyle("-fx-font: 14 arial");

        Button addItemButton = new Button("Add Menu Item");
        addItemButton.setStyle("-fx-font: 14 arial; -fx-text-fill: black;");
        addItemButton.setOnAction(e -> {
            handleAddMenu(restaurantNameInput.getText(), menuItemInput.getText(), priceInput.getText());
            menuItemInput.clear();
            priceInput.clear();
        });

        Button returnButton = new Button("Return");
        returnButton.setStyle("-fx-font: 14 arial; -fx-text-fill: black;");
        returnButton.setOnAction(e -> {
            restaurantNameInput.clear();
            mainApp.setScene(mainApp.getScene(this.user.getNamaUser()));
        });

        menuLayout.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #076585, #c7fbfc)");
        menuLayout.getChildren().addAll(restaurantNameLabel, restaurantNameInput, menuItemLabel, menuItemInput, priceLabel, priceInput, addItemButton, returnButton);

        return new Scene(menuLayout, 400, 600);
    }
    
    /**
     * Create view restaurants scene
     * 
     * @return scene for view restaurants form
     */
    private Scene createViewRestaurantsForm() {
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        Label restaurantNameLabel = new Label("Restaurant Name:");
        restaurantNameLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white;");

        TextField restaurantNameInput = new TextField();
        restaurantNameInput.setPrefHeight(30);
        restaurantNameInput.setMaxWidth(250);
        restaurantNameInput.setPromptText("Enter Restaurant Name");
        restaurantNameInput.setStyle("-fx-font: 14 arial");

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-font: 14 arial; -fx-text-fill: black;");
        searchButton.setOnAction(e -> {
            String name = restaurantNameInput.getText();
            menuItemsListView.getItems().clear();
            Restaurant restaurant = UserSystemCLI.searchResto(name, restoList);
            if (restaurant != null){
                restaurant.getMenu().stream().forEach(menu -> {
                    menuItemsListView.getItems().add(String.format("%s - Rp%.1f ", menu.getNamaMakanan(), menu.getHarga()));
                });
            }
            else {
                menuItemsListView.getItems().clear();
                showAlert("Error", "Restaurant Not Found", "Restaurant Not Found!", AlertType.ERROR);
            }        
        });

        Label menuLabel = new Label("Menu:");
        menuLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white;");

        menuItemsListView.getItems().clear();
        menuItemsListView.setMaxHeight(300);
        menuItemsListView.setStyle("-fx-font: 14 arial; -fx-text-fill: black;");

        Button returnButton = new Button("Return");
        returnButton.setStyle("-fx-font: 14 arial; -fx-text-fill: black;");
        returnButton.setOnAction(e -> {
            restaurantNameInput.clear();
            mainApp.setScene(mainApp.getScene(this.user.getNamaUser()));
        });

        menuLayout.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #076585, #c7fbfc)");
        menuLayout.getChildren().addAll(restaurantNameLabel, restaurantNameInput, searchButton, menuLabel, menuItemsListView, returnButton);

        return new Scene(menuLayout, 400, 600);
    }
    
    /**
     * Handles the addition of a new restaurant.
     *
     * @param nama The name of the restaurant to be added.
     */
    private void handleAddRestaurant(String nama) {
        if (UserSystemCLI.searchResto(nama, restoList) == null){
            if (nama.length() < 4){
                showAlert("Error", "Invalid Name", "Restaurant name has to be at least 4 characters!", AlertType.ERROR);
                return;
            }
            Restaurant restaurant = new Restaurant(nama);
            restoList.add(restaurant);
            showAlert("Success", "Restaurant Added", "Restaurant has been added successfully!", AlertType.INFORMATION);
        } else {
            String restaurantExistsError = String.format("Restaurant with the name %s already exists. Please enter a different name!", nama);
            showAlert("Error", "Invalid Name", restaurantExistsError, AlertType.ERROR);
            return;
        }
    }

    /**
     * Handles the addition of a menu item to a restaurant.
     * 
     * @param restaurantName the name of the restaurant
     * @param itemName the name of the menu item
     * @param priceString the price of the menu item as a string
     */
    private void handleAddMenu(String restaurantName, String itemName, String priceString) {
        Restaurant restaurant = UserSystemCLI.searchResto(restaurantName, restoList);
        if (restaurant == null) {
            showAlert("Error", "Restaurant Not Found", "Restaurant not found!", AlertType.ERROR);
            return;
        }

        if (itemName.equals("")){
            showAlert("Error", "Invalid Name", "Please input valid name!", AlertType.ERROR);
            return;
        }

        double price = 0;
        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Price", "Please input valid price!", AlertType.ERROR);
            return;
        }

        Menu menu = CustomerSystemCLI.searchMenu(restaurant, itemName);
        if (menu != null) {
            if (menu.getHarga() != price){
                menu.setHarga(price);
                showAlert("Success", "Menu Updated", "Price has been updated successfully!", AlertType.INFORMATION);
            }
            else {
                showAlert("Error", "Menu Exists", "Menu already exists!", AlertType.ERROR);
                
            }
        } else {
            Menu newMenu = new Menu(itemName, price);
            restaurant.addMenu(newMenu);
            showAlert("Success", "Menu Added", "Menu has been added successfully!", AlertType.INFORMATION);
        }
    }

    public void addMenuSceneToMap(){
        mainApp.addScene(this.user.getNamaUser(), scene);
    }

    public Scene getScene(){
        return this.scene;
    }
}
