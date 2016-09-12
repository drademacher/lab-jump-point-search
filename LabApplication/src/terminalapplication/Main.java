package terminalapplication;

import core.exception.MapInitialisationException;
import core.exception.NoPathFoundException;
import core.map.MapController;
import core.map.shortestpath.ShortestPathResult;
import core.util.Tuple2;
import core.util.Vector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Alternative application which provides a nice way to benchmark every algorithm for given scenarios.
 *
 * @author Danny Rademacher
 * @version 1.0
 * @since 1.0
 */
public class Main {
    private static HashMap<String, File> maps = new HashMap<>();
    private static HashMap<String, File> scenarios = new HashMap<>();

    public static void main(String[] args) throws MapInitialisationException, NoPathFoundException {
        // System.out.println("Welcome to the terminal application of LabApplication:");

        init("sc1");

        MapController controller = new MapController();
        controller.setUncutNeighborMovingRule();
        controller.setEuclideanHeuristic();

        controller.loadMap(maps.get("Aurora"));
        scenarioExecuter(controller);
    }

    private static void init(String dir) {
        try {
            Files.walk(Paths.get("maps/" + dir))
                    .filter(filePath -> Files.isRegularFile(filePath))
                    .filter(filePath -> filePath.toString().substring(filePath.toString().lastIndexOf(".")).equals(".map"))
                    .forEach(filePath -> {
                        maps.put(stripFileName(filePath.toString()), filePath.toFile());
                    });

            Files.walk(Paths.get("scenarios/" + dir))
                    .filter(filePath -> Files.isRegularFile(filePath))
                    .filter(filePath -> filePath.toString().substring(filePath.toString().lastIndexOf(".")).equals(".scen"))
                    .forEach(filePath -> scenarios.put(stripFileName(filePath.toString()), filePath.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void scenarioExecuter(MapController controller) throws NoPathFoundException {
        int counterSucc = 0, counterTotal = 0;
        Vector start;
        Vector goal;

        String scen = "Aurora";

        BufferedReader br = null;
        String currentLine = "";

        try {
            br = new BufferedReader(new FileReader(scenarios.get(scen)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        controller.setJPSPlusShortestPath();
        controller.preprocessShortestPath();

        System.out.println("Preprossesing finished");


        for (int y = 0; ; y++) {
            try {
                currentLine = br.readLine();
            } catch (IOException e) {

            }

            if (currentLine == null) break;
            // System.out.print(currentLine);
            String s[] = currentLine.split("\t");
            if (s.length != 9) continue;




            // execute algorithm and benchmark it
            start = new Vector(Integer.parseInt(s[4]), Integer.parseInt(s[5]));
            goal = new Vector(Integer.parseInt(s[6]), Integer.parseInt(s[7]));
            double cost = Double.parseDouble(s[8]);


            Tuple2<ShortestPathResult, Long> result1, result2;
            double deviation1 = 0, deviation2 = 0;
            boolean nopath1, nopath2;


            // long preprocessingTime = controller.preprocessShortestPath();
//            try {
//                controller.setAStarShortestPath();
//                result1 = controller.runShortstPath(start, goal);
//                deviation1 = result1.getArg1().getCost() - cost;
//                nopath1 = false;
//
//
//            } catch (NoPathFoundException e) {
//                nopath1 = true;
//            }




            try {

                result2 = controller.runShortstPath(start, goal);
                deviation2 = result2.getArg1().getCost() - cost;
                nopath2 = false;


            } catch (NoPathFoundException e) {
                nopath2 = true;
            }

            if ( (Math.abs(deviation2) > 0.01) || nopath2 ) {
                System.out.print(start + " " + goal + " :\t");
                // System.out.print(nopath1 ? "no path #\t" : String.format("cost: %1$,.2f #\t", deviation1));
                System.out.print(nopath2 ? "no path #\t" : String.format("cost: %1$,.2f #\t", deviation2));
                System.out.println();
            } else {
                counterSucc += 1;
            }
            counterTotal += 1;
        }

        System.out.println("counterSucc of sucessfull scenarios: " + counterSucc + " / " + counterTotal);
    }

    static String stripFileName(String in) {
        String[] s = in.split("/");
        String name = s[s.length - 1];
        name = name.substring(0, name.indexOf("."));
        return name;
    }
}
