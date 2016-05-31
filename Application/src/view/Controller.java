package view;


import controller.map.FieldType;
import controller.map.MapFactory;
import controller.map.Parser;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static view.Constant.defaultXDim;
import static view.Constant.defaultYDim;

public class Controller implements Initializable {
    public BooleanProperty editModeBool = new SimpleBooleanProperty(true);

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
            //map.createEmptyMap();
            // map = MapFactory.createEmptyMap(defaultXDim, defaultYDim);
            renderCanvas();
        });

        exampleMap.setOnAction(e -> {
            //map.setEmpty();
            // map = MapFactory.createExampleMap(defaultXDim, defaultYDim);
            renderCanvas();
        });

        randomMap.setOnAction(e -> {
            //map.setRnd(0.2);
            // map = MapFactory.createRandomMap(defaultXDim, defaultYDim, 0.2);
            renderCanvas();
        });

        genMap1.setOnAction(e -> {
            // map = MapFactory.createMazeMap(defaultXDim, defaultYDim);
            renderCanvas();
        });

        genMap2.setOnAction(e -> {
            // map = MapFactory.createMazeWithRoomsMap(defaultXDim, defaultYDim);
            renderCanvas();
        });

        genMap3.setOnAction(e -> {
            // map = MapFactory.createSingleConnRoomsMap(defaultXDim, defaultYDim);
            renderCanvas();
        });

        genMap4.setOnAction(e -> {
            // map = MapFactory.createLoopedRoomsMap(defaultXDim, defaultYDim);
            renderCanvas();
        });

        genMap5.setOnAction(e -> {
            // map = MapFactory.createDoubleConnRoomsMap(defaultXDim, defaultYDim);
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
                // map = Parser.getMap(selectedFile);
            }

            renderCanvas();
        });


        // action stuff here
        canvas.setOnMouseClicked((MouseEvent e) -> {
            canvasClicked(e.getX(), e.getY());
        });
    }

    public void renderCanvas() {
        // GridCanvas.getInstance().renderCanvas();
    }

    private void canvasClicked(double xReal, double yReal) {
        final int x = (int) (xReal / Constant.size);
        final int y = (int) (yReal / Constant.size);
        if (xReal % Constant.size == 0 || yReal % Constant.size == 0) {
            return;
        }

        // TODO: trigger event here for point x, y
        // System.out.println("click " + xCell + " " + yCell);
//        try {
//            if (map.getField(x, y).getFieldType().equals(FieldType.GRID_POINT)) {
//                map.setObstacle(x, y);
//            } else {
//                map.setField(x, y);
//            }
//        } catch (Exception e) { }
        renderCanvas();
    }

}