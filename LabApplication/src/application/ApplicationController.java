package application;

import exception.InvalidCoordinateException;
import exception.MapInitialisationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import map.MapController;
import util.Coordinate;
import util.Tuple2;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by paloka on 01.06.16.
 */
public class ApplicationController implements Initializable {

    @FXML
    private MenuItem emptyMapMenuItem, randomMapMenuItem, mazeMapMenuItem, mazeRoomMapMenuItem, singleRoomMapMenuItem, doubleRoomMapMenuItem, loopRoomMapMenuItem;

    @FXML
    private MenuItem openMapMenuItem, saveMapMenuItem;

    @FXML
    private MenuItem editMapMenuItem;

    @FXML
    private ToggleGroup heuristicToggleGroup, movingRuleToggleGroup;

    @FXML
    private RadioMenuItem zeroHeuristicMenuItem, manhattenHeursticMenuItem, euclideanHeuristicMenuItem, gridHeuristicMenuItem;

    @FXML
    private RadioMenuItem orthogonalOnlyMovingRuleMenuItem, cornerCuttingMovingRuleMenuItem, noCornerCuttingMovingRuleMenuItem;

    @FXML
    private CheckMenuItem viewObstacles, viewOpenlist, viewPath, viewDetails;

    @FXML
    private MenuItem runAStarMenuItem, runJPSMenuItem;

    @FXML
    private Canvas gridCanvas, obstacleCanvas, openlistCanvas, pathCanvas, detailsCanvas;

    private Stage primaryStage;
    private MapHolder mapHolder;

    private MapController mapController;

    private DialogExecuter dialogExecuter = new DialogExecuter();

    /* ------- Initialisation ------- */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.primaryStage = (Stage) resources.getObject(null);

        //Init mapController;
        this.mapController = new MapController();

        //Init mapHolder
        this.mapHolder = new MapHolder(this.gridCanvas, this.obstacleCanvas, this.openlistCanvas, this.pathCanvas, this.detailsCanvas);

        //Init Menu
        initEmptyMapMenuItem();
        initRandomMapMenuItem();
        initMazeMapMenuItem();
        initMazeRoomMapMenuItem();
        initSingleRoomMapMenuItem();
        initDoubleRoomMapMenuItem();
        initLoopRoomMapMenuItem();
        initOpenMapMenuItem();
        initSaveMapMenuItem();
        initEditMapMenuItem();
        initHeuristicToggleGroup();
        initMovingRuleToggleGroup();
        initRunAStarMenuItem();
        initRunJPSMenuItem();
        initViews();
        // initKeyEventListener();
    }

    private void initEmptyMapMenuItem() {
        emptyMapMenuItem.setOnAction(event -> {
            Coordinate dimension = dialogExecuter.executeMapDimensionDialog("New Empty Map");
            try {
                this.mapHolder.setMap(mapController.setEmptyMap(dimension));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: emptyMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }


    private void initRandomMapMenuItem() {
        randomMapMenuItem.setOnAction(event -> {
            Tuple2<Coordinate, Double> params = dialogExecuter.executeRandomMapDialog();
            try {
                this.mapHolder.setMap(mapController.setRandomMap(params.getArg1(), params.getArg2()));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: randomMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initMazeMapMenuItem() {
        mazeMapMenuItem.setOnAction(event -> {
            Coordinate dimension = dialogExecuter.executeMapDimensionDialog("New Maze Map");
            try {
                this.mapHolder.setMap(mapController.setMazeMap(dimension));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: mazeMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initMazeRoomMapMenuItem() {
        mazeRoomMapMenuItem.setOnAction(event -> {
            Coordinate dimension = dialogExecuter.executeMapDimensionDialog("New Maze Room Map");
            try {
                this.mapHolder.setMap(mapController.setMazeRoomMap(dimension));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: mazeRoomMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initSingleRoomMapMenuItem() {
        singleRoomMapMenuItem.setOnAction(event -> {
            Coordinate dimension = dialogExecuter.executeMapDimensionDialog("New Single Room Map");
            try {
                this.mapHolder.setMap(mapController.setSingleRoomMap(dimension));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: singleRoomMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initDoubleRoomMapMenuItem() {
        doubleRoomMapMenuItem.setOnAction(event -> {
            Coordinate dimension = dialogExecuter.executeMapDimensionDialog("New Double Room Map");
            try {
                this.mapHolder.setMap(mapController.setDoubleRoomMap(dimension));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: doubleRoomMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initLoopRoomMapMenuItem() {
        loopRoomMapMenuItem.setOnAction(event -> {
            Coordinate dimension = dialogExecuter.executeMapDimensionDialog("New Loop Room Map");
            try {
                this.mapHolder.setMap(mapController.setLoopRoomMap(dimension));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: loopRoomMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initOpenMapMenuItem() {
        openMapMenuItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Map Files", "*.map"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(this.primaryStage);
            if (selectedFile != null) {
                try {
                    this.mapHolder.setMap(mapController.loadMap(selectedFile));
                    setEditMapMode();
                } catch (MapInitialisationException e) {
                    e.printStackTrace();
                    //Todo: openMapMenuItem.setOnAction - MapInitialisationException
                }
            }
        });
    }

    private void initSaveMapMenuItem() {
        saveMapMenuItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Current Map");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Map Files", "*.map"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showSaveDialog(this.primaryStage);
            if (selectedFile != null) {
                this.mapController.saveMap(selectedFile);
            }
        });
    }

    private void initEditMapMenuItem() {
        editMapMenuItem.setOnAction(event -> setEditMapMode());
    }

    private void initHeuristicToggleGroup() {
        heuristicToggleGroup.selectedToggleProperty().addListener((ov, oldT, newT) -> {
            if (newT == this.zeroHeuristicMenuItem) this.mapController.setHeuristicZero();
            if (newT == this.manhattenHeursticMenuItem) this.mapController.setHeuristicManhatten();
            if (newT == this.gridHeuristicMenuItem) this.mapController.setHeuristicGrid();
            if (newT == this.euclideanHeuristicMenuItem) this.mapController.setHeuristicEuclidean();
        });
        this.heuristicToggleGroup.selectToggle(this.gridHeuristicMenuItem);
    }

    private void initMovingRuleToggleGroup() {
        movingRuleToggleGroup.selectedToggleProperty().addListener((ov, oldT, newT) -> {
            if (newT == this.orthogonalOnlyMovingRuleMenuItem) this.mapController.setMovingRuleOrthogonalOnly();
            if (newT == this.cornerCuttingMovingRuleMenuItem) this.mapController.setMovingRuleCornerCutting();
            if (newT == this.noCornerCuttingMovingRuleMenuItem) this.mapController.setMovingRuleNoCornerCutting();
        });
        this.movingRuleToggleGroup.selectToggle(this.noCornerCuttingMovingRuleMenuItem);
    }

    private void initRunAStarMenuItem() {
        runAStarMenuItem.setOnAction(event -> {
            setSetStartGoalMode((start, goal) -> {
                this.mapHolder.setShortestPath(this.mapController.findShortestPathWithAStar(start, goal));
            });
        });
    }

    private void initRunJPSMenuItem() {
        runJPSMenuItem.setOnAction(event -> {
            setSetStartGoalMode((start, goal) -> {
                this.mapHolder.setShortestPath(this.mapController.findShortestPathWithJPS(start, goal));
            });
        });
    }



    /* ------- View Selects ------- */

    private void initViews() {


        obstacleCanvas.visibleProperty().bind(viewObstacles.selectedProperty());
        openlistCanvas.visibleProperty().bind(viewOpenlist.selectedProperty());
        pathCanvas.visibleProperty().bind(viewPath.selectedProperty());
        detailsCanvas.visibleProperty().bind(viewDetails.selectedProperty());

        obstacleCanvas.setMouseTransparent(true);
        openlistCanvas.setMouseTransparent(true);
        pathCanvas.setMouseTransparent(true);
        detailsCanvas.setMouseTransparent(true);
    }



    /* ------- Mode Setter ------- */

    private void setEditMapMode() {
        this.mapHolder.setOnMouseClickedCallback((coordinate) -> {
            try {
                this.mapController.switchPassable(coordinate);
                this.mapHolder.switchPassable(coordinate);
            } catch (InvalidCoordinateException e) {
                e.printStackTrace();
                //Todo: setEditMapMode.mapConroller.switchPassable - InvalidCoordinateException
            }
        });
        this.mapHolder.refreshMap();
    }

    private void setSetStartGoalMode(OnStartGoalSetCallback callback) {
        this.mapHolder.setOnMouseClickedCallback((start) -> {
            try {
                if (!this.mapHolder.isPassable(start)) return;
                this.mapHolder.setOnMouseClickedCallback((goal) -> {
                    try {
                        if (!this.mapHolder.isPassable(goal) || (start.equals(goal))) return;
                        this.mapHolder.setGoalPoint(goal);
                        this.mapHolder.setOnMouseClickedCallback(null);
                        callback.call(start, goal);
                    } catch (InvalidCoordinateException e) {
                        e.printStackTrace();
                        //Todo InvalidCoordinateException
                    }
                });
                this.mapHolder.setStartPoint(start);
            } catch (InvalidCoordinateException e) {
                e.printStackTrace();
                //Todo InvalidCoordinateException
            }
        });
        this.mapHolder.refreshMap();
    }

    /* ------- Callbacks ------- */

    private interface OnStartGoalSetCallback {
        void call(Coordinate start, Coordinate goal);
    }
}