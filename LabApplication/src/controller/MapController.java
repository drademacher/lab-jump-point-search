package controller;

import exception.InvalideCoordinateException;
import exception.MapInitialisationException;
import model.map.Map;
import model.map.MapFactory;
import model.visualisation.MapVisualisation;
import model.visualisation.MapVisualisationFactory;

public class MapController {

    private Map map;

    public MapVisualisation setEmptyMap(int xDim, int yDim) throws MapInitialisationException {
        this.map    = MapFactory.createEmptyMap(xDim,yDim);
        return MapVisualisationFactory.createMapVisualisation(map);
    }

    public MapVisualisation switchPassable(int x, int y) throws InvalideCoordinateException {
        map.switchPassable(x,y);
        return MapVisualisationFactory.createMapVisualisation(map);
    }
}
