/**
 * Project name: Robot Simulation
 * File name: Robot.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Maksym Podhornyi(xpodho08)
 * Description: Represents a robot.
 */

package ija.project.game.common;

import ija.project.game.*;
import javafx.scene.shape.*;

/**
 * Represents a robot.
 */
public interface Robot {

    /**
     * Returns the position of the robot.
     *
     * @return The position of the robot as an instance of the Position class.
     */
    Position getPosition();

    /**
     * Returns the angle of the robot.
     *
     * @return The angle of the robot.
     */
    int getAngle();

    /**
     * Sets the angle of the robot.
     *
     * @param angle The angle to set.
     */
    void setAngle(int angle);

    /**
     * Turns the robot.
     */
    default void turn() {throw new UnsupportedOperationException("Not supported yet.");}

    /**
     * Checks if the robot can move to the specified position.
     *
     * @param x The X coordinate of the next position.
     * @param y The Y coordinate of the next position.
     * @param room The room in which the robot is located as an instance of the Room class.
     * @param position The position of the robot as an instance of the Position class.
     * @return True if the robot can move to the specified position, false otherwise.
     */
    default boolean canMove(double x, double y, Room room, Position position) {

        if (x < 0 || x >= 1110 || y < 0 || y >= 630) {
            return false;
        }

        for (AutomatedRobot robot  : room.getRobots()) {

            Circle robot1 = new Circle(x + 15, y + 15, 14);
            Circle robot2 = new Circle(robot.getPosition().getX() + 15, robot.getPosition().getY() + 15, 14);

            if ( robot1.getBoundsInParent().intersects(robot2.getBoundsInParent())  &&
                    !robot.getPosition().equals(position) ) {
                return false;
            }

        }

        for (Obstacle obstacle : room.getObstacles()) {
            if (checkCollision(new Rectangle(obstacle.getPosition().getX(), obstacle.getPosition().getY(), 29, 29),
                    new Circle(x + 15, y + 15, 15))) {
                return false;
            }
        }

        if (!room.getControlledRobots().isEmpty()) {

            ControlledRobot controlledRobot = room.getControlledRobots().get(0);
            Circle robot1 = new Circle(x + 15, y + 15, 14);
            Circle robot2 = new Circle(controlledRobot.getPosition().getX() + 15, controlledRobot.getPosition().getY() + 15, 14);

            return !robot1.getBoundsInParent().intersects(robot2.getBoundsInParent()) ||
                    controlledRobot.getPosition().equals(position);

        } else {
            return true;
        }

    }

    /**
     * Calculates the new position of the robot.
     */
    void move();

    /**
     * Checks if a collision occurs between a rectangle and a circle as of representation of the robot and an obstacle.
     *
     * @param rect The rectangle as an instance of the Rectangle class.
     * @param circle The circle as an instance of the Circle class.
     * @return True if a collision occurs between the rectangle and the circle, false otherwise.
     */
    default boolean checkCollision(Rectangle rect, Circle circle) {

        return (Math.pow(circle.getCenterX() - Math.max(rect.getX(), Math.min(rect.getX() + rect.getWidth(), circle.getCenterX())), 2) +
                Math.pow(circle.getCenterY() - Math.max(rect.getY(), Math.min(rect.getY() + rect.getHeight(), circle.getCenterY())), 2))
                <= Math.pow(circle.getRadius(), 2);

    }

    /**
     * Calculates the new position of the robot.
     *
     * @param angle The angle to calculate the new position.
     * @param position The position of the robot as an instance of the Position class.
     * @return The new position of the robot as an instance of the Position class.
     */
    default Position calculateNewPosition(int angle, Position position) {

        return new Position(position.getX() + Math.cos(Math.toRadians(angle)),
                position.getY() + Math.sin(Math.toRadians(angle)));

    }


}
