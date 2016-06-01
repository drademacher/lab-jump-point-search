package view;

import controller.MapController;
import exception.InvalideCoordinateException;
import exception.MapInitialisationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import model.visualisation.MapVisualisation;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by paloka on 01.06.16.
 */
public class ViewHolder implements Initializable {

    @FXML
    private MenuItem emptyMap, randomMap, open;

    @FXML
    private Canvas map;

    private MapController controller = new MapController();


    /* ------- Initialisation ------- */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Init Menu
        initEmptyMap();

        //Init Map
        initMap();

    }

    private void initEmptyMap(){
        emptyMap.setOnAction(event -> {
            try {
                renderMap(controller.setEmptyMap(10,5));
                //Todo: Set dimenstion of empty Map in Gui
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: emptyMap.setOnAction - MapInitialisationException
            }
        });
    }

    private void initMap(){
        map.setOnMouseClicked((MouseEvent event) -> {
            int x   = new Double((event.getX() -1) / ViewConstants.FIELD_SIZE).intValue();
            int y   = new Double((event.getY() -2) / ViewConstants.FIELD_SIZE).intValue();
            try{
                renderMap(controller.switchPassable(x,y));
            } catch (InvalideCoordinateException e) {
                e.printStackTrace();
                //Todo: map.setOnMouseClicked - InvalideCoordinateException
            }
        });
    }


    /* ------- Helper ------- */

    private void renderMap(MapVisualisation mapVisualisation) {
        GraphicsContext gc = map.getGraphicsContext2D();
        map.setWidth(ViewConstants.FIELD_SIZE*mapVisualisation.getXDim() +1);
        map.setHeight(ViewConstants.FIELD_SIZE*mapVisualisation.getYDim() +1);

        gc.setFill(Paint.valueOf("#212121"));
        gc.fillRect(0, 0, map.getWidth(), map.getHeight());

        for(int x=0;x<mapVisualisation.getXDim();x++){
            for(int y=0;y<mapVisualisation.getYDim();y++){
                try {
                    gc.setFill(mapVisualisation.getColor(x,y));
                } catch (InvalideCoordinateException e) {
                    e.printStackTrace();
                    //Todo: ViewHolder.renderMap - InvalideCoordinateException
                }
                gc.fillRect(x * ViewConstants.FIELD_SIZE + 1, y * ViewConstants.FIELD_SIZE + 1, ViewConstants.FIELD_SIZE -1, ViewConstants.FIELD_SIZE - 1);
            }
        }
    }
}