package view;


import controller.map.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static view.Consts.defaultXDim;
import static view.Consts.defaultYDim;

public class Controller implements Initializable {
    public BooleanProperty editMapBool = new SimpleBooleanProperty(false);

    private Map map;

    private int xSizeVis;
    private int ySizeVis;

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
    private Canvas canvas;

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

    @FXML
    private Button editModeStart;

    @FXML
    private Button editModeFinish;

    @FXML
    private Button setModeStart;

    @FXML
    private Button setModeFinish;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // buttons
        playButton.setOnAction(e -> System.out.println("TODO: play"));
        prevButton.setOnAction(e -> System.out.println("TODO: prev"));
        nextButton.setOnAction(e -> System.out.println("TODO: next"));
        restartButton.setOnAction(e -> System.out.println("TODO: reset"));

        // bindings for buttons and menu
//        playButton.disableProperty().bind(editModeBool);
//        prevButton.disableProperty().bind(editModeBool);
//        nextButton.disableProperty().bind(editModeBool);
//        restartButton.disableProperty().bind(editModeBool);


        mapMenu.disableProperty().bind(editMode.visibleProperty().not());
        algoMenu.disableProperty().bind(editMode.visibleProperty().not());

        editModeFinish.setOnAction(e -> {
            // do the visuals
            editMode.setVisible(false);
            setMode.setVisible(true);

            // stop edit mode
            editMapBool.setValue(false);
        });

        editModeStart.setOnAction(e -> {
            // do the visuals
            setMode.setVisible(false);
            editMode.setVisible(true);


            // TODO: remove all processing
        });

        setModeFinish.setOnAction(e -> {
            // do the visuals
            setMode.setVisible(false);
            visMode.setVisible(true);
        });

        setModeStart.setOnAction(e -> {
            // do the visuals
            visMode.setVisible(false);
            setMode.setVisible(true);

        });



        // action stuff here
        canvas.setOnMouseClicked((MouseEvent e) -> {
            canvasClicked(e.getX(), e.getY());
        });
    }

    public void postLoad() {
        menuSettings();
        canvasSettings();
    }

    private void menuSettings() {
        editMap.setOnAction(e -> {
            editMapBool.setValue(true);
        });

        emptyMap.setOnAction(e -> {
            map = MapFactory.createEmptyMap(defaultXDim, defaultYDim);
            renderCanvas();
        });

        exampleMap.setOnAction(e -> {
            map = MapFactory.createExampleMap(defaultXDim, defaultYDim);
            renderCanvas();
        });

        randomMap.setOnAction(e -> {
            map = MapFactory.createRandomMap(defaultXDim, defaultYDim, 0.2);
            renderCanvas();
        });

        genMap1.setOnAction(e -> {
            map = MapFactory.createMazeMap(defaultXDim, defaultYDim);
            renderCanvas();
        });

        genMap2.setOnAction(e -> {
            map = MapFactory.createMazeWithRoomsMap(defaultXDim, defaultYDim);
            renderCanvas();
        });

        genMap3.setOnAction(e -> {
            map = MapFactory.createSingleConnRoomsMap(defaultXDim, defaultYDim);
            renderCanvas();
        });

        genMap4.setOnAction(e -> {
            map = MapFactory.createLoopedRoomsMap(defaultXDim, defaultYDim);
            renderCanvas();
        });

        genMap5.setOnAction(e -> {
            map = MapFactory.createDoubleConnRoomsMap(defaultXDim, defaultYDim);
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
                map = Parser.getMap(selectedFile);
            }

            renderCanvas();
        });
    }

    private void canvasSettings() {
        // change listener for width change of the windows
        ChangeListener<Number> widthListener = (obs, prev, next) -> {
            final double w = ((StackPane) canvas.getParent()).getWidth();
            final double new_w = w + next.doubleValue() - prev.doubleValue() - 1;

            xSizeVis = (int) (new_w / Consts.size);
            if (xSizeVis > map.getxDim()) {
                xSizeVis = map.getxDim();
            }
            canvas.setWidth(Consts.size * xSizeVis + 1);

            renderCanvas();
        };

        // change listener for height change of the windows
        ChangeListener<Number> heightListener = (obs, prev, next) -> {
            final double h = ((StackPane) canvas.getParent()).getHeight();
            final double new_h = h + next.doubleValue() - prev.doubleValue() - 1;

            ySizeVis = (int) (new_h / Consts.size);
            if (ySizeVis > map.getyDim()) {
                ySizeVis = map.getyDim();
            }
            canvas.setHeight(Consts.size * ySizeVis + 1);

            renderCanvas();
        };





        map = new Map(defaultXDim, defaultYDim);

        Stage stage = (Stage) canvas.getScene().getWindow();
        stage.getScene().widthProperty().addListener(widthListener);
        stage.getScene().heightProperty().addListener(heightListener);

        stage.resizableProperty().set(false);
        stage.setWidth(Consts.windowWidth);
        stage.setHeight(Consts.windowHeight);
        stage.resizableProperty().set(true);
    }



    public void renderCanvas() {
        // full rendering of the map
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Paint.valueOf("#212121"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int x = 0; x < map.getxDim(); x++) {
            for (int y = 0; y < map.getyDim(); y++) {
                // change color
                // gc.setFill(CELLS[map.getCell(x, y)]);

                // draw rect
                try {
                    gc.setFill(map.getField(x, y).getColor());
                } catch (NotAFieldException e) {
                    // should not happen
                }
                gc.fillRect(x * Consts.size + 1, y * Consts.size + 1, Consts.size - 1, Consts.size - 1);
            }
        }
    }

    private void canvasClicked(double xReal, double yReal) {
        final int x = (int) (xReal / Consts.size);
        final int y = (int) (yReal / Consts.size);
        if (xReal % Consts.size == 0 || yReal % Consts.size == 0) {
            return;
        }

        // TODO: trigger event here for point x, y
        try {
            // obstacle editing only in edit mode
            if (editMapBool.getValue()) {
                if (map.getField(x, y).getFieldType().equals(FieldType.GRID_POINT)) {
                    map.setObstacle(x, y);
                } else {
                    map.setField(x, y);
                }
            }
        } catch (Exception e) {
        }
        renderCanvas();
    }

}