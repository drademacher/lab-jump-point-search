package view;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML //  fx:id="myButton"
    private Button play;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        play.setOnAction(e -> System.out.print("nice"));
        System.out.print("check");

        // initialize your logic here: all @FXML variables will have been injected

    }
}