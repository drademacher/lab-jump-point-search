package view;


import controller.map.FieldType;
import controller.map.MapFactory;
import controller.map.NotAFieldException;
import grid.Parser;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public BooleanProperty editModeBool = new SimpleBooleanProperty(true);
    private Global global = Global.getInstance();


    @FXML
    private ToggleGroup pruning;

    @FXML
    private MenuItem editMap;

    @FXML
    private MenuItem loadMap;

    @FXML
    private Menu mapMenu;

    @FXML
    private MenuItem about;

    @FXML
    private Button computeButton;

    @FXML
    private MenuItem emptyMap;

    @FXML
    private Button prevButton;

    @FXML
    private RadioMenuItem noPrune;

    @FXML
    private Button restartButton;

    @FXML
    // TODO: make this private
    public Canvas canvas;

    @FXML
    private MenuItem genMap5;

    @FXML
    private Menu algoMenu;

    @FXML
    private MenuItem genMap4;

    @FXML
    private HBox visMode;

    @FXML
    private HBox setMode;

    @FXML
    private MenuItem genMap1;

    @FXML
    private ToggleGroup heuristic;

    @FXML
    private Menu helpMenu;

    @FXML
    private MenuItem genMap3;

    @FXML
    private MenuItem genMap2;

    @FXML
    private RadioMenuItem jps;

    @FXML
    private Button playButton;

    @FXML
    private RadioMenuItem jpsPrune;

    @FXML
    private Button nextButton;

    @FXML
    private RadioMenuItem bbPrune;

    @FXML
    private MenuItem exampleMap;

    @FXML
    private MenuItem randomMap;

    @FXML
    private HBox editMode;

    @FXML
    private ToggleGroup algo;

    @FXML
    private RadioMenuItem astar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // buttons
        playButton.setOnAction(e -> System.out.println("TODO: play"));
        prevButton.setOnAction(e -> System.out.println("TODO: prev"));
        nextButton.setOnAction(e -> System.out.println("TODO: next"));
        restartButton.setOnAction(e -> System.out.println("TODO: reset"));

        computeButton.setOnAction(e -> {
            if (editModeBool.getValue()) {
                editModeBool.setValue(false);
                computeButton.setText("Reset");
            } else {
                editModeBool.setValue(true);
                computeButton.setText("Compute");
            }
        });

        // bindings for buttons and menu
        playButton.disableProperty().bind(editModeBool);
        prevButton.disableProperty().bind(editModeBool);
        nextButton.disableProperty().bind(editModeBool);
        restartButton.disableProperty().bind(editModeBool);

        mapMenu.disableProperty().bind(editModeBool.not());
        algoMenu.disableProperty().bind(editModeBool.not());


        // map menu
        emptyMap.setOnAction(e -> {
            //global.map.createEmptyMap();
            global.map  = MapFactory.createEmptyMap(global.n, global.m);
            renderCanvas();
        });

        exampleMap.setOnAction(e -> {
            //global.map.setEmpty();
            global.map  = MapFactory.createExampleMap(global.n, global.m);
            renderCanvas();
        });

        randomMap.setOnAction(e -> {
            //global.map.setRnd(0.2);
            global.map  = MapFactory.createRandomMap(global.n, global.m, 0.2);
            renderCanvas();
        });

        genMap1.setOnAction(e -> {
            global.map  = MapFactory.createMazeMap(global.n, global.m);
            renderCanvas();
        });

        genMap2.setOnAction(e -> {
            global.map  = MapFactory.createMazeWithRoomsMap(global.n, global.m);
            renderCanvas();
        });

        genMap3.setOnAction(e -> {
            global.map  = MapFactory.createSingleConnRoomsMap(global.n, global.m);
            renderCanvas();
        });

        genMap4.setOnAction(e -> {
            global.map  = MapFactory.createLoopedRoomsMap(global.n, global.m);
            renderCanvas();
        });

        genMap5.setOnAction(e -> {
            global.map  = MapFactory.createDoubleConnRoomsMap(global.n, global.m);
            renderCanvas();
        });


        // jps+ and a star cannot be combined!
        jpsPrune.disableProperty().bind(jps.selectedProperty().not());
        astar.setOnAction(e -> {
            jpsPrune.setSelected(false);
        });


        loadMap.setOnAction(e -> {
            Stage stage = (Stage) canvas.getScene().getWindow();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Map Files", "*.map"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                global.map = Parser.getMap(selectedFile);
            }

            renderCanvas();
        });


        // action stuff here
        canvas.setOnMouseClicked((MouseEvent e) -> {
            canvasClicked(e.getX(), e.getY());
        });
    }

    public void showNiceMap() {
        exampleMap.fire();
    }

    public void renderCanvas() {
        // full rendering of the map
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Paint.valueOf("#212121"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int x = 0; x < global.n; x++) {
            for (int y = 0; y < global.m; y++) {
                // change color
                // gc.setFill(CELLS[map.getCell(x, y)]);

                // draw rect
                try {
                    gc.setFill(global.map.getField(x, y).getColor());
                } catch (NotAFieldException e) {
                    // should not happen
                }
                gc.fillRect(x * global.size + 1, y * global.size + 1, global.size - 1, global.size - 1);
            }
        }

        // gc.clearRect(0, canvas.getHeight() - 1, canvas.getWidth(), canvas.getHeight());
        // gc.clearRect(canvas.getWidth() - 1, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void canvasClicked(double xReal, double yReal) {
        final int x = (int) (xReal / global.size);
        final int y = (int) (yReal / global.size);
        if (xReal % global.size == 0 || yReal % global.size == 0) {
            return;
        }

        // TODO: trigger event here for point x, y
        // System.out.println("click " + xCell + " " + yCell);
        try {
            if (global.map.getField(x, y).getFieldType().equals(FieldType.GRID_POINT)) {
                global.map.setObstacle(x, y);
            } else {
                global.map.setField(x, y);
            }
        } catch (Exception e) { }
        renderCanvas();
    }

}