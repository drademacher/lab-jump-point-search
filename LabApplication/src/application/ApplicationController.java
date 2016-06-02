package application;

import map.MapController;
import exception.InvalideCoordinateException;
import exception.MapInitialisationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by paloka on 01.06.16.
 */
public class ApplicationController implements Initializable {

    @FXML
    private MenuItem emptyMapMenuItem, randomMapMenuItem, openMenuItem;

    @FXML
    private Canvas mapCanvas;

    private MapController mapController = new MapController();

    private MapVisualisationFactory mapVisualisationFactory = new MapVisualisationFactory();

    /* ------- Initialisation ------- */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Init Menu
        initEmptyMapMenuItem();
        initRandomMapMenuItem();

        //Init Map
        initMapCanvas();
    }

    private void initEmptyMapMenuItem(){
        emptyMapMenuItem.setOnAction(event -> {
            try {
                renderMap(mapVisualisationFactory.createMapVisualisation(mapController.setEmptyMap(10,5)));
                //Todo: Set dimenstion of empty Map in Gui
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: emptyMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initRandomMapMenuItem(){
        randomMapMenuItem.setOnAction(event -> {
            try{
                renderMap(mapVisualisationFactory.createMapVisualisation(mapController.setRandomMap(10,5,0.8)));
                //Todo: Set dimenstion and probability of random Map in Gui
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: randomMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initMapCanvas(){
        mapCanvas.setOnMouseClicked((MouseEvent event) -> {
            int x   = new Double((event.getX() -1) / ApplicationConstants.FIELD_SIZE).intValue();
            int y   = new Double((event.getY() -2) / ApplicationConstants.FIELD_SIZE).intValue();
            try{
                renderMap(mapVisualisationFactory.createMapVisualisation(mapController.switchPassable(x,y)));
            } catch (InvalideCoordinateException e) {
                e.printStackTrace();
                //Todo: mapCanvas.setOnMouseClicked - InvalideCoordinateException
            }
        });
    }


    /* ------- Helper ------- */

    private void renderMap(MapVisualisation mapVisualisation) {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        mapCanvas.setWidth(ApplicationConstants.FIELD_SIZE*mapVisualisation.getXDim() +1);
        mapCanvas.setHeight(ApplicationConstants.FIELD_SIZE*mapVisualisation.getYDim() +1);

        gc.setFill(Paint.valueOf("#212121"));
        gc.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        for(int x=0;x<mapVisualisation.getXDim();x++){
            for(int y=0;y<mapVisualisation.getYDim();y++){
                try {
                    gc.setFill(mapVisualisation.getColor(x,y));
                } catch (InvalideCoordinateException e) {
                    e.printStackTrace();
                    //Todo: ApplicationController.renderMap - InvalideCoordinateException
                }
                gc.fillRect(x * ApplicationConstants.FIELD_SIZE + 1, y * ApplicationConstants.FIELD_SIZE + 1, ApplicationConstants.FIELD_SIZE -1, ApplicationConstants.FIELD_SIZE - 1);
            }
        }
    }
}