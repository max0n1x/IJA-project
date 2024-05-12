/**
 * Project name: Robot Simulation
 * File name: AutomatedRobot.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Maksym Podhornyi(xpodho08)
 * Description: Represents an automated robot that moves randomly.
 */

package ija.project.game;

import ija.project.game.common.Robot;

import java.util.Random;

/**
 * Represents an automated robot that moves randomly.
 */
public class AutomatedRobot implements Robot {

    /** The position of the robot. */
    private Position position;

     /** The room in which the robot is located. */
    private final Room room;

     /** The angle of the robot. */
    private int angle = 0;

     /** The real angle of the robot to smoothly turn it. */
    private int real_angle = 0;

    /**
     * Creates a new automated robot with the specified position and room.
     *
     * @param position The position of the robot as an instance of the Position class.
     * @param room The room in which the robot is located as an instance of the Room class.
     */
    public AutomatedRobot(Position position, Room room) {this.position = position;this.room = room;}

    @Override
    public Position getPosition() {return position;}

    /**
     * Turns the robot by a random angle.
     */
    @Override
    public void turn() {angle = (angle + new Random().nextInt(360)) % 360;}

    @Override
    public void move() {
        adjustAngle();
        if (isAngleAdjusted()) {return;}

        Position newPosition = calculateNewPosition(angle, position);

        if (canMove(newPosition.getX(), newPosition.getY(), room, position)) {

            position = newPosition;

        } else {

            turn();

        }

    }

    @Override
    public int getAngle() {return real_angle;}

    @Override
    public void setAngle(int angle) {this.real_angle = angle;}

    /**
     * Adjusts the angle of the robot to smoothly turn it.
     */
    private void adjustAngle() {
        if (real_angle < angle) {
            real_angle += 1;
        } else if (real_angle > angle) {
            real_angle -= 1;
        }
    }

    /**
     * Checks if the angle of the robot is adjusted.
     *
     * @return True if the angle of the robot is adjusted, false otherwise.
     */
    private boolean isAngleAdjusted() {return real_angle != angle;}

}
