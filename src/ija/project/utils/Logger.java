/**
 * Project name: Robot Simulation
 * File name: Logger.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Maksym Podhornyi(xpodho08)
 * Description: Logs the state of the room to a JSON file.
 */

package ija.project.utils;

import ija.project.game.*;
import java.io.*;

import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Logs the state of the room to a JSON file.
 */
public class Logger {

    /** The file to log to. */
    private final File file;

     /**      The unique ID of the frame. */
    private int uniqueID;

     /**      The current frame. */
    private int currentFrame;

     /**      Whether the frames are loaded. */
    private boolean isLoaded;

     /**      The frames. */
    private JSONArray frames;

    /**
     * Constructs a new Logger.
     *
     * @param mapName The name of the map to log as a string.
     */
    public Logger(String mapName) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateTime = formatter.format(new Date());

        this.file = new File(System.getProperty("user.dir") + File.separator + "data" +
                File.separator + "logs" + File.separator + mapName + "_" + dateTime + ".json");

        File directories = new File(file.getParent());

        if (!directories.exists()) {
            if (!directories.mkdirs()) {
                throw new RuntimeException("Error creating log directories");
            }
        }

        try {
            if (!file.createNewFile()) {
                throw new Exception("File already exists");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating log file");
        }

        this.uniqueID = 0;
        this.currentFrame = 0;

    }

    /**
     * Logs the state of the room.
     *
     * @param room The room to log as an instance of the Room class.
     */
    public void log(Room room) {

        if (isLoaded) {
            isLoaded = false;
        }

        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            long len = raf.length();

            if (len == 0) {
                raf.writeBytes("[]");
                len = raf.length();
            }

            raf.seek(len - 1);

            JSONObject main = new JSONObject();
            main.put("id", uniqueID);

            JSONObject robots = new JSONObject();
            for (AutomatedRobot robot : room.getRobots()) {
                JSONObject obj = new JSONObject();
                obj.put("x", robot.getPosition().getX());
                obj.put("y", robot.getPosition().getY());
                obj.put("angle", robot.getAngle());
                robots.put("robot" + room.getRobots().indexOf(robot), obj);
            }
            main.put("robots", robots);

            JSONObject controlledRobots = new JSONObject();
            for (ControlledRobot robot : room.getControlledRobots()) {
                JSONObject obj = new JSONObject();
                obj.put("x", robot.getPosition().getX());
                obj.put("y", robot.getPosition().getY());
                obj.put("angle", robot.getAngle());
                controlledRobots.put("robot" + room.getControlledRobots().indexOf(robot), obj);
            }
            main.put("controlledRobots", controlledRobots);

            // Handle the comma and bracket for JSON syntax correctness
            if (len > 2) { // More than just "[]"
                raf.writeBytes(",");
            }

            // Append the new object and close the JSON array bracket
            raf.writeBytes(main.toString(4) + "]");
            raf.close();

            uniqueID++;
            currentFrame++;
        } catch (IOException e) {
            throw new RuntimeException("Error writing to log file", e);
        }
    }


    /**
     * Gets the last frame.
     *
     * @param room The room to get the last frame for as an instance of the Room class.
     * @param order The order of the frame to get.
     */
    public void getLastFrame(Room room, int order) {

        int targetId = currentFrame - 1;

        if ((order == 1 && targetId - 1 >= uniqueID) || (order == -1 && targetId < 0)) {

            return;

        }

        room.getRobots().clear();
        room.getControlledRobots().clear();

        try {

            if (!isLoaded) {
                isLoaded = true;
                String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                frames = new JSONArray(content);
                if (order == 1) {
                    currentFrame = frames.length();
                }
            }

            for (int i = 0; i < frames.length(); i++) {
                JSONObject frame = frames.getJSONObject(i);
                if (frame.getInt("id") == targetId) {

                    if (!frame.getJSONObject("robots").isEmpty()) {

                        JSONArray robots = frame.getJSONObject("robots").names();
                        for (int j = 0; j < robots.length(); j++) {
                            JSONObject robot = frame.getJSONObject("robots").getJSONObject(robots.getString(j));
                            AutomatedRobot automatedRobot = new AutomatedRobot(new Position(robot.getDouble("x"), robot.getDouble("y")), room);
                            automatedRobot.setAngle(robot.getInt("angle"));
                            room.addRobot(automatedRobot);
                        }

                    }

                    if (!frame.getJSONObject("controlledRobots").isEmpty()) {
                        JSONArray controlledRobots = frame.getJSONObject("controlledRobots").names();
                        for (int j = 0; j < controlledRobots.length(); j++) {
                            JSONObject robot = frame.getJSONObject("controlledRobots").getJSONObject(controlledRobots.getString(j));
                            ControlledRobot controlledRobot = new ControlledRobot(new Position(robot.getDouble("x"), robot.getDouble("y")), room);
                            controlledRobot.setAngle(robot.getInt("angle"));
                            room.addControlledRobot(controlledRobot);
                        }
                    }

                    break;
                }
            }

            if (order == 1) {
                currentFrame++;
            } else {
                currentFrame--;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error reading from log file");
        }

    }

    /**
     * Resets the logger.
     */
    public void reset() {

        try {
            String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
            JSONArray frames = new JSONArray(content);
            JSONArray newFrames = new JSONArray();
            for (int i = 0; i < frames.length(); i++) {
                if (frames.getJSONObject(i).getInt("id") < currentFrame) {
                    newFrames.put(frames.getJSONObject(i));
                }
            }
            try (FileWriter writer = new FileWriter(file, false)) { // overwrite the file
                writer.write(newFrames.toString(4));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error reading from log file");
        }

        this.uniqueID = currentFrame;

    }




}
