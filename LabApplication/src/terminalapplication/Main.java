package terminalapplication;

import core.exception.MapInitialisationException;
import core.exception.NoPathFoundException;
import core.map.MapController;
import core.map.shortestpath.ShortestPathResult;
import core.util.Tuple2;
import core.util.Vector;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

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

    public static void main(String[] args) throws MapInitialisationException, NoPathFoundException, FileNotFoundException, UnsupportedEncodingException {
        // System.out.println("Welcome to the terminal application of LabApplication:");

        init(args[0]);

        MapController controller = new MapController();
        controller.setUncutNeighborMovingRule();
        controller.setEuclideanHeuristic();

        switch (args[1]) {
            case "astar":
                controller.setAStarShortestPath();
                break;
            case "astarbb":
                controller.setAStarBBShortestPath();
                break;
            case "jps":
                controller.setJPSShortestPath();
                break;
            case "jpsbb":
                controller.setJPSBBShortestPath();
                break;
            case "jps+":
                controller.setJPSPlusShortestPath();
                break;
            case "jps+bb":
                controller.setJPSPlusBBShortestPath();
                break;
            default:
                throw new IllegalArgumentException("second argument is no known algorithm of the following: astar, astarbb, jps, jpsbb, jps+, jps+bb");
        }



        scenarioExecuter(args, controller);
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

    static void scenarioExecuter(String[] args, MapController controller) throws NoPathFoundException, MapInitialisationException, FileNotFoundException, UnsupportedEncodingException {


        for (String scenario : scenarios.keySet()) {
            System.out.println("start benchmarking " + scenario);

            controller.loadMap(maps.get(scenario));
            long timePreprossessing = controller.preprocessShortestPath();


            PrintWriter writer = new PrintWriter("benchmarking/" + args[0] + "/" + scenario + "_" + args[1] + ".txt", "UTF-8");

            writer.println("type: " + args[0]);
            writer.println("name: " + scenario);
            writer.println("algorithm: " + args[1]);
            writer.println("preprossessing [ms]: " + timePreprossessing);

            writer.println("\n"
                    + "start x\t"
                    + "start y\t"
                    + "goal x\t"
                    + "goal y\t"
                    + "deviation\t"
                    + "origin cost\t"
                    + "measured cost\t"
                    + "processed points\t"
                    + "measured time [ms]");

            try (Stream<String> stream = Files.lines(scenarios.get(scenario).toPath())) {
                stream.forEach(input -> {
                    String data[] = input.split("\t");
                    if (data.length != 9) return;

                    // execute algorithm and benchmark it
                    Vector startScenario = new Vector(Integer.parseInt(data[4]), Integer.parseInt(data[5]));
                    Vector goalScenario = new Vector(Integer.parseInt(data[6]), Integer.parseInt(data[7]));
                    double costScenario = Double.parseDouble(data[8]);


                    Tuple2<ShortestPathResult, Long> result;
                    double deviation = 0;
                    boolean noPath;

                    try {
                        result = controller.runShortstPath(startScenario, goalScenario);
                        deviation = result.getArg1().getCost() - costScenario;
                        noPath = false;

                        writer.println(startScenario.getX() + "\t"
                                + startScenario.getY() + "\t"
                                + goalScenario.getX() + "\t"
                                + goalScenario.getY() + "\t"
                                + String.format("%1$,.5f", deviation) + "\t"
                                + String.format("%1$,.5f", costScenario) + "\t"
                                + String.format("%1$,.5f", result.getArg1().getCost()) + "\t"
                                + (result.getArg1().getOpenList().size() + result.getArg1().getClosedList().size()) + "\t"
                                + result.getArg2() );
                    } catch (NoPathFoundException e) {
                        noPath = true;

                        writer.println(startScenario + "\t"
                                + goalScenario + "\t"
                                + "no path found" );
                    }

                    if ((Math.abs(deviation) > 0.01) || noPath) {
                        System.out.print(startScenario + " " + goalScenario + " :\t");
                        System.out.print(noPath ? "no path #\t" : String.format("div: %1$,.2f", deviation));
                        System.out.println();
                    }


                });
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            writer.close();
        }
    }

    static String stripFileName(String in) {
        String[] s = in.split("/");
        String name = s[s.length - 1];
        name = name.substring(0, name.indexOf("."));
        return name;
    }
}
