/**
 * Project name: Robot Simulation
 * File name: ControlledRobot.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Maksym Podhornyi(xpodho08)
 * Description: Represents a controlled robot that moves in the direction specified by the user.
 */

package ija.project.game;

import ija.project.game.common.Robot;

/**
 * Represents a controlled robot that moves in the direction specified by the user.
 */
public class ControlledRobot implements Robot {

    /**   The position of the robot. */
    private Position position;

    /**   The room in which the robot is located. */
    private final Room room;

    /**   The angle of the robot. */
    private int angle = 0;

    /**
     * Creates a new controlled robot with the specified position and room.
     *
     * @param position The position of the robot as an instance of the Position class.
     * @param room The room in which the robot is located as an instance of the Room class.
     */
    public ControlledRobot(Position position, Room room) {this.position = position;this.room = room;}

    /**
     * Turns the robot in the specified direction.
     *
     * @param direction The direction in which to turn the robot. 1 for right, -1 for left.
     */
    public void turn(int direction) {angle = (angle + (direction == 1 ? 1 : -1) + 360) % 360;}

    @Override
    public void move() {
        Position newPosition = calculateNewPosition(angle, position);

        if (canMove(newPosition.getX(), newPosition.getY(), room, position)) {

            position = newPosition;

        }

    }

    @Override
    public Position getPosition() {return position;}

    @Override
    public int getAngle() {return angle;}

    @Override
    public void setAngle(int angle) {this.angle = angle;}

}
