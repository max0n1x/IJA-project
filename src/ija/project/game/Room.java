/**
 * Project name: Robot Simulation
 * File name: Room.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Neonila Mashlai(xmashl00)
 * Description: Represents a room with obstacles and robots.
 */

package ija.project.game;

import ija.project.game.common.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a room with obstacles and robots.
 */
public class Room implements Environment {

    /** List of obstacles in the room. */
    private final List<Obstacle> obstacles = new ArrayList<>();

    /** List of robots in the room. */
    private final List<AutomatedRobot> robots = new ArrayList<>();

    /** List of controlled robots in the room. */
    private final List<ControlledRobot> controlledRobots = new ArrayList<>();

    /**
     * Constructs a new Room.
     */
    public Room() {}

    @Override
    public boolean addObstacle(Obstacle obstacle) {

        if (obstacleAtPosition(obstacle.getPosition().getX(), obstacle.getPosition().getY()) ||
                robotAtPosition(obstacle.getPosition().getX(), obstacle.getPosition().getY())){
            return false;
        }

        return obstacles.add(obstacle);

    }

    @Override
    public boolean obstacleAtPosition(double x, double y) {
        for (Obstacle obstacle : obstacles) {

            if (obstacle.getPosition().getX() == x && obstacle.getPosition().getY() == y) {

                return true;

            }

        }
        return false;
    }

    @Override
    public List<Obstacle> getObstacles() { return obstacles; }

    @Override
    public void removeObstacle(Obstacle obstacle) { obstacles.remove(obstacle); }

    @Override
    public boolean addRobot(AutomatedRobot robot) {

        if (robotAtPosition(robot.getPosition().getX(), robot.getPosition().getY()) || obstacleAtPosition(robot.getPosition().getX(), robot.getPosition().getY())) {

            return false;

        }

        return robots.add(robot);

    }

    @Override
    public boolean robotAtPosition(double x, double y) {

        for (AutomatedRobot robot : robots) {
            if (robot.getPosition().getX() == x && robot.getPosition().getY() == y) { return true; }
        }

        for (ControlledRobot robot : controlledRobots) {
            if (robot.getPosition().getX() == x && robot.getPosition().getY() == y) { return true; }
        }

        return false;

    }

    @Override
    public List<AutomatedRobot> getRobots() { return robots; }

    @Override
    public void removeRobot(AutomatedRobot robot) {
        robots.remove(robot);
    }

    @Override
    public boolean addControlledRobot(ControlledRobot robot) {

        if (robotAtPosition(robot.getPosition().getX(), robot.getPosition().getY())
                || obstacleAtPosition(robot.getPosition().getX(), robot.getPosition().getY())) {

            return false;

        }

        return controlledRobots.add(robot);

    }

    @Override
    public List<ControlledRobot> getControlledRobots() { return controlledRobots; }

    @Override
    public void removeControlledRobot(ControlledRobot robot) { controlledRobots.remove(robot); }

}
