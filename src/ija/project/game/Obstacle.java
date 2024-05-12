/**
 * Project name: Robot Simulation
 * File name: Obstacle.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Neonila Mashlai(xmashl00)
 * Description: Represents an obstacle on the map.
 */

package ija.project.game;

/**
 * Represents an obstacle on the map.
 */
public class Obstacle {

    /**   The position of the obstacle; */
    private final Position position;

    /**
     * Creates a new obstacle with the specified position.
     *
     * @param position The position of the obstacle as an instance of the Position class.
     */
    public Obstacle(Position position) { this.position = position; }

    /**
     * Returns the position of the obstacle.
     *
     * @return The position of the obstacle as an instance of the Position class.
     */
    public Position getPosition() { return position; }

}
