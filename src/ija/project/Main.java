/**
 * Project name: Robot Simulation
 * File name: Main.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Neonila Mashlai(xmashl00)
 * Description: Entry point of the application.
 */

package ija.project;

import ija.project.view.MenuView;

/**
 * The Main class is the entry point of the application.
 */
public class Main {

    /**
     * Constructs a new instance of the Main class.
     */
    public Main() {}

    /**
     * The main method that starts the application by rendering the main menu.
     *
     * @param args The command-line arguments(ignored).
     */
    public static void main(String[] args) {

        MenuView menuView = new MenuView();
        menuView.render();

    }
}