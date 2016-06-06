package application;

import exception.InvalidCoordinateException;
import exception.MapInitialisationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import map.MapController;
import util.Tuple2;
import util.Tuple3;

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
    private MenuItem openMapMenuItem;

    @FXML
    private MenuItem editMapMenuItem;

    @FXML
    private Canvas mapCanvas;

    private Stage primaryStage;
    private MapVisualisationHolder mapVisualisationHolder;

    private MapController mapController = new MapController();

    private DialogExecuter dialogExecuter = new DialogExecuter();

    /* ------- Initialisation ------- */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.primaryStage   = (Stage) resources.getObject(null);

        //Init Menu
        initEmptyMapMenuItem();
        initRandomMapMenuItem();
        initMazeMapMenuItem();
        initMazeRoomMapMenuItem();
        initSingleRoomMapMenuItem();
        initDoubleRoomMapMenuItem();
        initLoopRoomMapMenuItem();
        initOpenMapMenuItem();
        initEditMapMenuItem();

        //Init mapVisualisationHolder
        this.mapVisualisationHolder = new MapVisualisationHolder(this.mapCanvas);
    }

    private void initEmptyMapMenuItem(){
        emptyMapMenuItem.setOnAction(event -> {
            Tuple2<Integer,Integer> params    = dialogExecuter.executeMapDimensionDialog("New Empty Map");
            try {
                this.mapVisualisationHolder.setMap(mapController.setEmptyMap(params.getArg1(),params.getArg2()));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: emptyMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initRandomMapMenuItem(){
        randomMapMenuItem.setOnAction(event -> {
            Tuple3<Integer,Integer,Double> params   = dialogExecuter.executeRandomMapDialog();
            try{
                this.mapVisualisationHolder.setMap(mapController.setRandomMap(params.getArg1(),params.getArg2(),params.getArg3()));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: randomMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initMazeMapMenuItem(){
        mazeMapMenuItem.setOnAction(event -> {
            Tuple2<Integer,Integer> params    = dialogExecuter.executeMapDimensionDialog("New Maze Map");
            try {
                this.mapVisualisationHolder.setMap(mapController.setMazeMap(params.getArg1(),params.getArg2()));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: mazeMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initMazeRoomMapMenuItem(){
        mazeRoomMapMenuItem.setOnAction(event -> {
            Tuple2<Integer,Integer> params    = dialogExecuter.executeMapDimensionDialog("New Maze Room Map");
            try {
                this.mapVisualisationHolder.setMap(mapController.setMazeRoomMap(params.getArg1(),params.getArg2()));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: mazeRoomMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initSingleRoomMapMenuItem(){
        singleRoomMapMenuItem.setOnAction(event -> {
            Tuple2<Integer,Integer> params    = dialogExecuter.executeMapDimensionDialog("New Single Room Map");
            try {
                this.mapVisualisationHolder.setMap(mapController.setSingleRoomMap(params.getArg1(),params.getArg2()));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: singleRoomMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initDoubleRoomMapMenuItem(){
        doubleRoomMapMenuItem.setOnAction(event -> {
            Tuple2<Integer,Integer> params    = dialogExecuter.executeMapDimensionDialog("New Double Room Map");
            try {
                this.mapVisualisationHolder.setMap(mapController.setDoubleRoomMap(params.getArg1(),params.getArg2()));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: doubleRoomMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initLoopRoomMapMenuItem(){
        loopRoomMapMenuItem.setOnAction(event -> {
            Tuple2<Integer,Integer> params    = dialogExecuter.executeMapDimensionDialog("New Loop Room Map");
            try {
                this.mapVisualisationHolder.setMap(mapController.setLoopRoomMap(params.getArg1(),params.getArg2()));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: loopRoomMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initOpenMapMenuItem(){
        openMapMenuItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Map Files", "*.map"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(this.primaryStage);
            if (selectedFile != null) {
                try {
                    this.mapVisualisationHolder.setMap(mapController.loadMap(selectedFile));
                    setEditMapMode();
                } catch (MapInitialisationException e) {
                    e.printStackTrace();
                    //Todo: openMapMenuItem.setOnAction - MapInitialisationException
                }
            }
        });
    }

    private void initEditMapMenuItem(){
        editMapMenuItem.setOnAction(event -> {
            setEditMapMode();
        });
    }


    /* ------- Mode Setter ------- */

    private void setEditMapMode(){
        this.mapVisualisationHolder.setOnMouseClickedCallback((x,y) -> {
            try {
                this.mapVisualisationHolder.setMap(this.mapController.switchPassable(x,y));
            } catch (InvalidCoordinateException e) {
                e.printStackTrace();
                //Todo: setEditMapMode.mapConroller.switchPassable - InvalidCoordinateException
            }
        });
    }
}