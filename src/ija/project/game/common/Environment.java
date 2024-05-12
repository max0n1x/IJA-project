/**
 * Project name: Robot Simulation
 * File name: Environment.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Neonila Mashlai(xmashl00)
 * Description: Interface representing an environment with obstacles and robots.
 */

package ija.project.game.common;

import ija.project.game.AutomatedRobot;
import ija.project.game.ControlledRobot;
import ija.project.game.Obstacle;

import java.util.List;

/**
 * Interface representing an environment with obstacles and robots.
 */
public interface Environment {

    /**
     * Adds an obstacle to the room.
     *
     * @param obstacle The obstacle to add as an instance of the Obstacle class.
     * @return True if the obstacle was added successfully, false otherwise.
     */
    boolean addObstacle(Obstacle obstacle);

    /**
     * Checks if an obstacle is at the specified position.
     *
     * @param x The X coordinate of the position.
     * @param y The Y coordinate of the position.
     * @return True if an obstacle is at the specified position, false otherwise.
     */
    boolean obstacleAtPosition(double x, double y);

    /**
     * Returns the list of obstacles in the room.
     *
     * @return The list of obstacles in the room as a List of Obstacle instances.
     */
    List<Obstacle> getObstacles();

    /**
     * Removes an obstacle from the room.
     *
     * @param obstacle The obstacle to remove as an instance of the Obstacle class.
     */
    void removeObstacle(Obstacle obstacle);

    /**
     * Adds a robot to the room.
     *
     * @param robot The robot to add as an instance of the AutomatedRobot class.
     * @return True if the robot was added successfully, false otherwise.
     */
    boolean addRobot(AutomatedRobot robot);

    /**
     * Checks if a robot is at the specified position.
     *
     * @param x The X coordinate of the position.
     * @param y The Y coordinate of the position.
     * @return True if a robot is at the specified position, false otherwise.
     */
    boolean robotAtPosition(double x, double y);

    /**
     * Returns the list of robots in the room.
     *
     * @return The list of robots in the room as a List of AutomatedRobot instances.
     */
    List<AutomatedRobot> getRobots();

    /**
     * Removes a robot from the room.
     *
     * @param robot The robot to remove as an instance of the AutomatedRobot class.
     */
    void removeRobot(AutomatedRobot robot);

    /**
     * Adds a controlled robot to the room.
     *
     * @param robot The controlled robot to add as an instance of the ControlledRobot class.
     * @return True if the controlled robot was added successfully, false otherwise.
     */
    boolean addControlledRobot(ControlledRobot robot);

    /**
     * Returns the list of controlled robots in the room.
     *
     * @return The list of controlled robots in the room as a List of ControlledRobot instances.
     */
    List<ControlledRobot> getControlledRobots();

    /**
     * Removes a controlled robot from the room.
     *
     * @param robot The controlled robot to remove as an instance of the ControlledRobot class.
     */
    void removeControlledRobot(ControlledRobot robot);

}
