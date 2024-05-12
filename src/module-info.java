/**
 * Project name: Robot Simulation
 * File name: module-info.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Neonila Mashlai(xmashl00)
 * Description: The module-info.java file that specifies the module dependencies.
 */

/**
 * The module ija.project specifies the module dependencies.
 * This is a main module of the application.
 */
module ija.project {

    requires javafx.swing;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires org.json;

    exports ija.project;
    exports ija.project.game;
    exports ija.project.utils;
    exports ija.project.view;
    exports ija.project.game.common;

}