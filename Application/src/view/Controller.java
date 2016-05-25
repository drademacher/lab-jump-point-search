package view;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private Button playButton;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Button resetButton;
    @FXML private MenuItem emptyMap;
    @FXML private MenuItem randomMap;
    @FXML private MenuItem genMap;
    @FXML private MenuItem loadMap;
    @FXML public Canvas canvas;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emptyMap.setOnAction(e -> System.out.println("nice"));
        prevButton.setOnAction(e -> System.out.println("nice"));



    }
}