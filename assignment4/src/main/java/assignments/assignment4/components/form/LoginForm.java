package assignments.assignment4.components.form;

import assignments.assignment3.DepeFood;
import assignments.assignment3.User;
import assignments.assignment4.page.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Shadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import assignments.assignment4.MainApp;
import assignments.assignment4.page.AdminMenu;
import assignments.assignment4.page.CustomerMenu;
import java.util.function.Consumer;

public class LoginForm {
    private Stage stage;
    private MainApp mainApp; // MainApp instance
    private TextField nameInput;
    private TextField phoneInput;

    public LoginForm(Stage stage, MainApp mainApp) { // Pass MainApp instance to constructor
        this.stage = stage;
        this.mainApp = mainApp; // Store MainApp instance
    }

    /**
     * Create lofin form scene
     * 
     * @return scene for login
     */
    private Scene createLoginForm() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(12);

        Label welcomeLabel = new Label("Welcome to DepeFood");
        welcomeLabel.setStyle("-fx-font: 30 arial; -fx-text-fill: white; -fx-font-weight: bold");
        GridPane.setConstraints(welcomeLabel, 0, 15, 31, 5, HPos.CENTER, VPos.CENTER);

        Label nameLabel = new Label("Name");
        nameLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");
        GridPane.setConstraints(nameLabel, 0, 23, 10, 4, HPos.LEFT, VPos.CENTER);

        nameInput = new TextField();
        nameInput.setPrefHeight(30);
        nameInput.setMaxWidth(235);
        nameInput.setPromptText("Enter your name");
        nameInput.setStyle("-fx-font: 14 arial; -fx-background-color: white;");
        GridPane.setConstraints(nameInput, 9, 23, 24, 4, HPos.CENTER, VPos.CENTER);

        Label phoneLabel = new Label("Phone Number");
        phoneLabel.setStyle("-fx-font: 15 arial; -fx-text-fill: white; -fx-font-weight: bold;");
        GridPane.setConstraints(phoneLabel, 0, 27, 12, 4, HPos.LEFT, VPos.CENTER);

        phoneInput = new TextField();
        phoneInput.setPrefHeight(30);
        phoneInput.setMaxWidth(235);
        phoneInput.setPromptText("Enter your phone number");
        phoneInput.setStyle("-fx-font: 14 arial; -fx-background-color: white;");
        GridPane.setConstraints(phoneInput, 9, 27, 24, 4, HPos.CENTER, VPos.CENTER);

        Button loginButton = new Button("Login");
        loginButton.setPrefSize(100, 27);
        loginButton.setStyle("-fx-font: 14 arial; -fx-text-fill: white; -fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff9966, #ff5e62)");
        loginButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> loginButton.setEffect(new DropShadow()));
        loginButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> loginButton.setEffect(null)); 
        GridPane.setConstraints(loginButton, 10, 31, 11, 4, HPos.CENTER, VPos.CENTER);
        loginButton.setOnAction(e -> handleLogin());

        grid.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #2b5876, #4e4376)");
        grid.getChildren().addAll(welcomeLabel, nameLabel, nameInput, phoneLabel, phoneInput, loginButton);

        return new Scene(grid, 400, 600);
    }


    /**
     * Handles the login process.
     * Retrieves the user information based on the name and phone number input.
     * If the user exists, sets the user in the main application and navigates to the appropriate user menu.
     * If the user does not exist, displays an error message.
     */
    private void handleLogin() {
        User user = DepeFood.getUser(nameInput.getText(), phoneInput.getText());
        if (user != null) {
            mainApp.setUser(user);
            Scene userMenu = mainApp.getScene(user.getNamaUser());
            if (user.getRole().equals("Admin")) {
                if (userMenu == null)
                    userMenu = new AdminMenu(stage, mainApp, user).getScene();
            } 
            else {
                if (userMenu == null)
                    userMenu = new CustomerMenu(stage, mainApp, user).getScene();
            }
            mainApp.setScene(userMenu);
        } 
        else {
            MemberMenu.showAlert("Login Failed", "Invalid Credentials", "Please check your name and phone number", Alert.AlertType.ERROR);
            return;
        }
    }

    public Scene getScene(){
        return this.createLoginForm();
    }

}
