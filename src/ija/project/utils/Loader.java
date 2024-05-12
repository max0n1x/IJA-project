/**
 * Project name: Robot Simulation
 * File name: Loader.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Neonila Mashlai(xmashl00)
 * Description: Represents a loader for loading the map from file and saving the map to file.
 */

package ija.project.utils;

import java.io.*;
import java.nio.file.Files;

import ija.project.game.*;
import org.json.*;

/**
 * Loader class.
 * Represents a loader for loading the map from file and saving the map to file.
 */
public class Loader {

    /** The room to load the map to. */
    private final Room room;

    /**
     * Constructs a new Loader.
     */
    public Loader() { room = new Room(); }

    /**
     * Loads the map from the file.
     *
     * @param map The name of the map to load as a string.
     */
    public void load(String map) {

        if (map.equals("new")) {
            return;
        }

        try {
            File mapFile = new File(System.getProperty("user.dir") + "/data/maps/" + map + ".json");

            JSONObject obj = new JSONObject(new String(Files.readAllBytes(mapFile.toPath())));

            JSONObject obstacles = obj.getJSONObject("obstacles");

            for (int i = 0; i < obstacles.length(); i++) {
                JSONObject obstacle = obstacles.getJSONObject("obstacle" + i);
                room.addObstacle(new Obstacle(new Position(obstacle.getInt("x"), obstacle.getInt("y"))));
            }

            JSONObject robots = obj.getJSONObject("robots");

            for (int i = 0; i < robots.length(); i++) {
                JSONObject robot = robots.getJSONObject("robot" + i);
                room.addRobot(new AutomatedRobot(new Position(robot.getInt("x"), robot.getInt("y")), room));
            }

            JSONObject controlledRobots = obj.getJSONObject("controlledRobots");

            for (int i = 0; i < controlledRobots.length(); i++) {
                JSONObject robot = controlledRobots.getJSONObject("robot" + i);
                room.addControlledRobot(new ControlledRobot(new Position(robot.getInt("x"), robot.getInt("y")), room));
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Map file not found");
        } catch (IOException e) {
            throw new RuntimeException("Error reading map file");
        }
    }

    /**
     * Returns the list of maps.
     *
     * @return The list of maps as an array of strings.
     */
    public static String[] getMaps() {
        File folder = new File(System.getProperty("user.dir") + "/data/maps/");
        File[] listOfFiles = folder.listFiles();
        try {
            if (listOfFiles == null) {
                return new String[0];
            }

            if (listOfFiles.length == 0) {
                return new String[0];
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
            System.exit(1);

        }

        String[] maps = new String[listOfFiles.length];

        for (int i = 0; i < listOfFiles.length; i++) {

            try {

                if (!listOfFiles[i].getName().endsWith(".json")) {
                    throw new Exception("Invalid map file");
                }

            } catch (Exception e) {

                System.out.println(e.getMessage());
                System.exit(1);

            }

            maps[i] = listOfFiles[i].getName().replace(".json", "");

        }

        return maps;
    }

    /**
     * Saves the map to the file with the specified name and json format.
     *
     * @param map The name of the map to save as a string.
     * @param room The room to save as an instance of the Room class.
     */
    public void saveMap(String map, Room room) {

        JSONObject main = new JSONObject();

        JSONObject obstacles = new JSONObject();
        main.put("obstacles", obstacles);

        JSONObject robots = new JSONObject();
        main.put("robots", robots);

        JSONObject controlledRobots = new JSONObject();
        main.put("controlledRobots", controlledRobots);

        for (Obstacle obstacle : room.getObstacles()) {
            JSONObject obstacleJson = new JSONObject();
            obstacleJson.put("x", obstacle.getPosition().getX());
            obstacleJson.put("y", obstacle.getPosition().getY());
            obstacles.put("obstacle" + room.getObstacles().indexOf(obstacle), new JSONObject(obstacleJson.toString()));
        }

        for (AutomatedRobot robot : room.getRobots()) {
            JSONObject robotJson = new JSONObject();
            robotJson.put("x", robot.getPosition().getX());
            robotJson.put("y", robot.getPosition().getY());
            robots.put("robot" + room.getRobots().indexOf(robot), new JSONObject(robotJson.toString()));
        }

        for (ControlledRobot robot : room.getControlledRobots()) {
            JSONObject robotJson = new JSONObject();
            robotJson.put("x", robot.getPosition().getX());
            robotJson.put("y", robot.getPosition().getY());
            controlledRobots.put("robot" + room.getControlledRobots().indexOf(robot), new JSONObject(robotJson.toString()));
        }

        File filename = new File(System.getProperty("user.dir") + File.separator + "data" + File.separator +
                "maps" + File.separator + map + ".json");
        File directories = new File(filename.getParent());

        if (!directories.exists()) {
            if (!directories.mkdirs()) {
                throw new RuntimeException("Error creating map directories");
            }
        }

        try (FileWriter file = new FileWriter(System.getProperty("user.dir") +
                File.separator + "data" + File.separator +
                "maps" + File.separator + map + ".json")) {

            file.write(main.toString(4));
            file.flush();

        } catch (IOException e) {

            throw new RuntimeException(e);

        }
    }

    /**
     * Deletes the map with the specified name.
     *
     * @param map The name of the map to delete as a string.
     */
    public static void deleteMap(String map) {
        File mapFile = new File(System.getProperty("user.dir") + "/data/maps/" + map + ".json");
        if (!mapFile.delete()) {
            throw new RuntimeException("Error deleting map file");
        }
    }

    /**
     * Returns the room.
     *
     * @return The room as an instance of the Room class.
     */
    public Room getRoom() { return room; }

}
