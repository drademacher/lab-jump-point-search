package map.shortestpath;

import exception.NoPathFoundExeception;
import map.MapFacade;
import map.heuristic.Heuristic;
import map.movingrule.MovingRule;
import util.Vector;

/**
 * Created by paloka on 08.06.16.
 */
public class ShortestPathStrategy {

    private ShortestPath shortestPath;

    /* ------- ShortestPath Operations ------- */

    public void preprocess(MapFacade map, MovingRule movingRule) {
        this.shortestPath.doPreprocessing(map, movingRule);
    }

    public ShortestPathResult run(MapFacade map, Vector start, Vector goal, Heuristic heuristic, MovingRule movingRule) throws NoPathFoundExeception {
        return this.shortestPath.findShortestPath(map, start, goal, heuristic, movingRule);
    }


    /* ------- ShortestPath Setter ------- */

    public void setAStarShortestPath() {
        this.shortestPath = new AStarShortestPath(this, new NoShortestPathPruning());
    }

    public void setJPSShortestPath() {
        this.shortestPath = new JPSShortestPath(this, new NoShortestPathPruning());
    }

    public void setJPSPlusShortestPath() {
        this.shortestPath = new PreCalculatedShortestPath(new JPSPreCalculationShortestPathPreprocessing(this), new NoShortestPathPruning());
    }

    public void setJPSPlusBBShortestPath() {
        this.shortestPath = new PreCalculatedShortestPath(new JPSPreCalculationShortestPathPreprocessing(this), new JPSBoundingBoxesShortestPathPruning());
    }

    public void setAStarBBShortestPath() {
        this.shortestPath   = new AStarShortestPath(this, new AStarBoundingBoxesShortestPathPruning());
    }

    public void setJPSBBShortestPath(){
        this.shortestPath   = new JPSShortestPath(this, new JPSBoundingBoxesShortestPathPruning());
    }
}