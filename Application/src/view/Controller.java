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
    public BooleanProperty editMode = new SimpleBooleanProperty(true);

    @FXML
    public Canvas canvas;
    @FXML
    private Button computeButton;
    @FXML
    private Button playButton;
    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button restartButton;

    @FXML
    private Menu showMode;
    @FXML
    private MenuItem editMap;
    @FXML
    private MenuItem emptyMap;
    @FXML
    private MenuItem exampleMap;
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
        restartButton.setOnAction(e -> System.out.println("TODO: reset"));

        computeButton.setOnAction(e -> {
            if (editMode.getValue()) {
                editMode.setValue(false);
                showMode.setText("Show");
                computeButton.setText("Reset");
            } else {
                editMode.setValue(true);
                showMode.setText("Edit");
                computeButton.setText("Compute");
            }
        });
        playButton.disableProperty().bind(editMode);
        prevButton.disableProperty().bind(editMode);
        nextButton.disableProperty().bind(editMode);
        restartButton.disableProperty().bind(editMode);

        emptyMap.setOnAction(e -> System.out.println("nice"));
    }
}