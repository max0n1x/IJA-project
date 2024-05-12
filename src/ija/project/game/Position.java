/**
 * Project name: Robot Simulation
 * File name: Position.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Maksym Podhornyi(xpodho08)
 * Description: Class representing a 2D position.
 */

package ija.project.game;

/**
 * Class representing a 2D position.
 */
public class Position {

    /** X coordinate. */
    private final double x;

    /** Y coordinate. */
    private final double y;

    /**
     * Constructs a new Position at the specified coordinates.
     *
     * @param x The X coordinate of the position.
     * @param y The Y coordinate of the position.
     */
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the X coordinate of the position.
     *
     * @return The X coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the Y coordinate of the position.
     *
     * @return The Y coordinate.
     */
    public double getY() {
        return y;
    }
}
