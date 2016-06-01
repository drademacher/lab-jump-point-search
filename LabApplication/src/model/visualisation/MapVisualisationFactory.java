package model.visualisation;

import exception.InvalideCoordinateException;
import exception.MapInitialisationException;
import model.map.Map;

/**
 * Created by paloka on 01.06.16.
 */
public class MapVisualisationFactory {

    public static MapVisualisation createMapVisualisation(Map map) {
        MapVisualisation mapVisualisation = null;
        try {
            mapVisualisation = new MapVisualisation(map.getXDim(),map.getYDim(), FieldVisualisation.GRID_POINT);
        } catch (MapInitialisationException e) {
            e.printStackTrace();
            //Todo: MapVisualisationFactory.createMapVisualisation - MapInitialisationException
        }
        for(int x = 0; x<map.getXDim(); x++){
            for(int y=0;y<map.getYDim();y++){
                try {
                    if(!map.isPassable(x,y)) mapVisualisation.setFieldVisualisation(x,y,FieldVisualisation.OBSTACLE_POINT);
                } catch (InvalideCoordinateException e) {
                    e.printStackTrace();
                    //Todo: MapVisualisationFactory.createMapVisualisation - InvalideCoordinateException
                }
            }
        }
        return mapVisualisation;
    }
}
