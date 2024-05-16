package assignments.assignment4.page;

import assignments.assignment3.Restaurant;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import java.util.ArrayList;

public abstract class MemberMenu {
    private Scene scene;
    protected static ArrayList<Restaurant> restoList = new ArrayList<>();

    abstract protected Scene createBaseMenu();
    abstract protected void addMenuSceneToMap();

    public static void showAlert(String title, String header, String content, Alert.AlertType c){
        Alert alert = new Alert(c);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public Scene getScene(){
        return this.scene;
    }
}
