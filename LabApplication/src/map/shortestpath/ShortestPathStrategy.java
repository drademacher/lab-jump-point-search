package map.shortestpath;

import exception.NoPathFoundException;
import map.MapFacade;
import map.heuristic.Heuristic;
import map.movingrule.MovingRule;
import util.Tuple2;
import util.Vector;

/**
 * Created by paloka on 08.06.16.
 */
public class ShortestPathStrategy {

    private ShortestPath shortestPath;

    /* ------- ShortestPath Operations ------- */

    public long preprocess(MapFacade map, MovingRule movingRule) {
        long starttime  = -System.currentTimeMillis();
        this.shortestPath.doPreprocessing(map, movingRule);
        return starttime + System.currentTimeMillis();
    }

    public Tuple2<ShortestPathResult,Long> run(MapFacade map, Vector start, Vector goal, Heuristic heuristic, MovingRule movingRule) throws NoPathFoundException {
        long starttime  = -System.currentTimeMillis();
        ShortestPathResult result   = this.shortestPath.findShortestPath(map, start, goal, heuristic, movingRule);
        return new Tuple2<>(result,starttime + System.currentTimeMillis());
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