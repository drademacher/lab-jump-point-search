package view;


import grid.Type;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public BooleanProperty editMode = new SimpleBooleanProperty(true);
    @FXML
    public Canvas canvas;
    private Context context = Context.getInstance();
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
    private Menu modeMenu;
    @FXML
    private Menu mapMenu;
    @FXML
    private Menu algoMenu;
    @FXML
    private Menu helpMenu;


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
                modeMenu.setText("Show");
                computeButton.setText("Reset");
            } else {
                editMode.setValue(true);
                modeMenu.setText("Edit");
                computeButton.setText("Compute");
            }
        });

        // bindings for buttons and menu
        playButton.disableProperty().bind(editMode);
        prevButton.disableProperty().bind(editMode);
        nextButton.disableProperty().bind(editMode);
        restartButton.disableProperty().bind(editMode);

        mapMenu.disableProperty().bind(editMode.not());
        algoMenu.disableProperty().bind(editMode.not());


        // grid menu
        emptyMap.setOnAction(e -> {
            context.grid.setEmpty();
            renderCanvas();
        });

        exampleMap.setOnAction(e -> {
            context.grid.setEmpty();
            renderCanvas();
        });

        randomMap.setOnAction(e -> {
            context.grid.setRnd(0.1);
            renderCanvas();
        });

        genMap.setOnAction(e -> {
            // TODO
            renderCanvas();
        });

        loadMap.setOnAction(e -> {
            Stage stage = (Stage) canvas.getScene().getWindow();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Map Files", "*.map"),
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                    new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            renderCanvas();
        });


        // action stuff here
        canvas.setOnMouseClicked((MouseEvent e) -> {
            canvasClicked(e.getX(), e.getY());
        });
    }

    public void renderCanvas() {
        // full rendering of the grid
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Paint.valueOf("#212121"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int x = 0; x < context.n; x++) {
            for (int y = 0; y < context.m; y++) {
                // change color
                // gc.setFill(CELLS[grid.getCell(x, y)]);

                // draw rect
                gc.setFill(context.grid.getCell(x, y).getColor());
                gc.fillRect(x * context.size + 1, y * context.size + 1, context.size - 1, context.size - 1);
            }
        }

        // gc.clearRect(0, canvas.getHeight() - 1, canvas.getWidth(), canvas.getHeight());
        // gc.clearRect(canvas.getWidth() - 1, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void canvasClicked(double xReal, double yReal) {
        final int x = (int) (xReal / context.size);
        final int y = (int) (yReal / context.size);
        if (xReal % context.size == 0 || yReal % context.size == 0) {
            return;
        }

        // TODO: trigger event here for point x, y
        // System.out.println("click " + xCell + " " + yCell);
        if (context.grid.getCell(x, y) == Type.NODE) {
            context.grid.setCell(x, y, Type.OBSTACLE);
        } else {
            context.grid.setCell(x, y, Type.NODE);
        }
        renderCanvas();
    }

}