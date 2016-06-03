package application;

import exception.InvalidCoordinateException;
import exception.MapInitialisationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import map.MapController;
import util.Tupel2;
import util.Tupel3;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static application.ApplicationConstants.ZOOM_FACTOR;

/**
 * Created by paloka on 01.06.16.
 */
public class ApplicationController implements Initializable {

    @FXML
    private MenuItem emptyMapMenuItem, randomMapMenuItem, openMapMenuItem;

    @FXML
    private Canvas mapCanvas;

    private Stage primaryStage;
    private MapVisualisation mapVisualisationHolder;
    private int fieldSize   = 10;

    private MapController mapController = new MapController();

    private MapVisualisationFactory mapVisualisationFactory = new MapVisualisationFactory();

    private DialogExecuter dialogExecuter = new DialogExecuter();

    /* ------- Initialisation ------- */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.primaryStage   = (Stage) resources.getObject(null);

        //Init Menu
        initEmptyMapMenuItem();
        initRandomMapMenuItem();
        initOpenMapMenuItem();

        //Init Map
        initMapCanvas();
    }

    private void initEmptyMapMenuItem(){
        emptyMapMenuItem.setOnAction(event -> {
            Tupel2<Integer,Integer> params    = dialogExecuter.executeEmptyMapDialog();
            try {
                renderMap(mapVisualisationFactory.createMapVisualisation(mapController.setEmptyMap(params.getArg1(),params.getArg2())));
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: emptyMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initRandomMapMenuItem(){
        randomMapMenuItem.setOnAction(event -> {
            Tupel3<Integer,Integer,Double> params   = dialogExecuter.executeRandomMapDialog();
            try{
                renderMap(mapVisualisationFactory.createMapVisualisation(mapController.setRandomMap(params.getArg1(),params.getArg2(),params.getArg3())));
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: randomMapMenuItem.setOnAction - MapInitialisationException
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
                    renderMap(mapVisualisationFactory.createMapVisualisation(mapController.loadMap(selectedFile)));
                } catch (MapInitialisationException e) {
                    e.printStackTrace();
                    //Todo: openMapMenuItem.setOnAction - MapInitialisationException
                }
            }
        });
    }

    private void initMapCanvas(){
        mapCanvas.setOnMouseClicked((MouseEvent event) -> {
            int x   = new Double((event.getX() -1) / this.fieldSize).intValue();
            int y   = new Double((event.getY() -2) / this.fieldSize).intValue();
            try{
                renderMap(mapVisualisationFactory.createMapVisualisation(mapController.switchPassable(x,y)));
            } catch (InvalidCoordinateException e) {
                e.printStackTrace();
                //Todo: mapCanvas.setOnMouseClicked - InvalidCoordinateException
            }
        });

        mapCanvas.setOnScroll(event -> {
            System.out.println(event.toString());
            if(event.getDeltaY()>0)                 this.fieldSize  += ZOOM_FACTOR;
            if(event.getDeltaY()<0 && fieldSize>2)  this.fieldSize  -= ZOOM_FACTOR;
            renderMap(this.mapVisualisationHolder);
        });
    }


    /* ------- Helper ------- */

    private void renderMap(MapVisualisation mapVisualisation) {
        this.mapVisualisationHolder = mapVisualisation;
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        mapCanvas.setWidth(this.fieldSize*mapVisualisation.getXDim() +1);
        mapCanvas.setHeight(this.fieldSize*mapVisualisation.getYDim() +1);

        gc.setFill(Paint.valueOf("#212121"));
        gc.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        for(int x=0;x<mapVisualisation.getXDim();x++){
            for(int y=0;y<mapVisualisation.getYDim();y++){
                try {
                    gc.setFill(mapVisualisation.getColor(x,y));
                } catch (InvalidCoordinateException e) {
                    e.printStackTrace();
                    //Todo: ApplicationController.renderMap - InvalidCoordinateException
                }
                gc.fillRect(x * this.fieldSize + 1, y * this.fieldSize + 1, this.fieldSize -1, this.fieldSize - 1);
            }
        }
    }
}