package terminalapplication;

import exception.MapInitialisationException;
import exception.NoPathFoundExeception;
import map.MapController;
import map.shortestpath.ShortestPathResult;
import util.Vector;

import java.io.File;

/**
 * Created by paloka on 07.09.16.
 */
public class Main {

    public static void main(String[] args) throws MapInitialisationException, NoPathFoundExeception {
        System.out.println("Welcome to the terminal application of LabApplication:");

        System.out.println("\n\nInput:\n");

        File file   = new File(args[0]);
        Vector start    = new Vector(Integer.parseInt(args[1]),Integer.parseInt(args[2]));
        Vector goal     = new Vector(Integer.parseInt(args[3]),Integer.parseInt(args[4]));
        System.out.println("Mapfile\t"+file);
        System.out.println("Start\t"+start);
        System.out.println("Goal\t"+goal);

        MapController controller    = new MapController();
        controller.setUncutNeighborMovingRule();
        controller.setEuclideanHeuristic();
        controller.loadMap(file);

        System.out.println("\n\nExecuting:\n");

        controller.setAStarShortestPath();
        System.out.println("Run AStar...");
        ShortestPathResult aStarResult  = controller.runShortstPath(start,goal);

        controller.setJPSShortestPath();
        System.out.println("Run JPS...");
        ShortestPathResult jpsResult  = controller.runShortstPath(start,goal);

        controller.setJPSPlusShortestPath();
        System.out.println("Preprocess JPS+...");
        controller.preprocessShortestPath();
        System.out.println("Run JPS+...");
        ShortestPathResult jpsPlusResult  = controller.runShortstPath(start,goal);

        controller.setJPSPlusBBShortestPath();
        System.out.println("Preprocess JPS+BB...");
        controller.preprocessShortestPath();
        System.out.println("Run JPS+BB...");
        ShortestPathResult jpsPlusBBResult  = controller.runShortstPath(start,goal);

        controller.setAStarBBShortestPath();
        System.out.println("Preprocess AStarBB...");
        controller.preprocessShortestPath();
        System.out.println("Run AStarBB...");
        ShortestPathResult aStarBBResult  = controller.runShortstPath(start,goal);

        controller.setJPSBBShortestPath();
        System.out.println("Preprocess JPSBB...");
        controller.preprocessShortestPath();
        System.out.println("Run JPSBB...");
        ShortestPathResult jpsBBResult  = controller.runShortstPath(start,goal);

        System.out.println("\n\nResults:\n");
        System.out.println("Algorithm\tCosts    \tPreprocessing\tRunning");
        System.out.println("--------------------------------------------------");
        System.out.println("AStar    \t" + String.format("%1$,.4f ",aStarResult.getCost()));
        System.out.println("JPS      \t" + String.format("%1$,.4f ",jpsResult.getCost()));
        System.out.println("JPS+     \t" + String.format("%1$,.4f ",jpsPlusResult.getCost()));
        System.out.println("JPS+BB   \t" + String.format("%1$,.4f ",jpsPlusBBResult.getCost()));
        System.out.println("AStarBB  \t" + String.format("%1$,.4f ",aStarBBResult.getCost()));
        System.out.println("JPSBB    \t" + String.format("%1$,.4f ",jpsBBResult.getCost()));
    }
}
