package terminalapplication;

import core.exception.MapInitialisationException;
import core.exception.NoPathFoundException;
import core.map.MapController;
import core.map.shortestpath.ShortestPathResult;
import core.util.Tuple2;
import core.util.Vector;

import java.io.File;

/**
 * Created by paloka on 07.09.16.
 */
public class Main {

    public static void main(String[] args) throws MapInitialisationException, NoPathFoundException {
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
        Tuple2<ShortestPathResult,Long> aStarResult  = controller.runShortstPath(start,goal);

        controller.setJPSShortestPath();
        System.out.println("Run JPS...");
        Tuple2<ShortestPathResult,Long> jpsResult  = controller.runShortstPath(start,goal);

        controller.setJPSPlusShortestPath();
        System.out.println("Preprocess JPS+...");
        long jpsPlusPreprocessingTime   = controller.preprocessShortestPath();
        System.out.println("Run JPS+...");
        Tuple2<ShortestPathResult,Long> jpsPlusResult  = controller.runShortstPath(start,goal);

        controller.setJPSPlusBBShortestPath();
        System.out.println("Preprocess JPS+BB...");
        long jpsPlusBBPreprocessingTime = controller.preprocessShortestPath();
        System.out.println("Run JPS+BB...");
        Tuple2<ShortestPathResult,Long> jpsPlusBBResult  = controller.runShortstPath(start,goal);

        controller.setAStarBBShortestPath();
        System.out.println("Preprocess AStarBB...");
        long aStarBBPreprocessingTime   = controller.preprocessShortestPath();
        System.out.println("Run AStarBB...");
        Tuple2<ShortestPathResult,Long> aStarBBResult  = controller.runShortstPath(start,goal);

        controller.setJPSBBShortestPath();
        System.out.println("Preprocess JPSBB...");
        long jpsBBPreprocessingTime     = controller.preprocessShortestPath();
        System.out.println("Run JPSBB...");
        Tuple2<ShortestPathResult,Long> jpsBBResult  = controller.runShortstPath(start,goal);

        System.out.println("\n\nResults:\n");
        System.out.println("Algorithm\tCosts    \tPreprocessing\tRunning");
        System.out.println("--------------------------------------------------");
        System.out.println("AStar    \t" + String.format("%1$,.4f ",aStarResult.getArg1().getCost())+"\t0 ms\t\t" + aStarResult.getArg2() + " ms");
        System.out.println("JPS      \t" + String.format("%1$,.4f ",jpsResult.getArg1().getCost())+"\t0 ms\t\t" + jpsResult.getArg2() + " ms");
        System.out.println("JPS+     \t" + String.format("%1$,.4f ",jpsPlusResult.getArg1().getCost()) +"\t"+jpsPlusPreprocessingTime+" ms\t\t" + jpsPlusResult.getArg2() + " ms");
        System.out.println("JPS+BB   \t" + String.format("%1$,.4f ",jpsPlusBBResult.getArg1().getCost()) +"\t"+jpsPlusBBPreprocessingTime+" ms\t\t" + jpsPlusBBResult.getArg2() + " ms");
        System.out.println("AStarBB  \t" + String.format("%1$,.4f ",aStarBBResult.getArg1().getCost()) +"\t"+aStarBBPreprocessingTime+" ms\t\t" + aStarBBResult.getArg2() + " ms");
        System.out.println("JPSBB    \t" + String.format("%1$,.4f ",jpsBBResult.getArg1().getCost()) +"\t"+jpsBBPreprocessingTime+" ms\t\t" + jpsBBResult.getArg2() + " ms");
    }
}
