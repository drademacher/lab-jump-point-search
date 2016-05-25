package view;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public BooleanProperty editMode = new SimpleBooleanProperty();

    @FXML
    public Canvas canvas;
    @FXML
    private Button playButton;
    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button resetButton;

    @FXML
    private Menu mode;
    @FXML
    private MenuItem emptyMap;
    @FXML
    private MenuItem randomMap;
    @FXML
    private MenuItem genMap;
    @FXML
    private MenuItem loadMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // buttons
        playButton.setOnAction(e -> System.out.println("TODO: play"));
        prevButton.setOnAction(e -> System.out.println("TODO: prev"));
        nextButton.setOnAction(e -> System.out.println("TODO: next"));
        resetButton.setOnAction(e -> System.out.println("TODO: reset"));

        mode.setOnMenuValidation(e -> System.out.print("hi"));
        mode.setOnAction(e -> {
            System.out.print("hi");
            if (editMode.getValue()) {
                editMode.setValue(false);
                mode.setText("Show");
            } else {
                editMode.setValue(true);
                mode.setText("Edit");
            }
        });
        playButton.disableProperty().bind(editMode);
        prevButton.disableProperty().bind(editMode);
        nextButton.disableProperty().bind(editMode);
        resetButton.disableProperty().bind(editMode);

        emptyMap.setOnAction(e -> System.out.println("nice"));
    }
}