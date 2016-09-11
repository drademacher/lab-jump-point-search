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

        init("wc3maps512");

        MapController controller = new MapController();
        controller.setUncutNeighborMovingRule();
        controller.setEuclideanHeuristic();
        controller.setAStarShortestPath();

        controller.loadMap(maps.get("bootybay"));
        scenarioExecuter(controller);
    }

    private static void init(String dir) {
        try {
            Files.walk(Paths.get("maps/" + dir))
                    .filter(filePath -> Files.isRegularFile(filePath))
                    .filter(filePath -> filePath.toString().substring(filePath.toString().lastIndexOf(".")).equals(".map"))
                    .forEach(filePath -> maps.put(stripFileName(filePath.toString()), filePath.toFile()));

            Files.walk(Paths.get("scenarios/" + dir))
                    .filter(filePath -> Files.isRegularFile(filePath))
                    .filter(filePath -> filePath.toString().substring(filePath.toString().lastIndexOf(".")).equals(".scen"))
                    .forEach(filePath -> scenarios.put(stripFileName(filePath.toString()), filePath.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void scenarioExecuter(MapController controller) throws NoPathFoundException {
        Vector start;
        Vector goal;

        String scen = "bootybay";

        BufferedReader br = null;
        String currentLine = "";

        try {
            br = new BufferedReader(new FileReader(scenarios.get(scen)));
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (int y = 0; ; y++) {
            try {
                currentLine = br.readLine();
            } catch (IOException e) {

            }

            if (br == null) break;
            String s[] = currentLine.split(" ");
            if (s.length != 9) continue;


            System.out.println();


            // execute algorithm and benchmark it
            start = new Vector(Integer.parseInt(s[4]), Integer.parseInt(s[5]));
            goal = new Vector(Integer.parseInt(s[6]), Integer.parseInt(s[7]));
            double cost = Double.parseDouble(s[8]);


            System.out.print(start + " " + goal + " - ");
            long preprocessingTime = controller.preprocessShortestPath();
            try {
                Tuple2<ShortestPathResult, Long> result = controller.runShortstPath(start, goal);
                double deviation = result.getArg1().getCost() - cost;
                System.out.print(String.format("deviation: %1$,.2f", deviation));
            } catch (NoPathFoundException e) {
                System.out.print("no path found");
            }


        }
    }

    static String stripFileName(String in) {
        String[] s = in.split("\\\\");
        String name = s[s.length - 1];
        name = name.substring(0, name.indexOf("."));
        return name;
    }
}
