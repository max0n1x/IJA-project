/**
 * Project name: Robot Simulation
 * File name: Converter.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Neonila Mashlai(xmashl00)
 * Description: Converts coordinates of the room.
 */

package ija.project.utils;

import ija.project.game.*;

/**
 * Converts coordinates of the room.
 */
public class Converter {

    /**
     * Constructs a new instance of the Converter class.
     */
    public Converter() {}

    /**
     * Converts coordinates of the room.
     *
     * @param room The room to convert as an instance of the Room class.
     * @return The room with converted coordinates.
     */
    public Room convertCoords(Room room) {
        Room newRoom = new Room();

        for (Obstacle obstacle : room.getObstacles()) {
            newRoom.addObstacle(new Obstacle(new Position(obstacle.getPosition().getX() * 30, obstacle.getPosition().getY() * 30)));
        }

        for (AutomatedRobot robot : room.getRobots()) {
            newRoom.addRobot(new AutomatedRobot(new Position(robot.getPosition().getX() * 30, robot.getPosition().getY() * 30), newRoom));
        }

        for (ControlledRobot robot : room.getControlledRobots()) {
            newRoom.addControlledRobot(new ControlledRobot(new Position(robot.getPosition().getX() * 30, robot.getPosition().getY() * 30), newRoom));
        }

        return newRoom;

    }
}
